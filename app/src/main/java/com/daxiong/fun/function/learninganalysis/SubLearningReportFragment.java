
package com.daxiong.fun.function.learninganalysis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.function.learninganalysis.adapter.LearningReportAdapter;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel.ReportDetailBen;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.util.AppUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学情报告Fragment
 *
 * @author: sky
 */
public class SubLearningReportFragment extends BaseFragment implements OnItemClickListener {

    private static final String TAG = "SubLearningReportFragme";

    private MainActivity activity;

    private View view;
    private GridView home_work_gridview;
    private TextView tv_meiyou;
    private LearningReportAdapter adapter;


    private List<ReportDetailBen> list = new ArrayList<>();
    private Handler mHandler;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");





    public void setList(List<ReportDetailBen> sublist) {
        this.list = sublist;

        if (list!=null&&list.size()>0) {
            tv_meiyou.setVisibility(View.GONE);
        } else {
            tv_meiyou.setVisibility(View.VISIBLE);
        }
        //adapter.notifyDataSetChanged();

        adapter = new LearningReportAdapter(activity, list);
        home_work_gridview.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.learning_report_fragment, null);
        home_work_gridview = (GridView) view.findViewById(R.id.home_work_gridview);
        tv_meiyou = (TextView) view.findViewById(R.id.tv_meiyou);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        home_work_gridview.setOnItemClickListener(this);
        adapter = new LearningReportAdapter(activity, list);
        home_work_gridview.setAdapter(adapter);
        mHandler =new Handler();
    }








    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MobclickAgent.onEvent(getActivity(), "Open_Analysis");
        final ReportDetailBen item = list.remove(position);
        if (item != null) {
            //调用接口告诉服务器已经读了

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        long time = item.getTime();
                        String timeStr=sdf.format(new Date(time*1000));
                        JSONObject json=new JSONObject();
                        json.put("date",Integer.parseInt(timeStr));
                        OkHttpHelper.post(getActivity(), "parents", "uplrpage", json, new OkHttpHelper.HttpListener() {
                            @Override
                            public void onSuccess(int code, String dataJson, String errMsg) {

                            }

                            @Override
                            public void onFail(int HttpCode, String errMsg) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            },3000);



            if(item.getIsread()==0){
                item.setIsread(1);
            }
            list.add(position,item);
            adapter.notifyDataSetChanged();
            AppUtils.clickevent("s_check", activity);
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("title", "学情分析");
            intent.putExtra("url", item.getUrl());
            intent.putExtra("isshowcall", "showcall");
            activity.startActivity(intent);
        }

    }



}
