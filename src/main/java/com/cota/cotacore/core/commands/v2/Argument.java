package com.cota.cotacore.core.commands.v2;


import com.cota.cotacore.core.commands.v2.processor.ExecuteTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME) // Retain annotation at runtime
@Target(ElementType.METHOD) // Target methods
public @interface Argument {
    int value();
    boolean suggest() default true;
    String requiredMethod() default "";
    String permission() default "";
    ExecuteTypes type() default ExecuteTypes.BOTH;
}
