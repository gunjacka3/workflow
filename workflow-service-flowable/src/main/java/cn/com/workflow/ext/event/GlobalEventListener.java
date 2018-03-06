package cn.com.workflow.ext.event;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEntityEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 * Flowable全局监听器实现类
 * Package : cn.com.workflow.ext.event
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午11:23:22
 *
 */
public class GlobalEventListener implements FlowableEventListener {
	static Logger logger = LogManager.getLogger();
	

	@Override
	public void onEvent(FlowableEvent event) {
	    FlowableEngineEventType fet = (FlowableEngineEventType)event.getType();
		switch (fet) {
			
			case TASK_CREATED:
				logger.debug("捕获事件：" + event.getType().name() + ",type=" + ToStringBuilder.reflectionToString(event));
				FlowableEntityEvent aee1 = (FlowableEntityEvent) event;
				TaskEntity task = (TaskEntity) aee1.getEntity();
				logger.debug("task:{} 's Assignee is {}",task,task.getAssignee());
				if(StringUtils.isBlank(task.getAssignee())){
					String taskDkey = task.getTaskDefinitionKey();
					String processInstId = task.getProcessInstanceId();
					List<HistoricTaskInstanceEntity> htasks =CommandContextUtil.getHistoricTaskService().findHistoricTasksByProcessInstanceId(processInstId);
					if(null!=htasks && !htasks.isEmpty()){
						if(!htasks.get(0).getTaskDefinitionKey().equals(taskDkey)){//判断是否为刚刚拒签或重置task的任务
							for(HistoricTaskInstance hts:htasks){
								if(hts.getTaskDefinitionKey().equals(taskDkey)){
									task.setAssignee(hts.getAssignee());
									logger.debug("task [{}] is setAssignee:{}",task.getId(),hts.getAssignee());
								}
							}
						}
					}
				}else{
					if(task.getAssignee().equals("temp")){
						task.setAssignee(null);
					}
					Set<IdentityLink> ils=task.getCandidates();
					for(IdentityLink il:ils){
						if(null!=il.getUserId() && il.getUserId().equals("temp")){
							task.deleteUserIdentityLink(il.getUserId(), il.getType());
						}else if(null!=il.getGroupId() && il.getGroupId().equals("temp")){
							task.deleteCandidateGroup(il.getGroupId());
						}
					}
					
				}
				logger.debug("process created a new task:{}" , task.getName());
				break;
//			  case JOB_EXECUTION_SUCCESS:
//		        System.out.println("A job well done!");
//		        FlowableEntityEvent aee2 = (FlowableEntityEvent) event;
//		        JobEntity job = (JobEntity) aee2.getEntity();
//		        break;
//		      case JOB_RETRIES_DECREMENTED:
//		        System.out.println("A job well decremented!");
//		        FlowableEntityEvent aee2 = (FlowableEntityEvent) event;
//		        JobEntity job = (JobEntity) aee2.getEntity();
//		        System.out.println("job.retries is "+job.getRetries());
//		        if(job.getRetries()==0){
//		        	 throw new BpmnError("BPM-ERROR-001");
//		        }
//		        break;	    	  
//		      case TIMER_FIRED:
//		    	  FlowableEntityEvent aee3 = (FlowableEntityEvent) event;
//		    	  TimerEntity te= (TimerEntity) aee3.getEntity();
//		    	  break;
//		      case JOB_EXECUTION_FAILURE:
//		    	  System.out.println("A job well failed!");
			}

	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return true;
	}

}
