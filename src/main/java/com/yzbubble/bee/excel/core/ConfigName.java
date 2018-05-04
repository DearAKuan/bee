package com.yzbubble.bee.excel.core;

import java.util.Objects;

public class ConfigName {
    private String fullClassName;
    private String fieldName;
    private boolean isSpecial;

    public ConfigName(String fullClassName, String fieldName) {
        this.fullClassName = fullClassName;
        this.fieldName = fieldName;
        this.isSpecial = false;
    }

    public ConfigName(String fullClassName, String fieldName, boolean isSpecial) {
        this.fullClassName = fullClassName;
        this.fieldName = fieldName;
        this.isSpecial = isSpecial;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    @Override
    public String toString() {
        return "ConfigName{" +
                "fullClassName='" + fullClassName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", isSpecial=" + isSpecial +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigName that = (ConfigName) o;
        return isSpecial() == that.isSpecial() &&
                Objects.equals(getFullClassName(), that.getFullClassName()) &&
                Objects.equals(getFieldName(), that.getFieldName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullClassName(), getFieldName(), isSpecial());
    }
}
