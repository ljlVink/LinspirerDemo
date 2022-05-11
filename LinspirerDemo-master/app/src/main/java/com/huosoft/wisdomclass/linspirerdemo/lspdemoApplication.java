package com.huosoft.wisdomclass.linspirerdemo;
import android.app.Application;
import android.content.Context;
import android.net.VpnService;
import android.util.Log;

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
import com.ljlVink.keepalive.Activity1;
import com.ljlVink.keepalive.Activity2;
import com.ljlVink.keepalive.Receiver1;
import com.ljlVink.keepalive.Receiver2;
import com.ljlVink.keepalive.Service2;
import com.tencent.bugly.Bugly;

import me.weishu.leoric.Leoric;
import me.weishu.leoric.LeoricConfigs;

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
        Log.e("lsptest","application_finish");
    }
    public int getMMDM(){
        return MMDM;
    }
    public void exit(String str){
        try{
            Log.e("lspdemo","https://gitee.com/ljlvink/huovink_-mdm_catch_for_-lenovo");
        }catch (Error e){
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 在这里调用Context的方法会崩溃
        super.attachBaseContext(base);
        Leoric.init(base, new LeoricConfigs(
                new LeoricConfigs.LeoricConfig(
                        getPackageName() + ":resident",
                        Service2.class.getCanonicalName(),
                        Receiver1.class.getCanonicalName(),
                        autostart.class.getCanonicalName()),
                new LeoricConfigs.LeoricConfig(
                        "android.media",
                        VpnService.class.getCanonicalName(),
                        Receiver2.class.getCanonicalName(),
                        Activity2.class.getCanonicalName())
        ));
        // 在这里可以正常调用Context的方法
    }

}