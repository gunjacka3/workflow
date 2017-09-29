package cn.com.workflow.controller.ws.impl;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.workflow.common.exception.BpmException;

/**
 * web服务父类 Package : cn.com.workflow.controller.ws.impl
 * 
 * @author wangzhiyin 2017年9月22日 下午4:39:13
 *
 */
public class BaseWebService {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 输入参数处理
     * 
     * @param _variablesJson
     * @param requiredStr
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:39:34
     */
    protected Map<String, Object> getInputDate(String _variablesJson, String requiredStr) throws BpmException {
        if (null == _variablesJson || "".equals(_variablesJson)) {
            throw new BpmException("调用方法传入参数为空！");
        }
        String[] rs = requiredStr.split(",");
        Map<String, Object> map = jackson2Map(_variablesJson);
        for (String r : rs) {
            if (!map.containsKey(r)) {
                throw new BpmException("传入参数异常，请确认必要变量:【" + requiredStr + "】已全部传递");
            }
        }
        return map;
    }

    /**
     * json转换map
     * 
     * @param _jackson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月22日 下午4:39:54
     */
    protected Map<String, Object> jackson2Map(String _jackson) throws BpmException {
        try {
            Map<String, Object> userData = mapper.readValue(_jackson, Map.class);
            return userData;
        } catch (IOException e) {
            throw new BpmException("[" + _jackson + "] to Map is error!", e);
        } 
    }

    /**
     * map转换json
     * 
     * @param _map
     * @return
     * @throws BpmException 
     * @author wangzhiyin
     *	       2017年9月22日 下午4:40:54
     */
    protected String map2Jackson(Map<String, Object> _map) throws BpmException {
        try {
            return mapper.writeValueAsString(_map);
        } catch (JsonProcessingException e) {
            throw new BpmException("[" + _map + "] to json is error!", e);
        }
    }

    /**
     * 对象转json
     * 
     * @param obj
     * @return
     * @throws BpmException 
     * @author wangzhiyin
     *	       2017年9月22日 下午4:41:26
     */
    protected String object2Jackson(Object obj) throws BpmException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BpmException("[" + obj + "] to json is error!", e);
        }
    }
    
    /**
     * map转json
     * 
     * @param map
     * @param beanClass
     * @return
     * @throws Exception 
     * @author wangzhiyin
     *	       2017年9月22日 下午4:41:52
     */
    public Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }
    
    /**
     * 对象转map
     * 
     * @param obj
     * @return 
     * @author wangzhiyin
     *	       2017年9月22日 下午4:42:10
     */
    public Map objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}
