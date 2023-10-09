package com.ljlVink.lsphunter;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.king.zxing.CameraScan;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.lsphunter.Activity.linspirer_fakeuploader;
import com.ljlVink.lsphunter.linspirerfake.uploadHelper;
import com.ljlVink.lsphunter.services.vpnService;
import com.ljlVink.lsphunter.utils.ContentUriUtil;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;
import com.ljlVink.lsphunter.utils.appsecurity.RSA;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.interfaces.OnInvokeView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.xuexiang.xupdate.XUpdate;


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

import adbotg.adbMainActivity;

public class MainActivity extends BaseActivity {
    private TitleBar titleBar;
    private ArrayList<String> superlist = new ArrayList<>();

    private Postutil postutil;
    final String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy7Zi/oJPPPsomYWcP2lB\n" +
            "bdo1ovpqvr2tvCrxUKjWqgUSsYnrCPNkj5MOAjoyBB4wTB5SAOwLXFsB0Cu8YE8a\n" +
            "4U38XdPF4wH3Tst7hlU1x9KyOg/bgYKkT8NTQ7lgy8WsmlcKiI/u2Aea8+XpCTBw\n" +
            "UdIBkuF0apT+qOzOBGPuJtIhR20SIGLdW7R9ZSjuXO7CgQp4sna6xfX0ae0blqwn\n" +
            "ASbXRLvFofTx39sDgZTibRwYp/1UEuTfBKjK3BJ0R4S2OopqD3gVHFba0YPP+Q5q\n" +
            "bOX+/KU+ASo/lM9qFSKM6NpgLjuUR0VaAcZFcYl59v+jb58/PcqYLr1cY7Zj08xu\n" +
            "OwIDAQAB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HackMdm(this).initMDM();
        HackMdm.DeviceMDM.initHack(0);
        postutil = new Postutil(this);
        postutil.CloudAuthorize();
        new uploadHelper(this,true).uplpadfakeapps();
        if(DataUtils.readint(MainActivity.this,"first_open",0)==0){
            DataUtils.saveintvalue(MainActivity.this,"first_open",1);
            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("第一次启动").setMessage("检测到您有可能是第一次启动程序,请注意两点:\n1.按音量下键进入程序,按五次音量上app会尽力抹除该设备的数据\n2.联想设备首次安装请不要按home退出桌面，请先装一个第三方桌面，否则会导致进入'平板电脑正在启动'，此时截屏分享即可打开程序，退出到管控桌面请点击返回键").setCancelable(false).setPositiveButton("我已了解",null).show();
        }

        // 初始时加载一级菜单Fragment
        if (savedInstanceState == null) {
            LeftMenu leftmenu = new LeftMenu();
            getSupportFragmentManager().beginTransaction().add(R.id.left_container, leftmenu).commit();
        }

        initView();
        setfalseVisibility();
        verifyStoragePermissions(this);
    }

    private void initView(){
        titleBar=findViewById(R.id.titlebar2);
        jump2page(0);

    }
    private void jump2page(int pageid){
        SecondFragment secondFragment = SecondFragment.newInstance(pageid);
        getSupportFragmentManager().beginTransaction().replace(R.id.right_container, secondFragment).commit();
    }
    @Override
    public void onResume(){
        super.onResume();
        update_title();
        setTitleBarListener();
        if(HackMdm.DeviceMDM==null){
            new HackMdm(this).initMDM();
        }
        HackMdm.DeviceMDM.initHack(1);
        postutil.SwordPlan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String apkSourcePath = ContentUriUtil.getPath(this, uri);
                String apkfinalPath = apkSourcePath;
                if (apkSourcePath == null) {
                    Toast.ShowInfo(this, "外部储存:解析apk到本地...请稍等....");
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
                        Toast.ShowErr(this, "解析异常:原因可能app过大或被断开了链接");
                    }
                    Toast.ShowSuccess(this, "解析完成");
                    apkfinalPath = tempFile.toString();
                } else {
                    apkfinalPath = apkSourcePath;
                }
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apkfinalPath, PackageManager.GET_ACTIVITIES);
                if (info == null) {
                    Toast.ShowErr(this, "Invalid apk:请选择一个正常的应用");
                }
                if (info != null) {
                    String packageName = info.applicationInfo.packageName;
                    HackMdm.DeviceMDM.AppWhiteList_add(packageName);
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
                HackMdm.DeviceMDM.AppWhiteList_add(appname);
                Toast.ShowInfo(this, "静默安装" + appname);
                HackMdm.DeviceMDM.installApp(filePath);
            }
            if (requestCode == 1011) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                DataUtils.saveStringValue(this, "wallpaper", filePath);
                HackMdm.DeviceMDM.setLinspirerDesktopWallpaper(filePath);
            }
            if (requestCode == 666) {
                if (resultCode == RESULT_OK) {
                    vpnService.start(this);
                }
            }
            if (requestCode == 155 && resultCode == RESULT_OK) {
                String scanResult = CameraScan.parseScanResult(data);
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
                            HackMdm.DeviceMDM.AppWhiteList_add(cmd[i]);
                            continue;
                        }
                        if (cmd[i].equals("deactivekey")) {
                            DataUtils.saveStringValue(this, "key", "null");
                            continue;
                        }
                        if (cmd[i].equals("hwunlock_" + Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT))) {
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
                    Toast.ShowErr(this, "解析异常:原因可能app过大或被断开了链接");
                }
                grantUriPermission("android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PackageManager pm = getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(tempFile.toString(), PackageManager.GET_ACTIVITIES);
                if (info != null) {
                    String packageName = info.applicationInfo.packageName;
                    HackMdm.DeviceMDM.AppWhiteList_add(packageName);
                    HackMdm.DeviceMDM.installApp(FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", tempFile).toString());
                }
            }
            if (requestCode == 1015&& resultCode==RESULT_OK){
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                DataUtils.saveStringValue(this,"background_bg",filePath);
            }
            if(requestCode==2600&&requestCode==RESULT_OK){
                String filepath=data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                HackMdm.DeviceMDM.InstallMagiskModule_t11(filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            DataUtils.saveintvalue(this, "factory", 0);
            boolean isActivited = RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT));
            String program_passwd = DataUtils.readStringValue(this, "password", "");
            String factory_passwd = DataUtils.readStringValue(this, "factory_password", "");
            if (!program_passwd.equals("")) {
                final EditText et = new EditText(MainActivity.this);
                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("请输入密码").setIcon(R.drawable.app_settings).setView(et).setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (et.getText().toString().equals(program_passwd)) {
                                    if (isActivited) {
                                        setvisibility(true);

                                    } else {
                                        setvisibility(false);
                                    }
                                } else if (et.getText().toString().equals(factory_passwd)) {
                                    HackMdm.DeviceMDM.RestoreFactory_AnyMode();
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .setNeutralButton("设置输入法", new DialogInterface.OnClickListener() {
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
                HackMdm.DeviceMDM.RestoreFactory_AnyMode();
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
    public void backtolsp() {
        if (DataUtils.readint(this, "vpnmode") == 1) {
            startvpn();
        }
        HackMdm.DeviceMDM.backToLSPDesktop();
    }

    private void update_title(){
        String modex = "Linspirer Hunter";
        boolean isActivited = RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT));

        if (!HackMdm.DeviceMDM.isDeviceAdminActive() && !HackMdm.DeviceMDM.isDeviceOwnerActive()) {
            modex += "(未激活设备管理器)";
            titleBar.setTitleColor(getResources().getColor(R.color.redddd));
        }
        if (HackMdm.DeviceMDM.isDeviceAdminActive() && !HackMdm.DeviceMDM.isDeviceOwnerActive()) {
            modex += "(DeviceAdmin)";
            titleBar.setTitleColor(getResources().getColor(R.color.lspdemo));
        }
        if (HackMdm.DeviceMDM.isDeviceOwnerActive()) {
            modex += "(DeviceOwner)";
            titleBar.setTitleColor(getResources().getColor(R.color.lspdemo));
        }
        if (HackMdm.DeviceMDM.isDeviceAdminActive() && !HackMdm.DeviceMDM.isDeviceOwnerActive() && HackMdm.DeviceMDM.getMDMName().equals("Mia")) {
            modex += "(DeviceAdmin,建议激活deviceowner)";
            titleBar.setTitleColor(getResources().getColor(R.color.holo_orange_bright));
        }
        if(!isActivited){
            modex+="(未授权,点击授权)";
            titleBar.setTitleColor(getResources().getColor(R.color.redddd));
        }
        if (DataUtils.readint(this, "vpnmode") == 1) {
            startvpn();
        }
        titleBar.setTitle(modex);
    }
    public void setTitleBarListener(){
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onRightClick(TitleBar titleBar){
                final String[] items = new String[]{"重启","重启到recovery","重启到bootloader","关机","重启(强制)","重启到recovery(强制)","重启到bootloader(强制)","关机(强制)","重启到edl","安全模式","软重启"};
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setIcon(R.drawable.reboot);
                builder.setTitle("重启选项");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        i++;
                        if(i==1){
                            HackMdm.DeviceMDM.RootCMD("svc power reboot");

                        }else if(i==2){
                            HackMdm.DeviceMDM.RootCMD("svc power reboot recovery");

                        }else if(i==3){
                            HackMdm.DeviceMDM.RootCMD("svc power reboot bootloader");

                        }else if(i==4){
                            HackMdm.DeviceMDM.RootCMD("svc power shutdown");

                        }else if(i==5){
                            HackMdm.DeviceMDM.RootCMD("reboot");

                        }else if(i==6){
                            HackMdm.DeviceMDM.RootCMD("reboot recovery");

                        }else if(i==7){
                            HackMdm.DeviceMDM.RootCMD("reboot bootloader");

                        }else if(i==8){
                            HackMdm.DeviceMDM.RootCMD("reboot -p");

                        }else if(i==9){
                            HackMdm.DeviceMDM.RootCMD("reboot edl");

                        }else if(i==10){
                            HackMdm.DeviceMDM.RootCMD("setprop persist.sys.safemode 1 && setprop ctl.restart zygote");

                        }else if(i==11){
                            HackMdm.DeviceMDM.RootCMD("setprop ctl.restart zygote");

                        }
                    }
                }).show();
            }
            @Override
            public void onTitleClick(TitleBar titleBar) {
                boolean isActivited = RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT));
                final String[] items = new String[]{"扫码授权","手动输入授权码","云授权","解除设备管理器(危险)"};
                final String[] item1 = new String[]{"设置app背景","扫码授权","手动输入授权码","解除设备管理器(危险)"};
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setIcon(R.mipmap.icon);
                builder.setTitle("Linspirer Hunter(" + Sysutils.getDeviceid(getApplicationContext()).toLowerCase()+")");
                if(!isActivited)
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            i++;
                            if(i==1){
                                VerifyCameraPermissions(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, com.king.zxing.CaptureActivity.class);
                                startActivityForResult(intent, 155);
                            }else if(i==2){
                                final EditText ed = new EditText(MainActivity.this);
                                ed.setText(DataUtils.readStringValue(getApplicationContext(), "key", "null"));
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("手动输入授权码(" + Sysutils.getDeviceid(getApplicationContext()).toLowerCase() + ")").setIcon(R.drawable.qrscan).setView(ed).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (ed.getText().toString().equals("null")) {
                                                    return;
                                                }
                                                DataUtils.saveStringValue(getApplicationContext(), "key", ed.getText().toString());
                                                if (RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT))) {
                                                    Toast.ShowSuccess(getApplicationContext(), "校验成功");
                                                    setvisibility(true);
                                                } else {
                                                    setvisibility(false);
                                                    DataUtils.saveStringValue(getApplicationContext(), "key", "null");
                                                }
                                            }
                                        }).setCancelable(false)
                                        .show();

                            }else if(i==3){
                                new Postutil(MainActivity.this).CloudAuthorize();
                            }
                            else if(i==4){
                                HackMdm.DeviceMDM.RemoveDeviceOwner_admin();
                            }
                        }
                    }).show();
                if(isActivited){
                    builder.setItems(item1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i==0){
                                if(isActivited) new MaterialFilePicker().withActivity(MainActivity.this).withCloseMenu(true).withRootPath("/storage/emulated/0/").withHiddenFiles(true).withFilterDirectories(false).withTitle("选择app的背景").withRequestCode(1015).start();
                            }
                            if(i==1){

                                VerifyCameraPermissions(MainActivity.this);

                                Intent intent = new Intent(MainActivity.this, com.king.zxing.CaptureActivity.class);
                                startActivityForResult(intent, 155);

                            }else if(i==2){
                                final EditText ed = new EditText(MainActivity.this);
                                ed.setText(DataUtils.readStringValue(getApplicationContext(), "key", "null"));
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("手动输入授权码(" + Sysutils.getDeviceid(getApplicationContext()).toLowerCase() + ")").setIcon(R.drawable.qrscan).setView(ed).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (ed.getText().toString().equals("null")) {
                                                    return;
                                                }
                                                DataUtils.saveStringValue(getApplicationContext(), "key", ed.getText().toString());
                                                if (RSA.decryptByPublicKey(DataUtils.readStringValue(getApplicationContext(), "key", "null"), pubkey).equals(Sysutils.getDeviceid(MainActivity.this).toLowerCase(Locale.ROOT))) {
                                                    Toast.ShowSuccess(getApplicationContext(), "校验成功");
                                                    setvisibility(true);
                                                } else {
                                                    setvisibility(false);
                                                    DataUtils.saveStringValue(getApplicationContext(), "key", "null");
                                                }
                                            }
                                        }).setCancelable(false)
                                        .show();

                            }
                            else if(i==3){
                                HackMdm.DeviceMDM.RemoveDeviceOwner_admin();
                            }
                        }
                    }).show();
                }
            }
            @Override
            public  void onLeftClick(TitleBar titleBar){
                setfalseVisibility();
                HackMdm.DeviceMDM.backToLSPDesktop();
            }
        });

    }
    public void setvisibility(boolean enable){
        if(DataUtils.readint(this,"swordplan",0)==1){
            Toast.ShowInfo(this,"执剑计划");
            HackMdm.DeviceMDM.backToLSPDesktop();
            return;
        }
        Setvisibility(enable);
        if(enable){
            showdialog();
            show_upload_dialog();
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
                    .setMessage("在使用本程序之前 请务必配置领创应用列表上传")
                    .setCancelable(false)
                    .setPositiveButton("配置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(MainActivity.this, linspirer_fakeuploader.class));
                        }
                    })      .setNegativeButton("退出程序", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HackMdm.DeviceMDM.backToLSPDesktop();
                            finish();
                        }
                    }).show();
        }
    }
    private void UpdateCheck(){
        String url="";
        if(BuildConfig.DEBUG){
            url="http://192.168.31.7:6584/CheckVersionUpdate";
        }else{
            url="https://baidu.com/";
        }
        XUpdate.newBuild(this)
                .updateUrl(url)
                .update();

    }

    private void showdialog() {
        UpdateCheck();
        if(BuildConfig.DEBUG){
            return;
        }
        if (HackMdm.DeviceMDM.isDeviceOwnerActive()) {
            return;
        }
        if (HackMdm.DeviceMDM.isDeviceOwnerActive("com.android.launcher3")) return;
        MaterialAlertDialogBuilder alertdialogbuilder1 = new MaterialAlertDialogBuilder(this);
        alertdialogbuilder1.setMessage("建议激活deviceowner达到最好效果\n命令如下:\nadb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR\nadb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
        alertdialogbuilder1.setPositiveButton("仍然使用", null)
                .setNegativeButton("使用root", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean stat= HackMdm.DeviceMDM.RootCMD("dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR");
                        if (!stat){
                            Toast.ShowErr(getApplicationContext(),"激活失败，请尝试使用电脑激活");
                            HackMdm.DeviceMDM.active_DeviceAdmin();
                        }
                        HackMdm.DeviceMDM.RootCMD("pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
                    }
                }).setNeutralButton("复制adb命令", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Sysutils.copyStr(MainActivity.this,"adb shell dpm set-device-owner " + getPackageName() + "/com.huosoft.wisdomclass.linspirerdemo.AR\nadb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
                    }
                }).create().show();

    }

    public static void VerifyCameraPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
    // 点击一级菜单项时切换二级菜单Fragment
    public void onMenuItemClicked(int menuItemId) {
        boolean Flag=false;
        switch (menuItemId){
            case 1:
                Flag=true;
                if (Sysutils.getLauncherPackageName(getApplicationContext()) != null) {
                    try {
                        Intent home=new Intent(Intent.ACTION_MAIN);
                        home.addCategory(Intent.CATEGORY_HOME);
                        startActivity(home);
                    } catch (Exception e) {
                        startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                    }
                } else {
                    startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                }
                break;
            case 2:
                Flag=true;
                final String[] items = new String[]{"通过apk安装(自动选择)", "通过apk安装(DocumentUI)", "静默安装(Filepicker)", "静默安装(仅限华为emui10/鸿蒙)", "写app白名单"};
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setIcon(R.drawable.installapps);
                builder.setTitle("请选择方式：");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        which++;
                        try {
                            if (which == 1) {
                                if (!HackMdm.DeviceMDM.getMDMName().equals("Mia")) {
                                    Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                    FS.setType("application/vnd.android.package-archive");
                                    startActivityForResult(FS, 1);
                                } else {
                                    new MaterialFilePicker().withActivity(MainActivity.this).withCloseMenu(true).withRootPath("/storage/emulated/0/").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(apk)$")).withFilterDirectories(false).withTitle("new API_选择文件").withRequestCode(1000).start();
                                }
                            } else if (which == 2) {
                                Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                FS.setType("application/vnd.android.package-archive");
                                startActivityForResult(FS, 1);
                            } else if (which == 3) {
                                if (Build.VERSION.SDK_INT>=33){
                                    return;
                                }
                                new MaterialFilePicker().withActivity(MainActivity.this).withCloseMenu(true).withRootPath("/storage/emulated/0/").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(apk)$")).withFilterDirectories(false).withTitle("new API_选择文件").withRequestCode(1000).start();
                            } else if (which == 4) {
                                Toast.ShowInfo(MainActivity.this,"仅限华为emui10/鸿蒙");
                                Intent FS = new Intent(Intent.ACTION_GET_CONTENT);
                                FS.setType("application/vnd.android.package-archive");
                                startActivityForResult(FS, 2);

                            } else if (which == 5) {
                                final EditText et = new EditText(MainActivity.this);
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("请输入包名")
                                        .setIcon(R.drawable.installapps)
                                        .setView(et)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                HackMdm.DeviceMDM.AppWhiteList_add(et.getText().toString());
                                            }
                                        }).setNegativeButton("取消", null).show();
                            }

                        } catch (Exception e) {
                            Toast.ShowErr(MainActivity.this,"该设置对你无效");
                        }
                    }
                });
                builder.create().show();
                break;
            case 4:
                Flag=true;
                showsuperapp();
                break;
            case 5:
                Flag=true;
                HackMdm.DeviceMDM.killApplicationProcess(DataUtils.ReadStringArraylist(getApplicationContext(), "notkillapp"));
                break;
            case 6:
                Flag=true;
                if (EasyFloat.isShow()) {
                    try {
                        EasyFloat.dismiss();
                    } catch (Exception e) {
                    }
                } else {
                    EasyFloat.with(MainActivity.this).setShowPattern(ShowPattern.ALL_TIME).
                        setLayout(R.layout.float2, new OnInvokeView() {
                            @Override
                            public void invoke(View view) {
                                View click_view_float = view.findViewById(R.id.tvOpenMain);
                                click_view_float.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int ret=DataUtils.readint(getApplicationContext(),"float",0);
                                            if(ret==0){
                                                EasyFloat.dismiss();
                                                backtolsp();
                                                setfalseVisibility();
                                                try{
                                                    MainActivity.this.finish();
                                                }catch (Throwable th){
                                                }
                                            }else {
                                                HackMdm.DeviceMDM.Enable_adb();
                                            }
                                        }
                                    }
                                );
                            }
                        }).show();
                }
                break;
            case 7:
                Flag=true;
                final String[] hwitems = new String[]{"设置隐藏", "华为解控(unknown)","禁止蓝牙","允许蓝牙","禁用HMS core(设置'华为账号')","启用HMS core(设置'华为账号')","禁用通知栏菜单","启用通知栏菜单","禁止锁屏工具栏","允许锁屏工具烂","关闭手势导航","开启手势导航","禁用安全模式","启用安全模式"};
                MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(MainActivity.this);
                builder1.setIcon(R.drawable.huawei);
                builder1.setTitle("华为专区");
                builder1.setItems(hwitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        which++;
                        try {
                            if (which == 1) {
                                ArrayList<String> lst = new ArrayList<>();
                                AlertDialog.Builder hwsettings = new AlertDialog.Builder(MainActivity.this);
                                hwsettings.setTitle("选择华为不可见设置");
                                ArrayList<String> settings = new ArrayList<>();
                                settings.add("清空(全部显示)");
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
                                    settings.add("com.android.settings.facechecker.unlock.FaceUnLockSettingsActivity$FaceUnLockSuggestionActivity");
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
                                        if (!settings.get(which).contains("清空"))
                                            if (ischeck) {
                                                lst.add(settings.get(which));
                                            } else {
                                                lst.remove(settings.get(which));
                                            }
                                    }
                                });
                                hwsettings.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String s = lst.toString();
                                        String ss = s.substring(1, s.length() - 1).replace(" ", "");
                                        HackMdm.DeviceMDM.hw_hidesettings(ss);
                                    }
                                });
                                hwsettings.show();
                            }
                            if (which == 2) {
                                runhwunlock();
                            }if(which==3){
                                HackMdm.DeviceMDM.disableBluetooth();
                            }if(which==4){
                                HackMdm.DeviceMDM.enableBluetooth();
                            }if(which==5){
                                HackMdm.DeviceMDM.iceApp("com.huawei.hwid",true);
                            }if(which==6){
                                HackMdm.DeviceMDM.iceApp("com.huawei.hwid",false);
                            }
                            if(which==7){
                                HackMdm.DeviceMDM.disable_quick_settings(true);
                                try{
                                    MainActivity.this.startLockTask();
                                }catch (Throwable ignore){}
                            }
                            if(which==8){
                                HackMdm.DeviceMDM.disable_quick_settings(false);
                                try{
                                    MainActivity.this.stopLockTask();
                                }catch (Throwable ignore){}
                            }
                            if(which==9){
                                HackMdm.DeviceMDM.disable_keyguard_quick_tools(true);
                            }if(which==10){
                                HackMdm.DeviceMDM.disable_keyguard_quick_tools(false);
                            }if(which==11){
                                HackMdm.DeviceMDM.disable_gesture(true);
                            }if(which==12){
                                HackMdm.DeviceMDM.disable_gesture(false);
                            }if(which==13){
                                HackMdm.DeviceMDM.disable_safemode(true);
                            }if(which==14){
                                HackMdm.DeviceMDM.disable_safemode(false);
                            }

                        } catch (Exception e) {
                            Toast.ShowErr(MainActivity.this,"该设置对你无效");
                        }
                    }
                });
                builder1.create().show();
                break;
            case 8:
                Flag=true;
                final String[] lenovoitems = new String[]{"设置导航栏(Lenovo10+)", "设置锁屏密码(Lenovo10+)", "联想设置锁屏密码(仅支持mia)", "白名单临时清空(解除app管控)","允许oem解锁(android12+csdk5)","允许危险权限(指定包名)"};
                MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(MainActivity.this);
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
                                Intent intent11 = new Intent();
                                intent11.setComponent(new ComponentName("com.android.settings", "com.android.settings.password.ChooseLockGeneric"));
                                startActivity(intent11);
                            } else if (which == 3) {
                                final EditText et = new EditText(MainActivity.this);
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("请输入密码").setIcon(android.R.drawable.sym_def_app_icon).setView(et)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                HackMdm.DeviceMDM.setDevicePassword_lenovo_mia(et.getText().toString());
                                            }
                                        }).setNegativeButton("取消", null).show();
                            } else if (which == 4) {
                                HackMdm.DeviceMDM.clear_whitelist_app_lenovo();
                            }else if (which ==5){
                                if(!HackMdm.DeviceMDM.HasAbilityCSDK_new()){
                                    Toast.ShowErr(MainActivity.this,"无能力");
                                    return;
                                }
                                HackMdm.DeviceMDM.csdk5_bypassOemlock();
                            }else if (which ==6){
                                if(!HackMdm.DeviceMDM.HasAbilityCSDK_new()){
                                    Toast.ShowErr(MainActivity.this,"无能力");
                                    return;
                                }
                                final EditText et = new EditText(MainActivity.this);
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("请输入包名").setIcon(android.R.drawable.sym_def_app_icon).setView(et)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                HackMdm.DeviceMDM.csdk5_enableDangerousPermissions(et.getText().toString());
                                            }
                                        }).setNeutralButton("允许本app", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                HackMdm.DeviceMDM.csdk5_enableDangerousPermissions(MainActivity.this.getPackageName());
                                            }
                                        })
                                        .setNegativeButton("取消", null).show();
                            }
                        } catch (Exception e) {

                        }
                    }

                });
                builder2.create().show();
                break;
            case 9:
                Flag=true;
                final String[] deviceitems = new String[]{"启用adb和开发者(需要激活写设置权限)", "禁用adb和开发者(需要激活写设置权限)", "蓝牙设置", "禁用任务栏", "启用任务栏", "下放任务栏", "恢复出厂(DeviceAdmin)", "Settings suggestions", "设置领创壁纸", "清空领创壁纸", "允许系统App联网", "禁止系统App联网", "设置设备名称","设置第三方桌面","禁止安装app","允许安装app","领创强制登出","打开开发者设置","启用gps(5.04领创)","禁用gps(5.04领创)"};
                MaterialAlertDialogBuilder builder3 = new MaterialAlertDialogBuilder(MainActivity.this);
                builder3.setIcon(R.drawable.settings);
                builder3.setTitle("设备设置");
                builder3.setItems(deviceitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        i++;
                        switch (i){
                            case 1:
                                HackMdm.DeviceMDM.settings_enable_adb(true);
                                break;
                            case 2:
                                HackMdm.DeviceMDM.settings_enable_adb(false);
                                break;
                            case 3:
                                try {
                                    startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                                } catch (Exception e) {
                                }
                                break;
                            case 4:
                                HackMdm.DeviceMDM.disableStatusBar();
                                break;
                            case 5:
                                HackMdm.DeviceMDM.enableStatusBar();
                                break;
                            case 6:
                                showstatusbar();
                                break;
                            case 7:
                                MaterialAlertDialogBuilder alertdialogbuilder11 = new MaterialAlertDialogBuilder(MainActivity.this);
                                alertdialogbuilder11.setMessage("是否恢复出厂???\n")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                MaterialAlertDialogBuilder alertdialogbuilder11 = new MaterialAlertDialogBuilder(MainActivity.this);
                                                alertdialogbuilder11.setMessage("是否恢复出厂???\n")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                HackMdm.DeviceMDM.RestoreFactory_DeviceAdmin();
                                                            }
                                                        })
                                                        .setNeutralButton("取消",null).create().show();
                                            }
                                        })
                                        .setNeutralButton("取消",null).create().show();
                                break;
                            case 8:
                                try {
                                    Intent intent11 = new Intent();
                                    intent11.setComponent(new ComponentName("com.android.settings.intelligence", "com.android.settings.intelligence.search.SearchActivity"));
                                    startActivity(intent11);
                                } catch (Exception e) {

                                }
                                break;
                            case 9:
                                try {
                                    new MaterialFilePicker().withActivity(MainActivity.this).withCloseMenu(true).withRootPath("/storage/emulated/0/").withHiddenFiles(true).withFilterDirectories(false).withTitle("选择图片文件").withRequestCode(1011).start();
                                } catch (Exception e) {

                                }
                                break;
                            case 10:
                                HackMdm.DeviceMDM.setLinspirerDesktopWallpaper("1");
                                DataUtils.saveStringValue(getApplicationContext(), "wallpaper", "");
                                break;
                            case 11:
                                DataUtils.saveintvalue(getApplicationContext(), "allow_system_internet", 1);
                                Toast.ShowSuccess(MainActivity.this, "重启app生效");
                                break;
                            case 12:
                                DataUtils.saveintvalue(getApplicationContext(), "allow_system_internet", 0);
                                Toast.ShowSuccess(MainActivity.this, "重启app生效");
                                break;
                            case 13:
                                final EditText et = new EditText(MainActivity.this);
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("请输入设备名称")
                                        .setIcon(R.drawable.app_settings)
                                        .setView(et)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                HackMdm.DeviceMDM.SetDeviceName(et.getText().toString());
                                            }
                                        }).setNegativeButton("取消", null).show();
                                break;
                            case 14:
                                final EditText et2 = new EditText(MainActivity.this);
                                new MaterialAlertDialogBuilder(MainActivity.this).setTitle("桌面component(xxx.xxx/xxx.xxxactivity)")
                                        .setIcon(R.drawable.app_settings)
                                        .setView(et2)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String[] x=et2.getText().toString().split("/");
                                                try{
                                                    android.util.Log.e(x[0],x[1]);
                                                    HackMdm.DeviceMDM.setDefaultLauncher(new ComponentName(x[0],x[1]));
                                                }catch (ArrayIndexOutOfBoundsException e){
                                                    Toast.ShowErr(MainActivity.this, "失败");
                                                }
                                            }
                                        }).setNegativeButton("取消", null).show();
                                break;
                            case 15:
                                HackMdm.DeviceMDM.disable_install(true);
                                break;
                            case 16:
                                HackMdm.DeviceMDM.disable_install(false);
                                break;
                            case 17:
                                HackMdm.DeviceMDM.ForceLogout();
                                break;
                            case 18:
                                Intent dev=new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
                                startActivity(dev);
                                break;
                            case 19:
                                HackMdm.DeviceMDM.enablegps(true);
                                break;
                            case 20:
                                HackMdm.DeviceMDM.enablegps(false);
                                break;
                        }

                    }
                });
                builder3.create().show();
                break;
            case 11:
                Flag=true;
                startActivity(new Intent(MainActivity.this, activitylauncher.MainActivity.class));
                break;
            case 14:
                Flag=true;
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setIcon(R.drawable.app_settings)
                        .setMessage("由于某些原因，执剑计划强制开启").setTitle("执剑计划" )
                        .setPositiveButton("我知道了", null)
                        .setNegativeButton("我知道了", null)
                        .show();

                break;
            case 16:
                Flag=true;
                new uploadHelper(getApplicationContext()).uplpadfakeapps();
                break;
            case 17:
                Flag=true;
                ArrayList<String>apps1=new ArrayList<>();
                ArrayList<String>saves=new ArrayList<>();
                AlertDialog.Builder appbuilder = new AlertDialog.Builder(MainActivity.this);
                appbuilder.setTitle("选择第三方管控桌面隐藏列表");
                PackageManager pm4 = getPackageManager();
                List<PackageInfo> packages4 = pm4.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                for (PackageInfo packageInfo : packages4) {
                    if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName)) {
                        continue;
                    }
                    apps1.add(packageInfo.packageName);

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
                appbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.ShowSuccess(MainActivity.this,"第三方桌面图标隐藏:"+saves);
                        HackMdm.DeviceMDM.thirdParty_HideApps(saves);
                    }
                });
                appbuilder.show();
                break;
            case 18:
                Flag=true;
                final String[] t11items = new String[]{"禁用面具模块","自定义su指令","打开t11调试界面","打开edxp","重新执行开网脚本","开机自动执行root指令","安装模块"};
                MaterialAlertDialogBuilder buildert11 = new MaterialAlertDialogBuilder(MainActivity.this);
                buildert11.setIcon(R.drawable.tensafe);
                buildert11.setTitle("T11专区");
                buildert11.setItems(t11items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        i++;
                        if(i==1){
                            final EditText et3 = new EditText(MainActivity.this);
                            et3.setHint("模块id");
                            new MaterialAlertDialogBuilder(MainActivity.this)
                                    .setIcon(R.drawable.app_settings)
                                    .setView(et3).setTitle("输入面具模块名")
                                    .setPositiveButton("禁用", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String ans=et3.getText().toString();
                                            HackMdm.DeviceMDM.RootCMD("touch /data/adb/modules/"+ans+"/disable");
                                        }
                                    }).setNegativeButton("启用", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String ans=et3.getText().toString();
                                            HackMdm.DeviceMDM.RootCMD("rm /data/adb/modules/"+ans+"/disable");
                                        }
                                    })
                                    .show();
                        }
                        else if(i == 2){
                            final EditText et3 = new EditText(MainActivity.this);
                            new MaterialAlertDialogBuilder(MainActivity.this)
                                    .setIcon(R.drawable.app_settings)
                                    .setView(et3).setTitle("输入命令(无回显)")
                                    .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String ans=et3.getText().toString();
                                            if(HackMdm.DeviceMDM.getMDMName().equals("supi_T11"))
                                                HackMdm.DeviceMDM.RootCMD(ans);
                                        }
                                    })
                                    .show();
                        }
                        else if(i==3){
                            HackMdm.DeviceMDM.RootCMD("am start -n de.robv.android.xposed.installer/de.robv.android.xposed.installer.DebugActivity");
                        }
                        else if(i==4){
                            HackMdm.DeviceMDM.RootCMD("am start -n de.robv.android.xposed.installer/de.robv.android.xposed.installer.WelcomeActivity");
                        }else if(i==5){
                            HackMdm.DeviceMDM.RootCMD("sh /system/tshook/network.sh");
                        }else if(i==6){
                            final EditText et3 = new EditText(MainActivity.this);
                            new MaterialAlertDialogBuilder(MainActivity.this)
                                    .setIcon(R.drawable.app_settings)
                                    .setView(et3).setTitle("输入每次开机自动执行的命令")
                                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String ans=et3.getText().toString();
                                            DataUtils.saveStringValue(MainActivity.this,"t11_start_rootCmd",ans);
                                        }
                                    })
                                    .show();
                        }else if(i==7){
                            new MaterialFilePicker().withActivity(MainActivity.this).withCloseMenu(true).withRootPath("/storage/emulated/0/").withHiddenFiles(true).withFilter(Pattern.compile(".*\\.(zip)$")).withFilterDirectories(false).withTitle("new API_选择文件").withRequestCode(2600).start();
                        }
                    }
                }).show();
                break;

            case 19:
                Flag=true;
                Intent intent=new Intent(MainActivity.this, adbMainActivity.class);
                startActivity(intent);
                break;
        }
        if(!Flag){
            jump2page(menuItemId);
        }
    }
    public void onMenuItemLongClicked(int menuItemId){

        switch (menuItemId) {
            case 1:
                startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                break;
            case 5:
                superlist.clear();
                AlertDialog.Builder superbuilder = new AlertDialog.Builder(MainActivity.this);
                superbuilder.setTitle("选择免杀进程名单");
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
                superbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataUtils.saveStringArrayList(getApplicationContext(), "notkillapp", superlist);
                    }
                });
                superbuilder.show();
                break;
            case 6:
                final String[] items = new String[]{"回领创","开启usb调试"};
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("悬浮窗选项");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                DataUtils.saveintvalue(getApplicationContext(),"float",0);//backtolsp
                                break;
                            case 1:
                                DataUtils.saveintvalue(getApplicationContext(),"float",1);//usb
                        }
                    }
                }).show();
                break;
            case 9:
                int orig_mode=DataUtils.readint(MainActivity.this,"OnTestMode",0);
                if (orig_mode ==0)
                    orig_mode=1;
                else
                    orig_mode=0;
                DataUtils.saveintvalue(MainActivity.this,"OnTestMode",orig_mode);
                Toast.ShowInfo(MainActivity.this,"debug mode on!");
                break;
            case 16:
                startActivity(new Intent(MainActivity.this, linspirer_fakeuploader.class));
                break;
        }
    }
    private  void showsuperapp(){
        if(!getPackageName().equals(BuildConfig.APPLICATION_ID)){
            Toast.ShowErr(MainActivity.this,"检测到您已改包，请安装官方安装包后使用!");
            return;
        }
        superlist.clear();
        AlertDialog.Builder superbuilder = new AlertDialog.Builder(MainActivity.this);
        superbuilder.setTitle("选择超级名单");
        PackageManager pm3 = getPackageManager();
        List<PackageInfo> packages3 = pm3.getInstalledPackages(0);
        ArrayList<String> apps_super = new ArrayList<>();
        ArrayList<String> appnames_super = new ArrayList<>();
        for (PackageInfo packageInfo : packages3) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if ("com.android.launcher3".equals(packageInfo.packageName) || "com.ndwill.swd.appstore".equals(packageInfo.packageName) || getPackageName().equals(packageInfo.packageName)) {
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
        superbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataUtils.saveStringArrayList(MainActivity.this,"superapp",superlist);
                Toast.ShowSuccess(MainActivity.this,"添加成功.包名为"+superlist.toString());
            }
        });
        superbuilder.show();

    }
    private   void verifyStoragePermissions(Activity activity) {
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all){}
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            Toast.ShowWarn(MainActivity.this,"部分权限没有被授权,请在设置赋予所有权限");
                        }
                    }
                });
    }
    private void startvpn() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent prepare = VpnService.prepare(MainActivity.this);
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
    private void showstatusbar() {
        HackMdm.DeviceMDM.RootCMD("cmd statusbar expand-notifications");
    }

    private void runhwunlock() {
        MaterialAlertDialogBuilder alertdialogbuilder11 = new MaterialAlertDialogBuilder(MainActivity.this);
        alertdialogbuilder11.setMessage("是否解控，没有防止恢复出厂可能会导致恢复出厂设置\n")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HackMdm.DeviceMDM.sendBackDoorLINS("command_release_control",1);
                    }
                })
                .setNeutralButton("取消",null).create().show();
    }

}