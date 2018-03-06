package cn.com.workflow.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FormValue;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.form.DefaultTaskFormHandler;
import org.flowable.engine.impl.form.FormPropertyHandler;
import org.flowable.engine.impl.form.TaskFormHandler;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityManager;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.com.workflow.common.WorkFlowContent;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActFormValueVO;
import cn.com.workflow.common.vo.ActFromVO;
import cn.com.workflow.common.vo.ActInsideFormVO;
import cn.com.workflow.common.vo.ActProcessInstanceVO;
import cn.com.workflow.common.vo.ActiveNode;
import cn.com.workflow.common.vo.SelectUserInfo;
import cn.com.workflow.common.vo.SelectUserInfos;
import cn.com.workflow.mybatis.client.TbSysSecurityRelUserPosMapper;
import cn.com.workflow.mybatis.model.TbSysSecurityRelUserPos;
import cn.com.workflow.service.ProcessService;

@Service("actTaskService")
public class ActTaskService implements cn.com.workflow.service.TaskService_ {

    private static final Logger LOGGER = LogManager.getLogger(ActStatisticsService.class);

    @Resource
    private TaskService taskService;

    @Resource
    private FormService formService;

    @Resource
    private ProcessService processService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessCustomService processCustomService;

    @Resource
    private IdentityService identityService;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private HistoryService historyService;

    @Resource
    private TbSysSecurityRelUserPosMapper tbSysSecurityRelUserPosMapper;
    
    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    @Override
    public void retrieveTask(String taskId, String userId, Map<String, Object> _variables) throws BpmException {
        if (StringUtils.isBlank(taskId) || taskId.equals("undefined")) {
            throw new BpmException("未选择任务,无法取回");
        }
        Task task = this.findTaskById(taskId);
        if (null == task) {
            throw new BpmException("任务已完成,无法取回");
        }

        List<Task> ts = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        if (ts.size() > 1) {
            throw new BpmException("并行任务无法取回");
        }

        List<HistoricTaskInstance> hts = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).orderByTaskCreateTime().desc().list();
        if (hts.size() <= 1) {
            throw new BpmException("首任务无法取回");
        }
        HistoricTaskInstance ht = hts.get(1);
        if (!userId.equals(ht.getAssignee())) {
            throw new BpmException("选择任务的前一个任务处理人与当前用户不符，无法取回");
        }

        String sourceActivity = ht.getTaskDefinitionKey();
        processCustomService.backTaskProcess(taskId, sourceActivity, null);

        List<Task> ts2 = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        if (ts.size() > 1) {
            throw new BpmException("选择任务的前一个任务为多实例任务，无法取回");
        }
        Task task2 = ts2.get(0);
        if (StringUtils.isBlank(task2.getAssignee())) {
            task2.setAssignee(userId);
        }
    }

    @Override
    public ActFromVO findTaskFormInfo(String taskId) {
        ActFromVO afv = new ActFromVO();
        afv.setTaskId(taskId);
        TaskFormData sfd = formService.getTaskFormData(taskId);
        // 如果task配置了formKey外置表单的话,优先使用
        if (StringUtils.isNotBlank(sfd.getFormKey())) {
            afv.setFromKey(sfd.getFormKey());
            return afv;
        }
        // 查看是否存在内置form表单配置
        List<FormProperty> fps = sfd.getFormProperties();
        List<ActInsideFormVO> afs = new ArrayList<>();
        List<ActFormValueVO> actions = new ArrayList<>();
        for (FormProperty fp : fps) {
            ActInsideFormVO af = new ActInsideFormVO();
            if (fp.isReadable() && fp.isWritable()) {
                af.setFiledId(fp.getId());
                af.setFiledName(fp.getName());
                af.setFiledValue(fp.getValue());
                af.setFiledType(fp.getType().getName());
                if (fp.getType().getName().equals("enum")) {
                    Map<String, Object> propertyValues = (Map<String, Object>) fp.getType().getInformation("values");
                    List<ActFormValueVO> values = new ArrayList<>();
                    for (String key : propertyValues.keySet()) {
                        ActFormValueVO actFormValueVO = new ActFormValueVO();
                        actFormValueVO.setValueId(key);
                        actFormValueVO.setValueName((String) propertyValues.get(key));
                        values.add(actFormValueVO);
                    }
                    af.setValues(values);
                }
                afs.add(af);
            }
            if (fp.getName().equals("action")) {
                FormType ft = fp.getType();
                Map<String, Object> propertyValues = (Map<String, Object>) ft.getInformation("values");
                for (String key : propertyValues.keySet()) {
                    ActFormValueVO actFormValueVO = new ActFormValueVO();
                    actFormValueVO.setValueId((String) propertyValues.get(key));
                    actFormValueVO.setValueName(getActionName((String) propertyValues.get(key)));
                    actions.add(actFormValueVO);
                }
            }

        }

        // action无定义则使用默认值
        if (actions.isEmpty()) {
            ActFormValueVO ActFormValueVO = new ActFormValueVO();
            ActFormValueVO.setValueId("complete");
            ActFormValueVO.setValueName("正常提交");
            actions.add(ActFormValueVO);
        }

        afv.setActions(actions);
        afv.setInsideFroms(afs);

        Map<String, Object> variables = this.getVariablesByTaskId(taskId);
        List<ActInsideFormVO> vs = new ArrayList<>();
        for (Map.Entry<String,Object> entry : variables.entrySet()) {

            ActInsideFormVO bf = new ActInsideFormVO();
            bf.setFiledName(entry.getKey());
            if (variables.get(entry.getKey()) instanceof String) {
                bf.setFiledValue((String)entry.getValue());
            } else {
                bf.setFiledValue(entry.getValue().toString());
            }
            vs.add(bf);
        }

        afv.setVariables(vs);

        afv.setOpinion(findCommentsForTask(taskId));

        return afv;

    }

    private String getActionName(String actionId) {
        if (actionId.equals("complete")) {
            return "正常提交";
        } else if (actionId.equals("previous")) {
            return "退回前一节点";
        } else if (actionId.equals("first")) {
            return "退回首节点";
        } else if (actionId.equals("appoint")) {
            return "退回指定节点";
        } else if (actionId.equals("cancel")) {
            return "撤销";
        } else {
            return "未定义";
        }

    }

    private String clearExceutionId(Task task) {
        String clr = ".to " + task.getName();
        LOGGER.debug("task.getExecutionId()--" + task.getExecutionId());
        LOGGER.debug("clear str--" + clr);
        LOGGER.debug("task.getExecutionId().indexOf(clr)--" + task.getExecutionId().indexOf(clr));
        if (task.getExecutionId().indexOf(clr) > -1) {
            return task.getExecutionId().substring(0, task.getExecutionId().indexOf(clr));
        } else {
            return task.getExecutionId();
        }

    }

    public SelectUserInfos findNextActivityUserInfo(String _taskId) throws BpmException {
        SelectUserInfos ss = new SelectUserInfos();
        Task task = this.findTaskById(_taskId);
        List<FlowElement> acs = processCustomService.findNextActivitys(task);
        
        for (FlowElement ac : acs) {
            UserTask userTask = (UserTask) ac;
            userTask.getFormProperties();

            SelectUserInfo sui=findUserInfoForTaskDefinition(userTask);

            ss.getSelectUserInfos().add(sui);
        }
        return ss;
    }

    public void endProcessByProcessId(String _taskId, boolean cascade) throws BpmException {
        Task task = this.findTaskById(_taskId);
        // 查询是否存在父流程
        ProcessInstance parentPi = runtimeService.createProcessInstanceQuery()
                .subProcessInstanceId(task.getProcessInstanceId()).singleResult();
        if (null != parentPi && cascade) {
            processService.cancelProcessByProcessId(parentPi.getId());
        }
        // 查询是否存在子流程
        List<ProcessInstance> subPis = runtimeService.createProcessInstanceQuery()
                .superProcessInstanceId(task.getProcessInstanceId()).list();
        for (ProcessInstance subPi : subPis) {
            processService.cancelProcessByProcessId(subPi.getId());
        }
        processCustomService.endProcess(_taskId);
    }

    /**
     * 
     * 
     * @param actName
     * @param td
     * @return
     * @author wangzhiyin 2017年11月2日 下午2:28:33
     */
    private SelectUserInfo findUserInfoForTaskDefinition(UserTask userTask) {
        String selectType = "00";// 不选人
        SelectUserInfo sui = new SelectUserInfo();
        sui.setActityId(userTask.getId());
        sui.setActityName(userTask.getName());
//        DefaultTaskFormHandler dtf = (DefaultTaskFormHandler) taskFormHandler;
        List<org.flowable.bpmn.model.FormProperty> formPropertys = userTask.getFormProperties();
        for (org.flowable.bpmn.model.FormProperty formProperty : formPropertys) {
            if (formProperty.getType().equals("enum")) {
                List<FormValue> formValues = formProperty.getFormValues();                
                if (formProperty.getName().equals("selectType")) {
                    
                    for (FormValue formValue : formValues) {
                        sui.setSelectType(formValue.getId());
                        selectType = formValue.getName();
                    }
                } else if (formProperty.getName().equals("role")) {
                    List<ActFormValueVO> roleList = new ArrayList<>();
                    for (FormValue formValue : formValues) {
                        ActFormValueVO roleValueVO = new ActFormValueVO();
                        roleValueVO.setValueId(formValue.getId());
//                        List<TbSysSecurityRelUserPos> trus = tbSysSecurityRelUserPosMapper.selectUserForPosition(formValue.getId());
                        List<ActFormValueVO> userList = new ArrayList<>();
//                        for (TbSysSecurityRelUserPos tru : trus) {
//                            ActFormValueVO userValue = new ActFormValueVO();
//                            userValue.setValueId(tru.getUserAccount());
//                            userValue.setValueName(tru.getUserName());
//                            userList.add(userValue);
//                            roleValueVO.setValueName(tru.getPositionName());
//                        }
                        
                        List<Group> groups = identityService.createGroupQuery().groupId(formValue.getId()).list();
                        for (Group group : groups) {
                            List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();  
                            for (User user : users) {
                                ActFormValueVO userValue = new ActFormValueVO();
                                userValue.setValueId(user.getId());
                                userValue.setValueName(user.getId());
                                userList.add(userValue);
                                roleValueVO.setValueName(group.getName());
                            }
                        }
                        
                        roleValueVO.setValues(userList);
                        roleList.add(roleValueVO);
                    }
                    sui.setRoles(roleList);
                }
            }
        }

        if (!selectType.equals("00")) {
            if (StringUtils.isNotBlank(userTask.getAssignee())) {
                sui.setUserVariable(userTask.getAssignee().replace("${", "").replace("}", ""));
            } else if (!CollectionUtils.isEmpty(userTask.getCandidateUsers())) {
                for (String ep : userTask.getCandidateUsers()) {
                    sui.setUserVariable(ep.replace("${", "").replace("}", ""));
                }
            } else if (!CollectionUtils.isEmpty(userTask.getCandidateGroups())) {
                for (String ep : userTask.getCandidateGroups()) {
                    sui.setUserVariable(ep.replace("${", "").replace("}", ""));
                }
            }
        }

        return sui;
    }

    private String getValueFromExpression(String expression, String taskId) {
        if (expression.trim().startsWith("$")) {
            expression = expression.replace("${", "").replace("}", "");
            LOGGER.debug("getValueFromExpression expression:{}", expression);
            Map variables = getVariablesByTaskId(taskId);
            return (String) variables.get(expression);
        } else {
            return expression;
        }
    }

    /**
     * 获取节点配置信息
     * 
     * @param _task
     * @param _tpid
     * @return
     */
    // private WfmidActivityConfigure getWfmidActivityConfigure(Task _task,
    // String _tpid,String version) {
    // return config.getActivatyMainInfo(_tpid, _task.getName(),version);
    // }

    @Override
    public void suspendTask(String _taskId, String timeout) throws BpmException {

        Task task = findTaskById(_taskId);
        processService.suspendProcess(task.getProcessInstanceId(), timeout);

    }

    @Override
    public void resumeTask(String _taskId){
        Task task = findTaskById(_taskId);
        processService.resumeProcess(task.getProcessInstanceId());

    }

    @Override
    public Map<String, Object> handleTaskById(Task task) throws BpmException {

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .singleResult();
        // TODO 获取xml配置信息
        return getTaskVariables(pi.getId());
    }

    @Override
    public Map<String, Object> handleTaskById(String taskId) throws BpmException {
        return handleTaskById(findTaskById(taskId));
    }

    @Override
    public void delegateTask(Task task, Map<String, Object> _variables) throws BpmException {
        String delegater = (String) _variables.get("delegater");
        if (null == delegater || "".equals(delegater)) {
            throw new BpmException("taskId is null");
        }
        // taskService.claim(task.getId(), delegater);
        task.setOwner(task.getAssignee());
        task.setAssignee(delegater);
        taskService.saveTask(task);

    }

    @Override
    public void delegateTask(String taskId, Map<String, Object> _variables) throws BpmException {
        delegateTask(findTaskById(taskId), _variables);

    }

    @Override
    public void submitTaskInstance(Task task, Map<String, Object> _variables) throws BpmException {
        return;
    }

    @Override
    public void takeTask(Task task, String timeout, Map<String, Object> _variables) {
        taskService.claim(task.getId(), (String) _variables.get("userId"));

    }

    @Override
    public void takeTask(String _taskId, String _userId) {
        taskService.claim(_taskId, _userId);

    }

    @Override
    public void takeTask(Task task, String timeout, String userId) {
        taskService.claim(task.getId(), userId);

    }

    @Override
    public void replaceTask(Task task, Map<String, Object> _variables) throws BpmException {
        processCustomService.backTasksProcess(task.getId(), task.getTaskDefinitionKey(), null);

    }

    @Override
    public void replaceTask(String taskId, Map<String, Object> _variables) throws BpmException {
        Task task = this.findTaskById(taskId);
        replaceTask(task, _variables);
    }

    public void gotoTask(String taskId, String desTaskDefinitionKey) throws BpmException {
        processCustomService.backTasksProcess(taskId, desTaskDefinitionKey, null);
    }

    @Override
    public void saveTask(String taskId, Map<String, Object> _variables) {
        Task task = findTaskById(taskId);
        this.addComment(task, _variables);
        taskService.setVariables(taskId, _variables);

    }

    @Override
    public String findCommentsForTask(String taskId) {
        List<Comment> comments = taskService.getTaskComments(taskId);
        if (!CollectionUtils.isEmpty(comments)) {
            return comments.get(0).getFullMessage();
        } else {
            return StringUtils.EMPTY;
        }

    }

    @Override
    public void addComment(Task task, Map<String, Object> _variables) {
        String commont = "";
        if (_variables.containsKey("opinion")) {
            commont = (String) _variables.get("opinion");
            _variables.remove("opinion");
        } else {
            return;
        }
        if (_variables.containsKey("userId") && (null != _variables.get("userId"))
                && (StringUtils.isNotBlank((String) _variables.get("userId")))) {
            identityService.setAuthenticatedUserId((String) _variables.get("userId"));
            _variables.remove("userId");
        }
        // 只保留一条批注记录
        List<Comment> comments = taskService.getTaskComments(task.getId());
        if (null != comments && !comments.isEmpty()) {
            for (Comment c : comments) {
                taskService.deleteComment(c.getId());
            }
        }
        taskService.addComment(task.getId(), task.getProcessInstanceId(), commont);
        /**
         * 
         * type意见类型可以自定义，可以用来记录意见结论，如 同意，不同意，退回等等
         * taskService.addComment(task.getId(), task.getProcessInstanceId(), new
         * String("type"),(String)_variables.get("opinion"));
         */

    }

    @Override
    public Task findTaskById(String _taskId) {
        return taskService.createTaskQuery().taskId(_taskId).singleResult();
    }

    @Override
    public void handleTaskInstance(Task task, Map<String, Object> variables) throws BpmException {
        return;
    }

    @Override
    public Object getValueFromTaskVariable(String taskId, String key) {
        return null;
    }

    @Override
    public ActProcessInstanceVO findProcessIdByTask(Task task) throws BpmException {
        return processService.findProcessByTask(task);
    }

    @Override
    public ActProcessInstanceVO findProcessIdByTask(String taskId) throws BpmException {
        return findProcessIdByTask(findTaskById(taskId));
    }

    @Override
    public String queryParentTaskById(String _taskId) throws BpmException {
        Task task = findTaskById(_taskId);
        return task.getParentTaskId();
    }

    @Override
    public void submitNomalTask(Task task, Map<String, Object> variables) {
        this.addComment(task, variables);
        this.clearVariables(variables);
        runtimeService.setVariables(task.getExecutionId(), variables);
        completeTask(task.getId(), null);
    }

    @Override
    public void submitNomalTask(String _taskId, Map<String, Object> variables) {
        Task task = findTaskById(_taskId);
        submitNomalTask(task, variables);

    }

    @Override
    public void submitByBack(String _taskId, String type, Map<String, Object> variables) throws BpmException {
        if (type.equals(WorkFlowContent.WF_TASK_SUBMIT_Type_FIRST)) {
            backToFirst(_taskId, variables);
        } else if (type.equals(WorkFlowContent.WF_TASK_SUBMIT_Type_PREVIOUS)) {

        } else if (type.equals(WorkFlowContent.WF_TASK_SUBMIT_Type_APPOINT)) {

        } else {

        }

    }

    private void backToFirst(String _taskId, Map<String, Object> variables) throws BpmException {
        Task task = findTaskById(_taskId);
        FlowElement pa = processCustomService.findFirstUserTask(task);
        processCustomService.backTasksProcess(_taskId, pa.getId(), variables);
    }

    private void clearVariables(Map<String, Object> variables) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (null == entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        if (!list.isEmpty()) {
            for (String key : list) {
                variables.remove(key);
            }
        }
    }

    @Override
    public void submitMeetingTask(Task task, String superTaskId, String mPassType, Map<String, Object> variables)
            throws BpmException {
        return;

    }

    /**
     * 
     * @param taskDefinitionKey
     * @param executionId
     * @return
     */
    public List<Task> findTasksForActivityId(String taskDefinitionKey, String executionId) {
        return taskService.createTaskQuery().executionId(executionId).taskDefinitionKey(taskDefinitionKey).list();
    }

    /**
     * 
     * @param _taskId
     * @return
     */
    private Map<String, Object> getTaskVariables(String _pId) {

        return runtimeService.getVariables(_pId);

    }

    private Map<String, Object> getVariablesByTaskId(String _taskId) {

        return taskService.getVariables(_taskId);

    }

    public InputStream readRunResource(String _taskId) throws BpmException {
        return processCustomService.createRunResource(_taskId);
    }

    /**
     * 
     * 
     * @param _taskId
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年11月2日 下午4:28:14
     */
    public List<ActiveNode> readActity(String _taskId) throws BpmException {
        List<ActiveNode> as = new ArrayList<>();
        Task task = this.findTaskById(_taskId);
        BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement ac = processCustomService.findActivityInfo(task);
        GraphicInfo gi = model.getGraphicInfo(ac.getId());
        ActiveNode an = new ActiveNode();
        an.setX(gi.getX());
        an.setY(gi.getY());
        an.setHeight(gi.getHeight());
        an.setWidth(gi.getWidth());
        an.setId(ac.getId());
        an.setName(ac.getName());
        as.add(an);

        return as;
    }

    /**
     * 
     * 
     * @param ann
     * @throws BpmException
     * @author wangzhiyin 2017年11月2日 下午4:14:12
     */
    public void readFakeActity(ActiveNode ann) throws BpmException {
        List<ActiveNode> as = new ArrayList<>();
        double x = ann.getX();
        double y = ann.getY();
        double width = ann.getWidth();
        double hight = ann.getHeight();
        String _id = ann.getId();
        double xxx;
        double yyy;
        double www;
        double hhh;
        xxx = x + width + 5;
        www = width + 24;
        if (y > 55) {
            yyy = y - 55;
        } else {
            yyy = 0;
        }
        double xx = 10;
        double yy = 10;
        Random r1 = new Random();
        int j = r1.nextInt(3) + 3;
        for (int i = 0; i <= j; i++) {

            ActiveNode an = new ActiveNode();
            an.setId(_id + "_" + i);
            an.setX(xx);
            an.setY(yy);
            an.setEnableFlag(r1.nextBoolean());
            an.setWidth(width);
            an.setHeight(hight);
            if (an.isEnableFlag()) {
                an.setName("服务【" + (i + 1) + "】已调用");
            } else {
                an.setName("服务【" + (i + 1) + "】未调用");
            }
            as.add(an);

            yy = yy + hight + 12;
        }
        hhh = yyy + yy - hight + 10;
        ann.setActiveNodes(as);
        ann.setParentX(xxx);
        ann.setParentY(yyy);
        ann.setParentH(hhh);
        ann.setParentW(www);
        ann.setParentId(ann.getId() + "_service");
    }

    public List<ActiveNode> readHisActity(String _taskId) throws BpmException {
        List<ActiveNode> as = new ArrayList<>();
        Task task = this.findTaskById(_taskId);
        BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        org.flowable.bpmn.model.Process process = model.getProcesses().get(0);
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).list();
        for (HistoricActivityInstance hai : activityInstances) {
            FlowElement pe = process.getFlowElement(hai.getActivityId());
            if (pe instanceof UserTask || pe instanceof ServiceTask) {
                GraphicInfo gi = model.getGraphicInfo(pe.getId());
                ActiveNode an = new ActiveNode();
                an.setX(gi.getX());
                an.setY(gi.getY());
                an.setHeight(gi.getHeight());
                an.setWidth(gi.getWidth());
                an.setId(pe.getId());
                an.setName(pe.getName());
                readFakeActity(an);
                as.add(an);
            }
        }

        return as;
    }

    /**
     * 
     * 
     * @param _taskId
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年11月2日 下午4:24:51
     */
    public List<ActiveNode> readAllActity(String _taskId) throws BpmException {
        List<ActiveNode> as = new ArrayList<>();
        Task task = this.findTaskById(_taskId);

        BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        org.flowable.bpmn.model.Process process = model.getProcesses().get(0);
        Collection<FlowElement> fes = process.getFlowElements();

        for (FlowElement fe : fes) {
            if (fe instanceof UserTask || fe instanceof ServiceTask) {
                GraphicInfo gi = model.getGraphicInfo(fe.getId());
                ActiveNode an = new ActiveNode();
                an.setX(gi.getX());
                an.setY(gi.getY());
                an.setHeight(gi.getHeight());
                an.setWidth(gi.getWidth());
                an.setId(fe.getId());
                an.setName(fe.getName());
                as.add(an);
            }
        }

        return as;
    }

    /**
     * 
     * @param _taskId
     * @param _outcomeName
     * @param _variables
     * @param pe
     */
    private void completeTask(String _taskId, Map<String, Object> _variables) {
        if (null != _taskId && _variables != null ) {
            taskService.complete(_taskId, _variables);
        } else if (null != _taskId && _variables == null ) {
            taskService.complete(_taskId);
        } else {
            // 抛出异常
        }
    }

    @Async
    @Override
    public void submitBatchAsync(List<String> _taskIds, Map<String, Object> _variables) throws BpmException {
        for (String taskId : _taskIds) {
            this.submitNomalTask(taskId, _variables);
        }
    }

    @Override
    public void submitBatch(List<String> _taskIds, Map<String, Object> _variables) throws BpmException {
        for (String taskId : _taskIds) {
            this.submitNomalTask(taskId, _variables);
        }
    }

    @Override
    public FlowElement findFirstUserTask(String _taskId) throws BpmException {
        return processCustomService.findFirstUserTask(this.findTaskById(_taskId));
    }

    @Override
    public FlowElement findFirstUserTask(Task task) throws BpmException {
        return processCustomService.findFirstUserTask(task);
    }

    @Override
    public List<FlowElement> findHisUserTask(Task task) throws BpmException {
        return processCustomService.findHisUserTask(task);
    }

    @Override
    public Map<String, Object> getVeriablesByTask(Task _task) {
        return getTaskVariables(_task.getExecutionId());
    }

    @Override
    public Map<String, Object> getVeriablesByTask(String taskId) {
        return getVeriablesByTask(findTaskById(taskId));
    }

    @Override
    public void cancelProcessByTaskId(String _taskId) {
        Task task = findTaskById(_taskId);
        processService.cancelProcessByProcessId(task.getProcessInstanceId());
    }

    @Override
    public void createSubTask(String _taskId) {
        Task task = findTaskById(_taskId);
        Task t = taskService.newTask("001");
        t.setParentTaskId(task.getId());
        t.setOwner(task.getAssignee());
        t.setName("加签");
        t.setAssignee(task.getAssignee());
        t.setFormKey(task.getFormKey());
        taskService.saveTask(t);
    }
}
