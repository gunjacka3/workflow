package cn.com.workflow.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class MeetingTaskComplateListener implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Expression meetingParm;
	

	@Override
	public void notify(DelegateTask delegateTask) {
		String pram=(String)meetingParm.getExpressionText();
		String users=(String)delegateTask.getVariable(pram);
		List<String> list =new ArrayList<String>();
		if(StringUtils.isNotBlank(users)){
			String[] u= users.split(",");
			list = Arrays.asList(u); 
		}
		delegateTask.setVariable(pram, list);
	}

}
