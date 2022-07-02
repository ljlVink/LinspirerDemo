package com.ljlVink.core.security;


import android.content.Context;
import android.os.Build;

import com.ljlVink.Activity.NewUI;
import com.ljlVink.core.core.HackMdm;

public class ROM_identifier {
    private Context mContext;
    public ROM_identifier(Context context){
        this.mContext=context;
    }
    public void checkrom(){
        String finger=Build.FINGERPRINT;
        String rommm= NewUI.getDevice();
        if (rommm.contains("Xiaomi")||finger.contains("anbox")||finger.contains("android_x86")||finger.contains("vpro")||rommm.contains("vmos")|| rommm.contains("Vmos")){
            new HackMdm(mContext).RemoveOwner_admin();
            throw new RuntimeException("Error_for_illegal os");
        }
    }
}
