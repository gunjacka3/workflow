package cn.com.workflow.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.workflow.common.vo.OSInfoVO;
import cn.com.workflow.service.SysInfoService;
import cn.com.workflow.user.Users;

/*
 * 完成了一个使用注解描述的Servlet程序开发。
　　使用@WebServlet将一个继承于javax.servlet.http.HttpServlet的类定义为Servlet组件。
　　@WebServlet有很多的属性：
    　　1、asyncSupported：    声明Servlet是否支持异步操作模式。
    　　2、description：　　    Servlet的描述。
    　　3、displayName：       Servlet的显示名称。
    　　4、initParams：        Servlet的init参数。
    　　5、name：　　　　       Servlet的名称。
    　　6、urlPatterns：　　   Servlet的访问URL。
    　　7、value：　　　        Servlet的访问URL。
　　Servlet的访问URL是Servlet的必选属性，可以选择使用urlPatterns或者value定义。
　　像上面的Servlet3Demo可以描述成@WebServlet(name="Servlet3Demo",value="/Servlet3Demo")。
　　也定义多个URL访问：
　　如@WebServlet(name="Servlet3Demo",urlPatterns={"/Servlet3Demo","/Servlet3Demo2"})
　　或者@WebServlet(name="AnnotationServlet",value={"/Servlet3Demo","/Servlet3Demo2"})
 *
 */
@WebServlet(description = "登录", urlPatterns = { "/LogServlet" })
public class LogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = new Users();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userCd = ((UserDetails) principal).getUsername();
            user.setUserCd(userCd);
            Iterator it = ((UserDetails) principal).getAuthorities().iterator();
            String authority = "";
            while (it.hasNext()) {
                authority = ((GrantedAuthority) it.next()).getAuthority();
                user.setPositionCd(authority);

            }
        }
        try {
            MDC.put("loginUserId", user.getUserCd());
            ServletContext servletContext = this.getServletContext();
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            SysInfoService sinfo = (SysInfoService) context.getBean("sysInfoServiceImpl");
            OSInfoVO osinfo = sinfo.getOSInfo();

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("osInfo", osinfo);
            // response.sendRedirect("operation/index.jsp");
            response.sendRedirect("pages/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearMDC();
        }

    }

    private void clearMDC() {
        MDC.clear();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = new Users();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userCd = ((UserDetails) principal).getUsername();
            user.setUserCd(userCd);
            // System.out.println("userCd==="+userCd);
            Iterator it = ((UserDetails) principal).getAuthorities().iterator();
            String authority = "";
            while (it.hasNext()) {
                authority = ((GrantedAuthority) it.next()).getAuthority();
                // System.out.println("Authority:"+authority);
                // System.out.println("authority==="+authority);
                user.setPositionCd(authority);

            }
        }

        try {
            MDC.put("loginUserId", user.getUserCd());
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("./operation/index.jsp");
        } finally {
            clearMDC();
        }

    }

}
