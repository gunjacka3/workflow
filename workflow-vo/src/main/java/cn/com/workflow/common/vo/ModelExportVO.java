package cn.com.workflow.common.vo;

public class ModelExportVO extends BaseVO{
	
	private String processName;
	
	private byte[] processDef;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public byte[] getProcessDef() {
		return processDef;
	}

	public void setProcessDef(byte[] processDef) {
		this.processDef = processDef;
	}
	
	
}
