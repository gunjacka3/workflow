package cn.com.workflow.common.vo;

import java.util.List;

public class ActFromVO extends BaseVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String taskId;
	
	private String taskName;

	private String fromKey;
	
	private String opinion;
	
	private List<ActInsideFormVO> insideFroms;
	
	private List<ActInsideFormVO> variables;
	
	private List<ActFormValueVO> actions;
	

	public List<ActFormValueVO> getActions() {
		return actions;
	}

	public void setActions(List<ActFormValueVO> actions) {
		this.actions = actions;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getFromKey() {
		return fromKey;
	}

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public List<ActInsideFormVO> getInsideFroms() {
		return insideFroms;
	}

	public void setInsideFroms(List<ActInsideFormVO> insideFroms) {
		this.insideFroms = insideFroms;
	}

	public List<ActInsideFormVO> getVariables() {
		return variables;
	}

	public void setVariables(List<ActInsideFormVO> variables) {
		this.variables = variables;
	}	
	
	
	
	
}
