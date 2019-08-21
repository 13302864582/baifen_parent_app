
package com.daxiong.fun.function.homework;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.daxiong.fun.R;
import com.daxiong.fun.api.AnswerAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.function.homework.ChoiceSubjectAndDateActivity.IExtraData;
import com.daxiong.fun.function.homework.adapter.HomeworkAnalysisAdapter;
import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.function.homework.view.MyMarkerView;
import com.daxiong.fun.model.HomeworkAnalysisModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作业分析
 * 
 * @author: sky
 */
public class HomeworkAnalysisActivity extends BaseActivity implements OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener, IExtraData {

    private LineChart mLineChart;

    private ListView lv;

    private int dayCount = 31;

    private HomeworkAnalysisAdapter mAdapter;

    private AnswerAPI answerApi;

    private int subjectid = 123;

    private String datastr = "";

    private HomeWorkModel homeworkmodel;

    private List<String> xValues;

    private List<Entry> yValues;

    private String tables;

    private List<HomeworkAnalysisModel> homeworkAnalysisList;

    private RelativeLayout back_layout;

    private TextView next_step_btn;

    private int complete_rate;

    private String extraDate;

    private ChoiceSubjectAndDateActivity pop;

    private TextView tv_chart_title;

    private String subjectName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.hw_analysis_activity);
        getExtraData();
        initObj();
        initView();
        initListener();
        initData(subjectid, extraDate);
    }

    public void getExtraData() {
        Intent intent = getIntent();
        homeworkmodel = (HomeWorkModel)intent.getSerializableExtra("homeworkmodel");
    }

    public void initObj() {
        homeworkAnalysisList = new ArrayList<HomeworkAnalysisModel>();
        xValues = new ArrayList<String>();
        yValues = new ArrayList<Entry>();

        answerApi = new AnswerAPI();
        subjectid = homeworkmodel.getSubjectid();
        subjectName = homeworkmodel.getSubject();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        extraDate = format.format(new Date(homeworkmodel.getDatatime()));

    }

    public void initData(int subjectid, String datastr) {
        showDialog("加载中...");
        answerApi.queryHomeworkAnalysis(requestQueue, subjectid, datastr, this,
                RequestConstant.HW_ANALYSIC_CODE);
    }

    @Override
    public void initView() {
        super.initView();
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        this.lv = (ListView)this.findViewById(R.id.lv);

        next_step_btn = (TextView)findViewById(R.id.next_step_btn);
        next_step_btn.setVisibility(View.VISIBLE);
        next_step_btn.setText("筛选");

        View headview = View.inflate(this, R.layout.homework_analysis_headview, null);
        mLineChart = (LineChart)headview.findViewById(R.id.line_chart);
        tv_chart_title = (TextView)headview.findViewById(R.id.tv_chart_title);
        if (!TextUtils.isEmpty(extraDate)) {
            tv_chart_title.setText(extraDate + "  " + subjectName);
        }

        mLineChart.setOnChartGestureListener(this);
        mLineChart.setOnChartValueSelectedListener(this);
        showChart(mLineChart, Color.parseColor("#96e3e3"));// Color.rgb(114,
                                                           // 188, 223)
        mLineChart.invalidate();
        mAdapter = new HomeworkAnalysisAdapter(this, homeworkAnalysisList);
        lv.addHeaderView(headview);
        lv.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
        next_step_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.next_step_btn:// 下一步
                // Intent intent=new
                // Intent(this,ChoiceSubjectAndDateActivity.class);
                // startActivity(intent);

                // pop = new ChoiceSubjectAndDateActivity(this, this);
                // pop.showAtLocation(findViewById(R.id.layout_header),
                // Gravity.TOP, 0, 120);

                pop = new ChoiceSubjectAndDateActivity(this, this);
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                pop.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0,
                        DensityUtil.dip2px(HomeworkAnalysisActivity.this, 48) + statusBarHeight);

                break;
        }
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, int color) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("暂时没有数据");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        lineChart.setScaleYEnabled(false);
        lineChart.setAutoScaleMinMaxEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        // set the marker to the chart
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        lineChart.setMarkerView(mv);

        lineChart.setBackgroundColor(color);// 设置背景

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(LegendForm.LINE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.RED);// 颜色
        // mLegend.setTypeface(mTf);// 字体

        // X轴样式。

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        // xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.removeAllLimitLines();
        xAxis.setSpaceBetweenLabels(0);
        // List<String> months=new ArrayList<String>();
        // for (int i = 0; i < 31; i++) {
        // months.add((i+1)+"");
        // }
        // xAxis.setValues(months);

        // Y轴样式。
        YAxis leftAxis = lineChart.getAxisLeft();// 左边的坐标轴
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(true);
        leftAxis.setLabelCount(10, false);
        leftAxis.setInverted(false);

        lineChart.getAxisRight().setEnabled(false);

        // lineChart.setMaxVisibleValueCount(100);
    }

    /**
     * 生成一个数据
     * 
     * @param count 表示图表中有多少个坐标点
     * @param range 用来生成range以内的随机数
     * @return
     */
    private void getLineData(int count) {

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "y:正确率  x:日期" /* 显示在比例图上 */);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3.5f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#d37d4d"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#d37d4d"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色

        // 设置填充 在折线和x轴之间填充颜色
        lineDataSet.setDrawCircles(true);// 图标上的数据点不用小圆圈表示
        lineDataSet.setDrawCubic(false);// 设置允许曲线平滑
        lineDataSet.setCubicIntensity(0.5f);// 设置折现的平滑度
        lineDataSet.setDrawFilled(true);// 设置允许填充
        lineDataSet.setFillColor(Color.parseColor("#e4fbfb"));// 设置填充的颜色//Color.rgb(0,
                                                              // 255, 255)

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        // add data
        mLineChart.setData(lineData); // 设置数据

        mLineChart.animateX(2500); // 立即执行的动画,x轴
        mLineChart.animateY(2500);

        for (DataSet<?> set : mLineChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        // mLineChart.invalidate();

        mLineChart.invalidate();

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.HW_ANALYSIC_CODE:// 作业分析
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    List<HomeworkAnalysisModel> list = null;
                    if (code == 0) {
                        try {
                            closeDialog();
                            if (homeworkAnalysisList != null && homeworkAnalysisList.size() > 0) {
                                homeworkAnalysisList.clear();
                            }
                            if (xValues != null && xValues.size() > 0) {
                                xValues.clear();
                            }
                            if (yValues != null && yValues.size() > 0) {
                                yValues.clear();
                            }

                            String dataStr = JsonUtil.getString(datas, "Data", "");
                            JSONArray dataJsonArray = null;
                            if (!"".equals(dataStr)) {
                                dataJsonArray = new JSONArray(dataStr);
                                if (dataJsonArray != null && dataJsonArray.length() > 0) {

                                    list = new ArrayList<HomeworkAnalysisModel>();
                                    for (int i = 0; i < dataJsonArray.length(); i++) {
                                        JSONObject itemObj = dataJsonArray.getJSONObject(i);
                                        if (itemObj.has("tables")) {
                                            tables = itemObj.optString("tables");
                                        } else {
                                            String avatar = itemObj.optString("avatar");
                                            int grabuserid = itemObj.optInt("grabuserid");
                                            String datatime = itemObj.optString("datatime");
                                            int day = itemObj.optInt("day");
                                            int id = itemObj.optInt("id");
                                            String kpoint = itemObj.optString("kpoint");
                                            int pointcnt = itemObj.optInt("pointcnt");
                                            String remark_snd_url = itemObj
                                                    .optString("remark_snd_url");
                                            String remark_txt = itemObj.optString("remark_txt");
                                            int rpointcnt = itemObj.optInt("rpointcnt");

                                            HomeworkAnalysisModel model = new HomeworkAnalysisModel(
                                                    avatar, grabuserid, datatime, day, id, kpoint,
                                                    pointcnt, remark_snd_url, remark_txt,
                                                    rpointcnt);
                                            list.add(model);

                                        }
                                    }
                                    homeworkAnalysisList.addAll(list);
                                    if (!TextUtils.isEmpty(tables)) {
                                        splitTableStr(tables);
                                        getLineData(dayCount);
                                    }
                                    if (list != null && list.size() > 0) {
                                        list.clear();
                                    }
                                    mAdapter.indexClear();
                                    mAdapter.notifyDataSetChanged();
                                    
                                }
                            } else {
                                if (homeworkAnalysisList != null
                                        && homeworkAnalysisList.size() == 0) {
                                     mAdapter.initDotData();
                                    for (int i = 0; i < dayCount; i++) {
                                        // x轴显示的数据，这里默认使用数字下标显示
                                        xValues.add(i + 1 + "");
                                        yValues.add(new Entry(0, 0));

                                    }
                                    getLineData(dayCount);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
                            for (int i = 0; i < dayCount; i++) {
                                xValues.add(i + "");
                                yValues.add(new Entry(0, 0));
                            }
                            getLineData(dayCount);
                            mAdapter.notifyDataSetChanged();

                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }

    private void splitTableStr(String tables) {
        if (!TextUtils.isEmpty(tables)) {
            String[] sp = tables.split(",");
            dayCount = sp.length;
            for (int i = 0; i < sp.length; i++) {
                String key = sp[i].split(":")[0];
                String value = sp[i].split(":")[1];
                // x轴显示的数据，这里默认使用数字下标显示
                xValues.add(key + "");
                if (!"-1".equals(value)) {
                    yValues.add(
                            new Entry(Float.parseFloat(value) * 100, Integer.parseInt(key) - 1));
                }

            }

        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("", "low: " + mLineChart.getLowestVisibleXIndex() + ", high: "
                + mLineChart.getHighestVisibleXIndex());
        if (mAdapter != null) {
            mAdapter.setDotData(e.getXIndex() + 1, (int)e.getVal());
        }

    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dealData(String subject, String date) {
        if (pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }

        if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(date)) {
            subject = subject.split("-")[1];
            date = date.replace("年", "").replace("月", "");
            if (Integer.parseInt(date.split("-")[1]) < 10) {
                date = date.split("-")[0] + "-0" + date.split("-")[1];
            } else {
                date = date.split("-")[0] + "-" + date.split("-")[1];
            }
            subjectid = convertSubject(subject);
            if (!TextUtils.isEmpty(subject)) {
                tv_chart_title.setText(date + "  " + subject);
            }
            initData(subjectid, date);
        }

    }

    public int convertSubject(String subject) {
        int subjectid = 2;
        if ("英语".equals(subject)) {
            subjectid = 1;
        } else if ("数学".equals(subject)) {
            subjectid = 2;
        } else if ("物理".equals(subject)) {
            subjectid = 3;
        } else if ("化学".equals(subject)) {
            subjectid = 4;
        } else if ("生物".equals(subject)) {
            subjectid = 5;
        } else if ("语文".equals(subject)) {
            subjectid = 6;
        } else if ("小学全科".equals(subject)) {
            subjectid = 0;
        }
        return subjectid;
    }

}
