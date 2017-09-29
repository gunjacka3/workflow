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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.util.BshUtil;
import cn.com.workflow.common.vo.ActFormValueVO;

@Service("processCustomService")
public class ProcessCustomService {

    private static final Logger LOGGER = LogManager.getLogger(ProcessCustomService.class);

	private static Map<String, ProcessDefinitionEntity> processDefinitionEntitys = new HashMap<String, ProcessDefinitionEntity>();
	

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
	
	public ProcessEngine getProcessEngine(){
		return processEngine;
	}
	
	
	public void findActivityInfo(String procDefId){
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(procDefId);
		List<ActivityImpl> as=processDefinition.getActivities();
		for(ActivityImpl a:as){
			PvmActivity pa=(PvmActivity)a;
			if("userTask".equals(pa.getProperty("type"))){
				TaskDefinition tdf=(TaskDefinition)pa.getProperty("taskDefinition");
				DefaultTaskFormHandler dt=(DefaultTaskFormHandler)tdf.getTaskFormHandler();
				List<FormPropertyHandler> fhs=dt.getFormPropertyHandlers();
				for(FormPropertyHandler fh:fhs){
					fh.getType().getName();
					Map<String, Object> propertyValues=(Map<String, Object>)fh.getType().getInformation("values");
					for(String key:propertyValues.keySet()){
						ActFormValueVO ActFormValueVO=new ActFormValueVO();
						ActFormValueVO.setValueId(key);
						ActFormValueVO.setValueName((String)propertyValues.get(key));
					}
				}
				pa=null;
			}
		}
	}
	
	
	/**
	 * 查询流程首节点信息
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public PvmActivity findFirstUserTask(Task task) throws BpmException{
		
		if(!isMainProcess(task.getExecutionId())){
			throw new BpmException("暂不支持分支或子流程退回操作！");
		}
		
		List<HistoricActivityInstance>  hais=historyService.createHistoricActivityInstanceQuery().executionId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
		HistoricActivityInstance ha=null;
		for(HistoricActivityInstance hai:hais){
			if(hai.getActivityType().equals("userTask")){
				ha=hai;
				break;
			}
		}
		
		if(null==ha){
			throw new BpmException("流程首节点未找到");
		}
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(ha.getProcessDefinitionId());
		
		PvmActivity activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(ha.getActivityId());
		
		if(activityImpl.getId().equals(task.getTaskDefinitionKey())){
			throw new BpmException("当前节点为首节点,无法退回");
		}
		
		return activityImpl;
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
	public List<PvmActivity> findHisUserTask(Task task) throws BpmException{
		List<PvmActivity> list=new ArrayList<PvmActivity>();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		List<HistoricActivityInstance>  hais=historyService.createHistoricActivityInstanceQuery().executionId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
		for(HistoricActivityInstance hai:hais){
			if(hai.getActivityType().equals("userTask")){
				PvmActivity activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(hai.getActivityId());
				if(!list.contains(activityImpl)){
					list.add(activityImpl);
				}
			}
		}
		
		
		
		return list;
	}
	
	/**
	 * 查询当前节点的前一个节点信息
	 * 仅适用于邮储个贷节点没有回退线的画法使用
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public PvmActivity findPreviousUserTask(Task task) throws BpmException{
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		
		PvmActivity activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(task.getTaskDefinitionKey());
		List<PvmActivity> pas=findPreviousForUserTask(activityImpl);
		if(null==pas || pas.size()>1){
			return null;
		}
		
		
		
//		List<HistoricActivityInstance>  hais=historyService.createHistoricActivityInstanceQuery().activityType("userTask").executionId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().desc().list();
//		if(null==hais ||hais.size()<2){
//			return null;
//		}
//		for(int i=1;i<hais.size();i++){
//			if(!hai.getActivityId().equals(task.getTaskDefinitionKey())){
//				PvmActivity activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(hai.getActivityId());
//				
//			}
//		}
		
		
		
		return  null;
	}
	
	
	private List<PvmActivity> findPreviousForUserTask(PvmActivity activity)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getIncomingTransitions();
		for (PvmTransition t : ts) {
			PvmActivity pa = t.getSource();
			LOGGER.debug("{}.getProperty(type)=={}", pa.getId(), pa.getProperty("type"));
			if ("exclusiveGateway".equals(pa.getProperty("type"))
					|| "inclusiveGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findPreviousForExclusiveGateway(pa));
			} else if ("parallelGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findPreviousForParallelGateway(pa));
			} else if ("userTask".equals(pa.getProperty("type"))) {
				activityImpls.add(pa);
			} else {
				
			}
		}

		return activityImpls;
	}
	
	private List<PvmActivity> findPreviousForExclusiveGateway(PvmActivity activity)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getIncomingTransitions();
		for (PvmTransition t : ts) {
				PvmActivity pa = t.getSource();
				LOGGER.debug("pa.getProperty(type)=={}", pa.getProperty("type"));
				if ("exclusiveGateway".equals(pa.getProperty("type"))
						|| "inclusiveGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findPreviousForExclusiveGateway(pa));
				} else if ("parallelGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findPreviousForParallelGateway(pa));
				} else if ("userTask".equals(pa.getProperty("type"))) {
					activityImpls.add(pa);
				} else {
					
				}
		}

		return activityImpls;
	}
	
	private List<PvmActivity> findPreviousForParallelGateway(PvmActivity activity)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getIncomingTransitions();
		for (PvmTransition t : ts) {
			PvmActivity pa = t.getSource();
			LOGGER.debug("pa.getProperty(type)=={}", pa.getProperty("type"));
			if ("exclusiveGateway".equals(pa.getProperty("type"))
					|| "inclusiveGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findPreviousForExclusiveGateway(pa));
			} else if ("parallelGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findPreviousForParallelGateway(pa));
			} else if ("userTask".equals(pa.getProperty("type"))) {
				activityImpls.add(pa);
			} else {
				
			}
		}

		return activityImpls;
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
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
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
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (StringUtils.isEmpty(activityId)) {
			taskService.complete(taskId, variables);
		} else {// 流程转向操作
			turnTransitionForUTask(taskId, activityId, variables);
		}
	}
	
	/**
	 * @param processId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws BpmException
	 */
	private void commitProcessForProcess(ProcessDefinitionEntity processDefinition,ProcessInstance process, Map<String, Object> variables, ActivityImpl activity) throws BpmException {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		turnTransitionForProcess(processDefinition,process, activity, variables);
	}

	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void endProcess(String taskId) throws BpmException {
//		ActivityImpl endActivity = findActivitiImpl(taskId, "end");
//		commitProcess(taskId, null, endActivity.getId());
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.deleteProcessInstance(task.getExecutionId(), "abortByUser");
		
	}
	
	/**
	 * 中止流程(特权人直接审批通过等)
	 * 
	 * @param taskId
	 */
	public void endProcessByPid(String processId,boolean cascade) throws BpmException {
		
		// 取得流程定义
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
		if(null==pi){
			return ;
		}
		
		//递归中止当前流程下所有子流程
		List<ProcessInstance> pis=runtimeService.createProcessInstanceQuery().superProcessInstanceId(processId).list();
		for(ProcessInstance pii :pis){
			endProcessByPid(pii.getId(),cascade);
		}
		
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(pi.getProcessDefinitionId());
		
		ActivityImpl endActivity = findActivitiImplForProcess(processDefinition, "end");
		commitProcessForProcess(processDefinition,pi, null, endActivity);
		
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
	private ActivityImpl filterNewestActivity(ProcessInstance processInstance, List<ActivityImpl> tempList) {
		while (tempList.size() > 0) {
			ActivityImpl activity_1 = tempList.get(0);
			HistoricActivityInstance activityInstance_1 = findHistoricUserTask(processInstance, activity_1.getId());
			if (activityInstance_1 == null) {
				tempList.remove(activity_1);
				continue;
			}

			if (tempList.size() > 1) {
				ActivityImpl activity_2 = tempList.get(1);
				HistoricActivityInstance activityInstance_2 = findHistoricUserTask(processInstance, activity_2.getId());
				if (activityInstance_2 == null) {
					tempList.remove(activity_2);
					continue;
				}

				if (activityInstance_1.getEndTime().before(activityInstance_2.getEndTime())) {
					tempList.remove(activity_1);
				} else {
					tempList.remove(activity_2);
				}
			} else {
				break;
			}
		}
		if (tempList.size() > 0) {
			return tempList.get(0);
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
	private ActivityImpl findActivitiImpl(String taskId, String activityId) throws BpmException {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (StringUtils.isEmpty(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

		return activityImpl;
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
	private ActivityImpl findActivitiImplForProcess(ProcessDefinitionEntity processDefinition, String activityId) throws BpmException {
		// 获取当前活动节点ID
		if (StringUtils.isEmpty(activityId)) {
			return null;
		}
		
		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

		return activityImpl;
	}

	/**
	 * 根据当前任务ID，查询可以驳回的任务节点
	 * 
	 * @param taskId
	 *            当前任务ID
	 */
	public List<ActivityImpl> findBackAvtivity(String taskId) throws BpmException {
		List<ActivityImpl> rtnList = iteratorBackActivity(taskId, findActivitiImpl(taskId, null),
				new ArrayList<ActivityImpl>(), new ArrayList<ActivityImpl>());
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
				.activityType("userTask").processInstanceId(processInstance.getId()).activityId(activityId).finished()
				.orderByHistoricActivityInstanceEndTime().desc().list();
		if (historicActivityInstances.size() > 0) {
			rtnVal = historicActivityInstances.get(0);
		}

		return rtnVal;
	}

	/**
	 * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
	 * 
	 * @param activityImpl
	 *            当前节点
	 * @return
	 */
	private String findParallelGatewayId(ActivityImpl activityImpl) {
		List<PvmTransition> incomingTransitions = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			activityImpl = transitionImpl.getDestination();
			String type = (String) activityImpl.getProperty("type");
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
				if ("END".equals(gatewayType.toUpperCase())) {
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
	private List<ActivityImpl> iteratorBackActivity(String taskId, ActivityImpl currActivity,
			List<ActivityImpl> rtnList, List<ActivityImpl> tempList) throws BpmException {
		// 查询流程定义，生成流程树结构
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);

		// 当前节点的流入来源
		List<PvmTransition> incomingTransitions = currActivity.getIncomingTransitions();
		// 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
		List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();
		// 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
		List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();
		// 遍历当前节点所有流入路径
		for (PvmTransition pvmTransition : incomingTransitions) {
			TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
			ActivityImpl activityImpl = transitionImpl.getSource();
			String type = (String) activityImpl.getProperty("type");
			/**
			 * 并行节点配置要求：<br>
			 * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
			 */
			if ("parallelGateway".equals(type)) {// 并行路线
				String gatewayId = activityImpl.getId();
				String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
				if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
					return rtnList;
				} else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
					parallelGateways.add(activityImpl);
				}
			} else if ("startEvent".equals(type)) {// 开始节点，停止递归
				return rtnList;
			} else if ("userTask".equals(type)) {// 用户任务
				tempList.add(activityImpl);
			} else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
				currActivity = transitionImpl.getSource();
				exclusiveGateways.add(currActivity);
			}
		}

		/**
		 * 迭代条件分支集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : exclusiveGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
		}

		/**
		 * 迭代并行集合，查询对应的userTask节点
		 */
		for (ActivityImpl activityImpl : parallelGateways) {
			iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
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
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/**
	 * 反向排序list集合，便于驳回节点按顺序显示
	 * 
	 * @param list
	 * @return
	 */
	private List<ActivityImpl> reverList(List<ActivityImpl> list) {
		List<ActivityImpl> rtnList = new ArrayList<ActivityImpl>();
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
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws BpmException
	 */
	private void turnTransitionForUTask(String taskId, String activityId, Map<String, Object> variables) throws BpmException {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		taskService.complete(taskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}
	
	/**
	 * 流程转向操作
	 * 
	 * @param processId
	 *            当前流程ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws BpmException
	 */
	private void turnTransitionForProcess(ProcessDefinitionEntity processDefinition,ProcessInstance process, ActivityImpl targetActivity, Map<String, Object> variables) throws BpmException {
		String sourceActivityId="";
		List<HistoricActivityInstance> has=historyService.createHistoricActivityInstanceQuery().processInstanceId(process.getId()).orderByHistoricActivityInstanceStartTime().desc().list();
		
		if(has.get(0).getActivityType().equals("callActivity") || has.get(0).getActivityType().equals("userTask") || has.get(0).getActivityType().equals("receivetask")){
			sourceActivityId=has.get(0).getActivityId();
		}else{
			return ;
		}
		
		// 当前节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(sourceActivityId);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(activityImpl);

		// 创建新流向
		TransitionImpl newTransition = activityImpl.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = targetActivity;
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向
		runtimeService.signal(process.getId());
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(activityImpl, oriPvmTransitionList);
	}

	public List<PvmActivity> findNextActivitys(String _taskId) throws BpmException {
		Task task = this.findTaskById(_taskId);
		return findNextActivitys(task);

	}
	
	public List<PvmActivity> findNextActivitys(Task task) throws BpmException {

		Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
		ProcessDefinitionEntity pde = null;
		if (processDefinitionEntitys.containsKey(task.getProcessDefinitionId())) {
			pde = processDefinitionEntitys.get(task.getProcessDefinitionId());
		} else {
			pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
			processDefinitionEntitys.put(task.getProcessDefinitionId(), pde);
		}
		ActivityImpl ac = pde.findActivity(task.getTaskDefinitionKey());
		return findNextForUserTask(ac, variables);

	}
	

	public ActivityImpl findActivityInfo(Task task, String _actityId) throws BpmException {

		ProcessDefinitionEntity pde = null;
		if (processDefinitionEntitys.containsKey(task.getProcessDefinitionId())) {
			pde = processDefinitionEntitys.get(task.getProcessDefinitionId());
		} else {
			pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
			processDefinitionEntitys.put(task.getProcessDefinitionId(), pde);
		}
		ActivityImpl ac = pde.findActivity(task.getTaskDefinitionKey());
		return ac;

	}

	/**
	 * 查询当前节点为ExclusiveGateway的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<PvmActivity> findNextForExclusiveGateway(PvmActivity activity, Map<String, Object> variables)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getOutgoingTransitions();
		for (PvmTransition t : ts) {
			Object condition = t.getProperty("conditionText");
			if(null==condition){
				PvmActivity pa = t.getDestination();
				if ("exclusiveGateway".equals(pa.getProperty("type"))
						|| "inclusiveGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
				} else if ("parallelGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findNextForParallelGateway(pa, variables));
				} else if ("userTask".equals(pa.getProperty("type"))) {
					activityImpls.add(pa);
				} else {
					activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
				}
			}else if (BshUtil.eval(condition.toString(), variables)) {
				PvmActivity pa = t.getDestination();
				LOGGER.debug("pa.getProperty(type)=={}", pa.getProperty("type"));
				if ("exclusiveGateway".equals(pa.getProperty("type"))
						|| "inclusiveGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
				} else if ("parallelGateway".equals(pa.getProperty("type"))) {
					activityImpls.addAll(findNextForParallelGateway(pa, variables));
				} else if ("userTask".equals(pa.getProperty("type"))) {
					activityImpls.add(pa);
				} else {
					activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
				}
			}
		}

		return activityImpls;
	}

	/**
	 * 查询当前节点为UserTask的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<PvmActivity> findNextForUserTask(PvmActivity activity, Map<String, Object> variables)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getOutgoingTransitions();
		for (PvmTransition t : ts) {
			PvmActivity pa = t.getDestination();
			LOGGER.debug("{}.getProperty(type)=={}", pa.getId(), pa.getProperty("type"));
			if ("exclusiveGateway".equals(pa.getProperty("type"))
					|| "inclusiveGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
			} else if ("parallelGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findNextForParallelGateway(pa, variables));
			} else if ("userTask".equals(pa.getProperty("type"))) {
				activityImpls.add(pa);
			} else {
				activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
			}
		}

		return activityImpls;
	}

	/**
	 * 查询当前节点为ParallelGateway的后续节点
	 * 
	 * @param activity
	 * @param variables
	 * @return
	 * @throws EvalError
	 */
	private List<PvmActivity> findNextForParallelGateway(PvmActivity activity, Map<String, Object> variables)
			throws BpmException {
		List<PvmActivity> activityImpls = new ArrayList<PvmActivity>();

		List<PvmTransition> ts = activity.getOutgoingTransitions();
		for (PvmTransition t : ts) {
			PvmActivity pa = t.getDestination();
			LOGGER.debug("pa.getProperty(type)=={}", pa.getProperty("type"));
			if ("exclusiveGateway".equals(pa.getProperty("type"))
					|| "inclusiveGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
			} else if ("parallelGateway".equals(pa.getProperty("type"))) {
				activityImpls.addAll(findNextForParallelGateway(pa, variables));
			} else if ("userTask".equals(pa.getProperty("type"))) {
				activityImpls.add(pa);
			} else {
				activityImpls.addAll(findNextForExclusiveGateway(pa, variables));
			}
		}

		return activityImpls;
	}

	public InputStream createRunResource(String _taskId) throws BpmException {
		try {
			Task task = this.findTaskById(_taskId);
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
			// List<String> activeActivityIds =
			// runtimeService.getActiveActivityIds(task.getExecutionId());
//			List<UserTask>  uts=bpmnModel.getMainProcess().findFlowElementsOfType(UserTask.class);
//			uts.get(0).getLoopCharacteristics().getLoopCardinality();
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();

			if (historicProcessInstance == null) {
				throw new RuntimeException("获取流程图异常!");
			} else {

				List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc()
						.list();// 获取流程走过的节点，并按照节点生成先后顺序排序
				List<String> activitiIds = new ArrayList<String>();
				List<String> flowIds = new ArrayList<String>();
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
				List<String> cls=clearErrorNode(processDefinition,activityInstances);
				flowIds = getHighLightedFlows(processDefinition, activityInstances,cls);
				for (HistoricActivityInstance hai : activityInstances) {
					if(!(hai.getActivityType().indexOf("Gateway")>0)){
						activitiIds.add(hai.getActivityId());
					}
				}

				InputStream pngs = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitiIds,
						flowIds, processEngine.getProcessEngineConfiguration().getActivityFontName(),
						processEngine.getProcessEngineConfiguration().getLabelFontName(),null, null, 1.0);
				return pngs;
			}
		} catch (Exception e) {
			LOGGER.error("获取流程图异常!", e);
			throw new BpmException("获取流程图异常!");
		}
	}

	private List<String> clearErrorNode(ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances){
		List<String> cls=new ArrayList<String>();
		for(HistoricActivityInstance hi:historicActivityInstances){
			if(hi.getActivityType().equals("boundaryError") ||hi.getActivityType().equals("boundarytimer")){
				ActivityImpl activityImpl = processDefinitionEntity
						.findActivity(hi.getActivityId());
				ActivityImpl parentActivityImpl=activityImpl.getParentActivity();
				if(null!=parentActivityImpl){
					cls.add(parentActivityImpl.getId());
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
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
//		List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();
//		for (int j = 0 ; j < historicActivityInstances.size() - 1; j++) {
//			ActivityImpl activityImpl = processDefinitionEntity
//					.findActivity(historicActivityInstances.get(j).getActivityId());
//			sameStartTimeNodes.add(activityImpl);
//		}

		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());// 将后面第一个节点放在时间相同节点的集合里
				sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i ; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
						sameStartTimeNodes.add(sameActivityImpl2);
				}else if(null!=historicActivityInstances.get(i).getEndTime() && historicActivityInstances.get(i).getEndTime().equals(activityImpl2.getStartTime())){
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
						sameStartTimeNodes.add(sameActivityImpl2);
				}else {// 有不相同跳出循环
					break;
				}
			}
			
			if(!cls.contains(activityImpl.getId())){
			
				List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
	
				for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
	
					ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
					if (sameStartTimeNodes.contains(pvmActivityImpl)) {
						highFlows.add(pvmTransition.getId());
					}
				}
			}

		}
		return highFlows;

	}
	
	
	public Map<String, byte[]> unZip(InputStream in,int parseLevel){ 
		if(in == null)   
            return null;  
          
        ZipEntry zipEntry = null;  
        FileOutputStream out = null;  
        Map<String, byte[]> map = new HashMap<String, byte[]>();  
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
		} catch (Exception e) {
			throw new BpmException("打包下载异常！",e);
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				throw new BpmException("打包下载异常！",e);
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
						throw new BpmException("打包下载异常！",e);
					}
					 
				}

				
            }
            
    }  
	
	private void writeFile(String id,String fileName) throws IOException{
		InputStream inputStream = repositoryService.getProcessModel(id);
		
		int byteCount, bufferLen = 1024;
		OutputStream  outputStream = new FileOutputStream(fileName);
		byte[] bytes = new byte[bufferLen];
		while ((byteCount = inputStream.read(bytes,0, bufferLen)) != -1)
		{
		          outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();

	}
	
}
