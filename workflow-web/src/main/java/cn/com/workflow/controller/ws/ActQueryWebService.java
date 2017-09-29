package cn.com.workflow.controller.ws;

import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

import cn.com.workflow.common.exception.BpmException;

/**
 * 
 * Package : cn.com.workflow.controller.ws
 * 
 * @author wangzhiyin 2017年9月25日 上午9:49:45
 *
 */
@WebService
@WSDLDocumentationCollection({
        @WSDLDocumentation(value = "查询管理接口，主要功能包括个人任务查询，岗位任务查询等.作者：gunjack，邮箱：wangzhiyin@git.com.cn", placement = WSDLDocumentation.Placement.TOP) })
public interface ActQueryWebService {

    /**
     * 查询个人待办任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryWorkList(String _variablesJson) throws BpmException;

    /**
     * 统计个人待办任务
     * 
     * @param userId
     * @param orgCd
     * @param roleCd
     * @param _map
     * @return
     * @throws BpmException
     */
    public String conutWorkList(String _variablesJson) throws BpmException;

    /**
     * 查询个人岗位任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryCandiWorkList(String _variablesJson) throws BpmException;

    /**
     * 统计个人岗位任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String conutCandiWorkList(String _variablesJson) throws BpmException;

    /**
     * 查询个人挂起任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String querySuspendList(String _variablesJson) throws BpmException;

    /**
     * 统计个人挂起任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String conutSuspendList(String _variablesJson) throws BpmException;

    /**
     * 查询个人历史流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryHisList(String _variablesJson) throws BpmException;

    /**
     * 统计个人历史流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String conutHisList(String _variablesJson) throws BpmException;

    /**
     * 查询已结束历史流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryFinishHisList(String _variablesJson) throws BpmException;

    /**
     * 查询全部历史流程
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryhisListAll(String _variablesJson) throws BpmException;

    /**
     * 根据历史流程实例id查询历史任务实例
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     */
    public String queryHisTaskList(String _variablesJson) throws BpmException;

    /**
     * 统计用户各个业务流程个人任务数量
     * 
     * @param _variablesJson
     * @return
     */
    public String countWorkForTemplate(String _variablesJson) throws BpmException;

    /**
     * 统计用户各个业务流程岗位任务数量
     * 
     * @param _variablesJson
     * @return
     */
    public String countCandiWorkByTemplate(String _variablesJson) throws BpmException;
}
