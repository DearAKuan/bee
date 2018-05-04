package com.yzbubble.bee.excel;

public class ExcelNotMatchFieldException extends RuntimeException {
    public ExcelNotMatchFieldException() {}
    public ExcelNotMatchFieldException(String msg) { super(msg); }
    public ExcelNotMatchFieldException(Throwable cause) { super(cause); }
    public ExcelNotMatchFieldException(String msg, Throwable cause) { super(msg, cause); }

}
