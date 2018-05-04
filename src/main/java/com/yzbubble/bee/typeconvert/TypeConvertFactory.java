package com.yzbubble.bee.typeconvert;

import com.yzbubble.bee.Unchecked;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

public class TypeConvertFactory {
    private Map<TypeConvertIdentity, TypeConverter> typeConverterRegistry = new HashMap<>();

    public TypeConvertFactory() {
        scan(DefaultTypeConverters.class);
    }

    public static TypeConvertFactory create() {
        return new TypeConvertFactory();
    }

    public void register(TypeConvertIdentity key, TypeConverter converter) {
        Objects.requireNonNull(key, "key不能为空");
        Objects.requireNonNull(converter, "converter不能为空");
        typeConverterRegistry.put(key, converter);
    }

    public void unregister(TypeConvertIdentity key) {
        Objects.requireNonNull(key, "key不能为空");
        typeConverterRegistry.remove(key);
    }

    public TypeConvertFactory scan(Class ... classes) {
        Objects.requireNonNull(classes, "classes参数值不能为空");
        Arrays.stream(classes).forEach(clazz -> {
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                Optional.ofNullable(method.getDeclaredAnnotation(TypeConverterFor.class)).ifPresent(anno -> {
                    register(new TypeConvertIdentity(anno.source(), anno.target()), (sourceValue, targetClass) -> {
                        int modifiers = method.getModifiers();
                        if (Modifier.isStatic(modifiers)) {
                            return Unchecked.of(() -> method.invoke(null, sourceValue, targetClass)).get();
                        } else {
                            return Unchecked.of(() -> method.invoke(clazz.newInstance(), sourceValue, targetClass)).get();
                        }
                    });
                });
            });
        });
        return this;
    }

    public Object convert(Object sourceValue, Class targetClass, TypeConvertOptionalStrategy optionalStrategy) {
        if (sourceValue == null) { return null; }
        Objects.requireNonNull("targetClass不能为空");
        Class sourceClass = sourceValue.getClass();
        if (sourceClass.equals(targetClass)) { return sourceValue; }

        TypeConvertIdentity key = new TypeConvertIdentity(sourceClass, targetClass);
        TypeConverter typeConverter = typeConverterRegistry.containsKey(key) ? typeConverterRegistry.get(key) : doConvertOptionalStrategy(key, optionalStrategy);
        if (typeConverter == null) {
            throw new NotFoundTypeConverterException(String.format("源类型:%s => 目标类型:%s", sourceClass.getCanonicalName(), targetClass.getCanonicalName()));
        }

        return typeConverter.convert(sourceValue, targetClass);
    }

    public <T> T convert(Object sourceValue, Class<T> targetClass) {
        return (T)convert(sourceValue, targetClass, TypeConvertOptionalStrategy.NONE);
    }

    private TypeConverter doConvertOptionalStrategy(TypeConvertIdentity originTypeConvertIdentity, TypeConvertOptionalStrategy optionalStrategy) {
        Supplier<TypeConvertIdentity> source2Any = (() -> new TypeConvertIdentity(originTypeConvertIdentity.getSourceClass(), Object.class));
        Supplier<TypeConvertIdentity> any2Target =  (() -> new TypeConvertIdentity(Object.class, originTypeConvertIdentity.getTargetClass()));
        Supplier<TypeConvertIdentity> any2Any = (() -> new TypeConvertIdentity(Object.class, Object.class));
        List<TypeConvertIdentity> keys = new ArrayList<>();
        switch (optionalStrategy) {
            case SOURCE2ANY:
                keys.add(source2Any.get());
                break;
            case ANY2TARGET:
                keys.add(any2Target.get());
                break;
            case ANY2ANY:
                keys.add(any2Any.get());
                break;
            case SOURCE2ANY_ANY2ANY:
                keys.add(source2Any.get());
                keys.add(any2Any.get());
                break;
            case ANY2TARGET_ANY2ANY:
                keys.add(any2Target.get());
                keys.add(any2Any.get());
                break;
            case SOURCE2ANY_ANY2TARGET_ANY2ANY:
                keys.add(source2Any.get());
                keys.add(any2Target.get());
                keys.add(any2Any.get());
                break;
            case ANY2TARGET_SOURCE2ANY_ANY2ANY:
                keys.add(any2Target.get());
                keys.add(source2Any.get());
                keys.add(any2Any.get());
                break;
            case NONE:
                break;
        }
        TypeConvertIdentity matchKey = keys.stream().filter(key -> typeConverterRegistry.containsKey(key)).findFirst().orElse(null);
        if (matchKey == null) { return null; }
        return typeConverterRegistry.get(matchKey);
    }
}
