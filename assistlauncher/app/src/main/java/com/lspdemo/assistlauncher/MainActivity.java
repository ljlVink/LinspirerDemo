package com.lspdemo.assistlauncher;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(DataUtils.readStringValue(this,"desktop_pkg","").equals("")){
            try {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.etiantian.stulauncherlc"));
            }catch (Exception ignore){}
        }else {
            try{
                startActivity(getPackageManager().getLaunchIntentForPackage(DataUtils.readStringValue(this,"desktop_pkg","")));
            }catch (Exception ignore){
            }
        }
    }
}