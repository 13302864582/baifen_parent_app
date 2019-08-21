package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.view.DragImageView;

import java.util.ArrayList;

public class TecHomeWorkDetail_OnlyReadAdapter extends PagerAdapter {
	private ArrayList<StuPublishHomeWorkPageModel> mUrlList;
	private ArrayList<RelativeLayout> images;
	private int window_height, window_width;

	public TecHomeWorkDetail_OnlyReadAdapter(ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList,
			Context context) {
		super();
		this.mUrlList = mHomeWorkPageModelList;
		images = new ArrayList<RelativeLayout>();
		for (int i = 0; i < mUrlList.size(); i++) {
			RelativeLayout imgParent = (RelativeLayout) View.inflate(context, R.layout.homework_detail_pic_item, null);
			String url = mUrlList.get(i).getImgpath();
			imgParent.setTag(url);
			images.add(imgParent);
		}
	}

	@Override
	public int getCount() {
		return mUrlList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(images.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// final DragImageView imageView = images.get(position);
		final RelativeLayout imgParentLayout = images.get(position);
		final DragImageView imageView = (DragImageView) imgParentLayout.findViewById(R.id.homework_ask_pic_item);
		String imgUrl = (String) imgParentLayout.getTag();
		if (imgUrl.startsWith("http://")) {
			ImageLoader.getInstance().loadImage(imgUrl, imageView, R.drawable.loading);
		} else if (imgUrl.startsWith("/")) {
			try {
				Bitmap bm = BitmapFactory.decodeFile(imgUrl);
				imgParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (window_height == 0 || window_width == 0) {
							window_height = imgParentLayout.getHeight();
							window_width = imgParentLayout.getWidth();
							imageView.setScreenSize(window_width, window_height);
						}
					}
				});
				imageView.setCustomBitmap(bm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		container.addView(imageView);
		return imageView;
	}

}
