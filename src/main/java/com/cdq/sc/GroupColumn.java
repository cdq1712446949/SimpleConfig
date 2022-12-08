package com.cdq.sc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author caodongquan
 * @Date 2022/12/7 16:45
 * @discription
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupColumn {
    String value() default "GROUP";
}
