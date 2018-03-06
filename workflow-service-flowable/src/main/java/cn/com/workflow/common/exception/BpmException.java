package cn.com.workflow.common.exception;

/**
 * 工作流异常封装类
 * Package : cn.com.workflow.common.exception
 * 
 * @author Ywangzhiyin
 *		   2017年9月26日 上午9:39:32
 *
 */
@SuppressWarnings("serial")
public class BpmException extends Exception{

	public BpmException() {
		super();
	}
	
	public BpmException(Throwable cause) {
        super(cause);
    }
	
	public BpmException(String message) {
		super(message);
	}
	
	public BpmException(String message,Throwable cause) {
		super(message,cause);
	}
	
}
