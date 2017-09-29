package cn.com.workflow.service;

import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import  cn.com.workflow.common.exception.BpmException;
import  cn.com.workflow.common.vo.ActFromVO;
import  cn.com.workflow.common.vo.ActProcessInstanceVO;

@Service("processService")
public interface ProcessService {
	
	/**
	 * 
	 * @param _processDefinitionId
	 * @return
	 */
	public ActFromVO findProcessFormInfo(String _processDefinitionId);

	/**
	 * 
	 * @param bizType
	 *            业务类型
	 * @param variables
	 * @return
	 * @throws WorkFLowBpmException
	 */

	public void saveVariables(String _processId,
			Map<String, Object> _variables, boolean isHis) throws BpmException;


	/**
	 * 根据流程实例Id撤销流程
	 * 
	 * @param _processId
	 *            流程实例Id
	 */
	public void cancelProcessByProcessId(String _processId);
	
	/**
	 * 查询已结束流程变量
	 * @param _processId
	 * @return
	 */
	public Map<String, Object> findFinishedVariables(String _processId);
	
	
	
	/**
	 * 查询流程变量
	 * @param _processId
	 * @return
	 */
	public Map<String, Object> findVariables(String _processId);
	
	
	/**
	 * 查询流程指定变量的值
	 * @param _processId
	 * @param variableName
	 * @return
	 */
	public Object findVariables(String _processId,String variableName);

	/**
	 * 根据流程id查询流程实例
	 * 
	 * @param _processId
	 * @return
	 * @throws BpmException
	 */

	public ActProcessInstanceVO findProcessByPId(String _processId)
			throws BpmException;

	/**
	 * 
	 * @param _processKey
	 * @param _variables
	 * @return
	 * @throws BpmException
	 */
	public String startProcess(String userId,String _processKey, String _bizId,
			Map<String, Object> _variables) throws BpmException;

	/**
	 * 
	 * @param _pid
	 * @param _processKey
	 * @param _variables
	 * @param status
	 */
	public void insertProcessInstance(String _pid, String _processKey,
			Map<String, Object> _variables, String status);

	/**
	 * 
	 * @param _processId
	 */
	public void rollbackCreateProcessByProcessId(String _processId)
			throws BpmException;

	/**
	 * 
	 * @param _taskId
	 * @return
	 * @throws BpmException
	 */
	public String findProcessIdByTaskId(String _taskId) throws BpmException;

	/**
	 * 
	 * @param _task
	 * @return
	 * @throws BpmException
	 */
	public ActProcessInstanceVO findProcessByTask(Task _task) throws BpmException;
	/**
	 * 
	 * @param _bizId
	 * @return
	 * @throws BpmException
	 */
	public String findProcessIdByBizId(String _bizId) throws BpmException;

	/**
	 * 
	 * @param _processId
	 * @return
	 * @throws BpmException
	 */
	public Map<String, Object> findVariablesForRestart(String _processId)
			throws BpmException;

	
	/**
	 * 结束一组流程，通过“,”分割
	 * @param _processIds
	 */
	public void cancelProcessByProcessIds(String _processIds);

	/**
	 * 
	 * @param _processId
	 */
	public void createProcessHis(String _processId);

	/**
	 * 
	 * @param _processId
	 * @param processDefId
	 * @param _variables
	 */
	public void createProcessinstanceForAuto(String _processId,
			String processDefId, Map<String, Object> _variables);

	/**
	 * 流程实例挂起
	 * @param _processId
	 * @param timeout
	 * @throws BpmException
	 */
	public void suspendProcess(String _processId, String timeout)
			throws BpmException;

	/**
	 * 流程实例恢复
	 * @param _processId
	 * @throws BpmException
	 */
	public void resumeProcess(String _processId) throws BpmException;
	
	/**
	 * 
	 * @param executionId
	 * @param processVariables
	 * @throws BpmException
	 */
	public void signalProcess(String executionId,Map<String, Object> processVariables);
}
