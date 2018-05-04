package com.yzbubble.bee.typeconvert;

@FunctionalInterface
public interface TypeConverter<T> {
    public Object convert(Object sourceValue, Class<T> targetClass);
}
