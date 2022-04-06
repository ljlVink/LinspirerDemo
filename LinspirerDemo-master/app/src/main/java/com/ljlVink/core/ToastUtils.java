package com.ljlVink.core;
import android.content.Context;
import android.widget.Toast;
public class ToastUtils {
    public static void ShowToast(String str, Context context){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
