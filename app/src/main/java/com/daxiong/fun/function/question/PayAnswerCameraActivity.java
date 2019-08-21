package com.daxiong.fun.function.question;
//package com.welearn.welearn.gasstation;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.UUID;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.hardware.Camera;
//import android.hardware.Camera.Size;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ProgressBar;
//
//import com.welearn.base.view.SingleFragmentActivity;
//import com.welearn.manager.IntentManager;
//import com.welearn.welearn.R;
//
//public class PayAnswerCameraActivity extends SingleFragmentActivity implements OnClickListener {
//
//	// private Button mButton;
//	private Camera mCamra;
//	private SurfaceView mSurfaceView;
//	private ProgressBar mProgressBar;
//	private CameraSizeCompatator sizeCompatator = new CameraSizeCompatator();
//
//	public static final String IMAGE_FILE_NAME = "image_path";
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// mActionBar.setTitle(R.string.text_take_photo);
//		// mActionBar.setCustomView(R.layout.crime_actionbar_custom);
//		// mButton = (Button)
//		// mActionBar.getCustomView().findViewById(R.id.crime_take_btn);
//		// mButton.setOnClickListener(this);
//
//		setContentView(R.layout.fragment_camera);
//		
//		setWelearnTitle(R.string.text_take_photo);
//		
//		findViewById(R.id.back_layout).setOnClickListener(this);
//
//		mSurfaceView = (SurfaceView) findViewById(R.id.pay_answer_camera_surfaceview);
//		mProgressBar = (ProgressBar) findViewById(R.id.pay_answer_camera_progressbar);
//		final SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		surfaceHolder.addCallback(new Callback() {
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//				if (mCamra != null) {
//					mCamra.stopPreview();
//				}
//			}
//
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//				try {
//					if (mCamra != null) {
//						mCamra.setPreviewDisplay(surfaceHolder);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//				if (mCamra != null) {
//					// mButton.setEnabled(true);
//					Camera.Parameters params = mCamra.getParameters();
//
//					Camera.CameraInfo info = new Camera.CameraInfo();
//					Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
//					int rotate = getPhotoDegree(info, PayAnswerCameraActivity.this);
//					params.setRotation(rotate);
//					mCamra.setParameters(params);
//
//					Size s = getBestSupportedSize(params.getSupportedPreviewSizes(), 800);
//					params.setPreviewSize(s.width, s.height);
//					System.out.println("PreviewSize: " + s.width + "  " + s.height);
//					s = getBestSupportedSize(params.getSupportedPictureSizes(), 800);
//					System.out.println("PictureSize: " + s.width + "  " + s.height);
//					params.setPictureSize(s.width, s.height);
//					mCamra.setParameters(params);
//
//					mCamra.setDisplayOrientation(getPreviewDegree(PayAnswerCameraActivity.this));
//
//					try {
//						mCamra.startPreview();
//					} catch (Exception e) {
//						e.printStackTrace();
//						mCamra.release();
//						mCamra = null;
//					}
//				}
//			}
//		});
//		PackageManager pm = getPackageManager();
//		boolean hasCamrea = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
//				|| pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
//				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD || Camera.getNumberOfCameras() > 0;
//		if (hasCamrea) {
//			// mButton.setEnabled(false);
//		}
//	}
//
//	private Size getBestSupportedSize(List<Size> sizes, int width) {
//		// Size bestSize = sizes.get(0);
//		// int largestArea = bestSize.width * bestSize.height;
//		// for (Size s : sizes) {
//		// System.out.println(s.width + "---" + s.height);
//		// int area = s.width * s.height;
//		// if (area > largestArea) {
//		// largestArea = area;
//		// bestSize = s;
//		// }
//		// }
//		// return bestSize;
//		Collections.sort(sizes, sizeCompatator);
//		int i = 0;
//		for (Size s : sizes) {
//			if (s.width > width && equalRadio(s, 1.33f)) {
//				break;
//			}
//			i++;
//		}
//		return sizes.get(i);
//	}
//
//	private boolean equalRadio(Size s, float radio) {
//		float r = s.width / s.height;
//		if (Math.abs(r - radio) <= 0.2) {
//			return true;
//		}
//		return false;
//	}
//
//	private class CameraSizeCompatator implements Comparator<Size> {
//
//		@Override
//		public int compare(Size size1, Size size2) {
//			if (size1.width == size2.width) {
//				return 0;
//			} else if (size1.width > size2.width) {
//				return 1;
//			}
//			return -1;
//		}
//
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		mCamra = Camera.open(0);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		if (mCamra != null) {
//			mCamra.release();
//			mCamra = null;
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back_layout:
//			finish();
//			break;
////		case R.id.crime_take_btn:
////			if (mCamra != null) {
////				mCamra.takePicture(mShutterCallback, null, mJpegCallback);
////			}
////			break;
//		}
//	}
//
//	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
//
//		@Override
//		public void onShutter() {
//			mProgressBar.setVisibility(View.VISIBLE);
//		}
//	};
//
//	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
//
//		@Override
//		public void onPictureTaken(byte[] data, Camera camera) {
//			String fileName = UUID.randomUUID().toString() + ".jpg";
//			FileOutputStream fos = null;
//			try {
//				fos = PayAnswerCameraActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE);
//				fos.write(data);
//				Bundle bundle = new Bundle();
//				String imagePath = PayAnswerCameraActivity.this.getFilesDir().toString() + File.separator + fileName;
//				bundle.putString(PayAnswerImageGridActivity.IMAGE_PATH, imagePath);
//				IntentManager.goToPhotoView(PayAnswerCameraActivity.this, bundle);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (fos != null) {
//					try {
//						fos.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					fos = null;
//				}
//			}
//		}
//	};
//
//	private int getPhotoDegree(Camera.CameraInfo info, Activity activity) {
//		// 
//		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//		int degree = 0;
//		switch (rotation) {
//		case Surface.ROTATION_0:
//			degree = 0;
//			break;
//		case Surface.ROTATION_90:
//			degree = 90;
//			break;
//		case Surface.ROTATION_180:
//			degree = 180;
//			break;
//		case Surface.ROTATION_270:
//			degree = 270;
//			break;
//		}
//		int rotate = (info.orientation - degree + 360) % 360;
//		return rotate;
//	}
//
//	private int getPreviewDegree(Activity activity) {
//		// 
//		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//		int degree = 0;
//		// 
//		switch (rotation) {
//		case Surface.ROTATION_0:
//			degree = 90;
//			break;
//		case Surface.ROTATION_90:
//			degree = 0;
//			break;
//		case Surface.ROTATION_180:
//			degree = 270;
//			break;
//		case Surface.ROTATION_270:
//			degree = 180;
//			break;
//		}
//		return degree;
//	}
//}
