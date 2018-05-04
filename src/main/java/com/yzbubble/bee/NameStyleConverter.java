package com.yzbubble.bee;

/**
 * 命名风格转换器
 * @author yzbubble
 */
public class NameStyleConverter {
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_UNDERLINE = '_';

    /**
     * 转换成小驼峰命名风格
     * @param input
     * @return
     */
    public static String toCamcelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder builder = new StringBuilder();
        boolean hasSeparator = false;
        char[] chs = input.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (ch == CHAR_SPACE || ch == CHAR_UNDERLINE) {
                hasSeparator = true;
                continue;
            }

            if (hasSeparator) {
                hasSeparator = false;
                if (builder.length() != 0) {
                    ch = Character.toUpperCase(ch);
                }
            } else if (builder.length() == 0) {
                ch = Character.toLowerCase(ch);
            }

            builder.append(ch);
        }
        return builder.toString();
    }

    /**
     * 转换成 Pascal 命名风格
     * @param input
     * @return
     */
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder builder = new StringBuilder();
        boolean hasSeparator = false;
        char[] chs = input.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (ch == CHAR_SPACE || ch == CHAR_UNDERLINE) {
                hasSeparator = true;
                continue;
            }

            if (hasSeparator) {
                hasSeparator = false;
                ch = Character.toUpperCase(ch);
            } else if (builder.length() == 0) {
                ch = Character.toUpperCase(ch);
            }

            builder.append(ch);
        }
        return builder.toString();
    }

    /**
     * 转换成 Snake 命名风格
     * @param input
     * @return
     */
    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder builder = new StringBuilder();
        boolean hasSeparator = false;
        char[] chs = input.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (ch == CHAR_SPACE || ch == CHAR_UNDERLINE) {
                hasSeparator = true;
                continue;
            }

            if (hasSeparator || Character.isUpperCase(ch)) {
                hasSeparator = false;
                ch = Character.toLowerCase(ch);
                if (builder.length() != 0) {
                    builder.append(CHAR_UNDERLINE);
                }
            }

            builder.append(ch);
        }
        return builder.toString();
    }
}
