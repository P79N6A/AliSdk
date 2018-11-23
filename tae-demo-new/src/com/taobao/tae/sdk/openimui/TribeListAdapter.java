package com.taobao.tae.sdk.openimui;

import java.util.List;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.taobao.tae.sdk.demo.R;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TribeListAdapter extends BaseAdapter {
	private List<YWTribe> list;
	protected LayoutInflater mInflater;
	protected Handler mHandler = new Handler(Looper.getMainLooper());
	public TribeListAdapter(Context context) {
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(list != null){
			return list.size();
		}
		return 0;
	}

	public Object getItem(int position) {
		return position;
	};

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.tribe_list_item, parent,false);
			holder.mTribeName = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		YWTribe tribe = list.get(position);
		if (tribe != null) {
			holder.mTribeName.setText(tribe.getTribeName());
		}
		return convertView;
	}

	public final class ViewHolder {
		public TextView mTribeName;
	}
	
	public List<YWTribe> getList() {
		return list;
	}

	public void setList(List<YWTribe> list) {
		this.list = list;
	}
}
