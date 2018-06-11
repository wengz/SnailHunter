package pers.wengzc.hunterpackage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被该注解标注的函数追踪其运行时间
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Examine {

}
