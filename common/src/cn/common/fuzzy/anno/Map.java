package cn.common.fuzzy.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 存放用户数据与表数据或内存数据的映射关系
 */
@Target(value = { ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Map {
	Data[] datas();
}
