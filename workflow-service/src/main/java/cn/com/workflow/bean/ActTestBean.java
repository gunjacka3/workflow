package cn.com.workflow.bean;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * bean方式调用服务bean类 Package : cn.com.workflow.bean
 * 
 * @author wangzhiyin 2017年9月26日 上午9:34:37
 *
 */
@Service("actTestBean")
public class ActTestBean {
    private static Logger log = LoggerFactory.getLogger(ActTestBean.class);

    /**
     * 
     * 
     * @param str
     * @return
     * @author wangzhiyin 2017年9月26日 上午9:35:05
     */
    public String print(String str) {
        log.debug("ActTestBean.print:[{}]", str);
        return "test:" + str;
    }

    /**
     * 
     * 
     * @param exc
     * @return
     * @author wangzhiyin 2017年9月26日 上午9:35:08
     */
    public String printBKey(DelegateExecution exc) {
        String key = exc.getProcessBusinessKey();
        log.debug("ActTestBean.printBKey:[{}]", key);
        return key;
    }

    /**
     * 
     * 
     * @param task
     * @author wangzhiyin 2017年9月26日 上午9:35:12
     */
    public void invokeTask(DelegateTask task) {
        task.setAssignee((String) task.getVariable("name"));
        task.setVariable("setByTask", task.getVariable("name"));
    }

    /**
     * 
     * 
     * @param time
     * @param execution
     * @throws Exception
     * @author wangzhiyin 2017年9月26日 上午9:35:15
     */
    public void sleepTask(String time, DelegateExecution execution) throws Exception {
        try {
            log.debug("sleep:[{}", time);
            Thread.sleep(Long.parseLong(time));
            log.debug("sleep:[{0}],ActTestBean.sleepTask:[{1}]", time, execution.getCurrentActivityName());
        } catch (NumberFormatException | InterruptedException e) {
            log.error(e.getMessage());
            throw new Exception(e);
        }
    }

    /**
     * 
     * 
     * @param execution
     * @return
     * @author wangzhiyin 2017年9月26日 上午9:35:20
     */
    public List<String> getUserList(DelegateExecution execution) {
        String ls = (String) execution.getVariable("assigneeList");
        String[] ll = ls.split(";");
        List<String> l = new ArrayList<>();
        for (String a : ll) {
            l.add(a);
        }
        return l;
    }

    // public String testRule(DelegateExecution execution){
    // return "1";
    // }
    //
    // public String testRule2(DelegateExecution execution){
    // execution.setVariable("lv", "0");
    // return "1";
    // }

    /**
     * 
     * 
     * @param execution
     * @author wangzhiyin 2017年9月26日 上午9:35:29
     */
    public void exceptionTask(DelegateExecution execution) {
        // try {
        // String str=null;
        // str.split(";");
        //
        // } catch (Exception e) {
        // if(null==e.getMessage()){
        // execution.setVariable("errMessage", e);
        // }else{
        // execution.setVariable("errMessage", e.getMessage());
        // }
        // e.printStackTrace();
        // throw new BpmnError("BPM-ERROR-001");
        //
        // }
    }

    /**
     * 
     * 
     * @param str
     * @author wangzhiyin 2017年9月26日 上午9:35:32
     */
    public void doError(String str) {
        // log.debug("**************************************************************************");
        // String str1=null;
        // str1.split(";");
    }
}
