package cn.common.fuzzy.dao;

import java.util.List;

public interface Dao {
	/**
	 * 统计条数
	 * @param clazz: ORM
	 * @param condition: 查询条件
	 * @return
	 */
	public abstract int count(Class<?> clazz, String condition);
	
	/**
	 * 统计条数
	 * @param clazz: ORM
	 * @return
	 */
	public abstract int count(Class<?> clazz);

	/**
	 * 查询数据
	 * @param clazz: ORM
	 * @param condition: 查询条件
	 * @param pager
	 * @param pageSize
	 * @return
	 */
	public abstract List<?> query(Class<?> clazz, String condition, int pager, int pageSize);

	/**
	 * 查询数据
	 * @param clazz: ORM
	 * @param pager
	 * @param pageSize
	 * @return
	 */
	public abstract List<?> query(Class<?> clazz, int pager, int pageSize);

	/**
	 * 查询数据
	 * @param clazz: ORM
	 * @return
	 */
	public abstract List<?> query(Class<?> clazz);
	
	/**
	 * 查询数据
	 * @param clazz: ORM
	 * @param condition: 查询条件
	 * @return
	 */
	public abstract List<?> query(Class<?> clazz, String condition);
}
