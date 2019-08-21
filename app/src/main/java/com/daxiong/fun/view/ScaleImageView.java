package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.model.FitBitmap;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.LogUtils;

public class ScaleImageView extends FrameLayout implements OnTouchListener {

	// private static final String TAG = ScaleImageView.class.getSimpleName();

	private NetworkImageView mPicView;
	private Matrix matrix = new Matrix();
	// public Bitmap initBm ;
	private Matrix savedMatrix = new Matrix();
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	// 第一个手指按下的店
	private PointF startPoint = new PointF();
	// 两个手指按下的触摸点的中点
	private PointF midPoint = new PointF();
	// 初始时两个手指按下的触摸点的距离
	private float oriDis = 1f;

	public NetworkImageView getmPicView() {
		return mPicView;
	}

	public void setmPicView(NetworkImageView mPicView) {
		this.mPicView = mPicView;
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
	}

	public ScaleImageView(Context context) {
		super(context);
		setUpViews();
	}

	public void loadImage(String url, NetworkImageView.OnImageLoadListener listener) {
		// ImageLoader.getInstance().displayImage(url, mPicView, listener,
		// null);
		ImageLoader.getInstance().loadImage(url, mPicView, 0, listener);
		// if (firstTouch && initBm == null) {
		// initBm = getBitMapFromView(mPicView);
		// firstTouch = false;
		// }
	}

	public void loadImage(String path) {
		FitBitmap fm = ImageUtil.getResizedImage(path, null, null, 600, false, 0);
		if (fm != null) {
			mPicView.setCustomBitmap(fm.getmBitmap());
			// mPicView.setImageBitmap(fm.getmBitmap());
		}
	}

	public void setBitmap2(String path, Activity activity) {
		ImageUtil.loadBitmap(path, mPicView);
	}
	public void setBitmap(String path, Activity activity) {
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
		int outWidth = opts.outWidth;
		int outHeight = opts.outHeight;
		// int ssize = 1;
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		int ssize = 1;
		if (deg == 90 || deg == 270) {
			int temp = outWidth;
			outWidth = outHeight;
			outHeight = temp;
		}

		ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels : outWidth / widthPixels;
		if (ssize <= 0) {
			ssize = 1;

		

		}
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = ssize;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		Bitmap mBitmap = BitmapFactory.decodeFile(path, opts);
		mBitmap = ImageUtil.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
		ImageUtil.saveFile(path, mBitmap);
		mPicView.setCustomBitmap(mBitmap);
	}

	public static Bitmap getBitMapFromView(ImageView view) {
		BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
		Bitmap orgBitmap = drawable.getBitmap();
		return orgBitmap;
	}

	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	private void setUpViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.scale_imageview, null);
		mPicView = (NetworkImageView) view.findViewById(R.id.img);
		mPicView.setDrawingCacheEnabled(true);
		mPicView.setOnTouchListener(this);

		addView(view);
	}

	@SuppressLint("FloatMath")
	private float distance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
//		return FloatMath.sqrt(x * x + y * y);
		return (float)Math.sqrt(x * x + y * y);
	}

	private PointF middle(MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		return new PointF(x / 2, y / 2);
	}

	/*
	 * public interface onClickLinstener{ public void onClick(); }
	 */
	private Activity mActivity;

	private void onClick() {
		if (mActivity != null) {
			mActivity.finish();
		}
	}

	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}

	private long touchTime;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ToastUtils.show(getContext(), v.toString() +":" +(v instanceof
		// ScaleImageView));
		ImageView view = (ImageView) v;
		view.setScaleType(ScaleType.MATRIX);

		// 进行与操作是为了判断多点触摸
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			touchTime = System.currentTimeMillis();
			matrix.set(view.getImageMatrix());
			savedMatrix.set(matrix);
			startPoint.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			// 第二个手指按下事件
			oriDis = distance(event);
			if (oriDis > 10f) {
				savedMatrix.set(matrix);
				midPoint = middle(event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			// 手指放开事件
			mode = NONE;
			if ((System.currentTimeMillis() - touchTime) < 100) {
				onClick();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				float dx = event.getX() - startPoint.x;
				float dy = event.getY() - startPoint.y;
				matrix.postTranslate(dx, dy);
				if (GlobalVariable.mOneQuestionMoreAnswersDetailActivity != null && (GlobalVariable.mViewPager != null)) {
					if (GlobalVariable.mOneQuestionMoreAnswersDetailActivity.currentPositionState == OneQuestionMoreAnswersDetailActivity.ONFIRST) {
						if (dx > 0) {
							GlobalVariable.mViewPager.isMoveInPager = true;
						} else {
							GlobalVariable.mViewPager.isMoveInPager = false;
						}
					} else if (GlobalVariable.mOneQuestionMoreAnswersDetailActivity.currentPositionState == OneQuestionMoreAnswersDetailActivity.ONLAST) {
						if (dx < 0) {
							GlobalVariable.mViewPager.isMoveInPager = true;
						} else {
							GlobalVariable.mViewPager.isMoveInPager = false;
						}
					}
				}

			} else if (mode == ZOOM) {
				float newDist = distance(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oriDis;
					matrix.postScale(scale, scale, midPoint.x, midPoint.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true;
	}

	public void setScaleType(ScaleType scaleType) {
		if (null != mPicView) {
			mPicView.setScaleType(scaleType);
		}
	}
}
