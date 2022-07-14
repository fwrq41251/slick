package org.winry.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import org.winry.proto.CommonProtos.ProtoMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProtobufUtil {

    public static <T> T fromBytes(Class<T> clazz, ByteString bytes) {
        try {
            Field field = clazz.getDeclaredField("PARSER");
            field.setAccessible(true);
            Method myMethod = field.getDeclaringClass().getDeclaredMethod("parseFrom", ByteString.class);
            return (T) myMethod.invoke(field, bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse bytes", e);
        }
    }

    public static ProtoMessage toMessage(String cmd, MessageLite message) {
        return ProtoMessage.newBuilder().setCmd(cmd).setData(message.toByteString()).build();
    }
}
