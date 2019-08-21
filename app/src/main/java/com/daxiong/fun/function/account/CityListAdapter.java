package com.daxiong.fun.function.account;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daxiong.fun.R;

public class CityListAdapter extends BaseAdapter {

	private String[] mInfos;
	private Context mContext;

	public CityListAdapter(Context context,String[] mInfos) {
		this.mContext = context;
		this.mInfos= mInfos;
	}

	@Override
	public int getCount() {
		return mInfos==null?0: mInfos.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		View view = null;
		if (convertView == null) {

			view = View.inflate(mContext, R.layout.item_city_layout, null);

		} else {
			view = convertView;
		}
		TextView tv = (TextView) view.findViewById(R.id.tv);
		tv.setText(mInfos[position]);
		return view;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
