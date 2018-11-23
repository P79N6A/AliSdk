package com.taobao.tae.sdk.openimui;

import com.alibaba.mobileim.YWAccount;
import com.alibaba.mobileim.YWSDK;
import com.alibaba.mobileim.YWUIAPI;
import com.alibaba.mobileim.login.YWLoginState;
import com.taobao.tae.sdk.TaeSDK;
import com.taobao.tae.sdk.demo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class ConversationActivityWithFragment extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		YWAccount api = OpenWxApi.getInstance().getUIApi().getAccount();
		setContentView(R.layout.activity_conversation);
		if (api.getLoginState() == YWLoginState.success) {
			Fragment fragment = OpenWxApi.getInstance().getUIApi()
					.getConversationFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.wx_conversation_container, fragment).commit();
		} else {
			Toast.makeText(ConversationActivityWithFragment.this, "未登录",
					Toast.LENGTH_SHORT).show();
			finish();

		}

	}

}
