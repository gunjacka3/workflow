package cn.com.workflow.common.vo;

import java.util.Map;

public class ActWorkListVO extends BaseVO {
	@Override
	public String toString() {
		return "ActWorkListVO [id=" + id + ", assignee=" + assignee + ", createTime=" + createTime + ", dueDate="
				+ dueDate + ", executionId=" + executionId + ", name=" + name + ", owner=" + owner
				+ ", processDefinitionId=" + processDefinitionId + ", taskDefinitionKey=" + taskDefinitionKey
				+ ", processVariables=" + processVariables + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8488832472535121137L;

	private String id;
	
	private String assignee;
	
	private java.util.Date createTime;
	
	private java.util.Date dueDate;
	
	private String executionId;
	
	private String name;
	
	private String owner;
	
	private String processDefinitionId;
	
	private String taskDefinitionKey;
	
	private Map<String,Object> processVariables;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(java.util.Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}
	
	
}
