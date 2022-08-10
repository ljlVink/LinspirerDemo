package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.immersionbar.ImmersionBar;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.utils.Toast;

public class webview extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        ImmersionBar.with(this).transparentStatusBar().init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webview);
        String url=getIntent().getStringExtra("url");
        if(url.contains("github")){
            Toast.ShowInfo(this, "github可能要打开较长时间,请稍等...");
        }
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //hook,防止其他浏览器打开
                return false;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent){
        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
        }
        return super.onKeyDown(keyCode,keyEvent);
    }
}