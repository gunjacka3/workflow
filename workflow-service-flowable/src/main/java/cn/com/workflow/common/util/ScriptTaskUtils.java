package cn.com.workflow.common.util;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.scripting.ScriptingEngines;

/**
 * 调用activiti内部类实现js脚本节点计算工具类
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午10:08:10
 *
 */
public class ScriptTaskUtils {
    
    private static final String JAVASCRIPT = "javaScript";

    /**
     * 
     * 
     * @param javaScriptString
     * @param execution
     * @return 
     * @author wangzhiyin
     *	       2017年9月27日 上午10:11:18
     */
    public Object evaluate(String javaScriptString, DelegateExecution execution) {
        ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
        
        return scriptingEngines.evaluate(javaScriptString, JAVASCRIPT, execution);
    }
}
