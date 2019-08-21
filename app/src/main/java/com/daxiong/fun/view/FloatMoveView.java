package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

@SuppressLint("ClickableViewAccessibility")
public class FloatMoveView extends FrameLayout
{
    private ViewDragHelper mDragHelper;
    
    public FloatMoveView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    public FloatMoveView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs,defStyle);
        init();
	}

	public FloatMoveView(Context context) {
		super(context);
        init();
	}

	public void init()
    {
        /**
         * @params ViewGroup forParent 必须是一个ViewGroup
         * @params float sensitivity 灵敏度
         * @params CallBack回调
         */
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
    }
    
    // 回调方法
    private class ViewDragHelperCallBack extends ViewDragHelper.Callback
    {
        // 尝试捕获子View,一定要返回true表示获取到了。
        /**
         * View child 尝试捕获的View int arg1 指示器ID?
         */
        @Override
        public boolean tryCaptureView(View view, int arg1)
        {
//             return mCanDragView == view;
            return true;
        }
        
        // c处理水平方向的拖动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            // 让子View一直在ViewGroup中
            if (getPaddingLeft() > left)
            {
                return getPaddingLeft();//
            }
            if (getWidth() - child.getWidth() < left)
            {
                return getWidth() - child.getWidth();
            }
            return left;
        }
        
        // 垂直方向的移动处理
        @Override
        public int clampViewPositionVertical(View child, int top, int dy)
        {
            if (getPaddingTop() > top)
            {
                return getPaddingTop();
            }
            if (getHeight() - child.getHeight() < top)
            {
                return getHeight() - child.getHeight();
            }
            return top;
        }
        
        // 当拖拽改变时回调
        @Override
        public void onViewDragStateChanged(int state)
        {
            switch (state)
            {
                case ViewDragHelper.STATE_DRAGGING:
                    Log.i("state", "正在被拖动");
                    break;
                case ViewDragHelper.STATE_IDLE:
                    Log.i("state", "view没有被拖拽或者 正在进行fling");
                    break;
                case ViewDragHelper.STATE_SETTLING:
                    Log.i("state", "View  重置完毕了。");
                    
            }
            super.onViewDragStateChanged(state);
        }
        
        
        @Override
        public int getViewHorizontalDragRange(View child)
        {
             return getMeasuredWidth()-child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child)
        {
             return getMeasuredHeight()-child.getMeasuredHeight();
        }

        
    }
    
    // 拦截拖动事件
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 将处理事件铺货给mDragHelper
        mDragHelper.processTouchEvent(event);
        return true;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mDragHelper.cancel();
                break;
        
        }
        // 检查是否可以拦截Touch事件
        // 如果onInterceptTouchEvent可以return true、则这里可以return true
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }
    
}
