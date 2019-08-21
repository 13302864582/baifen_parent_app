
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
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
 * 辅导券发布作业提示框dialog
 * 
 * @author: sky
 */
public class CustomFudaoquanPublishTipDialog extends Dialog {

	private TextView tv_title;

	private TextView tv_content1, tv_content2;

	private Button btn_cancle, btn_ok;

	private Activity context;

	public CustomFudaoquanPublishTipDialog(Activity context, String title, String content1, String content2,
			String button1Str, String button2Str) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
		setCustomDialog(context, title, content1, content2, button1Str, button2Str);
		setCanceledOnTouchOutside(false);
		setAttributes();
	}

	private void setCustomDialog(Context ctx, String title, String content1, String content2, String button1Str,
			String button2Str) {
		View mView = View.inflate(ctx, R.layout.custom_fudaoquan_publish_tip_dialog, null);
		tv_title = (TextView) mView.findViewById(R.id.tv_title);
		tv_content1 = (TextView) mView.findViewById(R.id.tv_content1);
		tv_content2 = (TextView) mView.findViewById(R.id.tv_content2);

		btn_cancle = (Button) mView.findViewById(R.id.btn_cancle);
		btn_ok = (Button) mView.findViewById(R.id.btn_yes);
		tv_title.setText(title);
		tv_content1.setText(Html.fromHtml(content1));
		tv_content2.setText(content2);
		btn_cancle.setText(button1Str);
		btn_ok.setText(button2Str);		
	
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

	public void setOnCanclesListener(View.OnClickListener listener) {
		btn_cancle.setOnClickListener(listener);

	}

	public void setOnOKListener(View.OnClickListener listener) {
		btn_ok.setOnClickListener(listener);

	}


}
