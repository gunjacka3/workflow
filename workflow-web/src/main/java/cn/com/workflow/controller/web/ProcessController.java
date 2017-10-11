package cn.com.workflow.controller.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActDefinitionPage;
import cn.com.workflow.common.vo.ModelExportVO;
import cn.com.workflow.service.ProcessDefinitionService;
import cn.com.workflow.service.ProcessService;
import cn.com.workflow.user.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 流程实例控制器 Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin 2017年9月22日 下午4:25:17
 *
 */
@Controller
@RequestMapping(value = "/process")
@Api(value = "/process", tags = "流程定义服务类接口")
public class ProcessController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(ProcessController.class);

    @Autowired(required = true)
    private ProcessDefinitionService processDefinitionService;

    @Autowired(required = true)
    private ProcessService processService;

    /**
     * bpm model处理页面初始化
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     */
    @RequestMapping(value = "/initDefinition")
    @ApiOperation(value = "init", notes = "流程定义列表页面初始化", httpMethod = "POST", response = ProcessController.class)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, Model model)
            throws BpmException {
        ActDefinitionPage adp = null;
        try {
            adp = processDefinitionService.findProcessDefinitons("", new Page());
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }

        model.addAttribute("templateName", "");
        model.addAttribute("templates", adp);
        model.addAttribute("page", adp.getPage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("../operation/processDef/definitionList.jsp");
        return modelAndView;
    }

    /**
     * bpm model页面列表查询
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     */
    @RequestMapping(value = "/showList")
    @ApiOperation(value = "showList", notes = "流程定义列表页面列表查询", httpMethod = "GET", response = ProcessController.class)
    public ModelAndView showList(HttpServletRequest request, HttpServletResponse response, Model model)
            throws BpmException {
        String pageNo = request.getParameter("pageNo");
        String templateName = request.getParameter("templateName");
        Page page = new Page();
        if (!StringUtils.isBlank(pageNo)) {
            page.setPageNo(new Integer(pageNo));
        }

        ActDefinitionPage adp = null;
        try {
            adp = processDefinitionService.findProcessDefinitons(templateName, page);
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }
        model.addAttribute("templateName", templateName);
        model.addAttribute("templates", adp);
        model.addAttribute("page", adp.getPage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("../../operation/processDef/definitionList.jsp");
        return modelAndView;
    }

    /**
     * 删除bpm model
     * 
     * @param deploymentId
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    @ApiOperation(value = "remove", notes = "删除已部署流程", httpMethod = "POST", response = ProcessController.class)
    public void remove(@RequestBody String deploymentId, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        LOGGER.debug("deploymentId====" + deploymentId);
        processDefinitionService.deleteDeployment(deploymentId, true);
    }

    /**
     * 展现 流程图
     * 
     * @param modelId
     * @param request
     * @param response
     * @param model
     * @throws BpmException
     */
    @RequestMapping(value = "/pic/{pefId}")
    @ResponseBody
    @ApiOperation(value = "pic", notes = "展现 流程图", httpMethod = "POST", response = ProcessController.class)
    public void pic(@PathVariable("pefId") String pefId, HttpServletRequest request, HttpServletResponse response,
            Model model) throws BpmException {
        InputStream is = processDefinitionService.readPictrueStreamForPdf(pefId);
        byte[] b = new byte[1024];
        int len = -1;
        ServletOutputStream sos;
        try {
            sos = response.getOutputStream();

            while ((len = is.read(b, 0, 1024)) != -1) {
                sos.write(b, 0, len);
            }

            sos.flush();
        } catch (IOException e) {
            throw new BpmException(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOGGER.error("展现 流程图异常:{}", e);
            }
        }

    }

    /**
     * 根据executionId触发
     * 
     * @param modelId
     * @param redirectAttributes
     * @throws BpmException
     */
    @RequestMapping(value = "/signalProcess/{executionId}")
    @ResponseBody
    @ApiOperation(value = "signalProcess", notes = "根据executionId触发流程", httpMethod = "POST", response = ProcessController.class)
    public void signalProcess(@PathVariable("executionId") String executionId, @RequestBody String parms,
            RedirectAttributes redirectAttributes) throws BpmException {
        Map<String, Object> variables = new HashMap<>();
        if (StringUtils.isNotBlank(parms)) {
            variables = str2map(parms);
        }
        processService.signalProcess(executionId, variables);
    }

    /**
     * 根据Model部署流程
     * 
     * @param modelId
     * @param redirectAttributes
     * @throws BpmException
     */
    @RequestMapping(value = "/deploy")
    @ResponseBody
    @ApiOperation(value = "deploy", notes = "根据ModelId部署流程", httpMethod = "POST", response = ProcessController.class)
    public void deploy(@RequestBody String modelId, RedirectAttributes redirectAttributes) throws BpmException {
        processDefinitionService.deploymentModel(modelId);
    }

    /**
     * 根据指定流程key发起流程实例
     * 
     * @param name
     * @param key
     * @param description
     * @param request
     * @param response
     * @throws BpmException
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @ApiOperation(value = "startProcessInstance", notes = "根据指定流程key发起流程实例", httpMethod = "POST", response = ProcessController.class)
    public void startProcessInstance(@RequestBody String parms, @ModelAttribute("user") Users user,
            HttpServletRequest request, HttpServletResponse response) throws BpmException {
        Map<String, Object> variables = str2map(parms);
        String key = (String) variables.get("pdfKey");
        variables.remove("pdfKey");
        String bizId = (String) variables.get("bizId");
        // variables.remove("bizId");
        LOGGER.debug("key:{}|bizId:{}|variables:{}", key, bizId, variables);

        try {
            String pid = processService.startProcess(user.getUserCd(), key, bizId, variables);
            LOGGER.debug("processService.startProcess====" + pid);
        } catch (Exception e) {
            LOGGER.error("创建模型失败：", e);
            throw new BpmException("创建模型失败：", e);
        }
    }

    /**
     * 导出bpm model对应的bpmn文件或json文件
     * 
     * @param modelId
     * @param type
     * @param response
     * @throws BpmException
     */
    @RequestMapping(value = "/export/{modelId}/{type}")
    @ResponseBody
    @ApiOperation(value = "export", notes = "导出bpm modelId对应的bpmn文件或json文件", httpMethod = "POST", response = ProcessController.class)
    public void export(@PathVariable("modelId") String modelId, @PathVariable("type") String type,
            HttpServletResponse response) throws BpmException {
        try {
            ModelExportVO mev = processDefinitionService.readResourceStream(modelId, type);
            ByteArrayInputStream in = new ByteArrayInputStream(mev.getProcessDef());
            IOUtils.copy(in, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + mev.getProcessName());
            response.flushBuffer();
        } catch (Exception e) {
            LOGGER.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
            throw new BpmException("导出模型失败：", e);
        }
    }

    /**
     * 上传bpmn文件
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     */
    @RequestMapping("/upload")
    @ApiOperation(value = "upload", notes = "上传bpmn文件", httpMethod = "POST", response = ProcessController.class)
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, Model model)
            throws BpmException {
        javax.servlet.ServletContext sc = request.getSession().getServletContext();
        String absoultePath = sc.getRealPath("template");
        LOGGER.debug("absoultePath====" + absoultePath);
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(sc);
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    // 取得当前上传文件的文件名称
                    InputStream io = null;
                    try {
                        io = file.getInputStream();
                        processDefinitionService.uploadProcess(io, file.getOriginalFilename());
                    } catch (IOException e) {
                        throw new BpmException("上传失败：", e);
                    } finally {
                        try {
                            if (null != io) {
                                io.close();
                            }
                        } catch (IOException e) {
                            LOGGER.error("上传bpmn文件异常:{}", e);
                        }
                    }

                }
                // 记录上传该文件后的时间
                int finaltime = (int) System.currentTimeMillis();
                LOGGER.debug("上传该文件的时间 :{}", finaltime - pre);
            }

        }
        return init(request, response, model);
    }

}
