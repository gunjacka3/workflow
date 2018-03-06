package cn.com.workflow.service;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActDefinitionPage;
import cn.com.workflow.common.vo.ActiveNodes;
import cn.com.workflow.common.vo.ModelExportVO;

@Service("processDefinitionService")
public interface ProcessDefinitionService {

    /**
     * 部署流程
     * 
     * @param templateFile
     * @param type
     * @throws BpmException
     */
    public String deploymentProcess(File templateFile, String type) throws BpmException;

    /**
     * 
     * @param modelId
     * @return
     * @throws BpmException
     */
    public String deploymentModel(String modelId) throws BpmException;

    /**
     * 
     * @param _input
     * @param _fileName
     * @return
     * @throws BpmException
     */
    public void uploadProcess(InputStream _input, String _fileName) throws BpmException;

    /**
     * 
     * @param input
     * @param _fileName
     * @return
     * @throws BpmException
     */
    public String deploymentProcess(InputStream input, String _fileName) throws BpmException;

    /**
     * 
     * @param deployMentId
     * @return
     */
    public ProcessDefinition findProcessDefinitonsByDid(String deployMentId);

    /**
     * 流程检索
     * 
     * @param templateName
     * @param _page
     * @return
     */
    public ActDefinitionPage findProcessDefinitons(String templateName, Page _page) throws BpmException;

    /**
     * 流程检索
     * 
     * @param templateName
     * @param _page
     * @return
     */
    public ActDefinitionPage findProcessModels(String templateName, Page _page) throws BpmException;

    /**
     * 
     * @param deploymentId
     * @param cascade
     */
    public void deleteDeployment(String deploymentId, boolean cascade);

    /**
     * 
     * @param templateName
     * @return
     */
    public long countDeployment(String templateName);

    /**
     * 获取配置文件
     * 
     * @param vo
     * @throws BpmException
     * @throws WorkFLowBpmException
     * @throws WorkFlowContextBpmException
     */
    public ModelExportVO readResourceStream(String deploymentId, String type) throws BpmException;

    /**
     * 找到历史活动节点的位置
     * 
     * @param taskId
     * @return
     * @throws BpmException
     * @throws WorkFLowBpmException
     * @throws WorkFlowContextBpmException
     */
    public ActiveNodes findActivityPositionHis(String taskId, String processId) throws BpmException;

    /**
     * 找到活动节点的位置
     * 
     * @param taskId
     * @return
     * @throws BpmException
     * @throws WorkFLowBpmException
     * @throws WorkFlowContextBpmException
     */
    public ActiveNodes findActivityPosition(String taskId, String processId) throws BpmException;

    /**
     * 
     * @param taskId
     * @param processId
     * @return
     * @throws BpmException
     */
    public InputStream readPictrueStream(String taskId, String processId) throws BpmException;

    /**
     * 
     * @param modelId
     * @return
     * @throws BpmException
     */
    public InputStream readPictrueStream(String modelId) throws BpmException;

    public InputStream readPictrueStreamForPdf(String pdfId) throws BpmException;

    // public IHistoryTask getHistoryTaskById(String _taskId);
    //
    /**
     * 
     * @param _pid
     * @return
     */
    public ProcessInstance queryProcessInstance(String _pid);
    //

    /**
     * 
     * @param name
     * @param key
     * @param description
     * @return
     * @throws BpmException
     */
    public String createModel(String name, String key, String description) throws BpmException;

    /**
     * 
     * @param modelId
     * @param type
     * @return
     * @throws BpmException
     */
    public ModelExportVO export(String modelId, String type) throws BpmException;
    // public IHistoryProcessInstance getHistoryProcessById(String _pid);

    public void deleteModel(String modelId, boolean cascade);

    public ModelExportVO exportAllTemplate(HttpServletRequest request) throws BpmException;
}
