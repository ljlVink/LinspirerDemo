package com.ljlVink.csdk5lib;

import android.app.csdk.CSDKManager;
import android.content.ComponentName;
import android.content.Context;

public class CSDK_new_MDM {
    private CSDKManager csdk;
    public CSDK_new_MDM(Context ctx){
        csdk=new CSDKManager(ctx);
    }
    public void Silent_active_deviceadmin(ComponentName selfcn){
        try{
            csdk.enableDeviceAdmin(selfcn.flattenToString(),true);
        }catch (Throwable ignore){}
    }
    public void enableDangerousPermissions(String pkgname){
        try{csdk.enableUsageStats(pkgname,true);}catch (Throwable ignore){}
        try{csdk.enableWriteSettings(pkgname,true);}catch (Throwable ignore){}
        try{csdk.enablePictureInPicture(pkgname,true);}catch (Throwable ignore){}
        try{csdk.enableWifiState(pkgname,true);}catch (Throwable ignore){}
        try{csdk.enableOverlayWindow(pkgname,true);}catch (Throwable ignore){}
        try{csdk.enableUnkownsources_v3(pkgname,true);}catch (Throwable ignore){}

    }

    public void bypassOemlock(){
        // for new csdk interface only
        try{
            csdk.disableOemUnLock(true);//详见 https://blog.csdn.net/wzh048503/article/details/108862577
            csdk.disallowOemUnLock(false);
        }catch (Throwable ignore){

        }
    }
}
