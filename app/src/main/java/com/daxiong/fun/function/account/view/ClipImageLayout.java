
package com.daxiong.fun.function.account.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;

    private ClipImageBorderView mClipImageView;

    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 20;

    private BitmapDrawable bd = null;

    private IButtonClick click;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        /**
         * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
         */
        // mZoomImageView.setImageDrawable(getResources().getDrawable(R.drawable.a));

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        RelativeLayout.LayoutParams button1Param = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final Button button1 = new Button(context);
        button1.setId(0);
        button1.setText("确定");
        button1.setTextSize(16);
        button1.setBackgroundResource(0);
        button1.setTextColor(Color.WHITE);
        button1Param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        button1Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        button1Param.setMargins(20, 45, 0, 0);
        addView(button1, button1Param);

        RelativeLayout.LayoutParams button2Param = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final Button button2 = new Button(context);
        button2.setId(1);
        button2.setText("取消");
        button2.setTextSize(16);
        button2.setBackgroundResource(0);
        button2.setTextColor(Color.WHITE);
        button2Param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        button2Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        button2Param.setMargins(0, 45, 20, 0);
        addView(button2, button2Param);

        RelativeLayout.LayoutParams textViewParam = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TextView showTip = new TextView(context);
        showTip.setText("移动图片选取要显示的部分");
        showTip.setTextSize(16);
        showTip.setTextColor(Color.WHITE);
        textViewParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textViewParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textViewParam.setMargins(0, 0, 0, 40);
        addView(showTip,textViewParam);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click.customClick(button1);

            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click.customClick(button2);

            }
        });

        // 计算padding的px
        mHorizontalPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mHorizontalPadding, getResources().getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     * 
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     * 
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

    public void setImagePath(String image_path) {
        // 根据图片的路径生成Drawable对象
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        bd = new BitmapDrawable(getResources(), bitmap);
        mZoomImageView.setImageDrawable(bd);
    }

    public void setButtonClick(IButtonClick ll) {
        this.click = ll;
    }

    public interface IButtonClick {
        void customClick(Button button);
    }

}
