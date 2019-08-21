package com.daxiong.fun.function.account.vip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.api.VIPAPI;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.dialog.CustomCommonDialog;
import com.daxiong.fun.function.account.adapter.YizhifuOrderListAdapter;
import com.daxiong.fun.function.account.model.MyOrderModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 已支付Fragment
 */
public class YizhifuFragment extends BaseFragment implements XListView.IXListViewListener {

    private static final String TAG = "YizhifuFragment";


    @Bind(R.id.order_listview)
    XListView orderListview;
    @Bind(R.id.tv_none_quan)
    TextView tvNoneQuan;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;

    private List<MyOrderModel> orderList = null;
    private YizhifuOrderListAdapter myOrderListAdapter;

    private int pageSize = 5;
    private int pageIndex = 0;
    private boolean isShow = false;
    private VIPAPI vipApi = null;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgStr = (String) msg.obj;
        }
    };

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_yizhifu, null);
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
        myOrderListAdapter = new YizhifuOrderListAdapter(getActivity(), orderList);
        orderListview.setAdapter(myOrderListAdapter);

    }


    @Override
    public void initListener() {
        super.initListener();
        orderListview.setXListViewListener(this);
        orderListview.setPullRefreshEnable(true);
        orderListview.setPullLoadEnable(true);
        myOrderListAdapter.setIButtonClickListener(new YizhifuOrderListAdapter.IButtonClickListener() {
            @Override
            public void doCancle(int position) {
                //取消订单操作
                execCancleOrder(position);

            }

            @Override
            public void doPayMoney(int position) {


            }
        });

    }


    public void initDate() {
        showDialog("加载中...");
        try {
            JSONObject json = new JSONObject();
            json.put("order_type", 1);//订单类型:0-预支付1-已完成
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
