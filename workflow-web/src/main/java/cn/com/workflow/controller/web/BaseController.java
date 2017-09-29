package cn.com.workflow.controller.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.ActivitiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.util.JsonUtil;
import cn.com.workflow.common.vo.ResultVO;
import net.sf.json.JSONObject;

/**
 * 基础控制器
 * Package : cn.com.workflow.controller.web
 * 
 * @author wangzhiyin
 *		   2017年9月22日 下午3:54:42
 *
 */
@SessionAttributes("user") 
public abstract class BaseController {
    
    private static final Logger LOGGER = LogManager.getLogger(BaseController.class);
    
	@Value("${productName}")
	private String productName;
	
	/**
	 * 捕获BpmException异常
	 * 
	 * @param response
	 * @param ex
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月25日 上午9:51:23
	 */
	@ExceptionHandler({BpmException.class})
    public Object exception(HttpServletResponse response, Exception ex) {  
          
	    ResultVO vo = new ResultVO();
	    vo.setMessage(ex.getMessage());
	    vo.setErrorType(ResultVO.ERROR_TYPE_BUSINESS);
	    
	    try {
			return this.renderString(response,vo);
		} catch (BpmException e) {
		    LOGGER.error("BaseController exception meggage:{}",e);
		    return null;
		}
    } 
	
	/**
	 * 捕获ActivitiException异常
	 * 
	 * @param response
	 * @param ex
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月25日 上午9:51:42
	 */
	@ExceptionHandler({ActivitiException.class})
	public Object activitiException(HttpServletResponse response, Exception ex) {
//		ex.printStackTrace();
//		return "error/500";
		
		String rootCauseMessage = org.apache.commons.lang.exception.ExceptionUtils.getRootCause(ex).getMessage();
				
		ResultVO vo = new ResultVO();
	    vo.setMessage(rootCauseMessage);
	    vo.setErrorType(ResultVO.ERROR_TYPE_BUSINESS);
	    
	    try {
			return this.renderString(response,vo);
		} catch (BpmException e) {
            LOGGER.error("BaseController activitiException meggage:{}",e);
            return null;
		}
	}
	
	/**
	 * 捕获Exception异常
	 * 
	 * @param response
	 * @param ex
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月25日 上午9:51:55
	 */
	@ExceptionHandler({Exception.class})
	public Object wholeException(HttpServletResponse response, Exception ex) {
//		ex.printStackTrace();
//		return "error/500";
		
//		String rootCauseMessage = org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage(ex);
				
	    ResultVO vo = new ResultVO();
	    vo.setMessage(ex.getMessage());
	    vo.setErrorType(ResultVO.ERROR_TYPE_BUSINESS);
	    
	    try {
			return this.renderString(response,vo);
		} catch (BpmException e) {
            LOGGER.error("BaseController wholeException meggage:{}",e);
            return null;
		}
	}
	
    /**
     * 捕获RuntimeException异常
     * 
     * @param response
     * @param ex
     * @return 
     * @author wangzhiyin
     *	       2017年9月25日 上午9:52:08
     */
    @ExceptionHandler({RuntimeException.class})
    public Object wholeRuntimeException(HttpServletResponse response, Exception ex) {
//      ex.printStackTrace();
//      return "error/500";
        
//      String rootCauseMessage = org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage(ex);
                
        ResultVO vo = new ResultVO();
        vo.setMessage(ex.getMessage());
        vo.setErrorType(ResultVO.ERROR_TYPE_BUSINESS);
        
        try {
            return this.renderString(response,vo);
        } catch (BpmException e) {
            LOGGER.error("BaseController wholeRuntimeException meggage:{}",e);
            return null;
        }
    }	


	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
            LOGGER.error("BaseController renderString meggage:{}",e);
            return null;
		}
	}
	
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 * @throws BpmException 
	 */
	protected String renderString(HttpServletResponse response, Object object) throws BpmException {
		try {
			return renderString(response, this.toJsonString(object), "application/json");
		} catch (JsonProcessingException e) {
			throw new BpmException(e.getMessage(), e);
		}
	}
	
	/**
	 * json字符串处理
	 * 
	 * @param object
	 * @return
	 * @throws JsonProcessingException 
	 * @author wangzhiyin
	 *	       2017年9月22日 下午3:46:52
	 */
	private String toJsonString(Object object) throws JsonProcessingException{
		return new ObjectMapper().writeValueAsString(object);
	}
	
	/**
	 * 对象转为map
	 * @param obj
	 * @return
	 */
	protected Map<String, Object> object2map(Object obj){
		Map<String, Object> map=new HashMap<>();
		
		JSONObject json = JSONObject.fromObject(obj);
		JsonUtil.JsonToHashMap(json, map);
		return map;
	}
	
	/**
	 * 字符串处理
	 * 
	 * @param str
	 * @return
	 * @throws BpmException 
	 * @author wangzhiyin
	 *	       2017年9月22日 下午3:47:14
	 */
	protected Map<String, Object> str2mapSimple(String str) throws BpmException{
		ObjectMapper objectMapper = new ObjectMapper();  
		try {
			Map<String, Object> map = objectMapper.readValue(str, Map.class);
			return map;
		} catch (IOException e) {
			throw new BpmException(e.getMessage(), e);
		}  
	}
	
	/**
	 * 字符串处理
	 * 
	 * @param str
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月22日 下午3:48:46
	 */
	protected Map<String, Object> str2map(String str){
		Map<String, Object> map=new HashMap<>();
		
		String[] tmp=str.split("&");
		for(String tt:tmp){
			String[] tmp2=tt.split("=");
			if(tmp2.length>1){
				if(tmp2[0].indexOf(".string")> -1){
					map.put(tmp2[0].substring(0, tmp2[0].indexOf(".string")), tmp2[1]);
				}else if(tmp2[0].indexOf(".double")> -1){
					map.put(tmp2[0].substring(0, tmp2[0].indexOf(".double")), Double.parseDouble(tmp2[1]));
				}else if(tmp2[0].indexOf(".long")> -1){
					map.put(tmp2[0].substring(0, tmp2[0].indexOf(".long")), Double.parseDouble(tmp2[1]));
				}else if(tmp2[0].indexOf(".date")> -1){
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date = format.parse(tmp2[1]);
						map.put(tmp2[0].substring(0, tmp2[0].indexOf(".date")),date);
					} catch (ParseException e) {
					}
					
					
				}else{
					map.put(tmp2[0], tmp2[1]);
				}
			}
		}
		return map;
	}
	
	/**
	 * 判断是否数字
	 * 
	 * @param str
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月22日 下午3:51:17
	 */
	protected boolean isNumeric(String str){ 
       Pattern pattern = Pattern.compile("[0-9]*"); 
       Matcher isNum = pattern.matcher(str);
       return isNum.matches(); 
	}
}
