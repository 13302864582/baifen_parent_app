
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
 * 提示dialog
 * @author: sky
 */
public class UpdateDialog extends Dialog {
    
    public TextView getTv_code() {
		return tv_code;
	}

	public void setTv_code(TextView tv_code) {
		this.tv_code = tv_code;
	}

	private TextView tv_title,tv_code;

    private TextView tv_content;

    private Button positiveButton, negativeButton;

    private Activity context;

    public Button getPositiveButton() {
        return positiveButton;
    }

    public Button getNegativeButton() {
        return negativeButton;
    }

   

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_content() {
        return tv_content;
    }

   

    public UpdateDialog(Activity context,String title,String content,String leftBtnStr,String rightBtnStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context,title,content,leftBtnStr,rightBtnStr);
        setAttributes();
    }

    private void setCustomDialog(Context ctx,String title,String content,String leftBtnStr,String rightBtnStr) {
        View mView = View.inflate(ctx, R.layout.update_dialog, null);
        tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_code = (TextView)mView.findViewById(R.id.tv_code);
        tv_content = (TextView)mView.findViewById(R.id.tv_content);
        positiveButton = (Button)mView.findViewById(R.id.btn_ok);
        negativeButton = (Button)mView.findViewById(R.id.btn_cancle);
        tv_title.setText(title);
        tv_content.setText(content);
        positiveButton.setText(rightBtnStr);
        negativeButton.setText(leftBtnStr);
        super.setContentView(mView);
    }

    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int)(d.getHeight() * 0.35); // 高度设置为屏幕的0.6
        p.width = (int)(d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
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

    /**
     * 确定键监听器
     * 
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);

    }

    /**
     * 取消键监听器
     * 
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}
