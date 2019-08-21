
package com.daxiong.fun.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.util.MySharePerfenceUtil;

public class UpdateManagerForDialog extends AbstractUpdateManager {

    private Dialog downloadDialog;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    public UpdateManagerForDialog(Context context) {
        super(context);
    }

    @Override
    protected void showResult() {
        // AlertDialog.Builder builder = new Builder(mContext);
        View mView = View.inflate(mContext, R.layout.custom_downapk_dialog, null);
        TextView tv_title = (TextView)mView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView)mView.findViewById(R.id.tv_content);
        mProgress = (ProgressBar)mView.findViewById(R.id.progress);        
        Button negativeButton = (Button)mView.findViewById(R.id.btn_cancle);
        tv_title.setText("正在下载，请稍候");        
        
        final Dialog builder = new Dialog(mContext, R.style.MyDialogStyleBottom);
        builder.setTitle(MySharePerfenceUtil.getInstance().getUpdateTitle());
        builder.setContentView(mView);       
        builder.setCancelable(false);
        negativeButton.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                builder.dismiss();
                interceptFlag = true;
                ((Activity)mContext).finish();
                MobclickAgent.onKillProcess(mContext);
                Process.killProcess(Process.myPid());            
                
            }
        });   
        
        builder.show();
    }

    @Override
    protected void setResult() {
        mProgress.setProgress(progress);
    }

}
