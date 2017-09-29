package cn.com.workflow.ext.identity.database.factory;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.workflow.ext.identity.database.manager.CusActGroupManager;

/**
 * activiti用户组扩展实现Factory
 * Package : cn.com.workflow.ext.identity.database.factory
 * 
 * @author wangzhiyin
 *         2017年9月27日 上午11:23:57
 *
 */
public class CusActGroupEntityManagerFactory implements SessionFactory{

	@Autowired(required = true)
	private CusActGroupManager cusActGroupManager;
	
	
	@Override
	public Class<?> getSessionType() {
		return GroupIdentityManager.class;
	}

	@Override
	public Session openSession() {
		return cusActGroupManager;
	}

	
}
