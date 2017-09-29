package cn.com.workflow.common.vo;

import java.util.ArrayList;
import java.util.List;

public class ActFormValueVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String valueId;
	
	private String valueName;
	
	private List<ActFormValueVO> values=new ArrayList<ActFormValueVO>();

	
	
	public List<ActFormValueVO> getValues() {
		return values;
	}

	public void setValues(List<ActFormValueVO> values) {
		this.values = values;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	@Override
	public String toString() {
		return "ActFormValueVO [valueId=" + valueId + ", valueName=" + valueName + ", values=" + values + "]";
	}



	
	
}
