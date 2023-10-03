package com.ljlVink.lsphunter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.core.Postutil;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;

import java.io.File;

public class BaseActivity extends FragmentActivity {
    public void setvisibility(boolean isVisible) {
        findViewById(R.id.titlebar2).setVisibility(View.VISIBLE);
        new Postutil(this).SwordPlan();
        try{
            if (isVisible) {
                findViewById(R.id.left_container).setVisibility(View.VISIBLE);
                if(Sysutils.isTabletDevice(this))
                    findViewById(R.id.right_container).setVisibility(View.VISIBLE);
                String bgpath= DataUtils.readStringValue(this,"background_bg","");
                if(!bgpath.equals("")&&new File(bgpath).exists()){
                    LinearLayout ll=findViewById(R.id.background_lsphunter);
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
                findViewById(R.id.left_container).setVisibility(View.VISIBLE);
                if(Sysutils.isTabletDevice(this))
                    findViewById(R.id.right_container).setVisibility(View.VISIBLE);
                onResume();
            }
        }catch (Exception e){

        }catch (Throwable th){

        }
    }
    public void setfalseVisibility(){
        try{
            findViewById(R.id.titlebar2).setVisibility(View.INVISIBLE);
            findViewById(R.id.left_container).setVisibility(View.INVISIBLE);
            if(Sysutils.isTabletDevice(getApplicationContext())) findViewById(R.id.right_container).setVisibility(View.INVISIBLE);
        }catch (Exception e){}
    }

}
