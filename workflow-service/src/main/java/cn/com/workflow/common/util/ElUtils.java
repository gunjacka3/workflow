package cn.com.workflow.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.impl.el.ActivitiElContext;
import org.activiti.engine.impl.javax.el.ArrayELResolver;
import org.activiti.engine.impl.javax.el.BeanELResolver;
import org.activiti.engine.impl.javax.el.CompositeELResolver;
import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.javax.el.ELResolver;
import org.activiti.engine.impl.javax.el.ListELResolver;
import org.activiti.engine.impl.javax.el.MapELResolver;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL工具类
 * 
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin 2017年9月27日 上午9:47:55
 *
 */
public class ElUtils {

    private static final Logger logger = LoggerFactory.getLogger(ElUtils.class);

    private ElUtils() {

    }

    ELContext context = null;
    ExpressionFactoryImpl f = new ExpressionFactoryImpl();

    /**
     * 
     * 
     * @param map
     * @return
     * @author wangzhiyin 2017年9月27日 上午9:48:22
     */
    public static ElUtils getInstance(Map<String, Object> map) {

        ElUtils el = new ElUtils();
        el.context = new ActivitiElContext(createElResolver());
        for (Entry<String, Object> entry : map.entrySet()) {
            el.setValue(entry.getKey(), entry.getValue());
        }
        return el;
    }

    /**
     * 
     * 
     * @return
     * @author wangzhiyin 2017年9月27日 上午9:48:27
     */
    protected static ELResolver createElResolver() {
        CompositeELResolver elResolver = new CompositeELResolver();
        elResolver.add(new RootResolver(new HashMap<>()));
        elResolver.add(new MapELResolver());
        elResolver.add(new ArrayELResolver());
        elResolver.add(new ListELResolver());
        elResolver.add(new BeanELResolver());
        return elResolver;
    }

    /**
     * 
     * 
     * @param expression
     * @return
     * @author wangzhiyin 2017年9月27日 上午9:48:30
     */
    public Object getValue(String expression) {
        try {
            return f.createValueExpression(context, expression, Object.class).getValue(context);
        } catch (Exception ex) {
            logger.error("没有找到对应的值 " + expression, ex);
            return null;
        }
    }

    /**
     * 自定义可以从EL表达式中可以获取的对象
     * 
     * @param name
     * @param obj
     * @param event
     */
    public void setValue(String name, Object obj) {
        context.getELResolver().setValue(context, null, name, obj);
    }

    /**
     * 自定义可以从EL表达式中可以获取的对象
     * 
     * @param name
     * @param obj
     * @param event
     */
    public void setValues(Map<String, Object> rmap) {
        for (Entry<String, Object> entry : rmap.entrySet()) {
            setValue(entry.getKey(), entry.getValue());
        }
    }

}
