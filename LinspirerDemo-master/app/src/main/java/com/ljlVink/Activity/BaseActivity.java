package com.ljlVink.Activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.immersionbar.ImmersionBar;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.utils.DataUtils;
import com.ljlVink.utils.Sysutils;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
    public void initview(){
        ImmersionBar.with(this).transparentStatusBar().init();
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
    }
    public void setvisibility(boolean isVisible) {
        findViewById(R.id.titlebar).setVisibility(View.VISIBLE);
        new Postutil(this).SwordPlan();
        try{
            if (isVisible) {
                findViewById(R.id.left).setVisibility(View.VISIBLE);
                if(Sysutils.isTabletDevice(this))
                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                findViewById(R.id.grid_photo).setVisibility(View.VISIBLE);
                String bgpath= DataUtils.readStringValue(this,"background_bg","");
                if(!bgpath.equals("")&&new File(bgpath).exists()){
                    LinearLayout ll=findViewById(R.id.background_newui);
                    Glide.with(this)
                            .load(bgpath)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    ll.setBackground(resource);
                                }
                            });
                }
                onResume();
            } else {
                findViewById(R.id.left).setVisibility(View.VISIBLE);
                if(Sysutils.isTabletDevice(this))
                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                findViewById(R.id.grid_photo).setVisibility(View.INVISIBLE);
                onResume();
            }
        }catch (Exception e){

        }catch (Throwable th){

        }
    }

}
