package com.taobao.tae.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.FailureCallback;
import com.alibaba.sdk.android.ma.MaService;
import com.alibaba.sdk.android.webview.UiSettings;

public class MaActivity extends Activity {

  
    EditText resultUrl;
    Button mOpenCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma);
        resultUrl = (EditText) this.findViewById(R.id.resultUrl);
        mOpenCamera = (Button) findViewById(R.id.openCamera);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void openCamera(View view) {

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CameraActivity.class);
        startActivity(intent);
    }

    public void showMedicineMaView(View view) {

        MaService maService = AlibabaSDK.getService(MaService.class);
        UiSettings settings = new UiSettings();
        settings.title = "药品详情页面";
        maService.showMedicinePage(this, new FailureCallback() {
            @Override
            public void onFailure(int code, String msg) {

                Toast.makeText(MaActivity.this, "打开药品页面出错，消息是 " + msg, Toast.LENGTH_SHORT).show();
            }
        }, settings, "6918717162222");
    }

    public void showGoodMaView(View view) {
        MaService maService = AlibabaSDK.getService(MaService.class);
        UiSettings settings = new UiSettings();
        settings.title = "商品详情页面";
        maService.showLandingPage(this, new FailureCallback() {
            @Override
            public void onFailure(int code, String msg) {

                Toast.makeText(MaActivity.this, "打开商品页面出错，消息是 " + msg, Toast.LENGTH_SHORT).show();
            }
        }, settings, "6918717162222");
    }

    
}
