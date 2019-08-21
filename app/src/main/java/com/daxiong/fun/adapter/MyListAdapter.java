package com.daxiong.fun.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.model.ExplainfeedbackreasonsModel;
import com.daxiong.fun.view.ChoiceView2;

import java.util.List;

public class MyListAdapter extends BaseAdapter {
	private List<ExplainfeedbackreasonsModel> list;
	private Activity context;
	private boolean falg;
	

	public MyListAdapter(List<ExplainfeedbackreasonsModel> list, Activity context, boolean falg) {
		this.context = context;
		this.list = list;
		this.falg =falg;
		
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

		final ChoiceView2 view;
		if (convertView == null) {
			view = new ChoiceView2(context);
		} else {
			view = (ChoiceView2) convertView;
		}
		view.setClickable(falg);
		view.setChecked(falg);
		view.setFocusable(falg);
		view.setText(list.get(position).getContent());
		return view;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

}
