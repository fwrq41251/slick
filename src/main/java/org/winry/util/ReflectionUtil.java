package org.winry.util;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by winry on 3/6/2017.
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Class<?> getGenericActualType(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>();

        for (Field field : clazz.getDeclaredFields()) {
            final int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers)) {
                fields.add(field);
            }
        }
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }

    public static Set<Class<?>> getClassesUnder(String myPackage) {
        try {
            return ClassPath.from(ReflectionUtil.class.getClassLoader()).getTopLevelClasses(myPackage).stream().map(c -> {
                try {
                    return Class.forName(c.getName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
