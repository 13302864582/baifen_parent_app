package com.daxiong.fun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.ActionDef;
import com.daxiong.fun.model.ImageItem;
import com.daxiong.fun.util.BitmapCache;
import com.daxiong.fun.util.BitmapCache.ImageCallback;
import com.daxiong.fun.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {

	final String TAG = getClass().getSimpleName();
	private Context context;
	List<ImageItem> dataList;
	List<String> paths = new ArrayList<String>();
	BitmapCache cache;
	private Handler mHandler;
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					LogUtils.e(TAG, "callback, bmp not match");
				}
			} else {
				LogUtils.e(TAG, "callback, bmp null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}


	public ImageGridAdapter(Context context, List<ImageItem> list, Handler mHandler) {
		this.context = context;
		dataList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private ImageView iv;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
				callback);
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;
				Message msg = mHandler.obtainMessage();
				msg.what = ActionDef.ACTION_PHOTO_SHOW;
				msg.obj = path;
				mHandler.sendMessage(msg);
			}
		});
		return convertView;
	}
}
