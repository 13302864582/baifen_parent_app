package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.util.LogUtils;

/****
 * 这里你要明白几个方法执行的流程： 首先ImageView是继承自View的子类.
 * onLayout方法：是一个回调方法.该方法会在在View中的layout方法中执行，在执行layout方法前面会首先执行setFrame方法.
 * layout方法：
 * setFrame方法：判断我们的View是否发生变化，如果发生变化，那么将最新的l，t，r，b传递给View，然后刷新进行动态更新UI.
 * 并且返回ture.没有变化返回false. invalidate方法：用于刷新当前控件,
 * 
 * @author
 */
public class DragImageView extends NetworkImageView implements OnImageLoadListener {

	public static final String TAG = DragImageView.class.getSimpleName();

	private boolean canDrag = true;

	private int screen_W, screen_H;// 可见屏幕的宽高度

	private int bitmap_W, bitmap_H;// 当前图片宽高

	private int MAX_W, MAX_H, MIN_W, MIN_H;// 极限值

	private int current_Top, current_Right, current_Bottom, current_Left;// 当前图片上下左右坐标

	private int start_Top = -1, start_Right = -1, start_Bottom = -1, start_Left = -1;// 初始化默认位置.

	private int start_x, start_y, current_x, current_y;// 触摸位置

	private float beforeLenght, afterLenght;// 两触点距离

	private float scale_temp;// 缩放比例

	private Bitmap tempBitmap;

	/**
	 * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
	 * 
	 * @author
	 */
	private enum MODE {
		NONE, DRAG, ZOOM
	};

	private MODE mode = MODE.NONE;// 默认模式

	private boolean isControl_V = false;// 垂直监控

	private boolean isControl_H = false;// 水平监控

	private boolean isScaleAnim = false;// 缩放动画

	private MyAsyncTask myAsyncTask;// 异步动画

	/** 能否向左移动 */
	private boolean canMoveLeft = false;
	/** 能否向右移动 */
	private boolean canMoveRight = false;

	/** 构造方法 **/
	public DragImageView(Context context) {
		super(context);
	}

	/** 可见屏幕宽高度 **/
	public void setScreenSize(int screen_W, int screen_H) {
		if (screen_W == 0 || screen_H == 0) {
			return;
		}
		this.screen_W = screen_W;
		this.screen_H = screen_H;
		// LogUtils.d(TAG, "yh sw=" + screen_W + "; sh=" + screen_H);
		if (null != tempBitmap) {
			setImageBitmap(tempBitmap);
		}
	}

	public DragImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 设置显示图片
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);

		if (null == bm) {
			return;
		}

		if (screen_W == 0 || screen_H == 0) {
			tempBitmap = bm;
			return;
		} else {
			tempBitmap = null;
		}

		/** 获取图片宽高 **/
		bitmap_W = bm.getWidth();
		bitmap_H = bm.getHeight();

		resetLayoutParams();

		LogUtils.d(TAG, "yh init l=" + getLeft() + "; t=" + getTop() + "; r=" + getRight() + "; b=" + getBottom());

		// MAX_W = bitmap_W * 4;
		// MAX_H = bitmap_H * 4;

		MAX_W = screen_W * 4;
		MAX_H = screen_H * 4;

		// MIN_W = (int) (bitmap_W * 0.5F);
		// MIN_H = (int) (bitmap_H * 0.5F);

		MIN_W = (int) (bitmap_H * 0.2F);
		MIN_H = (int) (bitmap_H * 0.2F);
	}

	private void resetLayoutParams() {
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();
		if (bitmap_H > bitmap_W) {
			lp.height = screen_H;
			lp.width = (int) (screen_H / ((bitmap_H * 1F) / bitmap_W));
		} else {
			lp.width = screen_W;
			lp.height = (int) (screen_W / ((bitmap_W * 1F) / bitmap_H));
		}
		setLayoutParams(lp);
		// LogUtils.d(TAG, "yh w=" + lp.width + "; h=" + lp.height);
		if (null != mOnScaleListener) {
			mOnScaleListener.onScale(false);
		}
		// GlobalVariable.mViewPager.isMoveInPager = false;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (left == right || top == bottom) {
			return;
		}
		if (start_Top == -1) {
			start_Top = top;
			start_Left = left;
			start_Bottom = bottom;
			start_Right = right;
		}
	}

	/***
	 * touch 事件
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canDrag) {
			/** 处理单点、多点触摸 **/
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				onTouchDown(event);
				break;
			// 多点触摸
			case MotionEvent.ACTION_POINTER_DOWN:
				onPointerDown(event);
				break;
			case MotionEvent.ACTION_MOVE:
				onTouchMove(event);
				break;
			case MotionEvent.ACTION_UP:
				mode = MODE.NONE;
				if (isScaleAnim) {
					doScaleAnim();
				}
				break;
			// 多点松开
			case MotionEvent.ACTION_POINTER_UP:
				mode = MODE.NONE;
				/** 执行缩放还原 **/
				if (isScaleAnim) {
					doScaleAnim();
				}
				break;
			}
			
				return true;
		

		} else {
			return super.onTouchEvent(event);
		}
	}

	/** 按下 **/
	void onTouchDown(MotionEvent event) {
		mode = MODE.DRAG;

		current_x = (int) event.getRawX();
		current_y = (int) event.getRawY();

		start_x = (int) event.getX();
		start_y = current_y - this.getTop();
	}

	/** 两个手指 只能放大缩小 **/
	void onPointerDown(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			mode = MODE.ZOOM;
			beforeLenght = getDistance(event);// 获取两点的距离
		}
	}

	/** 移动的处理 **/
	void onTouchMove(MotionEvent event) {
		int left = 0, top = 0, right = 0, bottom = 0;
		/** 处理拖动 **/
		if (mode == MODE.DRAG) {
			/** 在这里要进行判断处理，防止在drag时候越界 **/

			/** 获取相应的l，t,r ,b **/
			left = current_x - start_x;
			right = current_x + this.getWidth() - start_x;
			top = current_y - start_y;
			bottom = current_y - start_y + this.getHeight();

			// LogUtils.d(TAG, "v.left=" + this.getLeft() + ";v.right=" +
			// this.getRight());

			/** 移动方向 */
			boolean moveToLeft = start_x - (int) event.getX() < 0 ? true : false;
			// LogUtils.d(TAG, "向" + (moToLeft ? "左" : "右") + ";开始x=" + start_x
			// + ";结束x=" + (int)event.getX());

			if (this.getLeft() >= 0) {
				canMoveLeft = false;
			} else {
				canMoveLeft = true;
			}

			if (this.getRight() <= screen_W) {
				canMoveRight = false;
			} else {
				canMoveRight = true;
			}

			/** 水平进行判断 **/
			if (isControl_H) {
				if (left >= 0) {
					left = 0;
					right = this.getWidth();
				}
				if (right <= screen_W) {
					left = screen_W - this.getWidth();
					right = screen_W;
				}
			} else {
				left = this.getLeft();
				right = this.getRight();
			}

			// boolean isScaled = false;
			// if (moveToLeft && canMoveLeft) {
			// isScaled = true;
			// } else if (!moveToLeft && canMoveRight) {
			// isScaled = true;
			// } else {
			// isScaled = false;
			// }
			if (/*
				 * GlobalVariable.mOneQuestionMoreAnswersDetailFragment != null
				 * &&
				 */GlobalVariable.mViewPager != null) {
				// isScaled = GlobalVariable.mViewPager.isMoveInPager =
				// isScaled;
				if (moveToLeft && canMoveLeft) {
					/* isScaled = */GlobalVariable.mViewPager.isMoveInPager = true;
				} else if (!moveToLeft && canMoveRight) {
					/* isScaled = */GlobalVariable.mViewPager.isMoveInPager = true;
				} else {
					if(!GlobalVariable.mViewPager.misMove){
					/* isScaled = */GlobalVariable.mViewPager.isMoveInPager = false;
					}
				}
			}

			// if(null != mOnScaleListener){
			// mOnScaleListener.onScale(isScaled);
			// }

			/** 垂直判断 **/
			if (isControl_V) {
				if (top >= 0) {
					top = 0;
					bottom = this.getHeight();
				}

				if (bottom <= screen_H) {
					top = screen_H - this.getHeight();
					bottom = screen_H;
				}
			} else {
				top = this.getTop();
				bottom = this.getBottom();
			}
			if (isControl_H || isControl_V)
				this.setPosition(left, top, right, bottom);

			current_x = (int) event.getRawX();
			current_y = (int) event.getRawY();

		}
		/** 处理缩放 **/
		else if (mode == MODE.ZOOM) {

			afterLenght = getDistance(event);// 获取两点的距离
			float gapLenght = afterLenght - beforeLenght;// 变化的长度
			if (Math.abs(gapLenght) > 5f) {
				scale_temp = afterLenght / beforeLenght;// 求的缩放的比例
				this.setScale(scale_temp);
				beforeLenght = afterLenght;
				if (null != mOnScaleListener) {
					mOnScaleListener.onScale(true);
				}
			}
		}

	}

	/** 获取两点的距离 **/
	@SuppressLint("FloatMath")
	float getDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

//		return FloatMath.sqrt(x * x + y * y);
		return (float)Math.sqrt(x * x + y * y);
	}

	/** 获取两点中间的点 **/
	PointF middle(MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		return new PointF(x / 2, y / 2);
	}

	/** 实现处理拖动 **/
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);
	}

	/** 处理缩放 **/
	void setScale(float scale) {
		int disX = (int) (this.getWidth() * Math.abs(1 - scale) / 2);// 获取缩放水平距离
		int disY = (int) (this.getHeight() * Math.abs(1 - scale) / 2);// 获取缩放垂直距离

		// LogUtils.d(TAG, "scale=" + scale + ";disX =" + disX + ";disY=" +
		// disY);
		// LogUtils.d(TAG, "scale=" + scale + ";width=" + this.getWidth() +
		// ";height=" + this.getHeight());

		// 放大
		if (scale > 1 && this.getWidth() < MAX_W && this.getHeight() < MAX_H) {
			current_Left = this.getLeft() - disX;
			current_Top = this.getTop() - disY;
			current_Right = this.getRight() + disX;
			current_Bottom = this.getBottom() + disY;

			this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
			/***
			 * 此时因为考虑到对称，所以只做一遍判断就可以了。
			 */
			if (current_Top <= 0 && current_Bottom >= screen_H) {
				isControl_V = true;// 开启垂直监控
			} else {
				isControl_V = false;
			}
			if (current_Left <= 0 && current_Right >= screen_W) {
				isControl_H = true;// 开启水平监控
			} else {
				isControl_H = false;
			}

		}
		// 缩小
		else if (scale < 1 && (this.getWidth() > MIN_W || this.getHeight() > MIN_H)) {
			current_Left = this.getLeft() + disX;
			current_Top = this.getTop() + disY;
			current_Right = this.getRight() - disX;
			current_Bottom = this.getBottom() - disY;

			// LogUtils.d(TAG, "b=" + current_Bottom + ";screen_H=" + screen_H);

			/***
			 * 在这里要进行缩放处理
			 */
			// 上边越界
			if (isControl_V && current_Top > 0) {
				current_Top = 0;
				current_Bottom = this.getBottom() - 2 * disY;
				if (current_Bottom < screen_H) {
					current_Bottom = screen_H;
					isControl_V = false;// 关闭垂直监听
				}
			}
			// 下边越界
			if (isControl_V && current_Bottom < screen_H) {
				current_Bottom = screen_H;
				current_Top = this.getTop() + 2 * disY;
				if (current_Top > 0) {
					current_Top = 0;
					isControl_V = false;// 关闭垂直监听
				}
			}

			// 左边越界
			if (isControl_H && current_Left >= 0) {
				current_Left = 0;
				current_Right = this.getRight() - 2 * disX;
				if (current_Right <= screen_W) {
					current_Right = screen_W;
					isControl_H = false;// 关闭
				}
			}
			// 右边越界
			if (isControl_H && current_Right <= screen_W) {
				current_Right = screen_W;
				current_Left = this.getLeft() + 2 * disX;
				if (current_Left >= 0) {
					current_Left = 0;
					isControl_H = false;// 关闭
				}
			}

			if (isControl_H || isControl_V) {
				this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
			} else {
				this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
				isScaleAnim = true;// 开启缩放动画
			}

		}

	}

	/***
	 * 缩放动画处理
	 */
	public void doScaleAnim() {

		// setFrame(0, start_Top, screen_W, start_Bottom + bitmap_H);

		myAsyncTask = new MyAsyncTask(screen_W, screen_H, this.getWidth(), this.getHeight());
		myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
		myAsyncTask.execute();

		isScaleAnim = false;// 关闭动画
	}

	/***
	 * 回缩动画執行
	 */
	class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
		private int screen_W, screen_H, current_Width, current_Height;

		private int left, top, right, bottom;

		private float scale_WH;// 宽高的比例

		/** 当前的位置属性 **/
		public void setLTRB(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
			LogUtils.d(TAG, "b=" + bottom);
		}

		private float STEP = 6f;// 步伐

		private float step_H, step_V;// 水平步伐，垂直步伐

		public MyAsyncTask(int screen_W, int screen_H, int current_Width, int current_Height) {
			super();
			this.screen_W = screen_W;
			this.screen_H = screen_H;
			this.current_Width = current_Width;
			this.current_Height = current_Height;
			scale_WH = (float) current_Height / (float) current_Width;
			step_H = STEP;
			step_V = scale_WH * STEP;
		}

		@Override
		protected Void doInBackground(Void... params) {

			while (current_Width <= screen_W && current_Height <= screen_H) {

				left -= step_H;
				top -= step_V;
				right += step_H;
				bottom += step_V;

				current_Width += 2 * step_H;
				current_Height += 2 * step_V;

				if (left <= 0 || top <= 0 || right >= screen_W || bottom >= screen_H) {
					break;
				}

				left = Math.max(left, start_Left);
				top = Math.max(top, start_Top);
				right = Math.min(right, start_Right);
				bottom = Math.min(bottom, start_Bottom);

				// LogUtils.e(TAG, "top=" + top + ",bottom=" + bottom + ",left="
				// + left + ",right=" + right);
				// LogUtils.e(TAG, "start_Top=" + start_Top + ",start_Right=" +
				// start_Right);

				onProgressUpdate(new Integer[] { left, top, right, bottom });
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			((Activity) DragImageView.this.getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					resetLayoutParams();
				}
			});

			return null;
		}

		@Override
		protected void onProgressUpdate(final Integer... values) {
			super.onProgressUpdate(values);
			((Activity) DragImageView.this.getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// int s1 = values[0];
					setFrame(values[0], values[1], values[2], values[3]);
					// s1 = values[1];
					// int sd = s1;
				}
			});
		}
	}

	@Override
	public void onFailed(VolleyError arg0) {

	}

	@Override
	public void onSuccess(ImageContainer arg0) {
		if (null != arg0 && null != arg0.getBitmap()) {
			// setImageBitmap(arg0.getBitmap());
		} else {

		}
	}

	public void setEnableDrag(boolean canDrag) {
		this.canDrag = canDrag;
	}

	

	private OnScaleListener mOnScaleListener;

	public void setOnScaleListener(OnScaleListener listener) {
		mOnScaleListener = listener;
	}

	public interface OnScaleListener {
		void onScale(boolean isScale);
	}
}
