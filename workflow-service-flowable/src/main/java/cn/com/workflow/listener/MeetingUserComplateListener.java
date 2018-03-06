package cn.com.workflow.listener;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class MeetingUserComplateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Expression meetingParm;
	

	@Override
	public void notify(DelegateTask delegateTask) {
		String pram=(String)meetingParm.getExpressionText();
		Object obj=delegateTask.getVariable(pram);
		if(null!=obj){
			delegateTask.removeVariable(pram);
		}
	}

}
