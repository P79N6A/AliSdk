package com.socialshare;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.sdk.android.oauth.AppCredential;
import com.taobao.tae.sdk.demo.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class MyShareActivity extends Activity {

	private Map<Integer, AppCredential> plateform2Credential = new HashMap<Integer, AppCredential>();
	private Button shareButton;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		context = this;
		shareButton = (Button) findViewById(R.id.share);
		init();

		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 是否只有已登录用户才能打开分享选择页
				ShareWrapper.INSTANCE.mController.openShare(MyShareActivity.this, false);
			}
		});
	}

	private void init() {
		UMQQSsoHandler qq = new UMQQSsoHandler(MyShareActivity.this, "100424468", "c7394704798a158208a74ab60104f0ba");
		UMWXHandler wxHandler = new UMWXHandler(MyShareActivity.this, "100424468", "c7394704798a158208a74ab60104f0ba");
		QZoneSsoHandler qz = new QZoneSsoHandler(MyShareActivity.this, "100424468", "c7394704798a158208a74ab60104f0ba");
		SinaSsoHandler sina = new SinaSsoHandler(MyShareActivity.this, "2366699782");
		//ShareWrapper.INSTANCE.mController.getConfig().setSsoHandler(sina);

		sina.addToSocialSDK();
		qz.addToSocialSDK();
		qq.addToSocialSDK();
		// 添加微信平台
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(MyShareActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置分享内容
		ShareWrapper.INSTANCE.mController.setShareContent("测试而已");
		// 设置分享图片, 参数2为图片的url地址
		ShareWrapper.INSTANCE.mController.setShareMedia(new UMImage(this,
				"http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E6%96%B0%E6%B5%AA%E5%BE%AE%E5%8D%9A&step_word=&pn=0&spn=0&di=46524679520&pi=&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1010782832%2C2956265822&os=179918213%2C1576251670&adpicid=0&ln=1000&fr=&fmq=1437729359949_R&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&objurl=http%3A%2F%2Fimg4.cache.netease.com%2Ftech%2F2011%2F5%2F9%2F2011050915405516b73.png&fromurl=ippr_z2C%24qAzdH3FAzdH3F45ktsj_z%26e3B8mn_z%26e3Bv54AzdH3FfrjvtwsAzdH3Fn2da88AzdH3F&gsm=0"));// "http://www.umeng.com/images/pic/banner_module_social.png"));
		// ShareWrapper.INSTANCE.oauth(SHARE_MEDIA.SINA, context);

		ShareWrapper.INSTANCE.mController.postShare(MyShareActivity.this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
			@Override
			public void onStart() {
				Log.e("social", "start");
				Toast.makeText(MyShareActivity.this, "START", 0).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				if (eCode == 200) {
					Toast.makeText(MyShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(MyShareActivity.this, "分享失败[" + eCode + "] " + eMsg, Toast.LENGTH_SHORT).show();
				}
			}

		});

		
		/* * AlibabaSDK.getService(OauthService.class).addAppCredential(
		 * OauthPlateform .QQ, "100424468", "c7394704798a158208a74ab60104f0ba");
		 * AlibabaSDK.getService
		 * (OauthService.class).addAppCredential(OauthPlateform.WEIXIN,
		 * "100424468", "c7394704798a158208a74ab60104f0ba");*/
		 

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = ShareWrapper.INSTANCE.mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
