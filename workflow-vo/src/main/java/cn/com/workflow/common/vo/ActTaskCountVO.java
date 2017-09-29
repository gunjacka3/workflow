package cn.com.workflow.common.vo;

public class ActTaskCountVO extends BaseVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String templateName;
	
	private long count;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	

}
