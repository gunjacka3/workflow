package cn.com.workflow.ext.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * activiti扩展Gateway实现类(不适用多线程问题)
 * Package : cn.com.workflow.ext.behavior
 * 
 * @author wangzhiyin
 *         2017年9月27日 上午11:22:00
 *
 */
public class ParallelGatewayActivityBehaviorExt2 extends ParallelGatewayActivityBehavior {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ParallelGatewayActivityBehaviorExt.class);
//	private Map<String, ConcurrentHashMap<String,String>> activityJoinedExecutions = new ConcurrentHashMap<String, ConcurrentHashMap<String,String>> ();
//	private Map<String,ActivityExecution> activityJoinedExecutions = new ConcurrentHashMap<String, ActivityExecution> ();
	
	
	
	
	private boolean containsActivityJoinedExecutions(ActivityExecution execution,String parentId,String id) throws Exception {
		log.debug("---containsActivityJoinedExecutions---:parentId:{}--id:{}-",parentId,id);
		
		Map<String,Object> processVariables= execution.getEngineServices().getRuntimeService().getVariables(parentId);
		
		return processVariables.containsKey(parentId+"_"+id);		
	}
	

	
	private int sizeActivityJoinedExecutions(ActivityExecution execution,String parentId) throws Exception {
		log.debug("---sizeActivityJoinedExecutions---:parentId:{}---",parentId);
		int size=0;
		Map<String,Object> processVariables= execution.getEngineServices().getRuntimeService().getVariables(parentId);
		for(String key:processVariables.keySet()){
			if(key.startsWith(parentId)){
				size++;
			}
		}
		
		return size;

	}
	
	private void clearActivityJoinedExecutions(ActivityExecution execution,String parentId) throws Exception {
		log.debug("---clearActivityJoinedExecutions---:parentId:{}---",parentId);
		Map<String,Object> processVariables= execution.getEngineServices().getRuntimeService().getVariables(parentId);
		List<String> keys=new ArrayList<String>();
		for(String key:processVariables.keySet()){
			if(key.startsWith(parentId)){
				keys.add(key);
			}
		}
		execution.getEngineServices().getRuntimeService().removeVariablesLocal(parentId, keys);
	}

	public void execute(ActivityExecution execution) throws Exception {
		// Join
		PvmActivity activity = execution.getActivity();
		log.debug("execution.id:{}..execution.ParentId:{}",execution.getId(),execution.getParentId());
		
		String parentId=execution.getParentId()!=null?execution.getParentId():execution.getId();
		log.debug("parentId:{}",parentId);
		
		List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();

		execution.inactivate();
		lockConcurrentRoot(execution);

		List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
		int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();
		int nbrOfExecutionsJoined = joinedExecutions.size();
		
		log.debug("joinedExecutions.size():{}",nbrOfExecutionsJoined);
		
		//edit begin
		if (nbrOfExecutionsToJoin != nbrOfExecutionsJoined) {
			nbrOfExecutionsJoined = sizeActivityJoinedExecutions(execution,parentId);
			log.debug("sizeActivityJoinedExecutions:{}",nbrOfExecutionsJoined);
			if (nbrOfExecutionsJoined == nbrOfExecutionsToJoin && execution.getParentId() != null
					&& execution instanceof ExecutionEntity) {
				ExecutionEntity et = (ExecutionEntity) execution;
				while (joinedExecutions.size() != nbrOfExecutionsToJoin) {
					log.debug("joinedExecutions.size===={}",joinedExecutions.size());
					List<ExecutionEntity> ees=et.getParent().getExecutions();
					log.debug("execution.ees.:{}",ees);
					Thread.sleep(1000);
					for (int i = 0; i < ees.size(); i++) {
						ExecutionEntity ct =ees.get(i);
						if (containsActivityJoinedExecutions(execution,parentId,ct.getId())) {
							if(ct.getId().equals(et.getId())){
								et.getParent().getExecutions().set(i,ct);
							}else{
								ExecutionEntity ew=new ExecutionEntity();
								ew.setActivity(et.getActivity());
								ew.setActive(false);
								ew.setConcurrent(true);
								ew.setEnded(false);
								ew.setScope(false);
								ew.setRevision(1);
								ew.setEventScope(false);
								ew.setExecutions(et.getExecutions());
								ew.setParent(et.getParent());
								ew.setProcessDefinition(et.getProcessDefinition());
								ew.setProcessDefinitionId(et.getProcessDefinitionId());
								ew.setProcessInstance(et.getProcessInstance());
								ew.setId(ct.getId());
								ew.setCachedEntityState(3);
								ew.setCachedElContext(et.getCachedElContext());
								ew.setSuspensionState(1);
								ew.setParentId(et.getParentId());
								et.getParent().getExecutions().set(i,ew);
							}
						}

					}
					joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
					log.debug("execution.findInactiveConcurrentExecutions----{}",joinedExecutions);
				}
			}
		}
		if (nbrOfExecutionsJoined == nbrOfExecutionsToJoin) {
			clearActivityJoinedExecutions(execution,parentId);
			// Fork
			if (log.isDebugEnabled()) {
				log.debug("parallel gateway '{} is {}' activates: {} of {} joined", activity.getId(),activity.hashCode(), nbrOfExecutionsJoined,
						nbrOfExecutionsToJoin);
				
			}
			execution.takeAll(outgoingTransitions, joinedExecutions);

		} else if (log.isDebugEnabled()) {
			log.debug("parallel gateway '{}  is {}' activates: {} of {} joined", activity.getId(),activity.hashCode(), nbrOfExecutionsJoined,
					nbrOfExecutionsToJoin);
		}
	}

}
