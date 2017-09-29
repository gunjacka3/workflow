package cn.com.workflow.common.vo;

import java.util.Date;
import java.util.Map;

public class ActHisProcessListVO extends BaseVO {



	@Override
	public String toString() {
		return "ActHisProcessListVO [id=" + id + ", businessKey=" + businessKey + ", processDefinitionId="
				+ processDefinitionId + ", startTime=" + startTime + ", endTime=" + endTime + ", durationInMillis="
				+ durationInMillis + ", startUserId=" + startUserId + ", deleteReason=" + deleteReason + ", name="
				+ name + ", processVariables=" + processVariables + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String businessKey;
	
	private String processDefinitionId;
	
	private Date startTime;
	
	private Date endTime;
	
	private Long durationInMillis;
	
	private String startUserId;
	
	private String deleteReason;
	
	private String name;
	
	private String superProcessInstanceId;
	
	private Map<String, Object> processVariables;

	public String getSuperProcessInstanceId() {
		return superProcessInstanceId;
	}

	public void setSuperProcessInstanceId(String superProcessInstanceId) {
		this.superProcessInstanceId = superProcessInstanceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
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

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
