package cn.com.workflow.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityEventListener implements JavaDelegate{
	private static Logger log = LoggerFactory.getLogger(EntityEventListener.class);

	@Override
	public void execute(DelegateExecution execution){
        
		String parentId=execution.getParentId()!=null?execution.getParentId():execution.getId();

		execution.setVariable(parentId+"_"+execution.getId(),"ok");
	}
		
	
}
