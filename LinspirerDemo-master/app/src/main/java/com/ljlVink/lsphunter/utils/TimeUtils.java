package com.ljlVink.lsphunter.utils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;

public class TimeUtils {
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }
    public static String getNowString(@NonNull final DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

}
