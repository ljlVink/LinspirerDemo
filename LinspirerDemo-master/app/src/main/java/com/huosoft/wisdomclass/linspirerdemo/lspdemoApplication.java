package com.huosoft.wisdomclass.linspirerdemo;
import android.app.Application;
import android.content.Context;
import android.net.VpnService;

import com.ljlVink.Activity.NewUI;
import com.ljlVink.Activity.autostart;
import com.ljlVink.MDM;
import com.ljlVink.Receiver.MyReceiver;
import com.ljlVink.core.CrashHandler;
import com.ljlVink.core.DataCleanManager;
import com.ljlVink.core.FileUtils;
import com.ljlVink.core.HackMdm;
import com.ljlVink.core.security.ROM_identifier;
import com.ljlVink.core.security.Signutil;
import com.tencent.bugly.Bugly;
import com.zy.devicelibrary.UtilsApp;
import com.zy.devicelibrary.utils.CpuUtils;

public class lspdemoApplication extends Application {
    int MMDM=0;
    @Override
    public void onCreate() {
        super.onCreate();
        DataCleanManager.clearAllCache(this);
        /*int keystatus = new Signutil(this, "97:8D:89:23:F9:F3:AF:C9:A3:79:37:2C:C8:A6:FF:A8:26:CC:DE:EF").f();
        if (1154 == keystatus) {
            exit("fuckyoU bitch");
        }
        if (!new SoChecker(this).socheck()){
            exit("fuckyOu bitch");
        }
        if (!new AppEntranceChecker(this).checkEntrance()){
            exit("fuckYou bitch");
        }
        */
        new ROM_identifier(this).checkrom();
        int keystatus = new Signutil(this, "97:8D:89:23:F9:F3:AF:C9:A3:79:37:2C:C8:A6:FF:A8:26:CC:DE:EF").f();
        if ( 666 == keystatus) {
            Bugly.init(getApplicationContext(), "50a36059a3", false);
        }
        MMDM=new MDM(this).MDM();
        FileUtils.getInstance(this).copyAssetsToSD("apk","lspdemo.apks");
        UtilsApp.init(this);

    }
    public int getMMDM(){
        return MMDM;
    }
}