package com.yzbubble.bee.pipeline;

import java.util.Objects;

public class FilterResult {
    private boolean isContinue;
    private Object result;

    public FilterResult(boolean isContinue, Object result) {
        this.isContinue = isContinue;
        this.result = result;
    }

    public FilterResult(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static FilterResult stop(Object result) {
        return new FilterResult(false, result);
    }

    public static FilterResult stop() {
        return new FilterResult(false, null);
    }

    public static FilterResult goOn(Object result) {
        return new FilterResult(true, result);
    }

    public static FilterResult goOn() {
        return new FilterResult(true, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterResult that = (FilterResult) o;
        return isContinue == that.isContinue &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isContinue, result);
    }

    @Override
    public String toString() {
        return "FilterResult{" +
                "isContinue=" + isContinue +
                ", result=" + result +
                '}';
    }
}
