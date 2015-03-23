package cn.common.sort;

import java.util.Collections;
import java.util.List;

public class SortUtils {
	
	public static final String SORD_DEST = "desc";
	
	public static final String SORD_ASC = "asc";
	
	/**
	 * POJO数据排序
	 * @param dataCache
	 * @param order
	 */
	public static void sort(List<? extends CommonComparator> 
			dataCache, String order) {
		
		if (dataCache.size() == 0) {
			return;
		} else {
			Collections.sort(dataCache);
			if (!SORD_DEST.equalsIgnoreCase(order)) {
				Collections.reverse(dataCache);
			}
		}
	}
}
