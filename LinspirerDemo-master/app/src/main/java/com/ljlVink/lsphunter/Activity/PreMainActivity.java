package com.ljlVink.lsphunter.Activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.MainActivity;
public class PreMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premainactivity);
        Intent intent = new Intent(PreMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume(){
        finish();
        super.onResume();
    }
}