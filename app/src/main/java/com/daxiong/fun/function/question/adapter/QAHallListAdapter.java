package com.daxiong.fun.function.question.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.function.QAHallListItemView;
import com.daxiong.fun.function.question.model.AnswerListItemModel;

import java.util.ArrayList;

/**
 * 问题大厅adapter
 * @author:  sky
 */
public class QAHallListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<AnswerListItemModel> mList;

	private boolean isScrolling;
	
    private int  goTag=-1;
    
    public void setGoTag(int goTag){
        this.goTag=goTag;
    }
	

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public void setData(ArrayList<AnswerListItemModel> currentList) {
		this.mList = currentList;
		notifyDataSetChanged();
	}

	public QAHallListAdapter(Activity context) {
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
		QAHallListItemView itemView = null;
		if (convertView == null) {
			itemView = new QAHallListItemView(mActivity);
		} else {
			itemView = (QAHallListItemView) convertView;
		}
		if (mList.size() > postion) {
			AnswerListItemModel itemModel = mList.get(postion);
			itemView.showData(itemModel, isScrolling);
			itemView.setGoTag(goTag);
		}
		return itemView;
	}
}
