package cn.com.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.Execution;

public class MyTaskCreateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private org.activiti.engine.delegate.Expression method;
	
	private org.activiti.engine.delegate.Expression inputFields;
	
	private org.activiti.engine.delegate.Expression outputFields;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		System.out.println("method==="+method.getValue(delegateTask.getExecution()));
		System.out.println("inputFields==="+inputFields.getValue(delegateTask.getExecution()));
		System.out.println("outputFields==="+outputFields.getValue(delegateTask.getExecution()));

	}

}
