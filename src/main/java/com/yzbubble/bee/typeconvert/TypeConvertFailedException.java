package com.yzbubble.bee.typeconvert;

public class TypeConvertFailedException extends RuntimeException {
    public TypeConvertFailedException() {}
    public TypeConvertFailedException(String msg) { super(msg); }
    public TypeConvertFailedException(Throwable cause) { super(cause); }
    public TypeConvertFailedException(String msg, Throwable cause) { super(msg, cause); }
}
