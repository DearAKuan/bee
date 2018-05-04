package com.yzbubble.bee.excel;

import com.yzbubble.bee.Unchecked;
import com.yzbubble.bee.excel.core.ExcelVersionEnum;
import com.yzbubble.bee.excel.core.PoiReader;
import com.yzbubble.bee.excel.core.SimpleExcelDataTable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleExcelReader {
    private Map<String, String> properties = new HashMap<>();
    private PoiReader poiReader;

    public SimpleExcelReader(InputStream stream, ExcelVersionEnum version) {
        poiReader = Unchecked.of(() -> new PoiReader(stream, version)).get();
    }

    public SimpleExcelReader(InputStream stream) {
        poiReader = Unchecked.of( () ->  new PoiReader(stream)).get();
    }

    public SimpleExcelReader(String path) {
        poiReader = Unchecked.of(() -> new PoiReader(path)).get();
    }

    public static SimpleExcelReader read(InputStream stream, ExcelVersionEnum version) {
        return Unchecked.of( () ->  new SimpleExcelReader(stream, version)).get();
    }

    public static SimpleExcelReader read(InputStream stream) {
        return Unchecked.of( () ->  new SimpleExcelReader(stream)).get();
    }

    public static SimpleExcelReader read(String path) {
        return Unchecked.of( () -> new SimpleExcelReader(path)).get();
    }


    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<SimpleExcelDataTable> fillDataTables(List<Integer> sheetIndexs, List<String> sheetNames) {
        return poiReader.fillDataTables(sheetIndexs, sheetNames);
    }
}

