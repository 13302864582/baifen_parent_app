
package com.daxiong.fun.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.android.volley.RequestQueue;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dialog.WaitingDialog;
import com.daxiong.fun.http.volley.VolleyRequestQueueWrapper;

/**
 * 类描述：基本FragmentActivity 创建人：Leo 创建时间：2014-4-1 上午10:38:39 修改时间：2014-4-1
 * 上午10:38:39
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements IBaseFragment {

    public MyApplication app;

    public RequestQueue requestQueue;

    protected Dialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        app = (MyApplication)this.getApplication();
        requestQueue = VolleyRequestQueueWrapper.getInstance(this).getRequestQueue();
//        addActivity(this);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resultBack(Object... param) {
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.ERROR:
                break;
            case RequestConstant.COOKIE_INVILD:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消所有的请求
        requestQueue.cancelAll(this);
        if (requestQueue != null) {
            requestQueue = null;
        }
//        removeActivity(this);
    }

    /**
     * 此方法描述的是：显示进度条Dialog
     * 
     * @author: Sky
     * @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午8:06:20
     * @version: 2.0 showDialog void
     */
    protected void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog2(this, "正在加载中...");
            mProgressDialog.show();
        }
    }

    /**
     * 此方法描述的是：关闭进度条Dialog
     * 
     * @author: Sky
     * @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午7:28:45 closeDialog void
     */
    protected void closeDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mProgressDialog != null) {
                mProgressDialog = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void addActivity(Activity activity) {
//        synchronized (app) {
//            app.activityList.add(activity);
//        }
//    }
//
//    private void removeActivity(Activity activity) {
//        synchronized (app) {
//            app.activityList.remove(activity);
//        }
//    }

}
