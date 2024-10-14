package com.hsx.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // 运行时触发
@Target({ElementType.METHOD}) // 做用在方法上
@Component
@Documented
public @interface LimitedRole {

    String [] limitedRoleCodeList() default {};

}
