package cn.com.workflow.common;

import java.util.List;
import java.util.Map;

public class Page<T> {

	public static final int DEFAULT_PAGE_SIZE = 10;

	protected int pageNo = 1; // 当前页, 默认为第1页
	protected int pageSize = DEFAULT_PAGE_SIZE; // 每页记录数
	protected long totalRecord = -1; // 总记录数, 默认为-1, 表示需要查询
	protected int totalPage = -1; // 总页数, 默认为-1, 表示需要计算
	
	protected int start_index = 0;
	
	
	private Map ver;
	
	
	public Map getVer() {
		return ver;
	}

	public void setVer(Map ver) {
		this.ver = ver;
	}

	protected List<T> results; // 当前页记录List形式

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		computePageIndex();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getStart_index() {
		return start_index;
	}

	public void setStart_index(int start_index) {
		this.start_index = start_index;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		computeTotalPage();
		computePageIndex();
	}

	protected void computeTotalPage() {
		if (getPageSize() > 0 && getTotalRecord() > -1) {
			this.totalPage = (int) (getTotalRecord() % getPageSize() == 0 ? getTotalRecord() / getPageSize() : getTotalRecord() / getPageSize() + 1);
		}
	}
	
	protected void computePageIndex() {
		if (getPageNo()> 0 && getPageSize() > 0) {
			this.start_index=(pageNo -1) * pageSize;
		}
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append("Page [pageNo=").append(pageNo).append(", pageSize=").append(pageSize)
				.append(", totalRecord=").append(totalRecord < 0 ? "null" : totalRecord).append(", totalPage=")
				.append(totalPage < 0 ? "null" : totalPage);
		return builder.toString();
	}

	

}

