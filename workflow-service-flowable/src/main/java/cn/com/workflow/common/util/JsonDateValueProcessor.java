package cn.com.workflow.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * json工具类
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午9:49:52
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor {   
	
	  private String format ="yyyy-MM-dd hh:mm:ss ";   
	     
	  /**
	   * 
	   * 
	   * @param value
	   * @param config
	   * @return 
	   * @author wangzhiyin
	   *	       2017年9月27日 上午9:50:07
	   */
	  public Object processArrayValue(Object value, JsonConfig config) {   
	    return process(value);   
	  }   
	  
	  /**
	   * 
	   * 
	   * @param key
	   * @param value
	   * @param config
	   * @return 
	   * @author wangzhiyin
	   *	       2017年9月27日 上午9:50:11
	   */
	  public Object processObjectValue(String key, Object value, JsonConfig config) {   
	    return process(value);   
	  }   
	     
	  /**
	   * 
	   * 
	   * @param value
	   * @return 
	   * @author wangzhiyin
	   *	       2017年9月27日 上午9:50:14
	   */
	  private Object process(Object value){   
	    if(value instanceof Date || value instanceof Timestamp){   
	      SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.UK);   
	      return sdf.format(value);   
	    }
	    return value == null ? "" : value.toString();   
	  }   
	}