package com.taobao.tae.sdk.openimui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWConversationItemClickListener;
import com.alibaba.mobileim.IYWUIPushListener;
import com.alibaba.mobileim.YWAccount;
import com.alibaba.mobileim.YWAccountType;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWUIAPI;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationBody;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.login.YWLoginState;
import com.taobao.tae.sdk.TaeSDK;
import com.taobao.tae.sdk.TopComponent;
import com.taobao.tae.sdk.api.IOpenWxUiApi;
import com.taobao.tae.sdk.callback.LoginCallback;
import com.taobao.tae.sdk.callback.TradeProcessCallback;
import com.taobao.tae.sdk.constant.ResultCode;
import com.taobao.tae.sdk.demo.R;
import com.taobao.tae.sdk.model.Result;
import com.taobao.tae.sdk.model.Session;
import com.taobao.tae.sdk.model.TradeResult;

public class OpenImUiActivity extends AbstractActivity implements IYWConversationItemClickListener{

	private TextView mTextView;
	private YWUIAPI mUIApi;
	private YWAccount mAccount;
	private Handler mHandler = new Handler();
	private int iconId = R.drawable.ic_launcher;
	private int clientType = InitialActivity.OPEN_CLIENT;
//	private SharedPreferences preferences;

	// 账号：testpro1~100

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_openim_ui);
//		preferences=getSharedPreferences(OpenimInfoInitActivity.LOCAL_STORE_NAME, Context.MODE_PRIVATE);
		mTextView = (TextView) findViewById(R.id.unread);
//		clientType = getIntent().getIntExtra("CLIENT", clientType);
		((EditText)findViewById(R.id.userid)).setText("testpro1");
		((EditText)findViewById(R.id.password)).setText("taobao1234");
		((EditText)findViewById(R.id.target)).setText("testpro2");
		initAppKeys();
		initAccount();
//		mTextView.post(new Runnable() {
//
//			@Override
//			public void run() {
//				chooseIconDialog();
//			}
//		});
	}
	
	public void initInfos(View v){
		startActivity(new Intent(this, OpenimInfoInitActivity.class));
	}
	public void initAppKeys(){
//		String str = ((EditText)findViewById(R.id.appKey)).getText().toString();
//		String str=preferences.getString(OpenimInfoInitActivity.APPKEYS, "23015524 4272");
//		if (str == null) {
//			Notification.showToastMsg(OpenImUiActivity.this, "请输入appKeys");
//			return;
//		}
		
		
		
		//以下代码被照虚移到ApplicationOncreate中初始化 20150817
//		
//		String str = "23015524";
//		List<String> targetAppkeys = Tools.splitToList(str);
//		// 初始化接收消息的对象appkeys
//		YWChannel.prepare(this.getApplication(),"23015524");
//		YWChannel.prepareTargetAppKeys(targetAppkeys);
		
	}

	private void initAccount() {
		OpenWxApi.setClientType(clientType);
		mUIApi = OpenWxApi.getInstance().getUIApi();
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "尚未初始化 ", Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}
		mUIApi.registerPushListener(mPushListener);
		mUIApi.setResId(iconId);
		mUIApi.registerConversationClickListener(this);
		mAccount = mUIApi.getAccount();

	}

	private IYWUIPushListener mPushListener = new IYWUIPushListener() {

		@Override
		public void onMessageComing() {
			int count = mUIApi.getUnreadCount();
			mTextView.setText(String.valueOf(count));
		}

	};

//	public void openWwConversationTab(View view) {
//		if (mUIApi == null) {
//			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Intent intent = new Intent(this, ConversationActivityWithTab.class);
//		startActivity(intent);
//	}

	public void openWwConversation(View view) {
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = mUIApi.getConversationActivityIntent();
		intent.putExtra(YWAccountType.class.getSimpleName(), YWAccountType.open.getValue());
		startActivity(intent);
	}

	public void openWwConversationFragment(View view) {
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, ConversationActivityWithFragment.class);
		startActivity(intent);
	}

	public void openWwChat(View view) {
		String target = ((EditText) findViewById(R.id.target)).getText()
				.toString();
//		String target =preferences.getString(OpenimInfoInitActivity.TRAGET_ID, "yaochen002");
		if (TextUtils.isEmpty(target)) {
			Toast.makeText(OpenImUiActivity.this, "请输入聊天对象id",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
//		target=preferences.getString(OpenimInfoInitActivity.TRAGET_ID, "yaochen002");
//		String targetKey = preferences.getString(OpenimInfoInitActivity.TARGET_APPKEY, "23015524");
		Toast.makeText(OpenImUiActivity.this, "聊天对象id: " + target,
				Toast.LENGTH_SHORT).show();
		Map<String, ContactInfo> mContact = ContactFactory.getContacts();
		if (!mContact.containsKey(target)) {
			showDialog(target);
		} else {
			Intent intent = mUIApi.getChattingActivityIntent(target);
			startActivity(intent);
		}
	}
	
	
	
	private boolean defaultPrepare() {
		if (mUIApi == null) {
			Notification.showToastMsg(OpenImUiActivity.this, "appKey 未初始化！自动帮您做了初始化");
			initAppKeys();
			return false;
		}
		if (mAccount.getLoginState() != YWLoginState.success) {
			Notification.showToastMsg(OpenImUiActivity.this, "您还没有登录，请先登录！自动帮您做了登录");
			login(null);
			return false;
		}
		return true;
	}
	
	public void openTribeTest(View view){
		
		if(!this.defaultPrepare()) return;
		
		if (mAccount.getLoginState() != YWLoginState.success) {
			Notification.showToastMsg(OpenImUiActivity.this, "您还没有登录，请先登录！");
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), TribeOperationActivity.class);
		intent.putExtra("CLIENT", clientType);
		startActivity(intent);
	}

	public void logout(View view) {
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mAccount.logout(new IWxCallback() {

			@Override
			public void onSuccess(Object... result) {
				OpenWxApi.getInstance().setCurrentUserId(null);
				Toast.makeText(OpenImUiActivity.this, "登出成功",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int code, String info) {
				Toast.makeText(OpenImUiActivity.this,
						"登出失败，code = " + code + ", info = " + info,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void taeLogin(View view) {
		TaeSDK.showLogin(this, new LoginCallback() {

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(OpenImUiActivity.this, "登陆失败" + arg0,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(Session arg0) {
				Toast.makeText(OpenImUiActivity.this, "登陆成功",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void login(View view) {
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		final String userId = ((EditText) findViewById(R.id.userid)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.password)).getText()
				.toString();
//		final String userId=preferences.getString(OpenimInfoInitActivity.USERID, "testpro1");
//		String password=preferences.getString(OpenimInfoInitActivity.PASSWORD,"taobao1234");
		// WMOpenUIApi.getInstance().loginAccount("3245", userId, password, 0,
		mAccount.login(userId, password, 120*1000, new IWxCallback() {

			@Override
			public void onSuccess(Object... result) {
				Toast.makeText(OpenImUiActivity.this, "登陆成功",
						Toast.LENGTH_SHORT).show();
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						OpenWxApi.getInstance().setCurrentUserId(userId);
						int count = mUIApi.getUnreadCount();
						mTextView.setText(String.valueOf(count));
					}
				});
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(final int code, String info) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(OpenImUiActivity.this, "登陆失败" + code,
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

	public void showLoginState(View view) {
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		YWLoginState state;
		state = mAccount.getLoginState();
		Toast.makeText(OpenImUiActivity.this, "当前登录状态：" + state,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		if (mUIApi != null) {
			int count = mUIApi.getUnreadCount();
			mTextView.setText(String.valueOf(count));
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mUIApi == null) {
			Toast.makeText(OpenImUiActivity.this, "TAE 未初始化",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mUIApi.unRegisterPushListener(mPushListener);
	}

	public void showPage(View view) {
		TaeSDK.showPage(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(
						OpenImUiActivity.this,
						"支付成功" + tradeResult.paySuccessOrders + "   "
								+ tradeResult.payFailedOrders,
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(OpenImUiActivity.this, "确认交易订单失败",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(OpenImUiActivity.this,
							"交易异常 code: " + code + " message" + msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		}, null, "http://h5.wapa.taobao.com/cm/snap/index.html?id=38466679236");
	}

	public void getWwToken(View view) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Map<String, String> m = new HashMap<String, String>();
				m.put("method", "alibaba.cdo.hsf.zoneid.check");
				m.put("auth", "168887f343302fb8be66036c831cd603");
				m.put("zone_id", "172703");
				m.put("mobile", "15669998383");
				final Result<String> result = TopComponent.getInstance()
						.invoke(m);
				OpenImUiActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(OpenImUiActivity.this, "" + result.data,
								Toast.LENGTH_SHORT).show();
					}

				});
			}
		}).start();
	}

	private void showDialog(final String targetId) {
		final Context context = OpenImUiActivity.this;
		final View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_layout, null);
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("添加新用户")
				.setMessage("未找到该用户，请添加用户信息。")
				.setView(view)
				.setPositiveButton("添加", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText avatarPath = (EditText) view
								.findViewById(R.id.avatar_path);
						EditText nickName = (EditText) view
								.findViewById(R.id.nickName);
						if (avatarPath != null && nickName != null) {
							ContactInfo info = new ContactInfo();
							info.setAvatarPath(avatarPath.getText().toString());
							info.setNickName(nickName.getText().toString());
							ContactFactory.getContacts().put(targetId, info);
							Intent intent = mUIApi
									.getChattingActivityIntent(targetId);
							startActivity(intent);
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private void chooseIconDialog() {
		AlertDialog dialog = new AlertDialog.Builder(OpenImUiActivity.this)
				.setTitle("选择通知栏图标")
				.setSingleChoiceItems(new String[] { "图标1", "图标2" }, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int which) {
								iconId = R.drawable.ic_launcher;
								if (which == 0) {
									iconId = R.drawable.aliwx_s002;
								} else if (which == 1) {
									iconId = R.drawable.aliwx_s001;
								}
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						initAccount();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						initAccount();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	// private void chooseAppkeyDialog() {
	// AlertDialog dialog = new AlertDialog.Builder(OpenImUiActivity.this)
	// .setTitle("选择appkey")
	// .setSingleChoiceItems(new String[] { "23015524", "23080858" },
	// 0, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface arg0, int which) {
	// mIndex = which;
	// appkey = "23015524";
	// if (mIndex == 1) {
	// appkey = "23080858";
	// }
	// }
	// })
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// chooseIconDialog();
	// }
	// })
	// .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// chooseIconDialog();
	// }
	// }).create();
	// if (!dialog.isShowing()) {
	// dialog.show();
	// }
	// }

	public void sendGeo(View view) {
		// IWMMessage message = WMMessageFactory
		// .createGeoMessage(30.280028, 120.026722,
		// "浙江省杭州市余杭区文一西路靠近阿里巴巴西溪园区5号楼");
		// WMOpenAPI
		// .getInstance()
		// .getConversationManager()
		// .getConversation(
		// ((EditText) findViewById(R.id.edittext)).getText()
		// .toString())
		// .sendMessage(message, 120, null);
	}

	//
	public void sendGif(View view) {
		// IWMMessage message = WMMessageFactory
		// .createTBGifMessage("http://download.taobaocdn.com/tbsc/client/taotoy/008.gif?type=1&suffix=gif&width=80&height=80&fileId=T1Dta5XgVeXXXXXXXX.gif");
		// WMOpenAPI
		// .getInstance()
		// .getConversationManager().
		// getConversation(
		// ((EditText) findViewById(R.id.edittext)).getText()
		// .toString())
		// .sendMessage(message, 120, null);
	}

	@Override
	public boolean onItemClick(Object obj) {
		if (((YWConversation)obj).getConversationType() == YWConversationType.Tribe) {
			OpenWxApi.getInstance().getConversationManager().markReaded((YWConversation)obj);
			YWConversationBody body = ((YWConversation)obj).getConversationBody();
			YWTribe tribe = ((YWTribeConversationBody)body).getTribe();
			if (tribe != null) {
				Intent intent = new Intent();
				intent.setClass(this, TribeMsgActivity.class);
				intent.putExtra(TribeMsgActivity.TRIBE_ID, tribe.getTribeId());
				startActivity(intent);
			}
			return true;
		}
		return false;
	}

}
