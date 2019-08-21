
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;

/**
 * 支付dialog
 *
 * @author: sky
 */
public class CustomVipQuanDetailDialog extends Dialog {

    private Activity context;
    public TextView btn_left = null;
    private LinearLayout rl_auto_detail;
    private String content;



    private ClickListenerInterface clickListenerInterface;


    public CustomVipQuanDetailDialog(Activity context,String content, String buttonLeftStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        this.content=content;
        setCustomDialog(context, content,buttonLeftStr);
    }

    private void setCustomDialog(Context ctx,String content, String buttonLeftStr) {
        View view = View.inflate(ctx, R.layout.custom_vip_quan_detail_dialog, null);
        rl_auto_detail = (LinearLayout) view.findViewById(R.id.rl_auto_detail);
        btn_left = (TextView) view.findViewById(R.id.btn_left);
        btn_left.setText(buttonLeftStr);
        addDetailLayout(content);
        this.setCancelable(false);
        btn_left.setOnClickListener(new clickListener());
        setAttributes();
        super.setContentView(view);
    }

    private void addDetailLayout(String contents) {

        if (!TextUtils.isEmpty(contents)) {
            contents = contents.replace("{}", "@");
            String[] strs = contents.split("@");
            for (int i = 0; i < strs.length; i++) {
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                layoutParams.setMargins(0, 0, 0, 10);
                textView.setPadding(5, 0, 5, 0);
                textView.setText(strs[i]);
                textView.setTextColor(context.getResources().getColor(R.color.color1e1e1e));
                textView.setTextSize(14);
                textView.setGravity(Gravity.LEFT);
                textView.setLayoutParams(layoutParams);
                rl_auto_detail.addView(textView, layoutParams);
            }
        }
    }

    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
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
                    clickListenerInterface.doCancel();
                    break;

            }
        }

    }


    public interface ClickListenerInterface {

        void doCancel();
    }


}
