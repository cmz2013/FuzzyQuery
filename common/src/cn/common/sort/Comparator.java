package cn.common.sort;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.Locale;

/**
 * 用于POJO对象比较
 * 
 * @author Chongming
 *
 */
public class Comparator implements Comparable<Object> {
	
	protected String[] compareFields;
	
	/**
	 * 获取比较字段
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	private Field[] getFields() 
			throws NoSuchFieldException, SecurityException {
		
		if (null == compareFields || compareFields.length == 0) {
			return getClass().getDeclaredFields();
		} else {
			Field[] fds = new Field[compareFields.length];
			for (int i = 0; i < compareFields.length; i++) {
				fds[i] = getClass().getDeclaredField(compareFields[i]);
			}
			return fds;
		}
	}

	/**
	 * POJO比较算法
	 */
	@Override
	public int compareTo(Object o) {
		try {
			Field[] fds = getFields();
			for (Field fd : fds) {
				Object arg1 = null;
				Object arg2 = null;
				
				if (fd.isAccessible()) {
					arg1 = fd.get(this);
					arg2 = fd.get(o);
				} else {
					fd.setAccessible(true);
					arg1 = fd.get(this);
					arg2 = fd.get(o);
					fd.setAccessible(false);
				}
				
				if (null == arg1) {
					arg1 = "";
				}
				
				if (null == arg2) {
					arg2 = "";
				}
				
				int res = compare(arg1, arg2);
				if (0 != res) {
					return res;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * arg1与arg2比较大小
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	private int compare(Object arg1, Object arg2) {
		
		if (arg1 instanceof Number && 
				arg2 instanceof Number) {
			
			double num1 = ((Number) arg1).doubleValue();
			double num2 = ((Number) arg2).doubleValue();
			double res = num1 - num2;
			if (res == 0) {
				return 0;
			} else if (res > 0) {
				return 1;
			} else {
				return -1;
			}
		}
		
		return Collator.getInstance(
				Locale.CHINA).compare(arg1+"", arg2+"");
	}

	public void setCompareFields(String[] compareFields) {
		this.compareFields = compareFields;
	}

	public String[] getCompareFields() {
		return compareFields;
	}
	
}
