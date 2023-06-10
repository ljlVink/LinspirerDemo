package com.huosoft.wisdomclass.linspirerdemo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.ljlVink.Activity.NewUI;
import com.ljlVink.utils.DataCleanManager;
import com.ljlVink.utils.FileUtils;
import com.ljlVink.utils.Sysutils;
import com.ljlVink.utils.Toast;
import com.ljlVink.utils.Signutil;

import java.lang.reflect.Field;

public class lspdemoApplication extends Application {
    private static lspdemoApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        FileUtils.getInstance(this).copyAssetsToSD("apk","lspdemo.apks");
        DataCleanManager.clearAllCache(this);
        String finger=Build.FINGERPRINT;
        String romname= Sysutils.getDevice();
        if (finger.contains("TOSOT") ||finger.contains("anbox") ||finger.contains("android_x86") ||finger.contains("vpro") ||romname.contains("vmos") ||romname.contains("Vmos")) {
            throw new RuntimeException("Detected non-mdm illegal os");
        }
        if(BuildConfig.VERSION_NAME.contains("oem")){
            if(!Sysutils.isSystemApplication(this)){
                Toast.ShowErr(this,"请安装到系统或使用正常版本");
                throw new RuntimeException("oem app error");
            }
            else{
                Toast.ShowSuccess(this,"oem app loaded!");
                Intent intent=new Intent(this, NewUI.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }
    public static Context getApplication() {
        return mApplication;
    }
}