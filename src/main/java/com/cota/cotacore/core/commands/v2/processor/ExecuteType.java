package com.cota.cotacore.core.commands.v2.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Retain annotation at runtime
@Target(ElementType.TYPE) // Target methods
public @interface ExecuteType {

    ExecuteTypes value();
}
