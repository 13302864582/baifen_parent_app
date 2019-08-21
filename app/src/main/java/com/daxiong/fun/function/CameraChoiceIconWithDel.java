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
import com.daxiong.fun.util.MySharePerfenceUtil;

public class CameraChoiceIconWithDel extends FrameLayout {
	public ImageView getBgView() {
		return bgView;
	}

	public void setBgView(ImageView bgView) {
		this.bgView = bgView;
	}

	public RelativeLayout getRl() {
		return rl;
	}

	public void setRl(RelativeLayout rl) {
		this.rl = rl;
	}

	public TextView getIcon_del() {
		return icon_del;
	}

	public void setIcon_del(TextView icon_del) {
		this.icon_del = icon_del;
	}

	private ImageView mIcView;
	private ImageView bgView;
	private RelativeLayout rl_bc;
	private RelativeLayout rl;
	private TextView tv_subtype;
	private TextView icon_del;
	private int msubtype;
	public ImageView getIcView() {
		return mIcView;
	}
	
	public CameraChoiceIconWithDel(Context context,int subtype) {
		super(context);
		msubtype = subtype;
		setupViews();
	}

	public CameraChoiceIconWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.camera_choice_icon_with_del, null);
		this.mIcView = (ImageView) view.findViewById(R.id.ic_view);
		this.rl_bc = (RelativeLayout) view.findViewById(R.id.rl_bc);
		this.rl = (RelativeLayout) view.findViewById(R.id.rl);
		this.tv_subtype = (TextView) view.findViewById(R.id.tv_subtype);
		this.icon_del = (TextView) view.findViewById(R.id.icon_del);
		bgView = (ImageView) view.findViewById(R.id.bg_answer_view);
		if (MySharePerfenceUtil.getInstance().getUserRoleId() != GlobalContant.ROLE_ID_COLLEAGE) {
			bgView.setImageResource(R.drawable.me_v0_11_27);
			rl_bc.setBackgroundResource(R.drawable.me_bt);
			tv_subtype.setText("追问");
		}else {
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
