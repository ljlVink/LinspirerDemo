package com.ljlVink.lsphunter.utils;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import com.huosoft.wisdomclass.linspirerdemo.AR;

public class CommandUtils {
    Context context;
    DevicePolicyManager dpm;
    ComponentName admin;
    String pkgname;
    public CommandUtils(Context context){
        this.context=context;
        admin = new ComponentName(context, AR.class);
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        pkgname=context.getPackageName();
    }
    public void ThirdParty_Exec(String cmd){
        switch (cmd){
            case "disableStatusbar":
                if (dpm.isDeviceOwnerApp(pkgname))
                dpm.setStatusBarDisabled(admin,true);
                break;
            case "enableStatusbar":
                if (dpm.isDeviceOwnerApp(pkgname))
                    dpm.setStatusBarDisabled(admin,false);
                break;
        }
    }
}
