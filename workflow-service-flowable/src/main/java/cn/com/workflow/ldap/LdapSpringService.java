package cn.com.workflow.ldap;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import cn.com.workflow.user.User;

@Service("ldapSpringService")
public class LdapSpringService {
	
	private LdapTemplate ldapTemplate;
	
	private String userFilter;
	
	private String ugFilter;
	
	public void setUserFilterId(String userFilterId) {
		this.userFilterId = userFilterId;
	}


	public void setGroupFilterId(String groupFilterId) {
		this.groupFilterId = groupFilterId;
	}


	private String groupFilter;
	
	private String userFilterId;
	
	private String groupFilterId;
	

	/**
	 * 查询在LdapTemplate中是search说法
	 * @param uid
	 * @return
	 */
	public User getUserById(String uid) {
		String filter = "(&"+userFilter.replace(userFilterId, uid)+")";
		List<User> list = ldapTemplate.search("", filter, new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attributes) throws NamingException {
				User user = new User();

				Attribute a = attributes.get("cn");
				if (a != null)
					user.setGivenName((String) a.get());

				a = attributes.get("sn");
				if (a != null)
					user.setFamilyName((String) a.get());

				return user;
			}
		});
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	
	/**
	 * 查询在LdapTemplate中是search说法
	 * @param uid
	 * @return
	 */
	public User getGroupById(String gid) {
		String filter = "(&"+groupFilter.replace(groupFilterId, gid)+")";
		List<User> list = ldapTemplate.search("", filter, new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attributes) throws NamingException {
				User user = new User();

				Attribute a = attributes.get("ou");
				if (a != null)
					user.setGroupId((String) a.get());

				return user;
			}
		});
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	 * 查询在LdapTemplate中是search说法
	 * @param uid
	 * @return
	 */
	public List<User> getUsersByGroupId(String gid) {
		String filter = "(&"+ugFilter.replace(groupFilterId, gid)+")";
		SearchControls searchControls = new SearchControls(); 
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE); 
        
        
		List<User> list = ldapTemplate.search("", filter,searchControls,new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attributes) throws NamingException {
				User user = new User();

				Attribute a = attributes.get("cn");
				if (a != null)
					user.setGivenName((String) a.get());

				a = attributes.get("sn");
				if (a != null)
					user.setFamilyName((String) a.get());

				return user;
			}
		});
		
		return list;
	}
	
	private Name buildDn(String groupId) {
	      DistinguishedName dn = new DistinguishedName("dc=maxcrc,dc=com");//初始化时可以把根写上，也可写放在下面。
	      dn.add("ou", groupId);  //c前面的路径（公司）
	      return dn; //创建好之后就会返回一个dn可以直接放在search中
	}

	
	/**
	 * 在LDAP中是没有添加这一说法的，标准的叫法是绑定，对应的删除就是解绑。绑定时要将所有必须属性都添加上，首先是objectClass属性，
	 * 这个是LDAP中的对象，LDAP中的对象是继承的，每个对象都有一些特定的属性，有些属性是必须的，有些是可选的。第2个步骤就是将每个对象的必须属性和你想要的非必须属性填上交由LdapTemplate进行绑定即可。
	 * @param vo
	 * @return
	 */
	public boolean addUser(User vo) {
		try {
			// 基类设置
			BasicAttribute ocattr = new BasicAttribute("objectClass");
			// ocattr.add("top");
			ocattr.add("person");
			// ocattr.add("uidObject");
			// ocattr.add("inetOrgPerson");
			// ocattr.add("organizationalPerson");
			// 用户属性
			Attributes attrs = new BasicAttributes();
			attrs.put(ocattr);
			attrs.put("cn", StringUtils.trimToEmpty(vo.getGivenName()));
			attrs.put("sn", StringUtils.trimToEmpty(vo.getFamilyName()));
			// attrs.put("displayName",
			// StringUtils.trimToEmpty(vo.getGivenName()));
			// attrs.put("mail", StringUtils.trimToEmpty(vo.getEmail()));
			// attrs.put("telephoneNumber",
			// StringUtils.trimToEmpty(vo.getMobile()));
			// attrs.put("title", StringUtils.trimToEmpty(vo.getTitle()));
			attrs.put("userPassword", StringUtils.trimToEmpty(vo.getPassword()));
			ldapTemplate.bind("cn=" + vo.getGivenName().trim(), null, attrs);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	/**
	 * 更新就是替换属性，使用ModificationItem类进行处理。
	 * @param vo
	 * @return
	 */
	public boolean updateUser(User vo) {
		try {
			ldapTemplate.modifyAttributes("cn=" + vo.getGivenName().trim(),
					new ModificationItem[] {
							new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
									new BasicAttribute("cn", vo.getGivenName().trim())),
							new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
									new BasicAttribute("displayName", vo.getGivenName().trim())),
							new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
									new BasicAttribute("sn", vo.getFamilyName().trim())) });
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	/**
	 * 删除也就是解绑的过程，直接调用unbind即可。
	 * @param username
	 * @return
	 */
	public boolean deleteUser(String username) {
		try {
			ldapTemplate.unbind("cn=" + username.trim());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}


	public String getUserFilter() {
		return userFilter;
	}


	public void setUserFilter(String userFilter) {
		this.userFilter = userFilter;
	}


	public String getGroupFilter() {
		return groupFilter;
	}


	public void setGroupFilter(String groupFilter) {
		this.groupFilter = groupFilter;
	}


	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}


	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}


	public String getUserFilterId() {
		return userFilterId;
	}


	public String getGroupFilterId() {
		return groupFilterId;
	}


	public String getUgFilter() {
		return ugFilter;
	}


	public void setUgFilter(String ugFilter) {
		this.ugFilter = ugFilter;
	}



}
