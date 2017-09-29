package cn.com.workflow.common.vo;

import java.util.List;

import cn.com.workflow.common.Page;

public class ActHisProcessListPage {
	
	private List<ActHisProcessListVO> actHisProcessListVOs;
	
	private Page page;




	public List<ActHisProcessListVO> getActHisProcessListVOs() {
		return actHisProcessListVOs;
	}

	public void setActHisProcessListVOs(List<ActHisProcessListVO> actHisProcessListVOs) {
		this.actHisProcessListVOs = actHisProcessListVOs;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
