
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;

import static com.daxiong.fun.R.id.btn_cancle;
import static com.daxiong.fun.R.id.btn_ok;

/**
 * 提示dialog
 * 
 * @author: sky
 */
public class CustomModifyPhoneDialog extends Dialog {

    private Activity context;
    TextView tv_title =null;
    TextView tv_content = null;
    public LinearLayout layout_not_force_update = null;
    public Button btn_left = null;
    public Button btn_right = null;
    public LinearLayout layout_force_update = null;


    private ClickListenerInterface clickListenerInterface;


    public CustomModifyPhoneDialog(Activity context,String title,String message,String buttonLeftStr,String buttonRightStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context,title,message,buttonLeftStr, buttonRightStr);
        setAttributes();
    }

    private void setCustomDialog(Context ctx,String  title,String message, String buttonLeftStr,String buttonRightStr) {
        View mCheckUpdateView = View.inflate(ctx, R.layout.dialog_custom_modify_phone_layout, null);
        tv_title = (TextView) mCheckUpdateView.findViewById(R.id.tv_title);
        tv_content = (TextView) mCheckUpdateView.findViewById(R.id.tv_content);

        btn_left = (Button) mCheckUpdateView.findViewById(R.id.btn_left);
        btn_right = (Button) mCheckUpdateView.findViewById(R.id.btn_right);

        tv_title.setText(title);
        tv_content.setText(message);
        btn_left.setText(buttonLeftStr);
        btn_right.setText(buttonRightStr);
        this.setCancelable(false);
        btn_left.setOnClickListener(new clickListener());
        btn_right.setOnClickListener(new clickListener());

        super.setContentView(mCheckUpdateView);
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
                case R.id.btn_right:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.btn_left:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }


    public interface ClickListenerInterface {

        void doConfirm();

        void doCancel();


    }



}
