package pers.wengzc.hunterKit;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface HunterTarget {

    boolean justMainThread();

    long timeConstraint();

    boolean inherited();

    Action action();

}
