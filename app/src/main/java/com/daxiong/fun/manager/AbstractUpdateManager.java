
package com.daxiong.fun.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.dialog.CustomTipDialog;
import com.daxiong.fun.dispatch.ImMsgDispatch;
import com.daxiong.fun.dispatch.WelearnHandler;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractUpdateManager {
    protected Context mContext;

    CustomTipDialog tipDialog;
    // 返回的安装包url
    // private String apkUrl;

    // private int mCurrentVersionConde;

    /* 下载包安装路径 */
    private static final String savePath = Environment.getExternalStorageDirectory().toString()
            + "/";

    private static final String saveFileName = savePath + "welearn.apk";

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private static final int SHOW_NOTICE = 5;

    private static final int GO_SHOW_NOTICE = 22;

    private static final int SDCARD_NOT_NOUNTED = 3;

    protected int progress;

    private Thread downLoadThread;

    protected boolean interceptFlag = false;

    // private boolean isManual;

    // protected String upMsg;

    private static final String TAG = AbstractUpdateManager.class.getSimpleName();

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    setResult();
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                case SHOW_NOTICE:
                    cloesNoticeDialog();
                    showNoticeDialog(true);
                    break;
                case GO_SHOW_NOTICE:
                    String compel = (String)msg.obj;
                    cloesNoticeDialog();
                    if ("yes".equals(compel)) {
                        showNoticeDialog(true);
                    } else {
                        showNoticeDialog(false);
                    }

                    break;
                default:
                    break;
            }
        };
    };

    protected abstract void showResult();

    protected abstract void setResult();
    
    
    public void cloesNoticeDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }

    public AbstractUpdateManager(Context context) {
        this.mContext = context;
        if (!WelearnHandler.getInstance().getHandler().isRegisterObserver(mDispatch, TAG)) {
            WelearnHandler.getInstance().getHandler().registerObserver(mDispatch, TAG);
        }
    }

    /**
     * 程序自动检测更新
     * 
     * @param obj
     */
    public void checkUpdateInfo() {
        WeLearnApi.checkUpdate();
    }

    private ImMsgDispatch mDispatch = new ImMsgDispatch() {

        @Override
        public Bundle handleImMsg(Message msg) {
            switch (msg.what) {
                case MessageConstant.MSG_DEF_OBTAIN_UPDATE_SUCCESS:
                    JSONObject obj = (JSONObject)msg.obj;
                    if (obj == null) {
                        cloesNoticeDialog();
                        return null;
                    }

                    try {
                        int mCurrentVersionConde = mContext.getPackageManager()
                                .getPackageInfo(mContext.getPackageName(), 0).versionCode;

                        int versionCode = Integer
                                .parseInt(JsonUtil.getString(obj, "versioncode", ""));
                        MySharePerfenceUtil.getInstance().setLatestVersion(versionCode);

                        String title = JsonUtil.getString(obj, "title", "");
                        MySharePerfenceUtil.getInstance().setUpdateTitle(title);

                        String content = JsonUtil.getString(obj, "content", "");
                        MySharePerfenceUtil.getInstance().setUpdateContent(content);

                        String apkUrl = JsonUtil.getString(obj, "url", "");
                        MySharePerfenceUtil.getInstance().setUpdateUrl(apkUrl);
                        // 是否强制升级
                        String compel = JsonUtil.getString(obj, "compel", "");
                        if (mCurrentVersionConde < versionCode) {
                            Message message = Message.obtain();
                            message.what = GO_SHOW_NOTICE;
                            message.obj = compel;
                            mHandler.sendMessageDelayed(message, 2000);
                            // mHandler.sendEmptyMessageDelayed(SHOW_NOTICE,
                            // 2000);

                        }
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case SDCARD_NOT_NOUNTED:
                    cloesNoticeDialog();
                    ToastUtils.show("无法下载安装文件，请确认SD卡是否安装");
                    break;
            }
            return null;
        }
    };

    

    /**
     * 手动检测更新
     */
    // public void maunalCheckUpdateInfo() {
    // isManual = true;
    // WeLearnApi.checkUpdate();
    // }

    public void showNoticeDialog(String compel) {
        if (mContext != null && !((Activity)mContext).isFinishing()) {
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("弹出升级框");
            }

            if (!"yes".equals(compel)) {
                tipDialog = new CustomTipDialog((Activity)mContext,
                        MySharePerfenceUtil.getInstance().getUpdateTitle(),
                        MySharePerfenceUtil.getInstance().getUpdateContent(), "升级", "以后再说");
                final Button positiveBtn = tipDialog.getPositiveButton();
                final Button negativeBtn = tipDialog.getNegativeButton();
                tipDialog.setOnNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
                        tipDialog.dismiss();
                        showDownloadDialog();
                    }
                });

                tipDialog.setOnPositiveListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tipDialog.dismiss();
                    }
                });
                tipDialog.show();
            } else {//
                tipDialog = new CustomTipDialog((Activity)mContext,
                        MySharePerfenceUtil.getInstance().getUpdateTitle(),
                        MySharePerfenceUtil.getInstance().getUpdateContent(), "强制升级", "");
                final Button positiveBtn = tipDialog.getPositiveButton();
                final Button negativeBtn = tipDialog.getNegativeButton();
                tipDialog.setOnNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
                        tipDialog.dismiss();
                        showDownloadDialog();
                    }
                });

                tipDialog.setOnPositiveListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tipDialog.dismiss();
                    }
                });
                tipDialog.show();
            }

        }
    }

    public void showNoticeDialog(boolean isYes) {
        if (mContext != null && !((Activity)mContext).isFinishing()) {

            if (!isYes) {
                tipDialog = new CustomTipDialog((Activity)mContext,
                        MySharePerfenceUtil.getInstance().getUpdateTitle(),
                        MySharePerfenceUtil.getInstance().getUpdateContent(), "升级", "以后再说", true);
                final Button positiveBtn = tipDialog.getPositiveButton();
                final Button negativeBtn = tipDialog.getNegativeButton();

                tipDialog.setOnNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
                        tipDialog.dismiss();
                        showDownloadDialog();
                    }
                });

                tipDialog.setOnPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveBtn.setTextColor(Color.parseColor("#28b9b6"));
                        tipDialog.dismiss();

                    }
                });
            } else {
                tipDialog = new CustomTipDialog((Activity)mContext,
                        MySharePerfenceUtil.getInstance().getUpdateTitle(),
                        MySharePerfenceUtil.getInstance().getUpdateContent(), "升级", "以后再说", false);
                final Button positiveBtn = tipDialog.getPositiveButton();
                final Button negativeBtn = tipDialog.getNegativeButton();

                tipDialog.setOnNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
                        tipDialog.dismiss();
                        showDownloadDialog();
                    }
                });

                tipDialog.setOnPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        positiveBtn.setTextColor(Color.parseColor("#28b9b6"));
//                        tipDialog.dismiss();

                    }
                });
            }

            tipDialog.show();
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("弹出升级框");
            }

        }
    }

    private void showDownloadDialog() {
        showResult();
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        public void run() {
            if (!MyApplication.mNetworkUtil.isInternetConnected(mContext)) {
                ToastUtils.show("网络呀，你不要抖动的这么厉害呀！好吧，过会儿再试试");
            }
            try {
                URL url = new URL(MySharePerfenceUtil.getInstance().getUpdateUrl());

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                String state = Environment.getExternalStorageState();
                String apkFile = "";
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    apkFile = saveFileName;
                }
                if (TextUtils.isEmpty(apkFile)) {
                    mHandler.sendEmptyMessage(SDCARD_NOT_NOUNTED);
                }
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int)(((float)count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     * 
     * @param url
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     * 
     * @param url
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
