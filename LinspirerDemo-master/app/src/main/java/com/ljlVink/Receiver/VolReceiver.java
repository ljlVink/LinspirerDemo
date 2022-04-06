package com.ljlVink.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.ljlVink.Activity.NewUI;
import com.ljlVink.core.DataUtils;
import com.ljlVink.core.HackMdm;

public class VolReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
            int counter= DataUtils.readint(context,"vol");
            counter++;
            DataUtils.saveintvalue(context,"vol",counter);
            if(DataUtils.readint(context,"vol")==5){
                context.unregisterReceiver(this);
                Intent intent1 = new Intent(context, NewUI.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isstart",false);
                context.startActivity(intent1);
                DataUtils.saveintvalue(context,"vol",0);
            }
        }
    }
}
