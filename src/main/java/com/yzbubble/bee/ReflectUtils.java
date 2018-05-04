package com.yzbubble.bee;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class ReflectUtils {
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    public static <T> void setInstanceFieldValue(Class<T> clazz, String fieldName, Object value) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);
        Unchecked.of(() -> {
            Field targetField = clazz.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(null, value);
        }).run();
    }

    public static <T> void setStaticFieldValue(Object target, String fieldName, Object value) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(fieldName);
        Unchecked.of(() -> {
            Field targetField = target.getClass().getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(null, value);
        }).run();
    }
}

