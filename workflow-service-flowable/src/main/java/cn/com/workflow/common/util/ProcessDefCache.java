package cn.com.workflow.common.util;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
/**
 * 流程定义缓存类
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午9:57:35
 *
 */
@Service
public class ProcessDefCache {
	
	@Resource
	private RepositoryService repositoryService;

	private static final String ACT_CACHE = "actCache";
	private static final String ACT_CACHE_PD_ID_ = "pd_id_";
	
	/**
	 * 获得流程定义对象
	 * @param procDefId
	 * @return
	 */
	public ProcessDefinition get(String procDefId) {
		ProcessDefinition pd = (ProcessDefinition)CacheUtils.get(ACT_CACHE, ACT_CACHE_PD_ID_ + procDefId);
		if (pd == null) {
//			pd = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(pd);
			pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
			if (pd != null) {
				CacheUtils.put(ACT_CACHE, ACT_CACHE_PD_ID_ + procDefId, pd);
			}
		}
		return pd;
	}

	/**
	 * 获得流程定义的所有活动节点
	 * @param procDefId
	 * @return
	 */
	public Collection<FlowElement> getActivitys(String procDefId) {
		ProcessDefinition pd = get(procDefId);
		
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        return process.getFlowElements();
	}
	
	/**
	 * 获得流程定义活动节点
	 * @param procDefId
	 * @param activityId
	 * @return
	 */
	public FlowElement getActivity(String procDefId, String activityId) {
		ProcessDefinition pd = get(procDefId);
		if (pd != null) {
		    Collection<FlowElement> list = getActivitys(procDefId);
			if (list != null){
				for (FlowElement flowElement : list) {
					if (activityId.equals(flowElement.getId())){
						return flowElement;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取流程定义活动节点名称
	 * @param procDefId
	 * @param activityId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getActivityName(String procDefId, String activityId) {
	    FlowElement fe = getActivity(procDefId, activityId);
		if (fe != null) {
			return ObjectUtils.toString(fe.getName());
		}
		return null;
	}
}
