package com.taobao.tae.sdk.demo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.oauth.OauthPlateform;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.impl.OpenAccountContext;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.session.SessionListener;
import com.alibaba.sdk.android.session.SessionService;
import com.alibaba.sdk.android.session.model.Session;
import com.taobao.tae.sdk.demo.R;
import com.taobao.tae.sdk.demo.amap.utils.ToastUtil;
import com.taobao.tae.sdk.demo.model.TokenInfoBuilder;

public class OpenAccountActivity extends Activity {
    private static final String TAG = "AliSDKDemo";
    private EditText editOpenAccountISVToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_account);
        editOpenAccountISVToken = (EditText) findViewById(R.id.text_openAccountISVToken);
    }

    public void tokenLogin(View view) {
        try {
            String token = editOpenAccountISVToken.getText().toString();
            if (token == null || token.length() == 0) {
                EditText edit = (EditText) findViewById(R.id.text_openAccountOpenId);
                String openId = edit.getText().toString();
                edit = (EditText) findViewById(R.id.text_openAccountIsvSecret);
                String isvSecret = edit.getText().toString();
                token = "flKlNbdiFku05ZYQQC+L1q6/h1+z3mwzDKmCFhdD/TQZqv9vl0Dxj3Xr+Fum0yRo";
                editOpenAccountISVToken.setText(token);
            }
            OpenAccountService openAccountService = AlibabaSDK.getService(OpenAccountService.class);
            openAccountService.tokenLogin(this, token, new LoginCallback() {

                @Override
                public void onSuccess(OpenAccountSession session) {
                    ToastUtil.show(getApplicationContext(), "success: " + session.getUserId());
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "error: " + msg);
                }
            });
        } catch (Exception e) {
            ToastUtil.show(getApplicationContext(), "error: " + e.getMessage());
            Log.e(TAG, "error", e);
        }
    }

    public void login(View view) {
    	 AlibabaSDK.getService(OauthService.class).addAppCredential(OauthPlateform.QQ, "100424468",
                 "c7394704798a158208a74ab60104f0ba");
         AlibabaSDK.getService(OauthService.class).addAppCredential(OauthPlateform.WEIXIN, "100424468",
                 "c7394704798a158208a74ab60104f0ba");
         AlibabaSDK.getService(OauthService.class).addAppCredential(OauthPlateform.WEIBO, "2366699782", "54f8585c85714ab53dea279cd48095d3");
        OpenAccountUIService openAccountService = AlibabaSDK.getService(OpenAccountUIService.class);
        try {
            openAccountService.showLogin(this, new LoginCallback() {

                @Override
                public void onSuccess(OpenAccountSession session) {
                    ToastUtil.show(getApplicationContext(), "success: " + session.getUserId()+OpenAccountContext.sessionService.getSId());

                    Intent intent = new Intent(OpenAccountActivity.this, OpenAccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    OpenAccountActivity.this.startActivity(intent);

                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "error: " + msg);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public void register(View view) {
        OpenAccountUIService openAccountService = AlibabaSDK.getService(OpenAccountUIService.class);
        try {
            openAccountService.showRegister(this, new LoginCallback() {

                @Override
                public void onSuccess(OpenAccountSession session) {
                    ToastUtil.show(getApplicationContext(), "success: " + session.getUserId());
                    Intent intent = new Intent(OpenAccountActivity.this, OpenAccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    OpenAccountActivity.this.startActivity(intent);
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "error: " + msg);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public void resetPassword(View view) {
        OpenAccountUIService openAccountService = AlibabaSDK.getService(OpenAccountUIService.class);
        try {
            openAccountService.showResetPassword(this, new LoginCallback() {

                @Override
                public void onSuccess(OpenAccountSession session) {
                    ToastUtil.show(getApplicationContext(), "success: " + session.getUserId());
                    Intent intent = new Intent(OpenAccountActivity.this, OpenAccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    OpenAccountActivity.this.startActivity(intent);
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "error: " + msg);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public void logout(View view) {
        OpenAccountService openAccountService = AlibabaSDK.getService(OpenAccountService.class);
        try {
            openAccountService.logout(this, new LogoutCallback() {

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(getApplicationContext(), "登出失败" + code, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "登出成功", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public void autoLoginGetToken(View view) {
        final OpenAccountUIService openAccountUIService = AlibabaSDK.getService(OpenAccountUIService.class);
        final OpenAccountService openAccountService = AlibabaSDK.getService(OpenAccountService.class);
        final SessionService sessionService = OpenAccountContext.sessionService;
        openAccountService.addSessionListener(new SessionListener() {
            @Override
            public void onStateChanged(Session session) {
                if (session.isLogin()) {
                    ToastUtil.show(getApplicationContext(), "Session is login: " + session.getUserId());
                    ToastUtil.show(getApplicationContext(), "Session sid: " + sessionService.getSId());
                } else {
                    ToastUtil.show(getApplicationContext(), "Session not login");
                }
            }
        });
        Session session = openAccountService.getSession();
        if (session == null || !session.isLogin()) {
            openAccountUIService.showLogin(this, new LoginCallback() {
                @Override
                public void onSuccess(OpenAccountSession session) {
                    ToastUtil.show(getApplicationContext(), "Session login: " + session.getUserId());
                    ToastUtil.show(getApplicationContext(), "Session code: " + session.getAuthorizationCode());
                    ToastUtil.show(getApplicationContext(), "Session sid: " + sessionService.getSId());

                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "Login failure: " + msg);
                    Intent intent = new Intent(OpenAccountActivity.this, OpenAccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    OpenAccountActivity.this.startActivity(intent);
                }
            });
        } else {
            ToastUtil.show(getApplicationContext(), "Session code: " + session.getUserId());
//            final OpenTradeService openTradeService = AlibabaSDK.getService(OpenTradeService.class);
//            try {
//                openTradeService.createOrder(OpenAccountActivity.this
//                        , null
//                        , null
//                        , "title"
//                        , 100L
//                        , JSONUtils.toMap(new JSONObject("{data:'data'}"))
//                        , new CreateOrderCallback() {
//
//                    @Override
//                    public void onSuccess(Long orderId) {
//                        ToastUtil.show(getApplicationContext(), "success: " + orderId);
//                        openTradeService.showPayOrder(OpenAccountActivity.this
//                                , orderId
//                                , ""
//                                , ""
//                                , 1
//                                , "30m"
//                                , new TradeProcessCallback() {
//                            @Override
//                            public void onPaySuccess(TradeResult tradeResult) {
//                                ToastUtil.show(OpenAccountActivity.this, "success: " + tradeResult.paySuccessOrders);
//                            }
//
//                            @Override
//                            public void onFailure(int code, String msg) {
//                                ToastUtil.show(OpenAccountActivity.this, "error: " + msg);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        ToastUtil.show(getApplicationContext(), "error: " + msg);
//                    }
//                });
//            } catch (Exception e) {
//                Log.e(TAG, "error", e);
//            }
        }
    }

}
