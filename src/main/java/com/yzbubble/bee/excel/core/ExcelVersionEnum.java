package com.yzbubble.bee.excel.core;

import com.yzbubble.bee.PathUtils;

/***
 * Excel 版本枚举
 */
public enum ExcelVersionEnum {
    XLS, XLSX;

    /**
     * 根据源路径获取 Excel 版本
     * @param path 源路径
     * @return 返回Excel版本,如果获取不到，则返回null
     */
    public static ExcelVersionEnum parse(String path) {
        if (path.endsWith(".xls")) {
            return XLS;
        }
        if (path.endsWith(".xlsx")) {
            return XLSX;
        }
        throw new RuntimeException("无法确定Excel的文件类型");
    }
}
