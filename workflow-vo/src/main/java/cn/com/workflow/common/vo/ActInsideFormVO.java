package cn.com.workflow.common.vo;

import java.util.List;

public class ActInsideFormVO  extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String filedId;
	
	private String filedName;
	
	private String filedValue;
	
	private String filedWR;
	
	private String filedType;
	
	private List<ActFormValueVO> values;
	
	
	
	public String getFiledId() {
		return filedId;
	}

	public void setFiledId(String filedId) {
		this.filedId = filedId;
	}

	public List<ActFormValueVO> getValues() {
		return values;
	}

	public void setValues(List<ActFormValueVO> values) {
		this.values = values;
	}

	public String getFiledType() {
		return filedType;
	}

	public void setFiledType(String filedType) {
		this.filedType = filedType;
	}

	public String getFiledWR() {
		return filedWR;
	}

	public void setFiledWR(String filedWR) {
		this.filedWR = filedWR;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getFiledValue() {
		return filedValue;
	}

	public void setFiledValue(String filedValue) {
		this.filedValue = filedValue;
	}
	
	

	
	
}
