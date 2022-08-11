package com.ljlVink.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.huosoft.wisdomclass.linspirerdemo.lspdemoApplication;

import java.util.ArrayList;
import java.util.List;

public class LoadApptask extends AsyncTask<Void, Void, ArrayList<AppBean>> {
    private Callback mCallback;
    private int mode=0;
    public LoadApptask(int mode, Callback mCallback) {
        this.mCallback = mCallback;
        this.mode=mode;
        mPackageManager = lspdemoApplication.getApplication().getPackageManager();
    }
    @Override
    protected ArrayList<AppBean> doInBackground(Void... voids) {
        return readAppUsageList();
    }
    public static List<String> cleanlist(List<String> appInfos) {
        List<String> lst = new ArrayList<String>();
        List<String> pkgnamelist = new ArrayList<>();
        for (int i = 0; i < appInfos.size(); i++) {
            if (!pkgnamelist.contains(appInfos.get(i))) {
                pkgnamelist.add(appInfos.get(i));
                lst.add(appInfos.get(i));
            }
        }
        return lst;
    }
    private ArrayList<AppBean> readAppUsageList() {
        ArrayList<AppBean> mItems =new ArrayList<>();
        List<String> list = new ArrayList<String>();
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        if(mode==1){
            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    list.add(packageInfo.packageName);
                }
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> ResolveInfos = mPackageManager.queryIntentActivities(intent, 0);
            for (ResolveInfo ri : ResolveInfos) {
                String packageName = ri.activityInfo.packageName;
                list.add(packageName);
            }
            list= cleanlist(list);
        }if(mode==2){
            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                } else {
                    list.add(packageInfo.packageName);
                }
            }
        }
        for (String packageName : list) {
            ApplicationInfo info = getAppInfo(packageName);
            AppBean appUsageBean = new AppBean(packageName, info);
            appUsageBean.setAppInfo(info);
            if (info != null) {
                String label = (String) info.loadLabel(mPackageManager);
                Drawable icon = info.loadIcon(mPackageManager);
                appUsageBean.setAppName(label);
                appUsageBean.setAppIcon(icon);
            } else {
                appUsageBean.setAppName("应用已卸载");
            }
            mItems.add(appUsageBean);
        }
        return mItems;
    }
    private PackageManager mPackageManager;
    private ApplicationInfo getAppInfo(String pkgName) {
        try {
            return mPackageManager.getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }
    @Override
    protected void onPostExecute(ArrayList<AppBean> list) {
        if (mCallback != null) mCallback.onPostExecute(list);
    }
    public interface Callback {
        void onPostExecute(ArrayList<AppBean> list);
    }
}
