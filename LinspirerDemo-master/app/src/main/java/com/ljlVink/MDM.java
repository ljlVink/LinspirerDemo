package com.ljlVink;
import android.app.csdk.CSDKManager;
import android.content.Context;

import com.ljlVink.Activity.NewUI;

public class MDM {
    static  boolean LENOVO_MIAMDM=true;
    static boolean LENOVO_CSDK=true;
    Context context;
    public MDM(Context context){
        this.context=context;
    }
    public int MDM(){
        try{
            Class.forName("android.app.mia.MiaMdmPolicyManager");
        }
        catch (ClassNotFoundException e){
            LENOVO_MIAMDM=false;
        }
        try{
            Class.forName("android.app.csdk.CSDKManager");
        }
        catch (ClassNotFoundException e){
            LENOVO_CSDK=false;
        }
        if(LENOVO_CSDK==true){
            try{
                new CSDKManager(context).SetEnable(false);
            }catch (Throwable e){
                LENOVO_CSDK=false;
            }
        }
        if(LENOVO_CSDK==true&&LENOVO_MIAMDM==false){
            return 2;
        }
        if(LENOVO_CSDK==false&&LENOVO_MIAMDM==true){
            return 3;
        }
        //原来低下没有这几句话
        if(LENOVO_CSDK==true&&LENOVO_MIAMDM==true){
            return 3;
        }
        return -1;
    }
}
