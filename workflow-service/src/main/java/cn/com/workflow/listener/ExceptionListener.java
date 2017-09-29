package cn.com.workflow.listener;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

public class ExceptionListener implements ActivitiEventListener {

	@Override
  public void onEvent(ActivitiEvent event) {
	    switch (event.getType()) {
	
	      case JOB_EXECUTION_SUCCESS:
	        System.out.println("A job well done!");
	        break;
	      case JOB_RETRIES_DECREMENTED:
	        System.out.println("A job well decremented!");
	        break;	    	  
	      case JOB_EXECUTION_FAILURE:
	    	  System.out.println("A job well failed!");
	        throw new BpmnError("BPM-ERROR-001");
	
	      default:
	        System.out.println("Event received: " + event.getType());
	    }
		
	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
