package com.daxiong.fun.function;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;

public class CameraChoiceIconWithSer extends FrameLayout {


	public ImageView getmBgView() {
		return mBgView;
	}

	public void setmBgView(ImageView mBgView) {
		this.mBgView = mBgView;
	}

	public RelativeLayout getRl_bc() {
		return rl_bc;
	}

	public TextView getTv_subtype() {
		return tv_subtype;
	}

	private RelativeLayout rl_bc;
	private TextView tv_subtype;
	private TextView mIcSerView;
	private ImageView mBgView;
	private ImageView mIcView;
	private int mRoleid;
	private int msubtype;

	public ImageView getIcView() {
		return mIcView;
	}

	public TextView getIcSerView() {
		return mIcSerView;
	}

	// public ImageView getBgView() {
	// return mBgView;
	// }

	public CameraChoiceIconWithSer(Context context, int roleid, int subtype) {
		super(context);
		mRoleid = roleid;
		msubtype = subtype;
		setupViews();
	}

	public CameraChoiceIconWithSer(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.camera_choice_icon_with_ser, null);
		this.rl_bc = (RelativeLayout) view.findViewById(R.id.rl_bc);
		this.tv_subtype = (TextView) view.findViewById(R.id.tv_subtype);
		this.mIcSerView = (TextView) view.findViewById(R.id.icon_ser);
		this.mBgView = (ImageView) view.findViewById(R.id.bg_view);
		this.mIcView = (ImageView) view.findViewById(R.id.ic_view);
		if (mRoleid != GlobalContant.ROLE_ID_COLLEAGE) {
			mBgView.setImageResource(R.drawable.me_v0_11_27);
			rl_bc.setBackgroundResource(R.drawable.me_bt);
			tv_subtype.setText("追问");
		} else {
			switch (msubtype) {
			case 0:
				rl_bc.setVisibility(View.GONE);
				tv_subtype.setVisibility(View.GONE);
				break;
			case 1:
				tv_subtype.setText("思路");
				break;
			case 2:
				tv_subtype.setText("过程");
				break;
			case 3:
				tv_subtype.setText("总结");
				break;
			case 4:
				tv_subtype.setText("回复");
				break;
			case 5:
				tv_subtype.setText("讲解");
				break;
			}
		}
		addView(view);
	}
}
