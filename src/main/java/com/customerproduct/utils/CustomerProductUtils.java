package com.customerproduct.utils;

public class CustomerProductUtils {
    public static boolean isNotNullAndEmpty(String str) {
        System.out.println(str != null && !str.isEmpty());
        return str != null && !str.isEmpty();
    }
}
