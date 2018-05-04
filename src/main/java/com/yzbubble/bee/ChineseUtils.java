package com.yzbubble.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseUtils {
    private static final String CHINESE_MATCH_REGEX = "[\\u4e00-\\u9fa5]+";

    public static List<String> extractChinese(String input) {
        if (input == null || input.trim().isEmpty()) { return null; }
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(CHINESE_MATCH_REGEX);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String extractFirstChinese(String input) {
        if (input == null || input.isEmpty()) { return null; }
        Pattern pattern = Pattern.compile(CHINESE_MATCH_REGEX);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
