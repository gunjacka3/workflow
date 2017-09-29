package cn.com.workflow.common.vo;

import java.util.Date;
import java.util.Map;

public class ActHisTaskListVO extends BaseVO {



	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String description;
	
	private int priority;
	
	private String owner;
	
	private String assignee;
	
	private String processInstanceId;
	
	private String executionId;
	
	private String processDefinitionId;
	
	private Date createTime;
	
	private Date dueDate;
	
	private String taskDefinitionKey;
	
	private String category;
	
	private String parentTaskId;
	
	private String formKey;
	
	private String deleteReason;
	
	private Date startTime;
	
	private Date endTime;
	
	private Date claimTime;
	
	private String name;
	
	private String conclusion;
	
	private String opinion;
	
	private Map<String, Object> processVariables;
	
	private Map<String, Object> taskLocalVariables;

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}

	public Map<String, Object> getTaskLocalVariables() {
		return taskLocalVariables;
	}

	public void setTaskLocalVariables(Map<String, Object> taskLocalVariables) {
		this.taskLocalVariables = taskLocalVariables;
	}

	@Override
	public String toString() {
		return "ActHisTaskListVO [id=" + id + ", description=" + description + ", priority=" + priority + ", owner="
				+ owner + ", assignee=" + assignee + ", processInstanceId=" + processInstanceId + ", executionId="
				+ executionId + ", processDefinitionId=" + processDefinitionId + ", createTime=" + createTime
				+ ", dueDate=" + dueDate + ", taskDefinitionKey=" + taskDefinitionKey + ", category=" + category
				+ ", parentTaskId=" + parentTaskId + ", formKey=" + formKey + ", deleteReason=" + deleteReason
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", claimTime=" + claimTime
				+ ", processVariables=" + processVariables + ", taskLocalVariables=" + taskLocalVariables + "]";
	}

	
	
	
}
