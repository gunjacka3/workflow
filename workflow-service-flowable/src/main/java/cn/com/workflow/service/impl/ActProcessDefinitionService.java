package cn.com.workflow.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.util.io.BytesStreamSource;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActDefinitionPage;
import cn.com.workflow.common.vo.ActDefinitionVO;
import cn.com.workflow.common.vo.ActiveNode;
import cn.com.workflow.common.vo.ActiveNodes;
import cn.com.workflow.common.vo.ModelExportVO;
import cn.com.workflow.service.ProcessDefinitionService;

@Service("actProcessDefinitionService")
public class ActProcessDefinitionService implements ProcessDefinitionService {

    private static final Logger LOGGER = LogManager.getLogger(ActProcessDefinitionService.class);

    protected final static String zipType = "ZIP";

    protected final static String fileType = "FILE";

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private ProcessCustomService processCustomService;

    /**
     * 部署流程
     * 
     * @param templateFile
     * @param type
     * @throws Exception
     */
    @Override
    public String deploymentProcess(File templateFile, String type) throws BpmException {
        LOGGER.debug("templateFile===" + templateFile);
        LOGGER.debug("type===" + type);

        String deployMentId = "";

        try {
            if (type.equals(fileType)) {
                deployMentId = this.deploymentProcessByTemplate(templateFile);
            } else if (type.equals(zipType)) {
                deployMentId = this.deploymentProcess(new FileInputStream(templateFile));
            } else {
                throw new Exception("发布失败，流程模版发布方式不正确:[" + fileType + "," + zipType + "]");
            }
        } catch (Exception e) {
            throw new BpmException("发布失败！", e);
        }

        ProcessDefinition pdf = this.findProcessDefinitonsByDid(deployMentId);

        createModelData(pdf);
        return deployMentId;
    }

    /**
     * 
     */
    @Override
    public String deploymentModel(String modelId) throws BpmException {
        try {
            org.flowable.engine.repository.Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));

            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (null != model.getMainProcess()) {
                bpmnBytes = new BpmnXMLConverter().convertToXML(model);

                String processName = modelData.getName() + ".bpmn20.xml";
                Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
                        .addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
                // modelData.setDeploymentId(deployment.getId());
                // repositoryService.saveModel(modelData);
                return deployment.getId();
            } else {
                throw new BpmException("deploymentModel error: BpmnModel havn't MainProcess!");
            }
        } catch (IOException e) {
            LOGGER.error("根据模型部署流程失败：modelId={}", modelId, e);
            throw new BpmException("发布失败！", e);
        }
    }

    /**
     * 
     * @param pd
     * @throws BpmException
     */
    private void createModelData(ProcessDefinition pd) throws BpmException {

        try {
            BpmnModel bm = repositoryService.getBpmnModel(pd.getId());
            ObjectNode editorNode = new BpmnJsonConverter().convertToJson(bm);

            ObjectMapper objectMapper = new ObjectMapper();
            // ObjectNode editorNode = objectMapper.createObjectNode();
            // editorNode.put("id", "canvas");
            // editorNode.put("resourceId", "canvas");
            // ObjectNode stencilSetNode = objectMapper.createObjectNode();
            // stencilSetNode.put("namespace",
            // "http://b3mn.org/stencilset/bpmn2.0#");
            // editorNode.put("stencilset", stencilSetNode);
            org.flowable.engine.repository.Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, pd.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            String description = StringUtils.defaultString(pd.getDescription());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(pd.getName());
            modelData.setDeploymentId(pd.getDeploymentId());
            modelData.setKey(StringUtils.defaultString(pd.getKey()));
            modelData.setCategory(pd.getCategory());

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        } catch (IOException e) {
            throw new BpmException(e);
        }

    }

    /**
     * 
     * @param file
     * @return
     * @throws Exception
     */
    private String deploymentProcessByTemplate(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("file not exists");
        }
        String pngFilePath = file.getPath().replace("jpdl.xml", "png");
        File f = new File(pngFilePath);
        try {
            Deployment dp = null;
            if (f.exists()) {
                dp = repositoryService.createDeployment().addClasspathResource(file.getPath())
                        .addClasspathResource(f.getPath()).deploy();
            } else {
                dp = repositoryService.createDeployment().addClasspathResource(file.getPath()).deploy();
            }
            return dp.getId();
        } catch (Exception e) {
            throw new Exception("Deployment error!", e);
        }

    }

    /**
     * 文件流方式部署流程模板
     * 
     * @param input
     * @return
     * @throws Exception
     */
    private String deploymentProcess(FileInputStream input) throws BpmException {
        ZipInputStream zin = new ZipInputStream(input);
        Deployment dp = repositoryService.createDeployment().addZipInputStream(zin).deploy();
        return dp.getId();
    }

    private void uploadProcessForBPMN(InputStream _input) throws BpmException {

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
        int rc = 0;
        try {
            while ((rc = _input.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }

            byte[] in_b = swapStream.toByteArray();

            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(new BytesStreamSource(in_b), false, false);
            // 获取xml
            ObjectNode editorNode = new BpmnJsonConverter().convertToJson(bpmnModel);
            // 获取图片
            InputStream jpgIs = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "jpg",
                    processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    processEngine.getProcessEngineConfiguration().getLabelFontName(), null, null, 1.0);
            rc = 0;
            swapStream = new ByteArrayOutputStream();
            while ((rc = jpgIs.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in_png = swapStream.toByteArray();

            List<org.flowable.bpmn.model.Process> pl = bpmnModel.getProcesses();
            String name = "";
            String id = "";
            String description = "";
            for (org.flowable.bpmn.model.Process p : pl) {
                if (p.isExecutable()) {
                    name = p.getName();
                    id = p.getId();
                    description = p.getDocumentation();
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            org.flowable.engine.repository.Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(id));
            modelData.setCategory(bpmnModel.getTargetNamespace());

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            repositoryService.addModelEditorSourceExtra(modelData.getId(), in_png);

        } catch (IOException e) {
            throw new BpmException(e);
        } finally {
            try {
                swapStream.close();
            } catch (IOException e) {
                throw new BpmException(e);
            }
        }

    }

    /**
     * 
     */
    public void uploadProcess(InputStream _input, String _fileName) throws BpmException {

        if ("bpmn".equals(checkFileType(_fileName)) || "xml".equals(checkFileType(_fileName))) {
            uploadProcessForBPMN(_input);
        } else if (checkFileType(_fileName).equals("zip")) {
            uploadProcessForZIP(_input);
        } else {
            throw new BpmException("平台只支持bpmn或zip文件上传！");
        }

    }

    private String checkFileType(String fileName) throws BpmException {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            throw new BpmException("文件：{} 不能判断类型!");
        }
    }

    private void uploadProcessForZIP(InputStream _input) throws BpmException {

        Map<String, byte[]> files = processCustomService.unZip(_input);

        for (Map.Entry<String, byte[]> entry : files.entrySet()) {

            InputStream is = new ByteArrayInputStream(entry.getValue());

            LOGGER.debug("uploadProcessForZIP----file:{}", entry.getKey());
            uploadProcessForBPMN(is);
        }

    }

    /**
     * 
     */
    public String deploymentProcess(InputStream _input, String _fileName) throws BpmException {
        Deployment dp = repositoryService.createDeployment().addInputStream(_fileName, _input).deploy();
        return dp.getId();
    }

    /**
     * 
     */
    @Override
    public void deleteModel(String modelId, boolean cascade) {
        LOGGER.debug("deploymentId===" + modelId);
        // Model m = repositoryService.getModel(modelId);
        // if (null != m.getDeploymentId() && !"".equals(m.getDeploymentId())) {
        // repositoryService.deleteDeployment(m.getDeploymentId(), cascade);
        // }
        repositoryService.deleteModel(modelId);
    }

    /**
     * 
     */
    @Override
    public void deleteDeployment(String deploymentId, boolean cascade) {
        LOGGER.debug("deploymentId===" + deploymentId);

        // Model
        // model=repositoryService.createModelQuery().deploymentId(deploymentId).singleResult();
        repositoryService.deleteDeployment(deploymentId, cascade);
        // if(null!=model){
        // model.setDeploymentId("");
        // repositoryService.saveModel(model);
        // }

    }

    /**
     * 
     */
    @Override
    public long countDeployment(String templateName) {
        if (null != templateName && !"".equals(templateName)) {
            return repositoryService.createProcessDefinitionQuery().processDefinitionName(templateName).count();
        } else {
            return repositoryService.createProcessDefinitionQuery().count();
        }
    }

    /**
     * 获取配置文件
     * 
     * @param vo
     * @throws Exception
     * @throws WorkFLowException
     * @throws WorkFlowContextException
     */
    @Override
    public ModelExportVO readResourceStream(String deploymentId, String type) throws BpmException {
        ModelExportVO mev = new ModelExportVO();
        if (deploymentId == null) {
            throw new BpmException("deployMentId为空");
        }

        ProcessDefinition pd = repositoryService.getProcessDefinition(deploymentId);
        String fileName = "";
        InputStream inputStream = null;
        if (type.equals("bpmn")) {
            inputStream = repositoryService.getProcessModel(deploymentId);
            fileName = pd.getResourceName();
        } else {
            inputStream = repositoryService.getProcessDiagram(deploymentId);
            fileName = pd.getDiagramResourceName();
        }

        byte[] bb;
        try {
            bb = inputStreamToByte(inputStream);
        } catch (IOException e) {
            throw new BpmException("readResourceStream is error!", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // throw new BpmException("readResourceStream is error!",e);
            }
        }

        mev.setProcessDef(bb);
        mev.setProcessName(fileName);
        return mev;
    }

    private byte[] inputStreamToByte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in_b = swapStream.toByteArray();
        swapStream.close();
        return in_b;
    }

    /**
     * 
     * @param deployId
     * @return
     */
    private ProcessDefinition getProcessDefinitionInfoByDeployId(String deployId) {
        ProcessDefinition pd = null;
        pd = repositoryService.getProcessDefinition(deployId);
        // .createProcessDefinitionQuery().deploymentId(deployId).orderByProcessDefinitionVersion().desc().singleResult();
        return pd;
    }

    /**
     * 
     */
    public InputStream readPictrueStreamForPdf(String pdfId) throws BpmException {
        return repositoryService.getProcessDiagram(pdfId);
    }

    /**
     * 
     */
    public InputStream readPictrueStream(String modelId) throws BpmException {
        byte[] bb = repositoryService.getModelEditorSourceExtra(modelId);
        if (null == bb) {
            throw new BpmException("show Pictrue error,Model's Editor SourceExtra is null!");
        }
        return new ByteArrayInputStream(bb);
    }

    /**
     * 
     * @param taskId
     * @param processId
     * @return
     * @throws BpmException
     */
    @Override
    public InputStream readPictrueStream(String taskId, String processId) throws BpmException {

        ProcessInstance pi = null;
        // HistoricActivityInstance hpi = null;
        String processDefinitionId = null;

        if (processId != null && !"".equals(processId)) {
            pi = this.queryProcessInstance(processId);
            if (null != pi) {
                processDefinitionId = pi.getProcessDefinitionId();
            } else {
                HistoricProcessInstance hp = this.queryHisProcessInstance(processId);
                processDefinitionId = hp.getProcessDefinitionId();
            }

        } else {
            Task task = null;
            if (taskId != null && !"".equals(taskId)) {
                task = taskService.createTaskQuery().taskId(taskId).singleResult();
                if (null != task) {
                    pi = this.queryProcessInstance(task.getExecutionId());
                    processDefinitionId = pi.getProcessDefinitionId();
                }
            }
            if (null == task) {
                HistoricTaskInstance taskh = null;
                if (taskId != null && !"".equals(taskId)) {
                    // IHistoryService historyService
                    // =workFlowEngine.getHistoryService();
                    taskh = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                    pi = this.queryProcessInstance(taskh.getExecutionId());
                    if (null == pi) {
                        HistoricProcessInstance hp = this.queryHisProcessInstance(processId);
                        processDefinitionId = hp.getProcessDefinitionId();
                    } else {
                        processDefinitionId = pi.getProcessDefinitionId();
                    }
                }
            }
        }
        ProcessDefinition processDefinition = this.getProcessDefinitionInfoByPId(processDefinitionId);
        LOGGER.debug("----processDefinition====" + processDefinition);
        LOGGER.debug("----processDefinition====" + processDefinition.getName());
        return repositoryService.getProcessDiagram(processDefinition.getDeploymentId());

    }

    /**
     * 
     * @param pdid
     * @return
     * @throws Exception
     */
    private ProcessDefinition getProcessDefinitionInfoByPId(String pdid) throws BpmException {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid)
                .orderByProcessDefinitionVersion().desc().list();

        if (null == list || list.isEmpty()) {
            throw new BpmException("ProcessDefinition not find by " + pdid);
        }
        ProcessDefinition pdf = list.get(0);
        return pdf;
    }

    /**
     * 
     */
    @Override
    public ProcessDefinition findProcessDefinitonsByDid(String deployMentId) {
        return repositoryService.createProcessDefinitionQuery().deploymentId(deployMentId).singleResult();
    }

    /**
     * 
     */
    @Override
    public ActDefinitionPage findProcessModels(String templateName, Page _page) throws BpmException {
        long count = 0;
        ActDefinitionPage adp = new ActDefinitionPage();
        List<Model> list = new ArrayList<>();
        ;
        if (null == _page) {
            if (StringUtils.isEmpty(templateName)) {
                list = repositoryService.createModelQuery().list();
            } else {
                list = repositoryService.createModelQuery().modelName(templateName).list();
            }

        } else {
            if (StringUtils.isEmpty(templateName)) {
                list = repositoryService.createModelQuery().listPage(_page.getStart_index(), _page.getPageSize());
            } else {
                list = repositoryService.createModelQuery().modelName(templateName).listPage(_page.getStart_index(),
                        _page.getPageSize());
            }
        }

        for (Model model : list) {
            List<ProcessDefinition> li = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(model.getKey()).list();
            if (null != li && li.isEmpty()) {
                model.setDeploymentId("1");
            }
        }
        count = this.countDeployment(templateName);
        _page.setTotalRecord(count);
        adp.setActDefinitionVOs(pottingList(list));
        adp.setPage(_page);
        return adp;
    }

    /**
     * 
     */
    @Override
    public ActDefinitionPage findProcessDefinitons(String templateName, Page _page) throws BpmException {
        long count = 0;
        ActDefinitionPage adp = new ActDefinitionPage();
        List<ProcessDefinition> list = new ArrayList<>();
        ;
        if (null == _page) {
            if (StringUtils.isEmpty(templateName)) {
                list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
            } else {
                list = repositoryService.createProcessDefinitionQuery().latestVersion()
                        .processDefinitionName(templateName).list();
            }

        } else {
            if (StringUtils.isEmpty(templateName)) {
                list = repositoryService.createProcessDefinitionQuery().latestVersion().listPage(_page.getStart_index(),
                        _page.getPageSize());
            } else {
                list = repositoryService.createProcessDefinitionQuery().latestVersion()
                        .processDefinitionName(templateName).listPage(_page.getStart_index(), _page.getPageSize());
            }
        }

        count = this.countDeployment(templateName);
        _page.setTotalRecord(count);
        List<ActDefinitionVO> actDefinitionVOs = pottingList(list);
        for (ActDefinitionVO ad : actDefinitionVOs) {
            long keysCount = repositoryService.createProcessDefinitionQuery().processDefinitionKey(ad.getKey()).count();
            ad.setKeysCount(keysCount);
            Deployment dp = repositoryService.createDeploymentQuery().deploymentId(ad.getDeploymentId()).singleResult();
            ad.setDeploymentTime(dp.getDeploymentTime());
        }
        adp.setActDefinitionVOs(actDefinitionVOs);
        adp.setPage(_page);
        return adp;
    }

    /**
     * 
     * @param list
     * @return
     * @throws BpmException
     */
    // private List<ActDefinitionVO> pottingList(List<Model> list) throws
    // BpmException {
    // List<ActDefinitionVO> l = new ArrayList<ActDefinitionVO>();
    // for (Model pd : list) {
    // ActDefinitionVO av = new ActDefinitionVO();
    // try {
    // PropertyUtils.copyProperties(av, pd);
    // } catch (IllegalAccessException e) {
    // throw new BpmException(e.getMessage());
    // } catch (InvocationTargetException e) {
    // throw new BpmException(e.getMessage());
    // } catch (NoSuchMethodException e) {
    // throw new BpmException(e.getMessage());
    // }
    // l.add(av);
    // }
    // return l;
    // }

    /**
     * 
     * @param <T>
     * @param list
     * @return
     * @throws BpmException
     */
    private <T> List<ActDefinitionVO> pottingList(List<T> list) throws BpmException {
        List<ActDefinitionVO> l = new ArrayList<ActDefinitionVO>();
        for (T pd : list) {
            ActDefinitionVO av = new ActDefinitionVO();
            try {
                PropertyUtils.copyProperties(av, pd);
            } catch (IllegalAccessException e) {
                throw new BpmException(e);
            } catch (InvocationTargetException e) {
                throw new BpmException(e);
            } catch (NoSuchMethodException e) {
                throw new BpmException(e);
            }
            l.add(av);
        }
        return l;
    }

    /**
     * 找到历史活动节点的位置
     * 
     * @param taskId
     * @return
     * @throws Exception
     * @throws WorkFLowException
     * @throws WorkFlowContextException
     */
    @Override
    public ActiveNodes findActivityPositionHis(String taskId, String processId) throws BpmException {
        ActiveNodes activeNodes = new ActiveNodes();

        List<String> activityIds = new ArrayList<>();

        HistoricTaskInstance taskh = null;

        ProcessInstance pi = null;

        Task task = null;
        if (taskId != null && !"".equals(taskId)) {
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (null != task) {
                pi = this.queryProcessInstance(task.getExecutionId());
            } else if (task == null) {
                taskh = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                if (null != taskh) {
                    pi = this.queryProcessInstance(taskh.getExecutionId());
                } else {
                    throw new BpmException("taskId:" + taskId + " can't find entity");
                }
            }
        } else if (processId != null && !"".equals(processId)) {
            pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        } else {
            throw new BpmException("taskId and  ProcessId is null!");
        }

        if (processId == null || "".equals(processId)) {
            processId = task.getExecutionId();
        }

        // ProcessDefinitionEntity pd = (ProcessDefinitionEntity)
        // repositoryService
        // .getProcessDefinition(pi.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        // org.flowable.bpmn.model.Process process =
        // bpmnModel.getProcesses().get(0);
        pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).list();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        for (String id : activityIds) {
            // FlowElement fe = process.getFlowElement(id);
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(id);
            ActiveNode pp = new ActiveNode(null, graphicInfo.getX(), graphicInfo.getY(), graphicInfo.getWidth(),
                    graphicInfo.getHeight());
            activeNodes.getCurrentactiveNodes().add(pp);
        }

        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                // 过滤条件
                .processInstanceId(processId)
                // 分页条件
                // .listPage(firstResult, maxResults)
                // 排序条件
                .orderByHistoricActivityInstanceEndTime().asc()
                // 执行查询
                .list();

        for (HistoricActivityInstance hai : hais) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(hai.getActivityId());
            ActiveNode pp = new ActiveNode(null, graphicInfo.getX(), graphicInfo.getY(), graphicInfo.getWidth(),
                    graphicInfo.getHeight());
            activeNodes.getActiveNodes().add(pp);
        }

        return activeNodes;
    }

    /**
     * 找到活动节点的位置
     * 
     * @param taskId
     * @return
     * @throws Exception
     * @throws WorkFLowException
     * @throws WorkFlowContextException
     */
    @Override
    public ActiveNodes findActivityPosition(String taskId, String processId) throws BpmException {
        ActiveNodes activeNodes = new ActiveNodes();

        List<String> activityIds = new ArrayList<>();

        HistoricTaskInstance taskh = null;

        ProcessInstance pi = null;

        Task task = null;
        if (taskId != null && !"".equals(taskId)) {
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (null != task) {
                pi = this.queryProcessInstance(task.getExecutionId());
            } else if (task == null) {
                taskh = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                if (null != taskh) {
                    pi = this.queryProcessInstance(taskh.getExecutionId());
                } else {
                    throw new BpmException("taskId:" + taskId + " can't find entity");
                }
            }
        } else if (processId != null && !"".equals(processId)) {
            pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        } else {
            throw new BpmException("taskId and  ProcessId is null!");
        }

        if (processId == null || "".equals(processId)) {
            processId = task.getExecutionId();
        }

        // ProcessDefinitionEntity pd = (ProcessDefinitionEntity)
        // repositoryService
        // .getProcessDefinition(pi.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).list();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        for (String id : activityIds) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(id);
            ActiveNode pp = new ActiveNode(null, graphicInfo.getX(), graphicInfo.getY(), graphicInfo.getWidth(),
                    graphicInfo.getHeight());
            activeNodes.getCurrentactiveNodes().add(pp);
        }

        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                // 过滤条件
                .processInstanceId(processId)
                // 分页条件
                // .listPage(firstResult, maxResults)
                // 排序条件
                .orderByHistoricActivityInstanceEndTime().asc()
                // 执行查询
                .list();

        for (HistoricActivityInstance hai : hais) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(hai.getActivityId());
            ActiveNode pp = new ActiveNode(null, graphicInfo.getX(), graphicInfo.getY(), graphicInfo.getWidth(),
                    graphicInfo.getHeight());
            activeNodes.getActiveNodes().add(pp);
        }

        return activeNodes;
    }

    /**
     * 
     */
    @Override
    public ProcessInstance queryProcessInstance(String _pid) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId(_pid).list();
        if (null == list || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 
     * @param _pid
     * @return
     */
    public HistoricProcessInstance queryHisProcessInstance(String _pid) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(_pid)
                .list();
        if (null == list || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 
     */
    @Override
    public String createModel(String name, String key, String description) throws BpmException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        org.flowable.engine.repository.Model modelData = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        String description_s = StringUtils.defaultString(description);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description_s);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(name);
        modelData.setKey(StringUtils.defaultString(key));

        repositoryService.saveModel(modelData);
        try {
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BpmException("createModel error", e);
        }

        return modelData.getId();
    }

    /**
     * 
     */
    @Override
    public ModelExportVO export(String modelId, String type) throws BpmException {
        ModelExportVO mev = new ModelExportVO();
        org.flowable.engine.repository.Model modelData = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

        JsonNode editorNode;
        try {
            editorNode = new ObjectMapper().readTree(modelEditorSource);
        } catch (IOException e) {
            throw new BpmException("export Model error", e);
        }
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

        if (bpmnModel.getMainProcess() == null) {
            throw new BpmException("no main process, can't export for type: " + type);
        }

        String filename = "";
        byte[] exportBytes = null;

        String mainProcessId = bpmnModel.getMainProcess().getId();

        if ("bpmn".equals(type)) {

            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            exportBytes = xmlConverter.convertToXML(bpmnModel);

            filename = mainProcessId + ".bpmn20.xml";
        } else if ("json".equals(type)) {

            exportBytes = modelEditorSource;
            filename = mainProcessId + ".json";

        }
        mev.setProcessDef(exportBytes);
        mev.setProcessName(filename);
        return mev;
    }

    @Override
    public ModelExportVO exportAllTemplate(HttpServletRequest request) throws BpmException {
        ModelExportVO mev = new ModelExportVO();
        javax.servlet.ServletContext sc = request.getSession().getServletContext();
        String absoultePath = sc.getRealPath("template");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String time = sf.format(now);
        String fileName = time + ".zip";

        byte[] b = processCustomService.getAllTemplateStream(absoultePath, fileName);
        mev.setProcessDef(b);
        mev.setProcessName(fileName);
        return mev;
    }

}
