package com.daxiong.fun.util;


/**
 * 时间转换
 * @author:  sky
 */
public class TimeUnitConvert {
    
    

    /**
     * 将 毫秒 转成 天.
     * @param ms 毫秒
     * @return 天
     */
    public static double ms2day(double ms) {
        return ms / 1000.0 / 60.0 / 60.0 / 24.0;
    }

    /**
     * 将 毫秒 转成 时.
     * @param ms 毫秒
     * @return 时
     */
    public static double ms2h(double ms) {
        return ms / 1000.0 / 60.0 / 60.0;
    }

    /**
     * 将 毫秒 转成 分.
     * @param ms 毫秒
     * @return 分
     */
    public static double ms2min(double ms) {
        return ms / 1000.0 / 60.0;
    }

    /**
     * 将 毫秒 转成 秒.
     * @param ms 毫秒
     * @return 秒
     */
    public static double ms2s(double ms) {
        return ms / 1000.0;
    }

    /**
     * 将 秒 转成 天.
     * @param s 秒
     * @return 天
     */
    public static double s2day(double s) {
        return s / 60.0 / 60.0 / 24.0;
    }

    /**
     * 将 秒 转成 时.
     * @param s 秒
     * @return 时
     */
    public static double s2h(double s) {
        return s / 60.0 / 60.0;
    }

    /**
     * 将 秒 转成 分.
     * @param s 秒
     * @return 分
     */
    public static double s2min(double s) {
        return s / 60.0;
    }

    /**
     * 将 秒 转成 毫秒.
     * @param s 秒
     * @return 毫秒
     */
    public static double s2ms(double s) {
        return s * 1000.0;
    }

    /**
     * 将 分 转成 天.
     * @param min 分
     * @return 天
     */
    public static double min2day(double min) {
        return min / 60.0 / 24.0;
    }

    /**
     * 将 分 转成 时.
     * @param min 分
     * @return 时
     */
    public static double min2h(double min) {
        return min / 60.0;
    }

    /**
     * 将 分 转成 秒.
     * @param min 分
     * @return 秒
     */
    public static double min2s(double min) {
        return min * 60.0;
    }

    /**
     * 将 分 转成 毫秒.
     * @param min 分
     * @return 毫秒
     */
    public static double min2ms(double min) {
        return min * 60.0 * 1000.0;
    }

    /**
     * 将 时 转成 天.
     * @param h 时
     * @return 天
     */
    public static double h2day(double h) {
        return h / 24.0;
    }

    /**
     * 将 时 转成 分.
     * @param h 时
     * @return 分
     */
    public static double h2min(double h) {
        return h * 60.0;
    }

    /**
     * 将 时 转成 秒.
     * @param h 时
     * @return 秒
     */
    public static double h2s(double h) {
        return h * 60.0 * 60.0;
    }

    /**
     * 将 时 转成 毫秒.
     * @param h 时
     * @return 毫秒
     */
    public static double h2ms(double h) {
        return h * 60.0 * 60.0 * 1000.0;
    }

    /**
     * 将 天 转成 时.
     * @param day 天
     * @return 时
     */
    public static double day2h(double day) {
        return day * 24.0;
    }

    /**
     * 将 天 转成 分.
     * @param day 天
     * @return 分
     */
    public static double day2min(double day) {
        return day * 24.0 * 60.0;
    }

    /**
     * 将 天 转成 秒.
     * @param day 天
     * @return 秒
     */
    public static double day2s(double day) {
        return day * 24.0 * 60.0 * 60.0;
    }

    /**
     * 将 天 转成 毫秒.
     * @param day 天
     * @return 毫秒
     */
    public static double day2ms(double day) {
        return day * 24.0 * 60.0 * 60.0 * 1000.0;
    }
    

    /**
     * 将 天 转成周.
     * @param day 天
     * @return 周
     */
    public static double day2week(double day) {
        return day / 7;
    }
    /**
     * 将 周  转成天.
     * @param week 周
     * @return 天
     */
    public static double week2day(double week) {
        return week * 7;
    }


}
