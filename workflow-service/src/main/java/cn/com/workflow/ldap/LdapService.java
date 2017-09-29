package cn.com.workflow.ldap;

import java.util.Properties;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapService {
	
	public static void main(String[] args) {
		Properties env = new Properties();
		String adminName = "cn=Manager,dc=maxcrc,dc=com";//username@domain
		String adminPassword = "211513";//password
		String ldapURL = "LDAP://localhost:389";//ip:port
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simphle","strong"
		env.put(Context.SECURITY_PRINCIPAL, adminName);
		env.put(Context.SECURITY_CREDENTIALS, adminPassword);
		env.put(Context.PROVIDER_URL, ldapURL);
		try {
			LdapContext ctx = new InitialLdapContext(env, null);
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String searchFilter = "(&(objectClass=person)(cn=admin001))";
			String searchBase = "dc=maxcrc,dc=com";
			String returnedAtts[] = {"sn","cn","userPassword"};
			searchCtls.setReturningAttributes(returnedAtts);
			NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				System.out.println("<<<::[" + sr.getName()+"]::>>>>");
			}
			ctx.close();
		}catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Problem searching directory: " + e);
		}
	}

	
	private static void queryGroup(LdapContext ldapCtx) throws NamingException {  
	    SearchControls searchCtls = new SearchControls();  
	    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);  
	    String searchFilter = "objectClass=organizationalUnit";  
	    String searchBase = "ou=myDeptSubDept,ou=myDept,dc=DS-66,dc=com";  
	    String returnedAtts[] = {"distinguishedName", "objectGUID", "name"};  
	    searchCtls.setReturningAttributes(returnedAtts);  
	    NamingEnumeration<SearchResult> answer = ldapCtx.search(searchBase, searchFilter, searchCtls);  
	    while (answer.hasMoreElements()) {  
	        SearchResult sr = answer.next();  
	        Attributes Attrs = sr.getAttributes();  
	        if (Attrs != null) {  
	            NamingEnumeration<?> ne = Attrs.getAll();  
	            while(ne.hasMore()) {  
	                Attribute Attr = (Attribute)ne.next();  
	                String name = Attr.getID();  
	                NamingEnumeration<?> values = Attr.getAll();  
	                if (values != null) { // 迭代  
	                    while (values.hasMoreElements()) {  
	                        String value = "";  
	                        if("objectGUID".equals(name)) {  
	                            value = UUID.nameUUIDFromBytes((byte[]) values.nextElement()).toString();  
	                        } else {  
	                            value = (String)values.nextElement();  
	                        }  
	                        System.out.println(name + " " + value);  
	                    }  
	                }  
	            }  
	            System.out.println("=====================");  
	        }  
	    }  
	      
	}  
}
