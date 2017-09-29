package cn.com.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActFromVO;
import cn.com.workflow.common.vo.ActProcessInstanceVO;
import cn.com.workflow.common.vo.ActiveNode;
import cn.com.workflow.common.vo.SelectUserInfos;

@Service("taskService_")
public interface TaskService_ {

	/**
	 * 异步批量提交
	 * @param _taskIds
	 * @param _variables
	 */
	public void submitBatchAsync(List<String> _taskIds,Map<String , Object> _variables)throws BpmException;
	
	/**
	 * 同步批量提交
	 * @param _taskIds
	 * @param _variables
	 */
	public void submitBatch(List<String> _taskIds,Map<String , Object> _variables)throws BpmException;
	
	/**
	 * 任务取回
	 * @param taskId
	 * @param _variables
	 * @throws BpmException
	 */
	public void retrieveTask(String taskId,String userId,Map<String, Object> _variables)throws BpmException;
	/**
	 * 任务挂起
	 * 
	 * @param _taskId
	 * @param timeout
	 * @throws BpmException
	 */
	public void suspendTask(String _taskId, String timeout) throws BpmException;

	/**
	 * 任务恢复
	 * 
	 * @param _taskId
	 * @throws BpmException
	 */
	public void resumeTask(String _taskId) throws BpmException;

	/**
	 * 根据任务Id查询任务详情
	 * 
	 * @param _taskId
	 * @return 任务对象
	 * @throws BpmException
	 */
	public Map<String, Object> handleTaskById(Task  task) throws BpmException;
	/**
	 * 根据任务Id查询任务详情
	 * 
	 * @param _taskId
	 * @return 任务对象
	 * @throws BpmException
	 */	
	public Map<String, Object> handleTaskById(String taskId) throws BpmException;

	/**
	 * 任务转办
	 * 
	 * @param _taskId
	 * @param _variables
	 */
	public void delegateTask(Task task, Map<String, Object> _variables) throws BpmException;
	/**
	 * 任务转办
	 * 
	 * @param _taskId
	 * @param _variables
	 */	
	public void delegateTask(String taskId, Map<String, Object> _variables)
			throws BpmException;

	/**
	 * 任务提交
	 * 
	 * @param _taskId
	 * @param variables
	 * @throws BpmException
	 */
	public void submitTaskInstance(Task task, Map<String, Object> _variables)
			throws BpmException;

	/**
	 * 获取共享池指定任务
	 * 
	 * @param _taskId
	 * @param _variables
	 */
	public void takeTask(Task task, String timeout,Map<String, Object> _variables);

	/**
	 * 获取共享池指定任务
	 * 
	 * @param _taskId
	 * @param _variables
	 */
	public void takeTask(Task task, String timeout, String userId);

	/**
	 * 取消任务退回共享池
	 * 
	 * @param _taskId
	 * @param _variables
	 * @throws BpmException
	 */
	public void replaceTask(Task task, Map<String, Object> _variables)
			throws BpmException;
	
	/**
	 * 查询流程所有经过人工节点
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public List<PvmActivity> findHisUserTask(Task task) throws BpmException;
	
	/**
	 * 查询流程首节点信息
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public PvmActivity findFirstUserTask(String _taskId) throws BpmException;
	
	/**
	 * 查询流程首节点信息
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public PvmActivity findFirstUserTask(Task task) throws BpmException;
	/**
	 * 取消任务退回共享池
	 * 
	 * @param _taskId
	 * @param _variables
	 * @throws BpmException
	 */
	public void replaceTask(String taskId,  Map<String, Object> _variables)
			throws BpmException;

	/**
	 * 获取共享池指定任务
	 * 
	 * @param _taskId
	 * @param _variables
	 */
	public void saveTask(String taskId, Map<String, Object> _variables);

	/**
	 * 
	 * @param _taskId
	 * @return
	 * @throws BpmException
	 */
	public Task findTaskById(String _taskId)  ;
	
	/**
	 * 
	 * @param _task
	 * @return
	 * @throws BpmException
	 */
	public Map<String, Object> getVeriablesByTask(Task _task)  ;
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getVeriablesByTask(String taskId);

	/**
	 * 
	 * @param task
	 * @param variables
	 * @throws BpmException
	 */
	public void handleTaskInstance(Task task, Map<String, Object> variables)
			throws BpmException;

	/**
	 * 
	 * @param _taskId
	 * @param _variables
	 * @return
	 * @throws BpmException
	 */
//	public WfmidActivityConfigure findTaskConfigure(Task task,
//			Map<String, Object> _variables) throws BpmException;
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public ActFromVO findTaskFormInfo(String taskId);

	/**
	 * 
	 * @param taskId
	 * @param key
	 * @return
	 */
	public Object getValueFromTaskVariable(String taskId, String key);

	/**
	 * 
	 * @param task
	 * @return
	 * @throws BpmException
	 */
	public ActProcessInstanceVO findProcessIdByTask(Task task) throws BpmException;
	/**
	 * 
	 * @param task
	 * @return
	 * @throws BpmException
	 */	
	public ActProcessInstanceVO findProcessIdByTask(String taskId) throws BpmException;

	/**
	 * 
	 * @param _taskId
	 * @return
	 * @throws BpmException 
	 */
	public String queryParentTaskById(String _taskId) throws BpmException;

	/**
	 * 
	 * @param _taskId
	 * @param variables
	 */
	public void submitNomalTask(Task task, Map<String, Object> variables);
	
	/**
	 * 根据taskId实现流程任务跳转
	 * @param taskId
	 * @param desTaskDefinitionKey
	 * @throws BpmException
	 */
	public void gotoTask(String taskId,String desTaskDefinitionKey ) throws BpmException;

	/**
	 * 
	 * @param task
	 * @param superTaskId
	 * @param mPassType
	 * @param variables
	 * @throws BpmException
	 */
	public void submitMeetingTask(Task task ,String superTaskId, String mPassType,
			Map<String, Object> variables) throws BpmException;
	
	/**
	 * 查询当前任务节点是否存在多个任务
	 * @param taskDefinitionKey
	 * @param executionId
	 * @return
	 */
	public List<Task> findTasksForActivityId(String taskDefinitionKey,String executionId);
	
	/**
	 * 任务退回
	 * @param _taskId
	 * @param variables
	 */
	public void submitByBack(String _taskId,String type, Map<String, Object> variables)throws BpmException ;

//	public WfmidActivityConfigure findTaskConfigure(Task task,
//			Map<String, Object> _variables) throws Exception;

	public void takeTask(String _taskId, String _userId);

	public void addComment(Task task, Map<String, Object> _variables);
	
	/**
	 * 查询意见信息
	 * @param taskId
	 * @return
	 */
	public String findCommentsForTask(String taskId);
	
	/**
	 * 根据任务id撤销流程
	 * @param _taskId
	 */
	public void cancelProcessByTaskId(String _taskId);
	

	void submitNomalTask(String _taskId, Map<String, Object> variables);
	
	public SelectUserInfos findNextActivityUserInfo(String _taskId) throws BpmException;
	
	public List<ActiveNode> readActity(String _taskId) throws BpmException;

	public InputStream readRunResource(String taskId) throws BpmException;
	
	public List<ActiveNode> readHisActity(String _taskId) throws BpmException;
	
	public List<ActiveNode> readAllActity(String _taskId) throws BpmException;
	
	public void readFakeActity(ActiveNode ann) throws BpmException;

	public void createSubTask(String _taskId);
}
