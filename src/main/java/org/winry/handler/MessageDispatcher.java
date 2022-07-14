package org.winry.handler;

import com.google.protobuf.*;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);

    private static final Map<String, RequestHandlerInfo> handlerMap = new HashMap<>();

    public static void register(String cmd, Class<? extends AbstractRequestHandler<?>> messageHandlerClass) {
        Class<? extends MessageLite> messageType = (Class<? extends MessageLite>) ReflectionUtil.getGenericActualType(messageHandlerClass);
        handlerMap.put(cmd, new RequestHandlerInfo(messageHandlerClass, getDefaultInstance(messageType).getParserForType()));
        LOGGER.debug("Add handler, cmd:" + cmd + ", class:" + messageHandlerClass.getSimpleName());
    }

    public static <T extends MessageLite> void dispatch(String cmd, ByteString data, Channel channel) {
        if (handlerMap.containsKey(cmd)) {
            try {
                RequestHandlerInfo handlerInfo = handlerMap.get(cmd);
                AbstractRequestHandler<T> requestHandler =
                        (AbstractRequestHandler<T>) handlerInfo.handlerType.newInstance();
                requestHandler.setCmd(cmd);
                requestHandler.setChannel(channel);
                requestHandler.handle((T) handlerInfo.parser.parseFrom(data));
            } catch (InstantiationException | InvalidProtocolBufferException | IllegalAccessException e) {
                LOGGER.error("Failed to init handler", e);
            }
        } else {
            LOGGER.info("Unknown cmd:" + cmd);
        }
    }

    private static MessageLite getDefaultInstance(Class<? extends MessageLite> messageType) {
        try {
            Method method = messageType.getDeclaredMethod("getDefaultInstance");
            return (MessageLite) method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    static class RequestHandlerInfo {
        private final Class<? extends AbstractRequestHandler<?>> handlerType;
        private final Parser<? extends MessageLite> parser;

        public RequestHandlerInfo(Class<? extends AbstractRequestHandler<?>> handlerType, Parser<? extends MessageLite> parser) {
            this.handlerType = handlerType;
            this.parser = parser;
        }
    }
}
