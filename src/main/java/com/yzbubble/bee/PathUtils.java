package com.yzbubble.bee;


import java.nio.file.Paths;

public class PathUtils {
    /**
     * 返回路径的扩展名
     * @param path 源路径
     * @return 返回扩展名,不包含“.”，如果没有扩展名，将返回null
     */
    public static String getExtension(String path) {
        String ext = null;
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < path.length() - 1) {
            ext = path.substring(dotIndex + 1);
        }
        return ext;
    }

    public static String getFileName(String path) {
        if (path == null) { return null; }
        return Paths.get(path).getFileName().toString();
    }

    public static String getFileNameWithoutExtension(String path) {
        String fileName = getFileName(path);
        if (fileName != null) {
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                return fileName.substring(0, dotIndex);
            } else {
                return fileName;
            }
        }
        return "";

    }
}
