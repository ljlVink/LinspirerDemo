package com.ljlVink.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.king.zxing.CameraScan;
import com.ljlVink.core.Postutil;
import com.ljlVink.core.DataUtils;
import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.ContentUriUtil;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.HackMdm;
import com.ljlVink.core.RSA;

import com.ljlVink.core.ToastUtils;
import com.ljlVink.services.vpnService;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.interfaces.OnInvokeView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
public class NewUI extends AppCompatActivity {
    private final int Lenovo_Mia=3;
    private final int Lenovo_Csdk=2;
    private List<AppInfo> data;
    private AppAdapter adapter;
    private ListView applistview;
    private List<AppInfo> data_sys;
    private sysAppAdapter adapter_sys;
    private ListView applistview_sys;
    private String currMDM="未找到合适的mdm接口";
    private HackMdm hackMdm;
    private BaseAdapter mAdapter = null;
    private ArrayList<String> superlist=new ArrayList<>();
    private ArrayList<icon> mData = null;
    private int MMDM;
    private ArrayList<String> lspdemopkgname;
    private GridView grid_photo;
    private Postutil postutil;
    private  TextView refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String pubkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                "OwIDAQAB";
        try {
            getSupportActionBar().hide();
        }catch (Exception e){}
        try{
            setContentView(R.layout.activity_new_ui);
        }catch (Throwable rh){
            rh.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        hackMdm=new HackMdm(this);
        postutil=new Postutil(this);
        if(DataUtils.readint(this,"vpnmode")==1){
            startvpn();
        }
        postutil.CloudAuthorize();
        hackMdm.initHack(0);
        if(!isActiveime()){
            Toast.makeText(this,"请先配置输入法",Toast.LENGTH_SHORT).show();
            Intent intent111 = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent111);
        }
        if(!isAssistantApp()){
            Intent intent111111=new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
            PackageManager packageManager = getPackageManager();
            if (intent111111.resolveActivity(packageManager) != null) {
                Toast.makeText(this,"请将本程序注册成语音助手",Toast.LENGTH_LONG).show();
                startActivity(intent111111);
            } else {
                Toast.makeText(this,"这个rom不能设置语音助手",Toast.LENGTH_LONG).show();
            }
        }

        //初始化view
        MMDM=hackMdm.getMMDM();
        lspdemopkgname=FindLspDemoPkgName();
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        mData = new ArrayList<icon>();
        mData.add(new icon(R.drawable.backtodesktop, "返回桌面"));
        mData.add(new icon(R.drawable.installapps, "应用安装"));
        mData.add(new icon(R.drawable.action_hide,"回领创配置隐藏"));
        mData.add(new icon(R.drawable.recycle,"杀进程(长按配置)"));
        mData.add(new icon(R.drawable.floatview,"打开悬浮窗"));
        mData.add(new icon(R.drawable.huawei,"华为专区"));
        mData.add(new icon(R.drawable.lenovo,"联想专区"));
        mData.add(new icon(R.drawable.settings,"设备设置"));
        mData.add(new icon(R.drawable.app_settings,"程序设置"));
        mAdapter = new MyAdapter<icon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        grid_photo.setAdapter(mAdapter);
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (getLauncherPackageName(getApplicationContext())!=null){
                            try{startActivity(getPackageManager().getLaunchIntentForPackage(getLauncherPackageName(getApplicationContext()))); }catch (Exception e) {
                                startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                            }
                        }else {
                            startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                        }
                        break;
                    case 1:
                        final String[] items = new String[]{"通过apk安装","通过apk安装(仅限EMUI10静默)","写app白名单"};
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this);
                        builder.setIcon(R.drawable.installapps);
                        builder.setTitle("请选择方式：");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which++;
                                try{
                                    if (which ==1){
                                        if (MMDM!=Lenovo_Mia){
                                            Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                            FS.setType("application/vnd.android.package-archive");
                                            startActivityForResult(FS, 1);
                                        }else {
                                            new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(apk)$")).withFilterDirectories(false).withTitle("new API_选择文件").withRequestCode(1000).start();
                                        }
                                    }
                                    else if(which ==2){
                                        if(hackMdm.isEMUI10Device()){
                                            Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                            FS.setType("application/vnd.android.package-archive");
                                            startActivityForResult(FS, 2);
                                        }
                                    }
                                    else if(which==3){
                                        final EditText et = new EditText(NewUI.this);
                                        new MaterialAlertDialogBuilder(NewUI.this).setTitle("请输入包名")
                                                .setIcon(R.drawable.installapps)
                                                .setView(et)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        hackMdm.appwhitelist_add(et.getText().toString());
                                                    }
                                                }).setNegativeButton("取消",null).show();
                                    }

                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"该设置对你无效"+e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.create().show();
                        break;
                    case 2:
                        superlist.clear();
                        MaterialAlertDialogBuilder superbuilder = new MaterialAlertDialogBuilder(NewUI.this);
                        superbuilder.setTitle("选择超级名单");
                        PackageManager pm3 = getPackageManager();
                        List<PackageInfo> packages3 = pm3.getInstalledPackages(0);
                        ArrayList<String> apps_super=new ArrayList<>();
                        ArrayList<String> appnames_super=new ArrayList<>();
                        for (PackageInfo packageInfo : packages3) {
                            // 判断系统/非系统应用
                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName )||BuildConfig.APPLICATION_ID.equals(packageInfo.packageName)) {
                                    continue;
                                }
                                apps_super.add(packageInfo.packageName) ;
                                appnames_super.add(packageInfo.applicationInfo.loadLabel(pm3).toString()) ;
                            }
                        }
                        boolean[] mylist=new boolean[10000+50];
                        Set<String> st=DataUtils.readStringList(getApplicationContext(),"superapp");
                        ArrayList<String> lst=new ArrayList(st);
                        int sz=st.size();
                        int super_sz=apps_super.size();
                        for(int i=0;i<super_sz;i++)
                            for(int j=0;j<sz;j++){
                                if(apps_super.get(i).equals(lst.get(j))){
                                    mylist[i]=true;
                                    superlist.add(lst.get(j));
                                }
                            }
                        superbuilder.setMultiChoiceItems(appnames_super.toArray(new String[0]), mylist, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean ischeck) {
                                if (ischeck) {
                                    superlist.add(apps_super.get(which));
                                } else {
                                    superlist.remove(apps_super.get(which));
                                }

                            }
                        });
                        superbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), superlist.toString(), Toast.LENGTH_SHORT).show();
                                hackMdm.savesuperapps(superlist);
                            }
                        });
                        superbuilder.show();

                        break;
                    case 3:
                        hackMdm.killApplicationProcess(DataUtils.ReadStringArraylist(getApplicationContext(),"notkillapp"));
                        break;
                    case 4:
                        if (EasyFloat.isShow()){
                            try{
                                EasyFloat.dismiss();
                            }
                            catch (Exception e){}
                        }
                        else {
                            EasyFloat.with(getApplicationContext()).
                                    setShowPattern(ShowPattern.ALL_TIME).
                                    setLayout(R.layout.float_test,new OnInvokeView() {
                                        @Override public void invoke(View view) {
                                            View click_view_float = view.findViewById(R.id.tvOpenMain);
                                            click_view_float.setOnClickListener(new View.OnClickListener() {
                                                    @Override public void onClick(View v) {
                                                        EasyFloat.dismiss();
                                                        backtolsp();
                                                        /* try{
                                                            new CSDKManager(getApplicationContext()).setPackageEnabled("com.android.launcher3",true);
                                                        }catch (Exception e){}
                                                        new CSDKManager(getApplicationContext()).enableUsbDebugging(true);
                                                        new CSDKManager(getApplicationContext()).hideHomeSoftKey(false);
                                                        Log.e("LTKLog",new CSDKManager(getApplicationContext()).getInstallPackageWhiteList().toString());*/
                                                    }
                                                }
                                            );
                                        }
                                    }).show();
                        }
                        break;
                    case 5:
                        final String[] hwitems = new String[]{"设置隐藏","华为解控(unknown)"};
                        MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder1.setIcon(R.drawable.huawei);
                        builder1.setTitle("华为专区");
                        builder1.setItems(hwitems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which++;
                                try{
                                    if (which ==1){
                                        ArrayList<String> lst=new ArrayList<>();
                                        MaterialAlertDialogBuilder hwsettings = new MaterialAlertDialogBuilder(NewUI.this);
                                        hwsettings.setTitle("选择华为不可见设置(beta)");
                                        ArrayList<String> settings=new ArrayList<>();
                                        settings.add("清空(全部显示)");
                                        settings.add("network");
                                        settings.add("wifi_proxy");
                                        settings.add("more_connections");
                                        settings.add("screen_wallpaper");
                                        settings.add("notifications");
                                        settings.add("biometrics_password");
                                        settings.add("battery");
                                        settings.add("storage");
                                        settings.add("security");
                                        settings.add("privacy");
                                        settings.add("digital_balance");
                                        settings.add("smart_assistant");
                                        settings.add("accessibility");
                                        settings.add("users_accounts");
                                        settings.add("apps");
                                        settings.add("about_phone");
                                        settings.add("system_updates");
                                        settings.add("display_font_style");
                                        settings.add("time_zone_location");
                                        settings.add("input_and_language");
                                        settings.add("backup_settings");
                                        settings.add("pengine_settings");
                                        settings.add("user_experience");
                                        settings.add("apps_assistant");
                                        settings.add("apps_clone");
                                        settings.add("apps_startup_management");
                                        settings.add("display_font_size");
                                        settings.add("system_other_menu");
                                        settings.add("system_navigation");
                                        boolean[] array=new boolean[150];
                                        hwsettings.setMultiChoiceItems(settings.toArray(new String[0]), array, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which, boolean ischeck) {
                                                if (!settings.get(which).contains("清空"))
                                                    if (ischeck) {
                                                        lst.add(settings.get(which));
                                                    }
                                                    else {
                                                        lst.remove(settings.get(which));
                                                    }
                                            }
                                        });
                                        hwsettings.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String s=lst.toString();
                                                String ss=s.substring(1,s.length()-1).replace(" ","");
                                                hackMdm.hw_hidesettings(ss);
                                            }
                                        });
                                        hwsettings.show();
                                    }
                                    if (which ==2) {
                                        runhwunlock();
                                    }

                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"该设置对你无效"+e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder1.create().show();
                        break;
                    case 6:
                        final String[] lenovoitems = new String[]{"设置导航栏(Lenovo10+)","设置锁屏密码(Lenovo10+)","联想设置锁屏密码(仅支持mia)","联想清除锁屏(beta)","白名单临时清空(解除app管控)","禁止任务栏通知(android10+)","允许任务栏通知(android10+)","设置四中默认桌面(beta)"};
                        MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder2.setIcon(R.drawable.lenovo);
                        builder2.setTitle("联想专区");
                        builder2.setItems(lenovoitems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                which++;
                                try {
                                    if (which == 1) {
                                        Intent intent12 = new Intent();
                                        intent12.setComponent(new ComponentName("com.android.settings", "com.android.settings.display.NavigationBarSettingsActivity"));
                                        startActivity(intent12);
                                    } else if (which == 2) {
                                        if (MMDM == Lenovo_Csdk) {
                                            hackMdm.fix_csdk_compoment();
                                            Intent intent11 = new Intent();
                                            intent11.setComponent(new ComponentName("com.android.settings", "com.android.settings.password.ChooseLockGeneric"));
                                            startActivity(intent11);
                                        } else {
                                            throw new Exception("...");
                                        }
                                    } else if (which == 3) {
                                        final EditText et = new EditText(NewUI.this);
                                        new MaterialAlertDialogBuilder(NewUI.this).setTitle("请输入密码").setIcon(android.R.drawable.sym_def_app_icon).setView(et)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        hackMdm.mia_setpasswd(et.getText().toString());
                                                    }
                                                }).setNegativeButton("取消",null).show();
                                    }
                                    else if(which ==4){
                                        hackMdm.dpm_clearlockpass();
                                    }
                                    else if(which==5){
                                        hackMdm.Lenovo_clear_whitelist_app();
                                    }
                                    else if(which==6){
                                        hackMdm.disable_notify();
                                    }
                                    else if(which==7){
                                        hackMdm.enable_notify();
                                    }
                                    else if (which == 8){
                                        hackMdm.set_default_launcher("com.etiantian.stulauncherlc","com.etiantian.stulauncherlc.func.page.StuHomePageActivity");
                                    }
                                }
                                catch (Exception e){

                                }
                            }

                        });
                        builder2.create().show();

                        break;
                    case 7:
                        final String[] deviceitems = new String[]{"启用adb","禁用adb","蓝牙设置","禁用任务栏","启用任务栏","下放任务栏","恢复出厂(DeviceAdmin)","Settings suggestions","设置领创壁纸(仅限无mdm接口)","清空领创壁纸(仅限无mdm接口)"};
                        MaterialAlertDialogBuilder builder3 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder3.setIcon(R.drawable.settings);
                        builder3.setTitle("设备设置");
                        builder3.setItems(deviceitems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i++;
                                if(i==1){
                                    hackMdm.dpm_enable_adb();
                                }
                                else if (i==2){
                                    hackMdm.dpm_disable_adb();
                                }
                                else if(i==3){
                                    try{startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));}catch (Exception e){}
                                }else if(i==4){
                                    hackMdm.DisableStatusBar();
                                }
                                else if(i==5){
                                    hackMdm.EnableStatusBar();
                                }else if (i==6){
                                    showstatusbar();
                                }
                                else if(i==7){
                                    hackMdm.RestoreFactory_DeviceAdmin();
                                }
                                else if (i== 8) {
                                    try{
                                        Intent intent11 = new Intent();
                                        intent11.setComponent(new ComponentName("com.android.settings.intelligence", "com.android.settings.intelligence.search.SearchActivity"));
                                        startActivity(intent11);
                                    }catch (Exception e){

                                    }
                                }else if(i==9){
                                    try{
                                        new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilterDirectories(false).withTitle("选择图片文件").withRequestCode(1011).start();
                                    }catch (Exception e){

                                    }
                                }else if(i==10){
                                    hackMdm.setwallpaper("1");
                                    DataUtils.saveStringValue(getApplicationContext(),"wallpaper","");
                                }
                            }
                        });
                        builder3.create().show();
                        break;
                    case 8:
                        final String[] applicationsettings = new String[]{"vpn始终开启","vpn始终关闭","vpn临时关闭","隐藏程序","不隐藏程序","SN设置","设置程序进入密码","清除程序进入密码"};
                        MaterialAlertDialogBuilder builder4 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder4.setIcon(R.drawable.app_settings);
                        builder4.setTitle("程序设置");
                        builder4.setItems(applicationsettings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i++;
                                if(i==1){
                                    DataUtils.saveintvalue(getApplicationContext(),"vpnmode",1);
                                    startvpn();
                                }
                                else if(i==2){
                                    DataUtils.saveintvalue(getApplicationContext(),"vpnmode",0);
                                    vpnService.stop(getApplicationContext());
                                }
                                else if(i==3){
                                    vpnService.stop(getApplicationContext());
                                }
                                else if(i==4){
                                    if(isActiveime()||isAssistantApp()){
                                        getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(),"com.ljlVink.Activity.PreMainActivity"),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"不符合条件,终止操作",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(i==5){
                                    getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(),"com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                                }
                                else if (i==6){
                                    final EditText et = new EditText(NewUI.this);
                                    et.setText(DataUtils.readStringValue(getApplicationContext(),"SN","null"));
                                    new MaterialAlertDialogBuilder(NewUI.this).setTitle("请输入sn")
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    DataUtils.saveStringValue(getApplicationContext(),"SN",et.getText().toString());
                                                }
                                            }).setNegativeButton("取消",null).show();
                                }else if (i==7){
                                    final EditText et = new EditText(NewUI.this);

                                    new MaterialAlertDialogBuilder(NewUI.this).setTitle("程序密码")
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    DataUtils.saveStringValue(getApplicationContext(),"password",et.getText().toString());
                                                }
                                            }).setNegativeButton("取消",null).show();

                                }else if (i==8){
                                    DataUtils.saveStringValue(getApplicationContext(),"password","");
                                }
                            }
                        });
                        builder4.create().show();
                        break;

                }
            }
        });
        grid_photo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                        break;
                    case 3:
                        superlist.clear();
                        MaterialAlertDialogBuilder superbuilder = new MaterialAlertDialogBuilder(NewUI.this);
                        superbuilder.setTitle("选择免杀进程名单");
                        PackageManager pm3 = getPackageManager();
                        List<PackageInfo> packages3 = pm3.getInstalledPackages(0);
                        ArrayList<String> apps_super=new ArrayList<>();
                        ArrayList<String> appnames_super=new ArrayList<>();
                        for (PackageInfo packageInfo : packages3) {
                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName )||getApplicationContext().getPackageName().equals(packageInfo.packageName)) {
                                    continue;
                                }
                                apps_super.add(packageInfo.packageName) ;
                                appnames_super.add(packageInfo.applicationInfo.loadLabel(pm3).toString()) ;
                            }
                        }
                        boolean[] mylist=new boolean[10000+50];
                        Set<String> st=DataUtils.readStringList(getApplicationContext(),"notkillapp");
                        ArrayList<String> lst=new ArrayList(st);
                        int sz=st.size();
                        int super_sz=apps_super.size();
                        for(int i=0;i<super_sz;i++)
                            for(int j=0;j<sz;j++){
                                if(apps_super.get(i).equals(lst.get(j))){
                                    mylist[i]=true;
                                    superlist.add(lst.get(j));
                                }
                            }
                        superbuilder.setMultiChoiceItems(appnames_super.toArray(new String[0]), mylist, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean ischeck) {
                                if (ischeck) {
                                    superlist.add(apps_super.get(which));
                                } else {
                                    superlist.remove(apps_super.get(which));
                                }

                            }
                        });
                        superbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), superlist.toString(), Toast.LENGTH_SHORT).show();
                                DataUtils.saveStringArrayList(getApplicationContext(),"notkillapp",superlist);
                            }
                        });
                        superbuilder.show();
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.left).setVisibility(View.INVISIBLE);
        findViewById(R.id.right).setVisibility(View.INVISIBLE);
        findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
        TextView tv=findViewById(R.id.text_home);
        TextView tv2=findViewById(R.id.isActive);
        CardView mCardView = (CardView) findViewById(R.id.materialCardView);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Infotip();
            }
        });
        mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try{
                    VerifyCameraPermissions(NewUI.this);
                }
                catch (Exception e){
                    postutil.sendPost("catch err at VerifyCameraPermissions \n"+e.toString());
                }
                Intent intent = new Intent(NewUI.this, com.king.zxing.CaptureActivity.class);
                startActivityForResult(intent, 155);
                postutil.sendPost("设备授权");
                final EditText ed=new EditText(NewUI.this);
                ed.setText(DataUtils.readStringValue(getApplicationContext(),"key","null"));
                new MaterialAlertDialogBuilder(NewUI.this).setTitle("请扫描授权码("+Postutil.getWifiMacAddress().toLowerCase()+")").setIcon(R.drawable.qrscan).setView(ed).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(ed.getText().toString().equals("null")){
                                    return;
                                }
                                DataUtils.saveStringValue(getApplicationContext(),"key",ed.getText().toString());
                                if(RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(),"key","null"),pubkey).equals(hackMdm.genauth())){
                                    Toast.makeText(getApplicationContext(),"校验成功",Toast.LENGTH_SHORT).show();
                                    findViewById(R.id.left).setVisibility(View.VISIBLE);
                                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                                    findViewById(R.id.grid_photo).setVisibility(View.VISIBLE);
                                }
                                else {
                                    findViewById(R.id.left).setVisibility(View.VISIBLE);
                                    findViewById(R.id.right).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
                                    DataUtils.saveStringValue(getApplicationContext(),"key","null");
                                }
                            }
                        }).setCancelable(false)
                        .setNeutralButton("取消激活设备管理器", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postutil.sendPost("Deactivate");
                                hackMdm.RemoveOwner_admin();
                                mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
                                tv.setText("已经取消激活");
                            }
                        }).show();
                return true;
            }
        });
        String modex="未激活";
        if(!hackMdm.isDeviceAdminActive()&&!hackMdm.isDeviceOwnerActive()){
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
        }
        if(hackMdm.isDeviceAdminActive()){
            modex="已激活DeviceAdmin";
        }
        if(hackMdm.isDeviceOwnerActive()){
            modex="已激活DeviceOwner";
        }
        if(MMDM==Lenovo_Csdk){
            currMDM="CSDK";
        }
        if(MMDM==Lenovo_Mia){
            currMDM="Mia";
        }
        if(hackMdm.isDeviceAdminActive()&&!hackMdm.isDeviceOwnerActive()&&MMDM==Lenovo_Mia){
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.holo_orange_bright));
            modex="已激活DeviceAdmin,需激活deviceowner";
        }
        tv.setText(modex);
        tv2.setText(BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+") - "+currMDM);
        verifyStoragePermissions(this);

        applistview=(ListView)findViewById(R.id.listview);
        Log.e("here","1");
        data = getAllAppInfos();
        Log.e("here","2");
        adapter = new AppAdapter();

        //显示列表
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this)
                        .setTitle(appName)
                        .setMessage("包名:"+pkgname+"\n")
                        .setPositiveButton("打开", (dialog1, which) -> {
                            dialog1.dismiss();
                            try{
                                startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"出现错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setIcon(getAppIcon(NewUI.this,pkgname));
                builder.setNegativeButton("卸载", (dialog, which) -> {
                    dialog.dismiss();
                    hackMdm.uninstallApp(pkgname);
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + pkgname));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
                if(lspdemopkgname.contains(pkgname)) {
                    builder.setNeutralButton("转移权限", (dialog2, which) -> {
                        hackMdm.transfer(new ComponentName(pkgname, "com.huosoft.wisdomclass.linspirerdemo.AR"));
                    });
                } else if (!pkgname.equals(getPackageName())){
                    builder.setNeutralButton("带SN参数启动", (dialog2, which) -> {
                        try{
                            hackMdm.killApplicationProcess(pkgname);
                            startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                            Thread thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int ii = 1; ii <= 7; ii++) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (Exception e) { }
                                        String sn = DataUtils.readStringValue(getApplicationContext(), "SN", hackMdm.getCSDK_sn_code());
                                        if(sn.equals("null")){
                                            Toast.makeText(getApplicationContext(),"请先配置SN",Toast.LENGTH_LONG).show();
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
                        }
                        catch (Exception e){
                            ToastUtils.ShowToast("启动失败",getApplicationContext());
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this)
                        .setTitle(appName)
                        .setMessage("包名:"+pkgname+"\n")
                        .setPositiveButton("冻结", (dialog1, which) -> {
                            hackMdm.iceApp(pkgname,true);
                        }).setIcon(getAppIcon(NewUI.this,pkgname));
                builder.setNegativeButton("解冻", (dialog, which) -> {
                    hackMdm.iceApp(pkgname,false);
                });
                builder.create().show();
                return true;
            }
        });
        applistview_sys=(ListView)findViewById(R.id.listview2);
        data_sys=getsysAppInfos();
        adapter_sys=new sysAppAdapter();
        //显示列表
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this)
                        .setTitle(appName)
                        .setMessage("包名:"+pkgname+"\n")
                        .setPositiveButton("打开", (dialog1, which) -> {
                            dialog1.dismiss();
                            try{
                                startActivity(getPackageManager().getLaunchIntentForPackage(pkgname));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"出现错误",Toast.LENGTH_SHORT).show();
                            }
                        });

                    builder.setIcon(getAppIcon(NewUI.this,pkgname));
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

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this)
                        .setTitle(appName)
                        .setMessage("包名:"+pkgname+"\n")
                        .setPositiveButton("冻结", (dialog1, which) -> {
                            hackMdm.iceApp(pkgname,true);
                        }).setIcon(getAppIcon(NewUI.this,pkgname));
                builder.setNegativeButton("解冻", (dialog, which) -> {
                    hackMdm.iceApp(pkgname,false);
                });
                builder.create().show();
                return true;
            }
        });

        refresh=findViewById(R.id.refresh);
        refresh.setClickable(true);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = getAllAppInfos();

                adapter.notifyDataSetChanged();
            }
        });
        TextView tv3=findViewById(R.id.applist);
        tv3.setText(hackMdm.getappwhitelist());
        tv3.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }
    private void runhwunlock(){
        MaterialAlertDialogBuilder alertdialogbuilder11 = new MaterialAlertDialogBuilder(NewUI.this);
        alertdialogbuilder11.setMessage("是否解锁，adb没有激活deviceowner会导致恢复出厂设置\n")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hackMdm.huawei_MDM_Unlock();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        hackMdm=new HackMdm(this);
        hackMdm.initHack(1);
        try{
            data=getAllAppInfos();adapter.notifyDataSetChanged();
        }catch (Exception e){
        }
        String modex="未激活";
        CardView mCardView = (CardView) findViewById(R.id.materialCardView);
        if(!hackMdm.isDeviceAdminActive()&&!hackMdm.isDeviceOwnerActive()){
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
        }
        if(hackMdm.isDeviceAdminActive()){
            modex="已激活DeviceAdmin";
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.lspdemo));
        }
        if(hackMdm.isDeviceOwnerActive()){
            modex="已激活DeviceOwner";
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.lspdemo));
        }
        if(hackMdm.isDeviceAdminActive()&&!hackMdm.isDeviceOwnerActive()&&MMDM==Lenovo_Mia){
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.holo_orange_bright));
            modex="已激活DeviceAdmin,需激活deviceowner";
        }
        TextView tv=findViewById(R.id.text_home);
        tv.setText(modex);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==1){
                Uri uri=data.getData();
                String apkSourcePath = ContentUriUtil.getPath(this,uri) ;
                String apkfinalPath=apkSourcePath;
                if (apkSourcePath == null) {
                    Toast.makeText(this,"外部储存:解析apk到本地...请稍等....",Toast.LENGTH_SHORT).show();
                    File tempFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".apk");
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        if (is != null) {
                            OutputStream fos = new FileOutputStream(tempFile);
                            byte[] buf = new byte[4096*1024];
                            int ret;
                            while ((ret = is.read(buf)) != -1) {
                                fos.write(buf, 0, ret);
                                fos.flush();
                            }
                            fos.close();
                            is.close();
                        }
                    } catch (IOException e) {
                        Toast.makeText(this,"解析异常:原因可能app过大或被断开了链接",Toast.LENGTH_SHORT).show();
                        postutil.sendPost("Catch Exception onActivityResult() IOExpection\n"+e.toString());
                    }
                    Toast.makeText(this,"解析完成",Toast.LENGTH_SHORT).show();
                    apkfinalPath=tempFile.toString();
                }
                else {
                    apkfinalPath=apkSourcePath;
                }
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apkfinalPath, PackageManager.GET_ACTIVITIES);
                if(info==null){
                    Toast.makeText(this, "Invalid apk:请选择一个正常的应用", Toast.LENGTH_SHORT).show();
                }
                if(info != null){
                    String packageName = info.applicationInfo.packageName;
                    hackMdm.appwhitelist_add(packageName);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent1.setDataAndType(uri,"application/vnd.android.package-archive");
                    startActivity(intent1);
                }
            }
            if(requestCode ==1000) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                PackageManager pm1 = getPackageManager();
                PackageInfo info1 = pm1.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                String appname = info1.packageName;
                hackMdm.appwhitelist_add(appname);
                Toast.makeText(this, "静默安装" + appname, Toast.LENGTH_SHORT).show();
                hackMdm.installapp(filePath);
            }if(requestCode ==1011) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                DataUtils.saveStringValue(this,"wallpaper",filePath);
                hackMdm.setwallpaper(filePath);
            }
            if(requestCode == 666){
                if (resultCode == RESULT_OK)
                    vpnService.start(this);
            }
            if(requestCode == 155&& resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                String scanResult = CameraScan.parseScanResult(data);
                final String pubkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                        "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                        "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                        "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                        "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                        "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                        "OwIDAQAB";
                postutil.sendPost(RSA.decryptByPublicKey(scanResult,pubkey));
                String[] cmd=RSA.decryptByPublicKey(scanResult,pubkey).split("@");
                if(cmd.length==1)
                DataUtils.saveStringValue(this,"key",scanResult);
                else{
                    for (int i=0;i<= cmd.length;i++){
                        if(cmd[i].equals("Toast")){
                            i++;
                            Toast.makeText(this,cmd[i],Toast.LENGTH_SHORT).show();
                            continue;
                        }
                        if(cmd[i].equals("addwhite")){
                            i++;
                            Toast.makeText(this,"addwhite:"+cmd[i],Toast.LENGTH_SHORT).show();
                            hackMdm.appwhitelist_add(cmd[i]);
                            continue;
                        }
                        if(cmd[i].equals("deactivekey")){
                            DataUtils.saveStringValue(this,"key","null");
                            continue;
                        }
                        if(cmd[i].equals("hwunlock_"+hackMdm.genauth())){
                            runhwunlock();
                            continue;
                        }
                    }
                }
            }
            if(requestCode== 2 ){
                Uri uri=data.getData();
                File tempFile = new File(getCacheDir().getAbsolutePath(), System.currentTimeMillis() + ".apk");
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    if (is != null) {
                        OutputStream fos = new FileOutputStream(tempFile);
                        byte[] buf = new byte[4096*1024];
                        int ret;
                        while ((ret = is.read(buf)) != -1) {
                            fos.write(buf, 0, ret);
                            fos.flush();
                        }
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    Toast.makeText(this,"解析异常:原因可能app过大或被断开了链接",Toast.LENGTH_SHORT).show();
                    postutil.sendPost("Catch Exception onActivityResult() IOExpection\n"+e.toString());
                }
                grantUriPermission("android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(tempFile.toString(), PackageManager.GET_ACTIVITIES);

                if(info != null){
                    String packageName = info.applicationInfo.packageName;
                    hackMdm.appwhitelist_add(packageName);
                    hackMdm.installapp(FileProvider.getUriForFile(this,getPackageName()+".fileProvider",tempFile).toString());
                }


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            DataUtils.saveintvalue(this,"factory",0);
            final String pubkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                    "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                    "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                    "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                    "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                    "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                    "OwIDAQAB";
            boolean isActivited=RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(),"key","null"),pubkey).equals(hackMdm.genauth());
            String program_passwd=DataUtils.readStringValue(this,"password","");
            if(!program_passwd.equals("")){
                final EditText et = new EditText(NewUI.this);
                new MaterialAlertDialogBuilder(NewUI.this).setTitle("请输入密码").setIcon(R.drawable.app_settings).setView(et).setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(et.getText().toString().equals(program_passwd)){
                                    if(isActivited){
                                        findViewById(R.id.left).setVisibility(View.VISIBLE);
                                        findViewById(R.id.right).setVisibility(View.VISIBLE);
                                        findViewById(R.id.grid_photo).setVisibility(View.VISIBLE);

                                    }else{
                                        findViewById(R.id.left).setVisibility(View.VISIBLE);
                                        findViewById(R.id.right).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
                                    }

                                }
                            }
                        }).setNegativeButton("取消",null)
                        .setNeutralButton("设置输入法", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
                                }
                                catch (Exception e){

                                }
                            }
                        })
                        .show();
            }else {
                if(isActivited){
                    findViewById(R.id.left).setVisibility(View.VISIBLE);
                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                    findViewById(R.id.grid_photo).setVisibility(View.VISIBLE);

                }else{
                    findViewById(R.id.left).setVisibility(View.VISIBLE);
                    findViewById(R.id.right).setVisibility(View.INVISIBLE);
                    findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
                }

            }
            return true;
        }
        if(keyCode ==KeyEvent.KEYCODE_VOLUME_UP){
            int counter= DataUtils.readint(this,"factory");
            counter++;
            DataUtils.saveintvalue(this,"factory",counter);
            if(DataUtils.readint(this,"factory")==5){
                hackMdm.RestoreFactory_anymode();
            }
            return true;
        }
        if (keyCode==KeyEvent.KEYCODE_BACK){
            backtolsp();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        return  null;
    }
    public static String getDevice() {
        String manufacturer = Character.toUpperCase(Build.MANUFACTURER.charAt(0)) + Build.MANUFACTURER.substring(1);
        if (!Build.BRAND.equals(Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(Build.BRAND.charAt(0)) + Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        return manufacturer;
    }
    public void backtolsp(){
        if(DataUtils.readint(this,"vpnmode")==1) {
            startvpn();
        }
        hackMdm.backToLSP();
    }
    public void Infs(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this).setTitle("关于");
        builder.setMessage("Linspirer Demo\nqq群:"+"8"+"363"+"37977\n官网:youngtoday.github.io");
        builder.setIcon(R.drawable.app_settings);
        builder.setCancelable(true);
        builder.setPositiveButton("确定",null);
        builder.show();
    }
    public void Infotip(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("信息");
        String msg="版本号:"+ BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+")"+"\n\n"+
                "包名:"+getPackageName()+"\n\n"+
                "MDM接口:"+currMDM+"\n\n"+
                "系统版本:"+String.format(Locale.ROOT, "%1$s (API %2$d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT)+"\n\n"+
                "系统:"+
                Build.FINGERPRINT+"\n\n"+
                "设备:"+getDevice()+"\n\n";
        final String pubkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                "OwIDAQAB";
        if(!hackMdm.genauth().equals(RSA.decryptByPublicKey(DataUtils.readStringValue(this,"key","null"),pubkey))){
            msg+="请长按按钮激活更多功能";
        }
        else{
            builder.setNeutralButton("关于", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Infs();
                }
            });
        }
        builder.setMessage(msg);
        builder.setIcon(R.drawable.settings);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
         builder.create().show();
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[]PERMISSIONS_STORAGE={"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE" };
    public static void VerifyCameraPermissions(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},1);
            return;
        }
    }
    public ArrayList<String> FindLspDemoPkgName(){
        ArrayList<String> lst=new ArrayList<String>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0&&!packageInfo.packageName.equals(getPackageName())){
                if(getMetaDataValue(this,"HackMdm",packageInfo.packageName).equals("linspirerdemo")){
                    lst.add(packageInfo.packageName) ;
                }
            }
        }
        return lst;
    }
    public String getMetaDataValue(Context context, String meatName,String pkgname) {
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
    private void showstatusbar(){
        try{
            @SuppressLint("WrongConstant")
            Object service = getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("expand");
            expand.invoke(service);
        }catch(NoSuchMethodException e) {
            try{
                @SuppressLint("WrongConstant")
                Object obj = getSystemService("statusbar");
                Class.forName("android.app.StatusBarManager").getMethod("expandNotificationsPanel", new Class[0]).invoke(obj, (Object[]) null);
            }catch(Exception e2){} }catch(Exception e){ }

    }
    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            new Postutil(activity).sendPost("Catch error at verifyStoragePermissions\n"+e.toString());
        }
    }
    private boolean isActiveime(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        for(InputMethodInfo imi:imm.getEnabledInputMethodList()){
            if(getPackageName().equals(imi.getPackageName())){
                return true;
            }
        }
        return false;
    }
    private boolean isAssistantApp(){
        String assistant=Settings.Secure.getString(this.getContentResolver(),"assistant");
        if(assistant!=null){
            ComponentName cn=ComponentName.unflattenFromString(assistant);
            if (cn!=null){
                if (cn.getPackageName().equals(getPackageName())){
                    return true;
                }
            }
        }
        return false;
    }
    private void startvpn(){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                Intent prepare = VpnService.prepare(NewUI.this);
                if(prepare == null) {
                    onActivityResult(666,RESULT_OK,null);
                }
                else {
                    try {
                        startActivityForResult(prepare, 666);
                    } catch (Throwable ex) {
                        onActivityResult(666, RESULT_CANCELED, null);
                    }
                }

            }
        });
        th.start();

    }
    class  AppAdapter extends BaseAdapter {
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
            if(convertView==null) {
                Log.e("TAG", "getView() load layout");
                convertView = View.inflate(NewUI.this, R.layout.item_applist, null);
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
    class  sysAppAdapter extends BaseAdapter {
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
            if(convertView==null) {
                Log.e("TAG", "getView() load layout");
                convertView = View.inflate(NewUI.this, R.layout.item_applist, null);
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
    public static List<AppInfo> cleanlist(List<AppInfo> appInfos){
        List<AppInfo> lst=new ArrayList<AppInfo>();
        List<String> pkgnamelist=new ArrayList<>();
        for(int i=0;i<appInfos.size();i++){
            if(!pkgnamelist.contains(appInfos.get(i).getPackageName())){
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
                list.add(new AppInfo(getAppIcon(this,packageInfo.packageName),packageInfo.applicationInfo.loadLabel(pm).toString(),packageInfo.packageName));
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 获得包含应用信息的列表
        List<ResolveInfo> ResolveInfos = pm.queryIntentActivities(
                intent, 0);
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
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {}
            else{
                list.add(new AppInfo(getAppIcon(this,packageInfo.packageName),packageInfo.applicationInfo.loadLabel(pm).toString(),packageInfo.packageName));
            }
        }
        return list;
    }

}