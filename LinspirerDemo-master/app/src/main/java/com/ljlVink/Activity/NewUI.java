package com.ljlVink.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.king.zxing.CameraScan;
import com.ljlVink.ToastUtils.Toast;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.core.DataUtils;
import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.ContentUriUtil;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.core.HackMdm;
import com.ljlVink.core.RSA;

import com.ljlVink.core.security.envcheck;
import com.ljlVink.linspirerfake.uploadHelper;
import com.ljlVink.linspirerfake.utils;
import com.ljlVink.services.vpnService;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.interfaces.OnInvokeView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.zy.devicelibrary.utils.CpuUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import activitylauncher.MainActivity;

public class NewUI extends AppCompatActivity {
    private final int Lenovo_Mia = 3;
    private final int Lenovo_Csdk = 2;
    private final int T11=4;
    private String currMDM = "DevicePolicyManager";
    private HackMdm hackMdm;
    private BaseAdapter mAdapter = null;
    private ArrayList<String> superlist = new ArrayList<>();
    private ArrayList<icon> mData = null;
    private int MMDM;
    private GridView grid_photo;
    private Postutil postutil;
    private ScrollView right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                "OwIDAQAB";
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
        ImmersionBar.with(this).transparentStatusBar().init();
        setContentView(R.layout.activity_new_ui);
        super.onCreate(savedInstanceState);
        hackMdm = new HackMdm(this);
        postutil = new Postutil(this);
        postutil.CloudAuthorize();
        hackMdm.initHack(0);
        //?????????view
        new uploadHelper(this,true).uplpadfakeapps();

        hackMdm.findowner();
        if(!isTabletDevice(this)){
            right=findViewById(R.id.scrollvew_right);
            right.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        MMDM = hackMdm.getMMDM();
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        mData = new ArrayList<icon>();
        mData.add(new icon(R.drawable.backtodesktop, "????????????"));
        mData.add(new icon(R.drawable.installapps, "????????????"));
        mData.add(new icon(R.drawable.appmanage,"????????????"));
        mData.add(new icon(R.drawable.action_hide, "?????????????????????"));
        mData.add(new icon(R.drawable.recycle, "?????????(????????????)"));
        mData.add(new icon(R.drawable.floatview, "???????????????"));
        mData.add(new icon(R.drawable.huawei, "????????????"));
        mData.add(new icon(R.drawable.lenovo, "????????????"));
        mData.add(new icon(R.drawable.device_settings, "????????????"));
        mData.add(new icon(R.drawable.app_settings, "????????????"));
        mData.add(new icon(R.drawable.activitylauncher_ic_launcher_foreground, "???????????????"));
        mData.add(new icon(R.drawable.help, "??????"));
        mData.add(new icon(R.drawable.ic_baseline_calculate_24, "?????????"));
        mData.add(new icon(R.drawable.swordplan, "????????????"));
        mData.add(new icon(R.drawable.linspirer, "????????????"));
        mData.add(new icon(R.drawable.linspirer, "????????????(????????????)"));
        mData.add(new icon(R.drawable.linspirer, "??????????????????app??????"));
        mData.add(new icon(R.drawable.tensafe,"T11??????"));
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
                switch (position) {
                    case 0:
                        if (getLauncherPackageName(getApplicationContext()) != null) {
                            try {
                                startActivity(getPackageManager().getLaunchIntentForPackage(getLauncherPackageName(getApplicationContext())));
                            } catch (Exception e) {
                                startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                            }
                        } else {
                            startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                        }
                        break;
                    case 1:
                        final String[] items = new String[]{"??????apk??????(????????????)", "??????apk??????(DocumentUI)", "????????????(Filepicker)", "????????????(??????EMUI10??????)", "???app?????????"};
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewUI.this);
                        builder.setIcon(R.drawable.installapps);
                        builder.setTitle("??????????????????");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which++;
                                try {
                                    if (which == 1) {
                                        if (MMDM != Lenovo_Mia) {
                                            Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                            FS.setType("application/vnd.android.package-archive");
                                            startActivityForResult(FS, 1);
                                        } else {
                                            new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(apk)$")).withFilterDirectories(false).withTitle("new API_????????????").withRequestCode(1000).start();
                                        }
                                    } else if (which == 2) {
                                        Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                        FS.setType("application/vnd.android.package-archive");
                                        startActivityForResult(FS, 1);
                                    } else if (which == 3) {
                                        new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(apk)$")).withFilterDirectories(false).withTitle("new API_????????????").withRequestCode(1000).start();
                                    } else if (which == 4) {
                                        if (hackMdm.isEMUI10Device()) {
                                            Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                            FS.setType("application/vnd.android.package-archive");
                                            startActivityForResult(FS, 2);
                                        }
                                    } else if (which == 5) {
                                        final EditText et = new EditText(NewUI.this);
                                        new MaterialAlertDialogBuilder(NewUI.this).setTitle("???????????????")
                                                .setIcon(R.drawable.installapps)
                                                .setView(et)
                                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        hackMdm.appwhitelist_add(et.getText().toString());
                                                    }
                                                }).setNegativeButton("??????", null).show();
                                    }

                                } catch (Exception e) {
                                    Toast.ShowErr(NewUI.this,"?????????????????????");
                                }
                            }
                        });
                        builder.create().show();
                        break;
                    case 2:
                        startActivity(new Intent(NewUI.this,AppManageActivity.class));
                        break;
                    case 3:
                        superlist.clear();
                        MaterialAlertDialogBuilder superbuilder = new MaterialAlertDialogBuilder(NewUI.this);
                        superbuilder.setTitle("??????????????????");
                        PackageManager pm3 = getPackageManager();
                        List<PackageInfo> packages3 = pm3.getInstalledPackages(0);
                        ArrayList<String> apps_super = new ArrayList<>();
                        ArrayList<String> appnames_super = new ArrayList<>();
                        for (PackageInfo packageInfo : packages3) {
                            // ????????????/???????????????
                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName) || BuildConfig.APPLICATION_ID.equals(packageInfo.packageName)) {
                                    continue;
                                }
                                apps_super.add(packageInfo.packageName);
                                appnames_super.add(packageInfo.applicationInfo.loadLabel(pm3).toString());
                            }
                        }
                        boolean[] mylist = new boolean[10000 + 50];
                        Set<String> st = DataUtils.readStringList(getApplicationContext(), "superapp");
                        ArrayList<String> lst = new ArrayList(st);
                        int sz = st.size();
                        int super_sz = apps_super.size();
                        for (int i = 0; i < super_sz; i++)
                            for (int j = 0; j < sz; j++) {
                                if (apps_super.get(i).equals(lst.get(j))) {
                                    mylist[i] = true;
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
                        superbuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hackMdm.savesuperapps(superlist);
                            }
                        });
                        superbuilder.show();

                        break;
                    case 4:
                        hackMdm.killApplicationProcess(DataUtils.ReadStringArraylist(getApplicationContext(), "notkillapp"));
                        break;
                    case 5:
                        if (EasyFloat.isShow()) {
                            try {
                                EasyFloat.dismiss();
                            } catch (Exception e) {
                            }
                        } else {
                            EasyFloat.with(getApplicationContext()).
                                    setShowPattern(ShowPattern.ALL_TIME).
                                    setLayout(R.layout.float_test, new OnInvokeView() {
                                        @Override
                                        public void invoke(View view) {
                                            View click_view_float = view.findViewById(R.id.tvOpenMain);
                                            click_view_float.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        EasyFloat.dismiss();
                                                        backtolsp();
                                                        setfalseVisibility();
                                                    }
                                                }
                                            );
                                        }
                                    }).show();
                        }
                        break;
                    case 6:
                        final String[] hwitems = new String[]{"????????????", "????????????(unknown)"};
                        MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder1.setIcon(R.drawable.huawei);
                        builder1.setTitle("????????????");
                        builder1.setItems(hwitems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                which++;
                                try {
                                    if (which == 1) {
                                        ArrayList<String> lst = new ArrayList<>();
                                        MaterialAlertDialogBuilder hwsettings = new MaterialAlertDialogBuilder(NewUI.this);
                                        hwsettings.setTitle("???????????????????????????(beta)");
                                        ArrayList<String> settings = new ArrayList<>();
                                        settings.add("??????(????????????)");
                                        if (Build.VERSION.SDK_INT >= 29) {
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
                                        } else {
                                            settings.add("com.android.settings.Settings$AppAndNotificationDashboardActivity");
                                            settings.add("com.android.settings.Settings$HomeAndUnlockSettingsActivity");
                                            settings.add("com.huawei.notificationmanager.ui.NotificationManagmentActivity");
                                            settings.add("com.android.settings.Settings$StorageDashboardActivity");
                                            settings.add("com.huawei.parentcontrol.ui.activity.HomeActivity");
                                            settings.add("com.android.settings.Settings$SecurityDashboardActivity");
                                            settings.add("com.android.settings.Settings$BluetoothSettingsActivity");
                                            settings.add("com.huawei.systemmanager.power.ui.HwPowerManagerActivity");
                                            settings.add("com.android.settings.Settings$UserAndAccountDashboardActivity");
                                            settings.add("com.android.settings.Settings$MoreAssistanceSettingsActivity");
                                            settings.add("com.android.settings.Settings$AppCloneActivity");
                                            settings.add("com.huawei.hwid.cloudsettings.ui.HuaweiIDForSettingsActivity");
                                            settings.add("com.google.android.gms.app.settings.GoogleSettingsIALink");
                                            settings.add("com.android.settings.Settings$FingerprintEnrollSuggestionActivity");
                                            settings.add("com.android.settings.Settings$ZenModeAutomationSuggestionActivity");
                                            settings.add(" com.android.settings.facechecker.unlock.FaceUnLockSettingsActivity$FaceUnLockSuggestionActivity");
                                            settings.add("com.android.settings.wallpaper.WallpaperSuggestionActivity");
                                            settings.add("com.huawei.android.remotecontrol.ui.PhoneFinderForSettingActivity");
                                            settings.add("com.huawei.android.hicloud.ui.activity.BackupMainforSettingActivity");
                                            settings.add("com.huawei.android.FloatTasks.settings.FloatTaskSuggestionSettings");
                                            settings.add("toggle_airplane");
                                            settings.add("title_traffic_management");
                                            settings.add("vpn_settings");
                                            settings.add("system_navigation");
                                            settings.add("language_settings");
                                            settings.add("data_transmission");
                                            settings.add("backup_settings");
                                            settings.add("reset_settings");
                                            settings.add("user_experience_improve_plan");
                                            settings.add("authentication_info");
                                            settings.add("air_sharing");
                                            settings.add("usb_mode");
                                            settings.add("print_settings");
                                            settings.add("wifi_display");
                                            settings.add("font_size");
                                            settings.add("mobile_network_settings");
                                            settings.add("tether_settings");
                                            settings.add("call_settings");
                                        }
                                        boolean[] array = new boolean[150];
                                        hwsettings.setMultiChoiceItems(settings.toArray(new String[0]), array, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which, boolean ischeck) {
                                                if (!settings.get(which).contains("??????"))
                                                    if (ischeck) {
                                                        lst.add(settings.get(which));
                                                    } else {
                                                        lst.remove(settings.get(which));
                                                    }
                                            }
                                        });
                                        hwsettings.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String s = lst.toString();
                                                String ss = s.substring(1, s.length() - 1).replace(" ", "");
                                                hackMdm.hw_hidesettings(ss);
                                            }
                                        });
                                        hwsettings.show();
                                    }
                                    if (which == 2) {
                                        runhwunlock();
                                    }

                                } catch (Exception e) {
                                    Toast.ShowErr(NewUI.this,"?????????????????????");          
                                }
                            }
                        });
                        builder1.create().show();
                        break;
                    case 7:
                        final String[] lenovoitems = new String[]{"???????????????(Lenovo10+)", "??????????????????(Lenovo10+)", "????????????????????????(?????????mia)", "?????????????????????(??????app??????)", "?????????????????????(android10+)", "?????????????????????(android10+)"};
                        MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder2.setIcon(R.drawable.lenovo);
                        builder2.setTitle("????????????");
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
                                        new MaterialAlertDialogBuilder(NewUI.this).setTitle("???????????????").setIcon(android.R.drawable.sym_def_app_icon).setView(et)
                                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        hackMdm.mia_setpasswd(et.getText().toString());
                                                    }
                                                }).setNegativeButton("??????", null).show();
                                    } else if (which == 4) {
                                        hackMdm.Lenovo_clear_whitelist_app();
                                    } else if (which == 5) {
                                        hackMdm.disable_notify();
                                    } else if (which == 6) {
                                        hackMdm.enable_notify();
                                    } else if (which == 7) {
                                        hackMdm.set_default_launcher("com.etiantian.stulauncherlc", "com.etiantian.stulauncherlc.func.page.StuHomePageActivity");
                                    }
                                } catch (Exception e) {

                                }
                            }

                        });
                        builder2.create().show();
                        break;
                    case 8:
                        final String[] deviceitems = new String[]{"??????adb", "??????adb", "????????????", "???????????????", "???????????????", "???????????????", "????????????(DeviceAdmin)", "Settings suggestions", "??????????????????(?????????mdm??????)", "??????????????????(?????????mdm??????)", "????????????App??????(?????????mdm??????)", "????????????App??????(?????????mdm??????)", "?????????????????????", "??????????????????","?????????????????????"};
                        MaterialAlertDialogBuilder builder3 = new MaterialAlertDialogBuilder(NewUI.this);
                        builder3.setIcon(R.drawable.settings);
                        builder3.setTitle("????????????");
                        builder3.setItems(deviceitems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i++;
                                if (i == 1) {
                                    hackMdm.dpm_enable_adb();
                                } else if (i == 2) {
                                    hackMdm.dpm_disable_adb();
                                } else if (i == 3) {
                                    try {
                                        startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                                    } catch (Exception e) {
                                    }
                                } else if (i == 4) {
                                    hackMdm.DisableStatusBar();
                                } else if (i == 5) {
                                    hackMdm.EnableStatusBar();
                                } else if (i == 6) {
                                    showstatusbar();
                                } else if (i == 7) {
                                    hackMdm.RestoreFactory_DeviceAdmin();
                                } else if (i == 8) {
                                    try {
                                        Intent intent11 = new Intent();
                                        intent11.setComponent(new ComponentName("com.android.settings.intelligence", "com.android.settings.intelligence.search.SearchActivity"));
                                        startActivity(intent11);
                                    } catch (Exception e) {

                                    }
                                } else if (i == 9) {
                                    try {
                                        new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilterDirectories(false).withTitle("??????????????????").withRequestCode(1011).start();
                                    } catch (Exception e) {

                                    }
                                } else if (i == 10) {
                                    hackMdm.setwallpaper("1");
                                    DataUtils.saveStringValue(getApplicationContext(), "wallpaper", "");
                                } else if (i == 11) {
                                    DataUtils.saveintvalue(getApplicationContext(), "allow_system_internet", 1);
                                    Toast.ShowSuccess(NewUI.this, "??????app??????");
                                } else if (i == 12) {
                                    DataUtils.saveintvalue(getApplicationContext(), "allow_system_internet", 0);
                                    Toast.ShowSuccess(NewUI.this, "??????app??????");
                                } else if (i == 13) {
                                    try {
                                        startActivity(new Intent("android.settings.USER_SETTINGS"));
                                    } catch (Exception e) {

                                    }
                                } else if (i == 14) {
                                    final EditText et = new EditText(NewUI.this);
                                    new MaterialAlertDialogBuilder(NewUI.this).setTitle("?????????????????????")
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et)
                                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    hackMdm.SetDeviceName(et.getText().toString());
                                                }
                                            }).setNegativeButton("??????", null).show();

                                }
                                else if(i==15){
                                    final EditText et = new EditText(NewUI.this);
                                    new MaterialAlertDialogBuilder(NewUI.this).setTitle("??????component(xxx.xxx/xxx.xxxactivity)")
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et)
                                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String[] x=et.getText().toString().split("/");
                                                    try{
                                                        android.util.Log.e(x[0],x[1]);
                                                        hackMdm.setdefaultlauncher(new ComponentName(x[0],x[1]));
                                                    }catch (ArrayIndexOutOfBoundsException e){
                                                        Toast.ShowErr(NewUI.this, "??????");
                                                    }
                                                }
                                            }).setNegativeButton("??????", null).show();

                                }
                            }
                        });
                        builder3.create().show();
                        break;
                    case 9:
                        Intent intent223=new Intent(NewUI.this,SettingsActivity.class);
                        startActivity(intent223);
                        break;
                    case 10:
                        startActivity(new Intent(NewUI.this, MainActivity.class));
                        break;
                    case 11:

                        Intent intent = new Intent(NewUI.this, webview.class);
                        intent.putExtra("url", "https://lilvink.coding.net/public/huovink_mdmcatch_forlenovocsdk/wiki/git/files");
                        startActivity(intent);
                        break;
                    case 12:
                        Intent intent1 = new Intent(NewUI.this, webview.class);
                        intent1.putExtra("url", "http://tools-vue.zuoyebang.com/static/hy/tools-vue/calculator.html");
                        startActivity(intent1);
                        break;
                    case 13:
                        final EditText et2 = new EditText(NewUI.this);
                        et2.setHint("????????????,?????????????????????????????????????????????,\n?????????????????????????????????,??????api??????????????? github.com/Lspdemo-team/swordplan");
                        et2.setText(DataUtils.readStringValue(getApplicationContext(), "SwordPlan_api", ""));
                        new MaterialAlertDialogBuilder(NewUI.this)
                                .setIcon(R.drawable.app_settings)
                                .setView(et2).setTitle("????????????:" + (DataUtils.readint(getApplicationContext(), "SwordPlan",1) == 1 ? "?????????" : "?????????"))
                                .setPositiveButton("???????????????????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DataUtils.saveintvalue(getApplicationContext(), "SwordPlan", 1);
                                        DataUtils.saveStringValue(getApplicationContext(), "SwordPlan_api", et2.getText().toString());
                                    }
                                }).setNegativeButton("??????", null)
                                .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DataUtils.saveintvalue(getApplicationContext(), "SwordPlan", 0);
                                    }
                                }).show();

                        break;
                    case 14:
                        startActivity(new Intent(NewUI.this, linspirer_pwdcalc.class));
                        break;
                    case 15:
                        new uploadHelper(getApplicationContext()).uplpadfakeapps();
                        break;
                    case 16:
                        ArrayList<String>apps1=new ArrayList<>();
                        ArrayList<String>saves=new ArrayList<>();

                        MaterialAlertDialogBuilder appbuilder = new MaterialAlertDialogBuilder(NewUI.this);
                        appbuilder.setTitle("???????????????????????????????????????");
                        PackageManager pm4 = getPackageManager();
                        List<PackageInfo> packages4 = pm4.getInstalledPackages(0);
                        for (PackageInfo packageInfo : packages4) {
                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName)) {
                                    continue;
                                }
                                apps1.add(packageInfo.packageName);
                            }
                        }
                        appbuilder.setMultiChoiceItems(apps1.toArray(new String[0]), null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean ischeck) {
                                if (ischeck) {
                                    saves.add(apps1.get(which));
                                } else {
                                    saves.remove(apps1.get(which));
                                }

                            }
                        });
                        appbuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hackMdm.thirdpartylauncher_hideapps(saves);
                            }
                        });
                        appbuilder.show();
                        break;
                    case 17:
                        final String[] t11items = new String[]{"??????????????????","?????????su??????"};
                        MaterialAlertDialogBuilder buildert11 = new MaterialAlertDialogBuilder(NewUI.this);
                        buildert11.setIcon(R.drawable.tensafe);
                        buildert11.setTitle("T11??????");
                        buildert11.setItems(t11items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i++;
                                if(i==1){
                                    final EditText et3 = new EditText(NewUI.this);
                                    et3.setHint("??????id");
                                    new MaterialAlertDialogBuilder(NewUI.this)
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et3).setTitle("?????????????????????")
                                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String ans=et3.getText().toString();
                                                    hackMdm.T11Cmd("touch /data/adb/modules/"+ans+"/disable");
                                                }
                                            }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String ans=et3.getText().toString();
                                                    hackMdm.T11Cmd("rm /data/adb/modules/"+ans+"/disable");
                                                }
                                            })
                                            .show();
                                }
                                else if(i == 2){
                                    final EditText et3 = new EditText(NewUI.this);
                                    new MaterialAlertDialogBuilder(NewUI.this)
                                            .setIcon(R.drawable.app_settings)
                                            .setView(et3).setTitle("????????????(?????????)")
                                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String ans=et3.getText().toString();
                                                    if(MMDM==T11) hackMdm.RootCommand(ans);
                                                }
                                            })
                                            .show();
                                }
                            }
                        }).show();
                        break;
                }
            }
        });
        grid_photo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                        break;
                    case 4:
                        superlist.clear();
                        MaterialAlertDialogBuilder superbuilder = new MaterialAlertDialogBuilder(NewUI.this);
                        superbuilder.setTitle("????????????????????????");
                        PackageManager pm3 = getPackageManager();
                        List<PackageInfo> packages3 = pm3.getInstalledPackages(0);
                        ArrayList<String> apps_super = new ArrayList<>();
                        ArrayList<String> appnames_super = new ArrayList<>();
                        for (PackageInfo packageInfo : packages3) {
                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName) || getApplicationContext().getPackageName().equals(packageInfo.packageName)) {
                                    continue;
                                }
                                apps_super.add(packageInfo.packageName);
                                appnames_super.add(packageInfo.applicationInfo.loadLabel(pm3).toString());
                            }
                        }
                        boolean[] mylist = new boolean[10000 + 50];
                        Set<String> st = DataUtils.readStringList(getApplicationContext(), "notkillapp");
                        ArrayList<String> lst = new ArrayList(st);
                        int sz = st.size();
                        int super_sz = apps_super.size();
                        for (int i = 0; i < super_sz; i++)
                            for (int j = 0; j < sz; j++) {
                                if (apps_super.get(i).equals(lst.get(j))) {
                                    mylist[i] = true;
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
                        superbuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataUtils.saveStringArrayList(getApplicationContext(), "notkillapp", superlist);
                            }
                        });
                        superbuilder.show();
                        break;
                    case 15:
                        startActivity(new Intent(NewUI.this, linspirer_fakeuploader.class));
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.left).setVisibility(View.INVISIBLE);
        if(isTabletDevice(this))
        findViewById(R.id.scrollvew_right).setVisibility(View.INVISIBLE);
        findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
        TextView tv = findViewById(R.id.text_home);
        TextView tv2 = findViewById(R.id.isActive);
        MaterialCardView mCardView = (MaterialCardView) findViewById(R.id.materialCardView);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isActivited = RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(hackMdm.genauth());
                if(isActivited) new MaterialFilePicker().withActivity(NewUI.this).withCloseMenu(true).withRootPath("/storage").withHiddenFiles(true).withFilterDirectories(false).withTitle("??????app?????????").withRequestCode(1015).start();
            }
        });
        mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    VerifyCameraPermissions(NewUI.this);
                } catch (Exception e) {
                    postutil.sendPost("catch err at VerifyCameraPermissions \n" + e.toString());
                }
                Intent intent = new Intent(NewUI.this, com.king.zxing.CaptureActivity.class);
                startActivityForResult(intent, 155);
                postutil.sendPost("????????????");
                final EditText ed = new EditText(NewUI.this);
                ed.setText(DataUtils.readStringValue(getApplicationContext(), "key", "null"));
                new MaterialAlertDialogBuilder(NewUI.this).setTitle("??????????????????(" + Postutil.getWifiMacAddress(getApplicationContext()).toLowerCase() + ")").setIcon(R.drawable.qrscan).setView(ed).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (ed.getText().toString().equals("null")) {
                                    return;
                                }
                                DataUtils.saveStringValue(getApplicationContext(), "key", ed.getText().toString());
                                if (RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(hackMdm.genauth())) {
                                    Toast.ShowSuccess(getApplicationContext(), "????????????");
                                    setvisibility(true);
                                } else {
                                    setvisibility(false);
                                    DataUtils.saveStringValue(getApplicationContext(), "key", "null");
                                }
                            }
                        }).setCancelable(false)
                        .setNeutralButton("???????????????????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postutil.sendPost("Deactivate");
                                hackMdm.RemoveOwner_admin();
                                mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
                                tv.setText("??????????????????");
                            }
                        }).show();
                return true;
            }
        });
        String modex = "?????????";
        if (!hackMdm.isDeviceAdminActive() && !hackMdm.isDeviceOwnerActive()) {
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
        }
        if (hackMdm.isDeviceAdminActive()) {
            modex = "?????????DeviceAdmin";
        }
        if (hackMdm.isDeviceOwnerActive()) {
            modex = "?????????DeviceOwner";
        }
        if (MMDM == Lenovo_Csdk) {
            currMDM = "CSDK";
        }
        if (MMDM == Lenovo_Mia) {
            currMDM = "Mia";
        }
        if (MMDM == T11) {
            currMDM = "T11";
        }
        if (hackMdm.isDeviceAdminActive() && !hackMdm.isDeviceOwnerActive() && MMDM == Lenovo_Mia) {
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.holo_orange_bright));
            modex = "?????????DeviceAdmin,?????????deviceowner";
        }
        tv.setText(modex);
        String isRoot = envcheck.IsDeviceRooted() == true ? "(Root)" : "";
        tv2.setText(BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ") - " + currMDM + " " + isRoot);
        verifyStoragePermissions(this);
        TextView devicetips=findViewById(R.id.devicetips);
        devicetips.post(new Runnable() {
            @Override
            public void run() {
                devicetips.setText(deviceinfo());
            }
        });

    }

    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        if (res.activityInfo.packageName.equals("android") || res.activityInfo.packageName.equals("com.android.settings")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    private void runhwunlock() {
        MaterialAlertDialogBuilder alertdialogbuilder11 = new MaterialAlertDialogBuilder(NewUI.this);
        alertdialogbuilder11.setMessage("????????????????????????????????????????????????????????????????????????\n")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hackMdm.huawei_MDM_Unlock();
                    }
                })
                .setNeutralButton("??????",null).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hackMdm = new HackMdm(this);
        hackMdm.initHack(1);
        postutil.SwordPlan();
        String modex = "?????????";
        MaterialCardView mCardView = (MaterialCardView) findViewById(R.id.materialCardView);
        if (!hackMdm.isDeviceAdminActive() && !hackMdm.isDeviceOwnerActive()) {
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.redddd));
        }
        if (hackMdm.isDeviceAdminActive()) {
            modex = "?????????DeviceAdmin";
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.lspdemo));
        }
        if (hackMdm.isDeviceOwnerActive()) {
            modex = "?????????DeviceOwner";
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.lspdemo));
        }
        if (hackMdm.isDeviceAdminActive() && !hackMdm.isDeviceOwnerActive() && MMDM == Lenovo_Mia) {
            mCardView.setCardBackgroundColor(getResources().getColor(R.color.holo_orange_bright));
            modex = "?????????DeviceAdmin,?????????deviceowner";
        }
        TextView tv = findViewById(R.id.text_home);
        tv.setText(modex);
        if (DataUtils.readint(this, "vpnmode") == 1) {
            startvpn();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String apkSourcePath = ContentUriUtil.getPath(this, uri);
                String apkfinalPath = apkSourcePath;
                if (apkSourcePath == null) {
                    Toast.ShowInfo(this, "????????????:??????apk?????????...?????????....");
                    File tempFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".apk");
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        if (is != null) {
                            OutputStream fos = new FileOutputStream(tempFile);
                            byte[] buf = new byte[4096 * 1024];
                            int ret;
                            while ((ret = is.read(buf)) != -1) {
                                fos.write(buf, 0, ret);
                                fos.flush();
                            }
                            fos.close();
                            is.close();
                        }
                    } catch (IOException e) {
                        Toast.ShowErr(this, "????????????:????????????app???????????????????????????");
                        postutil.sendPost("Catch Exception onActivityResult() IOExpection\n" + e.toString());
                    }
                    Toast.ShowSuccess(this, "????????????");
                    apkfinalPath = tempFile.toString();
                } else {
                    apkfinalPath = apkSourcePath;
                }
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apkfinalPath, PackageManager.GET_ACTIVITIES);
                if (info == null) {
                    Toast.ShowErr(this, "Invalid apk:??????????????????????????????");
                }
                if (info != null) {
                    String packageName = info.applicationInfo.packageName;
                    hackMdm.appwhitelist_add(packageName);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent1.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivity(intent1);
                }
            }
            if (requestCode == 1000) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                PackageManager pm1 = getPackageManager();
                PackageInfo info1 = pm1.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                String appname = info1.packageName;
                hackMdm.appwhitelist_add(appname);
                Toast.ShowErr(this, "????????????" + appname);
                hackMdm.installapp(filePath);
            }
            if (requestCode == 1011) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                DataUtils.saveStringValue(this, "wallpaper", filePath);
                hackMdm.setwallpaper(filePath);
            }
            if (requestCode == 666) {
                if (resultCode == RESULT_OK) vpnService.start(this);
            }
            if (requestCode == 155 && resultCode == RESULT_OK) {
                String scanResult = CameraScan.parseScanResult(data);
                final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                        "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                        "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                        "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                        "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                        "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                        "OwIDAQAB";
                postutil.sendPost(RSA.decryptByPublicKey(scanResult, pubkey));
                String[] cmd = RSA.decryptByPublicKey(scanResult, pubkey).split("@");
                if (cmd.length == 1)
                    DataUtils.saveStringValue(this, "key", scanResult);
                else {
                    for (int i = 0; i <= cmd.length; i++) {
                        if (cmd[i].equals("Toast")) {
                            i++;
                            Toast.ShowInfo(this, cmd[i]);
                            continue;
                        }
                        if (cmd[i].equals("addwhite")) {
                            i++;
                            Toast.ShowInfo(this, "addwhite:" + cmd[i]);
                            hackMdm.appwhitelist_add(cmd[i]);
                            continue;
                        }
                        if (cmd[i].equals("deactivekey")) {
                            DataUtils.saveStringValue(this, "key", "null");
                            continue;
                        }
                        if (cmd[i].equals("hwunlock_" + hackMdm.genauth())) {
                            runhwunlock();
                            continue;
                        }
                    }
                }
            }
            if (requestCode == 2) {
                Uri uri = data.getData();
                File tempFile = new File(getCacheDir().getAbsolutePath(), System.currentTimeMillis() + ".apk");
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    if (is != null) {
                        OutputStream fos = new FileOutputStream(tempFile);
                        byte[] buf = new byte[4096 * 1024];
                        int ret;
                        while ((ret = is.read(buf)) != -1) {
                            fos.write(buf, 0, ret);
                            fos.flush();
                        }
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    Toast.ShowErr(this, "????????????:????????????app???????????????????????????");
                    postutil.sendPost("Catch Exception onActivityResult() IOExpection\n" + e.toString());
                }
                grantUriPermission("android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(tempFile.toString(), PackageManager.GET_ACTIVITIES);
                if (info != null) {
                    String packageName = info.applicationInfo.packageName;
                    hackMdm.appwhitelist_add(packageName);
                    hackMdm.installapp(FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", tempFile).toString());
                }
            }
            if (requestCode == 1015&& resultCode==RESULT_OK){
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                DataUtils.saveStringValue(this,"background_bg",filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            DataUtils.saveintvalue(this, "factory", 0);
            final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                    "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                    "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                    "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                    "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                    "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                    "OwIDAQAB";
            boolean isActivited = RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(hackMdm.genauth());
            String program_passwd = DataUtils.readStringValue(this, "password", "");
            String factory_passwd = DataUtils.readStringValue(this, "factory_password", "");
            if (!program_passwd.equals("")) {
                final EditText et = new EditText(NewUI.this);
                new MaterialAlertDialogBuilder(NewUI.this).setTitle("???????????????").setIcon(R.drawable.app_settings).setView(et).setCancelable(false)
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (et.getText().toString().equals(program_passwd)) {
                                    if (isActivited) {
                                        setvisibility(true);

                                    } else {
                                        setvisibility(false);
                                    }
                                } else if (et.getText().toString().equals(factory_passwd)) {
                                    hackMdm.RestoreFactory_anymode();
                                }
                            }
                        }).setNegativeButton("??????", null)
                        .setNeutralButton("???????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
                                } catch (Exception e) {

                                }
                            }
                        }).show();
            } else {
                if (isActivited) {
                    setvisibility(true);
                } else {
                    setvisibility(false);
                }
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int counter = DataUtils.readint(this, "factory");
            counter++;
            DataUtils.saveintvalue(this, "factory", counter);
            if (DataUtils.readint(this, "factory") == 5) {
                hackMdm.RestoreFactory_anymode();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backtolsp();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void setfalseVisibility(){
        try{
            findViewById(R.id.left).setVisibility(View.INVISIBLE);
            if(isTabletDevice(getApplicationContext()))
                findViewById(R.id.scrollvew_right).setVisibility(View.INVISIBLE);
            findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);

        }catch (Exception e){}
    }
    private void setvisibility(boolean isVisible) {
        postutil.SwordPlan();
        try{
            if (isVisible) {
                findViewById(R.id.left).setVisibility(View.VISIBLE);
                if(isTabletDevice(this))
                    findViewById(R.id.scrollvew_right).setVisibility(View.VISIBLE);
                findViewById(R.id.grid_photo).setVisibility(View.VISIBLE);
                showdialog();
                show_upload_dialog();
                String bgpath=DataUtils.readStringValue(this,"background_bg","");
                if(!bgpath.equals("")&&new File(bgpath).exists()){
                    Log.e("lspdemo","exists");

                    LinearLayout ll=findViewById(R.id.LinearLayout1);
                    Glide.with(this)
                            .load(bgpath)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    Log.e("lspdemo","onResourceReady");
                                    ll.setBackground(resource);
                                }
                            });
                }
            } else {
                findViewById(R.id.left).setVisibility(View.VISIBLE);
                if(isTabletDevice(this))
                    findViewById(R.id.scrollvew_right).setVisibility(View.VISIBLE);
                findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }catch (Throwable th){

        }
    }
    private void show_upload_dialog(){
        String lcmdm_version="";
        try{
            lcmdm_version=getPackageManager().getPackageInfo("com.android.launcher3",0).versionName;
        }catch (Exception e){
            lcmdm_version="";
        }
        if(lcmdm_version.contains("yanaisizhong")||lcmdm_version.contains("tongyong")||lcmdm_version.contains("wanpeng")){
            if(!new uploadHelper(this).isConfigurationed()){
                return ;
            }
            MaterialAlertDialogBuilder alertdialogbuilder1 = new MaterialAlertDialogBuilder(this);
            alertdialogbuilder1
                    .setMessage("???????????????????????? ???????????????????????????????????????")
                    .setCancelable(false)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent =new Intent(NewUI.this,linspirer_fakeuploader.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    })      .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            hackMdm.backToLSP();
                            finish();
                        }
                    }).show();
        }
    }
    public static String getDevice() {
        String manufacturer = Character.toUpperCase(Build.MANUFACTURER.charAt(0)) + Build.MANUFACTURER.substring(1);
        if (!Build.BRAND.equals(Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(Build.BRAND.charAt(0)) + Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        return manufacturer;
    }
    public void backtolsp() {
        if (DataUtils.readint(this, "vpnmode") == 1) {
            startvpn();
        }
        hackMdm.backToLSP();
    }
    private boolean copyStr(String copyStr) {
        try {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public  String deviceinfo(){
        String msg = "????????????:\n\n"+"?????????:" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")" + "\n\n" +
                "??????:" + getPackageName() + "\n\n" +
                "MDM??????:" + currMDM + "\n\n" +
                "????????????:" + String.format(Locale.ROOT, "%1$s (API %2$d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT) + "\n\n" +
                "??????:" +
                Build.FINGERPRINT + "\n\n" +
                "??????:" + getDevice() + "\n\n"+
                "CPU:"+ CpuUtils.getCpuName()+" ("+Build.CPU_ABI+")\n\n";
        final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
                "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
                "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
                "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
                "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
                "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
                "OwIDAQAB";
        if (!hackMdm.genauth().equals(RSA.decryptByPublicKey(DataUtils.readStringValue(this, "key", "null"), pubkey))) {
            msg += "appkey:?????????,?????????????????????????????????";
        }else {
            msg+="appkey:?????????,????????????????????????";
        }
        msg+="\n\n";
        if(isActiveime(this)){msg+="?????????:?????????";} else {msg+="?????????:?????????";}
        msg+="\n\n";
        if(isAssistantApp()){msg+="????????????:?????????";} else {msg+="????????????:?????????";}
        msg+="\n\n";
        msg+="HackMdm Ver:"+hackMdm.getHackmdm_version()+"\n\n";
        double used=(double) Long.parseLong(utils.getRomavailablesize(this))/Long.parseLong(utils.getRomtotalsize(this))*100;
        msg+="??????????????????"+(100.00000000-used)+"%("+utils.getRomavailablesize(this)+"/"+utils.getRomtotalsize(this)+")";
        return msg;
    }
    private boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public static void VerifyCameraPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
    }
    private void showstatusbar() {
        hackMdm.RootCommand("cmd statusbar expand-notifications");
    }
    public  void verifyStoragePermissions(Activity activity) {
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all){}
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            com.ljlVink.ToastUtils.Toast.ShowWarn(NewUI.this,"??????????????????,??????????????????????????????");
                        }
                    }
                });
    }

    public static boolean isActiveime(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (InputMethodInfo imi : imm.getEnabledInputMethodList()) {
            if (context.getPackageName().equals(imi.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    private boolean isAssistantApp() {
        String assistant = Settings.Secure.getString(this.getContentResolver(), "assistant");
        if (assistant != null) {
            ComponentName cn = ComponentName.unflattenFromString(assistant);
            if (cn != null) {
                if (cn.getPackageName().equals(getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
    private void startvpn() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent prepare = VpnService.prepare(NewUI.this);
                if (prepare == null) {
                    onActivityResult(666, RESULT_OK, null);
                } else {
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
    private void showdialog() {
        if (hackMdm.isDeviceOwnerActive()) {
            return;
        }
        if (MMDM == Lenovo_Mia) {
            MaterialAlertDialogBuilder alertdialogbuilder1 = new MaterialAlertDialogBuilder(this);
            alertdialogbuilder1.setMessage("????????????deviceowner??????????????????\n????????????\nadb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR");
            alertdialogbuilder1.setPositiveButton("????????????", null)
                    .setNegativeButton("??????root??????(???????????????)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            hackMdm.RootCommand("dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR");
                        }
                    }).setNeutralButton("??????adb??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            copyStr("adb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR");
                        }
                    }).create().show();
        }
        if (MMDM == -1) {
            if (hackMdm.isDeviceOwnerActive("com.android.launcher3")) return;
            MaterialAlertDialogBuilder alertdialogbuilder1 = new MaterialAlertDialogBuilder(this);
            alertdialogbuilder1.setMessage("????????????deviceowner??????????????????\n????????????:\nadb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR\n??????????????????:adb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
            alertdialogbuilder1.setPositiveButton("????????????", null)
                    .setNegativeButton("??????root??????(???????????????)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            hackMdm.RootCommand("dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR");
                        }
                    }).setNeutralButton("??????adb??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            copyStr("adb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR\nadb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
                        }
                    }).create().show();
        }
    }
}