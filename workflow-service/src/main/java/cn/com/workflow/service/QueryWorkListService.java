package cn.com.workflow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActHisActInstListPage;
import cn.com.workflow.common.vo.ActHisProcessListPage;
import cn.com.workflow.common.vo.ActHisTaskListPage;
import cn.com.workflow.common.vo.ActWorkListPage;

@Service("queryWorkListService")
public interface QueryWorkListService {

	/**
	 * 按照参数条件查询个人待办任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActWorkListPage queryWorkList(String userId, java.util.Map<String, Object> _map, Page page)
			throws BpmException;

	public Long conutWorkList(String userId, String orgCd, String roleCd, java.util.Map<String, Object> _map)
			throws BpmException;

	/**
	 * 按照参数条件查询池任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActWorkListPage queryCandiWorkList(String _candidateUserId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	public Long conutCandiWorkList(String _candidateUserId, java.util.Map<String, Object> _map) throws BpmException;

	/**
	 * 按照参数条件查询挂起任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActWorkListPage querySuspendList(String userId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	public Long conutSuspendList(String userId, java.util.Map<String, Object> _map) throws BpmException;

	/**
	 * 按照参数条件查询历史任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActHisProcessListPage queryHisList(String userId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	public ActHisProcessListPage queryFinishHisList(String userId, java.util.Map<String, Object> _map,
			Page _page) throws BpmException;

	public ActHisProcessListPage queryhisListAll(java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	public Long conutHisList(String userId, java.util.Map<String, Object> _map) throws BpmException;

	/**
	 * 按照参数条件查询历史任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActHisActInstListPage queryHisTaskList(String _HprocessId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	
	/**
	 * 按照参数条件查询历史会议任务
	 * 
	 * @param userId
	 * @param orgCd
	 * @param roleCd
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public ActHisTaskListPage queryHisMeetingTaskList(String _HprocessId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;
	/**
	 * 
	 * @param _processId
	 * @param _map
	 * @param _page
	 * @return
	 * @throws BpmException
	 */
	public List<Task> queryRunTaskList(String _processId, java.util.Map<String, Object> _map, Page _page)
			throws BpmException;

	/**
	 * 
	 * @param _HprocessId
	 * @param _map
	 * @return
	 * @throws BpmException
	 */
	public Long conutHisTaskList(String _HprocessId, java.util.Map<String, Object> _map) throws BpmException;

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Long> countWorkForTemplate(String userId);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Long> countCandiWorkByTemplate(String userId);
	
	/**
	 * 
	 * 
	 * @return 
	 * @author YixinCapital -- wangzhiyin
	 *	       2017年6月19日 下午3:05:21
	 */
	public  List<Map<String, Object>> findProcessInstanceCountChart();
}
