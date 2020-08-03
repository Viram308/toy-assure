package com.commons.util;

public class StringUtil {
    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String toLowerCase(String s) {
        return s == null ? null : s.toLowerCase().trim();
    }

    public static String toUpperCase(String s) {
        return s == null ? null : s.trim().toUpperCase();
    }
}
