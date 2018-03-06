package cn.com.workflow.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowElementsContainer;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.InclusiveGateway;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.RepositoryServiceImpl;
import org.flowable.engine.impl.form.DefaultTaskFormHandler;
import org.flowable.engine.impl.form.FormPropertyHandler;
import org.flowable.engine.impl.form.TaskFormHandler;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.util.BshUtil;
import cn.com.workflow.common.vo.ActFormValueVO;
import cn.com.workflow.ext.cmd.JumpTaskCmd;

@Service("processCustomService")
public class ProcessCustomService {

    private static final Logger LOGGER = LogManager.getLogger(ProcessCustomService.class);

	private static Map<String, ProcessDefinitionEntity> processDefinitionEntitys = new HashMap<>();
	

	@Resource
	private TaskService taskService;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private RepositoryService repositoryService;

	@Resource
	private HistoryService historyService;

	@Resource
	private ProcessEngine processEngine;
	
	@Resource
	private FormService formService ;
	
	public ProcessEngine getProcessEngine(){
		return processEngine;
	}
	
	
	public void findActivityInfo(String procDefId){
		

		ProcessDefinition processDefinitionEntity = ProcessDefinitionUtil.getProcessDefinition(procDefId);
        DeploymentEntity deploymentEntity = CommandContextUtil.getProcessEngineConfiguration()
                .getDeploymentEntityManager().findById(processDefinitionEntity.getDeploymentId());
        TaskFormHandler taskFormHandler = new DefaultTaskFormHandler();
        
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement>  fes=process.getFlowElements();
		for(FlowElement fe:fes){
		    if (fe instanceof UserTask) {
			    UserTask userTask = (UserTask) fe;
			    taskFormHandler.parseConfiguration(userTask.getFormProperties(), userTask.getFormKey(), deploymentEntity, processDefinitionEntity);
				DefaultTaskFormHandler dt=(DefaultTaskFormHandler)taskFormHandler;
				List<FormPropertyHandler> fhs=dt.getFormPropertyHandlers();
				for(FormPropertyHandler fh:fhs){
					fh.getType().getName();
					Map<String, Object> propertyValues=(Map<String, Object>)fh.getType().getInformation("values");
				    for (Map.Entry<String,Object> entry : propertyValues.entrySet()) {
						ActFormValueVO actFormValueVO=new ActFormValueVO();
						actFormValueVO.setValueId(entry.getKey());
						actFormValueVO.setValueName((String)entry.getValue());
					}
				}
			}
		}
	}
	
	
	/**
	 * 查询流程首节点信息
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public FlowElement findFirstUserTask(Task task) throws BpmException{
		
		if(!isMainProcess(task.getExecutionId())){
			throw new BpmException("暂不支持分支或子流程退回操作！");
		}
		
		List<HistoricActivityInstance>  hais=historyService.createHistoricActivityInstanceQuery().executionId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
		HistoricActivityInstance ha=null;
		for(HistoricActivityInstance hai:hais){
			if(hai.getActivityType().equals("User Task")){
				ha=hai;
				break;
			}
		}
		
		if(null==ha){
			throw new BpmException("流程首节点未找到");
		}
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(ha.getProcessDefinitionId());
		
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        FlowElement fe = process.getFlowElement(ha.getActivityId());
		
		if(fe.getId().equals(task.getTaskDefinitionKey())){
			throw new BpmException("当前节点为首节点,无法退回");
		}
		
		return fe;
	}
	
	//TODO 待测试
	public boolean isMainProcess(String processId){
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
		if(null==pi){
			return false;
		}else{
			if(null!=pi.getParentId() && !"".equals(pi.getParentId())){
				return false;
			}else{
				return true;
			}
		}
	}
	
	
	
	/**
	 * 查询流程所有经过人工节点
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public List<FlowElement> findHisUserTask(Task task) throws BpmException{
		List<FlowElement> list=new ArrayList<FlowElement>();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		List<HistoricActivityInstance>  hais=historyService.createHistoricActivityInstanceQuery().executionId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
		for(HistoricActivityInstance hai:hais){
			if(hai.getActivityType().equals("User Task")){
				FlowElement fe = process.getFlowElement(hai.getActivityId());
				if(!list.contains(fe)){
					list.add(fe);
				}
			}
		}
		
		
		
		return list;
	}
	
	/**
	 * 查询当前节点的前一个节点信息
	 * 仅适用于邮储个贷节点没有回退线的画法使用
	 * @param task
	 * @return FlowElement
	 * @throws BpmException
	 */
	public FlowElement findPreviousUserTask(Task task) throws BpmException{
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        FlowElement fe = process.getFlowElement(task.getTaskDefinitionKey());
		List<FlowElement> pas=findPreviousForUserTask(fe);
		if(null==pas || pas.size()>1){
			return null;
		}
		return  pas.get(0);
	}
	
	/**
	 * 
	 * 
	 * @param activity
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年11月2日 上午10:02:44
	 */
	private List<FlowElement> findPreviousForUserTask(FlowElement activity)
			throws BpmException {
		List<FlowElement> fes = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getIncomingFlows();
		for (SequenceFlow t : ts) {
			FlowElement fe = t.getSourceFlowElement();
			LOGGER.debug("{}.getProperty(type)=={}", fe.getId(), fe.getName());
			if (fe instanceof ExclusiveGateway
					|| fe instanceof InclusiveGateway) {
			    fes.addAll(findPreviousForExclusiveGateway(fe));
			} else if (fe instanceof ParallelGateway) {
			    fes.addAll(findPreviousForParallelGateway(fe));
			} else if (fe instanceof UserTask) {
			    fes.add(fe);
			} else {
				
			}
		}

		return fes;
	}
	
	/**
	 * 
	 * 
	 * @param activity
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年11月2日 上午10:02:49
	 */
	private List<FlowElement> findPreviousForExclusiveGateway(FlowElement activity)
			throws BpmException {
		List<FlowElement> fes = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getIncomingFlows();
		for (SequenceFlow t : ts) {
				FlowElement fe = t.getSourceFlowElement();
				LOGGER.debug("pa.getProperty(type)=={}", fe.getName());
				if (fe instanceof ExclusiveGateway
						|| fe instanceof InclusiveGateway) {
				    fes.addAll(findPreviousForExclusiveGateway(fe));
				} else if (fe instanceof ParallelGateway) {
				    fes.addAll(findPreviousForParallelGateway(fe));
				} else if (fe instanceof UserTask) {
				    fes.add(fe);
				} else {
					
				}
		}

		return fes;
	}
	
	/**
	 * 
	 * 
	 * @param activity
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年11月2日 上午10:03:48
	 */
	private List<FlowElement> findPreviousForParallelGateway(FlowElement activity)
			throws BpmException {
		List<FlowElement> FlowElements = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getIncomingFlows();
		for (SequenceFlow t : ts) {
		    FlowElement fe = t.getSourceFlowElement();
			LOGGER.debug("pa.getProperty(type)=={}", fe.getName());
			if (fe instanceof ExclusiveGateway
					|| fe instanceof InclusiveGateway) {
				FlowElements.addAll(findPreviousForExclusiveGateway(fe));
			} else if (fe instanceof ParallelGateway) {
				FlowElements.addAll(findPreviousForParallelGateway(fe));
			} else if (fe instanceof UserTask) {
				FlowElements.add(fe);
			} else {
				
			}
		}

		return FlowElements;
	}
	
	

	/**
	 * 驳回流程
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            驳回节点ID
	 * @param variables
	 *            流程存储参数
	 * @throws BpmException
	 */
	public void backTasksProcess(String taskId, String activityId, Map<String, Object> variables) throws BpmException {
		if (StringUtils.isEmpty(activityId)) {
			throw new BpmException("目标节点ID为空！");
		}

		// 查找所有并行任务节点，同时驳回
		List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(),
				findTaskById(taskId).getTaskDefinitionKey());
		for (Task task : taskList) {
			commitProcess(task.getId(), variables, activityId);
		}
	}
	
	/**
	 * 驳回流程
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            驳回节点ID
	 * @param variables
	 *            流程存储参数
	 * @throws BpmException
	 */
	public void backTaskProcess(String taskId, String activityId, Map<String, Object> variables) throws BpmException {
		if (StringUtils.isEmpty(activityId)) {
			throw new BpmException("目标节点ID为空！");
		}
		commitProcess(taskId, variables, activityId);
	}

	/**
	 * 取回流程
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            取回节点ID
	 * @throws BpmException
	 */
	public void callBackProcess(String taskId, String activityId) throws BpmException {
		if (StringUtils.isEmpty(activityId)) {
			throw new BpmException("目标节点ID为空！");
		}

		// 查找所有并行任务节点，同时取回
		List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(),
				findTaskById(taskId).getTaskDefinitionKey());
		for (Task task : taskList) {
			commitProcess(task.getId(), null, activityId);
		}
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param FlowElement
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<SequenceFlow> clearTransition(FlowElement flowElement) {
		// 存储当前节点所有流向临时变量
		List<SequenceFlow> oriSequenceFlowList = new ArrayList<>();
		FlowNode fn = (FlowNode)flowElement;
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<SequenceFlow> SequenceFlowList = fn.getOutgoingFlows();
		for (SequenceFlow SequenceFlow : SequenceFlowList) {
			oriSequenceFlowList.add(SequenceFlow);
		}
		SequenceFlowList.clear();

		return oriSequenceFlowList;
	}

	/**
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws BpmException
	 */
	private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws BpmException {
		if (variables != null) {
		    saveVeriableForTaskId(taskId, variables);
		}
		// 跳转节点为空，默认提交操作
		if (StringUtils.isEmpty(activityId)) {
			taskService.complete(taskId);
		} else {// 流程转向操作
		    jump(taskId, activityId);
		}
	}
	
	/**
	 * 
	 * 
	 * @param taskId
	 * @param variables 
	 * @author wangzhiyin
	 *	       2017年11月2日 上午11:55:07
	 */
	private void saveVeriableForTaskId(String taskId, Map<String, Object> variables) {
        TaskEntityManager taskEntityManager = org.flowable.task.service.impl.util.CommandContextUtil.getTaskEntityManager();
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        taskEntity.setVariables(variables);
	}
	
	/**
	 * 任务自动跳转节点
	 *  
	 * @author wangzhiyin
	 *	       2017年11月2日 上午11:47:09
	 */
	public void jump(String taskId, String activityId) {
	    ManagementService managementService = processEngine.getManagementService();
	    managementService.executeCommand(new JumpTaskCmd(taskId,activityId));
	}
	/**
	 * 
	 * 
	 * @param taskId
	 * @param variables 
	 * @author wangzhiyin
	 *	       2017年11月2日 上午11:56:21
	 */
    private void saveVeriableForExcutionId(String executionId, Map<String, Object> variables) {
        runtimeService.setVariables(executionId, variables);
    }
	
	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void endProcess(String taskId) throws BpmException {
//		FlowElement endActivity = findActivitiImpl(taskId, "end");
//		commitProcess(taskId, null, endActivity.getId());
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.deleteProcessInstance(task.getExecutionId(), "abortByUser");
		
	}
	
   /**
     * 中止流程(特权人直接审批通过等)
     * 
     * @param taskId
     */
    public void endProcessById(String processId) throws BpmException {
        runtimeService.deleteProcessInstance(processId, "abortByUser");
    }
    
	/**
	 * 根据流入任务集合，查询最近一次的流入任务节点
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param tempList
	 *            流入任务集合
	 * @return
	 */
	private FlowElement filterNewestActivity(ProcessInstance processInstance, List<FlowElement> fes) {
		while (!fes.isEmpty()) {
		    FlowElement activity_1 = fes.get(0);
			HistoricActivityInstance activityInstance_1 = findHistoricUserTask(processInstance, activity_1.getId());
			if (activityInstance_1 == null) {
				fes.remove(activity_1);
				continue;
			}

			if (fes.size() > 1) {
			    FlowElement activity_2 = fes.get(1);
				HistoricActivityInstance activityInstance_2 = findHistoricUserTask(processInstance, activity_2.getId());
				if (activityInstance_2 == null) {
					fes.remove(activity_2);
					continue;
				}

				if (activityInstance_1.getEndTime().before(activityInstance_2.getEndTime())) {
				    fes.remove(activity_1);
				} else {
				    fes.remove(activity_2);
				}
			} else {
				break;
			}
		}
		if (!fes.isEmpty()) {
			return fes.get(0);
		}
		return null;
	}

	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 * @throws BpmException
	 */
	private FlowElement findActivitiImpl(String taskId, String activityId) throws BpmException {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
        
		return findActivitiImplForProcess(processDefinition, activityId);
	}
	
	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 * @throws BpmException
	 */
	private FlowElement findActivitiImplForProcess(ProcessDefinitionEntity processDefinition, String activityId) throws BpmException {
		// 获取当前活动节点ID
		if (StringUtils.isEmpty(activityId)) {
			return null;
		}
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.equalsIgnoreCase("END")) {
		    Collection<FlowElement> fes = process.getFlowElements();
			for (FlowElement FlowElement : fes) {
			    FlowNode fn = (FlowNode)FlowElement;
				List<SequenceFlow> SequenceFlowList = fn.getOutgoingFlows();
				if (SequenceFlowList.isEmpty()) {
					return FlowElement;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		FlowElement FlowElement = process.getFlowElement(activityId);

		return FlowElement;
	}

	/**
	 * 根据当前任务ID，查询可以驳回的任务节点
	 * 
	 * @param taskId
	 *            当前任务ID
	 */
	public List<FlowElement> findBackAvtivity(String taskId) throws BpmException {
		List<FlowElement> rtnList = iteratorBackActivity(taskId, findActivitiImpl(taskId, null),
				new ArrayList<FlowElement>(), new ArrayList<FlowElement>());
		return reverList(rtnList);
	}

	/**
	 * 查询指定任务节点的最新记录
	 * 
	 * @param processInstance
	 *            流程实例
	 * @param activityId
	 * @return
	 */
	private HistoricActivityInstance findHistoricUserTask(ProcessInstance processInstance, String activityId) {
		HistoricActivityInstance rtnVal = null;
		// 查询当前流程实例审批结束的历史节点
		List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
				.activityType("User Task").processInstanceId(processInstance.getId()).activityId(activityId).finished()
				.orderByHistoricActivityInstanceEndTime().desc().list();
		if (!historicActivityInstances.isEmpty()) {
			rtnVal = historicActivityInstances.get(0);
		}

		return rtnVal;
	}

	/**
	 * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
	 * 
	 * @param FlowElement
	 *            当前节点
	 * @return
	 */
	private String findParallelGatewayId(FlowElement FlowElement) {
	    FlowNode fn = (FlowNode)FlowElement;
		List<SequenceFlow> incomingTransitions = fn.getOutgoingFlows();
		for (SequenceFlow sequenceFlow : incomingTransitions) {
			FlowElement fe = sequenceFlow.getTargetFlowElement();
			String type = fe.getAttributeValue(null,"type");
			if ("Parallel Gateway".equals(type)) {// 并行路线
				String gatewayId = FlowElement.getId();
				String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
				if (gatewayType.equalsIgnoreCase("END")) {
					return gatewayId.substring(0, gatewayId.lastIndexOf("_")) + "_start";
				}
			}
		}
		return null;
	}

	/**
	 * 根据任务ID获取流程定义
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws BpmException
	 */
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws BpmException {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

		if (processDefinition == null) {
			throw new BpmException("流程定义未找到!");
		}

		return processDefinition;
	}

	/**
	 * 根据任务ID获取对应的流程实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws BpmException
	 */
	public ProcessInstance findProcessInstanceByTaskId(String taskId) throws BpmException {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
		if (processInstance == null) {
			throw new BpmException("流程实例未找到!");
		}
		return processInstance;
	}

	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws BpmException
	 */
	private TaskEntity findTaskById(String taskId) throws BpmException {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new BpmException("任务实例未找到!");
		}
		return task;
	}

	/**
	 * 根据流程实例ID和任务key值查询所有同级任务集合
	 * 
	 * @param processInstanceId
	 * @param key
	 * @return
	 */
	private List<Task> findTaskListByKey(String processInstanceId, String key) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();
	}

	/**
	 * 迭代循环流程树结构，查询当前节点可驳回的任务节点
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param currActivity
	 *            当前活动节点
	 * @param rtnList
	 *            存储回退节点集合
	 * @param tempList
	 *            临时存储节点集合（存储一次迭代过程中的同级userTask节点）
	 * @return 回退节点集合
	 */
	private List<FlowElement> iteratorBackActivity(String taskId, FlowElement currActivity,
			List<FlowElement> rtnList, List<FlowElement> tempList) throws BpmException {
		// 查询流程定义，生成流程树结构
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
		FlowNode fn = (FlowNode)currActivity;
		// 当前节点的流入来源
		List<SequenceFlow> incomingTransitions = fn.getIncomingFlows();
		// 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
		List<FlowElement> exclusiveGateways = new ArrayList<>();
		// 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
		List<FlowElement> parallelGateways = new ArrayList<>();
		// 遍历当前节点所有流入路径
		for (SequenceFlow sequenceFlow : incomingTransitions) {
			FlowElement FlowElement = sequenceFlow.getSourceFlowElement();
			String type = (String) FlowElement.getAttributeValue(null,"type");
			/**
			 * 并行节点配置要求：<br>
			 * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
			 */
			if ("Parallel Gateway".equals(type)) {// 并行路线
				String gatewayId = FlowElement.getId();
				String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
				if (gatewayType.equalsIgnoreCase("START")) {// 并行起点，停止递归
					return rtnList;
				} else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
					parallelGateways.add(FlowElement);
				}
			} else if ("startEvent".equals(type)) {// 开始节点，停止递归
				return rtnList;
			} else if ("User Task".equals(type)) {// 用户任务
				tempList.add(FlowElement);
			} else if ("Exclusive Gateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
				currActivity = sequenceFlow.getSourceFlowElement();
				exclusiveGateways.add(currActivity);
			}
		}

		/**
		 * 迭代条件分支集合，查询对应的userTask节点
		 */
		for (FlowElement FlowElement : exclusiveGateways) {
			iteratorBackActivity(taskId, FlowElement, rtnList, tempList);
		}

		/**
		 * 迭代并行集合，查询对应的userTask节点
		 */
		for (FlowElement FlowElement : parallelGateways) {
			iteratorBackActivity(taskId, FlowElement, rtnList, tempList);
		}

		/**
		 * 根据同级userTask集合，过滤最近发生的节点
		 */
		currActivity = filterNewestActivity(processInstance, tempList);
		if (currActivity != null) {
			// 查询当前节点的流向是否为并行终点，并获取并行起点ID
			String id = findParallelGatewayId(currActivity);
			if (StringUtils.isEmpty(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
				rtnList.add(currActivity);
			} else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
				currActivity = findActivitiImpl(taskId, id);
			}

			// 清空本次迭代临时集合
			tempList.clear();
			// 执行下次迭代
			iteratorBackActivity(taskId, currActivity, rtnList, tempList);
		}
		return rtnList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param FlowElement
	 *            活动节点
	 * @param oriSequenceFlowList
	 *            原有节点流向集合
	 */
	private void restoreTransition(FlowElement flowElement, List<SequenceFlow> oriSequenceFlowList) {
		// 清空现有流向
	    FlowNode fn = (FlowNode)flowElement;
		List<SequenceFlow> SequenceFlowList = fn.getOutgoingFlows();
		SequenceFlowList.clear();
		// 还原以前流向
		for (SequenceFlow SequenceFlow : oriSequenceFlowList) {
			SequenceFlowList.add(SequenceFlow);
		}
	}

	/**
	 * 反向排序list集合，便于驳回节点按顺序显示
	 * 
	 * @param list
	 * @return
	 */
	private List<FlowElement> reverList(List<FlowElement> list) {
		List<FlowElement> rtnList = new ArrayList<>();
		// 由于迭代出现重复数据，排除重复
		for (int i = list.size(); i > 0; i--) {
			if (!rtnList.contains(list.get(i - 1)))
				rtnList.add(list.get(i - 1));
		}
		return rtnList;
	}

	/**
	 * 转办流程
	 * 
	 * @param taskId
	 *            当前任务节点ID
	 * @param userCode
	 *            被转办人Code
	 */
	public void transferAssignee(String taskId, String userCode) {
		taskService.setAssignee(taskId, userCode);
	}

	/**
	 * 
	 * 
	 * @param _taskId
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年11月2日 下午1:46:22
	 */
	public List<FlowElement> findNextActivitys(String _taskId) throws BpmException {
		Task task = this.findTaskById(_taskId);
//		ProcessInstance pi = findProcessInstanceByTaskId(_taskId);
		return findNextActivitys(task);

	}
	
	/**
	 * 
	 * 
	 * @param task
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年11月2日 下午1:46:27
	 */
	public List<FlowElement> findNextActivitys(Task task) throws BpmException {

		Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
		
        FlowElement ac = findActivityInfo(task);
		return findNextForUserTask(ac, variables);

	}
//	/**
//	 * 
//	 * 
//	 * @param task
//	 * @param execution
//	 * @return
//	 * @throws BpmException 
//	 * @author wangzhiyin
//	 *	       2017年11月7日 下午6:42:30
//	 */
//	public List<FlowElement> findNextActivitys(Task task, DelegateExecution execution) throws BpmException {
//
//        Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
//        
//        FlowElement ac = findActivityInfo(task);
//        return findNextForUserTask(ac, variables);
//
//    }
	

	public FlowElement findActivityInfo(Task task) throws BpmException {

		ProcessDefinitionEntity pde = null;
		if (processDefinitionEntitys.containsKey(task.getProcessDefinitionId())) {
			pde = processDefinitionEntitys.get(task.getProcessDefinitionId());
		} else {
			pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
			processDefinitionEntitys.put(task.getProcessDefinitionId(), pde);
		}
		BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        return process.getFlowElement(task.getTaskDefinitionKey());

	}

	/**
	 * 查询当前节点为ExclusiveGateway的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<FlowElement> findNextForExclusiveGateway(FlowElement activity, Map<String, Object> variables)
			throws BpmException {
		List<FlowElement> flowElements = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getOutgoingFlows();
		for (SequenceFlow t : ts) {
		    String condition = t.getConditionExpression();
			if(null==condition){
				FlowElement fe = t.getTargetFlowElement();
				if (fe instanceof ExclusiveGateway
						|| fe instanceof InclusiveGateway) {
				    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
				} else if (fe instanceof ParallelGateway) {
				    flowElements.addAll(findNextForParallelGateway(fe, variables));
				} else if (fe instanceof UserTask) {
				    flowElements.add(fe);
				} else {
				    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
				}
			}else if (BshUtil.eval(condition, variables)) {
				FlowElement fe = t.getTargetFlowElement();
				LOGGER.debug("pa.getProperty(type)=={}", fe.getName());
				if (fe instanceof ExclusiveGateway
						|| fe instanceof InclusiveGateway) {
				    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
				} else if (fe instanceof ParallelGateway) {
				    flowElements.addAll(findNextForParallelGateway(fe, variables));
				} else if (fe instanceof UserTask) {
				    flowElements.add(fe);
				} else {
				    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
				}
			}
		}

		return flowElements;
	}
	
	

	/**
	 * 查询当前节点为UserTask的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<FlowElement> findNextForUserTask(FlowElement activity, Map<String, Object> variables)
			throws BpmException {
		List<FlowElement> flowElements = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getOutgoingFlows();
		for (SequenceFlow t : ts) {
			FlowElement fe = t.getTargetFlowElement();
			LOGGER.debug("{}.type=={}", fe.getId(), fe.getName());
			if (fe instanceof ExclusiveGateway
					|| fe instanceof InclusiveGateway) {
			    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
			} else if (fe instanceof ParallelGateway) {
			    flowElements.addAll(findNextForParallelGateway(fe, variables));
			} else if (fe instanceof UserTask) {
			    flowElements.add(fe);
			} else {
			    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
			}
		}

		return flowElements;
	}

	/**
	 * 查询当前节点为ParallelGateway的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<FlowElement> findNextForParallelGateway(FlowElement activity, Map<String, Object> variables)
			throws BpmException {
		List<FlowElement> flowElements = new ArrayList<>();
		FlowNode fn = (FlowNode)activity;
		List<SequenceFlow> ts = fn.getOutgoingFlows();
		for (SequenceFlow t : ts) {
			FlowElement fe = t.getTargetFlowElement();
			LOGGER.debug("pa.getProperty(type)=={}", fe.getName());
			if (fe instanceof ExclusiveGateway
					|| fe instanceof InclusiveGateway) {
			} else if (fe instanceof ParallelGateway) {
			    flowElements.addAll(findNextForParallelGateway(fe, variables));
			} else if (fe instanceof UserTask) {
			    flowElements.add(fe);
			} else {
			    flowElements.addAll(findNextForExclusiveGateway(fe, variables));
			}
		}

		return flowElements;
	}

	public InputStream createRunResource(String _taskId) throws BpmException {
		try {
			Task task = this.findTaskById(_taskId);
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();

			if (historicProcessInstance == null) {
				throw new RuntimeException("获取流程图异常!");
			} else {

				List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc()
						.list();// 获取流程走过的节点，并按照节点生成先后顺序排序
				List<String> activitiIds = new ArrayList<>();
				List<String> flowIds = new ArrayList<>();
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
				List<String> cls=clearErrorNode(processDefinition,activityInstances);
				flowIds = getHighLightedFlows(processDefinition, activityInstances,cls);
				for (HistoricActivityInstance hai : activityInstances) {
					if(!(hai.getActivityType().indexOf("Gateway")>0)){
						activitiIds.add(hai.getActivityId());
					}
				}

				return new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitiIds,
						flowIds, processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getLabelFontName(),null, null, 1.0);
			}
		} catch (Exception e) {
			LOGGER.error("获取流程图异常!", e);
			throw new BpmException("获取流程图异常!");
		}
	}

	/**
	 * 
	 * 
	 * @param processDefinitionEntity
	 * @param historicActivityInstances
	 * @return 
	 * @author wangzhiyin
	 *	       2017年11月2日 下午1:55:57
	 */
	private List<String> clearErrorNode(ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances){
		List<String> cls=new ArrayList<>();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionEntity.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
		for(HistoricActivityInstance hi:historicActivityInstances){
			if("boundaryError".equals(hi.getActivityType()) ||"boundarytimer".equals(hi.getActivityType())){
				FlowElement flowElement =process.getFlowElement(hi.getActivityId());
				FlowNode fn = (FlowNode)flowElement;
				FlowElementsContainer flowElementsContainer=fn.getParentContainer();
				if(null!=flowElementsContainer){
				    for(FlowElement element: flowElementsContainer.getFlowElements()) {
				        cls.add(element.getId());
				    }
				}
			}
		}
		
		return cls;
	}
	

	/**
	 * 获取高亮显示的线的id
	 * 
	 * @param processDefinitionEntity
	 * @param historicActivityInstances
	 * @return
	 */
	public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances,List<String> cls) {
		List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionEntity.getId());
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			FlowElement FlowElement = process.getFlowElement(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<FlowElement> sameStartTimeNodes = new ArrayList<>();
			FlowElement sameFlowElement1 = process.getFlowElement(historicActivityInstances.get(i + 1).getActivityId());// 得到节点定义的详细信息
				sameStartTimeNodes.add(sameFlowElement1);
            for (int j = i; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance FlowElement1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance FlowElement2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (FlowElement1.getStartTime().equals(FlowElement2.getStartTime())
                        || (null != historicActivityInstances.get(i).getEndTime()
                                && historicActivityInstances.get(i).getEndTime().equals(FlowElement2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowElement sameFlowElement2 = process.getFlowElement(FlowElement2.getActivityId());
                    sameStartTimeNodes.add(sameFlowElement2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
			if(!cls.contains(FlowElement.getId())){
			    FlowNode fn = (FlowNode)FlowElement;
				List<SequenceFlow> SequenceFlows = fn.getOutgoingFlows();// 取出节点的所有出去的线
	
				for (SequenceFlow sequenceFlow : SequenceFlows) {// 对所有的线进行遍历
	
					FlowElement FlowElementImpl = sequenceFlow.getTargetFlowElement();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
					if (sameStartTimeNodes.contains(FlowElementImpl)) {
						highFlows.add(sequenceFlow.getId());
					}
				}
			}

		}
		return highFlows;

	}
	
	/**
	 * 
	 * 
	 * @param in
	 * @param parseLevel
	 * @return 
	 * @author wangzhiyin
	 *	       2017年11月2日 下午2:00:37
	 */
	public Map<String, byte[]> unZip(InputStream in){ 
		if(in == null)   
            return null;  
          
        ZipEntry zipEntry = null;  
        FileOutputStream out = null;  
        Map<String, byte[]> map = new HashMap<>();  
        ZipInputStream zipIn = new ZipInputStream(in);  
        try{  
            while ((zipEntry = zipIn.getNextEntry()) != null) {  
                //如果是文件夹路径方式，本方法内暂时不提供操作  
                if (zipEntry.isDirectory()) {  
                }   
                else {  
                    //如果是文件，则直接存放在Map中  
                    String name = zipEntry.getName();  
                    //把压缩文件内的流转化为字节数组，够外部逻辑使用(之后关闭流)  
                    byte[] bt = IOUtils.toByteArray(zipIn);  
                    map.put(name,bt);  
                }  
            }  
            return map;  
        } catch(Exception ex){  
        	LOGGER.error("in unZip(InputStream in,int parseLevel) has an error,e is " + ex);  
            return null;  
        }  
        finally{  
            IOUtils.closeQuietly(zipIn);  
            IOUtils.closeQuietly(in);  
            IOUtils.closeQuietly(out);  
        }  
		
	}
	
	
	
	public byte[] getAllTemplateStream(String absoultePath,String fileName) throws BpmException   {
		byte[] buffer = null;
		
		List<ProcessDefinition> processDefinitions=repositoryService.createProcessDefinitionQuery().active().latestVersion().orderByProcessDefinitionKey().asc().list();
		 String zipFilePath = absoultePath + File.separator + fileName;  
         File zipFile = new File(zipFilePath);             
         if (zipFile.exists())  
         {  
             //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException  
             SecurityManager securityManager = new SecurityManager();  
             securityManager.checkDelete(zipFilePath);  
             //删除已存在的目标文件  
             zipFile.delete();                 
         } 
		
		ZipOutputStream zos=null;
		try {
			zos = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()));
			zip(zos, processDefinitions,absoultePath);
			zos.flush(); 
		} catch (FileNotFoundException e) {
			throw new BpmException("打包下载异常！",e);
		} catch (IOException e) {
			throw new BpmException(e);
		} finally {
			if(null != zos){
				try {
                    zos.close();
                } catch (IOException e) {
                    throw new BpmException("打包下载异常！",e);
                }
			}
		}
		FileInputStream fis =null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);  
		try{
			fis = new FileInputStream(zipFile);
	        byte[] b = new byte[1024];  
	        int n;  
	        while ((n = fis.read(b)) != -1) {  
	            bos.write(b, 0, n);  
	        }  
	        buffer = bos.toByteArray();  
		} catch (FileNotFoundException e) {
			throw new BpmException("打包下载异常！",e);
		} catch (Exception e) {
			throw new BpmException("打包下载异常！",e);
		} finally {
			try {
		        fis.close();  
		        bos.close(); 
			} catch (IOException e) {
				throw new BpmException("打包下载异常！",e);
			} 
		}
		
		return buffer;
	}
	

	
	private  void zip( ZipOutputStream zos,List<ProcessDefinition> processDefinitions,String absoultePath) throws BpmException  
    {  
//            int count, bufferLen = 1024;  
//            byte data[] = new byte[bufferLen];  
            for(ProcessDefinition pd:processDefinitions){
//    			String name=pd.getName();
//    			String key=pd.getKey();
    			String resourceName=pd.getResourceName();
//    			int version=pd.getVersion();
    			InputStream inputStream = repositoryService.getProcessModel(pd.getId());
	            ZipEntry entry = new ZipEntry(resourceName);  
	            try {
					zos.putNextEntry(entry);
		            byte[] buff = new byte[1024];
		            int len = 0;
					while ((len = inputStream.read(buff)) != -1) {
						zos.write(buff, 0, len);
					}
	            } catch (IOException e) {
	            	throw new BpmException("打包下载异常！",e);
				}finally {
					try {
						inputStream.close();
						zos.closeEntry();
					} catch (IOException e) {
					    LOGGER.error("打包下载异常:" + e.getMessage(),e);
					}
					 
				}

				
            }
            
    }  
	
    private void writeFile(String id, String fileName) throws IOException {
        InputStream inputStream = repositoryService.getProcessModel(id);

        int byteCount = 1024;
        int bufferLen = 1024;
        OutputStream outputStream = new FileOutputStream(fileName);
        try {

            byte[] bytes = new byte[bufferLen];
            while ((byteCount = inputStream.read(bytes, 0, bufferLen)) != -1) {
                outputStream.write(bytes, 0, byteCount);
            }

        } finally {
            inputStream.close();
            outputStream.close();
        }

    }
	
}
