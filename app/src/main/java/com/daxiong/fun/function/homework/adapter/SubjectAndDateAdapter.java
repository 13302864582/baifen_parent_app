package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * 科目和日期时间Adapter
 * @author sky
 */
public class SubjectAndDateAdapter extends BaseExpandableListAdapter {
	
	private Context context;

	public SubjectAndDateAdapter(Context context) {
		super();
		this.context=context;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
