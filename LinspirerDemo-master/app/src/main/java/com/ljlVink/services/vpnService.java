package com.ljlVink.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.VpnService;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;

import androidx.preference.PreferenceManager;

import com.ljlVink.Activity.NewUI;
import com.ljlVink.utils.DataUtils;

import java.io.IOException;
import java.util.Set;
public class vpnService extends VpnService {
    public vpnService(){
    }
    private ParcelFileDescriptor vpn = null;
    private static final String EXTRA_COMMAND = "Command";
    private enum Command {start, reload, stop}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Command cmd = (intent == null ? Command.start : (Command) intent.getSerializableExtra(EXTRA_COMMAND));
        switch (cmd) {
            case start:
                if ( vpn == null)
                    vpn = vpnStart();
                break;
            case reload:
                ParcelFileDescriptor prev = vpn;
                vpn = vpnStart();
                if (prev != null)
                    vpnStop(prev);
                break;
            case stop:
                if (vpn != null) {
                    vpnStop(vpn);
                    vpn = null;
                }
                stopSelf();
                break;
        }
        return START_STICKY;
    }
    private ParcelFileDescriptor vpnStart() {
        final Builder builder = new Builder();
        builder.setSession("Linspirer Demo");
        builder.addAddress("10.1.10.1", 32);
        builder.addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128);
        builder.addRoute("0.0.0.0", 0);
        builder.addRoute("0:0:0:0:0:0:0:0", 0);
        try {
            builder.addAllowedApplication("com.android.launcher3");
            if(DataUtils.readint(this,"disallow_appstore_internet",1)==1){
                builder.addAllowedApplication("com.ndwill.swd.appstore");
            }
        }catch (Exception ignore){}
        Intent configure = new Intent(this, NewUI.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, configure, PendingIntent.FLAG_IMMUTABLE);
        builder.setConfigureIntent(pi);
        try {
            return builder.establish();
        } catch (Throwable ex) {
            return null;
        }
    }
    private void vpnStop(ParcelFileDescriptor pfd) {
        try {
            pfd.close();
        } catch (IOException ex) {
        }
    }
    private BroadcastReceiver connectivityChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(ConnectivityManager.EXTRA_NETWORK_TYPE) && intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY) == ConnectivityManager.TYPE_WIFI)
                reload(null, vpnService.this);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter ifConnectivity = new IntentFilter();
        ifConnectivity.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityChangedReceiver, ifConnectivity);
    }
    @Override
    public void onDestroy() {
        if (vpn != null) {
            vpnStop(vpn);
            vpn = null;
        }
        unregisterReceiver(connectivityChangedReceiver);
        super.onDestroy();
    }
    @Override
    public void onRevoke() {
        if (vpn != null) {
            vpnStop(vpn);
            vpn = null;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("enabled", false).apply();
        super.onRevoke();
    }
    public static void start(Context context) {
        Intent intent = new Intent(context, vpnService.class);
        intent.putExtra(EXTRA_COMMAND, Command.start);
        context.startService(intent);
    }
    public static void reload(String network, Context context) {
        Intent intent = new Intent(context, vpnService.class);
        intent.putExtra(EXTRA_COMMAND, Command.reload);
        context.startService(intent);
    }
    public static void stop(Context context) {
        Intent intent = new Intent(context, vpnService.class);
        intent.putExtra(EXTRA_COMMAND, Command.stop);
        context.startService(intent);
    }
}