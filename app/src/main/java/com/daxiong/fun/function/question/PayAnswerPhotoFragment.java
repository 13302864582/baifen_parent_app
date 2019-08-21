package com.daxiong.fun.function.question;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.daxiong.fun.R;
import com.daxiong.fun.util.ImageUtil;

public class PayAnswerPhotoFragment extends PayAnswerFragmentPhotoCommon implements OnClickListener {

	private Button mTurnLeftBtn;
	private Button mTurnRightBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.hide();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mTurnLeftBtn = (Button) view.findViewById(R.id.photo_view_turn_left_btn);
		mTurnRightBtn = (Button) view.findViewById(R.id.photo_view_turn_right_btn);

		mTurnLeftBtn.setOnClickListener(this);
		mTurnRightBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_photo_view, container, false);
	}

	@Override
	public void sureBtnClick() {

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.photo_view_turn_left_btn:// 左转
			rotate(R.id.photo_view_turn_left_btn);
			break;
		case R.id.photo_view_turn_right_btn:// 右转
			rotate(R.id.photo_view_turn_right_btn);
			break;
		}
	}

	private int degree = 0;

	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		if (rotate == R.id.photo_view_turn_right_btn) {
			degree += 90;
		} else {
			degree -= 90;
		}
		Bitmap orgBitmap = mPhotoImage.getDrawingCache();
		if (orgBitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, mPhotoImage.getMeasuredWidth(),
					mPhotoImage.getMeasuredHeight(), matrix, true);
			BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

			// RectF drawableRect = new RectF(0, 0, mImgWidth, mImgHeight);
			// RectF viewRect = new RectF(0, 0, photo.getMeasuredWidth(),
			// photo.getMeasuredHeight());
			// matrix.setRectToRect(drawableRect, viewRect,
			// Matrix.ScaleToFit.CENTER);
			// photo.setImageMatrix(matrix);

			mPhotoImage.setImageDrawable(bmd);
			if (degree == 360) {
				degree = 0;
			}
			ImageUtil.saveFile(path, resizedBitmap);
		}
	}

	@Override
	protected void goBack() {

	}

    @Override
    public void initView(View view) {
        // TODO Auto-generated method stub
        
    }

}
