package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.model.FuncModel;

import java.util.List;

public class FuncAdapter extends BaseAdapter {

	private List<FuncModel> data;
	private LayoutInflater inflater = null;

	public FuncAdapter(Context ctx, List<FuncModel> data) {
		inflater = LayoutInflater.from(ctx);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup group) {

		FuncViewHolder holder = null;
		if (null == convertView) {
			holder = new FuncViewHolder();
			convertView = inflater.inflate(R.layout.view_func_item, null);
			holder.iconIV = (ImageView) convertView.findViewById(R.id.func_icon);
			holder.nameTV = (TextView) convertView.findViewById(R.id.func_name);
			convertView.setTag(holder);
		} else {
			holder = (FuncViewHolder) convertView.getTag();
		}
		FuncModel fm = data.get(pos);
		holder.iconIV.setImageResource(fm.getBgResId());
		holder.nameTV.setText(fm.getName());

		return convertView;
	}
	
	class FuncViewHolder {
		public ImageView iconIV;
		public TextView nameTV;
	}

}
