package cn.common.fuzzy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.common.fuzzy.anno.Data;
import cn.common.fuzzy.anno.FuzzyField;
import cn.common.fuzzy.dao.Dao;
import cn.common.page.Pager;
import cn.common.page.PagerUtils;
import cn.common.sort.CommonComparator;
import cn.common.sort.SortUtils;

/**
 * 用于模糊查询,不区分大小写
 * 
 * 匹配字符串空，查询所有数据
 * 
 * @author chongming
 *
 */
public class FuzzyQuery {
	
	/**
	 * 基于数据库表的模糊查询
	 *  
	 *  @param conditions: 查询条件
	 *  @param pattern: 模糊查询内容
	 *  @param dataDict/conditions: 为空时，相当于执行 Pager query(Dao dao, Class clazz, String pattern, Pager pager)
	 *  @return：总的记录数，一页的数据，并支持排序
	 */
	@SuppressWarnings("rawtypes")
	public Pager query(Dao dao, Class clazz, String conditions, String pattern, Pager pager, DataDict dataDict) {
		Map<String, List<String>> fuzzyFields = getTableFuzzyFields(clazz, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return executeDao(dao, clazz, conditions, fuzzyFields, pattern, pager);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param conditions: 查询条件
	 * @param pattern: 模糊查询内容
	 * @param dataDict/conditions：为空时，相当于执行List query(Dao dao, Class clazz, String pattern)
	 */
	@SuppressWarnings("rawtypes")
	public List query(Dao dao, Class clazz, String conditions, String pattern, DataDict dataDict) {
		Map<String, List<String>> fuzzyFields = getTableFuzzyFields(clazz, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return executeDao(dao, clazz, conditions, fuzzyFields, pattern);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @return：总的记录数，一页的数据，并支持排序
	 */
	@SuppressWarnings("rawtypes")
	public Pager query(Dao dao, Class clazz, String pattern, Pager pager) {
		Map<String, List<String>> fuzzyFields = getTableFuzzyFields(clazz, pattern);
		return executeDao(dao, clazz, null, fuzzyFields, pattern, pager);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 */
	@SuppressWarnings("rawtypes")
	public List query(Dao dao, Class clazz, String pattern) {
		Map<String, List<String>> fuzzyFields = getTableFuzzyFields(clazz, pattern);
		return executeDao(dao, clazz, null, fuzzyFields, pattern);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param conditions: 查询条件
	 * @param pattern: 模糊查询内容
	 * @param dataDict/conditions：为空时，相当于执行List query(Dao dao, Class clazz, List<String> fields, String pattern) 
	 * @param fields: 动态设置模糊查询字段
	 */
	@SuppressWarnings("rawtypes")
	public List query(Dao dao, Class clazz, String conditions, List<String> fields, String pattern, DataDict dataDict) {
		Map<String, List<String>> fuzzyFields = getFuzzyFields(fields, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return executeDao(dao, clazz, conditions, fuzzyFields, pattern);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param conditions: 查询条件
	 * @param pattern: 模糊查询内容
	 * @param dataDict：为空时，相当于执行List query(Dao dao, Class clazz, List<String> fields, String pattern) 
	 * @param fields: 动态设置模糊查询字段
	 */
	@SuppressWarnings("rawtypes")
	public List query(Dao dao, Class clazz, List<String> fields, String pattern, DataDict dataDict) {
		Map<String, List<String>> fuzzyFields = getFuzzyFields(fields, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return executeDao(dao, clazz, null, fuzzyFields, pattern);
	}
	
	/**
	 * 基于数据库表的模糊查询
	 * 
	 * @param conditions: 查询条件
	 * @param pattern: 模糊查询内容
	 * @param fields: 动态设置模糊查询字段
	 */
	@SuppressWarnings("rawtypes")
	public List query(Dao dao, Class clazz, List<String> fields, String pattern) {
		Map<String, List<String>> fuzzyFields = getFuzzyFields(fields, pattern);
		return executeDao(dao, clazz, null, fuzzyFields, pattern);
	}
	
	/**
	 * 查询数据库
	 * 
	 * @param clazz
	 * @param conditions: 查询条件
	 * @param fuzzyFields
	 * @param pattern: 模糊查询内容
	 * @param pager
	 * @return 总的记录数，一页的数据，支持排序
	 */
	@SuppressWarnings({ "rawtypes" })
	private Pager executeDao(Dao dao, Class clazz, String conditions, 
			Map<String, List<String>> fuzzyFields, String pattern, Pager pager) {
		
		String likeStr = "";
		List datas = null;
		
		for (String field : fuzzyFields.keySet()) {
			if (fuzzyFields.get(field).size() == 0) {
				likeStr += "Upper(" + field + ") like '%" + pattern.toUpperCase() + "%' or ";
			} else {
				for (String patt : fuzzyFields.get(field)) {
					likeStr += "Upper(" + field + ") like '%" + patt.toUpperCase() + "%' or ";
				}
			}
		}
			
		if (!"".equals(likeStr)) {
			likeStr = likeStr.substring(0, likeStr.length() - 4);
			if (StringUtils.isNotBlank(conditions)) {
				conditions = "(" + conditions + ") and (" + likeStr + ")";
			} else {
				conditions = likeStr;
			}
			
			pager.setRecords(dao.count(clazz, conditions));
			if (null != pager.getSidx() && null != pager.getSord()) {
				conditions +=  " order by " + pager.getSidx() + " " + pager.getSord();
			}
			
			datas = dao.query(clazz, conditions, 
					pager.getPage(), pager.getPageSize());
			pager.setData(datas);
		} else {
			if (StringUtils.isNotBlank(conditions)) {
				pager.setRecords(dao.count(clazz, conditions));
			} else {
				pager.setRecords(dao.count(clazz));
			}
			
			if (null != pager.getSidx() && null != pager.getSord()) {
				if (StringUtils.isNotBlank(conditions)) {
					conditions +=  " order by " + pager.getSidx() + " " + pager.getSord();
				} else {
					conditions =  "order by " + pager.getSidx() + " " + pager.getSord();
				}
			}
			
			if (StringUtils.isNotBlank(conditions)) {
				datas = dao.query(clazz, conditions, 
						pager.getPage(), pager.getPageSize());
			} else {
				datas = dao.query(clazz, pager.getPage(), pager.getPageSize());
			}
			
			pager.setData(datas);
		}
		return pager;
	}
	
	/**
	 * 查询数据库
	 * 
	 * @param clazz
	 * @param conditions: 查询条件
	 * @param fuzzyFields
	 * @param pattern: 模糊查询内容
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private List executeDao(Dao dao, Class clazz, String conditions, 
			Map<String, List<String>> fuzzyFields, String pattern) {
		String likeStr = "";
		for (String field : fuzzyFields.keySet()) {
			if (fuzzyFields.get(field).size() == 0) {
				likeStr += "Upper(" + field + ") like '%" + pattern.toUpperCase() + "%' or ";
			} else {
				for (String patt : fuzzyFields.get(field)) {
					likeStr += "Upper(" + field + ") like '%" + patt.toUpperCase() + "%' or ";
				}
			}
		}
		
		if (!"".equals(likeStr)) {
			likeStr = likeStr.substring(0, likeStr.length() - 4);
			if (StringUtils.isNotBlank(conditions)) {
				conditions = "(" + conditions + ") and (" + likeStr + ")";
			} else {
				conditions = likeStr;
			}
			
			return dao.query(clazz, conditions);
		} else {
			if (StringUtils.isNotBlank(conditions)) {
				return dao.query(clazz, conditions);
			} else {
				return dao.query(clazz);
			}
		}
	}
	
	/**
	 * 获取table模糊查询字段
	 * 
	 * @param pattern: 模糊查询内容
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List<String>> getTableFuzzyFields(Class clazz, String pattern) {
		Map<String, List<String>> fuzzyFields = new HashMap<String, List<String>>();
		if (StringUtils.isEmpty(pattern)) {
			return fuzzyFields;
		}
		
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(FuzzyField.class)) {
				List<String> patts = new ArrayList<String>();
				initPatts(pattern, field.getAnnotation(FuzzyField.class), patts);
				FuzzyField fuzzyField = field.getAnnotation(FuzzyField.class);
				if (null != fuzzyField.name() && !"".equals(fuzzyField.name())) {
					fuzzyFields.put(fuzzyField.name(), patts);
				} else {
					fuzzyFields.put(field.getName(), patts);
				}
			}
		}
		return fuzzyFields;
	}
	
	/**
	 * 获取模糊查询字段
	 * @param fields
	 * @param pattern
	 * @return
	 */
	private Map<String, List<String>> getFuzzyFields(
			List<String> fields, String pattern) {
		Map<String, List<String>> fuzzyFields = new HashMap<String, List<String>>();
		if (StringUtils.isEmpty(pattern)) {
			return fuzzyFields;
		}
		
		for (String field : fields) {
			List<String> patts = new ArrayList<String>();
			fuzzyFields.put(field, patts);
		}
		return fuzzyFields;
	}

	/**
	 * 设置模糊字段匹配值
	 * 
	 * @param pattern: 模糊查询内容
	 */
	private void initPatts(String pattern, FuzzyField fuzzyField, List<String> patts) {
		cn.common.fuzzy.anno.Map map = fuzzyField.map();
		Data[] datas = map.datas();
		for (int i = 0; i < datas.length; i++) {
			if (datas[i].userData().contains(pattern)) {
				patts.add(datas[i].dbData());
			}
		}
	}

	/**
	 * 设置模糊字段匹配值
	 * 
	 * @param pattern: 模糊查询内容
	 */
	private void initPatts(Map<String, List<String>> fuzzyFields,
			String pattern, DataDict dataDict) {
		if (StringUtils.isEmpty(pattern)) {
			return;
		}
		
		if (null != dataDict) {
			Map<String, Map<String, String>> mappMap = dataDict.getDataDict();
			for (String tbField : mappMap.keySet()) {
				if (null != fuzzyFields.get(tbField)) {
					Map<String, String> mapp = mappMap.get(tbField);
					for (String webData : mapp.keySet()) {
						if (webData.contains(pattern)) {
							List<String> list = fuzzyFields.get(tbField);
							if (null != list) {
								list.add(mapp.get(webData));
							}
						} 
					}
				}
			}
		}
	}

	/**
	 * 获取Object模糊查询字段
	 * @param clazz
	 * @param pattern: 模糊查询内容
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List<String>> getObjectFuzzyFields(Class clazz, String pattern) {
		Map<String, List<String>> fuzzyFields = new HashMap<String, List<String>>();
		if (StringUtils.isEmpty(pattern)) {
			return fuzzyFields;
		}
		
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(FuzzyField.class)) {
				List<String> patts = new ArrayList<String>();
				initPatts(pattern, field.getAnnotation(FuzzyField.class), patts);
				fuzzyFields.put(field.getName(), patts);
			}
		}
		return fuzzyFields;
	}
	
	/**
	 * 查询内存数据
	 * @param dataCache 
	 * @param fuzzyFields
	 * @param pattern: 模糊查询内容
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private List filterData(Collection dataCache, Map<String, 
			List<String>> fuzzyFields, String pattern) throws Exception {
		
		List<Object> resList = new LinkedList<>();
		pattern = pattern.toUpperCase();
		
		Iterator itor = dataCache.iterator();
		while (itor.hasNext()) {
			Object data = itor.next();
			
			fieldFlag : for (String field : fuzzyFields.keySet()) {
				Field fd = data.getClass().getDeclaredField(field);
				
				String value = "";
				if (fd.isAccessible()) {
					value += fd.get(data);
				} else {
					fd.setAccessible(true);
					value += fd.get(data);
					fd.setAccessible(false);
				}
				value = value.toUpperCase();
				
				if (fuzzyFields.get(field).size() == 0) {
					if (value.contains(pattern)) {
						resList.add(data);
						break;
					}
				} else {
					for (String patt : fuzzyFields.get(field)) {
						if (value.contains(patt)) {
							resList.add(data);
							break fieldFlag;
						}
					}
				}
			}
		}
		return resList;
	}
	
	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param fields：动态指定模糊查询属性
	 * @param pattern: 模糊查询内容
	 * @param dataDict：如果为空，相当于执行List query(Collection dataCache, List<String> fields, String pattern)
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List query(Collection dataCache, List<String> fields, String pattern,  DataDict dataDict) throws Exception {
		
		if (null == dataCache || dataCache.size() == 0) {
			return new LinkedList<>();
		} else if (StringUtils.isEmpty(pattern)) {
			return new LinkedList<>(dataCache);
		}
		
		Map<String, List<String>> fuzzyFields = getFuzzyFields(fields, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return filterData(dataCache, fuzzyFields, pattern);
	}
	
	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @param fields：动态指定模糊查询属性
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List query(Collection dataCache, List<String> fields, String pattern) throws Exception {
		
		if (null == dataCache || dataCache.size() == 0) {
			return new LinkedList<>();
		} else if (StringUtils.isEmpty(pattern)) {
			return new LinkedList<>(dataCache);
		}
		
		Map<String, List<String>> fuzzyFields = getFuzzyFields(fields, pattern);
		return filterData(dataCache, fuzzyFields, pattern);
	}

	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @param dataDict：如果为空，相当于执行List query(Collection dataCache, String pattern)
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List query(Collection dataCache, String pattern,  DataDict dataDict) throws Exception {
		
		if (null == dataCache || dataCache.size() == 0) {
			return new LinkedList<>();
		} else if (StringUtils.isEmpty(pattern)) {
			return new LinkedList<>(dataCache);
		}
		
		Class clazz = dataCache.iterator().next().getClass();
		Map<String, List<String>> fuzzyFields = getObjectFuzzyFields(clazz, pattern);
		initPatts(fuzzyFields, pattern, dataDict);
		return filterData(dataCache, fuzzyFields, pattern);
	}
	
	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List query(Collection dataCache, 
			String pattern) throws Exception {
		
		if (null == dataCache || dataCache.size() == 0) {
			return new LinkedList<>();
		} else if (StringUtils.isEmpty(pattern)) {
			return new LinkedList<>(dataCache);
		}
		
		Class clazz = dataCache.iterator().next().getClass();
		Map<String, List<String>> fuzzyFields = getObjectFuzzyFields(clazz, pattern);
		return filterData(dataCache, fuzzyFields, pattern);
	}
	

	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @param dataDict：如果为空，相当于执行Pager query(List dataCache, String pattern, Pager pager)
	 * @return：总的记录数，一页的数据，并支持排序
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public Pager query(List<? extends CommonComparator> dataCache, 
			String pattern, Pager pager, DataDict dataDict) throws Exception {
		
		if (null != pager.getSidx() && null != pager.getSord()) {
			SortUtils.sort(dataCache, pager.getSord());
		}
		
		return PagerUtils.paging(query(dataCache, pattern, dataDict), pager);
	}
	
	/**
	 * 基于内存数据的模糊查询
	 * 
	 * @param pattern: 模糊查询内容
	 * @return：总的记录数，一页的数据，并支持排序
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public Pager query(List<? extends CommonComparator> dataCache, 
			String pattern, Pager pager) throws Exception {
		return PagerUtils.paging(query(dataCache, pattern), pager);
	}
}