package cn.com.workflow.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;

public class QuartzContextListener implements ServletContextListener {  
	  
    /* 
     * 
     *  
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet. 
     * ServletContextEvent) 
     */  
    @Override  
    public void contextDestroyed(ServletContextEvent arg0) {  
        WebApplicationContext webApplicationContext = (WebApplicationContext) arg0  
                .getServletContext()  
                .getAttribute(  
                        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);  
        org.quartz.impl.StdScheduler startQuertz = (org.quartz.impl.StdScheduler) webApplicationContext  
                .getBean("schedulerFactoryBean");  
        if(startQuertz != null) {  
            startQuertz.shutdown();
            System.out.println("---Quertz.shutdown---");
        }  
//        try {  
//            Thread.sleep(1000);  
//        } catch (InterruptedException e) {  
//            e.printStackTrace();  
//        }  
    }  
  
    /* 
     * (non-Javadoc) 
     *  
     * @see 
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet 
     * .ServletContextEvent) 
     */  
    @Override  
    public void contextInitialized(ServletContextEvent arg0) {  
    	
    }  
  
}  
