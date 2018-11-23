package com.taobao.tae.sdk.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.taobao.ma.bar.business.api.MaBarShoppingAPI;

public class ScanWebViewActivity extends Activity{
	
	 @SuppressLint("SetJavaScriptEnabled")
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.webview);
			
			WebView webview = (WebView) findViewById(R.id.demo_webview);
        	String surl = MaBarShoppingAPI.getLandingPageUrl("6918717162222");
        	Uri uri = Uri.parse(surl);
        	Log.e("MA", uri.toString());
        	
        	webview.getSettings().setJavaScriptEnabled(true);
        	webview.getSettings().setDomStorageEnabled(true);
        	String userAgent = webview.getSettings().getUserAgentString();
        	userAgent += " WindVane tae_sdk_a_1.0.3";
        	webview.getSettings().setUserAgentString(userAgent);
        	webview.loadUrl(surl);
	 }

}
