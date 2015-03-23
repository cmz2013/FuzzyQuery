package cn.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类，传递分页信息
 *
 */
@SuppressWarnings("serial")
public class Pager implements Serializable{

	/** 总页数 */
	private int total;
	
	/** 当前页码 */
	private int page = 1;
	
	/** 每页显示记录数 */
	private int pageSize = 20;

	/** 总记录数 */
	private int records;
	
	/** 记录集合 */
	private List<?> data;
	
	/** 排序字段 */
	private String sidx;
	
	/**	排序方式 */
	private String sord = "asc";
	
	public Pager() {
	}
	
	public int getTotal() {
		total = (int) Math.ceil((double) records / pageSize);
		return total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}
}
