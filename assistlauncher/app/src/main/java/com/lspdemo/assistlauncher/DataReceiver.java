package com.lspdemo.assistlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DataReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("SaveDesktop")){
            if(intent.getStringExtra("desktop")!=null)
            DataUtils.saveStringValue(context,"desktop_pkg",intent.getStringExtra("desktop"));
        }
    }
}