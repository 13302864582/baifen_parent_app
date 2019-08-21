
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.daxiong.fun.R;

/**
 * 索券dialog
 *
 * @author: sky
 */
public class CustomSuoquanDialog extends Dialog {

    private Activity context;
    public TextView btn_bottom = null;
    private TextView tv_content;


    private ClickListenerInterface clickListenerInterface;


    public CustomSuoquanDialog(Activity context,String content, String buttonStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context, content,buttonStr);

    }

    private void setCustomDialog(Context ctx, String content,String buttonStr) {
        View view = View.inflate(ctx, R.layout.custom_suoquan_dialog, null);
        btn_bottom = (TextView) view.findViewById(R.id.btn_bottom);
        btn_bottom.setText(buttonStr);
        tv_content= (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(content);
        this.setCancelable(false);
        btn_bottom.setOnClickListener(new clickListener());
        setAttributes();
        super.setContentView(view);
    }

    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_bottom:
                    clickListenerInterface.doCancel();
                    break;


            }
        }

    }


    public interface ClickListenerInterface {

        void doCancel();
    }


}
