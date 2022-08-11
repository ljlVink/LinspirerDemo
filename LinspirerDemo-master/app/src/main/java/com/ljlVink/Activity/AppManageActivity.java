package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.SystemUpdateInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.utils.Sysutils;
import com.ljlVink.utils.Toast;
import com.ljlVink.utils.DataUtils;
import com.ljlVink.core.core.HackMdm;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.tabs.TabLayout;


import java.util.List;

public class AppManageActivity extends AppCompatActivity {
    private List<AppBean> mItems;
    private AppAdapter mAdapter;
    private HackMdm hackMdm;
    private LoadApptask mLoadAppUsageTask;
    private ArrayList<String> lspdemopkgname;

    private static final String[] TAB_NAMES = {"已安装app", "系统app"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage_new);
        //getSupportActionBar().hide();
        //ImmersionBar.with(this).transparentStatusBar().init();
        lspdemopkgname = Sysutils.FindLspDemoPkgName(this,"linspirerdemo");
        hackMdm=new HackMdm(this);
        int c = 0;
        TabLayout tabLayout = findViewById(R.id.tab_condition);
        for (String name : TAB_NAMES) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setTag(c);
            tab.view.setOnClickListener(v -> onTabClick((int) tab.getTag()));
            tabLayout.addTab(tab.setText(name));
            c++;
        }
        onTabClick(0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        onTabClick(0);
    }
    public void onTabClick(int position) {
        setTitle(TAB_NAMES[position]);
        switch (position) {
            case 0:// 今天的数据  00:00 到 现在
                getapps("已经安装app",1);
                break;
            case 1:// 昨天的数据  昨天00:00 - 今天00:00
                getapps("系统app",2);
                break;
        }
    }


    private void getapps(String str,int center) {
        mLoadAppUsageTask = new LoadApptask(center, list -> {
            mItems = list;
            initAdapter();
        });
        mLoadAppUsageTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadAppUsageTask != null) {
            mLoadAppUsageTask.cancel(true);
            mLoadAppUsageTask = null;
        }
    }

    private void initAdapter() {
        setTitle(String.format("%s (共%s条)", getTitle(), mItems.size()));
        mAdapter =new AppAdapter();
        ListView rc=findViewById(R.id.rv_app_usage);
        rc.setAdapter(mAdapter);
        rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appName=mItems.get(i).getAppName();
                String pkgname=mItems.get(i).getPackageName();
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                        .setTitle(appName)
                        .setMessage("包名:" + pkgname + "\n")
                        .setPositiveButton("打开", (dialog1, which) -> {
                            dialog1.dismiss();
                            try {
                                startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                            } catch (Exception e) {
                                Toast.ShowErr(getApplicationContext(), "出现错误");
                            }
                        });
                builder.setIcon(Sysutils.getAppIcon(AppManageActivity.this, pkgname));
                builder.setNegativeButton("卸载", (dialog, which) -> {
                    dialog.dismiss();
                    hackMdm.uninstallApp(pkgname);
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + pkgname));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
                if (lspdemopkgname.contains(pkgname)) {
                    builder.setNeutralButton("转移权限", (dialog2, which) -> {
                        hackMdm.transfer(new ComponentName(pkgname, "com.huosoft.wisdomclass.linspirerdemo.AR"));
                    });
                } else if (!pkgname.equals(getPackageName())) {
                    builder.setNeutralButton("带SN参数启动", (dialog2, which) -> {
                        try {
                            hackMdm.killApplicationProcess(pkgname);
                            startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int ii = 1; ii <= 7; ii++) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (Exception e) {
                                        }
                                        String sn = DataUtils.readStringValue(getApplicationContext(), "SN", hackMdm.getCSDK_sn_code());
                                        if (sn.equals("null")) {
                                            return;
                                        }
                                        sendBroadcast(new Intent("com.linspirer.edu.getdevicesn"));
                                        Intent intent8 = new Intent("com.android.laucher3.mdm.obtaindevicesn");
                                        intent8.putExtra("device_sn", sn);
                                        sendBroadcast(intent8);
                                    }
                                }
                            });
                            thread.start();
                        } catch (Exception e) {
                            Toast.ShowErr(getApplicationContext(),"启动失败");
                        }
                    });
                }
                builder.create().show();

            }

        });
        rc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appName=mItems.get(i).getAppName();
                String pkgname=mItems.get(i).getPackageName();

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                        .setTitle(appName)
                        .setMessage("包名:" + pkgname + "\n")
                        .setPositiveButton("冻结", (dialog1, which) -> {
                            hackMdm.iceApp(pkgname, true);
                        }).setIcon(Sysutils.getAppIcon(AppManageActivity.this, pkgname));
                builder.setNegativeButton("解冻", (dialog, which) -> {
                    hackMdm.iceApp(pkgname, false);
                });
                builder.create().show();
                return true;
            }
        });
    }
    class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mItems.size();
        }
        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(AppManageActivity.this, R.layout.item_apps, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_iv_app_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.id_tv_app_name);
            imageView.setImageDrawable(mItems.get(position).getAppIcon());
            textView.setText(mItems.get(position).getAppName());
            return convertView;
        }
    }
}


/*
public class AppManageActivity extends AppCompatActivity {
    private AppAdapter adapter;
    private ListView applistview;
    private List<AppInfo> data_sys;
    private sysAppAdapter adapter_sys;
    private ListView applistview_sys;
    private ArrayList<String> lspdemopkgname;
    private TextView refresh;
    private List<AppInfo> data;
    HackMdm hackMdm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hackMdm=new HackMdm(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);
        getSupportActionBar().hide();
        ImmersionBar.with(this).transparentStatusBar().init();

        TextView tv3 = findViewById(R.id.applist);
        lspdemopkgname = Sysutils.FindLspDemoPkgName(this,"linspirerdemo");

        tv3.post(new Runnable() {
            @Override
            public void run() {
                tv3.setText(hackMdm.getappwhitelist());
                tv3.setMovementMethod(ScrollingMovementMethod.getInstance());

            }
        });
        applistview = (ListView) findViewById(R.id.listview);
        data = getAllAppInfos();
        adapter = new AppAdapter();
        //显示列表
        applistview.post(new Runnable() {
            @Override
            public void run() {
                applistview.setAdapter(adapter);
                applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //提示当前行的应用名称
                        String appName = data.get(position).getAppName();
                        String pkgname = data.get(position).getPackageName();

                    }
                });
                applistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String appName = data.get(position).getAppName();
                        String pkgname = data.get(position).getPackageName();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                                .setTitle(appName)
                                .setMessage("包名:" + pkgname + "\n")
                                .setPositiveButton("冻结", (dialog1, which) -> {
                                    hackMdm.iceApp(pkgname, true);
                                }).setIcon(Sysutils.getAppIcon(AppManageActivity.this, pkgname));
                        builder.setNegativeButton("解冻", (dialog, which) -> {
                            hackMdm.iceApp(pkgname, false);
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }
        });

        applistview_sys = (ListView) findViewById(R.id.listview2);
        data_sys = getsysAppInfos();
        adapter_sys = new sysAppAdapter();
        applistview_sys.post(new Runnable() {
            @Override
            public void run() {
                applistview_sys.setAdapter(adapter_sys);
                applistview_sys.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //提示当前行的应用名称
                        String appName = data_sys.get(position).getAppName();
                        String pkgname = data_sys.get(position).getPackageName();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                                .setTitle(appName)
                                .setMessage("包名:" + pkgname + "\n")
                                .setPositiveButton("打开", (dialog1, which) -> {
                                    dialog1.dismiss();
                                    try {
                                        startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                                    } catch (Exception e) {
                                        Toast.ShowErr(getApplicationContext(), "出现错误");
                                    }
                                });

                        builder.setIcon(Sysutils.getAppIcon(AppManageActivity.this, pkgname));
                        builder.setNegativeButton("卸载", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + pkgname));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
                        builder.create().show();
                    }
                });
                applistview_sys.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String appName = data_sys.get(position).getAppName();
                        String pkgname = data_sys.get(position).getPackageName();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                                .setTitle(appName)
                                .setMessage("包名:" + pkgname + "\n")
                                .setPositiveButton("冻结", (dialog1, which) -> {
                                    hackMdm.iceApp(pkgname, true);
                                }).setIcon(Sysutils.getAppIcon(AppManageActivity.this, pkgname));
                        builder.setNegativeButton("解冻", (dialog, which) -> {
                            hackMdm.iceApp(pkgname, false);
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }
        });

        refresh = findViewById(R.id.refresh);
        refresh.setClickable(true);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = getAllAppInfos();
                adapter.notifyDataSetChanged();
            }
        });
    }
    class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1. 若是convertView是null, 加载item的布局文件
            if (convertView == null) {
                convertView = View.inflate(AppManageActivity.this, R.layout.item_applist, null);
            }
            //2. 获得当前行数据对象
            AppInfo appInfo = data.get(position);
            //3. 获得当前行须要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_name);
            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());
            //返回convertView
            return convertView;
        }
    }

    class sysAppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data_sys.size();
        }

        @Override
        public Object getItem(int position) {
            return data_sys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1. 若是convertView是null, 加载item的布局文件
            if (convertView == null) {
                convertView = View.inflate(AppManageActivity.this, R.layout.item_applist, null);
            }
            //2. 获得当前行数据对象
            AppInfo appInfo = data_sys.get(position);
            //3. 获得当前行须要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_name);
            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());
            //返回convertView
            return convertView;
        }
    }
    @Override
    public void onResume(){
            super.onResume();
        try {
            data = getAllAppInfos();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

    }
    public static List<AppInfo> cleanlist(List<AppInfo> appInfos) {

        List<AppInfo> lst = new ArrayList<AppInfo>();
        List<String> pkgnamelist = new ArrayList<>();
        for (int i = 0; i < appInfos.size(); i++) {
            if (!pkgnamelist.contains(appInfos.get(i).getPackageName())) {
                pkgnamelist.add(appInfos.get(i).getPackageName());
                lst.add(appInfos.get(i));
            }
        }
        return lst;
    }

    protected List<AppInfo> getAllAppInfos() {
        //这里采用了intent反射和获取所有包 防止app落下
        List<AppInfo> list = new ArrayList<AppInfo>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                list.add(new AppInfo(Sysutils.getAppIcon(this, packageInfo.packageName), packageInfo.applicationInfo.loadLabel(pm).toString(), packageInfo.packageName));
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 获得包含应用信息的列表
        List<ResolveInfo> ResolveInfos = pm.queryIntentActivities(intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 获得包名
            String packageName = ri.activityInfo.packageName;
            // 获得图标
            Drawable icon = ri.loadIcon(pm);
            // 获得应用名称
            String appName = ri.loadLabel(pm).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return cleanlist(list);
    }
    protected List<AppInfo> getsysAppInfos() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            } else {
                list.add(new AppInfo(Sysutils.getAppIcon(this, packageInfo.packageName), packageInfo.applicationInfo.loadLabel(pm).toString(), packageInfo.packageName));
            }
        }
        return list;
    }

}*/