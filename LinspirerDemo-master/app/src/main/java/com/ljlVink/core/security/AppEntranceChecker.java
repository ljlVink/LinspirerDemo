package com.ljlVink.core.security;
import android.content.Context;
public class AppEntranceChecker {
    Context mContext;
    public AppEntranceChecker(Context context){
        this.mContext=context;
    }
    public boolean checkEntrance(){
        return mContext.getApplicationInfo().name.equals("com.huosoft.wisdomclass.linspirerdemo.lspdemoApplication");
    }
}
