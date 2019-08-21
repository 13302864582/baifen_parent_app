
package com.daxiong.fun.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.WxConstant;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.SharePerfenceUtil;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private int errCode;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weixinpayok);

        api = WXAPIFactory.createWXAPI(this, WxConstant.APP_ID_WW);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
    	 errCode = resp.errCode; 
    	
         if (errCode == 0) {
        	 if (SharePerfenceUtil.getInt("sharekey", 0) == 1) {
					
        		 AppUtils.clickevent("share_wechat",WXEntryActivity.this);
				} else if (SharePerfenceUtil.getInt("sharekey", 0) == 4) {
					AppUtils.clickevent("share_timeline",WXEntryActivity.this);
				}
            
         }
         finish();
    }
}
