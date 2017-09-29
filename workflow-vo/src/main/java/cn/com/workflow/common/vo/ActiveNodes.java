package cn.com.workflow.common.vo;

import java.io.Serializable;
import java.util.List;

public class ActiveNodes implements Serializable
{
	private ActiveNode activeNode;
	
	private List<ActiveNode> activeNodes;
	
	public List<ActiveNode> getCurrentactiveNodes() {
		return currentactiveNodes;
	}

	public void setCurrentactiveNodes(List<ActiveNode> currentactiveNodes) {
		this.currentactiveNodes = currentactiveNodes;
	}

	private List<ActiveNode> currentactiveNodes;
	
	private String taskId;

	public ActiveNode getActiveNode()
	{
		return activeNode;
	}

	public void setActiveNode(ActiveNode activeNode)
	{
		this.activeNode = activeNode;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public List<ActiveNode> getActiveNodes()
	{
		return activeNodes;
	}

	public void setActiveNodes(List<ActiveNode> activeNodes)
	{
		this.activeNodes = activeNodes;
	}
	
	
}
