package com.ljlVink.lsphunter.fragment;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.core.IPostcallback;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.TimeUtils;
import com.ljlVink.lsphunter.utils.appsecurity.envcheck;
import com.xuexiang.xui.widget.textview.LoggerTextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Homefragment {
    View view;
    Context ctx;
    LoggerTextView logger;
    public Homefragment(View view,Context ctx){
        this.view=view;
        this.ctx=ctx;
    }
    public void HandleFragment(){
        logger=view.findViewById(R.id.logger2);
        logger.setLogFormatter((logContent, logType) -> TimeUtils.getNowString(new SimpleDateFormat("[HH:mm]")) +" "+logContent);
        getannouncement();

    }
    private  void deviceinfo(){
        String MDMname= HackMdm.DeviceMDM.getMDMName();
        logger.logNormal("版本号:" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")"+"("+BuildConfig.BUILD_GITVER+")");
        logger.logNormal("编译时间:"+BuildConfig.BUILD_DATE);
        logger.logNormal("包名:" + ctx.getPackageName());
        logger.logNormal("MDM接口:" + MDMname);
        if(MDMname.equals("CSDK")){
            if(HackMdm.DeviceMDM.HasAbilityCSDK_new()){
                logger.logSuccess("机型支持新联想csdk接口");
            }
        }
        logger.logNormal("系统版本:" + String.format(Locale.ROOT, "%1$s (API %2$d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        logger.logNormal("系统:"+Build.FINGERPRINT);
        logger.logNormal("设备:" + Sysutils.getDevice());
        logger.logNormal("CPU:"+ Sysutils.getCpuName()+" ("+Build.CPU_ABI+")");
        logger.logNormal("HackMdm Version:"+HackMdm.DeviceMDM.getVersion());
        if(Sysutils.isAssistantApp(ctx)){
            logger.logSuccess("语音助手:已激活");
        }
        else{
            logger.logWarning("语音助手:未激活");
        }
        if(Sysutils.isActiveime(ctx)){
            logger.logSuccess("输入法:已激活");
        }
        else{
            logger.logWarning("输入法:未激活");
        }
        String emui=Sysutils.getHwemui();
        if(!emui.equals("")){
            if(emui.startsWith("EmotionUI_12")||emui.equals("EmotionUI_13")){
                logger.logSuccess("华为鸿蒙:"+emui);
            }else {
                logger.logSuccess("华为emui:"+emui);
            }
        }
        if(Sysutils.isUsbDebug(ctx)){
            logger.logSuccess("usb调试:已开启");
        }else{
            logger.logWarning("usb调试:未开启");
        }
        if(Sysutils.isDevSettings(ctx)){
            logger.logSuccess("开发者选项:已开启");
        }else{
            logger.logWarning("开发者选项:未开启");
        }
        String lspirer=Sysutils.getLcmdm_version(ctx);
        if(!lspirer.equals("设备未安装管控")){
            logger.logNormal("管控版本:"+lspirer);
        }
        if(DataUtils.readint(ctx,"vpnmode")!=1){
            logger.logNormal("领创网络屏蔽:未启动");
        }
        if(envcheck.IsDeviceRooted()){
            logger.logWarning("root状态:已root");
        }else{
            logger.logWarning("root状态:未root");
        }
        double used=(double) Long.parseLong(Sysutils.getRomavailablesize(ctx))/Long.parseLong(Sysutils.getRomtotalsize(ctx))*100;
        String msg="设备已用空间"+(100.00000000-used)+"%("+ Sysutils.getRomavailablesize(ctx)+"/"+ Sysutils.getRomtotalsize(ctx)+")";
        logger.logNormal(msg);
        logger.logNormal("app白名单"+HackMdm.DeviceMDM.getAppWhitelist());
    }

    private void getannouncement(){
        new Postutil(ctx).getAnnouncement(new IPostcallback() {
            @Override
            public void onSuccess(String data) {
                logger.logNormal("服务器连接成功!\n"+data);
                deviceinfo();
            }
            @Override
            public void onInternetErr() {
                logger.logError("网络连接异常，请检查网络");
                deviceinfo();
            }
        });
    }

}
