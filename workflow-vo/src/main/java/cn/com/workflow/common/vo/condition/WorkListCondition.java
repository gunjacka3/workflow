package cn.com.workflow.common.vo.condition;

import java.util.Date;

import cn.com.workflow.common.vo.BaseVO;


public class WorkListCondition extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerName;
	
	private String bizType;
	
	private String bizId;
	
	private Date startTime=null;
	
	private Date endTime=null;

	
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	
	
}
