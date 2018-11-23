package com.taobao.tae.sdk.openimui;

import java.util.List;

import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.taobao.tae.sdk.demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TribeMemberAdapter extends BaseAdapter {
	List<YWTribeMember> list;
	protected LayoutInflater mInflater;

	public TribeMemberAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.tribe_member_list_item, parent,false);
			holder.mUserId = (TextView) convertView.findViewById(R.id.name);
			holder.mRole = (TextView) convertView.findViewById(R.id.role);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		YWTribeMember contact = list.get(position);
		holder.mUserId.setText(contact.getUserId());

		if(contact.getTribeRole()==YWTribeMember.ROLE_HOST){
			holder.mRole.setText("群主");
		}else if(contact.getTribeRole()==YWTribeMember.ROLE_MANAGER){
			holder.mRole.setText("管理员");
		}else if(contact.getTribeRole()==YWTribeMember.ROLE_NORMAL){
			holder.mRole.setText("");
		}
		return convertView;
	}

	private final class ViewHolder {
		public TextView mUserId;
		public TextView mRole;
	}

	public List<YWTribeMember> getList() {
		return list;
	}

	public void setList(List<YWTribeMember> list) {
		this.list = list;
	}
}
