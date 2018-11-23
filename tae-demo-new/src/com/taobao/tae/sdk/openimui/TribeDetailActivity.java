package com.taobao.tae.sdk.openimui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.tribe.YWTribeManager;
import com.taobao.tae.sdk.demo.R;

/**
 * 群详情
 * 
 * @author peizhi.ypz
 * 
 */
public class TribeDetailActivity extends Activity implements OnClickListener,
		OnItemLongClickListener {

	private TextView groupName;
	private TextView groupAnnouncement;
	private ListView listview;
	private Button modifyTribeInfo;
	private Button inviteNewMember;
	private Button exitGroup;

	private long mTribeId;
	private YWTribeManager mTribeManager;
	private YWTribe mTribe;
	private TribeMemberAdapter mAdapter;
	private List<YWTribeMember> mMemberList;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private IYWTribeChangeListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribe_detail);
		mTribeId = getIntent().getLongExtra(TribeMsgActivity.TRIBE_ID, 0l);
		mTribeManager = OpenWxApi.getInstance().getTribeManager();
		mTribeManager.getTribeFromServer(new IWxCallback() { // 从服务器获取群信息

					@Override
					public void onSuccess(final Object... result) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								mTribe = (YWTribe) result[0];
								initData();
							}
						});
					}

					@Override
					public void onProgress(int progress) {

					}

					@Override
					public void onError(int code, String info) {

					}
				}, mTribeId);

		mAdapter = new TribeMemberAdapter(this);

		initView();

		getTribeMemberListFromServer();

		initListener();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		groupName = (TextView) findViewById(R.id.group_name_info);
		groupAnnouncement = (TextView) findViewById(R.id.group_announcement_info);
		listview = (ListView) findViewById(R.id.group_member_list);
		exitGroup = (Button) findViewById(R.id.exit_group);
		modifyTribeInfo = (Button) findViewById(R.id.modify_tribe_info);
		inviteNewMember = (Button) findViewById(R.id.invite_members);
		modifyTribeInfo.setOnClickListener(this);
		inviteNewMember.setOnClickListener(this);
		exitGroup.setOnClickListener(this);
		listview.setAdapter(mAdapter);
		listview.setOnItemLongClickListener(this);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		groupName.setText(mTribe.getTribeName());
		groupAnnouncement.setText(mTribe.getTribeNotice());
	}

	/**
	 * 初始化侦听
	 */
	private void initListener() {
		mListener = new IYWTribeChangeListener() {

			@Override
			public void onUserRemoved(final YWTribe arg0, final YWTribeMember arg1) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (((YWTribeMember)arg1).getUserId().equalsIgnoreCase(
									OpenWxApi.getInstance().getCurrentId())) {
								Notification.showToastMsg(getApplicationContext(), "您已被踢出群:"+arg0.getTribeName());
								setResult(Activity.RESULT_OK);
								finish();
							} else {
								getTribeMemberListFromServer();
							}
						}
					});
				}
			}

			@Override
			public void onUserQuit(YWTribe arg0, YWTribeMember arg1) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							getTribeMemberListFromServer();
						}
					});
				}
			}

			@Override
			public void onUserJoin(YWTribe arg0, YWTribeMember arg1) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							getTribeMemberListFromServer();
						}
					});
				}

			}

			@Override
			public void onTribeManagerChanged(YWTribe arg0, YWTribeMember arg1) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							getTribeMemberListFromServer();
						}
					});
				}
			}

			@Override
			public void onTribeInfoUpdated(YWTribe arg0) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mTribe = mTribeManager.getTribe(mTribeId);
							initData();
						}
					});
				}

			}

			@Override
			public void onTribeDestroyed(YWTribe arg0) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							setResult(Activity.RESULT_OK);
							finish();
						}
					});
				}

			}

			@Override
			public void onInvite(YWTribe arg0, YWTribeMember arg1) {
				if (arg0.getTribeId() == mTribeId) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							getTribeMemberListFromServer();
						}
					});
				}
			}
		};
	}

	/**
	 * 从服务器获取群成员列表
	 */
	private void getTribeMemberListFromServer() {
		mTribeManager.getMembersFromServer(new IWxCallback() {

			@Override
			public void onSuccess(final Object... result) {
				if (result != null) {
					mHandler.post(new Runnable() {

						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							mMemberList = (List<YWTribeMember>) result[0];
							mAdapter.setList(mMemberList);
							mAdapter.notifyDataSetChanged();
						}
					});
				}
			}

			@Override
			public void onProgress(int progress) {
			}

			@Override
			public void onError(int code, String info) {
			}
		}, mTribeId);
	}

	@Override
	protected void onStart() {
		mTribeManager.addTribeListener(mListener);
		super.onStart();
	}

	@Override
	protected void onStop() {
		mTribeManager.removeTribeListener(mListener);
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_tribe_info:
			modifyDialog();
			break;
		case R.id.invite_members:
			inviteDialog();
			break;
		case R.id.exit_group:
			exitGroup();
			break;
		default:
			break;
		}

	}

	/**
	 * 退出群
	 */
	private void exitGroup() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("退出群？")
				.setPositiveButton("退出", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTribeManager.exitFromTribe(new IWxCallback() {

							@Override
							public void onSuccess(final Object... result) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										Notification
												.showToastMsg(
														getApplicationContext(),
														"退出成功");
										setResult(Activity.RESULT_OK);
										finish();
									}
								});

							}

							@Override
							public void onProgress(int progress) {
							}

							@Override
							public void onError(int code, String info) {
								Notification.showToastMsg(
										getApplicationContext(), "退出失败" + code
												+ ":" + info);

							}
						}, mTribeId);

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
	 * 邀请新成员加入
	 */
	private void inviteDialog() {
		final EditText msg = new EditText(this);
		msg.setHint("多个用户之间以空格隔开");
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("请输入要邀请的用户ID:")
				.setView(msg)
				.setPositiveButton("邀请",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (msg.getText().toString() != null) {
									String invite = msg.getText().toString();
									List<String> inviteIds = Tools
											.splitToListWithNull(invite);
									mTribeManager.inviteMembers(
											new IWxCallback() {
												@Override
												public void onSuccess(
														Object... args) {
													mHandler.post(new Runnable() {

														@Override
														public void run() {
															Notification
																	.showToastMsg(
																			getApplicationContext(),
																			"邀请成功");
															getTribeMemberListFromServer();
														}
													});
												}

												@Override
												public void onError(int code,
														String info) {
													Notification
															.showToastMsg(
																	getApplicationContext(),
																	"邀请失败 "
																			+ code
																			+ ":"
																			+ info);
												}

												@Override
												public void onProgress(
														int progress) {
												}
											}, mTribeId, inviteIds);
								}
							}
						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 修改群信息
	 */
	@SuppressLint("InflateParams")
	private void modifyDialog() {
		final View view = LayoutInflater.from(this).inflate(
				R.layout.modify_tribe_info, null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("输入要修改的信息:").setView(view)
				.setPositiveButton("修改", new Dialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText name = (EditText) view.findViewById(R.id.name);
						EditText announcement = (EditText) view
								.findViewById(R.id.announcement);
						
						if (!TextUtils.isEmpty(name.getText().toString()) //群名称和公告不能同时为空
								|| !TextUtils.isEmpty(announcement.getText()
										.toString())) {

							mTribeManager.modifyTribeInfo(
									new IWxCallback() {

										@Override
										public void onError(int arg0,
												String arg1) {
											Notification
													.showToastMsg(
															getApplicationContext(),
															"修改失败:" + arg0
																	+ ":"
																	+ arg1);
										}

										@Override
										public void onProgress(int arg0) {
										}

										@Override
										public void onSuccess(
												final Object... arg0) {
											if (arg0 != null) {
												mHandler.post(new Runnable() {

													@Override
													public void run() {
														mTribe = (YWTribe) arg0[0];
														initData();
														Notification
																.showToastMsg(
																		getApplicationContext(),
																		"修改成功");
													}
												});
											}
										}

									}, mTribeId, name.getText().toString(),
									announcement.getText().toString());

						} else {
							Notification.showToastMsg(getApplicationContext(),
									"不能全为空哦");
						}
					}
				}).setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		if (!dialog.isShowing()) {
			dialog.show();
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 简单的权限管理，非群主不能踢人
		if (!isHostOfGroup(OpenWxApi.getInstance().getCurrentId())) {
			return true;
		}
		final YWTribeMember contact = mMemberList.get(position);
		if (contact.getUserId().equalsIgnoreCase(
				OpenWxApi.getInstance().getCurrentId())) { // 自己想踢自己相当于退群，这里没必要再做一套逻辑。
			return true;
		}
		String[] items = new String[] { contact.getShowName() };
		if (contact != null) {
			AlertDialog dialog = new AlertDialog.Builder(this).setTitle("踢出")
					.setItems(items, new Dialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mTribeManager.expelMember(new IWxCallback() {

								@Override
								public void onSuccess(Object... result) {
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											mMemberList.remove(contact);
											mAdapter.notifyDataSetChanged();
											Notification.showToastMsg(
													getApplicationContext(),
													"踢出成功");
										}
									});
								}

								@Override
								public void onProgress(int progress) {
								}

								@Override
								public void onError(int code, String info) {
									Notification.showToastMsg(
											getApplicationContext(),
											"踢出失败     " + code + ":" + info);
								}
							}, mTribeId, contact.getUserId());

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
		return true;
	}

	/**
	 * 判断是不是群主.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isHostOfGroup(String userId) {
		if (mMemberList == null || mMemberList.isEmpty()) {
			return false;
		} else {
			for (YWTribeMember member : mMemberList) {
				if (member.getUserId().equalsIgnoreCase(userId)
						&& member.getTribeRole() == YWTribeMember.ROLE_HOST) {
					return true;
				} else {
					continue;
				}
			}
		}
		return false;
	}

}
