package cn.com.workflow.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

public class MeetingTaskComplateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private org.activiti.engine.delegate.Expression meetingParm;
	

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		String pram=(String)meetingParm.getValue(delegateTask.getExecution());
		System.out.println("pram==="+pram);
		String users=(String)delegateTask.getExecution().getVariable(pram);
		System.out.println("users==="+users);
		
		List<String> list =new ArrayList<String>();
		if(StringUtils.isNotBlank(users)){
			String[] u= users.split(",");
			list = Arrays.asList(u); 
		}
		delegateTask.getExecution().setVariable(pram, list);
	}

}
