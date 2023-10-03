package com.ljlVink.lsphunter.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huosoft.wisdomclass.linspirerdemo.ddpm;

import com.ljlVink.Activity.autostart;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.lsphunter.MainActivity;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Toast;

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action=intent.getAction();
        if (action==null){
            return;
        }
        new HackMdm(context).initMDM();
        switch (action){
            case "android.intent.action.BOOT_COMPLETED":
                Intent myIntent = new Intent(context, autostart.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
                if(HackMdm.DeviceMDM.getMDMName().equals("supi_T11")){
                    String cmd=DataUtils.readStringValue(context,"t11_start_rootCmd","");
                    if(!cmd.equals("")){
                        HackMdm.DeviceMDM.RootCMD(cmd);
                    }
                }
                break;
            case "com.linspirer.edu.return.userinfo":
                String account=intent.getStringExtra("account");
                String nickName=intent.getStringExtra("nickName");
                String className=intent.getStringExtra("className");
                String schoolName=intent.getStringExtra("schoolName");
                if(account==null||nickName==null||className==null||schoolName==null){
                    Toast.ShowErr(context,"传回参数存在空值!请确保已经登录!");
                }
                Toast.ShowInfo(context, "acc:" + account + ",nickname" + nickName + ",classname" + className + ",schoolname" + schoolName);
                break;
            case "android.intent.action.PACKAGE_ADDED":
                String packageName = intent.getDataString();
                if(DataUtils.readint(context,"OnTestMode",0)==1)
                    Toast.ShowInfo(context, "安装了应用"+packageName);
                break;
            case "android.intent.action.PACKAGE_REMOVED":
                String packagen = intent.getDataString();
                if(DataUtils.readint(context,"OnTestMode",0)==1)
                    Toast.ShowInfo(context, "卸载了应用："+packagen);
                break;
            case "android.intent.action.PACKAGE_REPLACED":
                String pn = intent.getDataString();
                if(DataUtils.readint(context,"OnTestMode",0)==1)
                    Toast.ShowInfo(context, "覆盖安装应用："+pn);
        }
        String code=intent.getData().getHost();
        if (code !=null){
            switch (code){
                case "6666":
                    Intent intent1=new Intent(context, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    break;
                case "4567":
                    Intent intent2=new Intent(context, ddpm.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                    break;
                case "0000":
                    new HackMdm(context).initMDM();
                    HackMdm.DeviceMDM.RestoreFactory_AnyMode();
                    break;

            }
        }
    }
}
