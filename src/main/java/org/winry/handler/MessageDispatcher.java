package org.winry.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.Slick;
import org.winry.exception.NotLoginException;
import org.winry.pojo.User;
import org.winry.user.DefaultUserManager;
import org.winry.user.UserIdParam;
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

    public static <T extends MessageLite> void dispatch(String cmd, byte[] data, ChannelHandlerContext ctx, User user) {
        if (handlerMap.containsKey(cmd)) {
            try {
                RequestHandlerInfo handlerInfo = handlerMap.get(cmd);
                AbstractRequestHandler<T> requestHandler =
                        (AbstractRequestHandler<T>) handlerInfo.handlerType.newInstance();
                requestHandler.setCmd(cmd);
                requestHandler.setChannelHandlerContext(ctx);
                T t = (T) handlerInfo.parser.parseFrom(data);
                if (cmd.equals("login")) {
                    UserIdParam userIdParam = (UserIdParam) t;
                    user = getUser(userIdParam.getUserId(), ctx.channel());
                }
                if (user == null) {
                    throw new NotLoginException("user Not login");
                }
                requestHandler.handle(user, t);
            } catch (InstantiationException | InvalidProtocolBufferException | IllegalAccessException e) {
                LOGGER.error("Failed to init handler", e);
            }
        } else {
            LOGGER.info("Unknown cmd:" + cmd);
        }
    }

    public static <S extends MessageLite> void dispatch(String cmd, S s, ChannelHandlerContext ctx, User user) {
        if (handlerMap.containsKey(cmd)) {
            try {
                RequestHandlerInfo handlerInfo = handlerMap.get(cmd);
                AbstractRequestHandler<S> requestHandler =
                        (AbstractRequestHandler<S>) handlerInfo.handlerType.newInstance();
                requestHandler.setCmd(cmd);
                requestHandler.setChannelHandlerContext(ctx);
                requestHandler.handle(user, s);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Failed to init handler", e);
            }
        } else {
            LOGGER.info("Unknown cmd:" + cmd);
        }
    }

    private static User getUser(int userId, Channel channel) {
        User user;
        DefaultUserManager userManager = (DefaultUserManager) Slick.userManager();
        user = new User(userId, channel);
        userManager.add(user);
        return user;
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
