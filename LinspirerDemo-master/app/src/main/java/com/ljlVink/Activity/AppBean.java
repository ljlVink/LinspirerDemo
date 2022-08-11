package com.ljlVink.Activity;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppBean implements Serializable {
    private String packageName;
    private ApplicationInfo appInfo;
    private Drawable appIcon;
    private String appName;

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public AppBean(String packageName, ApplicationInfo appInfo) {
        this.packageName = packageName;
        this.appInfo = appInfo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

}
