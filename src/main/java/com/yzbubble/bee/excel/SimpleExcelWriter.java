package com.yzbubble.bee.excel;

import com.yzbubble.bee.excel.core.ExcelVersionEnum;
import com.yzbubble.bee.excel.core.PoiWriter;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class SimpleExcelWriter {
    private PoiWriter poiWriter;

    public SimpleExcelWriter() {
        this.poiWriter = new PoiWriter();
    }

    public SimpleExcelWriter( ExcelVersionEnum version) {
        this.poiWriter = new PoiWriter(version);
    }

    public <T> SimpleExcelWriter write(String sheetName, List<T> list, Class<T> clazz, Map<String, String> headerMap) {
        poiWriter.write(sheetName, list, clazz, headerMap);
        return this;
    }

    public void export(OutputStream outputStream) {
        poiWriter.export(outputStream);
    }

    public void exportFile(String path) {
        poiWriter.exportFile(path);
    }
}
