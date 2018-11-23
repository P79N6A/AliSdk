package com.socialshare;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthInfoConfig;
import com.alibaba.sdk.android.oauth.OauthPlateform;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class ShareWrapper {
	public static final ShareWrapper INSTANCE = new ShareWrapper();
	public static UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	
	public void oauth(SHARE_MEDIA type,final Context mContext){
		mController.doOauthVerify(mContext, type, new UMAuthListener() {
		    @Override
		    public void onStart(SHARE_MEDIA platform) {
		        Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
		    }
		    @Override
		    public void onError(SocializeException e, SHARE_MEDIA platform) {
		        Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
		    }
		    @Override
		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
		        Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
		        //获取相关授权信息或者跳转到自定义的分享编辑页面
		        String uid = value.getString("uid");
		    }
		    @Override
		    public void onCancel(SHARE_MEDIA platform) {
		        Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
		    }
		} );
	}
}
