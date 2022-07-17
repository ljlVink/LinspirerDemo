package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.huosoft.wisdomclass.linspirerdemo.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lspdemo_settings, new SettingsFragment())
                .commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}