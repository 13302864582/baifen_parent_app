
package com.daxiong.fun.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;

import static com.daxiong.fun.R.id.tv_title;

/**
 * 支付dialog
 *
 * @author: sky
 */
public class CustomPayDialog extends Dialog {

    private Activity context;
    public TextView btn_left = null;
    private LinearLayout rl_zhifubao;
    private LinearLayout rl_weixin;
    private ImageView iv_zhifubao,iv_weixin;
    private TextView tv_zhifubao,tv_weixin;


    private ClickListenerInterface clickListenerInterface;


    public CustomPayDialog(Activity context, String buttonLeftStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context, buttonLeftStr);

    }

    private void setCustomDialog(Context ctx, String buttonLeftStr) {
        View view = View.inflate(ctx, R.layout.custom_pay_dialog, null);
        rl_zhifubao = (LinearLayout) view.findViewById(R.id.rl_zhifubao);
        rl_weixin = (LinearLayout) view.findViewById(R.id.rl_weixin);
        iv_zhifubao= (ImageView) view.findViewById(R.id.iv_zhifubao);
        iv_weixin= (ImageView) view.findViewById(R.id.iv_weixin);
        btn_left = (TextView) view.findViewById(R.id.btn_left);
        btn_left.setText(buttonLeftStr);

        tv_zhifubao= (TextView) view.findViewById(R.id.tv_zhifubao);
        tv_weixin= (TextView) view.findViewById(R.id.tv_weixin);

        this.setCancelable(false);
//        rl_zhifubao.setClickable(true);
//        rl_weixin.setClickable(true);
        btn_left.setOnClickListener(new clickListener());
        rl_zhifubao.setOnClickListener(new clickListener());
        rl_weixin.setOnClickListener(new clickListener());

        tv_zhifubao.setOnClickListener(new clickListener());
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
                case R.id.rl_zhifubao:
                    clickListenerInterface.doZhifubaoPay();
                    break;
                case R.id.rl_weixin:
                    onClick(tv_weixin);
                    clickListenerInterface.doWeixinPay();
                    break;
                case R.id.btn_left:
                    clickListenerInterface.doCancel();
                    break;

            }
        }

    }


    public interface ClickListenerInterface {
        void doZhifubaoPay();

        void doWeixinPay();

        void doCancel();
    }


}
