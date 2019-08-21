package com.daxiong.fun.function.account.vip;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.account.model.VipInfoDetailModel;
import com.daxiong.fun.function.account.model.VipModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Vip套餐详情
 */

public class VipPlanDetailActivity extends BaseActivity {

    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.btn_xiadingdan)
    Button btnXiadingdan;

    private VipPlanDetailAdapter mAdapter;
    private VipModel.BuyVipInfosBean extra_buy_vip_infos;
    private String detail_content;
    private List<VipInfoDetailModel.DetailBean> list;
    int orgid=0;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.vip_plan_detail_activity);
        ButterKnife.bind(this);
        initView();
        initListener();
    }


    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        // detail_content = intent.getStringExtra("detail_content");
        orgid = intent.getIntExtra("parents_pool_orgid",0);
        extra_buy_vip_infos = (VipModel.BuyVipInfosBean) intent.getSerializableExtra("buy_vip_infos");
        detail_content = extra_buy_vip_infos.getDetail_content();
        list = new ArrayList<VipInfoDetailModel.DetailBean>();
        //解析json
        if (TextUtils.isEmpty(detail_content)) finish();

        String title = JsonUtil.getString(detail_content, "title", "");
        String detail = JsonUtil.getString(detail_content, "detail", "");

        List<VipInfoDetailModel.DetailBean> vipInfoDetailModels = JSON.parseArray(detail, VipInfoDetailModel.DetailBean.class);
        list.addAll(vipInfoDetailModels);
        View headview = View.inflate(this, R.layout.vip_plan_headview_layout, null);
        TextView tvTitle = (TextView) headview.findViewById(R.id.tv_title);


        try {

            int start1 = title.indexOf("{");
            int end1 = title.indexOf("}");

            int start2 = title.lastIndexOf("{");
            int end2 = title.lastIndexOf("}");
            title = title.replace("{", " ").replace("}", " ");
            ColorStateList redColors = ColorStateList.valueOf(getResources().getColor(R.color.colorfff715));
            SpannableStringBuilder spanBuilder = new SpannableStringBuilder(title);
            //style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
            //size  为0 即采用原始的正常的 size大小
            spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 45, redColors, null), start1, end1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 45, redColors, null), start2, end2 + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvTitle.setText(spanBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listview.addHeaderView(headview);
        mAdapter = new VipPlanDetailAdapter();
        listview.setAdapter(mAdapter);

    }


    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        btnXiadingdan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.btn_xiadingdan://下单操作

                if (extra_buy_vip_infos != null) {
                    int buy_category = extra_buy_vip_infos.getBuy_category();
                    int buy_type = extra_buy_vip_infos.getBuy_type();
                    //下单操作
                    try {
                        JSONObject json = new JSONObject();
                        json.put("buy_category", buy_category);
                        json.put("buy_type", buy_type);
                        if (orgid > 0){
                            json.put("orgid",orgid);
                            json.put("packageid",extra_buy_vip_infos.getPackageid());
                        }
                        showDialog("请稍等...");
                        OkHttpHelper.post(VipPlanDetailActivity.this, "parents", "newgenerateprepaidorders", json, new OkHttpHelper.HttpListener() {
                            @Override
                            public void onSuccess(int code, String dataJson, String errMsg) {
                                closeDialog();
                                //服务器返回orderid
                                final String orderid = JsonUtil.getString(dataJson, "orderid", "");

                                Intent intent = new Intent(VipPlanDetailActivity.this, MyOrderListActivity.class);
                                /*intent.putExtra(PayActivity.EXTRA_TAG_PAY_MODEL,
                                        (Serializable) buy_vip_infos.get(tag));
                                intent.putExtra(PayActivity.EXTRA_TAG_UID,
                                        MySharePerfenceUtil.getInstance().getUserId());
                                intent.putExtra("golangorderid", orderid);*/
                                startActivity(intent);

                            }

                            @Override
                            public void onFail(int HttpCode, String errMsg) {
                                closeDialog();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUtils.show("数据错误");
                }


                break;
        }
    }

    class VipPlanDetailAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VipPlanDetailAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(VipPlanDetailActivity.this, R.layout.vip_plan_detail_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            VipInfoDetailModel.DetailBean item = (VipInfoDetailModel.DetailBean) getItem(position);
            int icon_type = item.getIcon_type();
            holder.ivIcon.setBackgroundResource(getDrawableResourceId("icon_vip_detail" + icon_type));
            holder.tvTitle.setText(item.getTitle());
            holder.tvDetail.setText(item.getContent());
            if (position==list.size()-1){
                holder.view_line.setVisibility(View.GONE);
            }else{
                holder.view_line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        /**
         * 根据服务器返回图片名称找到本地的drawable中图片
         *
         * @param name
         * @return
         */
        public int getDrawableResourceId(String name) {
            try {
                Field field = R.drawable.class.getField(name);
                return field.getInt(new R.drawable());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }


        public class ViewHolder {
            @Bind(R.id.iv_icon)
            ImageView ivIcon;
            @Bind(R.id.tv_title)
            TextView tvTitle;
            @Bind(R.id.tv_detail)
            TextView tvDetail;
            @Bind(R.id.view_line)
            View view_line;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
