package pers.wengzc.hunterkit;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface HunterTarget {

    /**
     * 只检查主线程
     *
     * @return
     */
    boolean justMainThread() default false;

    /**
     * 时间约束(ms)
     *
     * @return
     */
    long timeConstraint() default -1;

    /**
     * 注解效果是否被子类继承
     *
     * @return
     */
    boolean inherited() default  false;

    /**
     * 注解表示的动作
     *
     * @return 包含/排除
     */
    Action action() default Action.Include;

}
