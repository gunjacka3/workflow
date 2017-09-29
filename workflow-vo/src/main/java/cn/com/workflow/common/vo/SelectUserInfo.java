package cn.com.workflow.common.vo;

import java.util.ArrayList;
import java.util.List;

public class SelectUserInfo extends BaseVO{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String actityName;
	
	private String actityId;
	
	private List<ActFormValueVO> roles=new ArrayList<ActFormValueVO>();
	
	private String selectType;
	
	private String userVariable;
	
	
	public List<ActFormValueVO> getRoles() {
		return roles;
	}

	public void setRoles(List<ActFormValueVO> roles) {
		this.roles = roles;
	}

	public String getUserVariable() {
		return userVariable;
	}

	public void setUserVariable(String userVariable) {
		this.userVariable = userVariable;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getActityName() {
		return actityName;
	}

	public String getActityId() {
		return actityId;
	}

	public void setActityId(String actityId) {
		this.actityId = actityId;
	}

	public void setActityName(String actityName) {
		this.actityName = actityName;
	}

	@Override
	public String toString() {
		return "SelectUserInfo [actityName=" + actityName + ", actityId=" + actityId + ", roles=" + roles
				+ ", selectType=" + selectType + ", userVariable=" + userVariable + "]";
	}


	
}
