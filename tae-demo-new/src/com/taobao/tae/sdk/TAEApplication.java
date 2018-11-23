package com.taobao.tae.sdk;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.util.UTWrapper;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.tcms.PushListener;
import com.alibaba.tcms.XPushManager;
import com.alibaba.tcms.env.YWEnvType;
import com.alibaba.tcms.utils.PushLog;
import com.taobao.tae.sdk.callback.InitResultCallback;
import com.taobao.tae.sdk.openimui.OpenimInfoInitActivity;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TAEApplication extends Application {

	private YWEnvType curEnviroment;
	@Override
	public void onCreate() {
		super.onCreate();
//		curEnviroment= OpenimInfoInitActivity
//				.getYWEnvTypeFromLocal(getSharedPreferences(
//						OpenimInfoInitActivity.LOCAL_STORE_NAME,
//						Context.MODE_PRIVATE).getString(
//						OpenimInfoInitActivity.ENVIROMENT, "test"));
//
//
//		/**
//		 * 可能的取值：
//		 * TaeSdkEnvironment.SANDBOX //沙箱
//		 * TaeSdkEnvironment.PRE//预发
//		 *  TaeSdkEnvironment.ONLINE//线上
//		 *  TaeSdkEnvironment.TEST//测试
//		 */
//		if(curEnviroment.getValue()==YWEnvType.ONLINE.getValue()){ 
//			TaeSDK.setEnvIndex( TaeSdkEnvironment.ONLINE.ordinal());
//		}else if(curEnviroment.getValue()==YWEnvType.TEST.getValue()){
//			TaeSDK.setEnvIndex(TaeSdkEnvironment.TEST.ordinal());
//		}else if(curEnviroment.getValue()==YWEnvType.PRE.getValue()){
//			TaeSDK.setEnvIndex(TaeSdkEnvironment.PRE.ordinal());
//		}
		
		AlibabaSDK.asyncInit(this, new InitResultCallback() {

			@Override
			public void onSuccess() {
				Toast.makeText(TAEApplication.this, "TaeSDK 初始化成功", Toast.LENGTH_SHORT)
				.show();
			}

			@Override
			public void onFailure(int code, String message) {
				Toast.makeText(TAEApplication.this, "TaeSDK 初始化异常，code = " + code + ", info = " + message, Toast.LENGTH_SHORT)
						.show();
				Log.w("mayongge", "初始化异常，code = " + code + ", info = " + message);
			}

		});
		UTWrapper.setUTEnable(true);
//		YWEnvManager.prepare(this,curEnviroment);
		YWChannel.prepare(
				this,
				getSharedPreferences(OpenimInfoInitActivity.LOCAL_STORE_NAME,
						Context.MODE_PRIVATE).getString(
						OpenimInfoInitActivity.APPKEY, "23015524"));
		YWChannel.prepare(this,"23015524");

		XPushManager.getInstance().init(this, new PushListener() {
			
			@Override
			public void onServiceStatus(boolean arg0) {
				// TODO Auto-generated method stub	
			}
			
			@Override
			public void onCustomPushData(Context arg0, String arg1) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onClientIdUpdate(String arg0) {
				PushLog.e("MyApplication", "clientId:"+arg0);
			}
		});

	}

}
