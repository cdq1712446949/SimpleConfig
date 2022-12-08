package com.cdq.sc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyColumn {

    /**
     * 数据库中表示key的实际字段名
     */
    String value() default "KEY";

}
