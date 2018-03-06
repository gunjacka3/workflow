package cn.com.workflow.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.idm.api.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userManagerService")
public class UserManagerService  implements UserDetailsService{
	
	private static Logger log =  LogManager.getLogger();
//	@Autowired
//	private TbSysSecurityRelUserPosMapper tbSysSecurityRelUserPosMapperImpl;
	
//	@Autowired
//	private TbSysSecurityUsersMapper tbSysSecurityUsersMapperImpl;
	
	@Autowired(required = true)
	private UserServiceImpl us;
	
	@Autowired  
    private MessageSource messageSource;  

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		 UserDetails user = null;  
		 log.debug("loadUserByUsername=={}",username);
	        try {  
	        	
//	        	TbSysSecurityUsers u=tbSysSecurityUsersMapperImpl.selectByPrimaryKey(username);
	            org.flowable.idm.api.User  u=us.findUserById(username);
	        	
	        	if(null==u){
	        		 throw new UsernameNotFoundException("\u7528\u6237\u672a\u627e\u5230"+username);  
	        	}
	        	
	        	User dbUser = new User();
	        	List<Group> list=us.findGroupsByUser(u.getId());
//	        	List<TbSysSecurityRelUserPos> list=tbSysSecurityRelUserPosMapperImpl.selectPositionForUserId(u.getUserId());
	        	
//	            user = new org.springframework.security.core.userdetails.User(username, u.getUserPassword()  
//	                    .toLowerCase(), true, true, true, true,  
//	                    getAuthorities(list));  
	        	
	            user = new org.springframework.security.core.userdetails.User(username, u.getPassword()  
                .toLowerCase(), true, true, true, true,  
                getAuthorities(list));  
	            log.debug("login is ok:");
	        } catch (Exception e) {  
	        	e.printStackTrace();
	        	log.error("Error in retrieving user");  
	            throw new UsernameNotFoundException(e.getMessage());  
	        }  
	  
	        return user;  
	} 
	
	/** 
     * 鑾峰緱璁块棶瑙掕壊鏉冮檺 
     *  
     * @param access 
     * @return 
     */  
    public Collection<GrantedAuthority> getAuthorities(List<Group> _list) {  
  
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();  
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        for(Group group:_list){
        	 log.debug("Grant "+group.getId()+" to this user");  
        	authList.add(new SimpleGrantedAuthority(group.getId()));  
        }
        return authList;  
    }  
    public Collection<GrantedAuthority> getAuthorities2(List<Group> _list) {  
    	  
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();  
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        for(Group group:_list){
        	authList.add(new SimpleGrantedAuthority(group.getName()));  
        }
        return authList;  
    } 

}
