package com.ljlVink.Receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ljlVink.core.CommandUtils;
import com.ljlVink.core.DataUtils;

public class thirdpartyreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommandUtils cmdutils=new CommandUtils(context);
        try{
            if (intent.getAction().equals("ljlVink.thirdparty.app.calldeviceowner")){
                String cmd= intent.getStringExtra("method");
                cmdutils.ThirdParty_Exec(cmd);
                Toast.makeText(context,"执行完成",Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("ljlVink.thirdparty.app.pullrsa")){
                String pkg= intent.getStringExtra("pkgname");
                Intent intent1=new Intent();
                intent1.putExtra("rsakey", DataUtils.readStringValue(context,"key","null"));
                intent1.setPackage(pkg);
                context.sendBroadcast(intent1);

            }

        }catch (Throwable th){}
    }
}