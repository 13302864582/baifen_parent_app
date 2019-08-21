
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daxiong.fun.R;

/**
 * 没有辅导券提示框dialog
 * 
 * @author: sky
 */
public class CustomNoFudaoquanTipDialog extends Dialog {

	private TextView tv_title;

	private TextView tv_content1, tv_content2;

	private Button btn_fangqi, btn_chongzhi, btn_suoquan;

	private Activity context;

	public CustomNoFudaoquanTipDialog(Activity context, String title, String content1, String content2,
			String button1Str, String button2Str) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
		setCustomDialog(context, title, content1, content2, button1Str, button2Str);
		setCanceledOnTouchOutside(false);
		setAttributes();
	}

	private void setCustomDialog(Context ctx, String title, String content1, String content2, String button1Str,
			String button2Str) {
		View mView = View.inflate(ctx, R.layout.custom_nofudaoquan_tip_dialog, null);
		tv_title = (TextView) mView.findViewById(R.id.tv_title);
		tv_content1 = (TextView) mView.findViewById(R.id.tv_content1);
		tv_content2 = (TextView) mView.findViewById(R.id.tv_content2);

		btn_fangqi = (Button) mView.findViewById(R.id.btn_fangqi);
		btn_chongzhi = (Button) mView.findViewById(R.id.btn_chongzhi);

		tv_title.setText(title);
		tv_content1.setText(content1);
		tv_content2.setText(content2);
		btn_fangqi.setText(button1Str);
		btn_chongzhi.setText(button2Str);		

		super.setContentView(mView);
	}

	public void setAttributes() {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.35); // 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
	}

	@Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}

	public void setOnFangqiListener(View.OnClickListener listener) {
		btn_fangqi.setOnClickListener(listener);

	}

	public void setOnSuoquanListener(View.OnClickListener listener) {
		btn_suoquan.setOnClickListener(listener);

	}

	/**
	 * 取消键监听器
	 * 
	 * @param listener
	 */
	public void setOnChongzhiListener(View.OnClickListener listener) {
		btn_chongzhi.setOnClickListener(listener);
	}
}
