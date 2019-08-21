
package com.daxiong.fun.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.WxConstant;
import com.daxiong.fun.util.LogUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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
        // LogUtils.e("wxError-->", errCode+"");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // builder.setTitle(R.string.app_tip);
            // builder.setMessage(getString(R.string.pay_result_callback_msg,
            // resp.errStr +";code=" + String.valueOf(resp.errCode)));
            // builder.show();
            LogUtils.e("wxError", getString(R.string.pay_result_callback_msg,
                    resp.errStr + ";code=" + String.valueOf(resp.errCode)));
        }
        if (errCode == 0) {
            Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(WXPayEntryActivity.this, MainActivity.class);
            Bundle data2 = new Bundle();
            data2.putString("layout", "layout_home");
            intent2.putExtras(data2);
            startActivity(intent2);
            finish();
        }

        finish();

    }
}
