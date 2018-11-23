package com.taobao.tae.sdk.openimui;

import java.util.List;

import com.alibaba.mobileim.YWAccount;
import com.alibaba.mobileim.YWSDK;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.YWTribeManager;
import com.taobao.tae.sdk.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

/**
 * 群基本操作：创建、加入等等。
 * 
 * @author peizhi.ypz
 * 
 */
public class TribeOperationActivity extends Activity implements OnClickListener {

	private YWTribeManager mTribeManager;
	private Button createTribe;
	private Button joinTribe;
	private Button tribeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribe_operation);
		
//		int clientType = getIntent().getIntExtra("CLIENT", 1);
//		YWAccount account;
//		if (clientType == InitialActivity.TB_CLIENT) {
//			account = YWSDK.getTBUIAPI().getAccount();
//		}else {
//			account = YWSDK.getOpenUIAPI().getAccount();
//		}
//		mTribeManager = account.getTribeManager();
		mTribeManager = OpenWxApi.getInstance().getTribeManager();
		createTribe = (Button) findViewById(R.id.create_tribe);
		joinTribe = (Button) findViewById(R.id.join_tribe);
		tribeList = (Button) findViewById(R.id.tribe_list);
		createTribe.setOnClickListener(this);
		joinTribe.setOnClickListener(this);
		tribeList.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_tribe:
			createTribe();
			break;
		case R.id.join_tribe:
			joinTribe();
			break;
		case R.id.tribe_list:
			Intent intent = new Intent(this, TribeListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	/**
	 * 加入群
	 */
	private void joinTribe() {
		final EditText edittext = new EditText(this);
		edittext.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		edittext.setHint("群号（不为空）");
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("请输入要加入的群号").setView(edittext)
				.setPositiveButton("申请加入", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTribeManager.joinTribe(new IWxCallback() {

							@Override
							public void onSuccess(Object... arg0) {
								Notification.showToastMsg(
										getApplicationContext(),
										"加入成功,请到群列表中查看。");
							}

							@Override
							public void onProgress(int arg0) {
							}

							@Override
							public void onError(int arg0, String arg1) {
								Notification.showToastMsg(
										getApplicationContext(), "加入失败:" + arg0
												+ ":" + arg1);
							}
						}, Long.valueOf(edittext.getText().toString()));

					}
				}).setNegativeButton("取消", null).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 创建群
	 */
	@SuppressLint("InflateParams")
	private void createTribe() {
		final View view = LayoutInflater.from(TribeOperationActivity.this)
				.inflate(R.layout.create_tribe, null);
		AlertDialog.Builder builder = new Builder(TribeOperationActivity.this);
		builder.setTitle("输入群信息").setView(view)
				.setPositiveButton("创建", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText name = (EditText) view.findViewById(R.id.name);
						EditText announcement = (EditText) view
								.findViewById(R.id.announcement);
						EditText member = (EditText) view
								.findViewById(R.id.member);
						List<String> memberList = Tools
								.splitToListWithNull(member.getText()
										.toString());
						if (!TextUtils.isEmpty(name.getText().toString())) {
							mTribeManager.createTribe(new IWxCallback() {

								@Override
								public void onError(int arg0, String arg1) {
									Notification.showToastMsg(
											getApplicationContext(), "创建失败:"
													+ arg0 + ":" + arg1);
								}

								@Override
								public void onProgress(int arg0) {
								}

								@Override
								public void onSuccess(Object... arg0) {
									Notification.showToastMsg(
											getApplicationContext(),
											"创建成功,请到群列表中查看。");
								}

							}, name.getText().toString(), announcement
									.getText().toString(), memberList);
						} else {
							Notification.showToastMsg(getApplicationContext(),
									"群名称不为空,创建失败");
						}

					}
				}).setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}
}
