package com.daxiong.fun.function.question;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.view.ScaleImageView;

public class PayAnswerQuestionDetailActivity extends BaseActivity implements OnClickListener {

	private ScaleImageView scaleImg;
	private LinearLayout mRootContainer;
	public static final String IMG_URL = "img_url";
	public static final String IMG_PATH = "img_path";
	private Matrix initMatrix = new Matrix();

	@Override
	public void onPause() {
		super.onPause();
		// StatService.onEventEnd(mActivity, "PayAnswerQustionDetail", "");
		MobclickAgent.onEventEnd(this, "PayAnswerQustionDetail");
	}

	@Override
	public void onResume() {
		super.onResume();
		// StatService.onEventStart(mActivity, "PayAnswerQustionDetail", "");
		MobclickAgent.onEventBegin(this, "PayAnswerQustionDetail");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.fragment_question_detail_pic);

		scaleImg = (ScaleImageView) findViewById(R.id.pic_scale);
		mRootContainer = (LinearLayout) findViewById(R.id.detail_root);

		boolean doNotRoate = getIntent().getBooleanExtra("doNotRoate", false);
		String url = getIntent().getStringExtra(IMG_URL);
		String path = getIntent().getStringExtra(IMG_PATH);
		if (doNotRoate) {
			findViewById(R.id.pay_answer_detail_rotate_container).setVisibility(View.GONE);
		}
		Button turnLeft = (Button) findViewById(R.id.pay_answer_detail_turn_left_btn);
		Button turnRight = (Button) findViewById(R.id.pay_answer_detail_turn_right_btn);

		if (TextUtils.isEmpty(path)) {
			scaleImg.loadImage(url, null);
		} else {
			scaleImg.loadImage(path);
		}
		// scaleImg.getmPicView().getLocationInWindow(xy);
		// xy[0] = scaleImg.getmPicView().getParent().get
		mRootContainer.setOnClickListener(this);
		// scaleImg.setOnClickListener(this);
		scaleImg.setActivity(this);
		turnLeft.setOnClickListener(this);
		turnRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pic_scale:
			// mActivity.finish();
			break;
		case R.id.pay_answer_detail_turn_left_btn:
			rotate(GlobalContant.TURN_LEFT);
			break;
		case R.id.pay_answer_detail_turn_right_btn:
			rotate(GlobalContant.TURN_RIGHT);
			break;
		default:
			break;
		}
	}

	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		int degree = 90;
		if (rotate == GlobalContant.TURN_RIGHT) {
			degree = 90;
		} else {
			degree = -90;
		}
		initMatrix.postTranslate(0, 0);
		initMatrix.postScale(1, 1);

		scaleImg.getmPicView().setImageMatrix(initMatrix);
		Bitmap orgBitmap = null;
		// if (bm != null) {
		// orgBitmap = Bitmap.createBitmap(bm);
		// } else {
		orgBitmap = ScaleImageView.getBitMapFromView(scaleImg.getmPicView());
		// ToastUtils.show(mActivity, "Kong ");
		// }
		if (orgBitmap != null && !orgBitmap.isRecycled()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, orgBitmap.getWidth(), orgBitmap.getHeight(),
					matrix, true);
			// bm = resizedBitmap;
//			if (orgBitmap != null && !orgBitmap.isRecycled()) {
//				orgBitmap.recycle();
//				orgBitmap = null;// Log.i(TAG, "---recycle---");
//			}
			scaleImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
			scaleImg.getmPicView().setImageBitmap(resizedBitmap);
			scaleImg.setScaleType(ImageView.ScaleType.MATRIX);
			// scaleImg.set
			// WeLearnImageUtil.saveFile(path, resizedBitmap);
		}
	}
}
