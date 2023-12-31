package com.huosoft.wisdomclass.linspirerdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ljlVink.lsphunter.MainActivity;
import com.ljlVink.lsphunter.utils.DataCleanManager;
import com.ljlVink.lsphunter.utils.FileUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;

public class lsphunterApplication extends Application {
    private static lsphunterApplication mApplication;
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
                Intent intent=new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
    public static void RestartApp(){
        Intent intent = getApplication().getPackageManager()
                .getLaunchIntentForPackage(getApplication().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplication().startActivity(intent);
    }

    public static Context getApplication() {
        return mApplication;
    }
}