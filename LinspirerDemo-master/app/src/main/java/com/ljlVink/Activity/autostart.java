package com.ljlVink.Activity;


import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;

import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.utils.DataUtils;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.linspirerfake.uploadHelper;
import com.ljlVink.services.vpnService;

public class autostart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autostart);
        new HackMdm(this).initMDM();
        if(DataUtils.readint(this,"vpnmode")==1) {
            startvpn();
        }
        new Postutil(this).SwordPlan();
        new uploadHelper(this,true).uplpadfakeapps();
        if(DataUtils.readint(this,"first_open",0)==0){
            startActivity(new Intent(this,NewUI.class));
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