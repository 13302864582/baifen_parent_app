package com.daxiong.fun.function.homework.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;

import java.util.ArrayList;

public class TecHomeWrokCheckCommonGridViewAdapter extends BaseAdapter {
	private ArrayList<StuPublishHomeWorkPageModel> mUrlList;
	private Activity mContext;
//	private int[] loadStates;
//	private final int LOADING = 0;
//	private final int LOADSUCCESS = 1;
//	private final int LOADFAIL = 2;
	public final static String URLLIST = "urllist";
	public TecHomeWrokCheckCommonGridViewAdapter(ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList , Activity context) {
		super();
		this.mContext = context;
		this.mUrlList = mHomeWorkPageModelList;
//		loadStates = new int[mUrlList.size()];
	}

	public void setList(ArrayList<StuPublishHomeWorkPageModel> urlList){
		this.mUrlList = urlList;
//		loadStates = new int[mUrlList.size()];
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return mUrlList == null ? 0 :mUrlList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (RelativeLayout) View.inflate(mContext, R.layout.homework_check_common_gridview_item, null);
		}
		NetworkImageView view = (NetworkImageView) convertView.findViewById(R.id.homework_pic_gridview_item_common);
		ImageLoader.getInstance().loadImage(mUrlList.get(position).getImgpath(), view,  R.drawable.default_loading_img);
//		loadImg(position, (FrameLayout) convertView);
		return convertView;
	}

//	private void loadImg(final int position, FrameLayout convertView) {
//		loadStates[position] = LOADING;//0是加载中
//		NetworkImageView view = (NetworkImageView) convertView.findViewById(R.id.homework_pic_gridview_item_common);
//		ImageLoader.getInstance().resetUrl(view);
//		view.setImageResource(R.drawable.loading);
//		if (mUrlList.size()> position) {
//			ImageLoader.getInstance().loadImage(mUrlList.get(position), view, R.drawable.loading, R.drawable.retry, new OnImageLoadListener() {
//				
//				@Override
//				public void onSuccess(ImageContainer response) {
//					loadStates[position] = LOADSUCCESS;
//				}
//				
//				@Override
//				public void onFailed(VolleyError error) {
//					loadStates[position] = LOADFAIL;
//				}
//			});
//		}
//	}

//
//	public void click(View view, int position) {
//		switch (loadStates[position] ) {
//		case LOADING:
//			
//			break;
//		case LOADSUCCESS:
//			Bundle data = new Bundle();
//			data.putInt(AnswerListItemView.EXTRA_POSITION, position);
//			data.putStringArrayList(URLLIST, mUrlList);
//			IntentManager.goToHomeWorkDetail_OnlyReadActivity( mContext, data, false);
//			break;
//		case LOADFAIL:
//			loadImg(position, (FrameLayout) view);
//			break;
//
//		default:
//			break;
//		}
//	}

}
