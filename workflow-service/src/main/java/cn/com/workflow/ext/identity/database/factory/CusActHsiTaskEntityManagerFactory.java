package cn.com.workflow.ext.identity.database.factory;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.workflow.ext.identity.database.manager.CusHisTaskInsManager;

/**
 * activiti历史任务扩展实现Factory
 * Package : cn.com.workflow.ext.identity.database.factory
 * 
 * @author wangzhiyin
 *         2017年9月27日 上午11:23:57
 *
 */
public class CusActHsiTaskEntityManagerFactory implements SessionFactory{

	@Autowired(required = true)
	private CusHisTaskInsManager cusHisTaskInsManager;
	
	@Override
	public Class<?> getSessionType() {
		return HistoricTaskInstanceEntityManager.class;
	}

	@Override
	public Session openSession() {
		return cusHisTaskInsManager;
	}

}
