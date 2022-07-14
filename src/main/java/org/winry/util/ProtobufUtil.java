package org.winry.util;

import com.google.protobuf.ByteString;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProtobufUtil {

    public static <T> T fromBytes(Class<T> clazz, ByteString bytes) {
        try {
            Field field = clazz.getDeclaredField("PARSER");
            field.setAccessible(true);
            Method myMethod = field.getDeclaringClass().getDeclaredMethod("parseFrom", byte[].class);
            return (T) myMethod.invoke(field, bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse bytes", e);
        }
    }
}
