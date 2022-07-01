package com.ljlVink.linspirerfake;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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
    public static String getMacaddress(Context context){
        String defaultMac = "02:00:00:00:00:00";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ntwInterface : interfaces) {
                if (ntwInterface.getName().equalsIgnoreCase("wlan0")) {//之前是p2p0，修正为wlan
                    byte[] byteMac = ntwInterface.getHardwareAddress();
                    if (byteMac == null) {}
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < byteMac.length; i++) {
                        strBuilder.append(String.format("%02X:", byteMac[i]));
                    }
                    if (strBuilder.length() > 0) {
                        strBuilder.deleteCharAt(strBuilder.length() - 1);
                    }
                    return strBuilder.toString();
                }
            }
        } catch (Exception e) {

        }
        if(!defaultMac.equals("02:00:00:00:00:00")) return defaultMac;
        return  Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    @SuppressLint("MissingPermission")
    public static String getBluetoothMacAddress() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter == null ? "unknown" : defaultAdapter.getAddress();
    }
    public static int getsystemversion() {
        return Integer.valueOf(Build.VERSION.RELEASE);
    }
    public static String  getsystemversion_str(){
        return  Build.VERSION.RELEASE;
    }
    public static String getBrand() {
        return Build.BRAND;
    }

}
