package com.daxiong.fun.function.communicate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.model.ChatInfo;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

	private Context mContext;
	private List<ChatInfo> mInfos;
	
	public MessageListAdapter(Context context) {
		this.mContext = context;
	}
	
	public void setData(List<ChatInfo> infos) {
		mInfos = infos;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mInfos == null ? 0 : mInfos.size();
		//return mInfos == null || mInfos.size() == 0 ? 1 : mInfos.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		MessageListItemView itemView = null;
		if (convertView == null) {
			itemView = new MessageListItemView(mContext);
		} else {
			itemView = (MessageListItemView) convertView;
		}
		itemView.showData(mInfos.get(position));
		return itemView;
	}

}
