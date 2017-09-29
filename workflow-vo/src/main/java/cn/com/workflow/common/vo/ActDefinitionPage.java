package cn.com.workflow.common.vo;

import java.util.List;

import cn.com.workflow.common.Page;

public class ActDefinitionPage {
	
	private List<ActDefinitionVO> actDefinitionVOs;
	
	private Page page;

	public List<ActDefinitionVO> getActDefinitionVOs() {
		return actDefinitionVOs;
	}

	public void setActDefinitionVOs(List<ActDefinitionVO> actDefinitionVOs) {
		this.actDefinitionVOs = actDefinitionVOs;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
