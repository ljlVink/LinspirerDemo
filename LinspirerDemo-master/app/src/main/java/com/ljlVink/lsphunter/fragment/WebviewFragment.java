package com.ljlVink.lsphunter.fragment;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huosoft.wisdomclass.linspirerdemo.R;

public class WebviewFragment {
    View view;
    Context ctx;
    WebView webView;
    public WebviewFragment(View view, Context ctx){
        this.view=view;
        this.ctx=ctx;
    }
    public void HandleWebView(String url){

        webView = (WebView) view.findViewById(R.id.webview);
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

    }
}
