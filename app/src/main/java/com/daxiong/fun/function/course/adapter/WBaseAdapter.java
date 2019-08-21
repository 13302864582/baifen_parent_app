package com.daxiong.fun.function.course.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daxiong.fun.function.course.holder.BaseHolder;

import java.util.List;

/**
 * 此类应配合BaseHolder使用，对BaseAdapter的装饰
 *
 * @param <T> 数据类型
 */
public abstract class WBaseAdapter<T> extends BaseAdapter{
	protected List<T> mData;
	protected BaseHolder<T> mHolder;
	
	public WBaseAdapter(List<T> data){
		setData(data);
	}
	
	public List<T> getData() {
		return mData;
	}

	public void setData(List<T> data) {
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null != convertView && convertView.getTag() instanceof BaseHolder){
			mHolder = (BaseHolder<T>) convertView.getTag();
		}else{
			mHolder = createHolder();
		}
		
		//设置数据
		mHolder.setParamInt(getIntParam());
		mHolder.setParamBool(getBoolParam());
		mHolder.setParamStr(getStringParam());
		mHolder.setPosition(position);
		mHolder.setData(mData.get(position));
		return mHolder.getRootView();
	}

	public String getStringParam() {
		return "";
	}

	public boolean getBoolParam() {
		return false;
	}

	public int getIntParam() {
		return -1;
	}

	/**当 convertView 没有可以复用时， 创建View及其对应Holder*/
	public abstract BaseHolder<T> createHolder();
}
