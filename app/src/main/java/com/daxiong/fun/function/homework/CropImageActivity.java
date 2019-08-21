package com.daxiong.fun.function.homework;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.FitBitmap;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.File;

public class CropImageActivity extends BaseActivity implements OnClickListener {
	
	public static final String IMAGE_SAVE_PATH_TAG = "image_save_path_tag";
	
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ON_TOUCH = 1;

	private TextView nextStepTV;  
	private RelativeLayout nextStepLayout;
	private LinearLayout ll;
	
	private TextView mBackBtn;
	private TextView mSubmitBtn;
	private Button mLeftBtn;
	private Button mRightBtn;

	private String imageSourcePath;
	private String imageSavePath;
	private String publish_opera="";
	private FitBitmap mPhoto;
	private CropImageView mPhotoImage;
	private int left;
	private int top;
	private int width;
	private int height;
	private ImageView tixing;

	private static final String TAG = CropImageActivity.class.getSimpleName();
	public static final String QUESTION_IMG_PATH = "quesiton_img_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.hide();

		setContentView(R.layout.fragment_question_photo_view);

		setWelearnTitle(R.string.text_image_processing);
		
		findViewById(R.id.back_layout).setOnClickListener(this);

		ll = (LinearLayout) findViewById(R.id.ll);
		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_nav_submit);
		nextStepLayout.setOnClickListener(this);
		
		tixing = (ImageView) findViewById(R.id.tixing_crop_iv_qphoto);

		mBackBtn = (TextView) findViewById(R.id.question_photo_view_nav_btn_back);
		mSubmitBtn = (TextView) findViewById(R.id.question_photo_view_nav_submit);
		mLeftBtn = (Button) findViewById(R.id.question_photo_view_turn_left_btn);
		mRightBtn = (Button) findViewById(R.id.question_photo_view_turn_right_btn);
		mPhotoImage = (CropImageView) findViewById(R.id.question_img);
		//getDpi();
		mPhotoImage.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

		mPhotoImage.setGuidelines(ON_TOUCH);

		tixing.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

		showPhoto();
	}
	
//	/**
//	 * @param resId
//	 * @return 如果图片太小，那么就拉伸
//	 */
//	public Bitmap getBitmap(String path) {
//		WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
//		int width = wm.getDefaultDisplay().getWidth();
//
//		Bitmap bitmap = BitmapFactory.decodeFile(path);
//		float scaleWidth = 1, scaleHeight = 1;
//		if (bitmap.getWidth() < width) {
//			scaleWidth = width / bitmap.getWidth();
//			scaleHeight = scaleWidth;
//		}
//		Matrix matrix = new Matrix();
//		matrix.postScale(scaleWidth, scaleHeight);
//		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		return bitmap;
//	}

//	private Bitmap mBitmap;
	private boolean isFromPhotoList;

	private void showPhoto() {
		Intent intent = getIntent();
		if (intent != null) {
			imageSourcePath = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
			imageSavePath = intent.getStringExtra(IMAGE_SAVE_PATH_TAG);
			isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
			publish_opera=intent.getStringExtra("publish_opera");
			if (!TextUtils.isEmpty(imageSourcePath)) {
				imageSourcePath = autoFixOrientation(imageSourcePath,isFromPhotoList,this ,mPhotoImage);// , outWidth
//				mPhotoImage.setImageBitmap(mBitmap);
			}
		}

	}

	public static String autoFixOrientation(String imageSourcePath, boolean isFromPhotoList,Activity activity ,CropImageView imageView) {// , int outWidth,
		int deg = 0;
		Matrix m = new Matrix();
		try {
			ExifInterface exif = new ExifInterface(imageSourcePath);
//			exif = new ExifInterface(imageSourcePath);

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
		
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			//不加载图片到内存，仅获得图片宽高
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imageSourcePath, opts);
			int outWidth = opts.outWidth;
			int outHeight = opts.outHeight;
			// int ssize = 1;
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int widthPixels = metrics.widthPixels;
			int heightPixels = metrics.heightPixels;
			int inSampleSize = 1;
			if (deg == 90 || deg == 270) {
				int temp = outWidth;
				outWidth = outHeight;
				outHeight = temp;
				// ssize = outHeight/ widthPixels > outWidth/heightPixels ?
				// outHeight/ widthPixels:outWidth/heightPixels;
			}

			inSampleSize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels : outWidth
					/ widthPixels;
//			inSampleSize=ImageUtil.calculateInSampleSize(opts, widthPixels, heightPixels);
			if (inSampleSize <= 0) {
				inSampleSize = 1;

				float scaleWidth = 1.0f, scaleHeight =  1.0f;

				scaleWidth = (float)widthPixels / (float)outWidth;
				scaleHeight = (float)heightPixels /(float) outHeight;
				if (scaleWidth < scaleHeight) {
					scaleHeight = scaleWidth;
				} else {
					scaleWidth = scaleHeight;
				}
				m.postScale(scaleWidth, scaleHeight);

			}
			// Options opts = new Options();
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = inSampleSize;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			Bitmap mBitmap = BitmapFactory.decodeFile(imageSourcePath, opts);
			if (null == mBitmap) {
				ToastUtils.show(R.string.text_image_not_exists);
				if (activity instanceof CropImageActivity) {
					activity.finish();
				}
				return imageSourcePath;
			}
			mBitmap = ImageUtil.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
			if (isFromPhotoList) {
				String absolutePath = activity.getCacheDir().getAbsolutePath();
				imageSourcePath = absolutePath + "publish.png";
			}

			ImageUtil.saveFile(imageSourcePath, mBitmap);
			if (imageView != null) {
				imageView.setImageBitmap(mBitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageSourcePath;
	}


	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		if (null != mPhotoImage) {
			int degree = 0;
			if (rotate == R.id.question_photo_view_turn_right_btn) {
				degree = 90;
			} else {
				degree = -90;
			}
			mPhotoImage.rotateImage(degree, imageSourcePath);
		}
	}

	public void goPrevious() {
		// WeLearnFileUtil.deleteFile(imageSourcePath);
//		if (mPhotoImage != null) {
//			ImageLoader.recycleBitmap(mPhotoImage);
//		}
		// IntentManager.goToPayAnswerAskView(this, true);
		finish();
	}

	@Override
	public void onClick(View view) {
		tixing.setVisibility(View.GONE);
		switch (view.getId()) {
		case R.id.back_layout:
			goPrevious();
			break;
		case R.id.question_photo_view_turn_left_btn:
			rotate(R.id.question_photo_view_turn_left_btn);
			break;
		case R.id.question_photo_view_turn_right_btn:
			rotate(R.id.question_photo_view_turn_right_btn);
			break;
		case R.id.question_photo_view_nav_btn_back://重拍
			if (isFromPhotoList) {
				WeLearnFileUtil.deleteFile(imageSourcePath);
//				if (mPhotoImage != null) {
//					ImageLoader.recycleBitmap(mPhotoImage);
//				}
				Bundle data = new Bundle();
				data.putInt("tag", GlobalContant.PAY_ANSWER_ASK);
				IntentManager.goToAlbumView(this, data);
			}else {
				IntentManager.startImageCapture(this, GlobalContant.PAY_ANSWER_ASK);
			}
			break;
		case R.id.question_photo_view_nav_submit:
		case R.id.next_setp_layout:
			try {
				Bitmap currentBm = mPhotoImage.getCroppedImage();
				if (currentBm != null) {
					ImageUtil.saveFile(imageSavePath, currentBm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			Intent data = new Intent();
			// data.putExtra(QUESTION_IMG_PATH, imageSourcePath);
			data.putExtra(IMAGE_SAVE_PATH_TAG, imageSavePath);
			
			setResult(RESULT_OK, data);
			finish();
			// IntentManager.goToPayAnswerAskView(this, data, true);
		

			
			break;
		}
	}

	@Override
	public void onBackPressed() {
		goPrevious();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT) {
			if (resultCode == RESULT_OK) {
				String path = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator + "publish.png";
				LogUtils.i(TAG, path);
				if (!TextUtils.isEmpty(path)) {
					imageSourcePath = autoFixOrientation(imageSourcePath,false,this ,mPhotoImage);// , outWidth
				}
			}
		} 
	}
}
