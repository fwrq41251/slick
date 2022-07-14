package org.winry.util;

import org.winry.handler.AbstractRequestHandler;
import org.winry.handler.MessageDispatcher;

public class HandlerAutoRegister {

    private final String myPackage;

    public HandlerAutoRegister(String myPackage) {
        this.myPackage = myPackage;
    }

    public void register() {
        ReflectionUtil.getClassesUnder(myPackage).forEach(c -> {
            String handlerName = c.getSimpleName();
            String cmd = getCmd(handlerName);
            Class<? extends AbstractRequestHandler<?>> handlerClass = (Class<? extends AbstractRequestHandler<?>>) c;
            MessageDispatcher.register(cmd, handlerClass);
        });
    }

    private String getCmd(String handlerName) {
        int end = handlerName.indexOf("handler");
        return handlerName.substring(0, end);
    }
}
