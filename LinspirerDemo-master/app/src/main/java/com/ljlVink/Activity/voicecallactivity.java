package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ljlVink.core.DataUtils;

public class voicecallactivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int counter= DataUtils.readint(this,"cnt");
        counter++;
        DataUtils.saveintvalue(this,"cnt",counter);
        if(counter == 3){
            Intent intent = new Intent(this, NewUI.class);
            intent.putExtra("isstart",false);
            startActivity(intent);
            DataUtils.saveintvalue(this,"cnt",0);
        }
        else{
            finish();
            moveTaskToBack(true);
        }
    }
    @Override
    protected void onResume(){
        finish();
        super.onResume();
    }
}