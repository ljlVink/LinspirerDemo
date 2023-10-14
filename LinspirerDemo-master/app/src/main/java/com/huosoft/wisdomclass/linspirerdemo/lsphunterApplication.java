package com.huosoft.wisdomclass.linspirerdemo;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NETWORK;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.ljlVink.lsphunter.MainActivity;
import com.ljlVink.lsphunter.services.OKHttpUpdateHttpService;
import com.ljlVink.lsphunter.utils.DataCleanManager;
import com.ljlVink.lsphunter.utils.FileUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;

import okhttp3.OkHttpClient;

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
        initXupdate();
    }
    public static void RestartApp(){
        Intent intent = getApplication().getPackageManager()
                .getLaunchIntentForPackage(getApplication().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplication().startActivity(intent);
    }
    private void initXupdate(){
        XUpdate.get()
                .debug(true)
                .isWifiOnly(false)
                .isGet(false)
                .isAutoMode(false)
                .param("versionCode", UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {
                    @Override
                    public void onFailure(UpdateError error) {
                        int code=error.getCode();
                        if (code != CHECK_NO_NEW_VERSION) {
                            Toast.ShowErr(getApplicationContext(),"检测更新失败->>"+error.toString()+"<<");
                        }
                        if(code == CHECK_NO_NETWORK){
                            Toast.ShowErr(getApplicationContext(),"检测更新失败->>无互联网连接!<<");
                        }
                    }
                })
                .setOnInstallListener(new OnInstallListener() {
                    @Override
                    public boolean onInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
                        String filepath=apkFile.getPath();

                        return false;
                    }

                    @Override
                    public void onInstallApkSuccess() {

                    }
                })
                .supportSilentInstall(false)
                .setIUpdateHttpService(new OKHttpUpdateHttpService())
                .init(this);

    }
    public static Context getApplication() {
        return mApplication;
    }
}