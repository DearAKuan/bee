package com.yzbubble.bee.excel.core;

import com.yzbubble.bee.Unchecked;
import com.yzbubble.bee.typeconvert.TypeConvertFactory;
import com.yzbubble.bee.typeconvert.TypeConvertOptionalStrategy;
import com.yzbubble.bee.excel.ExcelNotMatchFieldException;
import com.yzbubble.bee.excel.ProcessRowCallback;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class SimpleExcelDataTable {
    private static final Map<String, String> properties = new HashMap<>();

    private final List<List<String>> rows = new LinkedList<>();
    private String name;
    private int headerSize;
    private boolean hasManualSetHeaderSize = false;
    private int headerRowIndex = 0;

    public SimpleExcelDataTable() {
    }

    public SimpleExcelDataTable(String name) {
        this.name = name;
    }

    /**
     * 添加新行
     *
     * @param row 新行
     */
    public boolean addRow(List<String> row) {
        if (row != null && row.size() <= 0) {
            return false;
        }
        return rows.add(row);
    }

    /**
     * 根据下标获取某一行
     *
     * @param index 行下标
     * @return 行
     */
    public List<String> getRow(int index) {
        if (index < 0 && index < this.rows.size()) {
            return null;
        }
        return this.rows.get(index);
    }

    /**
     * 根据下标获取头部名称
     *
     * @param headerColumnIndex 头部下标
     * @return 头部名称
     */
    public String getHeaderColumnName(int headerColumnIndex) {
        return getHeaderRow().get(headerColumnIndex);
    }

    /**
     * 获取头部行
     *
     * @return 头部行
     */
    public List<String> getHeaderRow() {
        if (this.headerRowIndex < 0 || this.headerRowIndex >= this.rows.size()) {
            return null;
        }
        return getRow(this.headerRowIndex);
    }

    /**
     * 迭代访问数据表中的每一行
     *
     * @param action 处理行的方法
     */
    public void forEach(Consumer<List<String>> action) {
        if (action == null) {
            return;
        }
        for (List<String> str : rows) {
            action.accept(str);
        }
    }

    /**
     * 迭代访问数据表中的每一行并且跳过配置为表头的一行。
     *
     * @param action 处理行的方法
     */
    public void forEachWithoutHeader(Consumer<List<String>> action) {
        if (action == null) {
            return;
        }
        for (int i = 0, rowsSize = rows.size(); i < rowsSize; i++) {
            if (i == headerRowIndex) {
                continue;
            }
            List<String> str = rows.get(i);
            action.accept(str);
        }
    }

    /**
     * 删除表格空行
     */
    public void removeEmptyRows() {
        final Set<List<String>> deleteRows = new HashSet<>();
        this.rows.forEach(row -> {
            boolean isEmptyRow = true;
            for (String cell : row) {
                if (cell != null && !cell.isEmpty()) {
                    isEmptyRow = false;
                    break;
                }
            }
            if (isEmptyRow) {
                deleteRows.add(row);
            }
        });
        this.rows.removeAll(deleteRows);
    }

    public int getHeaderRowIndex() {
        return headerRowIndex;
    }

    public void setHeaderRowIndex(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getHeaderSize() {
        if (!hasManualSetHeaderSize && this.headerRowIndex >= 0 && this.headerRowIndex < this.rows.size()) {
            return this.rows.get(this.headerRowIndex).size();
        }
        return this.headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
        hasManualSetHeaderSize = true;
    }

    public <T> List<T> toObjects(Map<String, String> headerMap, ProcessRowCallback<T> processRowCallback) {
        List<T> result = new LinkedList<>();
        List<List<String>> rows = this.getRows();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            if (getHeaderRowIndex() == rowIndex) {
                continue;
            }
            List<String> row = rows.get(rowIndex);
            T obj = processRowCallback.processRow(this, row, rowIndex, headerMap);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    public <T> List<T> toSingleTypeObjects(Class<T> clazz, Map<String, String> originHeaderMap, boolean ignoreNotInHeaderMap, boolean ignoreNotFoundField) {
        return toObjects(originHeaderMap, (table, row, rowIndex, headerMap) -> {
            T targetObj = Unchecked.of(() -> clazz.newInstance()).get();
            for (int columnIndex = 0; columnIndex < Math.min(row.size(), table.getHeaderSize()); columnIndex++) {
                String cell = row.get(columnIndex);
                String headerName = table.getHeaderColumnName(columnIndex);

                // 获取类名和字段名称
                String fullName = null;
                if (headerMap.containsKey(headerName)) {
                    fullName = headerMap.get(headerName);
                } else if (ignoreNotInHeaderMap) {
                    continue;
                } else {
                    fullName = headerName;
                }
                List<String> nameParts = Arrays.stream(fullName.split(":"))
                        .filter(x -> x != null && StringUtils.isNotBlank(x))
                        .map(x -> x.trim())
                        .collect(Collectors.toList());
                if (nameParts.size() < 1) {
                    if (ignoreNotFoundField) {
                        continue;
                    }
                    throw new ExcelNotMatchFieldException(String.format("Excel头部名称：\"%s\"; 配置字段名称 -> \"%s\"", headerName, fullName));
                }
                String classFullName = nameParts.size() >= 2 ? nameParts.get(0) : null;
                String fieldName = nameParts.size() >= 2 ? nameParts.get(1) : nameParts.get(0);

                // 如果不是当前想要转换的目标类，自动忽视掉
                if (classFullName != null && !classFullName.startsWith(clazz.getCanonicalName())) {
                    continue;
                }

                // 设置字段的值
                try {
                    Field targetField = clazz.getDeclaredField(fieldName);
                    targetField.setAccessible(true);
                    Object transformdValue = TypeConvertFactory.create().convert(cell, targetField.getType(), TypeConvertOptionalStrategy.SOURCE2ANY);
                    targetField.set(targetObj, transformdValue);
                } catch (NoSuchFieldException e) {
                    if (ignoreNotFoundField) {
                        continue;
                    }
                    throw new ExcelNotMatchFieldException(String.format("excel头部名称：\"%s\"; 配置字段名称 -> \"%s\"", headerName, fullName));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            return targetObj;
        });
    }

    public List<Map<Class, Object>> toMultiTypeObjects(Map<String, String> originHeaderMap, boolean ignoreNotInHeaderMap, boolean ignoreNotFoundField) {
        return toObjects(originHeaderMap, (table, row, rowIndex, headerMap) -> {
            Map<Class, Object> map = new HashMap<>();
            for (int columnIndex = 0; columnIndex < Math.min(row.size(), table.getHeaderSize()); columnIndex++) {
                String cell = row.get(columnIndex);
                final String headerName = table.getHeaderColumnName(columnIndex);

                // 获取类名和字段名称
                final String configNameStr;
                if (headerMap.containsKey(headerName)) {
                    configNameStr = headerMap.get(headerName);
                } else if (ignoreNotInHeaderMap) {
                    continue;
                } else {
                    configNameStr = headerName;
                }

                List<ConfigName> configNames = ConfigNameUtils.parse(configNameStr);
                configNames.forEach(configName -> {
                    if (configName.isSpecial()) { return; }
                    if (configName.getFullClassName() == null) {
                        if (ignoreNotFoundField) { return; }
                        throw new ExcelNotMatchFieldException(String.format("Excel头部名称：\"%s\"; 配置字段名称 -> \"%s\"", headerName, configNameStr));
                    }
                    try {
                        // 创建字段所对应类型的对象
                        Object targetObj = null;
                        Class targetClass = Class.forName(configName.getFullClassName());
                        if (map.containsKey(targetClass)) {
                            targetObj = map.get(targetClass);
                        } else {
                            targetObj = targetClass.newInstance();
                            map.put(targetClass, targetObj);
                        }

                        // 设置字段的值
                        Field targetField = targetObj.getClass().getDeclaredField(configName.getFieldName());
                        targetField.setAccessible(true);
                        Object transformdValue = TypeConvertFactory.create().convert(cell, targetField.getType(), TypeConvertOptionalStrategy.SOURCE2ANY);
                        targetField.set(targetObj, transformdValue);
                    } catch (ClassNotFoundException e) {
                        if (ignoreNotFoundField) { return; }
                        throw new ExcelNotMatchFieldException(String.format("excel头部名称：\"%s\"; 配置字段名称 -> \"%s\"", headerName, configNameStr));
                    } catch (NoSuchFieldException e) {
                        if (ignoreNotFoundField) { return; }
                        throw new ExcelNotMatchFieldException(String.format("excel头部名称：\"%s\"; 配置字段名称 -> \"%s\"", headerName, configNameStr));
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return map;
        });
    }

    public String get(int x, int y) {
        return this.getRow(y).get(x);
    }

    public SimpleExcelDataTable putProperty(String key, String value) {
        if (key != null) {
            properties.put(key, value);
        }
        return this;
    }

    public SimpleExcelDataTable putProperties(Map<String, String> map) {
        if (map != null) {
            properties.putAll(map);
        }
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "SimpleExcelDataTable{" +
                "rows=" + rows +
                ", name='" + name + '\'' +
                ", headerSize=" + headerSize +
                ", headerRowIndex=" + headerRowIndex +
                '}';
    }
}
