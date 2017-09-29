package cn.com.workflow.controller.ws;

import javax.jws.WebService;
import cn.com.workflow.common.exception.BpmException;
import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

/**
 * 
 * Package : cn.com.workflow.controller.ws
 * 
 * @author wangzhiyin 2017年9月25日 上午9:50:01
 *
 */
@WebService
@WSDLDocumentationCollection({
        @WSDLDocumentation(value = "流程实例管理接口，主要功能包括流程发起，挂起，恢复，变量处理等.作者：gunjack，邮箱：wangzhiyin@git.com.cn", placement = WSDLDocumentation.Placement.TOP) })
public interface ActProcessWebService {

    /**
     * 启动流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String startProcess(String _variablesJson) throws BpmException;

    /**
     * 挂起流程
     * 
     * @param _variablesJson
     * @param timeout
     * @throws BpmException
     */
    public void suspendProcess(String _variablesJson) throws BpmException;

    /**
     * 恢复已挂起流程
     * 
     * @param _variablesJson
     * @throws BpmException
     */
    public void resumeProcess(String _variablesJson) throws BpmException;

    /**
     * 保存流程变量
     * 
     * @param _variablesJson
     * @throws BpmException
     */
    public void saveVariables(String _variablesJson) throws BpmException;

    /**
     * 根据流程id查询流程实例(运行中流程)
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findProcessByPId(String _variablesJson) throws BpmException;

    /**
     * 撤销流程
     * 
     * @param _variablesJson
     */
    public void cancelProcessByProcessId(String _variablesJson) throws BpmException;

    /**
     * 根据任务id查询流程实例id
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findProcessIdByTaskId(String _variablesJson) throws BpmException;

    /**
     * 根据业务id查询流程实例id
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findProcessIdByBizId(String _variablesJson) throws BpmException;

    /**
     * 查询流程变量
     * 
     * @param _variablesJson
     * @return
     */
    public String findVariables(String _variablesJson) throws BpmException;

    /**
     * 激活处于recived状态的流程
     * 
     * @param _variablesJson
     * @param processVariables
     * @throws BpmException
     */
    public void signalProcess(String _variablesJson) throws BpmException;

}
