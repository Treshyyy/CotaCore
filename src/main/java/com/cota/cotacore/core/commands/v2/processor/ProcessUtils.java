package com.cota.cotacore.core.commands.v2.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessUtils {

    public static void invokeMethod(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
