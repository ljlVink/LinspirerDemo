package com.ljlVink.core.security;

import android.content.Context;

import com.lahm.library.EasyProtectorLib;

public class EasyProtectDetector {
    Context mContext;
    public EasyProtectDetector(Context context){
        this.mContext=context;
    }
    public boolean check(){
        if(EasyProtectorLib.checkIsBeingTracedByJava()||EasyProtectorLib.checkIsDebug(mContext)||EasyProtectorLib.checkIsXposedExist()){
            return false;
        }
        return true;

    }
}
