package com.yzbubble.bee.typeconvert;

public enum TypeConvertOptionalStrategy {
    SOURCE2ANY,
    ANY2TARGET,
    ANY2ANY,
    SOURCE2ANY_ANY2ANY,
    ANY2TARGET_ANY2ANY,
    SOURCE2ANY_ANY2TARGET_ANY2ANY,
    ANY2TARGET_SOURCE2ANY_ANY2ANY,
    NONE;
}
