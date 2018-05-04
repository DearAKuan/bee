package com.yzbubble.bee;

public class ResourceUtils {
    public static String getResourceRootPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
    }

    public static String getResourcePath(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name).getPath().substring(1);
    }
}
