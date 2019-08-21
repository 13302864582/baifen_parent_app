package com.daxiong.fun.util;

import java.util.Calendar;

/**
 * 工具类
 */
public class SpecialCalendarUtils {

    private int daysOfMonth = 0;      //某月的天数
    private int dayOfWeek = 0;        //具体某一天是星期几

    // 判断是否为闰年
    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    //得到某月有多少天数
    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    //指定某年中的某月的第一天是星期几
    public int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    //根据月份推算前一个月
    public int getLastMonth(String monthStr) {
        int lastMonth = 0;
        int month=Integer.parseInt(monthStr);
        switch (month) {
            case 1:
                lastMonth = 12;
                break;
            case 2:
                lastMonth = 1;
                break;
            case 3:
                lastMonth = 2;
                break;
            case 4:
                lastMonth = 3;
                break;
            case 5:
                lastMonth = 4;
                break;
            case 6:
                lastMonth = 5;
                break;
            case 7:
                lastMonth = 6;
                break;
            case 8:
                lastMonth = 7;
                break;
            case 9:
                lastMonth = 8;
                break;

            case 10:
                lastMonth = 9;
                break;
            case 11:
                lastMonth = 10;
                break;
            case 12:
                lastMonth = 11;
                break;
        }
        return  lastMonth;
    }


    //根据月份推算后一个月
    public int getNextMonth(String monthStr) {
        int month=Integer.parseInt(monthStr);
        int nextMonth = 0;
        switch (month) {
            case 1:
                nextMonth = 2;
                break;
            case 2:
                nextMonth = 3;
                break;
            case 3:
                nextMonth = 4;
                break;
            case 4:
                nextMonth = 5;
                break;
            case 5:
                nextMonth = 6;
                break;
            case 6:
                nextMonth = 7;
                break;
            case 7:
                nextMonth = 8;
                break;
            case 8:
                nextMonth = 9;
                break;
            case 9:
                nextMonth = 10;
                break;

            case 10:
                nextMonth = 11;
                break;
            case 11:
                nextMonth = 12;
                break;
            case 12:
                nextMonth = 1;
                break;
        }
        return  nextMonth;
    }


}
