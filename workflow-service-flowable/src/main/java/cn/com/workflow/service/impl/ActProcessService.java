package cn.com.workflow.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.ActContent;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActFormValueVO;
import cn.com.workflow.common.vo.ActFromVO;
import cn.com.workflow.common.vo.ActInsideFormVO;
import cn.com.workflow.common.vo.ActProcessInstanceVO;
import cn.com.workflow.service.ProcessService;

@Service("actProcessService")
public class ActProcessService implements ProcessService {
	
    private static final Logger LOGGER = LogManager.getLogger(ActProcessService.class);
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private IdentityService identityService;
	
	@Resource
	private FormService formService;
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private ProcessCustomService processCustomService;
	
	
	public ActFromVO findProcessFormInfo(String _processDefinitionId){
		ActFromVO afv=new ActFromVO();
		StartFormData sfd=formService.getStartFormData(_processDefinitionId);
		
		//如果task配置了formKey外置表单的话,优先使用
		if(StringUtils.isNotBlank(sfd.getFormKey())){
			afv.setFromKey(sfd.getFormKey());
			return afv;
		}
		
		//查看是否存在内置form表单配置
		List<org.flowable.engine.form.FormProperty> fps=sfd.getFormProperties();
		List<ActInsideFormVO> afs=new ArrayList<>();
//		List<ActFormValueVO> actions=new ArrayList<>();
		for(org.flowable.engine.form.FormProperty fp:fps){
			ActInsideFormVO af=new ActInsideFormVO();
			if(fp.isReadable() && fp.isWritable()){
//				af.setFiledWR(fp.isWritable()? "1":"0");
				af.setFiledId(fp.getId());
				af.setFiledName(fp.getName());
				af.setFiledValue(fp.getValue());
				af.setFiledType(fp.getType().getName());
				if("enum".equals(fp.getType().getName())){
					Map<String, Object> propertyValues=(Map<String, Object>) fp.getType().getInformation("values");
					List<ActFormValueVO> values=new ArrayList<>();
				    for (Map.Entry<String,Object> entry : propertyValues.entrySet()) {
						ActFormValueVO ActFormValueVO=new ActFormValueVO();
						ActFormValueVO.setValueId(entry.getKey());
						ActFormValueVO.setValueName((String)entry.getValue());
						values.add(ActFormValueVO);
					}
					af.setValues(values);
				}
				afs.add(af);
			}
		}
		afv.setInsideFroms(afs);
		return afv;
		
	}
	
	@Override
	public void suspendProcess(String _processId, String timeout) throws BpmException {
		runtimeService.suspendProcessInstanceById(_processId);
	}
	
	@Override
	public void resumeProcess(String _processId){
		runtimeService.activateProcessInstanceById(_processId);
	}

	@Override
	public void saveVariables(String _processId,
			Map<String, Object> _variables, boolean isHis) throws BpmException {
		runtimeService.setVariables(_processId, _variables);

	}

	@Override
	public void cancelProcessByProcessId(String _processId) {
		runtimeService.deleteProcessInstance(_processId, "cancelProcess");
	}
	
	@Override
	public void cancelProcessByProcessIds(String _processIds) {
		String[] pids=_processIds.split(",");
		for(String pid:pids){
			runtimeService.deleteProcessInstance(pid, "cancelProcess");
		}
	}
	
	public void endProcessByProcessId(String _processId) throws BpmException{
		processCustomService.endProcessById(_processId);
	}

	@Override
	public ActProcessInstanceVO findProcessByPId(String _processId) throws BpmException {
		ProcessInstance pi= runtimeService.createProcessInstanceQuery().processInstanceId(_processId).singleResult();
		ActProcessInstanceVO ap=new ActProcessInstanceVO();
		try {
			PropertyUtils.copyProperties(ap,pi);
		} catch (IllegalAccessException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is IllegalAccessException",e);
		} catch (InvocationTargetException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is InvocationTargetException",e);
		} catch (NoSuchMethodException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is NoSuchMethodException",e);
		}
		return ap;
	}

	@Override
	public String startProcess(String userId,String _processKey,String _bizId,
			Map<String, Object> _variables) throws BpmException {
		identityService.setAuthenticatedUserId(userId);
		_variables.put(ActContent.PROCESS_CREATER_VAR_NAME, userId);
		ProcessInstance pi=runtimeService.startProcessInstanceByKey(_processKey, _bizId, _variables);
		return pi.getProcessInstanceId();
	}

	@Override
	public void insertProcessInstance(String _pid, String _processKey,
			Map<String, Object> _variables, String status) {
	    return ;
	}

	@Override
	public void rollbackCreateProcessByProcessId(String _processId)
			throws BpmException {
	    return ;

	}

	@Override
	public String findProcessIdByTaskId(String _taskId) throws BpmException {

		Task task=taskService.createTaskQuery().taskId(_taskId).singleResult();
		
		return task.getExecutionId();
	}

	@Override
	public String findProcessIdByBizId(String _bizId) throws BpmException {
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(_bizId).singleResult();
		if(null==pi){
			throw new BpmException("根据业务id["+_bizId+"]查询流程运行实例未找到");
		}
		return pi.getId();
	}

	@Override
	public Map<String, Object> findVariablesForRestart(String _processId)
			throws BpmException {
		return null;
	}
	
	@Override
	public Map<String, Object> findFinishedVariables(String _processId) {
		
		HistoricProcessInstance hp=historyService.createHistoricProcessInstanceQuery().processInstanceId(_processId).asc().includeProcessVariables().singleResult();
		hp.getProcessVariables().put("processDefName", hp.getProcessDefinitionId());
		return hp.getProcessVariables();
	}
	
	@Override
	public Map<String, Object> findVariables(String _processId) {
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(_processId).includeProcessVariables().singleResult();
		pi.getProcessVariables().put("processDefName", pi.getProcessDefinitionId());
		return pi.getProcessVariables();
	}
	
	@Override
	public Object findVariables(String _processId,String variableName) {
		return runtimeService.getVariable(_processId, variableName);
		
	}

	@Override
	public void createProcessHis(String _processId) {
	    return ;
	}

	@Override
	public void createProcessinstanceForAuto(String _processId,
			String processDefId, Map<String, Object> _variables) {
		return ;

	}

	@Override
	public ActProcessInstanceVO findProcessByTask(TaskInfo _task)
			throws BpmException {
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(_task.getProcessInstanceId()).singleResult();
		ActProcessInstanceVO ap=new ActProcessInstanceVO();
		try {
			PropertyUtils.copyProperties(ap,pi);
		} catch (IllegalAccessException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is IllegalAccessException",e);
		} catch (InvocationTargetException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is InvocationTargetException",e);
		} catch (NoSuchMethodException e) {
		    LOGGER.error(e);
			throw new BpmException("findProcessByPId.copyProperties is NoSuchMethodException",e);
		}
		return ap;
	}
	
	
	@Override
	public void signalProcess(String executionId,Map<String, Object> processVariables) {
		Execution ex=runtimeService.createExecutionQuery().parentId(executionId).singleResult();
		if(null!=processVariables){
			runtimeService.trigger(ex.getId(), processVariables);
		}else{
			runtimeService.trigger(ex.getId());
		}
	}

}
