
package com.daxiong.fun.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dialog.WaitingDialog;
import com.daxiong.fun.http.volley.VolleyRequestQueueWrapper;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

/**
 * 此类的描述： Fragment基类
 * 
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午2:12:15
 * @version: 2.0
 */

public  abstract class BaseFragment extends Fragment implements IBaseFragment,OnClickListener {
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		
	}

	

	public MyApplication app;

    public RequestQueue requestQueue;

    protected Dialog mProgressDialog;
    
    
    public <T extends View> T $(View view,int resId) {
        return (T)view.findViewById(resId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = VolleyRequestQueueWrapper.getInstance(getActivity()).getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initListener() {

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
                String datas = param[1].toString();
                int code = JsonUtil.getInt(datas, "Code", -1);
                String msg = JsonUtil.getString(datas, "Msg", "");
                // 用户未登录
                if (code == RequestConstant.USER_UNLOGIN &&
                        msg.contains("用户未登")) {
                    //OkHttpHelper.doReLogin(getActivity(), (BaseActivity) getActivity());
                    ToastUtils.show("登录过期请重新登录");
                    IntentManager.goToPhoneLoginActivity(getActivity(),null,true);
                }
                break;
        }

    }


    public void showDialog(String text) {
        if (mProgressDialog == null&&getActivity()!=null) {
            mProgressDialog = WaitingDialog.createLoadingProgress(getActivity(), text);
            if (getActivity()!=null&&!getActivity().isFinishing()) {
            	 mProgressDialog.show();
			}
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
    public Context getContext() {
        return null;
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (mProgressDialog!=null) {
    		mProgressDialog.dismiss();
    		mProgressDialog=null;
		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消所有的请求
        requestQueue.cancelAll(this);
        if (requestQueue != null) {
            requestQueue = null;
        }
    }

}
