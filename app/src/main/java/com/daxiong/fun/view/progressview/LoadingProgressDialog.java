package com.daxiong.fun.view.progressview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.util.DensityUtil;

/**
 * Created by Sky on 2016/6/20 0020.
 */

public class LoadingProgressDialog extends Dialog {

private Context context;
    private View mView;
    private TextView mLabelText;
    private String mDetailsLabel;
    private FrameLayout mCustomViewContainer;
    private BackgroundLayout mBackgroundLayout;
    private int mWidth, mHeight;

    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private int mAnimateSpeed;
    private boolean mIsAutoDismiss;

    public LoadingProgressDialog(Context context) {
        super(context,R.style.waiting_dialog_style);
        this.context=context;
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kprogresshud_hud);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = mDimAmount;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        //是否允许点击消失
        setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        mBackgroundLayout = (BackgroundLayout) findViewById(R.id.background);
        mBackgroundLayout.setBaseColor(mWindowColor);
        mBackgroundLayout.setCornerRadius(mCornerRadius);
        if (mWidth != 0) {
            updateBackgroundSize();
        }
        ImageView img = (ImageView)findViewById(R.id.img);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.waiting_animation);
        // 显示动画
        img.startAnimation(animation);
        mLabelText = (TextView) findViewById(R.id.label);
        mLabelText.setVisibility(View.VISIBLE);
        mLabelText.setText("请稍等...");

    }



    private void updateBackgroundSize() {
        ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
        params.width = DensityUtil.dip2px(getContext(),mWidth);
        params.height = DensityUtil.dip2px(getContext(),mHeight);
        mBackgroundLayout.setLayoutParams(params);
    }



}
