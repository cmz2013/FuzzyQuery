package cn.common.fuzzy.dao;

import java.util.List;

import javax.sql.DataSource;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

/**
 * Nutz框架实现Dao操作
 *
 */
public class NutzDao implements Dao {
	
	private NutDao nutzDao = null;
	
	public NutzDao(DataSource dataSource) {
		nutzDao = new NutDao(dataSource);
	}

	@Override
	public int count(Class<?> clazz, String condition) {
		return nutzDao.count(clazz, Cnd.wrap(condition));
	}

	@Override
	public List<?> query(Class<?> clazz, String condition,
			int pager, int pageSize) {
		
		return nutzDao.query(clazz, 
				Cnd.wrap(condition), 
				nutzDao.createPager(pager, pageSize));
	}
	
	@Override
	public int count(Class<?> clazz) {
		return nutzDao.count(clazz);
	}

	@Override
	public List<?> query(Class<?> clazz,
			int pager, int pageSize) {
		
		return nutzDao.query(clazz, null,
				nutzDao.createPager(pager, pageSize));
	}

	@Override
	public List<?> query(Class<?> clazz) {
		return nutzDao.query(clazz, null);
	}

	@Override
	public List<?> query(Class<?> clazz, String condition) {
		return nutzDao.query(clazz, Cnd.wrap(condition));
	}
}
