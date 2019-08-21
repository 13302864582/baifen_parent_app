package com.daxiong.fun.function.question;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.view.BaseFragment;

public abstract class PayAnswerFragmentPhotoCommon extends BaseFragment implements OnClickListener {

	protected NetworkImageView mPhotoImage;
	protected TextView mBackBtn;
	protected TextView mSureBtn;
	protected RelativeLayout mAnswerPicContainer;

	protected String path;
	protected boolean isFromPhotoList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getActivity().getIntent();
		if (intent == null) {
			return;
		}
		path = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
		isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
	}

	public abstract View inflateView(LayoutInflater inflater, ViewGroup container);

	public abstract void sureBtnClick();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflateView(inflater, container);
		mAnswerPicContainer = (RelativeLayout) view.findViewById(R.id.anser_pic_container);
		mBackBtn = (TextView) view.findViewById(R.id.photo_view_nav_btn_back);
		mSureBtn = (TextView) view.findViewById(R.id.photo_view_nav_submit);
		mPhotoImage = (NetworkImageView) view.findViewById(R.id.photo_view_image);
		mPhotoImage.setDrawingCacheEnabled(true);
		mBackBtn.setOnClickListener(this);
		mSureBtn.setOnClickListener(this);
//		mAnswerPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				int window_height = mAnswerPicContainer.getHeight();
//				int window_width = mAnswerPicContainer.getWidth();
//				// mPhotoImage.setScreenSize(window_width, window_height);
//			}
//		});

		showPhoto();
		return view;
	}

	protected void showPhoto() {

		if (!TextUtils.isEmpty(path)) {
			int deg = 0;
			Matrix m = new Matrix();

			try {
				ExifInterface exif = new ExifInterface(path);
				exif = new ExifInterface(path);
				int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				switch (rotateValue) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					deg = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					deg = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					deg = 270;
					break;
				default:
					deg = 0;
					break;
				}
				m.preRotate(deg);
			} catch (Exception ee) {
				LogUtils.d("catch img error", "return");

			}

			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			double outWidth = opts.outWidth;
			double outHeight = opts.outHeight;
			double ssize = 1;
			DisplayMetrics metrics = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			double widthPixels = metrics.widthPixels;
			double heightPixels = metrics.heightPixels;

			if (deg == 90 || deg == 270) {
				double temp = outWidth;
				outWidth = outHeight;
				outHeight = temp;
			}
			heightPixels = heightPixels - DensityUtil.dip2px(mActivity, 60);

			ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels
					: outWidth / widthPixels;

			if (ssize < 1) {
				ssize = 1;
			}
			ssize = Math.ceil(ssize);
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = (int) ssize;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			Bitmap mBitmap = BitmapFactory.decodeFile(path, opts);

			outWidth = mBitmap.getWidth();
			outHeight = mBitmap.getHeight();
			if (deg == 90 || deg == 270) {
				double temp = outWidth;
				outWidth = outHeight;
				outHeight = temp;
			}
			float scaleWidth = 1, scaleHeight = 1;
			scaleWidth = (float) (widthPixels / outWidth);
			scaleHeight = (float) (heightPixels / outHeight);
			if (scaleWidth < scaleHeight) {
				scaleHeight = scaleWidth;
			}
			m.postScale(scaleWidth, scaleHeight);

			mBitmap = ImageUtil.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
			ImageUtil.saveFile(path, mBitmap);
			// mPhotoImage.setImageBitmap(mBitmap);
			mPhotoImage.setCustomBitmap(mBitmap);
		}
	}

	protected abstract void goBack();

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.photo_view_nav_btn_back:
			goBack();
			break;
		case R.id.photo_view_nav_submit:
			sureBtnClick();
			break;
		}
	}
}
