package com.daxiong.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.communicate.ChatMsgRecvView;
import com.daxiong.fun.function.communicate.ChatMsgSendView;
import com.daxiong.fun.model.ChatInfo;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

	private List<ChatInfo> mInfos;
	private Context mContext;

	public ChatListAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<ChatInfo> infos) {
		this.mInfos = infos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mInfos == null ? 0 : mInfos.size();
	}

	@Override
	public int getItemViewType(int position) {
		return mInfos.get(position).getType();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		int type = getItemViewType(position);
		if (convertView == null) {
			if (GlobalContant.MSG_TYPE_SEND == type) {// 发送消息
				convertView = new ChatMsgSendView(mContext);
			} else if (GlobalContant.MSG_TYPE_RECV == type) {
				convertView = new ChatMsgRecvView(mContext);
			}
		}
		if (mInfos.size() > position) {
			if (GlobalContant.MSG_TYPE_SEND == type && convertView instanceof ChatMsgSendView) {// 发送消息
				((ChatMsgSendView) convertView).showData(mInfos.get(position), mInfos, position);
			} else if (GlobalContant.MSG_TYPE_RECV == type && convertView instanceof ChatMsgRecvView) {
				((ChatMsgRecvView) convertView).showData(mInfos.get(position), mInfos, position);
			}
		}
		return convertView;
	}
}
