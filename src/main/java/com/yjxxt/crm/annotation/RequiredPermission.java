package com.yjxxt.crm.annotation;

import java.lang.annotation.*;

//自定义注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    String code() default "";
}
