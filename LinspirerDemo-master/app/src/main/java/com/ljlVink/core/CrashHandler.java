package com.ljlVink.core;
import android.content.Context;

import com.ljlVink.core.core.Postutil;

import java.io.PrintWriter;
import java.io.StringWriter;
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private Context context;
    private Thread.UncaughtExceptionHandler defalutHandler;
    private CrashHandler(){}
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }
    public void init(Context context) {
        this.context = context;
        defalutHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter sw= new StringWriter();
        ex.printStackTrace(new PrintWriter(sw,true));
        try{
        new Postutil(context).sendPost("\n全局异常捕获器已经捕获到以下错误:\n"+sw.toString());}
        catch (Exception e){}
        System.exit(0);
        throw new Error("exit with Exceptions");

    }

}
