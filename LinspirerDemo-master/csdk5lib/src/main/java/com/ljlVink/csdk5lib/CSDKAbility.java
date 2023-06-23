package com.ljlVink.csdk5lib;

import android.os.Build;
import android.text.TextUtils;

public class CSDKAbility {
    public static boolean HasAbilityCSDK_new() {
        return isLenovoPad(Build.MODEL) && Build.VERSION.SDK_INT >= 31;
    }
    public static boolean isLenovoPad(String str) {
        return !TextUtils.isEmpty(str) && (str.contains("Lenovo") || str.contains("TB"));
    }

}
