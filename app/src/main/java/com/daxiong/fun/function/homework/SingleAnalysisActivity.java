
package com.daxiong.fun.function.homework;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;

/**
 * 单个作业分析activity
 * 
 * @author: sky
 */
public class SingleAnalysisActivity extends BaseActivity {

    private RelativeLayout back_layout;
    private ImageView iv_teacher_avatar;
 
    private TextView tv_sumary;

    private FrameLayout layout_record;
    private ImageView iv_voice;
    private ImageView iv_del;

    private AnimationDrawable mAnimationDrawable;

    private HomeWorkAPI homeworkApi;

    private int taskid = 0;

  
    
    
  

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.single_analysis_activity);
        getExtra();
        initView();
        initListener();
        initData();

    }

    public void getExtra() {
        taskid = getIntent().getIntExtra("taskid", 0);
    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout)this.findViewById(R.id.back_layout);
        RelativeLayout next_setp_layout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        next_setp_layout.setVisibility(View.GONE);
        tv_sumary = (TextView)this.findViewById(R.id.tv_sumary);
        layout_record = (FrameLayout)this.findViewById(R.id.layout_record);
        iv_voice = (ImageView)this.findViewById(R.id.iv_voice);
        iv_teacher_avatar=(ImageView)this.findViewById(R.id.iv_teacher_avatar);
        iv_del=(ImageView)this.findViewById(R.id.iv_del);
        homeworkApi = new HomeWorkAPI();
        setWelearnTitle("作业分析");
    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        iv_del.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.iv_del:
                finish();
                break;
        }
    }

    public void initData() {
        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            if (taskid > 0) {
                homeworkApi.viewremark(requestQueue, taskid, this,
                        RequestConstant.SINGLE_HW_ANALYSIS);
            } else {
                ToastUtils.show("参数传递错误");
            }

        } else {
            ToastUtils.show("无网络");
        }

    }

    private void showVoice(final SingleHwAnalysisModel model) {
        // 显示语音
        layout_record.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.drawable.ic_play2);
        layout_record.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(SingleAnalysisActivity.this,
                        EventConstant.CUSTOM_EVENT_PLAY_AUDIO);
                /*
                 * StatService .onEvent( mActivity, EventConstant
                 * .CUSTOM_EVENT_PLAY_AUDIO, "");
                 */
                if (TextUtils.isEmpty(model.getRemark_snd_url())) {
                    ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
                    return;
                }
                iv_voice.setImageResource(R.drawable.play_animation);
                mAnimationDrawable = (AnimationDrawable)iv_voice.getDrawable();
                MyApplication.animationDrawables.add(mAnimationDrawable);
                MyApplication.anmimationPlayViews.add(iv_voice);
                /*
                 * WeLearnMediaUtil.getInstance(false) .stopPlay();
                 */
                MediaUtil.getInstance(false).playVoice(false, model.getRemark_snd_url(),
                        mAnimationDrawable, new ResetImageSourceCallback() {

                    @Override
                    public void reset() {
                        iv_voice.setImageResource(R.drawable.ic_play2);
                        // isSound[i] =
                        // false;
                    }

                    @Override
                    public void playAnimation() {
                    }

                    @Override
                    public void beforePlay() {
                        MediaUtil.getInstance(false).resetAnimationPlay(iv_voice);
                    }
                }, null);
            }
        });

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.SINGLE_HW_ANALYSIS:// 单个作业分析
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            closeDialog();
                            String dataStr = JsonUtil.getString(datas, "Data", "");
                            if (!TextUtils.isEmpty(dataStr)) {
                                SingleHwAnalysisModel singleHwAnalysisModel = JSON
                                        .parseObject(dataStr, SingleHwAnalysisModel.class);

                                bindData(singleHwAnalysisModel);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }

    @SuppressWarnings("unchecked")
    public void bindData(SingleHwAnalysisModel singleHwAnalysisModel) {    
        if (!TextUtils.isEmpty(singleHwAnalysisModel.getAvatar())) {
            Glide.with(SingleAnalysisActivity.this).load(singleHwAnalysisModel.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(SingleAnalysisActivity.this))
            .placeholder(R.drawable.teacher_img).into(iv_teacher_avatar);                 
        }
        if (!TextUtils.isEmpty(singleHwAnalysisModel.getRemark_txt())) {
            tv_sumary.setVisibility(View.VISIBLE);
            tv_sumary.setText(singleHwAnalysisModel.getRemark_txt());           
        } else {
            tv_sumary.setVisibility(View.GONE);
        }
              

        if (TextUtils.isEmpty(singleHwAnalysisModel.getRemark_snd_url())) {
            layout_record.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(singleHwAnalysisModel.getRemark_snd_url())
                    && singleHwAnalysisModel.getRemark_snd_url().contains(".amr")) {
                layout_record.setVisibility(View.VISIBLE);                
                // Glide.with(this).load(singleHwAnalysisModel.getAvatar())
                // .bitmapTransform(new CropCircleTransformation(this))
                // .placeholder(R.drawable.teacher_img).into(iv_user_avatar_voice);
                showVoice(singleHwAnalysisModel);
            } else {
                layout_record.setVisibility(View.GONE);
            }

        }
    }

}
