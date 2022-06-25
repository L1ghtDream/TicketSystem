package dev.lightdream.ticketsystem.utils;

import dev.lightdream.ticketsystem.annotation.EventListener;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.event.TicketEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionHelper {

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends EventListener> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

}
