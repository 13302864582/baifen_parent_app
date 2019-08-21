package com.daxiong.fun.function.account.vip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.api.VIPAPI;
import com.daxiong.fun.api.WXPayApi;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.constant.WxConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.CustomCommonDialog;
import com.daxiong.fun.dialog.CustomPayDialog;
import com.daxiong.fun.function.account.adapter.DaizhifuOrderListAdapter;
import com.daxiong.fun.function.account.model.MyOrderModel;
import com.daxiong.fun.function.account.model.WoInfoModel;
import com.daxiong.fun.function.goldnotless.OrderHelper;
import com.daxiong.fun.function.goldnotless.OrderModel;
import com.daxiong.fun.function.goldnotless.Result;
import com.daxiong.fun.function.goldnotless.PayResult;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ThreadPoolUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 待支付
 *
 * @author sky
 */
public class DaizhifuFragment extends BaseFragment implements IXListViewListener {
    private static final String TAG = "DaizhifuFragment";


    @Bind(R.id.order_listview)
    XListView orderListview;
    @Bind(R.id.tv_none_quan)
    TextView tvNoneQuan;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;

    private List<MyOrderModel> orderList = null;
    private DaizhifuOrderListAdapter myOrderListAdapter;
    public static final int CONFIRM_PAY = 3232;
    private int pageSize = 5;
    private int pageIndex = 0;
    private boolean isShow = false;
    private PayAsyncTask mTask;
    private String body;
    private VIPAPI vipApi = null;

    private int del_position = -1;
    private int fromUserId, toUserId, vip_server_type;
    private String golangorderid;
    private float money = 0f;
    private IWXAPI msgApi;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //String msgStr = (String) msg.obj;
            String msgStr = "";
            switch (msg.what) {
                case GlobalContant.CLOSEDIALOG:
                    if (isShow) {
                        closeDialog();
                        isShow = false;
                        ToastUtils.show("连接超时");
                    }
                    break;
                case GlobalContant.ERROR_MSG:
                    ToastUtils.show(msgStr);
                    break;
                case OrderHelper.RQF_PAY:
                case OrderHelper.RQF_LOGIN:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    mTask = new PayAsyncTask(resultInfo, "content");
                    mTask.excute();
                    break;
                case CONFIRM_PAY:// 支付确认
                    String resultStr = (String) msg.obj;
                    int code = JsonUtil.getInt(resultStr, "code", 0);
                    double trade_coin = JsonUtil.getDouble(resultStr, "trade_coin", 0);
                    double all_coin = JsonUtil.getDouble(resultStr, "all_coin", 0);
                    String errmsg = JsonUtil.getString(resultStr, "errmsg", "");

                    closeDialog();
                    mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
                    isShow = false;
                    // ToastUtils.show("code-->"+code);
                    if (code == 0) {
                        final UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                        if (null != uInfo) {
                            uInfo.setGold((float) all_coin);
                            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                            //再次获取用户信息，更新supervip字段
                            OkHttpHelper.post(getActivity(), "parents", "myselfpageinfos", null, new OkHttpHelper.HttpListener() {

                                @Override
                                public void onSuccess(int code, String dataJson, String errMsg) {
                                    WoInfoModel woInfoModel = JSON.parseObject(dataJson, WoInfoModel.class);
                                    int supervip = woInfoModel.getMy_infos().getSupervip();
                                    //数据库中的supervip
                                    DBHelper.getInstance().getWeLearnDB().updateSupervip(uInfo.getUserid(), supervip);
                                }

                                @Override
                                public void onFail(int HttpCode, String errMsg) {

                                }
                            });
                            Intent intent2 = new Intent(getActivity(), MainActivity.class);
                            Bundle data2 = new Bundle();
                            data2.putString("layout", "layout_home");
                            intent2.putExtras(data2);
                            startActivity(intent2);
                            getActivity().finish();
                        }
                    } else {
                        if (!TextUtils.isEmpty(errmsg)) {
                            ToastUtils.show(errmsg, 1);
                        }
                    }
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_daizhifu, null);
        ButterKnife.bind(this, view);
        initView(view);
        initListener();
        initDate();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        vipApi = new VIPAPI();
        orderList = new ArrayList<MyOrderModel>();
        myOrderListAdapter = new DaizhifuOrderListAdapter(getActivity(), orderList);
        orderListview.setAdapter(myOrderListAdapter);

    }


    @Override
    public void initListener() {
        super.initListener();
        orderListview.setXListViewListener(this);
        orderListview.setPullRefreshEnable(true);
        orderListview.setPullLoadEnable(true);
        myOrderListAdapter.setIButtonClickListener(new DaizhifuOrderListAdapter.IButtonClickListener() {
            @Override
            public void doCancle(int position) {
                //取消订单操作
                execCancleOrder(position);

            }

            @Override
            public void doPayMoney(int position) {
                //立即支付操作
                execPayMoney(position);

            }
        });

    }


    public void initDate() {
        showDialog("加载中...");
        try {
            JSONObject json = new JSONObject();
            json.put("order_type", 0);//订单类型:0-预支付1-已完成
            json.put("page", pageIndex);
            json.put("count", pageSize);
            OkHttpHelper.post(getActivity(), "parents", "neworderlist", json, new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    onLoadFinish();
                    closeDialog();
                    if (!TextUtils.isEmpty(dataJson)) {
                        List<MyOrderModel> myOrderList = JSON.parseArray(dataJson, MyOrderModel.class);
                        if (myOrderList.size() < 5) {
                            orderListview.setPullLoadEnable(false);
                        } else {
                            orderListview.setPullLoadEnable(true);
                        }
                        orderList.addAll(myOrderList);
                        if (orderList != null && orderList.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                            myOrderListAdapter.notifyDataSetChanged();
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            orderListview.setEmptyView(emptyView);
                        }
                    }

                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    onLoadFinish();
                    closeDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        }
    }

    private void execCancleOrder(final int position) {
        final String orderid = orderList.get(position).getOrderid();
        final CustomCommonDialog dialog = new CustomCommonDialog(getActivity(), "", "确认要删除该订单?", "取消", "确定");
        dialog.show();
        dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject();
                    json.put("orderid", orderid);
                    showDialog("请稍后...");
                    OkHttpHelper.post(getActivity(), "parents", "cancleorder", json, new OkHttpHelper.HttpListener() {
                        @Override
                        public void onSuccess(int code, String dataJson, String errMsg) {
                            closeDialog();
                            ToastUtils.show("删除订单成功");
                            orderList.remove(position);
                            myOrderListAdapter.notifyDataSetChanged();
                            //if (!TextUtils.isEmpty(dataJson)) {}

                            if (orderList != null && orderList.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                myOrderListAdapter.notifyDataSetChanged();
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                orderListview.setEmptyView(emptyView);
                            }
                        }

                        @Override
                        public void onFail(int HttpCode, String errMsg) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void doCancel() {
                dialog.dismiss();
            }
        });


    }


    /**
     * 支付操作
     *
     * @param position
     */
    public void execPayMoney(final int position) {
        final CustomPayDialog payDialog = new CustomPayDialog(getActivity(), "取消");
        payDialog.show();
        payDialog.setClicklistener(new CustomPayDialog.ClickListenerInterface() {
            @Override
            public void doZhifubaoPay() {
                payDialog.dismiss();
                getPayParameter("zhifubao", orderList.get(position));
            }

            @Override
            public void doWeixinPay() {
                payDialog.dismiss();
                getPayParameter("wx", orderList.get(position));

            }

            @Override
            public void doCancel() {
                payDialog.dismiss();
            }
        });

    }


    @Override
    public void onRefresh() {
        orderList.clear();
        pageIndex = 0;
        initDate();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        initDate();
    }

    public void onLoadFinish() {
        orderListview.stopRefresh();
        orderListview.stopLoadMore();
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        orderListview.setRefreshTime(time);
    }


    ///////////////////////////////////// 支付部分////////////////////////

    //得到支付的参数
    public void getPayParameter(String tag, MyOrderModel myorderModel) {
        //uMengEvent("openrecharge_" + (int) money);
        toUserId = MySharePerfenceUtil.getInstance().getUserId();
        golangorderid = myorderModel.getOrderid();

        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo) {
            ToastUtils.show(R.string.params_error);
            getActivity().finish();
        }

        fromUserId = uInfo.getUserid();

        if (null == myorderModel || toUserId == 0) {
            ToastUtils.show(R.string.params_error);
            getActivity().finish();
        }
        //money = myorderModel.getPrice();

        //决定了最后支付多少钱
        // money = myorderModel.getOriginal_price();
        money = myorderModel.getChanged_price();
        //money = 0.01f;

        //vip_server_type = myorderModel.getVip_server_type();
        vip_server_type = myorderModel.getServer_type();

        body = myorderModel.getDescription();
        if ("wx".equals(tag)) {
            wxpay();
        } else {
            alipay();
        }
    }

    /**
     * 微信支付
     */
    private void wxpay() {
        //uMengEvent("wxpay_" + (int) money);
        msgApi = WXAPIFactory.createWXAPI(getActivity(), WxConstant.APP_ID_WW, true);
        boolean registerApp = msgApi.registerApp(WxConstant.APP_ID_WW);
        if (!msgApi.isWXAppInstalled()) {
            Toast.makeText(getActivity(), "微信未安装", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!registerApp) {
            Toast.makeText(getActivity(), "微信注册失败", Toast.LENGTH_SHORT).show();
            return;
        }

        showDialog(getString(R.string.getting_prepayid));

        try {
            new WXPayApi().WXpay(requestQueue, toUserId, fromUserId, money, body, vip_server_type, golangorderid,
                    this, RequestConstant.GET_WXPAY);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void executePay(JSONObject dataJson) {

        PayReq req = new PayReq();

        String transactionNo = dataJson.optString("transactionNo");
        String partnerid = dataJson.optString("partnerid");
        String appid = dataJson.optString("appid");
        String prepayid = dataJson.optString("prepayid");
        String timestamp = dataJson.optString("timestamp");
        String noncestr = dataJson.optString("noncestr");
        String packages = dataJson.optString("package");
        String sign = dataJson.optString("sign");

        req.appId = appid;
        req.partnerId = partnerid;
        req.prepayId = prepayid;
        req.packageValue = packages;
        req.nonceStr = noncestr;
        req.timeStamp = timestamp;
        req.sign = sign;

        closeDialog();
        msgApi.registerApp(WxConstant.APP_ID_WW);
        msgApi.sendReq(req);

    }

    /////////////// 支付宝支付/////////////////////


    /**
     * 阿里支付
     */
    private void alipay() {
        //uMengEvent("alipay_" + (int) money);
        MobclickAgent.onEvent(getActivity(), "recharge_" + money);
        isShow = true;
        showDialog(getString(R.string.please_wait));
        mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
        final OrderModel order = new OrderModel();

        order.price = money;
        // order.price=0.01f;//暂时设置为0.01
        order.userid = toUserId;
        order.orderid = OrderHelper.getOutTradeNo();
        order.body = "可购得学点" + money + "个";
        if (fromUserId == toUserId) {
            order.subject = getString(R.string.pay_user_x_pay, order.userid);
        } else {
            order.subject = getString(R.string.pay_for_others_user_x_pay, fromUserId, order.userid);
        }
        order.subject = body;
        final String info = OrderHelper.getNewOrderInfo(order);

        ThreadPoolUtil.execute(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                // /Uri uri = Uri.parse(Config.URL);
                HttpPost post = new HttpPost(AppConfig.ALIURL + "orderencrypt");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("content", info));
                params.add(new BasicNameValuePair("userid", String.valueOf(order.userid)));
                params.add(new BasicNameValuePair("fromuserid", String.valueOf(fromUserId)));
                params.add(new BasicNameValuePair("servertype", String.valueOf(vip_server_type)));

                params.add(new BasicNameValuePair("golangorderid", golangorderid));
                HttpResponse httpResponse = null;
                String result = "";
                try {
                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(post);
                    result = EntityUtils.toString(httpResponse.getEntity());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // if (httpResponse.getStatusLine().getStatusCode() ==
                // 200)
                // {
                int code = JsonUtil.getInt(result, "code", 0);
                String sign = JsonUtil.getString(result, "content", "");
                String errmsg = JsonUtil.getString(result, "errmsg", "");

                System.out.println("转换qian--->" + sign);
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign);
                System.out.println("转换hou--->" + sign);
                // 完整的符合支付宝参数规范的订单信息
                //final String orderinfo = info + "&sign=\"" + sign + "\"&" + getSignType();
                final String orderinfo = info + "&sign=" + sign ;
                closeDialog();
                mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
                isShow = false;
                if (code == 0) {
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> payResult = alipay.payV2(orderinfo, true);
                    Message msg = new Message();
                    msg.what = OrderHelper.RQF_PAY;
                    msg.obj = payResult;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 987;
                    msg.obj = errmsg;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }


    private String getSignType() {
        return "sign_type=\"RSA2\"";
    }


    private class PayAsyncTask extends MyAsyncTask {
        private String mResult;

        private int code;

        @SuppressWarnings("unused")
        private double trade_coin;

        private String tag;

        public PayAsyncTask(String result, String tag) {
            super();
            this.mResult = result;
            this.tag = tag;
        }

        @Override
        public void preTask() {
            isShow = true;
            showDialog(getString(R.string.please_wait));
            mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
        }

        @Override
        public void doInBack() {
            String url = "";
            if ("content".equals(tag)) {
                url = AppConfig.ALIURL;
            } else if ("orderid".equals(tag)) {
                url = AppConfig.YEEURL;
            }
            HttpPost post = new HttpPost(url + "orderconfirm");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(tag, mResult));
            params.add(new BasicNameValuePair("user_id", String.valueOf(toUserId)));
            HttpResponse httpResponse = null;
            String result = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                httpResponse = new DefaultHttpClient().execute(post);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.e("result:", result);
            // if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // code = JsonUtil.getInt(result, "code", 0);
            // trade_coin = JsonUtil.getDouble(result, "trade_coin", 0);
            // all_coin = JsonUtil.getDouble(result, "all_coin", 0);
            // errmsg = JsonUtil.getString(result, "errmsg", "");
            // }

            Message msg = Message.obtain();
            msg.what = CONFIRM_PAY;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }

        private String errmsg;

        private double all_coin;

        @Override
        public void postTask() {

            // closeDialog();
            // mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
            // isShow = false;
            // // ToastUtils.show("code-->"+code);
            // if (code == 0) {
            // UserInfoModel uInfo =
            // DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
            // // if (null != uInfo) {
            // Toast.makeText(PayActivity.this, "支付成功",
            // Toast.LENGTH_SHORT).show();
            // uInfo.setGold((float) all_coin);
            // DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
            // Intent intent2 = new Intent(PayActivity.this,
            // MainActivity.class);
            // Bundle data2 = new Bundle();
            // data2.putString("layout", "layout_home");
            // intent2.putExtras(data2);
            // startActivity(intent2);
            // finish();
            // // }
            // }
            // if (!TextUtils.isEmpty(errmsg)) {
            // ToastUtils.show(errmsg, 1);
            // }

        }
    }


    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        onLoadFinish();
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_WXPAY:// 微信支付请求
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataJson)) {
                                JSONObject rootJson = new JSONObject(dataJson);
                                executePay(rootJson);
                            } else {
                                closeDialog();
                                ToastUtils.show(msg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        closeDialog();
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDialog();
        mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
    }

}
