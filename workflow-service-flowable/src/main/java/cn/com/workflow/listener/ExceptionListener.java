package cn.com.workflow.listener;

import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.BpmnError;

public class ExceptionListener implements FlowableEventListener {

	@Override
  public void onEvent(FlowableEvent event) {
	    FlowableEngineEventType fet = (FlowableEngineEventType)event.getType();
	    switch (fet) {
	
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
		return false;
	}

}
