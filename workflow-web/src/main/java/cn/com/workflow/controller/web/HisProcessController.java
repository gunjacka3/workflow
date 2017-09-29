package cn.com.workflow.controller.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActHisActInstListPage;
import cn.com.workflow.common.vo.ActHisProcessListPage;
import cn.com.workflow.common.vo.ActHisTaskListPage;
import cn.com.workflow.common.vo.condition.WorkListCondition;
import cn.com.workflow.service.QueryWorkListService;
import cn.com.workflow.user.Users;

/**
 * 历史流程实例控制器 Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin 2017年9月22日 下午4:38:14
 *
 */
@Controller
@RequestMapping(value = "/hisProcess")
public class HisProcessController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(HisProcessController.class);

    @Autowired(required = true)
    private QueryWorkListService queryWorkListService;

    /**
     * bpm model处理页面初始化
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     */
    @RequestMapping(value = "/initList")
    public ModelAndView init(@ModelAttribute("user") Users user, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("init");
        Map map = new HashMap();
        ActHisProcessListPage ahl = queryWorkListService.queryHisList(user.getUserCd(), map, new Page());
        model.addAttribute("templateName", "");
        model.addAttribute("templates", ahl);
        model.addAttribute("page", ahl.getPage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/bootstrap3/hisProcess/hisProList.jsp");
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
    @RequestMapping(value = "/showList", method = RequestMethod.POST)
    public ModelAndView showList(@ModelAttribute("user") Users user, WorkListCondition condition,
            HttpServletRequest request, HttpServletResponse response, Model model) throws BpmException {
        Map<String, Object> map = object2map(condition);
        String pageNo = request.getParameter("pageNo");
        String templateName = request.getParameter("templateName");

        if (StringUtils.isNotBlank(templateName)) {
            map.put("template", templateName);
        }

        Page page = new Page();
        if (!StringUtils.isBlank(pageNo)) {
            page.setPageNo(new Integer(pageNo));
        }
        ActHisProcessListPage ahl = queryWorkListService.queryHisList(user.getUserCd(), map, page);
        model.addAttribute("templateName", templateName);
        model.addAttribute("templates", ahl);
        model.addAttribute("page", ahl.getPage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/bootstrap3/hisProcess/hisProList.jsp");
        return modelAndView;
    }

    /**
     * 查询历史详细
     * 
     * @param taskId
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/view/{processId}")
    public ModelAndView handleTask(@PathVariable("processId") String processId, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("processId===={}", processId);
        ModelAndView modelAndView = new ModelAndView();
        ActHisActInstListPage hts = queryWorkListService.queryHisTaskList(processId, null, null);
        model.addAttribute("page", hts.getPage());
        model.addAttribute("hisTasks", hts);
        modelAndView.setViewName("/bootstrap3/hisProcess/hisTasks.jsp");
        return modelAndView;
    }

    /**
     * 查询最近的会议记录
     * 
     * @param taskId
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/viewMeeting/{processId}")
    public ModelAndView handleMeetingTask(@PathVariable("processId") String processId, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("processId===={}", processId);
        ModelAndView modelAndView = new ModelAndView();
        ActHisTaskListPage hts = queryWorkListService.queryHisMeetingTaskList(processId, null, null);
        model.addAttribute("page", hts.getPage());
        model.addAttribute("hisTasks", hts);
        modelAndView.setViewName("/bootstrap3/hisProcess/hisTasks.jsp");
        return modelAndView;
    }

}
