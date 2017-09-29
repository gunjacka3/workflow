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
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

/**
 * 在线模板设计器操作控制器 Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin 2017年9月22日 下午4:29:18
 *
 */
@Controller
@RequestMapping(value = "/model")
public class ModelController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(ModelController.class);

    @Autowired(required = true)
    private ProcessDefinitionService processDefinitionService;

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
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, Model model)
            throws BpmException {
        ActDefinitionPage adp = null;
        try {
            adp = processDefinitionService.findProcessModels("", new Page());
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }

        model.addAttribute("templateName", "");
        model.addAttribute("templates", adp);
        model.addAttribute("page", adp.getPage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/bootstrap3/model/definitionList.jsp");
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
    @RequestMapping(value = "/showList", method = RequestMethod.GET)
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
            adp = processDefinitionService.findProcessModels(templateName, page);
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }
        model.addAttribute("templateName", templateName);
        model.addAttribute("templates", adp);
        model.addAttribute("page", adp.getPage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/bootstrap3/model/definitionList.jsp");
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
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@RequestBody String modelId, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        LOGGER.debug("modelId====" + modelId);
        processDefinitionService.deleteModel(modelId, false);
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
    @RequestMapping(value = "/pic/{modelId}")
    @ResponseBody
    public void pic(@PathVariable("modelId") String modelId, HttpServletRequest request, HttpServletResponse response,
            Model model) throws BpmException {
        InputStream is = processDefinitionService.readPictrueStream(modelId);
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
     * 根据Model部署流程
     * 
     * @param modelId
     * @param redirectAttributes
     * @throws BpmException
     */
    @RequestMapping(value = "/deploy")
    @ResponseBody
    public void deploy(@RequestBody String modelId, RedirectAttributes redirectAttributes) throws BpmException {
        processDefinitionService.deploymentModel(modelId);
    }

    /**
     * 通过activiti modeler新建流程图
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
    public String create(@RequestBody String parms, HttpServletRequest request, HttpServletResponse response)
            throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, "UTF-8");
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }
            String name = (String) variables.get("name");
            String key = (String) variables.get("key");
            String description = (String) variables.get("description");
            String modelId = processDefinitionService.createModel(name, key, description);
            LOGGER.debug("createModel: id{}", modelId);
            // response.sendRedirect(request.getContextPath() +
            // "/modeler.html?modelId=" + modelId);
            return modelId;
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
     */
    @RequestMapping(value = "/export/{modelId}/{type}")
    @ResponseBody
    public void export(@PathVariable("modelId") String modelId, @PathVariable("type") String type,
            HttpServletResponse response) {
        try {
            ModelExportVO mev = processDefinitionService.export(modelId, type);
            ByteArrayInputStream in = new ByteArrayInputStream(mev.getProcessDef());
            IOUtils.copy(in, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + mev.getProcessName());
            response.flushBuffer();
        } catch (Exception e) {
            LOGGER.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
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
                int pre = (int) System.currentTimeMillis();
                // 记录上传过程起始时的时间，用来计算上传时间
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

    // @Deprecated
    // @SuppressWarnings("unused")
    // public static void inputstreamTofile(InputStream ins,File file) {
    // try {
    // OutputStream os = new FileOutputStream(file);
    // int bytesRead = 0;
    // byte[] buffer = new byte[8192];
    // while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
    // os.write(buffer, 0, bytesRead);
    // }
    // os.close();
    // ins.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
