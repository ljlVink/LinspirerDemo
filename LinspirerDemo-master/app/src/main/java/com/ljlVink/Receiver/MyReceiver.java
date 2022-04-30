package com.ljlVink.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ljlVink.Activity.autostart;
import com.ljlVink.core.Postutil;

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent myIntent = new Intent(context, autostart.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);

        }
        if(intent.getAction().equals("com.linspirer.edu.return.userinfo")){
            String account=intent.getStringExtra("account");
            String nickName=intent.getStringExtra("nickName");
            String className=intent.getStringExtra("className");
            String schoolName=intent.getStringExtra("schoolName");
            Toast.makeText(context,"acc:"+account+",nickname"+nickName+",classname"+className+",schoolname"+schoolName,Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            Toast.makeText(context, "安装了应用"+packageName, Toast.LENGTH_SHORT).show();
            new Postutil(context).sendPost("安装了应用："+packageName);
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            Toast.makeText(context, "卸载了应用："+packageName, Toast.LENGTH_SHORT).show();

            new Postutil(context).sendPost("卸载了应用："+packageName);
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            String packageName = intent.getDataString();
            Toast.makeText(context, "覆盖安装应用："+packageName, Toast.LENGTH_SHORT).show();
            new Postutil(context).sendPost("覆盖安装应用："+packageName);
        }
    }
}
