package cn.com.workflow.common.vo;

import java.util.Map;

public class ActProcessInstanceVO extends BaseVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private boolean suspended;
	
	private boolean ended;
	
	private String activityId;
	
	private String processInstanceId;
	
	private String parentId;
	
	private String tenantId;
	
	private String name;
	
	private String description;
	
	private String processDefinitionId;
	
	private String processDefinitionName;
	
	private String processDefinitionKey;
	
	private Integer processDefinitionVersion;
	
	private String deploymentId;
	
	private String businessKey;
	
	private Map<String, Object> processVariables;
	
	private String localizedName;
	
	private String localizedDescription;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}


	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}

	public String getLocalizedName() {
		return localizedName;
	}

	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}

	public String getLocalizedDescription() {
		return localizedDescription;
	}

	public void setLocalizedDescription(String localizedDescription) {
		this.localizedDescription = localizedDescription;
	}

	@Override
	public String toString() {
		return "ActProcessInstanceVO [id=" + id + ", suspended=" + suspended + ", ended=" + ended + ", activityId="
				+ activityId + ", processInstanceId=" + processInstanceId + ", parentId=" + parentId + ", tenantId="
				+ tenantId + ", name=" + name + ", description=" + description + ", processDefinitionId="
				+ processDefinitionId + ", processDefinitionName=" + processDefinitionName + ", processDefinitionKey="
				+ processDefinitionKey + ", processDefinitionVersion=" + processDefinitionVersion + ", deploymentId="
				+ deploymentId + ", businessKey=" + businessKey + ", processVariables=" + processVariables
				+ ", localizedName=" + localizedName + ", localizedDescription=" + localizedDescription + "]";
	}
	
	
	
}
