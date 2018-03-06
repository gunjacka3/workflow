package cn.com.workflow.user;

import cn.com.workflow.common.vo.BaseVO;


public class User extends BaseVO{
	
	private String givenName;
	
	private String familyName;
	
	private String password;
	
	private String userId;
	
	private String mail;
	
	private String groupId;
	
	public User() {
	}
	
	public User(String userId,String password,String familyName) {
		this.userId=userId;
		this.password=password;
		this.familyName=familyName;
		
	}


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}



	

}
