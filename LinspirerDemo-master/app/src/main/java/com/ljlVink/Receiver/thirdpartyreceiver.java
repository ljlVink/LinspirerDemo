package com.ljlVink.Receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ljlVink.utils.Toast;
import com.ljlVink.utils.CommandUtils;
import com.ljlVink.utils.DataUtils;

public class thirdpartyreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommandUtils cmdutils=new CommandUtils(context);
        try{
            if (intent.getAction().equals("ljlVink.thirdparty.app.calldeviceowner")){
                String cmd= intent.getStringExtra("method");
                cmdutils.ThirdParty_Exec(cmd);
                Toast.ShowInfo(context, "执行完成");
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