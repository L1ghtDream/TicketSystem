package dev.lightdream.ticketsystem.manager;

import dev.lightdream.ticketsystem.annotation.EventHandler;
import dev.lightdream.ticketsystem.event.TicketEvent;
import dev.lightdream.ticketsystem.utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final List<EventMapper> eventMappers = new ArrayList<>();

    public EventManager() {

    }

    public void register(Object object) {
        getEventMapper(object).register();
    }

    public EventMapper getEventMapper(Object object) {
        for (EventMapper eventMapper : eventMappers) {
            if (eventMapper.object == object) {
                return eventMapper;
            }
        }

        EventMapper eventMapper = new EventMapper(object);
        eventMappers.add(eventMapper);
        return eventMapper;
    }

    public void fire(TicketEvent event) {
        for (EventMapper eventMapper : eventMappers) {
            eventMapper.fire(event);
        }
    }

    public static class EventMapper {

        private final Object object;
        private final List<EventMethod> eventMethods;

        public EventMapper(Object object) {
            this.object = object;
            this.eventMethods = new ArrayList<>();
        }

        public void register() {
            ReflectionHelper.getMethodsAnnotatedWith(object.getClass(), EventHandler.class).forEach(method -> {
                if (method.getParameters().length == 0) {
                    return;
                }

                Class<?> eventClassUnchecked = method.getParameters()[0].getType();
                Class<? extends TicketEvent> eventClass;

                try {
                    //noinspection unchecked
                    eventClass = (Class<? extends TicketEvent>) eventClassUnchecked;
                } catch (Exception e) {
                    return;
                }

                getEventMethod(eventClass).addMethod(method);
            });
        }

        public EventMethod getEventMethod(Class<? extends TicketEvent> clazz) {
            for (EventMethod eventMethod : eventMethods) {
                if (eventMethod.clazz.equals(clazz)) {
                    return eventMethod;
                }
            }

            EventMethod method = new EventMethod(clazz);
            eventMethods.add(method);
            return method;
        }

        public void fire(TicketEvent event) {
            getEventMethod(event.getClass()).fire(object, event);
        }

        @Override
        public String toString() {
            return "EventMapper{" +
                    "object=" + object +
                    ", eventMethods=" + eventMethods +
                    '}';
        }
    }

    public static class EventMethod {

        private final Class<? extends TicketEvent> clazz;
        private final List<Method> methods;

        public EventMethod(Class<? extends TicketEvent> clazz) {
            this.clazz = clazz;
            this.methods = new ArrayList<>();
        }

        public void addMethod(Method method) {
            methods.add(method);
        }

        public void fire(Object object, TicketEvent event) {
            for (Method method : methods) {
                try {
                    method.invoke(object, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public String toString() {
            return "EventMethod{" +
                    "clazz=" + clazz +
                    ", methods=" + methods +
                    '}';
        }
    }


}
