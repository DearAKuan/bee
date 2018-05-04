package com.yzbubble.bee.excel.core;

import com.yzbubble.bee.ReflectUtils;
import javafx.scene.control.DatePicker;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiWriter {
    private Workbook workbook;
    private int headerRowIndex = 0;

    public PoiWriter() {
        init(ExcelVersionEnum.XLSX);
    }

    public PoiWriter( ExcelVersionEnum version) {
        init(version);
    }

    public <T> PoiWriter write(String sheetName, List<T> list, Class<T> clazz, Map<String, String> headerMap) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> exportFileds = new ArrayList<>();

        // 打印标题行
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(headerRowIndex);
        final int[] headerColumnIndex = {0};
        headerMap.forEach((key, value) -> {
            Arrays.stream(fields).filter(x -> x.getName().equals(key)).findFirst().ifPresent(targetField -> {
                exportFileds.add(targetField);
                Cell cell = headerRow.createCell(headerColumnIndex[0]);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(value);
                headerColumnIndex[0]++;
            });
        });

        // 打印数据
        final int[] rowIndex = {headerRowIndex + 1};
        list.forEach(item -> {
            final int[] columnIndex = {0};
            final Row row = sheet.createRow(rowIndex[0]);
            exportFileds.forEach(targetField -> {
                targetField.setAccessible(true);
                try {
                    Cell cell = row.createCell(columnIndex[0]);
                    cell.setCellType(CellType.STRING);
                    Optional<Object> optionalValue = Optional.ofNullable(targetField.get(item));
                    if (optionalValue.isPresent()) {
                        cell.setCellValue(optionalValue.get().toString());
                    }
                    columnIndex[0]++;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            rowIndex[0]++;
        });

        return this;
    }

    public void export(OutputStream outputStream) {
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init(ExcelVersionEnum version) {
        this.workbook = createWorkbook(version);
    }

    private Workbook createWorkbook(ExcelVersionEnum version) {
        switch (version) {
            case XLS:
                return new HSSFWorkbook();
            case XLSX:
                return new XSSFWorkbook();
            default:
                throw new RuntimeException("无法确认Excel类型为xls还是xlsx。");
        }
    }
}
