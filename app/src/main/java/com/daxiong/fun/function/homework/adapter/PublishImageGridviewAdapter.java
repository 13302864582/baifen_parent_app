package com.daxiong.fun.function.homework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageUtils;
import com.daxiong.fun.R;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布作业的拍照adapter
 * 
 * @author: sky
 */
public class PublishImageGridviewAdapter extends BaseAdapter {

	private Context context;
	private List<StuPublishHomeWorkPageModel> hwImagePathList = new ArrayList<StuPublishHomeWorkPageModel>();
	private OnImageDeleteClickListener mOnImageDeleteClickListener;
	private int imageSize;

	public PublishImageGridviewAdapter(Context context, List<StuPublishHomeWorkPageModel> hwImagePathList,
			OnImageDeleteClickListener listener) {
		this.context = context;
		this.hwImagePathList = hwImagePathList;
		this.mOnImageDeleteClickListener = listener;
		imageSize = context.getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
	}

	public void setData(List<StuPublishHomeWorkPageModel> hwImagePathList) {
		this.hwImagePathList = hwImagePathList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return hwImagePathList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return hwImagePathList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		HomeWorkItemViewHolder holder = null;
		if (null == convertView) {
			holder = new HomeWorkItemViewHolder();
			convertView = View.inflate(context, R.layout.view_home_work_publish_item, null);
			holder.imageIV = (ImageView) convertView.findViewById(R.id.home_work_image_iv);
			holder.deleteIV = (ImageView) convertView.findViewById(R.id.home_work_delete_iv);
			convertView.setTag(holder);
		} else {
			holder = (HomeWorkItemViewHolder) convertView.getTag();
		}
		StuPublishHomeWorkPageModel fm = hwImagePathList.get(pos);
		String path = fm.getImgpath();
		if (null != path) {
			if (path.equals(PublishHomeWorkActivity.ADD_IMAGE_TAG)) {
				holder.deleteIV.setVisibility(View.GONE);
//				holder.imageIV.setImageResource(R.drawable.publish_add_image_selector);
				holder.imageIV.setImageResource(R.drawable.publish_add_image_selector);
				// holder.imageIV.setImageResource(R.drawable.carema_icon);
			} else {
				Bitmap bm = BitmapFactory.decodeFile(path);
				if (null != bm) {
					bm = ImageUtils.corner(bm, imageSize / 10, imageSize);
					holder.imageIV.setImageBitmap(bm);
					holder.imageIV.setVisibility(View.VISIBLE);
					holder.imageIV.invalidate();
					holder.deleteIV.setVisibility(View.VISIBLE);
				} else {

				}
			}
		} else {
			holder.deleteIV.setVisibility(View.GONE);
		}
		final int postp = pos;
		holder.deleteIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != mOnImageDeleteClickListener) {
					mOnImageDeleteClickListener.onDeleteClick(postp);
				}
			}
		});
		return convertView;
	}

	class HomeWorkItemViewHolder {
		public ImageView imageIV;
		public ImageView deleteIV;
	}

	public interface OnImageDeleteClickListener {
		void onDeleteClick(int pos);
	}

}
