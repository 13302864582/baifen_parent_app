
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
 * 选择身份框dialog
 * 
 * @author: sky
 */
public class CustomCommonTipDialog extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	private TextView tv_title;
	private TextView tv_content;
	private Button btn_cancle, btn_ok;      

	private ImyDialogClickListenerLister myDialogClickListenerLister;

	public CustomCommonTipDialog(Activity context, String title, String content, String button1Str, String button2Str) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
		setCustomDialog(context, title, content, button1Str, button2Str);
		setCanceledOnTouchOutside(false);
		setAttributes();
	}

	private void setCustomDialog(Context ctx, String title, String content, String button1Str, String button2Str) {
		View mView = View.inflate(ctx, R.layout.custom_comon_tip_dialog, null);
		tv_title = (TextView) mView.findViewById(R.id.tv_title);
		tv_content = (TextView) mView.findViewById(R.id.tv_content);

		btn_ok = (Button) mView.findViewById(R.id.btn_yes);
		btn_cancle = (Button) mView.findViewById(R.id.btn_cancle);
		tv_title.setText(title);
		tv_content.setText(Html.fromHtml(content));
		btn_cancle.setText(button1Str);
		btn_ok.setText(button2Str);

		btn_ok.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			myDialogClickListenerLister.doConfirm();
			break;
		case R.id.btn_cancle:
			myDialogClickListenerLister.doCancel();
			break;		
		}

	}

	public void setMyDialogClickListenerLister(ImyDialogClickListenerLister myDialogClickListenerLister) {
		this.myDialogClickListenerLister = myDialogClickListenerLister;
	}

	public interface ImyDialogClickListenerLister {
		void doConfirm();

		void doCancel();
	}

}
