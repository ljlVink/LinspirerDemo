package com.huosoft.wisdomclass.linspirerdemo;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class AR extends DeviceAdminReceiver {
    public void onEnabled(Context context, Intent intent) {
        Log.e("LTKLog","LSD_?");
    }
    public CharSequence onDisableRequested(Context context, Intent intent) { return "bye~"; }
}