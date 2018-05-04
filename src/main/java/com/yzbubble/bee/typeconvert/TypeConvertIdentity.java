package com.yzbubble.bee.typeconvert;

import java.util.Objects;

public class TypeConvertIdentity {
    private Class sourceClass;
    private Class targetClass;

    public TypeConvertIdentity(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    public Class getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(Class sourceClass) {
        Objects.requireNonNull(sourceClass, "sourceClass不能为空");
        this.sourceClass = sourceClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        Objects.requireNonNull(sourceClass, "targetClass不能为空");
        this.targetClass = targetClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeConvertIdentity that = (TypeConvertIdentity) o;

        if (!sourceClass.equals(that.sourceClass)) return false;
        return targetClass.equals(that.targetClass);
    }

    @Override
    public int hashCode() {
        int result = sourceClass.hashCode();
        result = 31 * result + targetClass.hashCode();
        return result;
    }
}
