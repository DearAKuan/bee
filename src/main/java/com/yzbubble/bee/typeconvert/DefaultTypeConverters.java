package com.yzbubble.bee.typeconvert;

import com.yzbubble.bee.Unchecked;
import com.yzbubble.bee.pipeline.FilterResult;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTypeConverters {
    @TypeConverterFor(source = String.class, target = Object.class)
    public Object stringToAny(String sourceValue, Class targetClass) {
        if (sourceValue == null || sourceValue.isEmpty()) { return null; }
        Objects.requireNonNull(targetClass, "targetClass不能为空");
        if (String.class.equals(targetClass)) { return sourceValue; }

        Object result;
        try {
            Constructor constructor = targetClass.getConstructor(String.class);
            constructor.setAccessible(true);
            result = constructor.newInstance(sourceValue);
        } catch (Exception e) {
            throw new TypeConvertFailedException(String.format("(源类型:String;原始值:%s) => 目标类型:%s", sourceValue.toString(), targetClass.getCanonicalName()), e);
        }

        return result;
    }

    @TypeConverterFor(source = String.class, target = int.class)
    public Object stringToInt(String sourceValue, Class targetClass) {
        if (sourceValue == null || sourceValue.isEmpty()) { return 0; }
        Objects.requireNonNull(targetClass, "targetClass不能为空");
        return Integer.parseInt(sourceValue);
    }

    @TypeConverterFor(source = String.class, target = Date.class)
    public Object stringToDate(String sourceValue, Class<Date> targetClass) {
        if (sourceValue == null || sourceValue.isEmpty()) { return 0; }
        Objects.requireNonNull(targetClass, "targetClass不能为空");
        Pattern pattern = Pattern.compile("(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)(\\s*(?<hour>\\d+)(:(?<minute>\\d+)(:(?<second>\\d+)(\\.(?<millisecond>\\d+))?)?)?)?");
        Matcher m = pattern.matcher(sourceValue);
        if (m.find()) {
            String formatDateStr = String.format("%s-%s-%s %s:%s:%s.%s",
                    Optional.ofNullable(m.group("year")).orElse("0"),
                    Optional.ofNullable(m.group("month")).orElse("0"),
                    Optional.ofNullable(m.group("day")).orElse("0"),
                    Optional.ofNullable(m.group("hour")).orElse("0"),
                    Optional.ofNullable(m.group("minute")).orElse("0"),
                    Optional.ofNullable(m.group("second")).orElse("0"),
                    Optional.ofNullable(m.group("millisecond")).orElse("0")
                );
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return Unchecked.of(() -> formater.parse(formatDateStr)).get();
        } else {
            pattern = Pattern.compile("(?<month>\\d{2})/(?<day>\\d{2})/(?<year>\\d{2})");
            m = pattern.matcher(sourceValue);
            if (m.find()) {
                String formatDateStr = String.format("%s/%s/%s",
                        Optional.ofNullable(m.group("month")).orElse("0"),
                        Optional.ofNullable(m.group("day")).orElse("0"),
                        Optional.ofNullable(m.group("year")).orElse("0")
                );
                SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yy");
                return Unchecked.of(() -> formater.parse(formatDateStr)).get();
            }
        }
        return null;
    }

    public FilterResult universalFilter(Object sourceValue, Class targetClass) {
        if (sourceValue == null) { return FilterResult.stop(); }
        Objects.requireNonNull(targetClass, "targetClass不能为空");
        return FilterResult.goOn();
    }
}
