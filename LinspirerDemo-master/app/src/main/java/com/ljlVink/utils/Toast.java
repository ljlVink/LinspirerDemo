package com.ljlVink.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import es.dmoral.toasty.Toasty;

public class Toast {
    public static void ShowSuccess(Context context,String str){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toasty.success(context, str, Toasty.LENGTH_SHORT, true).show();
                Looper.loop();
            }
        });
        th.start();

    }
    public static void ShowWarn(Context context,String str){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toasty.warning(context, str, Toasty.LENGTH_SHORT, true).show();
                Looper.loop();
            }
        });
        th.start();

    }
    public static void ShowErr(Context context,String str){
        Log.e("Toast",str);
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toasty.error(context, str, Toasty.LENGTH_SHORT, true).show();
                Looper.loop();
            }
        });
        th.start();
    }

    public static void ShowInfo(Context context,String str){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toasty.warning(context, str, Toasty.LENGTH_SHORT, true).show();
                Looper.loop();
            }
        });
        th.start();

    }
}
