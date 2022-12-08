package com.cdq.sc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IdColumn {

    /**
     * 数据库中主键id的实际字段名
     * @return
     */
    String value() default "ID";

}
