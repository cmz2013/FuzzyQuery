package cn.common.sort;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * 用于POJO对象比较
 * 
 * @author Chongming
 *
 */
public abstract class CommonComparator implements Comparable<Object> {
	
	private String compareField ;
	
	public CommonComparator(String compareField) {
		this.compareField = compareField;
	}
	
	public String getCompareField() {
		return compareField;
	}

	/**
	 * 比较算法,必须初始化compareField
	 */
	@Override
	public int compareTo(Object o) {
		Comparator<Object> cmp = Collator.getInstance(Locale.CHINA);
		Object arg1 = null;
		Object arg2 = null;
		try {
			Field fd = getClass().getDeclaredField(compareField);
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
			
			if (arg1 instanceof Number && arg2 instanceof Number) {
				double num1 = ((Number) arg1).doubleValue();
				double num2 = ((Number) arg2).doubleValue();
				double res = num2 - num1;
				if (res == 0) {
					return 0;
				} else if (res > 0) {
					return 1;
				} else {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmp.compare(arg2+"", arg1+"");
	}
}