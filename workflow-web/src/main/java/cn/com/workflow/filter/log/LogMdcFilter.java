package cn.com.workflow.filter.log;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import cn.com.workflow.controller.web.HisProcessController;
import cn.com.workflow.user.Users;
/**
 * log封装mdc处理类
 * Package : cn.com.workflow.filter.log
 * 
 * @author wangzhiyin
 *		   2017年9月25日 上午9:53:02
 *
 */
public class LogMdcFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(LogMdcFilter.class); 

	private final static String DEFAULT_USERID = "anonymous";
	
	private final static String KEY_USERID = "userId";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		return;
	}
	
	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException 
	 * @author wangzhiyin
	 *	       2017年9月25日 上午9:54:35
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		if (session == null) {
			MDC.put(KEY_USERID, DEFAULT_USERID);
		} else {
			Object object = session.getAttribute("user");
			if (object == null) {
				MDC.put(KEY_USERID, DEFAULT_USERID);
			} else {
				Users user = (Users) session.getAttribute("user");
				LOGGER.debug("test for MDC.userId :{}", user);
				if (user == null) {
					MDC.put(KEY_USERID, DEFAULT_USERID);
				} else {
					MDC.put(KEY_USERID, user.getUserCd());
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		return;
	}

}
