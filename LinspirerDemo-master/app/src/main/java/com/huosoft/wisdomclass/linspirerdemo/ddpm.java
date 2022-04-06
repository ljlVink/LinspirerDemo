package com.huosoft.wisdomclass.linspirerdemo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ljlVink.core.HackMdm;
import com.ljlVink.core.ToastUtils;

public class ddpm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddpm);
        try{
            new HackMdm(this).RemoveOwner_admin();
        }catch (Exception e){
            ToastUtils.ShowToast("失败",this);
        }
        ToastUtils.ShowToast("成功",this);
    }
}