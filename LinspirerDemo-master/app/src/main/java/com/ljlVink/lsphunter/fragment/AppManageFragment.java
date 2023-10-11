package com.ljlVink.lsphunter.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.bean.AppBean;
import com.ljlVink.lsphunter.utils.LoadApptask;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppManageFragment {
    View view;
    Context ctx;
    private List<AppBean> mItems;
    private AppAdapter mAdapter;
    private LoadApptask mLoadAppUsageTask;
    private ArrayList<String> lspdemopkgname;

    private static final String[] TAB_NAMES = {"已安装app", "系统app"};

    public AppManageFragment(View view, Context ctx){
        this.view=view;
        this.ctx=ctx;
    }
    public void HandleFragment(){
        lspdemopkgname = Sysutils.FindLspDemoPkgName(ctx,"linspirerdemo");
        int c = 0;
        TabLayout tabLayout = view.findViewById(R.id.tab_condition);
        for (String name : TAB_NAMES) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setTag(c);
            tab.view.setOnClickListener(v -> onTabClick((int) tab.getTag()));
            tabLayout.addTab(tab.setText(name));
            c++;
        }
        onTabClick(0);

    }
    public void onTabClick(int position) {
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
    private void initAdapter() {
        mAdapter = new AppAdapter();
        ListView rc = view.findViewById(R.id.rv_app_usage);
        rc.setAdapter(mAdapter);
        rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appName = mItems.get(i).getAppName();
                String pkgname = mItems.get(i).getPackageName();
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ctx)
                        .setTitle(appName)
                        .setMessage("包名:" + pkgname + "\n")
                        .setPositiveButton("打开", (dialog1, which) -> {
                            dialog1.dismiss();
                            try {
                                ctx.startActivity(ctx.getPackageManager().getLaunchIntentForPackage(pkgname));
                            } catch (Exception e) {
                                Toast.ShowErr(ctx, "出现错误");
                            }
                        });
                builder.setIcon(Sysutils.getAppIcon(ctx, pkgname));
                builder.setNegativeButton("卸载", (dialog, which) -> {
                    dialog.dismiss();
                    HackMdm.DeviceMDM.unblockApp(pkgname);
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + pkgname));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                });
                if (lspdemopkgname.contains(pkgname)) {
                    builder.setNeutralButton("转移权限", (dialog2, which) -> {
                        HackMdm.DeviceMDM.transferOwner(new ComponentName(pkgname, "com.huosoft.wisdomclass.linspirerdemo.AR"));
                    });
                } else if (!pkgname.equals(ctx.getPackageName())) {
                    builder.setNeutralButton("带SN参数启动", (dialog2, which) -> {
                        try {
                            HackMdm.DeviceMDM.killApplicationProcess(pkgname);
                            ctx.startActivity(ctx.getPackageManager().getLaunchIntentForPackage(pkgname));
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int ii = 1; ii <= 7; ii++) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (Exception e) {
                                        }
                                        String sn = DataUtils.readStringValue(ctx, "SN", HackMdm.DeviceMDM.getSerialCode());
                                        if (sn.equals("null")) {
                                            return;
                                        }
                                        ctx.sendBroadcast(new Intent("com.linspirer.edu.getdevicesn"));
                                        Intent intent8 = new Intent("com.android.laucher3.mdm.obtaindevicesn");
                                        intent8.putExtra("device_sn", sn);
                                        ctx.sendBroadcast(intent8);
                                    }
                                }
                            });
                            thread.start();
                        } catch (Exception e) {
                            Toast.ShowErr(ctx, "启动失败");
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

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ctx)
                        .setTitle(appName)
                        .setMessage("包名:" + pkgname + "\n")
                        .setPositiveButton("冻结", (dialog1, which) -> {
                            HackMdm.DeviceMDM.iceApp(pkgname, true);
                        }).setIcon(Sysutils.getAppIcon(ctx, pkgname));
                builder.setNegativeButton("解冻", (dialog, which) -> {
                    HackMdm.DeviceMDM.iceApp(pkgname, false);
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
                    convertView = View.inflate(ctx, R.layout.item_apps, null);
                }
                ImageView imageView = (ImageView) convertView.findViewById(R.id.id_iv_app_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.id_tv_app_name);
                imageView.setImageDrawable(mItems.get(position).getAppIcon());
                textView.setText(mItems.get(position).getAppName());
                return convertView;
            }
        }


}
