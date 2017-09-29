package cn.com.workflow.common.vo;

import java.util.Date;
import java.util.Map;

public class ActHisActInstListVO extends BaseVO {



	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String activityId;
	
	private String activityName;
	
	private String activityType;
	
	private String processDefinitionId;
	
	private String processInstanceId;
	
	private String executionId;
	
	private String taskId;
	
	private String calledProcessInstanceId;
	
	private String Assignee;
	
	private Date startTime;
	
	private Date endTime;
	
	private Long durationInMillis;
	
	private String tenantId;
	
	private String formKey;
	
	private Date time;
	
	private Date claimTime;
	
	private String name;
	
	private String conclusion;
	
	private String opinion;
	
	private Map<String, Object> processVariables;
	
	private Map<String, Object> taskLocalVariables;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCalledProcessInstanceId() {
		return calledProcessInstanceId;
	}

	public void setCalledProcessInstanceId(String calledProcessInstanceId) {
		this.calledProcessInstanceId = calledProcessInstanceId;
	}

	public String getAssignee() {
		return Assignee;
	}

	public void setAssignee(String assignee) {
		Assignee = assignee;
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

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		return "ActHisActInstListVO [id=" + id + ", activityId=" + activityId + ", activityName=" + activityName
				+ ", activityType=" + activityType + ", processDefinitionId=" + processDefinitionId
				+ ", processInstanceId=" + processInstanceId + ", executionId=" + executionId + ", taskId=" + taskId
				+ ", calledProcessInstanceId=" + calledProcessInstanceId + ", Assignee=" + Assignee + ", startTime="
				+ startTime + ", endTime=" + endTime + ", durationInMillis=" + durationInMillis + ", tenantId="
				+ tenantId + ", formKey=" + formKey + ", time=" + time + ", claimTime=" + claimTime + ", name=" + name
				+ ", conclusion=" + conclusion + ", opinion=" + opinion + ", processVariables=" + processVariables
				+ ", taskLocalVariables=" + taskLocalVariables + "]";
	}

	
	
	
	
}
