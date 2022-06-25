package dev.lightdream.ticketsystem.utils;

import dev.lightdream.logger.Debugger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper {

    public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        while (clazz != Object.class) {
            Debugger.log("Methods of class " + clazz.getSimpleName() + ": " + Arrays.toString(clazz.getDeclaredMethods()));
            for (Method method : clazz.getDeclaredMethods()) {
                Debugger.log("Method " + method + " annotations: " + Arrays.toString(method.getAnnotations()));
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        Debugger.log("Methods with annotation " + annotation.getSimpleName() + ": " + methods);
        return methods;
    }

}
