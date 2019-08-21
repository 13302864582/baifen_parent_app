
package com.daxiong.fun.function.learninganalysis.model;

import java.io.Serializable;
import java.util.List;

/**
 * 日历信息类
 *
 * @author: sky
 */
public class CalendarInfoModel implements Serializable {

    private static final long serialVersionUID = 1417447014231208433L;

    private long register_time;// 注册时间戳

    private List<Calendar_info> calendar_infos;// 日历信息

    private Month_checks_infos month_checks_infos;

    private Month_errors_infos month_errors_infos;

    private Month_server_infos month_server_infos;

    public static class Calendar_info implements Serializable {

        private static final long serialVersionUID = -7632027630924507647L;

        private int state;// 状态(0:未检查,1:有检查,才会有以下参数)

        private int error_count;// 错误个数

        private String dataStr = "";

        private String emptyStr = "";

        private int monthTag;

        private int yearTag;

        private long register_time; // 注册时间
        private int isRight = -1;

        private int isFlagg = -1;


        public int getIsFlagg() {
            return isFlagg;
        }

        public void setIsFlagg(int isFlagg) {
            this.isFlagg = isFlagg;
        }

        public int getIsRight() {
            return isRight;
        }

        public void setIsRight(int isRight) {
            this.isRight = isRight;
        }

        public long getRegister_time() {
            return register_time;
        }

        public void setRegister_time(long register_time) {
            this.register_time = register_time;
        }

        public int getYearTag() {
            return yearTag;
        }

        public void setYearTag(int yearTag) {
            this.yearTag = yearTag;
        }

        public int getMonthTag() {
            return monthTag;
        }

        public void setMonthTag(int monthTag) {
            this.monthTag = monthTag;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getError_count() {
            return error_count;
        }

        public void setError_count(int error_count) {
            this.error_count = error_count;
        }

        public String getDataStr() {
            return dataStr;
        }

        public void setDataStr(String dataStr) {
            this.dataStr = dataStr;
        }

        public String getEmptyStr() {
            return emptyStr;
        }

        public void setEmptyStr(String emptyStr) {
            this.emptyStr = emptyStr;
        }

    }

    public class Month_checks_infos implements Serializable {

        private static final long serialVersionUID = -490624724539682086L;

        private int count;

        private int rate;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

    }

    public class Month_errors_infos implements Serializable {

        private static final long serialVersionUID = -278861210760168696L;

        private int count;

        private int rate;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

    }

    public class Month_server_infos implements Serializable {

        private static final long serialVersionUID = -7359683051665495868L;

        private int time;

        private int rate;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

    }

    public long getRegister_time() {
        return register_time;
    }

    public void setRegister_time(long register_time) {
        this.register_time = register_time;
    }

    public List<Calendar_info> getCalendar_infos() {
        return calendar_infos;
    }

    public void setCalendar_infos(List<Calendar_info> calendar_infos) {
        this.calendar_infos = calendar_infos;
    }

    public Month_checks_infos getMonth_checks_infos() {
        return month_checks_infos;
    }

    public void setMonth_checks_infos(Month_checks_infos month_checks_infos) {
        this.month_checks_infos = month_checks_infos;
    }

    public Month_errors_infos getMonth_errors_infos() {
        return month_errors_infos;
    }

    public void setMonth_errors_infos(Month_errors_infos month_errors_infos) {
        this.month_errors_infos = month_errors_infos;
    }

    public Month_server_infos getMonth_server_infos() {
        return month_server_infos;
    }

    public void setMonth_server_infos(Month_server_infos month_server_infos) {
        this.month_server_infos = month_server_infos;
    }

}
