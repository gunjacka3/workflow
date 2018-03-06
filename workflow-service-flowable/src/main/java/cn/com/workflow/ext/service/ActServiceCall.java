package cn.com.workflow.ext.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.common.impl.javax.el.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.util.JsonUtil;

public class ActServiceCall implements JavaDelegate {
    static Logger logger = LogManager.getLogger();

    private static Map<String, Client> map = new HashMap<>();

    private Expression input_field;

    private Expression method;

    private Expression wsdl;

    private Expression output_field;

    private int lastRunTimes = 3;

    private int sleeptime = 1000;

    @Override
    public void execute(DelegateExecution execution){


        String input = findResourceFormVariables(execution);
        String outJson = "";
        String errorMessage = "";
        boolean callSuccess = false;
        do {
            try {
                outJson = call(execution, input);
                callSuccess = true;// 调用成功
                lastRunTimes = 0;// 执行成功，则无需重试，直接退出
            } catch (Exception e) {
                logger.error(e);
                lastRunTimes--;// 可以重试的次数-1
                errorMessage = e.getMessage();
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }// 延迟指定的毫秒数
            }
        } while (lastRunTimes > 0);

        if (!callSuccess)// 重试N次后仍然失败
        {
            try {
                throw new BpmException(errorMessage);
            } catch (BpmException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }

        if ("Fault.faultcode".equals(outJson)) {
            throw new RuntimeException(outJson);
        }

        saveVariableFromReturnValue(execution, outJson);

        logger.debug("ActServiceCall end...");
    }

    /**
     * webservice 调用
     * 
     * @param execution
     * @param _json
     * @return
     * @throws Exception
     */
    private String call(DelegateExecution execution, String _json) throws Exception {
        String wsdlUrl =  wsdl.getExpressionString().trim();
        Client cl;
        if (null == map.get(wsdlUrl)) {
            JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
            cl = factory.createClient(wsdlUrl);
            map.put(wsdlUrl, cl);
        } else {
            cl = map.get(wsdlUrl);
        }
        Object[] obj = cl.invoke((String) method.getExpressionString(), new Object[] { _json });
        return obj[0].toString();
    }

    /**
     * 
     * 
     * @param execution
     * @return 
     * @author wangzhiyin
     *	       2017年9月29日 上午9:53:24
     */
    private String findResourceFormVariables(DelegateExecution execution) {

        StringBuilder json = new StringBuilder();
        String input = (String) input_field.getExpressionString();

        Map<String, Object> map = JsonUtil.getMap4Json(input);
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            if (StringUtils.isBlank(json.toString())) {
                json.append("{\"").append(entry.getKey()).append("\":\"").append(execution.getVariable(entry.getKey()).toString())
                        .append("\"");
            } else {
                json.append(",\"").append(entry.getKey()).append("\":\"").append(execution.getVariable(entry.getKey()).toString())
                        .append("\"");
            }
        }
        if (StringUtils.isNotBlank(json.toString())) {
            json.append("}");
        }
        return json.toString();
    }

    /**
     * 
     * 
     * @param execution
     * @param json 
     * @author wangzhiyin
     *	       2017年9月29日 上午9:53:31
     */
    private void saveVariableFromReturnValue(DelegateExecution execution, String json) {
        Map<String, Object> jsonMaps = JsonUtil.getMap4Json(json);
        String output = (String) output_field.getExpressionString();
        Map<String, Object> map = JsonUtil.getMap4Json(output);
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            execution.setVariable(entry.getKey(), jsonMaps.get(entry.getKey()));
        }

    }

    /**
     * 
     * 
     * @param execution
     * @throws BpmException 
     * @author wangzhiyin
     *	       2017年9月29日 上午9:53:39
     */
    private void checkParms(DelegateExecution execution) throws BpmException {
        if (null == input_field || null == input_field.getExpressionString()
                || StringUtils.isBlank((String) input_field.getExpressionString())) {
            logger.error("webservice call field: [input_field] is null,process Id is " + execution.getId());
            throw new BpmException("webservice call field: [input_field] is null");
        }
        if (null == method || null == method.getExpressionString()
                || StringUtils.isBlank((String) method.getExpressionString())) {
            logger.error("webservice call field: [method] is null,process Id is " + execution.getId());
            throw new BpmException("webservice call field: [method] is null");
        }
        if (null == wsdl || null == wsdl.getExpressionString()
                || StringUtils.isBlank((String) wsdl.getExpressionString())) {
            logger.error("webservice call field: [wsdl] is null,process Id is " + execution.getId());
            throw new BpmException("webservice call field: [wsdl] is null");
        }

    }

    public static Map<String, Client> getMap() {
        return map;
    }

    public static void setMap(Map<String, Client> map) {
        ActServiceCall.map = map;
    }

    public Expression getInput_field() {
        return input_field;
    }

    public void setInput_field(Expression input_field) {
        this.input_field = input_field;
    }

    public Expression getMethod() {
        return method;
    }

    public void setMethod(Expression method) {
        this.method = method;
    }

    public Expression getWsdl() {
        return wsdl;
    }

    public void setWsdl(Expression wsdl) {
        this.wsdl = wsdl;
    }

    public Expression getOutput_field() {
        return output_field;
    }

    public void setOutput_field(Expression output_field) {
        this.output_field = output_field;
    }

    public int getLastRunTimes() {
        return lastRunTimes;
    }

    public void setLastRunTimes(int lastRunTimes) {
        this.lastRunTimes = lastRunTimes;
    }

    public int getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(int sleeptime) {
        this.sleeptime = sleeptime;
    }

    @Override
    public String toString() {
        return "ActServiceCall [input_field=" + input_field + ", method=" + method + ", wsdl=" + wsdl
                + ", output_field=" + output_field + ", lastRunTimes=" + lastRunTimes + ", sleeptime=" + sleeptime
                + "]";
    }

}
