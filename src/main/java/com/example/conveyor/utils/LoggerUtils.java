package com.example.conveyor.utils;

public final class LoggerUtils {
    public static <T> String cut(T object, int length) {
        return object.toString().substring(0, Math.min(object.toString().length(), length));
    }
}
