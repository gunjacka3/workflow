package cn.com.workflow.controller.ws.impl;

import java.util.Map;
import javax.jws.WebMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActProcessInstanceVO;
import cn.com.workflow.controller.ws.ActProcessWebService;
import cn.com.workflow.service.ProcessService;

/**
 * 流程实例相关web服务类 Package : cn.com.workflow.controller.ws.impl
 * 
 * @author wangzhiyin 2017年9月25日 上午9:32:53
 *
 */
@Component("actProcessWebServiceImpl")
public class ActProcessWebServiceImpl extends BaseWebService implements ActProcessWebService {
    @Autowired(required = true)
    private ProcessService processService;

    /**
     * 校验用字段
     */
    private static final String REQUIRE_STR = "userId,processId";
    /**
     * remove常量userId
     */
    private static final String REMOVE_USERID = "userId";
    /**
     * remove常量bizId
     */
    private static final String REMOVE_BIZID = "bizId";

    /**
     * 启动流程实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:45:14
     */
    @Override
    @WebMethod(operationName = "startProcess")
    public String startProcess(String _variablesJson) throws BpmException {
        String requiredStr = "userId,processKey,bizId";
        Map<String, Object> map = getInputDate(_variablesJson, requiredStr);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processKey = (String) map.get("processKey");
        map.remove("processKey");
        String bizId = (String) map.get(REMOVE_BIZID);
        map.remove(REMOVE_BIZID);
        String pid = processService.startProcess(userId, processKey, bizId, map);
        return "{\"processId\":\"" + pid + "\"}";
    }

    /**
     * 挂起流程实例
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:45:26
     */
    @Override
    @WebMethod(operationName = "suspendProcess")
    public void suspendProcess(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        processService.suspendProcess(processId, null);

    }

    /**
     * 恢复挂起流程实例
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:45:35
     */
    @Override
    @WebMethod(operationName = "resumeProcess")
    public void resumeProcess(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        processService.resumeProcess(processId);
    }

    /**
     * 保存流程实例变量
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:45:46
     */
    @Override
    @WebMethod(operationName = "saveVariables")
    public void saveVariables(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        processService.saveVariables(processId, map, true);

    }

    /**
     * 根据实例id查询流程实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:46:03
     */
    @Override
    @WebMethod(operationName = "findProcessByPId")
    public String findProcessByPId(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        ActProcessInstanceVO pi = processService.findProcessByPId(processId);
        return object2Jackson(pi);
    }

    /**
     * 根据流程实例id撤销流程
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:46:35
     */
    @Override
    @WebMethod(operationName = "cancelProcessByProcessId")
    public void cancelProcessByProcessId(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        processService.cancelProcessByProcessId(processId);

    }

    /**
     * 查询指定任务所属流程实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:46:58
     */
    @Override
    @WebMethod(operationName = "findProcessIdByTaskId")
    public String findProcessIdByTaskId(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String taskId = (String) map.get("taskId");
        map.remove("taskId");
        String pid = processService.findProcessIdByTaskId(taskId);
        return "{\"processId\":\"" + pid + "\"}";
    }

    /**
     * 查询指定业务所属流程实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:47:13
     */
    @Override
    @WebMethod(operationName = "findProcessIdByBizId")
    public String findProcessIdByBizId(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String bizId = (String) map.get(REMOVE_BIZID);
        map.remove(REMOVE_BIZID);
        String pid = processService.findProcessIdByBizId(bizId);
        return "{\"processId\":\"" + pid + "\"}";
    }

    /**
     * 查询流程实例所有变量
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:47:25
     */
    @Override
    @WebMethod(operationName = "findVariables")
    public String findVariables(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        Map<String, Object> variables = processService.findVariables(processId);
        return map2Jackson(variables);
    }

    /**
     * 激活指定待命流程实例
     * 
     * @param _variablesJson
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:47:40
     */
    @Override
    @WebMethod(operationName = "signalProcess")
    public void signalProcess(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        // String userId=(String)map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        processService.signalProcess(processId, map);
    }

}
