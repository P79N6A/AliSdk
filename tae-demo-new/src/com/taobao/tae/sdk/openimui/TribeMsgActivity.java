package com.taobao.tae.sdk.openimui;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWMessageListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationManager;
import com.alibaba.mobileim.conversation.YWFileManager;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.tribe.YWTribeManager;
import com.taobao.tae.sdk.demo.R;
import com.taobao.tae.sdk.openimui.refreshlist.PullToRefreshBase;
import com.taobao.tae.sdk.openimui.refreshlist.PullToRefreshBase.OnRefreshListener;
import com.taobao.tae.sdk.openimui.refreshlist.PullToRefreshListView;

@SuppressWarnings("unused")
/**
 * 群消息列表
 * @author peizhi.ypz
 *
 */
public class TribeMsgActivity extends Activity implements OnClickListener,
		OnItemLongClickListener {

	/**
	 * 群ID
	 */
	public final static String TRIBE_ID = "tribe_id";
	/**
	 * 访问群详情的请求码：为了在群详情内退出群后，这个Activity也能被Finish掉。
	 */
	public final static int REQUEST_CODE_FOR_DETAIL = 1;

	private long mTribeId;
	private YWTribeManager mTribeManager;
	private YWTribe mTribe;
	private TextView mTitle;
	private TextView mTribeInfo;
	private Button mTextMsg;
	private Button mImageMsg;
	private Button mAudioMsg;
	private Button mGeoMsg;
	private Button mCustomMsg;
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private TribeMsgAdapter mAdapter;
	private YWConversationManager mConversationManager;
	private YWConversation mConversation;
	private List<YWMessage> msgList;
	private YWFileManager mFileManager;

	private int mIndex;
	private YWMessage mMessage;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribe_msg);
		Intent intent = this.getIntent();
		mTribeId = intent.getLongExtra(TRIBE_ID, 0L);
		mFileManager = OpenWxApi.getInstance().getFileManager();
		mAdapter = new TribeMsgAdapter(this);
		mTribeManager = OpenWxApi.getInstance().getTribeManager();
		mConversationManager = OpenWxApi.getInstance().getConversationManager();
		mTribe = mTribeManager.getTribe(mTribeId);

		initView();
		mListView = mPullRefreshListView.getRefreshableView();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemLongClickListener(this);
		mConversation = mConversationManager.getConversationCreater()
				.createTribeConversation(mTribeId);

	}

	private void initTitle() {
		String title = String.format(Locale.CHINA, "%s(%d)",
				mTribe.getTribeName(), mTribeId);
		mTitle.setText(title);
	}

	@Override
	protected void onStart() {
		initTitle();
		loadMessage();
		mTribeManager.addTribeListener(mTribeListener);
		mConversation.getMessageLoader().addMessageListener(mMessageListener);
		mConversationManager.addPushListener(mPushListener);
		super.onStart();
	}

	@Override
	protected void onStop() {
		mConversationManager.removePushListener(mPushListener);
		mConversation.getMessageLoader()
				.removeMessageListener(mMessageListener);
		mTribeManager.removeTribeListener(mTribeListener);
		super.onStop();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.tribe_msg_title);
		mTribeInfo = (TextView) findViewById(R.id.tribe_info);
		mTextMsg = (Button) findViewById(R.id.tribe_msg_text);
		mImageMsg = (Button) findViewById(R.id.tribe_msg_image);
		mAudioMsg = (Button) findViewById(R.id.tribe_msg_audio);
		mGeoMsg = (Button) findViewById(R.id.tribe_msg_geo);
		mCustomMsg = (Button) findViewById(R.id.tribe_msg_custom);
		mTribeInfo.setOnClickListener(this);
		mTextMsg.setOnClickListener(this);
		mImageMsg.setOnClickListener(this);
		mAudioMsg.setOnClickListener(this);
		mGeoMsg.setOnClickListener(this);
		mCustomMsg.setOnClickListener(this);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.tribe_msg_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						loadMoreMessage();
					}
				});

	}

	/**
	 * 加载历史消息记录列表，该方法一般在进入会话页面初始化消息列表时调用，以便用户在打开会话页面时可以看到历史消息。
	 */
	private void loadMessage() {
		mConversation.getMessageLoader().loadMessage(10, new IWxCallback() {

			@Override
			public void onSuccess(final Object... arg0) {
				if (arg0 != null) {
					mHandler.post(new Runnable() {

						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							msgList = (List<YWMessage>) arg0[0];
							mAdapter.setList(msgList);
							mAdapter.notifyDataSetChanged();
						}
					});
				}

			}

			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int arg0, String arg1) {
				Notification.showToastMsg(getApplicationContext(), "加载历史消息失败: "
						+ arg0 + " : " + arg1);
			}
		});
	}

	/**
	 * 加载更多历史消息，用户下拉listview时调用该方法。
	 */
	protected void loadMoreMessage() {
		mConversation.getMessageLoader().loadMoreMessage(10, new IWxCallback() {

			@Override
			public void onSuccess(final Object... arg0) {
				if (arg0 != null) {
					mHandler.post(new Runnable() {
						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							msgList = (List<YWMessage>) arg0[0];
							mAdapter.setList(msgList);
							mAdapter.notifyDataSetChanged();
							mPullRefreshListView.onRefreshComplete();
						}
					});
				}

			}

			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(final int arg0, final String arg1) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Notification.showToastMsg(getApplicationContext(),
								"加载更多消息失败: " + arg0 + " : " + arg1);
						mPullRefreshListView.onRefreshComplete();
					}
				});
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tribe_msg_text:
			textDialog();
			break;
		case R.id.tribe_msg_image:
			imageDialog(Constant.IMAGE_URLS);
			break;
		case R.id.tribe_msg_audio:
			audioDialog(Constant.AUDIO_URLS);
			break;
		case R.id.tribe_msg_geo:
			geoDialog(Constant.GEO_INFO);
			break;
		case R.id.tribe_msg_custom:
			customDialog();
			break;
		case R.id.tribe_info:
			Intent intent = new Intent(this, TribeDetailActivity.class);
			intent.putExtra(TRIBE_ID, mTribeId);
			startActivityForResult(intent, REQUEST_CODE_FOR_DETAIL);
		default:
			break;
		}

	}

	// 发送消息回调
	IWxCallback mSendMsgCallback = new IWxCallback() {

		@Override
		public void onSuccess(Object... arg0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Notification
							.showToastMsg(getApplicationContext(), "消息发送成功");
					mAdapter.notifyDataSetChanged();
				}
			});

		}

		@Override
		public void onProgress(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onError(int arg0, String arg1) {
			Notification.showToastMsg(getApplicationContext(), "消息发送失败" + arg0
					+ " : " + arg1);

		}
	};

	/**
	 * 发送文本消息
	 */
	private void textDialog() {
		final EditText msg = new EditText(this);
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请输入文本消息")
				.setView(msg)
				.setPositiveButton("发送", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (msg.getText().toString() != null) {
							// 创建消息，发送消息前要先根据消息类型(文本，图片，语音)创建一个YWMessage实例，用于消息发送。
							mMessage = YWMessageChannel.createTextMessage(msg
									.getText().toString());
							// 发送消息
							mConversation.getMessageSender().sendMessage(
									mMessage, Constant.TIMEOUT,
									mSendMsgCallback);
						}
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 发送文本消息
	 */
	private void customDialog() {
		final View view = View.inflate(this, R.layout.custom_message, null);
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请输入文本消息")
				.setView(view)
				.setPositiveButton("发送", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String content = ((EditText) view
								.findViewById(R.id.content)).getText()
								.toString();
						String subContent = ((EditText) view
								.findViewById(R.id.subContent)).getText()
								.toString();
						MyMessageBody body = new MyMessageBody();
						body.setContent(content);
						body.setSubContent(subContent);
						
						JSONObject object = new JSONObject();
						JSONObject head = new JSONObject();
						try {
							head.put("summary", subContent);
							object.put("customize", content);
							object.put("header", head);
							body.setContent(object.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						
						mMessage = YWMessageChannel.createTribeCustomMessage(body);
						// 发送消息
						mConversation.getMessageSender().sendMessage(mMessage,
								Constant.TIMEOUT, mSendMsgCallback);
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 发送图片消息
	 */
	private void imageDialog(final String[] items) {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("对话框")
				.setSingleChoiceItems(items, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface alertDialog,
									int index) {
								mIndex = index;
							}
						})
				.setPositiveButton("发送", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int index) {
						if (mIndex != -1) {
							final String url = items[mIndex];
							final String fileName = System.currentTimeMillis()
									+ ".jpg";
							// 根据url下载网络图片到本地
							mFileManager.downloadFile(url, Constant.STORE_PATH,
									fileName, new IWxCallback() {

										@Override
										public void onSuccess(Object... arg0) {
											String filePath = Constant.STORE_PATH
													+ fileName;
											Bitmap bitmap = BitmapFactory
													.decodeFile(filePath);
											int width = bitmap.getWidth();
											int height = bitmap.getHeight();
											int size = bitmap.getRowBytes() * bitmap.getHeight();
											// 图片下载成功后，将图片的本地地址传递给createImageMessag生成一个图片消息对象。
											// 如果用户要发送的图片消息本来就在本地，则不需要执行下载操作，直接将本地地址传递给createImageMessag即可。
											mMessage = YWMessageChannel
													.createImageMessag(
															filePath, url,
															width, height,
															size, "jpg");
											mHandler.post(new Runnable() {
												@Override
												public void run() {
													// 发送消息，该操作必须在UI线程执行。
													mConversation
															.getMessageSender()
															.sendMessage(
																	mMessage,
																	Constant.TIMEOUT,
																	mSendMsgCallback);

												}
											});
										}

										@Override
										public void onProgress(int arg0) {
										}

										@Override
										public void onError(int arg0,
												String arg1) {

										}
									});
						}
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mMessage = null;
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * @param items
	 *            发送位置消息
	 */
	private void geoDialog(final String[] items) {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("对话框")
				.setSingleChoiceItems(items, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface alertDialog,
									int index) {
								mIndex = index;
							}
						})
				.setPositiveButton("发送", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int index) {
						if (mIndex != -1) {
							final String info = items[mIndex];
							String[] infos = items[mIndex].split(",");
							double latitude = Double.valueOf(infos[0]);
							double longtitude = Double.valueOf(infos[1]);
							String address = infos[2];
							mMessage = YWMessageChannel.createGeoMessage(   //经纬度和地址 创建一个地理位置消息
									latitude, longtitude, address);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									// 发送消息，该方法必须在UI线程执行
									mConversation.getMessageSender()
											.sendMessage(mMessage,
													Constant.TIMEOUT,
													mSendMsgCallback);
								}
							});
						}
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mMessage = null;
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * @param items
	 *            发送语音消息
	 */
	private void audioDialog(final String[] items) {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("对话框")
				.setSingleChoiceItems(items, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface alertDialog,
									int index) {
								mIndex = index;
							}
						})
				.setPositiveButton("发送", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int index) {
						if (mIndex != -1) {
							final String url = items[mIndex];
							final String fileName = System.currentTimeMillis()
									+ ".amr";
							// 根据url下载语音消息到本地
							mFileManager.downloadFile(url, Constant.STORE_PATH,
									fileName, new IWxCallback() {
										@Override
										public void onSuccess(Object... objects) {
											String filePath = Constant.STORE_PATH
													+ fileName;
											File file = new File(filePath);
											int size = (int) file.length();
											// 语音时长，此处为了简单起见，直接将语音时长设置为2。
											// 但是，用户在发送语音消息时需要根据具体的语音消息获取时长并传递给createAudioMessage。
											int duration = 2;
											// 语音下载成功后，将语音的本地地址传递给createAudioMessage生成一个图片消息对象。
											// 如果用户要发送的语音消息本来就在本地，则不需要执行下载操作，直接将本地地址传递给createAudioMessage即可。
											mMessage = YWMessageChannel
													.createAudioMessage(
															filePath, duration,
															size, "amr");
											mHandler.post(new Runnable() {
												@Override
												public void run() {
													// 发送消息，该方法必须在UI线程执行
													mConversation
															.getMessageSender()
															.sendMessage(
																	mMessage,
																	Constant.TIMEOUT,
																	mSendMsgCallback);
												}
											});
										}

										@Override
										public void onError(int arg0,
												String arg1) {

										}

										@Override
										public void onProgress(int arg0) {

										}
									});
						}
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mMessage = null;
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_FOR_DETAIL) {//群详情内退出群
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final YWMessage message = msgList.get(position - 1);
		AlertDialog dialog = new AlertDialog.Builder(TribeMsgActivity.this)
				.setTitle("删除该消息？")
				.setPositiveButton("确定", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mConversation.getMessageLoader().deleteMessage(message);
						mAdapter.notifyDataSetChanged();
					}
				}).setNegativeButton("取消", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
		return true;
	}

	private IYWTribeChangeListener mTribeListener = new IYWTribeChangeListener() {

		@Override
		public void onUserRemoved(YWTribe arg0, YWTribeMember arg1) {
			if (arg0.getTribeId() == mTribeId
					&& ((YWTribeMember)arg1).getUserId().equalsIgnoreCase(
							OpenWxApi.getInstance().getCurrentId())) { // 自己被踢了。 Activity Finish掉。
				Notification.showToastMsg(getApplicationContext(), "您已被踢出群:"+arg0.getTribeName());
				finish();
			}
		}

		@Override
		public void onUserQuit(YWTribe arg0, YWTribeMember arg1) {

		}

		@Override
		public void onUserJoin(YWTribe arg0, YWTribeMember arg1) {

		}

		@Override
		public void onTribeManagerChanged(YWTribe arg0, YWTribeMember arg1) {

		}

		@Override
		public void onTribeInfoUpdated(YWTribe arg0) {

		}

		@Override
		public void onTribeDestroyed(YWTribe arg0) { //群被解散了，Activity Finish掉。
			if (arg0.getTribeId() == mTribeId) {
				finish();
			}

		}

		@Override
		public void onInvite(YWTribe arg0, YWTribeMember arg1) {

		}
	};

	private IYWPushListener mPushListener = new IYWPushListener() {

		@Override
		public void onPushMessage(final IYWContact paramIYWContact,
				final YWMessage paramYWMessage) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (TextUtils.equals(paramIYWContact.getUserId(), OpenWxApi
							.getInstance().getCurrConversationId())) {
						Notification.showNotification(TribeMsgActivity.this,
								paramYWMessage, null);
					}
				}
			});
		}

		@Override
		public void onPushMessage(final YWTribe paramIYWTribe,
				final YWMessage paramYWMessage) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (paramIYWTribe.getTribeId() == mTribeId) {
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
					} else {
						Notification.showNotification(TribeMsgActivity.this,
								null, paramYWMessage);
					}
				}
			});
		}
	};

	private IYWMessageListener mMessageListener = new IYWMessageListener() {
		/*
		 * 消息内容变更通知，该回调被触发时表示当前会话的消息列表发生了变化，应该在该方法内执行刷新消息列表相关操作。
		 */
		@Override
		public void onItemUpdated() {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
				}
			});
		}

		/*
		 * 新消息通知，该回调被触发时表示当前会话收到了一条新的消息，应该在该方法内执行刷新消息列表相关操作。
		 */
		@Override
		public void onItemComing() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
				}
			});
		}

		@Override
		public void onInputStatus(byte arg0) {
			// TODO Auto-generated method stub
			
		}
	};

}
