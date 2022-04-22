package com.ljlVink.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;

import com.ljlVink.core.DataUtils;
import com.ljlVink.services.vpnService;

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            if(DataUtils.readint(context,"vpnmode")==1) {

                Intent intent1 = VpnService.prepare(context);
                if (intent1 == null) {
                    vpnService.start(context);
                }
            }
        }
    }
}
