package com.taobao.tae.sdk.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ResultCode;
import com.alibaba.sdk.android.callback.ResultCallback;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.login.callback.LoginCallback;
import com.alibaba.sdk.android.session.model.Session;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.ui.support.WebViewActivitySupport;
import com.socialshare.MyShareActivity;

import com.taobao.tae.sdk.demo.amap.AMapActivity;
import com.taobao.tae.sdk.demo.ui.activity.OpenAccountActivity;
import com.taobao.tae.sdk.demo.ui.activity.OpenTradeActivity;
import com.taobao.tae.sdk.demo.util.StringUtils;

//import com.taobao.tae.sdk.openimsdk.OpenImSdkActivity;

public class MainActivity extends AbstractActivity {

	public int index;
	RadioGroup type;
	private int col = 0;
	private static final String SHARED_PRE = "_tae_sdk_demo";

	private static final String KEY_ENV_INDEX = "envIndex";

	private static final String FORMAT_STRING = "{\"version\":\"1.0.0.daily\",\"target\":\"thirdpartlogin\",\"params\":{\"loginInfo\":{\"loginId\":\"%s\",\"password\":\"%s\"}}}";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		type = (RadioGroup) findViewById(R.id.type);
		type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.taobao) {
					col = 0;
				} else
					col = 1;

			}
		});
		int envIndex = 1;// getEnvIndex();
		TextView view = (TextView) this.findViewById(R.id.envIndex);
		view.setText(String.valueOf(envIndex));

		String inputData = view.getText().toString();
		index = Integer.parseInt(inputData);

		TextView upView = (TextView) this.findViewById(R.id.up);
		upView.setText(this.getResources().getString(R.string.upValue));
		// throwNPE();
	}

	public void showPage(View view) {
		String[] str = EnvData.showPageUrl[index].split(",");
		TextView textView = (TextView) this.findViewById(R.id.showPageUrl);
		String inputData = textView.getText().toString();
		if (StringUtils.isEmpty(inputData)) {
			inputData = str[col];
		}

		AlibabaSDK.getService(ItemService.class).showPage(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(MainActivity.this,
						"支付成功" + tradeResult.paySuccessOrders + "   " + tradeResult.payFailedOrders, Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(MainActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "交易异常 code: " + code + " message" + msg, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}, null, inputData);
		// "file:///android_asset/wanhuJump.html");
	}

	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.opCart:
			intent = new Intent(this.getApplicationContext(), CartActivity.class);
			intent.putExtra("Index", index);
			break;
		case R.id.openShare:
			intent = new Intent(this.getApplicationContext(), MyShareActivity.class);
			intent.putExtra("Index", index);
			break;
		case R.id.opOrder:
			intent = new Intent(this.getApplicationContext(), OrderActivity.class);
			intent.putExtra("Index", index);
			break;
		case R.id.opItem:
			intent = new Intent(this.getApplicationContext(), ItemActivity.class);
			intent.putExtra("Index", index);
			break;
		case R.id.opMember:
			intent = new Intent(this.getApplicationContext(), MemberActivity.class);
			break;
		case R.id.opPic:
			intent = new Intent(this.getApplicationContext(), UploadActivity.class);
			break;
		case R.id.opMa:
			intent = new Intent(this.getApplicationContext(), MaActivity.class);
			break;
		case R.id.opOpenAccount:
			intent = new Intent(this.getApplicationContext(), OpenAccountActivity.class);
			break;
		case R.id.opOpenTrade:
			intent = new Intent(this.getApplicationContext(), OpenTradeActivity.class);
			break;
		case R.id.opIMUI:
			try {
				intent = new Intent(this.getApplicationContext(),
						Class.forName("com.taobao.tae.sdk.openimui.OpenImUiActivity"));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				Toast.makeText(MainActivity.this, "当前demo不支持 openIM ui 入口", Toast.LENGTH_LONG).show();
				return;
			}
			break;
		/*
		 * case R.id.opIMSDK: try { intent = new
		 * Intent(this.getApplicationContext(),
		 * Class.forName("com.taobao.tae.sdk.openimsdk.OpenImSdkActivity")); }
		 * catch (ClassNotFoundException e1) { e1.printStackTrace();
		 * Toast.makeText(MainActivity.this, "当前demo不支持 openIM 非ui 入口",
		 * Toast.LENGTH_LONG).show(); return; } break;
		 */
		case R.id.openAMap:
			intent = new Intent(this, AMapActivity.class);
			break;
		case R.id.open_web_view:
			AlibabaSDK.getService(this, LoginService.class, new ResultCallback<LoginService>() {

				@Override
				public void onFailure(int code, String msg) {

				}

				@Override
				public void onSuccess(LoginService loginService) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("domain", "yunoswatch"); // 打开yunoswatch定义的样式
					params.put("config", "{\"qrwidth\": 200}");
					params.put("userDefActivity", "com.alibaba.sdk.android.login.ui.QrLoginActivity");
					params.put("userDefLayoutId", "tae_sdk_login_qr_activity_layout");
					AlibabaSDK.getService(LoginService.class).showQrCodeLogin(MainActivity.this, params,
							new InternalLoginCallback());
				}
			});
			return;
		case R.id.opRestartApp:
			TextView view = (TextView) this.findViewById(R.id.envIndex);
			String envIndexStr = view.getText().toString();
			try {
				int envIndex = Integer.parseInt(envIndexStr);
				setEnvIndex(envIndex);
				restart();
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "please input Integer", Toast.LENGTH_LONG).show();
			}
			return;
		case R.id.opLogin:
			TextView upView = (TextView) this.findViewById(R.id.up);
			String upStr = upView.getText().toString();
			if (upStr == null || "".equals(upStr)) {
				Toast.makeText(MainActivity.this, "input err.", Toast.LENGTH_LONG).show();
				return;
			}
			final String[] upArray = upStr.split(",");
			if (upArray == null || upArray.length < 2) {
				Toast.makeText(MainActivity.this, "input format err.", Toast.LENGTH_LONG).show();
				return;
			}
			return;
		}
		startActivity(intent);
	}

	private class InternalLoginCallback implements LoginCallback {

		@Override
		public void onSuccess(Session session) {
			Toast.makeText(MainActivity.this, "授权成功" + session.getUserId() + session.getUser().nick + session.isLogin()
					+ session.getLoginTime() + session.getUser().avatarUrl, Toast.LENGTH_SHORT).show();
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
			Toast.makeText(MainActivity.this, "授权取消" + code, Toast.LENGTH_SHORT).show();
		}
	}

	private void setEnvIndex(int envIndex) {
		SharedPreferences.Editor sharedata = getSharedPreferences(SHARED_PRE, 0).edit();
		sharedata.putInt(KEY_ENV_INDEX, envIndex);
		sharedata.commit();
	}

	private void restart() {
		Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private int getEnvIndex() {
		SharedPreferences sharedata = getSharedPreferences(SHARED_PRE, 0);
		int envIndex = sharedata.getInt(KEY_ENV_INDEX, 0);
		return envIndex;
	}

	private void throwNPE() {
		throw new NullPointerException();
	}

}
