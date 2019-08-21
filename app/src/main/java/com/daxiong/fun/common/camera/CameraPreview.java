package com.daxiong.fun.common.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.TakePhotoUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Class: CameraPreview
 * @Description: 自定义相机
 * @author: lling(www.cnblogs.com/liuling)
 * @Date: 2015/10/25
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, AutoFocusCallback {
	private static final String TAG = "CameraPreview";

	private int viewWidth = 0;
	private int viewHeight = 0;

	/** 监听接口 */
	private OnCameraStatusListener listener;

	private SurfaceHolder holder;
	private Camera camera;
	private FocusView mFocusView;

	public Size size;

	private Camera.Size adapterSize = null;
	private Camera.Size previewSize = null;

	// 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
	private PictureCallback pictureCallback = new PictureCallback() {

		// 该方法用于处理拍摄后的照片数据
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 停止照片拍摄
			try {
				camera.stopPreview();
			} catch (Exception e) {
			}
			// 调用结束事件
			if (null != listener) {
				listener.onCameraStopped(data);

			}
		}
	};

	// Preview类的构造方法
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获得SurfaceHolder对象
		holder = getHolder();
		// 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
		holder.addCallback(this);
		// 设置SurfaceHolder对象的类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setOnTouchListener(onTouchListener);
	}

	// 在surface创建时激发
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "==surfaceCreated==");
		if (!TakePhotoUtils.checkCameraHardware(getContext())) {
			Toast.makeText(getContext(), "摄像头打开失败！", Toast.LENGTH_SHORT).show();
			return;
		}
		// 获得Camera对象
		camera = getCameraInstance();
		adjustLight(2);
		try {
			// 设置用于显示拍照摄像的SurfaceHolder对象
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			// 释放手机摄像头
			camera.release();
			camera = null;
		}
		updateCameraParameters();
		if (camera != null) {
			camera.startPreview();
		}
		setFocus();
	}

	// 在surface销毁时激发
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "==surfaceDestroyed==");
		// 释放手机摄像头
		camera.release();
		camera = null;
	}

	// 在surface的大小发生改变时激发
	public void surfaceChanged(final SurfaceHolder holder, int format, int w, int h) {
		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		// set preview size and make any resize, rotate or
		// reformatting changes here
		updateCameraParameters();
		// start preview with new settings
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
		setFocus();
	}

	/**
	 * 点击显示焦点区域
	 */
	OnTouchListener onTouchListener = new OnTouchListener() {
		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int width = mFocusView.getWidth();
				int height = mFocusView.getHeight();
				mFocusView.setX(event.getX() - (width / 2));
				mFocusView.setY(event.getY() - (height / 2));
				mFocusView.beginFocus();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				focusOnTouch(event);
			}
			return true;
		}
	};

	/**
	 * 获取摄像头实例
	 * 
	 * @return
	 */
	private Camera getCameraInstance() {
		Camera c = null;
		try {
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras(); // get cameras number

			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
				// 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						c = Camera.open(camIdx); // 打开后置摄像头
					} catch (RuntimeException e) {
						Toast.makeText(getContext(), "摄像头打开失败！", Toast.LENGTH_SHORT).show();
					}
				}
			}
			if (c == null) {
				c = Camera.open(0); // attempt to get a Camera instance
			}
		} catch (Exception e) {
			Toast.makeText(getContext(), "摄像头打开失败！", Toast.LENGTH_SHORT).show();
		}
		return c;
	}

	private void updateCameraParameters() {
		if (camera != null) {
			Camera.Parameters p = camera.getParameters();
			setParameters(p);
			try {
				camera.setParameters(p);
			} catch (Exception e) {
				Camera.Size previewSize = findBestPreviewSize(p);
				p.setPreviewSize(previewSize.width, previewSize.height);
				p.setPictureSize(previewSize.width, previewSize.height);
				camera.setParameters(p);
			}
		}
	}

	/**
	 * @param p
	 */
	private void setParameters(Camera.Parameters p) {
		List<String> focusModes = p.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		}

		long time = new Date().getTime();
		p.setGpsTimestamp(time);
		// 设置照片格式
		p.setPictureFormat(PixelFormat.JPEG);
		// 1.第一种
		// Camera.Size previewSize = findPreviewSizeByScreen(p);
		// p.setPreviewSize(previewSize.width, previewSize.height);
		// p.setPictureSize(size.width, size.height);
		/*
		 * 第二种 List<Size> sizeList = p.getSupportedPreviewSizes(); size = null;
		 * if (sizeList != null && sizeList.size() > 0) { Size firstSize =
		 * sizeList.get(0); if (sizeList.size() > 2) { Size secondSize =
		 * sizeList.get(1); if (firstSize.width > secondSize.width) { size =
		 * firstSize; } else { size = sizeList.get(sizeList.size() - 1); } }
		 * else { size = firstSize; } }
		 * 
		 * if (size != null) { p.setPreviewSize(size.width, size.height); }
		 */
		// 第三种
		adapterSize = findBestPictureResolution();
		previewSize = findBestPreviewResolution();
		p.setPictureSize(adapterSize.width, adapterSize.height);
		p.setPreviewSize(previewSize.width, previewSize.height);

		p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		/*if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			camera.setDisplayOrientation(90);
			p.setRotation(90);
		}*/

		if (Build.VERSION.SDK_INT >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			p.setRotation(90);
		}
	}

	private void setDisplayOrientation(Camera camera, int i) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null) {
				downPolymorphic.invoke(camera, new Object[] { i });
			}
		} catch (Exception e) {
			Log.e("Came_e", "图像出错");
		}
	}

	// 进行拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
	public void takePicture() {
		if (camera != null) {
			try {
				shootSound();
				camera.takePicture(null, null, pictureCallback);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 设置监听事件
	public void setOnCameraStatusListener(OnCameraStatusListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {

	}

	public void start() {
		if (camera != null) {
			camera.startPreview();
		}
	}

	public void stop() {
		if (camera != null) {
			camera.stopPreview();
		}
	}

	/**
	 * 相机拍照监听接口
	 */
	public interface OnCameraStatusListener {
		// 相机拍照结束事件
		void onCameraStopped(byte[] data);
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		viewWidth = MeasureSpec.getSize(widthSpec);
		viewHeight = MeasureSpec.getSize(heightSpec);
		super.onMeasure(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY));
	}

	/**
	 * 将预览大小设置为屏幕大小
	 * 
	 * @param parameters
	 * @return
	 */
	private Camera.Size findPreviewSizeByScreen(Camera.Parameters parameters) {
		if (viewWidth != 0 && viewHeight != 0) {
			return camera.new Size(Math.max(viewWidth, viewHeight), Math.min(viewWidth, viewHeight));
		} else {
			return camera.new Size(TakePhotoUtils.getScreenWH(getContext()).heightPixels,
					TakePhotoUtils.getScreenWH(getContext()).widthPixels);
		}
	}

	/**
	 * 找到最合适的显示分辨率 （防止预览图像变形）
	 * 
	 * @param parameters
	 * @return
	 */
	private Camera.Size findBestPreviewSize(Camera.Parameters parameters) {

		// 系统支持的所有预览分辨率
		String previewSizeValueString = null;
		previewSizeValueString = parameters.get("preview-size-values");

		if (previewSizeValueString == null) {
			previewSizeValueString = parameters.get("preview-size-value");
		}

		if (previewSizeValueString == null) { // 有些手机例如m9获取不到支持的预览大小 就直接返回屏幕大小
			return camera.new Size(TakePhotoUtils.getScreenWH(getContext()).widthPixels,
					TakePhotoUtils.getScreenWH(getContext()).heightPixels);
		}
		float bestX = 0;
		float bestY = 0;

		float tmpRadio = 0;
		float viewRadio = 0;

		if (viewWidth != 0 && viewHeight != 0) {
			viewRadio = Math.min((float) viewWidth, (float) viewHeight)
					/ Math.max((float) viewWidth, (float) viewHeight);
		}

		String[] COMMA_PATTERN = previewSizeValueString.split(",");
		for (String prewsizeString : COMMA_PATTERN) {
			prewsizeString = prewsizeString.trim();

			int dimPosition = prewsizeString.indexOf('x');
			if (dimPosition == -1) {
				continue;
			}

			float newX = 0;
			float newY = 0;

			try {
				newX = Float.parseFloat(prewsizeString.substring(0, dimPosition));
				newY = Float.parseFloat(prewsizeString.substring(dimPosition + 1));
			} catch (NumberFormatException e) {
				continue;
			}

			float radio = Math.min(newX, newY) / Math.max(newX, newY);
			if (tmpRadio == 0) {
				tmpRadio = radio;
				bestX = newX;
				bestY = newY;
			} else if (tmpRadio != 0 && (Math.abs(radio - viewRadio)) < (Math.abs(tmpRadio - viewRadio))) {
				tmpRadio = radio;
				bestX = newX;
				bestY = newY;
			}
		}

		if (bestX > 0 && bestY > 0) {
			return camera.new Size((int) bestX, (int) bestY);
		}
		return null;
	}

	/**
	 * 设置焦点和测光区域
	 *
	 * @param event
	 */
	public void focusOnTouch(MotionEvent event) {

		int[] location = new int[2];
		RelativeLayout relativeLayout = (RelativeLayout) getParent();
		relativeLayout.getLocationOnScreen(location);

		Rect focusRect = TakePhotoUtils.calculateTapArea(mFocusView.getWidth(), mFocusView.getHeight(), 1f,
				event.getRawX(), event.getRawY(), location[0], location[0] + relativeLayout.getWidth(), location[1],
				location[1] + relativeLayout.getHeight());
		Rect meteringRect = TakePhotoUtils.calculateTapArea(mFocusView.getWidth(), mFocusView.getHeight(), 1.5f,
				event.getRawX(), event.getRawY(), location[0], location[0] + relativeLayout.getWidth(), location[1],
				location[1] + relativeLayout.getHeight());

		Camera.Parameters parameters = camera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

		if (parameters.getMaxNumFocusAreas() > 0) {
			List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
			focusAreas.add(new Camera.Area(focusRect, 1000));

			parameters.setFocusAreas(focusAreas);
		}

		if (parameters.getMaxNumMeteringAreas() > 0) {
			List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
			meteringAreas.add(new Camera.Area(meteringRect, 1000));

			parameters.setMeteringAreas(meteringAreas);
		}

		try {
			camera.setParameters(parameters);
		} catch (Exception e) {
		}
		camera.autoFocus(this);
	}

	/**
	 * 设置聚焦的图片
	 * 
	 * @param focusView
	 */
	public void setFocusView(FocusView focusView) {
		this.mFocusView = focusView;
	}

	/**
	 * 设置自动聚焦，并且聚焦的圈圈显示在屏幕中间位置
	 */
	public void setFocus() {
		if (!mFocusView.isFocusing()) {
			try {
				camera.autoFocus(this);
				mFocusView.setX((TakePhotoUtils.getWidthInPx(getContext()) - mFocusView.getWidth()) / 2);
				mFocusView.setY((TakePhotoUtils.getHeightInPx(getContext()) - mFocusView.getHeight()) / 2);
				mFocusView.beginFocus();
			} catch (Exception e) {
			}
		}
	}

	//
	// public void setShanguangdeng(){
	//
	// Camera.Parameters parameters = null;
	// //直接开启
	// camera = Camera.open();
	// parameters = camera.getParameters();
	// parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
	// camera.setParameters(parameters);
	// //直接关闭
	//// parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
	//// camera.setParameters(parameters);
	//// camera.release();
	// }

	/**
	 * 控制闪光灯
	 * 
	 * @param flag
	 */
	public void adjustLight(int flag) {

		if (null == camera) {
			camera = Camera.open();
		}

		Camera.Parameters parameters = camera.getParameters();// 获取相机参数集
		if (parameters.getFlashMode() == null) {
			// Toast.makeText(this, "无闪光灯", Toast.LENGTH_SHORT).show();
			Toast.makeText(this.getContext(), "无闪光灯", 4).show();
			return;
		}

		switch (flag) {
		case 0:// 自动
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			camera.setParameters(parameters);
			break;
		case 1:// 开启闪光灯
			if (camera != null) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				camera.autoFocus(new Camera.AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {

					}
				});
				camera.startPreview();
			}

			break;
		case 2:// 关闭闪光灯
				// if (camera != null) {
				// camera.stopPreview();
				// camera.release();
				// camera = null;
				// }

			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.startPreview();

			break;

		}

	}

	/**
	 * 播放系统拍照声音
	 */
	public void shootSound() {
		AudioManager meng = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
		int volume = meng.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		MediaPlayer shootMP = null;
		if (volume != 0) {
			if (shootMP == null)
				shootMP = MediaPlayer.create(getContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
			if (shootMP != null)
				shootMP.start();
		}
	}

	/***********************************************/
	private static final int MIN_PREVIEW_PIXELS = 480 * 320;
	private static final double MAX_ASPECT_DISTORTION = 0.15;

	private Camera.Size findBestPreviewResolution() {
		Camera.Parameters cameraParameters = camera.getParameters();
		Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();

		List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
		if (rawSupportedSizes == null) {
			return defaultPreviewResolution;
		}

		List<Camera.Size> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
		Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
			@Override
			public int compare(Camera.Size a, Camera.Size b) {
				int aPixels = a.height * a.width;
				int bPixels = b.height * b.width;
				if (bPixels < aPixels) {
					return -1;
				}
				if (bPixels > aPixels) {
					return 1;
				}
				return 0;
			}
		});

		StringBuilder builder = new StringBuilder();
		for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
			builder.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
					.append(' ');
		}

		double screenAspectRatio = (double) DensityUtil.getScreenWidth(getContext());
		Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
		while (it.hasNext()) {
			Camera.Size supportedPreviewResolution = it.next();
			int width = supportedPreviewResolution.width;
			int height = supportedPreviewResolution.height;

			if (width * height < MIN_PREVIEW_PIXELS) {
				it.remove();
				continue;
			}

			boolean isCandidatePortrait = width > height;
			int maybeFlippedWidth = isCandidatePortrait ? height : width;
			int maybeFlippedHeight = isCandidatePortrait ? width : height;
			double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION) {
				it.remove();
				continue;
			}

			if (maybeFlippedWidth == DensityUtil.getScreenWidth(getContext())
					&& maybeFlippedHeight == DensityUtil.getScreenHeight(getContext())) {
				return supportedPreviewResolution;
			}
		}
		Camera.Size largestPreview;
		if (!supportedPreviewResolutions.isEmpty()) {
			largestPreview = supportedPreviewResolutions.get(0);
			return largestPreview;
		}
		// defaultPreviewResolution.width=1920;
		// defaultPreviewResolution.height=1088

		return defaultPreviewResolution;
	}

	private Camera.Size findBestPictureResolution() {
		Camera.Parameters cameraParameters = camera.getParameters();
		List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

		StringBuilder picResolutionSb = new StringBuilder();
		for (Camera.Size supportedPicResolution : supportedPicResolutions) {
			picResolutionSb.append(supportedPicResolution.width).append('x').append(supportedPicResolution.height)
					.append(" ");
		}
		Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);
		Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
		Log.d(TAG,
				"default picture resolution " + defaultPictureResolution.width + "x" + defaultPictureResolution.height);

		List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(supportedPicResolutions);
		Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
			@Override
			public int compare(Camera.Size a, Camera.Size b) {
				int aPixels = a.height * a.width;
				int bPixels = b.height * b.width;
				if (bPixels < aPixels) {
					return -1;
				}
				if (bPixels > aPixels) {
					return 1;
				}
				return 0;
			}
		});

		// 移除不符合条件的分辨率
		double screenAspectRatio = (double) DensityUtil.getScreenWidth(getContext())
				/ (double) DensityUtil.getScreenHeight(getContext());
		Iterator<Camera.Size> it = sortedSupportedPicResolutions.iterator();
		while (it.hasNext()) {
			Camera.Size supportedPreviewResolution = it.next();
			int width = supportedPreviewResolution.width;
			int height = supportedPreviewResolution.height;

			// 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
			// 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
			// 因此这里要先交换然后在比较宽高比
			boolean isCandidatePortrait = width > height;
			int maybeFlippedWidth = isCandidatePortrait ? height : width;
			int maybeFlippedHeight = isCandidatePortrait ? width : height;
			double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION) {
				it.remove();
				continue;
			}
		}

		// 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
		if (!sortedSupportedPicResolutions.isEmpty()) {
			return sortedSupportedPicResolutions.get(0);
		}

		// defaultPictureResolution.width=1920;
		// defaultPictureResolution.height=1088;

		return defaultPictureResolution;
	}

}