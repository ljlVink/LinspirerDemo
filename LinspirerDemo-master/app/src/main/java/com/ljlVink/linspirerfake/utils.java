package com.ljlVink.linspirerfake;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class utils {
    public static String getRomavailablesize(Context context) {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return String.valueOf(((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getRomtotalsize(Context context) {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return String.valueOf(((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    @SuppressLint("MissingPermission")
    public static String getBluetoothMacAddress() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter == null ? "unknown" : defaultAdapter.getAddress();
    }
    public static String getsystemversion() {
        return Build.VERSION.RELEASE;
    }
    public static String getBrand() {
        return Build.BRAND;
    }

}
