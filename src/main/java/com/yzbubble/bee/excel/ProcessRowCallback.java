package com.yzbubble.bee.excel;

import com.yzbubble.bee.excel.core.SimpleExcelDataTable;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface ProcessRowCallback<T> {
    T processRow(SimpleExcelDataTable table, List<String> row, int rowIndex, Map<String, String> headerMap);
}
