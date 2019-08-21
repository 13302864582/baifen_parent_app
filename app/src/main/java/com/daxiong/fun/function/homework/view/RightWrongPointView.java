package com.daxiong.fun.function.homework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;

public class RightWrongPointView extends FrameLayout {

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			changWrong();
		}else {
			changRight();
		}
	}

	private int isRight;
	private int type;
	private ImageView mNextBtn;
	private ImageView mRightView;
	private ImageView mWrongView;

	// public Button getmNextBtn() {
	// return mNextBtn;
	// }

	public ImageView getImageView() {
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			return mWrongView;
		} else {
			return mRightView;
		}
	}

	public RightWrongPointView(Context context, int isRight, int type) {
		super(context);
		this.isRight = isRight;
		this.type = type;
		setupViews();
	}

	public RightWrongPointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.rightwrong_point_view, null);
		this.mRightView = (ImageView) view.findViewById(R.id.right_point_iv);
		this.mWrongView = (ImageView) view.findViewById(R.id.wrong_point_iv);
		this.mNextBtn = (ImageView) view.findViewById(R.id.gointo_btn_rightwrong_point);
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			mRightView.setVisibility(View.GONE);
			mWrongView.setVisibility(View.VISIBLE);
			mNextBtn.setVisibility(View.VISIBLE);
			changWrong();
		} else {
			mRightView.setVisibility(View.VISIBLE);
			mWrongView.setVisibility(View.GONE);
			mNextBtn.setVisibility(View.GONE);
			changRight();
		}
		addView(view);
	}

	public void changRight() {
		if (type == 0) {

			mRightView.setImageResource(R.drawable.dui_1);
		}  else if (type == 1) {
			mRightView.setImageResource(R.drawable.dui_2_1);

		} else if (type == 2) {
			mRightView.setImageResource(R.drawable.dui_3);

		}
	}

	public void changWrong() {
		if (type == 0) {

			mWrongView.setImageResource(R.drawable.cuo_1);
			mNextBtn.setImageResource(R.drawable.cha_1);
		} else if (type ==1) {
			mWrongView.setImageResource(R.drawable.cuo_2);
			mNextBtn.setImageResource(R.drawable.cha_2_1);

		} else if (type == 2) {
			mWrongView.setImageResource(R.drawable.cuo_3);
			mNextBtn.setImageResource(R.drawable.cha_3);

		}
	}
}
