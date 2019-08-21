package com.daxiong.fun.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.function.account.model.PayCardModel;
import com.daxiong.fun.view.ChoiceView;

import java.util.ArrayList;
import java.util.List;

public class MyGridAdapter extends BaseAdapter {
	private List<PayCardModel> list;
	private Activity context;

	public MyGridAdapter(ArrayList<PayCardModel> list, Activity context) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ChoiceView view;
		if (convertView == null) {
			view = new ChoiceView(context);
		} else {
			view = (ChoiceView) convertView;
		}
		view.setText(list.get(position).getProduct());
		return view;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

}
