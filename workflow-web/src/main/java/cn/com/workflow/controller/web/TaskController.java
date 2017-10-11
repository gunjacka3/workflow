package cn.com.workflow.controller.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.workflow.common.WorkFlowContent;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActFromVO;
import cn.com.workflow.common.vo.ActiveNode;
import cn.com.workflow.common.vo.SelectUserInfos;
import cn.com.workflow.service.TaskService_;
import cn.com.workflow.user.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 工作任务控制器 Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin 2017年9月22日 下午3:40:40
 *
 */
@Controller
@RequestMapping(value = "/task")
@Api(value = "/task", tags = "任务服务类接口")
public class TaskController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

    @Autowired(required = true)
    private TaskService_ taskService_;
    /**
     * 默认编码
     */
    private static final String DEFAULT_DECODE = "UTF-8";

    /**
     * 任务领取
     * 
     * @param taskId
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午3:59:14
     */
    @RequestMapping(value = "/handle/{taskId}")
    @ApiOperation(value = "handleTask", notes = "根据任务id获取任务信息", httpMethod = "GET", response = TaskController.class)
    public ModelAndView handleTask(@PathVariable("taskId") String taskId, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("handleTask taskId===={}", taskId);
        String message = "";
        ModelAndView modelAndView = new ModelAndView();
        ActFromVO af = taskService_.findTaskFormInfo(taskId);
        SelectUserInfos users = null;
        try {
            users = taskService_.findNextActivityUserInfo(taskId);
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }

        if (StringUtils.isBlank(af.getFromKey())) {
            model.addAttribute("actFromInfos", af.getInsideFroms());
            model.addAttribute("actActions", af.getActions());
            model.addAttribute("variables", af.getVariables());
            model.addAttribute("taskId", af.getTaskId());
            model.addAttribute("opinion", af.getOpinion());
            model.addAttribute("nextUsers", users);
            model.addAttribute("message", message);
            modelAndView.setViewName("/bootstrap3/task/submitForm.jsp");
            return modelAndView;
        } else {
            modelAndView.setViewName(af.getFromKey());
            return modelAndView;
        }
    }

    /**
     * 任务批量领取
     * 
     * @param taskIds
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午3:59:57
     */
    @RequestMapping(value = "/handleBatch/{taskIds}")
    @ApiOperation(value = "handleBatchTask", notes = "任务批量领取,格式:id1,id2", httpMethod = "POST", response = TaskController.class)
    public ModelAndView handleBatchTask(@PathVariable("taskIds") String taskIds, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("taskIds===={}", taskIds);
        String[] tis = taskIds.split(",");
        String taskId = tis[0].substring(0, tis[0].indexOf('@'));
        String message = "";

        ModelAndView modelAndView = new ModelAndView();
        ActFromVO af = taskService_.findTaskFormInfo(taskId);
        SelectUserInfos users = null;
        try {
            users = taskService_.findNextActivityUserInfo(taskId);
        } catch (BpmException e) {
            throw new BpmException(e.getMessage(), e);
        }

        if (StringUtils.isBlank(af.getFromKey())) {
            model.addAttribute("actFromInfos", af.getInsideFroms());
            model.addAttribute("actActions", af.getActions());
            model.addAttribute("variables", af.getVariables());
            model.addAttribute("taskId", af.getTaskId());
            model.addAttribute("opinion", af.getOpinion());
            model.addAttribute("nextUsers", users);
            model.addAttribute("message", message);
            modelAndView.setViewName("/bootstrap3/task/submitForm.jsp");
            return modelAndView;
        } else {
            modelAndView.setViewName(af.getFromKey());
            return modelAndView;
        }
    }

    /**
     * 任务取回
     * 
     * @param taskId
     * @param user
     * @param request
     * @param response
     * @param model
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:00:13
     */
    @RequestMapping(value = "/retrieve/{taskId}")
    @ResponseBody
    @ApiOperation(value = "retrieveTask", notes = "根据任务id取回任务", httpMethod = "POST", response = TaskController.class)
    public void retrieveTask(@PathVariable("taskId") String taskId, @ModelAttribute("user") Users user,
            HttpServletRequest request, HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("retrieveTask taskId===={}", taskId);
        // Map<String, Object> variables = new HashMap<String, Object>();
        taskService_.retrieveTask(taskId, user.getUserCd(), null);
    }

    /**
     * 任务转派
     * 
     * @param taskId
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:00:33
     */
    @RequestMapping(value = "/turn/{taskId}")
    @ApiOperation(value = "turn", notes = "根据任务id转派任务", httpMethod = "POST", response = TaskController.class)
    public ModelAndView turn(@PathVariable("taskId") String taskId, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("turn taskId===={}", taskId);
        ModelAndView modelAndView = new ModelAndView();
        ActFromVO af = taskService_.findTaskFormInfo(taskId);
        if (StringUtils.isBlank(af.getFromKey())) {
            model.addAttribute("actFromInfos", af.getInsideFroms());
            model.addAttribute("actActions", af.getActions());
            model.addAttribute("variables", af.getVariables());
            model.addAttribute("taskId", af.getTaskId());
            modelAndView.setViewName("/bootstrap3/task/turnForm.jsp");
            return modelAndView;
        } else {
            modelAndView.setViewName(af.getFromKey());
            return modelAndView;
        }
    }

    /**
     * 任务代理
     * 
     * @param taskId
     * @param parms
     * @param user
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:00:54
     */
    @RequestMapping(value = "/delegate/{taskId}")
    @ResponseBody
    @ApiOperation(value = "delegateTask", notes = "根据任务id和用户信息设置任务代理", httpMethod = "POST", response = TaskController.class)
    public void delegateTask(@PathVariable("taskId") String taskId, @RequestBody String parms,
            @ModelAttribute("user") Users user, HttpServletRequest request, HttpServletResponse response)
            throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }

            // variables.put("userId", user.getUserCd());
            LOGGER.debug("delegateTask variables:{}", variables);

            taskService_.delegateTask(taskId, variables);
        } catch (Exception e) {
            LOGGER.error("delegateTask 任务保存失败:", e);
            throw new BpmException("delegateTask 任务保存失败:", e);
        }
    }

    /**
     * 取消任务退回共享池
     * 
     * @param taskId
     * @param parms
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:10:17
     */
    @RequestMapping(value = "/replace/{taskId}")
    @ResponseBody
    @ApiOperation(value = "replaceTask", notes = "取消已领取的任务将其退回共享池", httpMethod = "POST", response = TaskController.class)
    public void replaceTask(@PathVariable("taskId") String taskId, @RequestBody String parms,
            HttpServletRequest request, HttpServletResponse response) throws BpmException {
        LOGGER.debug("taskId===={}", taskId);
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }
            taskService_.replaceTask(taskId, variables);

        } catch (Exception e) {
            LOGGER.error("replaceTask 任务处理失败:", e);
            throw new BpmException("replaceTask 任务处理失败:", e);
        }
    }

    /**
     * 批量提交任务
     * 
     * @param taskIds
     * @param parms
     * @param user
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:10:33
     */
    @RequestMapping(value = "/batch/{taskIds}")
    @ResponseBody
    @ApiOperation(value = "submitBatch", notes = "根据任务id批量提交任务,格式为:id1,id2...", httpMethod = "POST", response = TaskController.class)
    public void submitBatch(@PathVariable("taskIds") String taskIds, @RequestBody String parms,
            @ModelAttribute("user") Users user, HttpServletRequest request, HttpServletResponse response)
            throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }
            variables.put("userId", user.getUserCd());
            LOGGER.debug("submitBatch variables:{}", variables);
            String[] temps = taskIds.split(",");
            List<String> taskIdList = new ArrayList<>();

            String procDefId = "";

            for (String str : temps) {
                if (StringUtils.isBlank(procDefId)) {
                    procDefId = str.substring(str.indexOf('@') + 1);
                } else if (!procDefId.equals(str.substring(str.indexOf('@') + 1))) {
                    throw new BpmException("批量提交流程模板不一致,不能批量提交");
                }
                taskIdList.add(str.substring(0, str.indexOf('@')));
            }

            taskService_.submitBatch(taskIdList, variables);
        } catch (Exception e) {
            LOGGER.error("submitBatch 任务提交失败", e);
            throw new BpmException(e.getMessage(), e);
        }
    }

    /**
     * 提交任务
     * 
     * @param taskId
     * @param parms
     * @param user
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:10:49
     */
    @RequestMapping(value = "/submit/{taskId}")
    @ResponseBody
    @ApiOperation(value = "submitForForm", notes = "根据任务id提交任务", httpMethod = "POST", response = TaskController.class)
    public void submitForForm(@PathVariable("taskId") String taskId, @RequestBody String parms,
            @ModelAttribute("user") Users user, HttpServletRequest request, HttpServletResponse response)
            throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }
            String submitType = (String) variables.get("submitType");
            variables.remove("variables");
            variables.put("userId", user.getUserCd());
            LOGGER.debug("submitForForm variables:{}", variables);
            if (submitType.equals(WorkFlowContent.WF_TASK_SUBMIT_Type_COMPLETE)) {
                taskService_.submitNomalTask(taskId, variables);
            } else if (submitType.equals(WorkFlowContent.WF_TASK_SUBMIT_Type_CANCEL)) {
                taskService_.cancelProcessByTaskId(taskId);
            }
        } catch (Exception e) {
            LOGGER.error("submitForForm 任务提交失败", e);
            throw new BpmException("submitForForm 任务提交失败", e);
        }
    }

    /**
     * 提交退回
     * 
     * @param type
     * @param taskId
     * @param parms
     * @param user
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:11:01
     */
    @RequestMapping(value = "/submitByBack/{type}/{taskId}")
    @ResponseBody
    @ApiOperation(value = "submitByBack", notes = "根据任务id提交需退回的任务", httpMethod = "POST", response = TaskController.class)
    public void submitByBack(@PathVariable("type") String type, @PathVariable("taskId") String taskId,
            @RequestBody String parms, @ModelAttribute("user") Users user, HttpServletRequest request,
            HttpServletResponse response) throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }

            variables.put("userId", user.getUserCd());
            LOGGER.debug("variables:{}", variables);

            taskService_.submitByBack(taskId, type, variables);
        } catch (Exception e) {
            LOGGER.error("submitByBack 任务提交失败:", e);
            throw new BpmException("submitByBack 任务提交失败:", e);
        }
    }

    /**
     * 临时保存任务
     * 
     * @param taskId
     * @param parms
     * @param user
     * @param request
     * @param response
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:11:12
     */
    @RequestMapping(value = "/save/{taskId}")
    @ResponseBody
    @ApiOperation(value = "save", notes = "根据任务id临时保存任务", httpMethod = "POST", response = TaskController.class)
    public void save(@PathVariable("taskId") String taskId, @RequestBody String parms,
            @ModelAttribute("user") Users user, HttpServletRequest request, HttpServletResponse response)
            throws BpmException {
        try {
            String parmsUTF8 = java.net.URLDecoder.decode(parms, DEFAULT_DECODE);
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.isNotBlank(parmsUTF8)) {
                variables = str2map(parmsUTF8);
            }

            // variables.put("userId", user.getUserCd());
            LOGGER.debug("variables:{}", variables);

            taskService_.saveTask(taskId, variables);
        } catch (Exception e) {
            LOGGER.error("save 任务保存失败:", e);
            throw new BpmException("save 任务保存失败:", e);
        }
    }

    /**
     * 查询下一个人工任务节点
     * 
     * @param taskId
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:11:21
     */
    @RequestMapping(value = "/findNextActivityUserInfo/{taskId}")
    @ApiOperation(value = "findNextActivityUserInfo", notes = "根据任务id查询下一个人工任务节点", httpMethod = "POST", response = TaskController.class)
    public ModelAndView findNextActivityUserInfo(@PathVariable("taskId") String taskId, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("taskId===={}", taskId);
        ModelAndView modelAndView = new ModelAndView();
        try {
            SelectUserInfos users = taskService_.findNextActivityUserInfo(taskId);

            model.addAttribute("nextUsers", users);
            // modelAndView.setViewName("/bootstrap3/task/selectOneUser.jsp");
        } catch (Exception e) {
            LOGGER.error("findNextActivityUserInfo 创建模型失败:", e);
            throw new BpmException("findNextActivityUserInfo 创建模型失败:", e);
        }
        return modelAndView;
    }

    /**
     * 读取节点
     * 
     * @param taskId
     * @param response
     * @param model
     * @return
     * @throws Exception
     * @author wangzhiyin 2017年9月22日 下午4:11:47
     */
    @RequestMapping(value = "/readActity/{taskId}")
    @ApiOperation(value = "readActity", notes = "根据任务id读取当前节点信息", httpMethod = "POST", response = TaskController.class)
    public ModelAndView readActity(@PathVariable("taskId") String taskId, HttpServletResponse response, Model model)
            throws Exception {
        List<ActiveNode> list4 = new ArrayList<>();
        // List<ActiveNode> list1= taskService.readActity(taskId);
        List<ActiveNode> list2 = taskService_.readHisActity(taskId);

        // List<ActivityImpl> list3= taskService.readAllActity(taskId);
        ModelAndView modelAndView = new ModelAndView();
        // model.addAttribute("actitys", list1);
        model.addAttribute("actitysHis", list2);
        model.addAttribute("actitysService", list4);
        // model.addAttribute("actitysAll", list3);
        model.addAttribute("taskId", taskId);
        modelAndView.setViewName("/operation/worklist/show.jsp");
        return modelAndView;
    }

    /**
     * 读取流程图资源文件
     * 
     * @param taskId
     * @param response
     * @throws Exception
     * @author wangzhiyin 2017年9月22日 下午4:12:02
     */
    @RequestMapping(value = "/readResource/{taskId}")
    @ApiOperation(value = "readResource", notes = "根据任务id读取流程图资源文件", httpMethod = "POST", response = TaskController.class)
    public void readResource(@PathVariable("taskId") String taskId, HttpServletResponse response) throws Exception {

        InputStream imageStream = taskService_.readRunResource(taskId);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
