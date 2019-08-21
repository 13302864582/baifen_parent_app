package com.daxiong.fun.function.cram.holder;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.course.holder.BaseHolder;

public class EduHolder extends BaseHolder<EduModel>{

	private NetworkImageView iv_head_icon;
	private TextView tv_name;
	private TextView tv_type_normal;
	private TextView tv_type_vip;
	private int avatarSize;

	@Override
	public View initView() {
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.edu_list_icon_size);
		
		View view = View.inflate(MyApplication.getContext(), R.layout.edu_lv_itme, null);
		iv_head_icon = (NetworkImageView) view.findViewById(R.id.edu_item_iv_head_icon);
		tv_name = (TextView) view.findViewById(R.id.edu_item_tv_name);
		tv_type_normal = (TextView) view.findViewById(R.id.edu_tv_type_normal);
		tv_type_vip = (TextView) view.findViewById(R.id.edu_tv_type_vip);
		
		return view;
	}

	@Override
	public void refreshView() {
		EduModel data = getData();
		
		String url = data.getLogo();
		ImageLoader.getInstance().loadImage(url, iv_head_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		
		tv_name.setText(data.getOrgname());
		
		setTypeID(data.getRelationtype());
	}

	private void setTypeID(int type) {
		if(type == EduModel.TYPE_VIP){
			tv_type_vip.setVisibility(View.VISIBLE);
			tv_type_normal.setVisibility(View.GONE);
			
//			WeLearnSpUtil.getInstance().setOrgVip();
		}else if(type == EduModel.TYPE_NORMAL){
			tv_type_normal.setVisibility(View.VISIBLE);
			tv_type_vip.setVisibility(View.GONE);
		}else{
			tv_type_normal.setVisibility(View.GONE);
			tv_type_vip.setVisibility(View.GONE);
		}
	}
}
