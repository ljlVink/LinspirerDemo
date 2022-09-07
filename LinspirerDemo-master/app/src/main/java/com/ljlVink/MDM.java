package com.ljlVink;
import android.annotation.SuppressLint;
import android.app.csdk.CSDKManager;
import android.content.Context;
import android.os.Build;

public class MDM {
    static  boolean LENOVO_MIAMDM=true;
    static boolean LENOVO_CSDK=true;
    Context context;
    public MDM(Context context){
        this.context=context;
    }
    @SuppressLint("NotConstructor")
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
        if(LENOVO_CSDK){
            try{
                if(new CSDKManager(context).getDeviceInfo(1).equals("")){
                    LENOVO_CSDK=false;
                }
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
        //针对x605fc这个奇葩设备
        if(LENOVO_CSDK==true&&LENOVO_MIAMDM==true){
            return 3;
        }
        if(Build.BRAND.equals("T11")){
            return 4;//t11 配领创增强模式
        }
        return -1;
    }
    public int t11_rootmode(){
        if(Build.BRAND.equals("T11")){
            return 4;//bug太多了 暂时入口点改成通用
        }
        return MDM();
    }
}
