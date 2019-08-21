package com.daxiong.fun.function.homework.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.function.homework.view.HomeWorkHallItemCommonView;

import java.util.ArrayList;

public class HomeWrokHallAdapter extends BaseAdapter {
	private ArrayList<HomeWorkModel> mHomeWorkModelList;
	private boolean isScrolling;
	private Context mContext;
	private int packtype;

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}
	public HomeWrokHallAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public void setData(ArrayList<HomeWorkModel> mHomeWorkModelList ,int packtype) {
		this.mHomeWorkModelList = mHomeWorkModelList;
		this.packtype = packtype;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mHomeWorkModelList == null ? 0 : mHomeWorkModelList.size();
	}

	@Override
	public HomeWorkModel getItem(int arg0) {
		return mHomeWorkModelList == null ? null : mHomeWorkModelList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		HomeWorkHallItemCommonView view;
		if (convertView == null) {
			view = new HomeWorkHallItemCommonView(mContext);
		}else {
			view = (HomeWorkHallItemCommonView) convertView;
		}
		int size = 0;
		if (mHomeWorkModelList != null) {
			size = mHomeWorkModelList.size();
		}
		if (position < size) {
			HomeWorkModel homeWorkModel = mHomeWorkModelList.get(position);
			if (homeWorkModel != null) {
				view.showData(homeWorkModel , packtype);
			}
		}
		
		return view;
	}

}
