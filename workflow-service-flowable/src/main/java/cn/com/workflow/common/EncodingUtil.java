package cn.com.workflow.common;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * Package : cn.com.workflow.common
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午10:22:16
 *
 */
public class EncodingUtil {
	private static Logger log = LogManager.getLogger();
	
	public static String changeEncoding(String message) throws UnsupportedEncodingException{
		String encodeType=getEncoding(message);
		log.debug("encodeType====="+encodeType);
		return  new String(message.getBytes(encodeType),EncodeContent.ENCODING_UTF8);
	}
	
	
	
	/**
	 * 
	 * 
	 * @param str
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月27日 上午10:22:09
	 */
	public static String getEncoding(String str) {      
	       String encode = EncodeContent.ENCODING_UTF8;      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	              return encode;      
	           }      
	       } catch (Exception exception) {
	           log.error(exception);
	       }      
	       encode = EncodeContent.ENCODING_ISO8859;      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	              return encode;      
	           }      
	       } catch (Exception exception1) {     
	           log.error(exception1);
	       }      
	       encode = EncodeContent.ENCODING_GB2312;      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	              return encode;      
	           }      
	       } catch (Exception exception2) {   
	           log.error(exception2);
	       }      
	       encode = EncodeContent.GBK;      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	              return encode;      
	           }      
	       } catch (Exception exception3) {  
	           log.error(exception3);
	       }      
	      return "";      
	   }
}
