package cn.com.workflow.ext.behavior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * activiti扩展Gateway实现类(不适用多线程问题)
 * Package : cn.com.workflow.ext.behavior
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午11:22:00
 *
 */
public class ParallelGatewayActivityBehaviorExt extends ParallelGatewayActivityBehavior {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(ParallelGatewayActivityBehaviorExt.class);
//	private Map<String, ConcurrentHashMap<String,String>> activityJoinedExecutions = new ConcurrentHashMap<String, ConcurrentHashMap<String,String>> ();
//	private Map<String,ActivityExecution> activityJoinedExecutions = new ConcurrentHashMap<String, ActivityExecution> ();
	
	
	private void putActivityJoinedExecutions(ActivityExecution execution,String parentId,String id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		log.debug("---putActivityJoinedExecutions---:parentId:{}---",parentId);
		
		ProcessInstance pi=execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getId()).includeProcessVariables().singleResult();
		Map processVariables=pi.getProcessVariables();
		
		if(processVariables.containsKey(parentId)){
//			ConcurrentHashMap<String,String> map=(ConcurrentHashMap)execution.getVariable(parentId);
			String jsonString=(String)processVariables.get(parentId);
			log.debug("---putActivityJoinedExecutions---:jsonString:{}---",jsonString);
				Map map = mapper.readValue(jsonString, Map.class);
				map.put(id, id);
				String jsonfromMap = mapper.writeValueAsString(map);
//				execution.setVariable(parentId, jsonfromMap);
				execution.getEngineServices().getRuntimeService().setVariable(parentId, parentId, jsonfromMap);
			
		}else{
			Map<String,String> map=new HashMap<String,String>();
			map.put(id,id);
			String jsonfromMap = mapper.writeValueAsString(map);
//			execution.setVariable(parentId, jsonfromMap);
			execution.getEngineServices().getRuntimeService().setVariable(parentId, parentId, jsonfromMap);
		}
//		if(activityJoinedExecutions.containsKey(parentId)){
//			((ConcurrentHashMap)activityJoinedExecutions.get(parentId)).put(id, id);
//		}else{
//			ConcurrentHashMap map=new ConcurrentHashMap();
//			map.put(id,id);
//			activityJoinedExecutions.put(parentId, map);
//		}
	}
	
	private boolean containsActivityJoinedExecutions(ActivityExecution execution,String parentId,String id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ProcessInstance pi=execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getId()).includeProcessVariables().singleResult();
		Map processVariables=pi.getProcessVariables();
		log.debug("---containsActivityJoinedExecutions---:parentId:{}---",parentId);
		if(processVariables.containsKey(parentId)){
			String jsonString=(String)processVariables.get(parentId);
			log.debug("---containsActivityJoinedExecutions---:jsonString:{}---",jsonString);
			Map map = mapper.readValue(jsonString, Map.class);
			return map.containsKey(id);
		}else{
			return false;
		}
		
		
		
//		if(!activityJoinedExecutions.containsKey(parentId)){
//			return false;
//		}else{
//			ConcurrentHashMap map=activityJoinedExecutions.get(parentId);
//			return map.containsKey(id);
//		}
	}
	
	private String getActivityJoinedExecutions(ActivityExecution execution,String parentId,String id) throws Exception {
//		log.debug("---find---:parentId:{} {}---activityJoinedExecutions:{}",parentId,id,activityJoinedExecutions);
		log.debug("---getActivityJoinedExecutions---:parentId:{}---",parentId);
		ObjectMapper mapper = new ObjectMapper();
		if(null!=execution.getVariable(parentId)){
			String jsonString=(String)execution.getVariable(parentId);
			log.debug("---getActivityJoinedExecutions---:jsonString:{}---",jsonString);
			Map map = mapper.readValue(jsonString, Map.class);
			return (String)map.get(id);
		}else{
			return null;
		}		
		
//		if(!activityJoinedExecutions.containsKey(parentId)){
//			return null;
//		}else{
//			ConcurrentHashMap map=activityJoinedExecutions.get(parentId);
//			return (String)map.get(id);
//		}
	}
	
	private int sizeActivityJoinedExecutions(ActivityExecution execution,String parentId) throws Exception {
		log.debug("---sizeActivityJoinedExecutions---:parentId:{}---",parentId);
		ObjectMapper mapper = new ObjectMapper();
		ProcessInstance pi=execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getId()).includeProcessVariables().singleResult();
		Map processVariables=pi.getProcessVariables();
		if(processVariables.containsKey(parentId)){
			String jsonString=(String)processVariables.get(parentId);
			log.debug("---sizeActivityJoinedExecutions---:jsonString:{}---",jsonString);
			Map map = mapper.readValue(jsonString, Map.class);
			return map.size();
		}else{
			return 0;
		}	
//		if(!activityJoinedExecutions.containsKey(parentId)){
//			return 0;
//		}else{
//			ConcurrentHashMap map=activityJoinedExecutions.get(parentId);
//			return map.size();
//		}
	}
	
	private void clearActivityJoinedExecutions(ActivityExecution execution,String parentId) throws Exception {
		log.debug("---clearActivityJoinedExecutions---:parentId:{}---",parentId);
		ProcessInstance pi=execution.getEngineServices().getRuntimeService().createProcessInstanceQuery().processInstanceId(execution.getId()).includeProcessVariables().singleResult();
		if(null==pi){
			return;
		}
		Map processVariables=pi.getProcessVariables();
		if(processVariables.containsKey(parentId)){
			log.debug("---clear---:parentId:{}---",parentId);
			execution.getEngineServices().getRuntimeService().removeVariable(execution.getId(), parentId);
//			execution.removeVariable(parentId);
		}else{
			
		}	
		
//		if(!activityJoinedExecutions.containsKey(parentId)){
//		}else{
//			activityJoinedExecutions.remove(parentId);
//		}
	}

	public void execute(ActivityExecution execution) throws Exception {
//		log.debug("execute activityJoinedExecutions:{}",activityJoinedExecutions);

		// Join
		PvmActivity activity = execution.getActivity();
		log.debug("execution.id:{}..execution.ParentId:{}",execution.getId(),execution.getParentId());
		
		String parentId=execution.getParentId()!=null?execution.getParentId():execution.getId();
		ActivityExecution ae=execution.getParent()!=null?execution.getParent():execution;
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
			for (ActivityExecution e : joinedExecutions) {
				log.debug("activityJoinedExecutions.putId:{}",e.getId());
//				activityJoinedExecutions.put(e.getId(), e);
				putActivityJoinedExecutions(ae,parentId,e.getId());
			}
//			nbrOfExecutionsJoined = activityJoinedExecutions.size();
			nbrOfExecutionsJoined = sizeActivityJoinedExecutions(ae,parentId);
			log.debug("sizeActivityJoinedExecutions:{}",nbrOfExecutionsJoined);
			if (nbrOfExecutionsJoined == nbrOfExecutionsToJoin && execution.getParentId() != null
					&& execution instanceof ExecutionEntity) {
				ExecutionEntity et = (ExecutionEntity) execution;
				while (joinedExecutions.size() != nbrOfExecutionsToJoin) {
					List<ExecutionEntity> ees=et.getParent().getExecutions();
					log.debug("execution.ees.:{}",ees);
					Thread.sleep(1000);
					for (int i = 0; i < ees.size(); i++) {
						ExecutionEntity ct =ees.get(i);
						if (containsActivityJoinedExecutions(ae,parentId,ct.getId())) {
//						if (activityJoinedExecutions.containsKey(ct.getId())) {
//							et.getParent().getExecutions().set(i,(ExecutionEntity)activityJoinedExecutions.get(ct.getId()));
							if(ct.getId().equals(et.getId())){
								et.getParent().getExecutions().set(i,ct);
							}else{
//								String id=getActivityJoinedExecutions(execution,parentId,ct.getId());
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
//			activityJoinedExecutions.clear();
			clearActivityJoinedExecutions(ae,parentId);
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
