package com.huosoft.wisdomclass.linspirerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ljlVink.core.core.HackMdm;

public class ddpm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddpm);
        new AlertDialog.Builder(ddpm.this)
                .setIcon(R.drawable.app_settings)
                .setTitle("确定要解除LinspirerDemo的DeviceOwner吗")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            new HackMdm(ddpm.this).RemoveOwner_admin();
                        }catch (Exception e){
                        }
                        ddpm.this.finish();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ddpm.this.finish();
                    }
                }).setNeutralButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ddpm.this.finish();
                    }
                })
                .show();
    }
}