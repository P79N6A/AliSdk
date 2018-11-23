package com.taobao.tae.sdk.openimui;

import java.util.List;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.tribe.YWTribeManager;
import com.taobao.tae.sdk.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 群列表
 * 
 * @author peizhi.ypz
 * 
 */
public class TribeListActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private YWTribeManager mTribeManager;
	private TribeListAdapter mAdapter;
	private List<YWTribe> mTribeList;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private IYWTribeChangeListener mTribeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribe_list);
		mTribeManager = OpenWxApi.getInstance().getTribeManager();
		mAdapter = new TribeListAdapter(this);
		mListView = (ListView) this.findViewById(R.id.tribe_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mTribeManager.getAllTribesFromServer(new IWxCallback() {
			@Override
			public void onSuccess(final Object... arg0) {
				mHandler.post(new Runnable() {
					@Override
					@SuppressWarnings("unchecked")
					public void run() {
						mTribeList = (List<YWTribe>) arg0[0];
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onProgress(int arg0) {

			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});

		mTribeListener = new IYWTribeChangeListener() { // 群信息变更事件的侦听

			@Override
			public void onUserRemoved(YWTribe arg0, YWTribeMember arg1) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onUserQuit(YWTribe arg0, YWTribeMember arg1) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});

			}

			@Override
			public void onUserJoin(YWTribe arg0, YWTribeMember arg1) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});

			}

			@Override
			public void onTribeManagerChanged(YWTribe arg0, YWTribeMember arg1) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onTribeInfoUpdated(YWTribe arg0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onTribeDestroyed(YWTribe arg0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onInvite(final YWTribe arg0, YWTribeMember arg1) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTribeList = mTribeManager.getAllTribes();
						mAdapter.setList(mTribeList);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		};

	}

	@Override
	protected void onResume() {
		mTribeList = mTribeManager.getAllTribes();// 本地获得群列表
		mAdapter.setList(mTribeList);
		mAdapter.notifyDataSetChanged();
		mTribeManager.addTribeListener(mTribeListener);
		super.onResume();
	}

	@Override
	protected void onPause() {
		mTribeManager.removeTribeListener(mTribeListener);
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		YWTribe tribe = mTribeList.get(position);
		if (tribe != null) {
			Intent intent = new Intent();
			intent.setClass(this, TribeMsgActivity.class);
			intent.putExtra(TribeMsgActivity.TRIBE_ID, tribe.getTribeId());
			startActivity(intent);
		}

	}

}
