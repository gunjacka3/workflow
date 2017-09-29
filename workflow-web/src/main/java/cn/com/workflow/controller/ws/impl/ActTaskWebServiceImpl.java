package cn.com.workflow.controller.ws.impl;

import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActFromVO;
import cn.com.workflow.common.vo.ActProcessInstanceVO;
import cn.com.workflow.common.vo.SelectUserInfos;
import cn.com.workflow.controller.ws.ActTaskWebService;
import cn.com.workflow.service.TaskService_;

/**
 * 任务相关web服务类 Package : cn.com.workflow.controller.ws.impl
 * 
 * @author wangzhiyin 2017年9月22日 下午4:43:58
 *
 */
@Component("actTaskWebServiceImpl")
public class ActTaskWebServiceImpl extends BaseWebService implements ActTaskWebService {

    @Autowired(required = true)
    private TaskService_ taskService_;

    /**
     * 校验用字段
     */
    private static final String REQUIRE_STR = "userId,taskId";
    /**
     * remove常量userId
     */
    private static final String REMOVE_USERID = "userId";
    /**
     * remove常量taskId
     */
    private static final String REMOVE_TASKID = "taskId";

    /**
     * 根据taskID查询表单信息
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:46:40
     */
    @Override
    @WebMethod(operationName = "findTaskFormInfo")
    public String findTaskFormInfo(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        ActFromVO fromVO = taskService_.findTaskFormInfo(taskId);
        Map returnObject = objectToMap(fromVO);
        return map2Jackson(returnObject);
    }

    /**
     * 查询下一个人工任务节点信息
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:48:38
     */
    @Override
    @WebMethod(operationName = "findNextActivityUserInfo")
    public String findNextActivityUserInfo(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        SelectUserInfos suis = taskService_.findNextActivityUserInfo(taskId);
        Map returnObject = objectToMap(suis);
        return map2Jackson(returnObject);
    }

    /**
     * 挂起任务
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:49:02
     */
    @Override
    @WebMethod(operationName = "suspendTask")
    public void suspendTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        taskService_.suspendTask(taskId, null);
    }

    /**
     * 恢复已挂起任务
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:50:21
     */
    @Override
    @WebMethod(operationName = "resumeTask")
    public void resumeTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        taskService_.resumeTask(taskId);
    }

    /**
     * 领取任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:50:45
     */
    @Override
    @WebMethod(operationName = "handleTaskById")
    public String handleTaskById(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        Map<String, Object> returnMap = taskService_.handleTaskById(taskId);
        return map2Jackson(returnMap);
    }

    /**
     * 代理任务
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:51:10
     */
    @Override
    @WebMethod(operationName = "delegateTask")
    public void delegateTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        taskService_.delegateTask(taskId, map);
    }

    /**
     * 共享池中领取任务
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:51:30
     */
    @Override
    @WebMethod(operationName = "takeTask")
    public void takeTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        taskService_.takeTask(taskId, userId);
    }

    /**
     * 任务恢复到共享池
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:52:08
     */
    @Override
    @WebMethod(operationName = "replaceTask")
    public void replaceTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        taskService_.replaceTask(taskId, map);

    }

    /**
     * 任务跳转
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:52:37
     */
    @Override
    @WebMethod(operationName = "gotoTask")
    public void gotoTask(String _variablesJson) throws BpmException {

        String requiredStr = "userId,taskId,desTaskDefinitionKey";
        Map<String, Object> map = getInputDate(_variablesJson, requiredStr);
        String taskId = (String) map.get(REMOVE_TASKID);
        String desTaskDefinitionKey = (String) map.get("desTaskDefinitionKey");
        map.remove("desTaskDefinitionKey");
        taskService_.gotoTask(taskId, desTaskDefinitionKey);

    }

    /**
     * 临时保存任务
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:53:27
     */
    @Override
    @WebMethod(operationName = "saveTask")
    public void saveTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        taskService_.saveTask(taskId, map);
    }

    /**
     * 查询任务意见结论
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:53:52
     */
    @Override
    @WebMethod(operationName = "findCommentsForTask")
    public String findCommentsForTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        String comment = taskService_.findCommentsForTask(taskId);
        return "{\"comment\":\"" + comment + "\"}";
    }

    /**
     * 根据任务id查询流程实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:54:08
     */
    @Override
    @WebMethod(operationName = "findProcessIdByTask")
    public String findProcessIdByTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        ActProcessInstanceVO pi = taskService_.findProcessIdByTask(taskId);
        return object2Jackson(pi);
    }

    /**
     * 提交退回
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:54:25
     */
    @Override
    @WebMethod(operationName = "submitByBack")
    public void submitByBack(String _variablesJson) throws BpmException {

        String requiredStr = "userId,taskId,type";
        Map<String, Object> map = getInputDate(_variablesJson, requiredStr);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get(REMOVE_TASKID);
        map.remove(REMOVE_TASKID);
        String type = (String) map.get("type");
        map.remove("type");
        taskService_.submitByBack(taskId, type, map);
    }

    /**
     * 根据节点id查询任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:54:42
     */
    @Override
    @WebMethod(operationName = "findTasksForActivityId")
    public String findTasksForActivityId(String _variablesJson) throws BpmException {
        String requiredStr = "userId,executionId,taskDefinitionKey";
        Map<String, Object> map = getInputDate(_variablesJson, requiredStr);
        String taskDefinitionKey = (String) map.get("taskDefinitionKey");
        String executionId = (String) map.get("executionId");
        List<Task> tasks = taskService_.findTasksForActivityId(taskDefinitionKey, executionId);
        return object2Jackson(tasks);
    }

    /**
     * 查询首个用户人工任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:55:20
     */
    @Override
    @WebMethod(operationName = "findFirstUserTask")
    public String findFirstUserTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        PvmActivity pa = taskService_.findFirstUserTask(taskId);
        return object2Jackson(pa);
    }

    /**
     * 根据任务id查询流程变量
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:32:29
     */
    @Override
    @WebMethod(operationName = "getVeriablesByTask")
    public String getVeriablesByTask(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String taskId = (String) map.get(REMOVE_TASKID);
        Map<String, Object> returnMap = taskService_.getVeriablesByTask(taskId);
        return map2Jackson(returnMap);
    }

}
