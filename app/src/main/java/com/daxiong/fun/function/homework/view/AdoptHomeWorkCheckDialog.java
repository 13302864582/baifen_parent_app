package com.daxiong.fun.function.homework.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.daxiong.fun.R;
import com.daxiong.fun.util.ToastUtils;

/**
 * 采纳答案并且提交满意度评价的dialog
 */
public class AdoptHomeWorkCheckDialog extends Dialog implements android.view.View.OnClickListener {
	private Context mContext;
	private CheckBox checkBox1;
	private android.view.View.OnClickListener mListener;

	public interface AdoptSubmitBtnClick {
		void ensure(int degree, String comment,int checkbox);
	}

	private RatingBar mDegreeRatingBar;
	private AdoptSubmitBtnClick mReset;
	private EditText mCommentEditText;

	public AdoptHomeWorkCheckDialog(Context context) {
		super(context);
	}

	public AdoptHomeWorkCheckDialog(Context context, AdoptSubmitBtnClick reset) {
		super(context, R.style.adoptHomeWorkRatingBar);
		this.mReset = reset;
		// if (context instanceof StuHomeWorkCheckDetailActivity) {
		this.mContext = context;
		if (context instanceof android.view.View.OnClickListener) {
			mListener = (android.view.View.OnClickListener) context;
		}
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = View.inflate(mContext, R.layout.adopt_homework_dialog, null);
		setContentView(view);
		if (mListener != null) {
			view.setOnClickListener(mListener);
		}
		ActionBar bar = getActionBar();
		if (bar != null) {
			bar.hide();
		}

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		mDegreeRatingBar = (RatingBar) findViewById(R.id.degree_ratingBar_adopt_dialog);
		mCommentEditText = (EditText) findViewById(R.id.comment_et_adopt_dialog);
		// mDegreeRatingBar.setOnClickListener(mActivity);
		findViewById(R.id.submit_adopt_btn).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边
		// WindowManager.LayoutParams params = getWindow().getAttributes();
		// params.width = (int)
		// (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
		// params.height = (int)
		// (getContext().getResources().getDisplayMetrics().heightPixels *
		// 0.42);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_adopt_btn:
			int degree = mDegreeRatingBar.getProgress();
			if (degree == 0) {
				ToastUtils.show(R.string.text_please_choose_satisfaction);
				return;
			}
			dismiss();
			String comment = mCommentEditText.getText().toString().trim();
			if (TextUtils.isEmpty(comment)) {
				comment = "";
			}
			if(checkBox1.isChecked()){
				
				mReset.ensure(degree, comment,1);
			}else{
				mReset.ensure(degree, comment,0);
				
			}

			break;
		case R.id.cancel:
			
			dismiss();
			
			
			break;

		default:
			break;
		}
	}
}
