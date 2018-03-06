package cn.com.workflow.listener;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class MyTaskCreateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Expression method;
	
	private Expression inputFields;
	
	private Expression outputFields;

	@Override
	public void notify(DelegateTask delegateTask) {

	}

}
