package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.DataUtils;
import com.ljlVink.core.HackMdm;
import com.ljlVink.core.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
        lspdemopkgname = FindLspDemoPkgName();

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
                    /**
                     * parent : ListView
                     * view : 当前行的item视图对象
                     * position : 当前行的下标
                     */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //提示当前行的应用名称
                        String appName = data.get(position).getAppName();
                        String pkgname = data.get(position).getPackageName();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppManageActivity.this)
                                .setTitle(appName)
                                .setMessage("包名:" + pkgname + "\n")
                                .setPositiveButton("打开", (dialog1, which) -> {
                                    dialog1.dismiss();
                                    try {
                                        startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "出现错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder.setIcon(getAppIcon(AppManageActivity.this, pkgname));
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
                                    ToastUtils.ShowToast("启动失败", getApplicationContext());
                                }
                            });
                        }
                        builder.create().show();
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
                                }).setIcon(getAppIcon(AppManageActivity.this, pkgname));
                        builder.setNegativeButton("解冻", (dialog, which) -> {
                            hackMdm.iceApp(pkgname, false);
                        })/*.setNeutralButton("设置为第一启动器", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hackMdm.setfirstlauncher(getPackageManager().getLaunchIntentForPackage(pkgname));
                        DataUtils.saveStringValue(getApplicationContext(),"firstlauncher",pkgname);
                    }
                })*/;
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
                    /**
                     * parent : ListView
                     * view : 当前行的item视图对象
                     * position : 当前行的下标
                     */
                    @Override
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
                                        Toast.makeText(getApplicationContext(), "出现错误", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        builder.setIcon(getAppIcon(AppManageActivity.this, pkgname));
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
                                }).setIcon(getAppIcon(AppManageActivity.this, pkgname));
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
                list.add(new AppInfo(getAppIcon(this, packageInfo.packageName), packageInfo.applicationInfo.loadLabel(pm).toString(), packageInfo.packageName));
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
                list.add(new AppInfo(getAppIcon(this, packageInfo.packageName), packageInfo.applicationInfo.loadLabel(pm).toString(), packageInfo.packageName));
            }
        }
        return list;
    }
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> FindLspDemoPkgName() {
        ArrayList<String> lst = new ArrayList<String>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !packageInfo.packageName.equals(getPackageName())) {
                if (getMetaDataValue(this, "HackMdm", packageInfo.packageName).equals("linspirerdemo")) {
                    lst.add(packageInfo.packageName);
                }
            }
        }
        return lst;
    }
    public String getMetaDataValue(Context context, String meatName, String pkgname) {
        String value = "null";
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(pkgname, PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                Object object = applicationInfo.metaData.get(meatName);
                if (object != null) {
                    value = object.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return value;
    }

}