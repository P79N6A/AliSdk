package com.taobao.tae.sdk.openimui;

import java.util.List;

import com.alibaba.mobileim.conversation.YWMessage;
import com.taobao.tae.sdk.demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TribeMsgAdapter extends BaseAdapter{
	private  LayoutInflater mInflater;
	private List<YWMessage> list;
	
	public TribeMsgAdapter(Context context) {
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(list !=null){
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.tribe_message_item,parent,false);
			holder.mMsg = (TextView)convertView.findViewById(R.id.msg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		YWMessage message=list.get(position);
		
		//message.getSubType() 判断消息类型
		if(message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS){
			//MyMessageBody body = (MyMessageBody) message.getMessageBody();
			//holder.mMsg.setText("Content:" + body.getContent() + "  SubContent:" + body.getSubContent());
			//String content = message.getMessageBody().getContent();
			holder.mMsg.setText("群自定义消息");
		}else{
			holder.mMsg.setText(message.getMessageBody().getContent());
		}
		
		return convertView;
	}

	public List<YWMessage> getList() {
		return list;
	}

	public void setList(List<YWMessage> list) {
		this.list = list;
	}
	private final class ViewHolder{
		public TextView mMsg;
	}

}
