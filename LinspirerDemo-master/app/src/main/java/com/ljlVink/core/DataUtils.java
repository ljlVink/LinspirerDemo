package com.ljlVink.core;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class DataUtils {
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }
    public static ArrayList<String> ReadStringArraylist(Context context,String tag){
        SharedPreferences spf = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        return new ArrayList<String>(spf.getStringSet(tag,new HashSet<String>()));
    }
    public static void saveStringArrayList(Context context,String tag, ArrayList<String> dataList){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        Set set = new HashSet();
        set.addAll(dataList);
        editor.putStringSet(tag,set);
        editor.apply();
    }

    public static Set<String> readStringList(Context context,String tag){
        SharedPreferences spf = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        return spf.getStringSet(tag, new HashSet<String>());
    }

    public static int readint(Context context,String tag){
        SharedPreferences spf =context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        return spf.getInt(tag,0);
    }
    public static int readint(Context context,String tag,int defval){
        SharedPreferences spf =context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        int x=defval;
        try{
            x=spf.getInt(tag,defval);
        }catch (Exception e){
            Log.e("removed","removed conflicting val");
            removeintvalue(context,tag);
            x=spf.getInt(tag,defval);
        }
        return x;
    }
    public static void removeintvalue(Context context,String tag){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.remove(tag);
        editor.apply();
    }
    public static void saveintvalue(Context context,String tag,int value){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putInt(tag,value);
        editor.apply();
    }
    public static void saveStringValue(Context context,String tag,String value){
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(tag,value);
        editor.apply();
    }

    public static String readStringValue(Context context,String tag,String defVal){
        SharedPreferences spf = context.getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        return spf.getString(tag,defVal);
    }

}
