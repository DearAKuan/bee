package com.yzbubble.bee.excel.core;

import org.junit.platform.commons.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigNameUtils {
    public static String NAME_SEPARATOR_REGEX = ":";
    public static String PAIR_SEPARATOR_REGEX = ",";
    public static String SPECIAL_FLAG_REGEX = "^#.*";

    public static List<ConfigName> parse(String configStr) {
        List<String> items = Arrays.stream(configStr.split(PAIR_SEPARATOR_REGEX))
                .filter(x -> x != null && StringUtils.isNotBlank(x))
                .map(x -> x.trim())
                .collect(Collectors.toList());
        return items.stream().map(item -> {
            boolean isSpecial = false;
            if (item.matches(SPECIAL_FLAG_REGEX)) {
                item = item.substring(1);
                isSpecial = true;
            }
            List<String> pair = Arrays.stream(item.split(NAME_SEPARATOR_REGEX))
                    .filter(x -> x != null && StringUtils.isNotBlank(x))
                    .map(x -> x.trim())
                    .collect(Collectors.toList());
            if (pair.size() == 1) {
                return new ConfigName(null, pair.get(0), isSpecial);
            } else if (pair.size() >= 2) {
                return new ConfigName(pair.get(0), pair.get(1), isSpecial);
            }
            return null;
        }).filter(x -> x != null).collect(Collectors.toList());
    }
}
