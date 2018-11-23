package com.taobao.tae.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TaobaoActivity extends Activity implements OnClickListener {

    private static final String TAG = "LOG_TAOBAO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.openTBAuth:
            startTaoBao();
            return;

        default:
            Log.i(TAG, "onClick error");
        }
    }

    private void startTaoBao() {
        TaoOpen.startOauth(this, "23006411", "0176ed8477d01e631d2ff5982dc08dd2");
        // TaoOpen.startOauth(this, "611265",
        // "53b1f3b3da8bf6aefb1bd69ca2e4cdfd");
        // appkey=23006411
        // appsecret=0176ed8477d01e631d2ff5982dc08dd2
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 返回成功，淘宝将返回正确的信息，信息保存在intent中的result项中.
            Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            // 用户主动取消操作.
        } else if (resultCode == -2) {
            // error,淘宝将返回错误码，同样解析intent中的result项，形式如下：
            Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
        }
    }
}
