package com.yzbubble.bee.excel.core;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PoiReader {
    private Workbook workbook;
    private final ExcelVersionEnum version;

    public PoiReader(InputStream stream, ExcelVersionEnum version) throws IOException {
        this.version = version;
        this.workbook = createWorkbook(stream);
    }

    public PoiReader(InputStream stream) throws IOException {
        this(stream, ExcelVersionEnum.XLSX);
    }

    public PoiReader(String path) throws IOException {
        this(new FileInputStream(path), ExcelVersionEnum.parse(path));
    }

    /**
     * 填充 @link {SimpleExcelDataTable} 集合
     * @return 返回 @link {SimpleExcelDataTable} 集合,如果没有值，则返回空集合。
     */
    public List<SimpleExcelDataTable> fillDataTables(List<Integer> sheetIndexs, List<String> sheetNames) {
        List<SimpleExcelDataTable> result = new LinkedList<>();
        Set<Sheet> sheets = getSheets(sheetIndexs, sheetNames);
        sheets.forEach(sheet -> {
            SimpleExcelDataTable newTable = new SimpleExcelDataTable();
            for (int rowIndex = 0, lastRowNum = sheet.getLastRowNum(); rowIndex < lastRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                List<String> newRow = new LinkedList<>();
                for (int cellIndex = 0, lastCellNum = row.getLastCellNum(); cellIndex < lastCellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    newRow.add(getCellStringValue(cell));
                }
                newTable.addRow(newRow);
            }
            newTable.removeEmptyRows();
            result.add(newTable);
        });
        return result;
    }

    private String getCellStringValue(Cell cell) {
//        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//        return Optional.ofNullable(evaluator.evaluate(cell)).map(cellValue -> {
//            Object objectResult = null;
//            switch (cellValue.getCellTypeEnum()) {
//                case NUMERIC:
//                    objectResult = cellValue.getNumberValue();
//                    break;
//                case STRING:
//                    objectResult = cellValue.getStringValue();
//                    break;
//                case BLANK:
//                    objectResult = cell.getStringCellValue();
//                    break;
//                case BOOLEAN:
//                    objectResult = cellValue.getBooleanValue();
//                case FORMULA:
//                    break;
//                case ERROR:
//                    break;
//                case _NONE:
//                    break;
//            }
//            return Optional.ofNullable(objectResult).map(r -> r.toString()).orElse(null);
//        }).orElse(null);
        // 第二种方案
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    private Workbook createWorkbook(InputStream stream) throws IOException {
        switch (version) {
            case XLS:
                return new HSSFWorkbook(stream);
            case XLSX:
                return new XSSFWorkbook(stream);
            default:
                throw new RuntimeException("无法确认Excel类型为xls还是xlsx。");
        }
    }

    private Set<Sheet> getSheets(List<Integer> sheetIndexs, List<String> sheetNames) {
        Set<Sheet> sheets = new LinkedHashSet<Sheet>();
        if (sheetIndexs == null && sheetNames == null) {
            this.workbook.forEach(sheets::add);
        } else {
            Optional.ofNullable(sheetIndexs).ifPresent(indexs -> indexs.forEach(index -> {
                if (index >= 0  && index < this.workbook.getNumberOfSheets()) {
                    Optional.ofNullable(this.workbook.getSheetAt(index)).ifPresent(sheets::add);
                }
            }));
            Optional.ofNullable(sheetNames).ifPresent(names -> names.forEach(name -> {
                Optional.ofNullable((this.workbook.getSheet(name))).ifPresent(sheets::add);
            }));
        }
        return sheets;
    }
}
