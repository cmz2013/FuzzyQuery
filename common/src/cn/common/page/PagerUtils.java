package cn.common.page;

import java.util.List;

import cn.common.sort.Comparator;
import cn.common.sort.Order;
import cn.common.sort.SortUtils;

public class PagerUtils {
	/**
	 * POJO排序分页，返回总记录数和一页数据
	 * 
	 * 如果排序字段空则不排序
	 * 
	 * @param pager
	 * @return
	 * @throws NoSuchFieldException 
	 */
	public static Pager paging(
			List<? extends Comparator> dataCache, Pager pager) 
					throws NoSuchFieldException {
		
		List<? extends Comparator> subList = null;
		if(dataCache == null || dataCache.size() == 0 ) {
			pager.setRecords(0);
			return pager;
		}
		
		if (null != pager.getSidx() && null != pager.getOrder()) {
			SortUtils.sort(dataCache, Order.valueOf(pager.getOrder()));
		}
		
		if(pager.getPageSize() > dataCache.size())	 {
			subList = dataCache;
		} else if(pager.getPage()*pager.getPageSize() <= dataCache.size()) { 
			subList = dataCache.subList((pager.getPage()-1)*pager.getPageSize(), 
					pager.getPage()*pager.getPageSize());
		} else {	
			//当前记录数 > 数据总数
			subList = dataCache.subList(
					(pager.getPage()-1)*pager.getPageSize(), dataCache.size());
		}
		pager.setData(subList);
		pager.setRecords(dataCache.size());
		return pager;
	}

}
