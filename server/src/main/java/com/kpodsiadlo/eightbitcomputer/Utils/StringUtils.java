package com.kpodsiadlo.eightbitcomputer.Utils;

public class StringUtils {
    private StringUtils() {
        throw new

                IllegalStateException("Utility class");
    }

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
