
package com.daxiong.fun.function.learninganalysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel.RatedetailBean;
import com.daxiong.fun.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 正确率Fragment
 *
 * @author: sky
 */
public class SubRatedetailFragment extends BaseFragment {
    private static final String TAG = "SubRatedetailFragment";
    
    private View view;
    private BaseActivity activity;
    private int maxrcnt;

    private List<RatedetailBean> ratedetails = new ArrayList<>();
    private LinearLayout[] mLl_wais = new LinearLayout[6];
    private RelativeLayout[] mRl_zongs = new RelativeLayout[6];
//    private View[] mV_lvs = new View[6];
    private TextView[] mTv_zhengs = new TextView[6];
    private TextView[] mTv_zongs = new TextView[6];
    private TextView[] mTv_kemus = new TextView[6];



    public void setRatedetails(List<RatedetailBean> ratedetails) {
        this.ratedetails = ratedetails;
        initdata();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.analysis_ratedetail_fragment, null);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void initListener() {
        super.initListener();

    }


  /*  public void setData(MainActivity activity,List<RatedetailBean> ratedetails) {
        LogUtils.e(TAG,"setData");
        this.activity = activity;
        this.ratedetails=ratedetails;
        initdata();

    }*/


    private void initdata() {
        int iWidth = DensityUtil.getScreenWidth() - DensityUtil.dip2px(activity, 40);
        int zongWidth = ratedetails.size() > 4 ? 40 : 50;
        double ihight = 40;
        double ihight2 = 0;
        double jiangeWidth = (iWidth - ((double)DensityUtil.dip2px(activity, zongWidth) * ratedetails.size())) / (ratedetails.size() - 1);

        for (int i=0;i<mLl_wais.length;i++) {
            mLl_wais[i].setVisibility(View.GONE);
            mTv_zhengs[i].clearAnimation();
            mTv_zhengs[i].setVisibility(View.GONE);
        }
        maxrcnt = ratedetails.get(0).getCnt();
        for (RatedetailBean ra : ratedetails) {
            if (ra.getRcnt() > maxrcnt) {
                maxrcnt = ra.getCnt();
            }
        }
        for (int i = 0; i < ratedetails.size() && i < 6; i++) {
            mLl_wais[i].setVisibility(View.VISIBLE);
            if (i != 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins((int)jiangeWidth, 0, 0, 0);
                mLl_wais[i].setLayoutParams(lp);

            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 0);
                mLl_wais[i].setLayoutParams(lp);
            }
            mTv_kemus[i].setText(ratedetails.get(i).getSubject());
            if (ratedetails.get(i).getRcnt() != 0) {
                mTv_zongs[i].setText(ratedetails.get(i).getCnt() + "");
                mTv_zhengs[i].setText(ratedetails.get(i).getRcnt() + "");
                mTv_zhengs[i].setVisibility(View.VISIBLE);
                mTv_zongs[i].setVisibility(View.VISIBLE);
                mRl_zongs[i].setBackgroundResource(R.drawable.learning_situation_correct_rate_nonesubject_bg);

                if (maxrcnt == ratedetails.get(i).getCnt()) {
                    ihight = 190D;


                } else {
                    ihight = (190D / maxrcnt) * ratedetails.get(i).getCnt() ;
                    ihight = ihight > 40D ? ihight : 40D;

                }

                if (ratedetails.get(i).getCnt() == 0) {
                    mTv_zhengs[i].setVisibility(View.GONE);
                    ihight2 = 0;
                } else {
                    ihight2 = (ihight / ratedetails.get(i).getCnt()) * ratedetails.get(i).getRcnt() + 1;
                    ihight2 = ihight2 > 14D ? ihight2 : 14D;
                    ihight2 = (ihight2!=ihight)&&(ihight2>ihight- 14D) ? ihight- 14D : ihight2;
                }

                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(activity,(int) ihight2));
                lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                mTv_zhengs[i].setLayoutParams(lp3);
                final ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                animation.setDuration(800);


                animation.setFillAfter(true);

                mTv_zhengs[i].setAnimation(animation);
                animation.startNow();
            } else {

                mTv_zongs[i].setVisibility(View.GONE);
                mTv_zhengs[i].setVisibility(View.GONE);
                mTv_zongs[i].setText("");
                mTv_zhengs[i].setText("");
                mRl_zongs[i].setBackgroundResource(R.drawable.learning_situation_correct_rate_totalsbj_bg);
                ihight = 190d;
            }
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(DensityUtil.dip2px(activity, zongWidth), DensityUtil.dip2px(activity, (int)ihight));

            mRl_zongs[i].setLayoutParams(lp2);

        }
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void initView(View view) {
        activity = (MainActivity) getActivity();
        mLl_wais[0] = (LinearLayout) view.findViewById(R.id.ll_wai1);
        mRl_zongs[0] = (RelativeLayout) view.findViewById(R.id.rl_zong1);
      //  mV_lvs[0] = view.findViewById(R.id.v_lv1);
        mTv_zhengs[0] = (TextView) view.findViewById(R.id.tv_zheng1);
        mTv_zongs[0] = (TextView) view.findViewById(R.id.tv_zong1);
        mTv_kemus[0] = (TextView) view.findViewById(R.id.tv_kemu1);
        mLl_wais[1] = (LinearLayout) view.findViewById(R.id.ll_wai2);
        mRl_zongs[1] = (RelativeLayout) view.findViewById(R.id.rl_zong2);
       // mV_lvs[1] = view.findViewById(R.id.v_lv2);
        mTv_zhengs[1] = (TextView) view.findViewById(R.id.tv_zheng2);
        mTv_zongs[1] = (TextView) view.findViewById(R.id.tv_zong2);
        mTv_kemus[1] = (TextView) view.findViewById(R.id.tv_kemu2);
        mLl_wais[2] = (LinearLayout) view.findViewById(R.id.ll_wai3);
        mRl_zongs[2] = (RelativeLayout) view.findViewById(R.id.rl_zong3);
    //    mV_lvs[2] = view.findViewById(R.id.v_lv3);
        mTv_zhengs[2] = (TextView) view.findViewById(R.id.tv_zheng3);
        mTv_zongs[2] = (TextView) view.findViewById(R.id.tv_zong3);
        mTv_kemus[2] = (TextView) view.findViewById(R.id.tv_kemu3);
        mLl_wais[3] = (LinearLayout) view.findViewById(R.id.ll_wai4);
        mRl_zongs[3] = (RelativeLayout) view.findViewById(R.id.rl_zong4);
      //  mV_lvs[3] = view.findViewById(R.id.v_lv4);
        mTv_zhengs[3] = (TextView) view.findViewById(R.id.tv_zheng4);
        mTv_zongs[3] = (TextView) view.findViewById(R.id.tv_zong4);
        mTv_kemus[3] = (TextView) view.findViewById(R.id.tv_kemu4);
        mLl_wais[4] = (LinearLayout) view.findViewById(R.id.ll_wai5);
        mRl_zongs[4] = (RelativeLayout) view.findViewById(R.id.rl_zong5);
     //   mV_lvs[4] = view.findViewById(R.id.v_lv5);
        mTv_zhengs[4] = (TextView) view.findViewById(R.id.tv_zheng5);
        mTv_zongs[4] = (TextView) view.findViewById(R.id.tv_zong5);
        mTv_kemus[4] = (TextView) view.findViewById(R.id.tv_kemu5);
        mLl_wais[5] = (LinearLayout) view.findViewById(R.id.ll_wai6);
        mRl_zongs[5] = (RelativeLayout) view.findViewById(R.id.rl_zong6);
     //   mV_lvs[5] = view.findViewById(R.id.v_lv6);
        mTv_zhengs[5] = (TextView) view.findViewById(R.id.tv_zheng6);
        mTv_zongs[5] = (TextView) view.findViewById(R.id.tv_zong6);
        mTv_kemus[5] = (TextView) view.findViewById(R.id.tv_kemu6);





    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
