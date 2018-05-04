package com.yzbubble.bee.typeconvert;

public class NotFoundTypeConverterException extends RuntimeException {
    public NotFoundTypeConverterException() {}
    public NotFoundTypeConverterException(String msg) { super(msg); }
    public NotFoundTypeConverterException(Throwable cause) { super(cause); }
    public NotFoundTypeConverterException(String msg, Throwable cause) { super(msg, cause); }
}
