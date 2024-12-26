package com.alibaba.work.faas.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {
    // 获取当前时间字符串的方法
    public static String getCurrentTimeString() {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化时间为字符串
        return now.format(formatter);
    }
}
