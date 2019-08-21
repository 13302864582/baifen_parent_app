package com.daxiong.fun.function.homework.view;

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

public class VoiceOrTextPoint extends FrameLayout {

	private TextView mIcSerView;
	private ImageView mIcView;
	private int mRoleid;
	private RelativeLayout rl_bc;
	private int msubtype;
	private TextView tv_subtype;

	public ImageView getIcView() {
		return mIcView;
	}

	public TextView getIcSerView() {
		return mIcSerView;
	}

	public VoiceOrTextPoint(Context context, int roleid, int subtype) {
		super(context);
		mRoleid = roleid;
		msubtype = subtype;
		setupViews();
	}

	public VoiceOrTextPoint(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.voice_text_point_view, null);
		this.mIcSerView = (TextView) view.findViewById(R.id.ser_num_tv_voiceortext);
		this.rl_bc = (RelativeLayout) view.findViewById(R.id.rl_bc);
		this.mIcView = (ImageView) view.findViewById(R.id.ic_view_voiceortext);
		this.tv_subtype = (TextView) view.findViewById(R.id.tv_subtype);
		if (mRoleid != GlobalContant.ROLE_ID_COLLEAGE) {
			mIcView.setImageResource(R.drawable.me_v3);
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
