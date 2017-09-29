package cn.com.workflow.common.vo;

/**
 * 请求服务器返回的结果对象
 * 
 * @author tangyz
 *
 */
public class ResultVO extends BaseVO{
	
	private static final long serialVersionUID = 1L;
	
	public final static String ERROR="error";
	public final static String SUCCESS="success";
	
	
	private String result;
	private String message;
	private String errorType;
//	private RequestType requestType;
	private String description;
	private String remark;
	
	/**
	 * 获取 result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 获取 message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获取 requestType
	 */
//	public RequestType getRequestType() {
//		return requestType;
//	}
//
//	/**
//	 * 设置requestType
//	 */
//	public void setRequestType(RequestType requestType) {
//		this.requestType = requestType;
//	}

	public static String ERROR_TYPE_SYSTEM = "system";

	public static String ERROR_TYPE_BUSINESS = "business";

	/**
	 * 获取 errorType
	 */
	public String getErrorType() {
		return errorType;
	}

	/**
	 * 设置errorType
	 */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	/**
	 * 获取 remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取 description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
