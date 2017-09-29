package cn.com.workflow.mybatis.model;

import java.util.Date;

import cn.com.workflow.common.vo.BaseVO;


public class WfmidLoginfo extends BaseVO{
    private String id;

    private String taskId;
    
    private String processId;

    private Date logTime;
    
    private String activityName;

    public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	private String operationType;

    private String infoText="";

    public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}


}