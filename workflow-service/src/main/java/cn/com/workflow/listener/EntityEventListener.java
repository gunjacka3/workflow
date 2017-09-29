package cn.com.workflow.listener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EntityEventListener implements JavaDelegate{
	private static Logger log = LoggerFactory.getLogger(EntityEventListener.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();  
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
//        TransactionStatus txStatus = txManager.getTransaction(def);// 获得事务状态  	
        
		ObjectMapper mapper = new ObjectMapper();
		RuntimeService runtimeService=execution.getEngineServices().getRuntimeService();
//		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(execution.getParentId()).singleResult();
		String parentId=execution.getParentId()!=null?execution.getParentId():execution.getId();
		runtimeService.setVariable(parentId, parentId+"_"+execution.getId(), "ok");

//        try {
//    		//逻辑代码
//        	txManager.commit(txStatus);
//    	} catch (Exception e) {
//    		txManager.rollback(txStatus);
//    	}
		
//		ObjectMapper mapper = new ObjectMapper();
//		String processId=execution.getParentId();
//		RuntimeService runtimeService=execution.getEngineServices().getRuntimeService();
//		
//		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
//		if(StringUtils.isNotBlank(pi.getParentId())){
//
//			try{
//				if(null!=runtimeService.getVariable(pi.getParentId(),pi.getParentId())){
//					String jsonString=(String)runtimeService.getVariable(pi.getParentId(),pi.getParentId());
//					log.debug("---putActivityJoinedExecutions---:jsonString:{}---",jsonString);
//					Map map = mapper.readValue(jsonString, Map.class);
//					map.put(execution.getProcessInstanceId(), execution.getProcessInstanceId());
//					String jsonfromMap = mapper.writeValueAsString(map);
//					runtimeService.setVariable(pi.getParentId(), pi.getParentId(), jsonfromMap);
//				
//				}else{
//					Map<String,String> map=new HashMap<String,String>();
//					map.put(execution.getProcessInstanceId(),execution.getProcessInstanceId());
//					String jsonfromMap = mapper.writeValueAsString(map);
//					runtimeService.setVariable(pi.getParentId(), pi.getParentId(), jsonfromMap);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				runtimeService.setVariable(pi.getParentId(), "errMessage", e);
//				throw new BpmnError("BPM-ERROR-001");
//			}
//		}
	}
		
	
}
