package com.cdq.sc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueColumn {

    /**
     * 数据库中value字段的实际字段名称
     */
    String value() default "VALUE";

}
