package com.daxiong.fun.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.R;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.model.ViewPageModel;

import java.util.LinkedList;
import java.util.List;

public class CardAdapter extends PagerAdapter {
	private Activity activity;
	private List<ViewPageModel> urlList;

	public CardAdapter(Activity activity, List<ViewPageModel> urlList) {
		super();
		this.activity = activity;
		this.urlList = urlList;
	}

	// 当前viewPager里面有多少个条目
	LinkedList<ImageView> convertView = new LinkedList<ImageView>();

	// ArrayList
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	/* 判断返回的对象和 加载view对象的关系 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ImageView view = (ImageView) object;
		convertView.add(view);// 把移除的对象 添加到缓存集合中
		container.removeView(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final int index = position % urlList.size();
		ImageView view;
		if (convertView.size() > 0) {
			view = convertView.remove(0);
		} else {

			view = new ImageView(activity);
		}
		view.setScaleType(ScaleType.FIT_XY);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, WebViewActivity.class);
				intent.putExtra("title", "大熊作业");
				intent.putExtra("url", urlList.get(index).getWeburl());
				activity.startActivity(intent);

			}
		});
		view.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		Glide.with(activity).load(urlList.get(index).getImageurl())
		.placeholder(R.drawable.default_contact_image)
		.diskCacheStrategy(DiskCacheStrategy.ALL)
		.into( view);
		container.addView(view); // 加载的view对象
		return view; // 返回的对象
	}

}
