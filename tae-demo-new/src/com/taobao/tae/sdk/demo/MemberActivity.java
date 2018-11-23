package com.taobao.tae.sdk.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.login.callback.LoginCallback;
import com.alibaba.sdk.android.login.callback.LogoutCallback;
import com.alibaba.sdk.android.session.model.Session;
import com.alibaba.sdk.android.ui.support.WebViewActivitySupport;

public class MemberActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

    }

    // authenticate & authorize
    public void showLogin(View view) {
        // TaoOpen.startOauth(MainActivity.this, "23009223",
        // "05966f24a4e76e7c6f9ce40f15f1018f");
        // TaoOpen.startOauth(MainActivity.this, "23006411",
        // "0176ed8477d01e631d2ff5982dc08dd2");
        AlibabaSDK.getService(LoginService.class).showLogin(MemberActivity.this, new InternalLoginCallback());
    }

    public void logout(View view) {
        AlibabaSDK.getService(LoginService.class).logout(this, new LogoutCallback() {

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(MemberActivity.this, "登出失败" + code, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess() {
                Toast.makeText(MemberActivity.this, "登出成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showQrLogin(View view) {
    	Map<String,String> params = new HashMap<String,String>();
    	params.put("domain", "yunoswatch"); // 打开yunoswatch定义的样式
    	params.put("config", "{\"qrwidth\": 200}");
    	params.put("userDefActivity", "com.alibaba.sdk.android.login.ui.QrLoginActivity");
    	params.put("userDefLayoutId", "tae_sdk_login_qr_activity_layout");
        AlibabaSDK.getService(LoginService.class).showQrCodeLogin(this, params, new InternalLoginCallback());
    }

    private class InternalLoginCallback implements LoginCallback {

        @Override
        public void onSuccess(Session session) {
            Toast.makeText(
                    MemberActivity.this,
                    "授权成功" + session.getUserId() + session.getUser().nick + session.isLogin() + session.getLoginTime()
                            + session.getUser().avatarUrl, Toast.LENGTH_SHORT).show();
            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync();
            Map<String, String[]> m = WebViewActivitySupport.getInstance().getCookies();
            for (Entry<String, String[]> e : m.entrySet()) {
                for (String s : e.getValue()) {
                    CookieManager.getInstance().setCookie(e.getKey(), s);
                    System.out.println("key: " + e.getKey() + " value: " + s);
                }
            }
            CookieSyncManager.getInstance().sync();
            System.out.println("------------" + CookieManager.getInstance().getCookie("http://taobao.com"));
        }

        @Override
        public void onFailure(int code, String message) {
            Toast.makeText(MemberActivity.this, "授权取消" + code, Toast.LENGTH_SHORT).show();
        }
    }
}
