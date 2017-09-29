package cn.com.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MeetingUserComplateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private org.activiti.engine.delegate.Expression meetingParm;
	

	@Override
	public void notify(DelegateTask delegateTask) {
		String pram=(String)meetingParm.getValue(delegateTask.getExecution());
		System.out.println("pram==="+pram);
		Object obj=delegateTask.getExecution().getVariable(pram);
		if(null!=obj){
			delegateTask.getExecution().removeVariable(pram);
		}
	}

}
