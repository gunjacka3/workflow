package cn.com.workflow.controller.ws;

import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

import cn.com.workflow.common.exception.BpmException;

/**
 * 
 * Package : cn.com.workflow.controller.ws
 * 
 * @author wangzhiyin 2017年9月25日 上午9:49:23
 *
 */
@WebService
@WSDLDocumentationCollection({
        @WSDLDocumentation(value = "任务实例管理接口，主要功能包括任务提交，挂起，保存，领取，重置等.作者：gunjack，邮箱：wangzhiyin@git.com.cn", placement = WSDLDocumentation.Placement.TOP) })
public interface ActTaskWebService {

    /**
     * 查询任务内置表单数据及内部变量
     * 
     * @param _variablesJson
     * @return
     */
    public String findTaskFormInfo(String _variablesJson) throws BpmException;

    /**
     * 查询下一节点人员信息
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findNextActivityUserInfo(String _variablesJson) throws BpmException;

    /**
     * 根据任务挂起流程
     * 
     * @param _variablesJson
     * @throws BpmException
     */
    public void suspendTask(String _variablesJson) throws BpmException;

    /**
     * 根据任务恢复流程
     * 
     * @param _taskId
     * @throws BpmException
     */
    public void resumeTask(String _variablesJson) throws BpmException;

    /**
     * 处理待办列表指定任务
     * 
     * @param task
     * @return
     * @throws BpmException
     */
    public String handleTaskById(String _variablesJson) throws BpmException;

    /**
     * 任务代理
     * 
     * @param task
     * @param _variables
     */
    public void delegateTask(String _variablesJson) throws BpmException;

    /**
     * 任务签收
     * 
     * @param _variablesJson
     */
    public void takeTask(String _variablesJson) throws BpmException;

    /**
     * 已签收 任务恢复
     * 
     * @param task
     * @param _variables
     * @throws BpmException
     */
    public void replaceTask(String _variablesJson) throws BpmException;

    /**
     * 任务跳转到指定节点
     * 
     * @param taskId
     * @param desTaskDefinitionKey
     * @throws BpmException
     */
    public void gotoTask(String _variablesJson) throws BpmException;

    /**
     * 任务保存
     * 
     * @param _variablesJson
     * @throws BpmException
     */
    public void saveTask(String _variablesJson) throws BpmException;

    /**
     * 查询任务意见及结论
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findCommentsForTask(String _variablesJson) throws BpmException;

    /**
     * 根据任务查询流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findProcessIdByTask(String _variablesJson) throws BpmException;

    /**
     * 任务退回(流程图无退回线)
     * 
     * @param _variablesJson
     * @param variables
     * @throws BpmException
     */
    public void submitByBack(String _variablesJson) throws BpmException;

    /**
     * 根据节点id查询任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findTasksForActivityId(String _variablesJson) throws BpmException;

    /**
     * 查询收个人工任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String findFirstUserTask(String _variablesJson) throws BpmException;

    /**
     * 获取变量
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String getVeriablesByTask(String _variablesJson) throws BpmException;
}
