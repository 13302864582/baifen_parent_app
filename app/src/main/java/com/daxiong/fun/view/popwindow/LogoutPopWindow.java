package com.daxiong.fun.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.daxiong.fun.R;

/**
 * Created by Sky on 2016/7/4 0004.
 */

public class LogoutPopWindow extends PopupWindow implements View.OnClickListener {

    private Button btn_exit_login, btn_cancel;
    private ILogoutListener logoutListener;

    public LogoutPopWindow(Activity activity, ILogoutListener logoutListener) {
        this.logoutListener = logoutListener;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_logout_popwindow, null);
        btn_exit_login = (Button) view.findViewById(R.id.btn_exit_login);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_exit_login.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.logout_pop_window);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框


        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_login:
                logoutListener.doSure();
                break;
            case R.id.btn_cancel:
                logoutListener.doCancle();
                break;

        }

    }


    public interface ILogoutListener {
        void doSure();
        void doCancle();
    }
}
