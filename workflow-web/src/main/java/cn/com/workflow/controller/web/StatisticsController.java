package cn.com.workflow.controller.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.chart.ActInstanceStatistcsEchartsVO;
import cn.com.workflow.service.StatisticsService;
import cn.com.workflow.user.Users;

/**
 * 系统信息报表控制器 Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin 2017年9月22日 下午4:13:49
 *
 */
@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(StatisticsController.class);

    @Autowired(required = true)
    private StatisticsService statisticsService;

    /**
     * 查询流程实例总数
     * 
     * @param user
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:14:08
     */
    @RequestMapping(value = "/findProcessInstanceCount")
    public ModelAndView findProcessInstanceCount(@ModelAttribute("user") Users user, HttpServletRequest request,
            HttpServletResponse response, Model model) throws BpmException {
        LOGGER.debug("findProcessInstanceCount");
        ActInstanceStatistcsEchartsVO vo = statisticsService.findProcessInstanceCount();
        model.addAttribute("instancesCount", vo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/bootstrap3/statistics/statisticsList.jsp");
        return modelAndView;
    }

}
