package cn.common.fuzzy;

import java.util.Map;

public interface DataDict {
	/**
	 * 开发人员可以通过该方法，批量设置用户数据与DB数据或内存数据的映射关系
	 * 
	 * @return 
	 *  key：字段， value：子MAP(key：用户数据，value：表数据/内存数据)
	 */
	public Map<String, Map<String, String>> getDataDict();
}
