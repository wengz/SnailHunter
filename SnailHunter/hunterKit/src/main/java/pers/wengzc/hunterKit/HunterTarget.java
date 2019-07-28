package pers.wengzc.hunterkit;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface HunterTarget {

    boolean justMainThread() default false;

    long timeConstraint() default -1;

    boolean inherited() default  false;

    Action action() default Action.Include;

}
