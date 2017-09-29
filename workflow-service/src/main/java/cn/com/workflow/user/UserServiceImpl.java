package cn.com.workflow.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.workflow.common.MD5Util;
import cn.com.workflow.common.Page;
import cn.com.workflow.mybatis.client.impl.SqlQueryMapperImpl;

@Component("userServiceImpl")
public class UserServiceImpl {

	
//	@Autowired(required = true)
//	private SpringHelper springHelper;
	
	@Autowired(required = true)
	private IdentityService identityService;
	
	@Autowired(required = true)
	private SqlQueryMapperImpl sqm;

	/**
	 * 创建用户
	 * @param _userId：用户ID
	 * @param _givenName：用户名
	 * @param _familyName：姓氏
	 * @param _businessEmail：电子邮件
	 */
	public void createUser(String _userId, String _givenName, String _familyName, String _email,String _password)
	{
		org.activiti.engine.identity.User  user=identityService.newUser(_userId);
		user.setEmail(_email);
		user.setPassword(MD5Util.GetMD5Code(_password));
		user.setFirstName(_givenName);
		user.setLastName(_familyName);
		identityService.saveUser(user);
		//updateUserPassword(_userId,_password);
	}
	
	/**
	 * 更新用户密码
	 * @param _userId
	 * @param _password
	 */
	public void updateUserPassword(String _userId,String _password){
		org.activiti.engine.identity.User user=findUserById(_userId);
		user.setPassword(MD5Util.GetMD5Code(_password));
		identityService.saveUser(user);
	}

	/**
	 * 新建组
	 * @param _groupName
	 * @param _parentGroupId
	 */
	public void createGroup(String _groupName, String _parentGroupId)
	{
		org.activiti.engine.identity.Group group=identityService.newGroup(_groupName);
		group.setName(_groupName);
		group.setType("group");
		identityService.saveGroup(group);
	}

	/**
	 * 新建关联
	 * @param _userId
	 * @param _groupId
	 */
	public void createMembership(String _userId, String _groupId)
	{
		identityService.createMembership( _userId,  _groupId);
	}

	/**
	 * 
	 * @param _userId
	 */
	public void deleteUser(String _userId)
	{
		identityService.deleteUser( _userId);
	}

	/**
	 * 
	 * @param _groupId
	 */
	public void deleteGroup(String _groupId)
	{
		identityService.deleteGroup( _groupId);
	}

	/**
	 * 
	 * @param _userId
	 * @param _groupId
	 */
	public void deleteMembership(String _userId, String _groupId)
	{
		identityService.deleteMembership(_userId, _groupId);
	}

	/**
	 * 
	 * @param _userId
	 * @return
	 */
	public org.activiti.engine.identity.User findUserById(String _userId)
	{
		return identityService.createUserQuery().userId(_userId).singleResult();
//		return sqm.queryUserById(_userId);
	}

	/**
	 * 
	 * @param _groupId
	 * @return
	 */
	public List<Group>  selectGroupDBIDById(String _groupId)
	{
		return identityService.createGroupQuery().groupId(_groupId).list();
	}
	
	/**
	 * 
	 * @param _groupId
	 * @return
	 */
	public Group findGroupById(String _groupId)
	{
		return identityService.createGroupQuery().groupId(_groupId).singleResult();
		
	}

	/**
	 * 
	 * @param _userId
	 * @return
	 */
	public List<Group> findGroupsByUser(String _userId)
	{
		return identityService.createGroupQuery().groupMember(_userId).list();
	}
	
	/**
	 * 
	 * @param _info
	 * @return
	 */
	public List<HashMap> findGroupsByInfo(Map _info)
	{
//		return sqm.queryGroupList(_info);
		return null;
	}
	
	/**
	 * 
	 * @param _info
	 * @param page
	 * @return
	 */
	public List<HashMap> findGroupsByInfo(Map _info,Page page)
	{
//		return sqm.queryGroupList(_info, page);
		return null;
	}
	
	/**
	 * 
	 * @param _info
	 * @param page
	 * @return
	 */
	public List<HashMap> findUserByInfo(Map _info,Page page)
	{
//		return sqm.queryUserList(_info, page);
		return null;
	}
	
	
	/**
	 * 
	 * @param _info
	 * @return
	 */
	public List<HashMap> findUserByInfo(Map _info)
	{
//		return sqm.queryUserList(_info);
		return null;
	}
	
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public List<User> queryUserByGroupId(String groupId)
	{
		//TODO
		identityService.createNativeGroupQuery().sql("");
		
		return null;
	}
	
	public List<User> queryUserByGroupId(String groupId,Page page)
	{
		//TODO
		identityService.createNativeGroupQuery().sql("");
		
		return null;
	}
}
