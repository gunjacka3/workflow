package cn.com.workflow.common.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * json工具类
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午9:50:53
 *
 */
public class JsonUtil implements Serializable{

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	/**
     * 
     * @author wangzhiyin
     *		   2017年9月27日 上午9:51:10
     *
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return java对象
	 */
	public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}
	
	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String,Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<String> keyIter = jsonObject.keys();
		String key;
		Object value;
		Map<String,Object> valueMap = new HashMap<>();

		while (keyIter.hasNext()) {
			key = keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}

	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List<Object> getList4Json(String jsonString, Class pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;
	}

	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}

	/**
	 * 从json数组中解析出java Date 型对象数组，使用本方法必须保证
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Date[] getDateArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Date[] dateArray = new Date[jsonArray.size()];
		Date date = null;

		for (int i = 0; i < jsonArray.size(); i++) {
			date = null;
			dateArray[i] = date;

		}
		return dateArray;
	}

	/**
	 * 从json数组中解析出java Integer型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	/*
	 * public static Double[] getDoubleArray4Json(String jsonString) {
	 * 
	 * JSONArray jsonArray = JSONArray.fromObject(jsonString); Double[]
	 * doubleArray = new Double[jsonArray.size()]; for (int i = 0; i <
	 * jsonArray.size(); i++) { doubleArray[i] = jsonArray.getDouble(i);
	 * 
	 * } return doubleArray; }
	 */
	
	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj) {
		JSONObject json;
		json = JSONObject.fromObject(javaObj);
		return json.toString();
	}
	
	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonString4JavaArray(Object javaObj) {
		JSONArray ja;
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Timestamp.class , new JsonDateValueProcessor());
		ja = JSONArray.fromObject(javaObj,jsonConfig);
		return ja.toString();
	}
	
	/**
	 * 将java对象转换成HashMap
	 * 
	 * @param jsonData
	 * @param rstList
	 * @param params 
	 * @author wangzhiyin
	 *	       2017年9月27日 上午9:54:04
	 */
	public static void JsonToHashMap(JSONObject jsonData, Map<String, Object> rstList,  
            String... params) {  
        try {  
            for (Iterator<String> keyStr = jsonData.keys(); keyStr.hasNext();) {  
  
                String key1 = keyStr.next().trim();  
                if (jsonData.get(key1) instanceof JSONObject) {  
                    HashMap<String, Object> mapObj = new HashMap<>();  
                    JsonToHashMap((JSONObject) jsonData.get(key1), mapObj, params);  
                    rstList.put(key1, mapObj);  
                    continue;  
                }  
                if (jsonData.get(key1) instanceof JSONArray) {  
                    ArrayList<Map<String, Object>> arrayList = new ArrayList<>();  
  
                    JsonToHashMap((JSONArray) jsonData.get(key1), arrayList, params);  
                    rstList.put(key1, arrayList);  
                    continue;  
                }  
                JsonToHashMap(key1, jsonData.get(key1), rstList);  
            }  
            // 追加字段  
            if (params != null && params.length == 2) {  
                rstList.put(params[0], params[1]);  
            }  
            if (params != null && params.length == 4) {  
                rstList.put(params[0], params[1]);  
                rstList.put(params[2], params[3]);  
            }  
  
        } catch (JSONException e) {  
            logger.error("JsonToHashMap异常!", e);
        }  
  
    }  
	
	/**
	 * 
	 * 将java对象转换成HashMap
	 * @param jsonarray
	 * @param rstList
	 * @param params 
	 * @author YixinCapital -- wangzhiyin
	 *	       2017年9月27日 上午9:56:07
	 */
	private static void JsonToHashMap(JSONArray jsonarray, List<Map<String, Object>> rstList,  
            String... params) {  
        try {  
            for (int i = 0; i < jsonarray.size(); i++) {  
                if (jsonarray.get(i) instanceof JSONObject) {  
  
                    HashMap<String, Object> mapObj = new HashMap<>();  
                    JsonToHashMap((JSONObject) jsonarray.get(i), mapObj, params);  
                    rstList.add(mapObj);  
                    continue;  
                }  
            }  
  
        } catch (JSONException e) {  
            logger.error("JsonToHashMap异常!", e);
        }  
  
    }   
	
	/**
	 * 
	 * 
	 * @param key
	 * @param value
	 * @param rstList 
	 * @author wangzhiyin
	 *	       2017年9月27日 上午9:57:11
	 */
	private static void JsonToHashMap(String key, Object value, Map<String, Object> rstList) {  
        if (value instanceof String) {  
        	if(!"".equals(value)){
        		rstList.put(key, value);  
        	}
            return;  
        }  
        rstList.put(key, value);  
    } 
}
