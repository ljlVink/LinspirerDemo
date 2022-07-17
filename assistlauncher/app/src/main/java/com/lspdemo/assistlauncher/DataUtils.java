package com.lspdemo.assistlauncher;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataUtils {
    public static void removevalue(Context context,String tag){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.remove(tag);
        editor.apply();
    }
    public static void saveStringValue(Context context,String tag,String value){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(tag,value);
        editor.apply();
    }

    public static String readStringValue(Context context,String tag,String defVal){
        SharedPreferences spf = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        String x;
        try{
            x= spf.getString(tag,defVal);
        }catch (Exception e){
            Log.e("removed","removed conflicting val");
            removevalue(context,tag);
            x=spf.getString(tag,defVal);
        }
        return x;
    }

}
