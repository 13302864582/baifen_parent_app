
package com.daxiong.fun.function.agent.adapter;

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
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;

import java.util.List;

/**
 * 
 * 此类的描述：拍照发作业的adapter
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2015-7-27 下午3:05:38
 * @version: 2.0
 */
public class AddHomeWorkPhotoAdapter extends BaseAdapter {
    private Context context;
    private List<StuPublishHomeWorkPageModel> list = null;
    private IDeleteImageClickListener mOnImageDeleteClickListener;
    private int imageSize;

    public AddHomeWorkPhotoAdapter(Context context,
            List<StuPublishHomeWorkPageModel> list, IDeleteImageClickListener listener) {
        this.context = context;
        this.list = list;
        this.mOnImageDeleteClickListener = listener;
        imageSize = context.getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
    }

    public void setData(List<StuPublishHomeWorkPageModel> hwImagePathList) {
        this.list = hwImagePathList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup arg2) {
        // long s = System.currentTimeMillis();
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.view_home_work_publish_item, null);
            holder.iv_photo = (ImageView)convertView.findViewById(R.id.home_work_image_iv);
            holder.tv_del = (ImageView)convertView.findViewById(R.id.home_work_delete_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        StuPublishHomeWorkPageModel fm = list.get(pos);
        String path = fm.getImgpath();
        if (null != path) {
            if ("add_image_tag".equals(path)) {
                holder.tv_del.setVisibility(View.GONE);
                // holder.imageIV.setImageResource(R.drawable.bg_add_photo_selector);
                holder.iv_photo.setImageResource(R.drawable.carema_icon);
            } else {
                Bitmap bm = BitmapFactory.decodeFile(path);
                if (null != bm) {
                    bm = ImageUtils.corner(bm, imageSize / 10, imageSize);
                    holder.iv_photo.setImageBitmap(bm);
                    holder.iv_photo.setVisibility(View.VISIBLE);
                    holder.iv_photo.invalidate();
                    holder.tv_del.setVisibility(View.VISIBLE);
                } else {

                }
            }
        } else {
            holder.tv_del.setVisibility(View.GONE);
        }

        holder.tv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (null != mOnImageDeleteClickListener) {
                    mOnImageDeleteClickListener.onDeleteClick(pos);
                }
            }
        });
        // LogUtils.d("yh", "time=" + (System.currentTimeMillis() - s));
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_photo;
        public ImageView tv_del;
    }

    public interface IDeleteImageClickListener {
        void onDeleteClick(int pos);
    }

}
