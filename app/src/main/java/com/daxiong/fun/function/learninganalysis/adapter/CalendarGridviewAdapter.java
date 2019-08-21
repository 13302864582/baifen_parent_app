package com.daxiong.fun.function.learninganalysis.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.function.learninganalysis.model.XueqingBigModel.WrongCalendarBean;
import com.daxiong.fun.util.DateUtil;
import com.daxiong.fun.util.SpecialCalendarUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 错题分析adapter
 * 日历gridview中的每一个item显示的textview
 * @author sky
 */
public class CalendarGridviewAdapter extends BaseAdapter {

    private static final String TAG = "CalendarGridviewAdapter";

    private Context context;
    private List<WrongCalendarBean> calendarInfoList;


    // 系统当前时间 年月日
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    //当前时间 年月日
    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    // 一个gridview中的日期存入此数组中
    private WrongCalendarBean[] dayNumber = new WrongCalendarBean[42];

    private SpecialCalendarUtils sc = null;
    private Resources res = null;


    private int currentFlag = -1; // 用于标记当天
    private int clickFlag = -1;
    private int clickPosition=-1;
    private String showYear = ""; // 用于在头部显示的年份
    private String showMonth = ""; // 用于在头部显示的月份
    private String leapMonth = ""; // 闰哪一个月


    private boolean isLeapyear = false; // 是否为闰年
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数

    public int getCurrentFlag() {
        return currentFlag;
    }

    public int getClickFlag() {
        return clickFlag;
    }


    public void setClickSelector(int clickPosition,int clickFlag){
        this.clickPosition=clickPosition;
        this.clickFlag=clickFlag;
        notifyDataSetChanged();
    }


    String clickData;
    boolean isChecked;
    private long register_time;



    public void clearCalendarInfoList() {
        calendarInfoList.clear();
    }

    public CalendarGridviewAdapter() {
        Date date = new Date(System.currentTimeMillis());
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    }


    public CalendarGridviewAdapter(Context context, int year, int month, int day) {
        this();
        this.context = context;
        sc = new SpecialCalendarUtils();
        this.res = context.getResources();
        currentYear = String.valueOf(year);
        // 得到跳转到的年份
        currentMonth = String.valueOf(month); // 得到跳转到的月份
        currentDay = String.valueOf(day); // 得到跳转到的天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

    }

    public CalendarGridviewAdapter(Context context, List<WrongCalendarBean> calendarInfoList, int jumpMonth, int jumpYear,
                                   int year_c, int month_c, int day_c, String clickData, boolean isChecked, long register_time) {
        this();
        this.context = context;
        this.calendarInfoList = calendarInfoList;
        this.res = context.getResources();
        this.sc = new SpecialCalendarUtils();
        this.clickData = clickData;
        this.isChecked = isChecked;
        this.register_time = register_time;

        /*int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        // 得到当前的年份
        currentYear = String.valueOf(stepYear);
        // 得到本月
        currentMonth = String.valueOf(stepMonth);
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
        LogUtils.e(TAG,"注册时间:"+register_time);*/




        // 得到当前的年份
        currentYear = String.valueOf(year_c);
        // 得到本月
        currentMonth = String.valueOf(month_c);
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));


    }


    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return dayNumber[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.calendar_item, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WrongCalendarBean item = dayNumber[position];
        String d = item.getDataStr();
        holder.tvValue.setTextColor(Color.GRAY);
        holder.tvValue.setText(d);


        //处理色块的显示




       // 显示上一个月数据
        if ("last".equals(item.getEmptyStr())) {

            holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hui_colore8e8e8));
            holder.tvValue.setTextColor(res.getColor(R.color.colorcccccc));
            holder.tvValue.setText(sc.getLastMonth(currentMonth)+"-"+d);
            holder.tvValue.setTextSize(11);
        }else if("next".equals(item.getEmptyStr())){//显示下个月数据
            holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hui_colore8e8e8));
            holder.tvValue.setTextColor(res.getColor(R.color.colorcccccc));
            holder.tvValue.setText(sc.getNextMonth(currentMonth)+"-"+d);
            holder.tvValue.setTextSize(11);
        }else{//显示本月数据
            holder.tvValue.setTextSize(12);
            if (item.getWcnt() == -1) {// 当日未发布(未检查)
                holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_bai_colorffffff));
                holder.tvValue.setTextColor(res.getColor(R.color.color8d8d8d));
                holder.tvValue.setText("未检查");
                clickHandlers(position, holder);
            } else if(item.getWcnt()==-2){//当日未来临
                holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hui_colore8e8e8));
                holder.tvValue.setTextColor(res.getColor(R.color.colorcccccc));
                holder.tvValue.setText(d);
            }else if(item.getWcnt()==0){//全对
                holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_lv_colorf22c00d));
                holder.tvValue.setTextColor(res.getColor(R.color.white));
                holder.tvValue.setText("全对");
                clickHandlers(position, holder);
            }else if(item.getWcnt()>0){//有错题
                holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hong_colorf74344));
                holder.tvValue.setTextColor(res.getColor(R.color.white));
                holder.tvValue.setText(item.getWcnt()+"");
                clickHandlers(position, holder);
            }else{
                holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hui_colore8e8e8));
                holder.tvValue.setTextColor(res.getColor(R.color.colorcccccc));
                holder.tvValue.setText(d);
            }
        }


        //日期在注册日期之前
        if(item.getIsRight()==0){
            holder.tvValue.setBackgroundDrawable( res.getDrawable(R.drawable.shape_round_rectangle_bg_hui_colore8e8e8));
            holder.tvValue.setTextColor(res.getColor(R.color.colorcccccc));
            holder.tvValue.setText(d);
        }








        return convertView;
    }

    /**
     * 处理点击事件
     * @param position
     * @param holder
     */
    private void clickHandlers(int position, ViewHolder holder) {
        AnimationDrawable mAnimationDrawable=null;
        if (clickPosition==position&&clickFlag==1){
           if("未检查".equals(holder.tvValue.getText())){
               holder.iv_voice.setImageResource(R.drawable.play_animation2);
           }else{
               holder.iv_voice.setImageResource(R.drawable.play_animation3);
           }


            holder.layout_item.setBackgroundDrawable(holder.tvValue.getBackground());
            holder.tvValue.setVisibility(View.GONE);
            holder.iv_voice.setVisibility(View.VISIBLE);

             mAnimationDrawable = (AnimationDrawable) holder.iv_voice.getDrawable();
            MyApplication.animationDrawables.add(mAnimationDrawable);
            MyApplication.anmimationPlayViews.add(holder.iv_voice);
            if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
                mAnimationDrawable.start();
            }
        }else{
            holder.layout_item.setBackgroundDrawable(null);
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.iv_voice.setVisibility(View.GONE);
            if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
            }

        }
    }


    static class ViewHolder {
        @Bind(R.id.layout_item)
        FrameLayout layout_item;
        @Bind(R.id.tv_value)
        TextView tvValue;
        @Bind(R.id.iv_voice)
        ImageView iv_voice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }




    // 得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
        Log.d("DAY", isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek
                + "  =========   " + lastDaysOfMonth);
        getweek(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNuber中
    @SuppressWarnings("deprecation")
    private void getweek(int year, int month) {
        int j = 1;
        int index = 0;

        int isRight = -1;
        int isFlagg = -1;

        for (int i = 0; i < dayNumber.length; i++) {
            // 周一
            if (i < dayOfWeek) { // 前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                // lunarDay = lc.getLunarDate(year, month - 1, temp + i, false);
                WrongCalendarBean item = new WrongCalendarBean();
                item.setDataStr(temp + i + "");
                item.setEmptyStr("last");

                int[] yearAndMonth2 = getYearAndMonth(year, month, true);
                //日期是否在注册时间之前
                boolean x1 = DateUtil.isDateBefore(yearAndMonth2[0] + "-" + yearAndMonth2[1] + "-" + (temp + i),
                        DateUtil.getStrTimeWithMonthAndDay(register_time*1000 + ""));
                if (x1) {
                    item.setIsRight(0);
                } else {
                    item.setIsRight(1);
                }


                boolean x11 = DateUtil.isDateBefore(DateUtil.getStrTimeWithMonthAndDay((System.currentTimeMillis()) + ""), yearAndMonth2[0] + "-" + yearAndMonth2[1] + "-" + (temp+i));
                if (x11) {
                    item.setIsFlagg(0);
                } else {

                    item.setIsFlagg(1);
                }

                dayNumber[i] = item;
            } else if (i < daysOfMonth + dayOfWeek) { // 本月
                String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
                // lunarDay=getData().get(index);
                WrongCalendarBean item = calendarInfoList.get(index);
                index++;
                item.setDataStr(i - dayOfWeek + 1 + "");
                item.setEmptyStr(i - dayOfWeek + 1 + "");


                // 对于当前月才去标记当前日期
                if (!isChecked && sys_year.equals(String.valueOf(year))
                        && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)) {
                    // 笔记当前日期
                    currentFlag = i;
                }

                //日期是否在注册时间之前
                boolean x2 = DateUtil.isDateBefore(year + "-" + month + "-" + day,
                        DateUtil.getStrTimeWithMonthAndDay(register_time*1000+ ""));
                boolean x22 = DateUtil.isDateBefore(DateUtil.getStrTimeWithMonthAndDay((System.currentTimeMillis()) + ""), year + "-" + month + "-" + day);
              /*  if (x1 | x2) {
                    isRight = 2;
                    if (x1) {
                        isFlagg = 1;
                    } else {
                        isFlagg = 2;
                    }

                } else {
                    isRight = 1;
                }*/


                if(x2){
                    isRight=0;
                }else{
                    isRight=1;
                }

                if (x22) {
                    isFlagg = 0;
                } else {
                    isFlagg = 1;
                }

                item.setIsRight(isRight);
                item.setIsFlagg(isFlagg);
                dayNumber[i] = item;
                setShowYear(String.valueOf(year));
                setShowMonth(String.valueOf(month));
            } else { // 下一个月
                WrongCalendarBean item = new WrongCalendarBean();
                item.setDataStr(j + "");
                item.setEmptyStr("next");

                int[] yearAndMonth = getYearAndMonth(year, month, false);

                boolean x3 = DateUtil.isDateBefore(yearAndMonth[0] + "-" + yearAndMonth[1] + "-" + (j + ""),
                        DateUtil.getStrTimeWithMonthAndDay(register_time*1000 + ""));
                if (x3) {
                    item.setIsRight(0);
                } else {
                    item.setIsRight(1);
                }


                //系统当前日期是否在选择的日期之前
                boolean x33 = DateUtil.isDateBefore(DateUtil.getStrTimeWithMonthAndDay((System.currentTimeMillis()) + ""), yearAndMonth[0] + "-" + yearAndMonth[1] + "-" + j);
                if (x33) {
                    item.setIsFlagg(0);
                } else {

                    item.setIsFlagg(1);
                }



                dayNumber[i] = item;
                j++;
            }
        }

        String abc = "";
        for (int i = 0; i < dayNumber.length; i++) {
            abc = abc + dayNumber[i] + ":";
        }
        Log.d("DAYNUMBER", abc);

    }

    public int[] getYearAndMonth(int myear, int mmonth, boolean after) {
        int[] ym = new int[2];
        int stepYear = myear;
        int stepMonth = mmonth;
        if (after) {


            if (mmonth == 1) {
                stepYear = myear - 1;
                stepMonth = 12;
            } else {
                stepYear = myear;
                stepMonth = mmonth - 1;
            }
        } else {

            stepMonth = mmonth + 1;
            if (mmonth == 12) {
                stepYear = myear + 1;
                stepMonth = 1;
            } else {
                stepYear = myear;
                stepMonth = mmonth + 1;
            }
        }

        ym[0] = stepYear;
        ym[1] = stepMonth;
        return ym;
    }

    /**
     * 点击每一个item时返回item中的日期
     *
     * @param position
     * @return
     */
    public WrongCalendarBean getDateByClickItem(int position) {
        return dayNumber[position];
    }

    /**
     * 在点击gridView时，得到这个月中第一天的位置
     *
     * @return
     */
    public int getStartPositon() {
        return dayOfWeek;
    }

    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     *
     * @return
     */
    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }


}
