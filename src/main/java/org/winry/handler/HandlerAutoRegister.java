package org.winry.handler;

import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import org.winry.util.ReflectionUtil;

public class HandlerAutoRegister {

    public static void register() {
        ReflectionUtil.getClassesUnder("com.aemobile.handler").forEach(c -> {
            String cmd = c.getAnnotation(HandlerType.class).value();
            Class<? extends EventHandler<?>> eventHandlerClass = (Class<? extends EventHandler<?>>) c;
            EventDispatcher.register(cmd, eventHandlerClass);
        });
    }
}
