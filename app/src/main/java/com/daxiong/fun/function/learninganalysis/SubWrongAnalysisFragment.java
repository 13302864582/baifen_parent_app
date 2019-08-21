package com.daxiong.fun.function.learninganalysis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.function.learninganalysis.adapter.CalendarGridviewAdapter;
import com.daxiong.fun.function.learninganalysis.iflytek.util.activity.TtsSettings;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 错题分布Fragment
 *
 * @author: sky
 */
public class SubWrongAnalysisFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    private static final String TAG = "SubWrongAnalysisFragmen";

    @Bind(R.id.calendar_gridview)
    MyGridView calendarGridview;

    private MainActivity activity;
    private View view;
    private CalendarGridviewAdapter calendarAdapter;
    private List<XueqingBigModel.WrongCalendarBean> list;
    private long regtime;//注册时间
    private String currentDate = "";
    private int currentYear, currentMonth, currentDay;
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    String clickData = "";
    boolean isChecked = false;


    //讯飞
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.hw_analysis_fragment, null);
        ButterKnife.bind(this, view);
        initView(view);
        initListener();
        initObject();
        return view;

    }

    private void initObject() {

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(activity, mTtsInitListener);

        // 云端发音人名称列表
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        mSharedPreferences = activity.getSharedPreferences(TtsSettings.PREFER_NAME, activity.MODE_PRIVATE);
        mToast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //initData(subjectid, extraDate);
        } else {
            if (isPauseSpeaking) {
                isPauseSpeaking = false;
                mTts.pauseSpeaking();
                calendarAdapter.setClickSelector(clickPosition, 0);
            }
        }
    }
    @Override
    public void initView(View view) {
        activity = (MainActivity) getActivity();


    }

    @Override
    public void initListener() {
        super.initListener();
        calendarGridview.setOnItemClickListener(this);

    }

    public void setData(long regtime, String currentYearStr, String currentMonthStr, String currentDayStr, List<XueqingBigModel.WrongCalendarBean> list) {
        this.regtime = regtime;
        this.list = list;

     /*   Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        currentYear = Integer.parseInt(currentDate.split("-")[0]);
        currentMonth = Integer.parseInt(currentDate.split("-")[1]);
        currentDay = Integer.parseInt(currentDate.split("-")[2]);*/

        jumpYear = 0;
        jumpMonth = 0;

        currentYear = Integer.parseInt(currentYearStr);
        currentMonth = Integer.parseInt(currentMonthStr);
        currentDay = Integer.parseInt(currentDayStr);


        /*currentYear=2015;
        currentMonth=11;
        currentDay=01;*/

        calendarAdapter = new CalendarGridviewAdapter(getActivity(), list, jumpMonth, jumpYear, currentYear, currentMonth, currentDay, clickData, isChecked, regtime);
        calendarGridview.setAdapter(calendarAdapter);

    }


    @Override
    public void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(activity);
        FlowerCollector.onPageStart(TAG);
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(activity);
        mTts.pauseSpeaking();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step_btn:// 下一步
                break;

        }
    }

    boolean  isPauseSpeaking=false;

    public void playText(String text) {
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(activity, "tts_play");

        // 设置参数
        setParam();

        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

            if (code != ErrorCode.SUCCESS) {
                if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                    //未安装则跳转到提示安装页面
                    // mInstaller.install();
                } else {
                    showTip("语音合成失败,错误码: " + code);
                }
            }

        }




    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //showTip("播放完成");
                calendarAdapter.setClickSelector(clickPosition,0);
                isPauseSpeaking=false;
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    /**
     * 参数设置
     *
     * @param
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
     /*   if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            *//**
         * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
         * 开发者如需自定义参数，请参考在线合成参数设置
         *//*
        }*/

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "80"));

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    int clickPosition=-1;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XueqingBigModel.WrongCalendarBean item = (XueqingBigModel.WrongCalendarBean) calendarAdapter.getItem(position);
        //calendarGridview.findViewWithTag()

        if ("last".equals(item.getEmptyStr())) {
            if (item.getIsRight() == 0) {
                ToastUtils.show("哈哈，这天还没有注册啦，今天有作业请发啦");
                return;
            } else {
                if (item.getIsFlagg() == 0) {
                    //点击的日期在今天之后
                    ToastUtils.show("哈哈，今天还没有到来啦，今天有作业请发啦");
                    return;
                }
                //调用接口回到上个月
                Intent intentxxx = new Intent("com.action.choosedate");
                intentxxx.putExtra("date", currentYear + "-" + currentMonth);
                intentxxx.putExtra("tag", "last");
                activity.sendBroadcast(intentxxx);
            }

        } else if ("next".equals(item.getEmptyStr())) {
            if (item.getIsRight() == 0) {
                ToastUtils.show("哈哈，这天还没有注册啦，今天有作业请发啦");
                return;
            } else {
                if (item.getIsFlagg() == 0) {
                    //点击的日期在今天之后
                    ToastUtils.show("哈哈，今天还没有到来啦，今天有作业请发啦");
                    return;
                }

                //调用接口回到上个月
                Intent intentxxx = new Intent("com.action.choosedate");
                intentxxx.putExtra("date", currentYear + "-" + currentMonth);
                intentxxx.putExtra("tag", "next");
                activity.sendBroadcast(intentxxx);

            }
        } else {
            if (item.getIsRight() == 0) {
                ToastUtils.show("哈哈，这天还没有注册啦，今天有作业请发啦");
                return;
            }


            if (item.getIsFlagg() == 0) {
                //点击的日期在今天之后
                ToastUtils.show("哈哈，今天还没有到来啦，今天有作业请发啦");
                return;
            }




            if (clickPosition==position&&isPauseSpeaking){
                isPauseSpeaking=false;
                mTts.pauseSpeaking();
                calendarAdapter.setClickSelector(clickPosition,0);
            }else{
                isPauseSpeaking=true;
                calendarAdapter.setClickSelector(position,1);
                //语音
                playText(item.getCm());
            }
            clickPosition=position;

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
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();

    }


}
