package com.daxiong.fun.function.study.yeartopic.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.model.AnswerListItemGsonModel;

import java.util.ArrayList;

/**
 * 历年真题adapter
 * @author:  sky
 */
public class AnswerListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<AnswerListItemGsonModel> mList;
	private boolean isScrolling;
	
	private int goTag=-1;
	
	public void setGoTag(int goTag){
	    this.goTag=goTag;
	}

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public void setData(ArrayList<AnswerListItemGsonModel> currentList) {
		this.mList = currentList;
		notifyDataSetChanged();
	}

	public AnswerListAdapter(Activity context) {
		super();
		this.mActivity = context;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int postion) {
		return postion;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup container) {
		AnswerListItemView itemView = null;
		if (convertView == null) {
			itemView = new AnswerListItemView(mActivity);
		} else {
			itemView = (AnswerListItemView) convertView;
		}
		if (mList.size() > postion) {
			AnswerListItemGsonModel itemModel = mList.get(postion);
			itemView.showData(itemModel, isScrolling);
			itemView.setGoTag(goTag);
		}
		
		return itemView;
	}
}
