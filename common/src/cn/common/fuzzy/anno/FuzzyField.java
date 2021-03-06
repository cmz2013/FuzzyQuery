package cn.common.fuzzy.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于静态设置模糊查询字段
 * 
 * 静态设置和动态设置分别独立使用，不能混合使用
 * 
 * @author chongming
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FuzzyField {
	/**
	 * 在模糊查询数据库表时，name可用于指定表字段名称，如果name空，表字段名称为POJO属性名
	 * @return
	 */
	String name() default "";
	
	Map map() default @Map(datas = {});
}
