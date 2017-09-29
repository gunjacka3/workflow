package cn.com.workflow.common.vo;

/**
 * 流程定义，接口
 * @author Administrator
 *
 */
public interface ProcessDefinitionVO
{
	public String getId();
	public String getKey();
	public int getVersion();
	public String getDescription();
	public String getDiagramResourceName();
	public String getDeploymentId();
	public String getName();
	public String getResourceName();
}
