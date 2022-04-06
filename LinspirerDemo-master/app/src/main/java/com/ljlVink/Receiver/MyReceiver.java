package com.ljlVink.Receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ljlVink.MDM;
import com.ljlVink.Activity.NewUI;
import com.ljlVink.core.DataUtils;
import com.ljlVink.core.HackMdm;
import com.ljlVink.core.Postutil;

import java.util.Objects;

public class MyReceiver extends BroadcastReceiver {

    int x=0;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            DataUtils.saveintvalue(context,"vol",0);
            int MMDM=new MDM(context).MDM();
            new Postutil(context).sendPost("设备已经开机;接口="+MMDM);
            Intent i = new Intent(context, NewUI.class);
            i.putExtra("isstart",true);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}