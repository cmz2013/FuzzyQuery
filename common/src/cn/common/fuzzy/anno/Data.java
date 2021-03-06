/**   
 * Copyright: 版权所有 ( c ) 北京启明星辰信息技术股份有限公司 2012。保留所有权利。
 * @author chongming
 * @date 2012-7-25 下午06:8:00
 */
package cn.common.fuzzy.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Data {
	/**
	 * 用户数据与表数据或内存数据的映射关系
	 * @return
	 */
	String userData();
	String dbData();
}
