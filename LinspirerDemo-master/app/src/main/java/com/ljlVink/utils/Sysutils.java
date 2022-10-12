package com.ljlVink.utils;

import android.annotation.SuppressLint;
import android.app.csdk.CSDKManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.huosoft.wisdomclass.linspirerdemo.lspdemoApplication;
import com.ljlVink.core.hackmdm.v2.HackMdm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Sysutils {
    public static String getRomavailablesize(Context context) {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return String.valueOf(((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private static String getprop(String str) {
        try {
            Class<?>[] clsArr = {String.class};
            Object[] objArr = {str};
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", clsArr).invoke(cls, objArr);
        } catch (Throwable th) {
            return "";
        }
    }
    public static boolean isUsbDebug(Context mContext) {
        boolean enableAdb = (Settings.Secure.getInt(mContext.getContentResolver(), "adb_enabled", 0) > 0);
        return enableAdb;
    }
    public static String getLcmdm_version(Context context){
        String lcmdm_version="";
        try{
            lcmdm_version=context.getPackageManager().getPackageInfo("com.android.launcher3",0).versionName;
        }catch (Exception e){
            lcmdm_version="设备未安装管控";
        }
        return lcmdm_version;
    }
    public static String getHwemui(){
        return getprop("ro.build.version.emui");
    }
    public static boolean isSystemApplication(Context context){
        PackageManager mPackageManager = context.getPackageManager();
        try {
            final PackageInfo packageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
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
        if(HackMdm.DeviceMDM.getMDMName().equals("CSDK")){
            try{
                defaultMac=HackMdm.DeviceMDM.getMacAddr();
            }catch (Throwable ingore){
                defaultMac="02:00:00:00:00:00";
            }
            return defaultMac;
        }
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
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        if (res.activityInfo.packageName.equals("android") || res.activityInfo.packageName.equals("com.android.settings")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }
    public static String getDevice() {
        String manufacturer = Character.toUpperCase(Build.MANUFACTURER.charAt(0)) + Build.MANUFACTURER.substring(1);
        if (!Build.BRAND.equals(Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(Build.BRAND.charAt(0)) + Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        return manufacturer;
    }
    public static boolean isTabletDevice(Context context) {
        /*return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;*/
        return true;
    }
    public static boolean copyStr(Context c,String copyStr) {
        try {
            ClipboardManager cm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isAssistantApp(Context context) {
        String assistant = Settings.Secure.getString(context.getContentResolver(), "assistant");
        if (assistant != null) {
            ComponentName cn = ComponentName.unflattenFromString(assistant);
            if (cn != null) {
                if (cn.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isActiveime(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (InputMethodInfo imi : imm.getEnabledInputMethodList()) {
            if (context.getPackageName().equals(imi.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    public static boolean isContextDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
        }
        return null;
    }
    public static ArrayList<String> FindLspDemoPkgName(Context context, String meta) {
        ArrayList<String> lst = new ArrayList<String>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            String pkgname=packageInfo.packageName;
            if (!pkgname.equals(context.getPackageName())) {
                if (getMetaDataValue(context, "HackMdm_oldif", pkgname).equals(meta)) {
                    lst.add(packageInfo.packageName);
                }
            }
        }
        return lst;
    }
    public static String getMetaDataValue(Context context, String meatName, String pkgname) {
        String value = "null";
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(pkgname, PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                Object object = applicationInfo.metaData.get(meatName);
                if (object != null) {
                    value = object.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return value;
    }

    public static String getDeviceid(Context context) {
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
        if(!defaultMac.equals("02:00:00:00:00:00"))
            return defaultMac;
        return  Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase(Locale.ROOT);
    }



}
