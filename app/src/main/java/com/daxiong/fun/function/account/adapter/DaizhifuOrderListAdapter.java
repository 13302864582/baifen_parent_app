package com.daxiong.fun.function.account.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.account.model.MyOrderModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 待支付订单列表adapter
 *
 * @author Administrator
 */
public class DaizhifuOrderListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyOrderModel> orderList;

    public DaizhifuOrderListAdapter(Context mContext, List<MyOrderModel> orderList) {
        super();
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.daizhifu_order_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyOrderModel item = orderList.get(position);

        //订单号
        String orderid = item.getOrderid();
        holder.tvOrderNo.setText("订单号:"+orderid);
        //日期时间
        String dataTime = item.getDatatime();
        holder.tvOrderDateTime.setText(dataTime);

        //订单标题
        String des = item.getDescription();
        holder.tvOrderTitle.setText(des);

        //订单修改后的价格
        float changed_price = item.getChanged_price();
        /*String moneyStr = "<p>费用：<h2 size='4' color=#57be6a>" + changed_price
                + "元</h2></p>";

        holder.tvMoney.setText(Html.fromHtml(moneyStr));*/
        holder.tvMoney.setText(changed_price+"元");
        //购买类别: 1-辅导券 2-VIP
        int buy_category = item.getBuy_category();
        //辅导券类型:1-难题答疑2-作业检查
        int coupon_type = item.getCoupon_type();
        if (buy_category == 1) {//辅导团
            switch (coupon_type) {
                case 1:
                    holder.llIcon.setBackgroundResource(R.drawable.icon_vip_pay_question);
                    holder.tvYuanjia.setVisibility(View.VISIBLE);
                    holder.tvYuanjia.setText(item.getOriginal_price()+"元");
                    holder.tvFudaoquanCount.setVisibility(View.VISIBLE);
                    holder.tvFudaoquanCount.setText(item.getCoupon_count()+"张");
                    break;
                case 2:
                    holder.llIcon.setBackgroundResource(R.drawable.icon_vip_pay_homework);
                    holder.tvYuanjia.setVisibility(View.VISIBLE);
                    holder.tvYuanjia.setText(item.getOriginal_price()+"元");
                    holder.tvFudaoquanCount.setVisibility(View.VISIBLE);
                    holder.tvFudaoquanCount.setText(item.getCoupon_count()+"张");
                    break;
            }


        } else {//vip
            holder.llIcon.setBackgroundResource(R.drawable.icon_vip_pay_vip);
            holder.tvYuanjia.setVisibility(View.GONE);
            holder.tvFudaoquanCount.setVisibility(View.GONE);
        }
        //取消操作
        holder.ivDelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doCancle(position);
            }
        });

        //立即支付操作
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doPayMoney(position);
            }
        });


       /* if (item.getState() == 1) {// 支付成功
            holder.tv_pay_state.setText("支付成功");
            holder.layout_pay_type.setVisibility(View.GONE);

            holder.layout_order_btn.setVisibility(View.GONE);
            holder.layout_complete_pay.setVisibility(View.VISIBLE);
            holder.tv_order_date.setText(item.getDatatime());

            if (item.getPaychan() == 0) {
                holder.tv_pay_complete_type.setText("没有支付");
            } else if (item.getPaychan() == 1) {
                holder.tv_pay_complete_type.setText("微信支付");
            } else if (item.getPaychan() == 2) {
                holder.tv_pay_complete_type.setText("支付宝支付");
            }

        } else if (item.getState() == 0 || item.getState() == 2) {// 预支付或者支付失败
            if (item.getState() == 0) {
                holder.tv_pay_state.setText("预支付");
            } else if (item.getState() == 2) {
                holder.tv_pay_state.setText("支付失败");
            }
            holder.layout_pay_type.setVisibility(View.VISIBLE);

            holder.checkbox_wechat_pay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cx_wx.setChecked(true);
                        cx_zhifubao.setChecked(false);

                        // mContext.setEachPayChan(pos, 1);
                    }

                }
            });
            holder.checkbox_alipay_pay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cx_zhifubao.setChecked(true);
                        cx_wx.setChecked(false);

                        // mContext.setEachPayChan(pos, 2);
                    }

                }
            });

            holder.layout_order_btn.setVisibility(View.VISIBLE);
            holder.btn_sure_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cx_wx.isChecked()) {
                        //ToastUtils.show("--微信");
                        mContext.getPayParameter("wx", orderList.get(pos));
                    } else {
                        //ToastUtils.show("--支付宝");

                    }

                }
            });
            holder.btn_cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ToastUtils.show("取消");
                    mContext.cancleOrder(pos, orderList.get(pos).getOrderid());
                }
            });
            holder.layout_complete_pay.setVisibility(View.GONE);

        }*/

//        mContext.getPayParameter("zhifubao", orderList.get(pos));

        return convertView;
    }


    final class ViewHolder {
        @Bind(R.id.ll_icon)
        LinearLayout llIcon;
        @Bind(R.id.tv_yuanjia)
        TextView tvYuanjia;
        @Bind(R.id.tv_fudaoquan_count)
        TextView tvFudaoquanCount;


        @Bind(R.id.tv_order_no)
        TextView tvOrderNo;
        @Bind(R.id.tv_order_date_time)
        TextView tvOrderDateTime;
        @Bind(R.id.iv_del_order)
        ImageView ivDelOrder;

        @Bind(R.id.tv_order_title)
        TextView tvOrderTitle;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.btn_pay)
        Button btnPay;
        @Bind(R.id.layout_order_btn)
        RelativeLayout layoutOrderBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public IButtonClickListener listener;
    public void setIButtonClickListener(IButtonClickListener listener){
        this.listener=listener;
    }

    public interface  IButtonClickListener{
        //取消订单
        void doCancle(int position);
        //支付
        void doPayMoney(int position);
    }
}
