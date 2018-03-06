package cn.com.workflow.user;

import java.io.Serializable;

public class Users implements Serializable {
	private String userCd;
	private String userLevel;
	private String positionCd;
	private String orgCd;

	public String getOrgCd() {
		return orgCd;
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	public String getUserCd() {
		return userCd;
	}

	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getPositionCd() {
		return positionCd;
	}

	public void setPositionCd(String positionCd) {
		this.positionCd = positionCd;
	}
	
	@Override
	public String toString() {
		return "userCd="+userCd+"|userLevel="+userLevel+"|positionCd="+positionCd+"|orgCd="+orgCd;
	}
}
