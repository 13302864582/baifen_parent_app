
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
import android.widget.TextView;

import com.daxiong.fun.R;

/**
 * 发布作业和问题放弃的dialog
 *
 * @author: sky
 */
public class CustomPublishGiveupDialog extends Dialog {

    private Activity context;
    public Button btn_left ;
    public Button btn_right;

    private TextView tv_content;


    private ClickListenerInterface clickListenerInterface;


    public CustomPublishGiveupDialog(Activity context, String buttonLeftStr,String buttonRightStr,String content) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context, buttonLeftStr,buttonRightStr,content);

    }



    private void setCustomDialog(Context ctx, String buttonLeftStr,String buttonRightStr,String content) {
        View view = View.inflate(ctx, R.layout.custom_publish_giveup_dialog, null);

        btn_left = (Button) view.findViewById(R.id.btn_left);
        btn_right = (Button) view.findViewById(R.id.btn_right);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        btn_left.setText(buttonLeftStr);
        tv_content.setText(content);
        btn_right.setText(buttonRightStr);

        this.setCancelable(false);

        btn_left.setOnClickListener(new clickListener());
        btn_right.setOnClickListener(new clickListener());

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
                case R.id.btn_left:
                    clickListenerInterface.doLeft();
                    break;
                case R.id.btn_right:
                    clickListenerInterface.doRight();
                    break;
            }
        }

    }


    public interface ClickListenerInterface {
        void doLeft();
        void doRight();

    }


}
