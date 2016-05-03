package com.lncosie.robot.log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/4/21.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
   String value() default "";
}
