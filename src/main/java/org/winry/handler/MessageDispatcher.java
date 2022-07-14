package org.winry.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.util.ProtobufUtil;
import org.winry.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);

    private static final Map<String, Class<? extends AbstractRequestHandler<?>>> handlerMap = new HashMap<>();

    public static void register(String cmd, Class<? extends AbstractRequestHandler<?>> messageHandlerClass) {
        handlerMap.put(cmd, messageHandlerClass);
    }

    public static <T extends MessageLite> void dispatch(String cmd, ByteString data, Channel channel) {
        if (handlerMap.containsKey(cmd)) {
            try {
                Class<? extends AbstractRequestHandler<?>> requestHandlerClass = handlerMap.get(cmd);
                AbstractRequestHandler<T> requestHandler =
                        (AbstractRequestHandler<T>) requestHandlerClass.getDeclaredConstructor(Channel.class).newInstance(channel);
                Class<T> messageType = (Class<T>) ReflectionUtil.getGenericActualType(requestHandlerClass);
                requestHandler.handle(ProtobufUtil.fromBytes(messageType, data));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                LOGGER.error("Failed to init handler", e);
            }
        } else {
            LOGGER.info("Unknown cmd:" + cmd);
        }
    }
}
