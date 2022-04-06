package cn.lspdemo_lspfakedata.linspirer;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lspdemo_lspfakedata.tools.AesUtil;
import cn.lspdemo_lspfakedata.tools.PostUtil;

public class LingchuangUtils {
    Context context;
    private PostUtil postUtil;
    public LingchuangUtils(Context context){
        this.context=context;
        this.postUtil=new PostUtil(context);
    }
    public void User_login(@NonNull String account,
                           @NonNull String passwd,
                           @NonNull String swdid,
                           String Version,
                           String device_type,
                           String rom_version,
                           String device_id){

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userid",account);
        jsonObject.put("password",passwd);
        jsonObject.put("swdid",swdid);
        if(device_type!=null){
            jsonObject.put("device_type",device_type);
        }if(rom_version!=null){
            jsonObject.put("rom_version",rom_version);
        }if(device_id!=null){
            jsonObject.put("device_id",device_id);
        }if(device_id!=null){
            jsonObject.put("device_id",device_id);
        }
        JSONObject jsonObject1;
        if (Version ==null){
            jsonObject1=createbasejson("tongyongshengchan_5.03.007.0","com.linspirer.user.login");
        }else {
            jsonObject1=createbasejson(Version,"com.linspirer.user.login");
        }
        jsonObject1.put("params", AesUtil.encrypt("1191ADF18489D8DA",jsonObject.toString()));
        postUtil.sendPost(jsonObject1, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                Log.e("tag1",str);
                Log.e("tag1",AesUtil.decrypt("1191ADF18489D8DA",str));
            }
        });

    }
    private JSONObject createbasejson(@NonNull  String version,String method){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("is_encrypt",true);
        jsonObject.put("method",method);
        jsonObject.put("id",1);
        jsonObject.put("!version",5);
        jsonObject.put("jsonrpc","2.0");
        jsonObject.put("client_version",version);
        return  jsonObject;
    }
}
