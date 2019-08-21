package com.daxiong.fun.function.homework.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

import java.util.ArrayList;

public class StudyAnalysisAdapter extends BaseAdapter{
	private ArrayList<String> mList;
	private BaseActivity mActivity;
	private int num ;
	
	public StudyAnalysisAdapter(BaseActivity activity,ArrayList<String> mList , int num) {
		super();
		this.mActivity = activity;
		this.mList = mList;
		this.num = num;
	}

	@Override
	public int getCount() {
		return num;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = View.inflate(mActivity, R.layout.study_analysis_item, null);
		}
//		TextView btn = (TextView) convertView.findViewById(R.id.wrong_analysis_btn_item);
//		if (mList != null && position<mList.size()) {
//			String string = mList.get(position);
//			btn.setVisibility(View.VISIBLE);
//			btn.setText(string);
//		}else {
//			btn.setVisibility(View.GONE);
//		}
		return convertView;
	}

}
