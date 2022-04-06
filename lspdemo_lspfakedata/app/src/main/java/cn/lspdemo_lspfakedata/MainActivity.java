package cn.lspdemo_lspfakedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import cn.lspdemo_lspfakedata.linspirer.LingchuangUtils;
import cn.lspdemo_lspfakedata.tools.AesUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("tag", AesUtil.encrypt("1191ADF18489D8DA","aaaa"));
        //new LingchuangUtils(this).User_login("acc","1243","aaa","bbb","ccc","ddd","eee");

        Intent intent=new Intent();
        intent.setAction("111");
        intent.setClassName("com.ndwill.swd.appstore","com.ndwill.swd.appstore.receiver.ControlAppReceiver");
        ArrayList<String>a=new ArrayList<>();
        a.add("com.teslacoilsw.launcher");
        a.add("com.tencent.mm");
        a.add("com.ljlVink.broadcastdemo");
        intent.putExtra("control_apps",a);
        sendBroadcast(intent);
    }
}