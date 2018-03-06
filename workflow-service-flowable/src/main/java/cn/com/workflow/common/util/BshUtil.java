package cn.com.workflow.common.util;

import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.util.condition.ConditionUtil;

import cn.com.workflow.common.exception.BpmException;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

/**
 * Bsh工具类:校验正则表达式(暂不使用) Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin 2017年9月26日 上午9:39:56
 *
 */
public class BshUtil {
    static Logger logger = LogManager.getLogger();

    /**
     * 
     * 
     * @param condition
     * @param variables
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月26日 上午9:40:47
     */
    public static boolean eval(String condition, Map<String, Object> variables) throws BpmException {
        try {
            logger.debug("condition: [{}]", condition);
            boolean flag = false;
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext(new SimpleResolver());
            for (String key : variables.keySet()) {
                if (condition.indexOf(key) > -1) {
                    logger.debug("{} setValue : {}'s {}", condition, key, variables.get(key));
                    factory.createValueExpression(context, "#{" + key + "}", String.class).setValue(context,
                            variables.get(key));
                }
            }

            ValueExpression expr1 = factory.createValueExpression(context, condition, boolean.class);
            flag = (boolean) expr1.getValue(context);
            logger.debug("flag: [{}]", flag);
            return flag;
        } catch (PropertyNotFoundException e) {
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BpmException("", e);
        }
        
    }
    
    /**
     * 通过flowable引擎工具类计算
     * 
     * @param sequenceFlow
     * @param execution
     * @return
     * @throws BpmException 
     * @author YixinCapital -- wangzhiyin
     *	       2017年11月7日 下午6:38:32
     */
    public static boolean evalByConditionUtil(SequenceFlow sequenceFlow, DelegateExecution execution) {
        return ConditionUtil.hasTrueCondition(sequenceFlow, execution);
    }

    /**
     * 获得字符串编码
     * 
     * @param str
     * @return
     * @author wangzhiyin 2017年9月26日 上午9:41:01
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception1) {
            logger.error(exception1);
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception2) {
            logger.error(exception2);
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception3) {
            logger.error(exception3);
        }
        return "";
    }

}
