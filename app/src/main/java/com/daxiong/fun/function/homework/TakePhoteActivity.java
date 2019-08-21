package com.daxiong.fun.function.homework;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.common.camera.CameraPreview;
import com.daxiong.fun.common.camera.CropImageView;
import com.daxiong.fun.common.camera.CropperImage;
import com.daxiong.fun.common.camera.FocusView;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.util.CustomCameraUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Class: TakePhoteActivity
 * @Description: 拍照界面
 * @author: lling(www.cnblogs.com/liuling)
 * @Date: 2015/10/25
 */
public class TakePhoteActivity extends Activity
		implements OnClickListener, CameraPreview.OnCameraStatusListener, SensorEventListener {
	private final String TAG = TakePhoteActivity.this.getClass().getSimpleName();
	public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	// public static final String PATH =
	// Environment.getExternalStorageDirectory().toString() + "/AndroidMedia/";

	public static final String PATH = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator;
	CameraPreview mCameraPreview;
	CropImageView mCropImageView;
	RelativeLayout mTakePhotoLayout;
	LinearLayout mCropperLayout;
	ImageView mIv_takephoto;
	TextView mTv_xiangce;
	boolean isOpen = false;
	ImageView mIv_shanguangdeng;

	private static final int REQUEST_CODE_PICK_IMAGE = 0x1;
	String path = "";
	private Intent intent;

	private int PHOTO_SIZE = 2000;

	private Button mLeftBtn;
	private Button mRightBtn;

	private Bitmap bm;

	/** 压缩后的图片最大值 单位KB */
	private int maxSize = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置横屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_take_phote);
		initView();

	}

	public void initView() {
		intent = getIntent();
		// Initialize components of the app
		mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
		mCameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
		FocusView focusView = (FocusView) findViewById(R.id.view_focus);
		mTakePhotoLayout = (RelativeLayout) findViewById(R.id.take_photo_layout);
		mCropperLayout = (LinearLayout) findViewById(R.id.cropper_layout);
		ImageView mIv_takephoto = (ImageView) this.findViewById(R.id.iv_takephoto);
		mIv_takephoto.setOnClickListener(this);
		mTv_xiangce = (TextView) this.findViewById(R.id.tv_xiangce);
		mTv_xiangce.setOnClickListener(this);
		ImageView mIv_close = (ImageView) this.findViewById(R.id.iv_close);
		mIv_close.setOnClickListener(this);
		mIv_shanguangdeng = (ImageView) this.findViewById(R.id.iv_shanguangdeng);
		mIv_shanguangdeng.setOnClickListener(this);

		mCameraPreview.setFocusView(focusView);
		mCameraPreview.setOnCameraStatusListener(this);
		mCropImageView.setGuidelines(2);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mLeftBtn = (Button) findViewById(R.id.question_photo_view_turn_left_btn);
		mRightBtn = (Button) findViewById(R.id.question_photo_view_turn_right_btn);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

	}

	boolean isRotated = false;

	@Override
	public void onResume() {
		super.onResume();
		if (!isRotated) {
			TextView hint_tv = (TextView) findViewById(R.id.hint);
			ObjectAnimator animator = ObjectAnimator.ofFloat(hint_tv, "rotation", 0f, 90f);
			animator.setStartDelay(800);
			animator.setDuration(1000);
			animator.setInterpolator(new LinearInterpolator());
			animator.start();
			View view = findViewById(R.id.crop_hint);
			AnimatorSet animSet = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", 0f, 90f);
			ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationX", 0f, -50f);
			animSet.play(animator1).before(moveIn);
			animSet.setDuration(10);
			animSet.start();
			isRotated = true;
		}
		mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.e(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_xiangce:// 相册
			// Intent picture = new
			// Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// startActivityForResult(picture, 110);

			try {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}

			break;

		case R.id.iv_takephoto:// 拍照
			if (mCameraPreview != null) {
				mCameraPreview.takePicture();
			}
			break;
		case R.id.iv_close:// 关闭
			finish();
			break;
		case R.id.iv_shanguangdeng:// 打开关闭闪光灯
			if (isOpen) {
				mIv_shanguangdeng.setBackgroundResource(R.drawable.close_shanguangdeng);
				// 打开闪光灯
				mCameraPreview.adjustLight(2);
			} else {
				mIv_shanguangdeng.setBackgroundResource(R.drawable.open_shanguangdeng);
				mCameraPreview.adjustLight(1);
			}
			isOpen = !isOpen;

			break;
		case R.id.question_photo_view_turn_left_btn:
			CustomCameraUtils.rotate(mCropImageView, R.id.question_photo_view_turn_left_btn);
			break;
		case R.id.question_photo_view_turn_right_btn:
			CustomCameraUtils.rotate(mCropImageView, R.id.question_photo_view_turn_right_btn);
			break;
		}

	}

	/**
	 * 关闭截图界面
	 * 
	 * @param view
	 */
	public void closeCropper(View view) {
		showTakePhotoLayout();
	}

	/**
	 * 开始截图，并保存图片
	 * 
	 * @param view
	 */
	public void startCropper(View view) {
		// 获取截图并旋转90度
		CropperImage cropperImage = mCropImageView.getCroppedImage();
		Log.e(TAG, cropperImage.getX() + "," + cropperImage.getY());
		Log.e(TAG, cropperImage.getWidth() + "," + cropperImage.getHeight());
		// Bitmap bitmap = TakePhotoUtils.rotate(cropperImage.getBitmap(), -90);

		Bitmap bitmap = cropperImage.getBitmap();
		// 系统时间
		long dateTaken = System.currentTimeMillis();
		// 图像名称
		String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";
		Uri uri = insertImage(getContentResolver(), filename, dateTaken, PATH, filename, bitmap, null);
		cropperImage.getBitmap().recycle();
		cropperImage.setBitmap(null);
		/*
		 * Intent intent = new Intent(this, ShowCropperedActivity.class);
		 * intent.setData(uri); intent.putExtra("path", PATH + filename);
		 * intent.putExtra("width", bitmap.getWidth());
		 * intent.putExtra("height", bitmap.getHeight());
		 * intent.putExtra("cropperImage", cropperImage); startActivity(intent);
		 * 
		 * bitmap.recycle(); finish();
		 * //super.overridePendingTransition(R.anim.camera_fade_in,
		 * R.anim.camera_fade_out); // doAnimation(cropperImage);
		 */
		Intent data = new Intent();
		data.putExtra("image_save_path_tag", CustomCameraUtils.getAbsoluteImagePath(TakePhoteActivity.this, uri));
		setResult(RESULT_OK, data);
		bitmap.recycle();
		finish();

	}

	private void doAnimation(CropperImage cropperImage) {
		ImageView imageView = new ImageView(this);
		View view = LayoutInflater.from(this).inflate(R.layout.image_view_layout, null);
		((RelativeLayout) view.findViewById(R.id.root_layout)).addView(imageView);
		RelativeLayout relativeLayout = ((RelativeLayout) findViewById(R.id.root_layout));
		// relativeLayout.addView(imageView);
		imageView.setX(cropperImage.getX());
		imageView.setY(cropperImage.getY());
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		lp.width = (int) cropperImage.getWidth();
		lp.height = (int) cropperImage.getHeight();
		imageView.setLayoutParams(lp);
		imageView.setImageBitmap(cropperImage.getBitmap());
		try {
			getWindow().addContentView(view, lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * AnimatorSet animSet = new AnimatorSet(); ObjectAnimator translationX
		 * = ObjectAnimator.ofFloat(this, "translationX", cropperImage.getX(),
		 * 0); ObjectAnimator translationY = ObjectAnimator.ofFloat(this,
		 * "translationY", cropperImage.getY(), 0);
		 */

		TranslateAnimation translateAnimation = new TranslateAnimation(0, -cropperImage.getX(), 0,
				-(Math.abs(cropperImage.getHeight() - cropperImage.getY())));// 当前位置移动到指定位置
		RotateAnimation rotateAnimation = new RotateAnimation(0, -90, Animation.ABSOLUTE, cropperImage.getX(),
				Animation.ABSOLUTE, cropperImage.getY());
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(translateAnimation);
		animationSet.addAnimation(rotateAnimation);
		animationSet.setFillAfter(true);
		animationSet.setDuration(2000L);
		imageView.startAnimation(animationSet);
		// finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_PICK_IMAGE:// 從相冊中選擇
				if (null == data) {
					return;
				}
				Uri uri = data.getData();
				// to do find the path of pic by uri
				path = CustomCameraUtils.getImageAbsolutePath(this, uri);
				intent.putExtra("isFromPhotoList", true);
				intent.putExtra("path", path);
				setResult(RESULT_OK, intent);
				finish();
				break;
			case RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS:// 相机拍照
				setResult(resultCode, data);
				finish();
				break;

			}
		}

	}

	/**
	 * 拍照成功后回调 存储图片并显示截图界面
	 * 
	 * @param data
	 */
	@Override
	public void onCameraStopped(byte[] data) {
		Log.i("TAG", "==onCameraStopped==");
		try {
			new SavePicTask(data).execute();
			// saveToSDCard(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此类的描述：拍照保存照片Task
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2016年5月25日 下午7:39:59
	 */
	private class SavePicTask extends AsyncTask<Void, Void, Uri> {

		private byte[] data;

		private ProgressDialog progressDialog;

		public SavePicTask(byte[] data) {
			this.data = data;
		}

		protected void onPreExecute() {
			// progressDialog = ProgressDialog.show(TakePhoteActivity.this, "",
			// "保存图片中...", true, false);
		}

		@Override
		protected Uri doInBackground(Void... params) {
			try {
				return saveToSDCard(data);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Uri result) {
			super.onPostExecute(result);

			/*
			 * if (!TextUtils.isEmpty(result)) { progressDialog.dismiss(); //
			 * CameraManager.getInstance().processPhotoItem(TakePhoteActivity.
			 * this, // result); } else { // toast("拍照失败，请稍后重试！",
			 * Toast.LENGTH_LONG); }
			 */

			// 准备截图
			try {
				mCropImageView.setImageBitmap(
						MediaStore.Images.Media.getBitmap(TakePhoteActivity.this.getContentResolver(), result));
				// mCropImageView.setImageBitmap(bm);
				/**
				 * 检测手机拍照是否 int
				 * degree=CustomCameraUtils.getBitmapDegree(getAbsoluteImagePath
				 * (source)); CustomCameraUtils.rotateBitmapByDegree(bitmap,
				 * degree);
				 */

				/**
				 * 原来的旋转方法 裁剪自定义控件 mCropImageView.rotateImage(90);
				 */

				CustomCameraUtils.rotateCropImage(mCropImageView, bm);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			showCropperLayout();
			/*
			 * path = getAbsoluteImagePath(source); mCameraPreview.start(); //
			 * 继续启动摄像头
			 */
			// intent.putExtra("isFromPhotoList", false);
			// intent.putExtra("path", path);
			// setResult(RESULT_OK, intent);
			// finish();

		}
	}

	/**
	 * 保存照片到SD卡
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2016年5月30日 下午6:16:11 saveToSDCard
	 * @param data
	 * @return
	 * @throws IOException
	 *             Uri
	 */
	public Uri saveToSDCard(byte[] data) throws IOException {
		Uri source = null;
		String newPath = "";
		String filename = "";
		if (data != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			// 创建图像
			bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			// 获取bitmap的宽和高
			// int width=bm.getWidth();
			// int height=bm.getHeight();
			// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth()/2,
			// bm.getHeight()/2);
			// 系统时间
			long dateTaken = System.currentTimeMillis();
			// 图像名称
			filename = "publish_" + DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";

			// 存储图像（PATH目录）
			source = insertImage(getContentResolver(), filename, dateTaken, PATH, filename, bm, data);
			// newPath = CustomCameraUtils.saveBitmapToFile(new File(PATH,
			// filename), PATH + filename);

			// File jpgFile = new File(PATH, filename);
			// FileOutputStream fos=new FileOutputStream(jpgFile);// 文件输出流
			// ByteArrayOutputStream bos=compress(bm);
			// fos.write(bos.toByteArray());
			// fos.flush();
			// fos.close();

		}
		// return PATH+filename;
		return source;

	}

	/**
	 * 图片压缩方法
	 * 
	 * @param bitmap
	 *            图片文件
	 * @param max
	 *            文件大小最大值
	 * @return 压缩后的字节流
	 * @throws Exception
	 */
	public ByteArrayOutputStream compress(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 99;
		while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 3;// 每次都减少10
			// 压缩比小于0，不再压缩
			if (options < 0) {
				break;
			}
			Log.i(TAG, baos.toByteArray().length / 1024 + "");
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		return baos;
	}

	/**
	 * 存储图像并将信息添加入媒体数据库
	 */
	private Uri insertImage(ContentResolver cr, String name, long dateTaken, String directory, String filename,
			Bitmap source, byte[] jpegData) {
		OutputStream outputStream = null;
		String filePath = directory + filename;
		try {
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(directory, filename);
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);

				/////
				int ratio = 0;
				int size = (int) (source.getWidth() * source.getHeight() / 1024);
				if (size > 4000) {// 超过1000k进行压缩
					ratio = 70;
				} else if (4000 > size && size > 3000) {
					ratio = 75;
				} else if (3000 > size && size > 2000) {
					ratio = 80;
				} else if (2000 > size && size > 1000) {
					ratio = 85;
				} else if (1000 > size && size > 500) {
					ratio = 90;
				} else if (500 > size && size > 200) {
					ratio = 95;
				} else if (200 > size && size > 100) {
					ratio = 100;
				} else {
					ratio = 100;
				}

				if (source != null) {
					source.compress(Bitmap.CompressFormat.JPEG, ratio, outputStream);
				} else {
					outputStream.write(jpegData);
				}
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			return null;
		} finally {
			// source.recycle();
			if (outputStream != null) {
				try {
					outputStream.close();

				} catch (Throwable t) {
				}
			}
		}
		ContentValues values = new ContentValues(7);
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
		values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, filePath);
		return cr.insert(IMAGE_URI, values);
	}

	private void showTakePhotoLayout() {
		mTakePhotoLayout.setVisibility(View.VISIBLE);
		mCropperLayout.setVisibility(View.GONE);
	}

	private void showCropperLayout() {
		mTakePhotoLayout.setVisibility(View.GONE);
		mCropperLayout.setVisibility(View.VISIBLE);
		mCameraPreview.start(); // 继续启动摄像头
	}

	private float mLastX = 0;
	private float mLastY = 0;
	private float mLastZ = 0;
	private boolean mInitialized = false;
	private SensorManager mSensorManager;
	private Sensor mAccel;

	@Override
	public void onSensorChanged(SensorEvent event) {

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		}
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);

		if (deltaX > 0.8 || deltaY > 0.8 || deltaZ > 0.8) {
			mCameraPreview.setFocus();
		}
		mLastX = x;
		mLastY = y;
		mLastZ = z;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
