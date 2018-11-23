package com.taobao.tae.sdk.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.sdk.android.Constants;

@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_webview, null);
        WebView webview = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.addView(webview, params);
        setContentView(linearLayout);
        webview.getSettings().setJavaScriptEnabled(true);
        String userAgent = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgent + " " + " WindVane");
        webview.clearCache(true);
        CookieSyncManager.getInstance().sync();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webview.loadUrl(getIntent().getStringExtra(Constants.URL));
    }
}
