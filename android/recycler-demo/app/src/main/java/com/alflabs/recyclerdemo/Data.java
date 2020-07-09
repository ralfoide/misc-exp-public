package com.alflabs.recyclerdemo;

public class Data {
    public static final long NO_VALUE = Long.MIN_VALUE;
    public static final long INITIAL_VALUE = 0L;

    private final long mValue;

    public Data(long value) {
        mValue = value;
    }

    public long getValue() {
        return mValue;
    }
}
