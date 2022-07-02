package com.ljlVink.xposed;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String pkg=loadPackageParam.packageName;
        Log.i("LenovoHook", "pkg:"+pkg);
        if(pkg.equals("com.android.systemui")){
            XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView", loadPackageParam.classLoader, "panelEnabled",new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(true);
                }
            });
        }
    }
}
