package com.ljlVink.Activity;


import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;

import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.DataUtils;
import com.ljlVink.services.vpnService;

public class autostart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autostart);
        if(DataUtils.readint(this,"vpnmode")==1) {
            startvpn();
        }
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 666){
            if (resultCode == RESULT_OK)
                vpnService.start(this);
        }

    }
    private void startvpn(){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Intent prepare = VpnService.prepare(autostart.this);
                if(prepare == null) {
                    onActivityResult(666,RESULT_OK,null);
                }
                else {
                    try {
                        startActivityForResult(prepare, 666);
                    } catch (Throwable ex) {
                        onActivityResult(666, RESULT_CANCELED, null);
                    }
                }
            }
        });
        th.start();
    }

}