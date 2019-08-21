/**
 * 
 */

package com.daxiong.fun.util;

import android.text.TextUtils;

import com.daxiong.fun.config.AppConfig;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author parsonswang 时间处理工具类.
 */
public class DateUtil {

	public static final String TAG = DateUtil.class.getSimpleName();

	private static SimpleDateFormat MY_SDF = new SimpleDateFormat("MM月dd日 HH:mm:ss");

	/**
	 * 规则 1分钟以内（今天零点后） 刚刚 刚刚 1分钟-1小时内（今天零点后） n分钟前 1分钟前 ；59分钟前 1小时-24小时内（今天零点后）
	 * n小时前 1小时前 ；23小时前 今天零点-昨天零点 昨天 n时：n分 昨天 23：59 ；昨天 0：00 昨天零点-前天零点 前天 n时：n分
	 * 前天 23：59 ；前天 0：00 前天零点-本月第一天零点 n天前 3天前 ；30天前 本月第一天零点-今年1月1日零点 n个月前 1个月前
	 * ；11个月前 今年1月1日零点前 n年前 1年前 ；n年前
	 * 
	 * @param d
	 * @return
	 */

	public static final String JUST_MINS = "刚刚";

	public static final String MINS_AGO = "分钟前";

	public static final String HOURS_AGO = "小时前";

	public static final String YESTERDAY = "昨天";

	public static final String BEFOREY_YESTERDAY = "前天";

	public static final String DAYS_AGO = "天前";

	public static final String MONTH_AGO = "月前";

	public static final String YEAR_AGO = "年前";

	public static final String HOUR = "时";

	public static final String YEAR = "年";

	public static final String MONTH = "月";

	public static final String DAY = "日";

	public static final String MIN = "分";

	public static final String BEFORE = "前";

	public static final String COLON = ":";

	public static final int DAY_SECOND = 86400;

	public static final int TWO_DAY_SECOND = 172800;

	public static final int TIME_ZONE_SPACE = 28800;

	// private static TimeZone timezone=TimeZone.;
	static String doubleD(int i) {
		if (i < 10) {
			return '0' + String.valueOf(i);
		} else {
			return String.valueOf(i);
		}
	}

	public static boolean isToday(Date d) {

		int year = d.getYear() + 1900;
		int mon = d.getMonth() + 1;
		int day = d.getDate();

		Date now = new Date();

		int nowYear = now.getYear() + 1900;
		int nowMon = now.getMonth() + 1;
		int nowDay = now.getDate();

		if (year == nowYear && mon == nowMon && day == nowDay) {
			return true;
		} else {
			return false;
		}
	}

	public static String getCurTime() {
		return "" + System.currentTimeMillis();
	}

	public static final String getDisplayTime_New(int time, String dateStr, String timeString) {

		if (dateStr == null || timeString == null) {
			return DateUtil.getDisplayTime(time);
		}

		int curTime = (int) (System.currentTimeMillis() / 1000);
		int theDayTime = curTime - curTime % DAY_SECOND - TIME_ZONE_SPACE;
		int theLastDayTime = theDayTime - DAY_SECOND;
		int theTwoLastDayTime = theLastDayTime - DAY_SECOND;

		int intervalTime = curTime - time;

		String RetString = "";

		if (intervalTime < 60) {
			RetString += JUST_MINS;
		} else if (intervalTime >= 60 && intervalTime < 3600) {
			RetString += intervalTime / 60 + MINS_AGO; // 鍒嗛挓
		} else if (time > theDayTime) {
			RetString += intervalTime / 3600 + HOURS_AGO; // 灏忔椂
		} else if (time > theLastDayTime) {
			RetString += YESTERDAY + timeString;
		} else if (time > theTwoLastDayTime) {
			RetString += BEFOREY_YESTERDAY + timeString;
		} else {
			RetString += dateStr + timeString;
		}

		return RetString;

	}

	public static Date parseString2Date(String dateStr, String dateFormat) {

		if (TextUtils.isEmpty(dateStr)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date d = null;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static boolean isBetween() {
		Calendar calendar = Calendar.getInstance();
		// 指定01:00:00点执行
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);

		long beginTs = calendar.getTimeInMillis();

		Calendar calendar1 = Calendar.getInstance();
		// 指定01:00:00点执行
		calendar1.set(Calendar.HOUR_OF_DAY, 21);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);

		long afterTs = calendar1.getTimeInMillis();

		long currentTs = System.currentTimeMillis();

		return currentTs >= beginTs && currentTs <= afterTs;
	}

	public static final String getDisplayTime(Date d) {

		if (null == d) {
			if (AppConfig.IS_DEBUG) {
				LogUtils.e(TAG, "Date is null !");
			}
			return "";
		}

		Calendar calendar = GregorianCalendar.getInstance(Locale.CHINA);
		calendar.setTime(d);
		int yearOfPubDate = d.getYear() + 1900;
		int monOfPubDate = d.getMonth() + 1;
		int dayOfPubDate = d.getDate();
		int hourOfPubDate = d.getHours();
		int minOfPubDate = d.getMinutes();

		// time_t now = time(0);

		// tm* today = localtime((const time_t*)&now);
		Date now = new Date();
		int yearOfToday = now.getYear() + 1900;
		// int dayOfToday = now.getDate();

		long times000 = new Date(now.getYear(), now.getMonth(), now.getDate(), 0, 0, 0).getTime() - d.getTime();

		long times = now.getTime() - d.getTime();

		if (times < 0) {
			times = 0;
		}
		times = times / 1000;

		if (times >= 0 && times < 60) {
			return JUST_MINS;// "刚刚";
		} else if (times >= 60 && times < 60 * 60) {
			return times / 60 + MINS_AGO;// "分钟前";
		} else if (times000 < 0) {
			return times / (60 * 60) + HOURS_AGO;// "小时前";
		} else if (times000 >= 0 && times000 < 60 * 60 * 24 * 1000) {
			return YESTERDAY + doubleD(hourOfPubDate) + COLON + doubleD(minOfPubDate);
		} else if (times000 >= 60 * 60 * 24 * 1000 && times000 < 60 * 60 * 24 * 2 * 1000) {
			return BEFOREY_YESTERDAY + doubleD(hourOfPubDate) + COLON + doubleD(minOfPubDate);
		} else if (times000 >= 60 * 60 * 24 * 2 * 1000) {
			if (yearOfToday != yearOfPubDate) {
				return yearOfPubDate + YEAR + doubleD(monOfPubDate) + MONTH + doubleD(dayOfPubDate) + DAY;
			} else {
				return doubleD(d.getMonth() + 1) + MONTH + doubleD(d.getDate()) + DAY;
			}
		}

		return "";
	}

	public static final String getDisplayTime(long time) {
		// 14463900
		if (time > 99999999) {
			return getDisplayTime(new Date(time * 1000L));
		} else {
			Date d = new Date(time * 1000L);
			return sdf_withoutYear.format(d);
		}
	}	public static final String getDisplayTime2(long time) {

			Date d = new Date(time );
			return sdf_withoutYear2.format(d);

	}

	public static String getCommTime(long time) {
		return MY_SDF.format(new Date(time));
	}

	public static String getMonthweek(long time) {
		return sdf_witMonthweek.format(new Date(time));
	}
	public static String getMonthweek2(long time) {
		return sdf_witMonthweek2.format(new Date(time));
	}

	public static final String getCurrentTime() {
		return sdf.format(new Date());
	}

	public static long convertTimestampToLong(float timestamp) {
		BigDecimal bd = new BigDecimal(timestamp);
		return bd.longValueExact();
	}

	private static long BETWEEN_TIMESTAMPS = 5 * 60 * 1000;// ms为单位

	public static boolean isAfter(long timetamps, long preTimestamp) {
		long result = timetamps - preTimestamp;
		return Math.abs(result) >= BETWEEN_TIMESTAMPS;
	}

	public static final String getDisplayChatTime(Date d) {

		Date now = new Date();

		long between = (now.getTime() - d.getTime()) / 1000;// 除以1000是为了转换成秒

		long day = between / (24 * 3600);

		Calendar calendar = GregorianCalendar.getInstance(Locale.CHINA);
		calendar.setTime(d);
		int huorOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int miniute = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		if (day > 0) {
			if (day == 1) {

				return YESTERDAY + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (day == 2) {
				return BEFOREY_YESTERDAY + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			} else if (day < 30) {
				return day + DAYS_AGO + " " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			} else if (day >= 30) {
				return day / 30 + MONTH_AGO + " " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON
						+ doubleD(seconds);
			}
		} else {
			if (huorOfDay >= 0 && huorOfDay <= 6) {
				return "凌晨 " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			} else if (huorOfDay > 6 && huorOfDay <= 12) {
				return "上午 " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			} else if (huorOfDay > 12 && huorOfDay <= 17) {
				return "下午 " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			} else if (huorOfDay > 17 && huorOfDay < 24) {
				return "晚上 " + doubleD(huorOfDay) + COLON + doubleD(miniute) + COLON + doubleD(seconds);
			}
		}
		return "";
	}

	public static final String getDisplayChatTimeWithOutSeconds(Date d) {

		Date now = new Date();

		long between = (now.getTime() - d.getTime()) / 1000;// 除以1000是为了转换成秒

		long day = between / (24 * 3600);

		Calendar calendar = GregorianCalendar.getInstance(Locale.CHINA);
		calendar.setTime(d);
		int huorOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int miniute = calendar.get(Calendar.MINUTE);
		if (day > 0) {
			if (day == 1) {

				return YESTERDAY + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (day == 2) {
				return BEFOREY_YESTERDAY + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (day < 30) {
				return day + DAYS_AGO + " " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (day >= 30) {
				return day / 30 + MONTH_AGO + " " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			}
		} else {
			if (huorOfDay >= 0 && huorOfDay <= 6) {
				return "凌晨 " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (huorOfDay > 6 && huorOfDay <= 12) {
				return "上午 " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (huorOfDay > 12 && huorOfDay <= 17) {
				return "下午 " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			} else if (huorOfDay > 17 && huorOfDay < 24) {
				return "晚上 " + doubleD(huorOfDay) + COLON + doubleD(miniute);
			}
		}
		return "";
	}

	/**
	 * 计算给定日期，到现在差多少个小时。
	 * 
	 * @param d
	 * @return
	 */
	public static long getHourOffset(Date d) {
		Date now = new Date();

		long between = (now.getTime() - d.getTime()) / 1000;// 除以1000是为了转换成秒
		return between % (24 * 3600) / 3600;
	}

	public static long getHourOffset(int d) {
		Date dd = new Date(d * 1000L);
		Date now = new Date();

		long between = (now.getTime() - dd.getTime()) / 1000;// 除以1000是为了转换成秒
		return between / 3600;
	}

	public static String getDisplayTime1(Date d) {
		Date now = new Date();
		int now_year = now.getYear();
		int now_month = now.getMonth();
		int now_day = now.getDate();
		int now_hour = now.getHours();
		int now_min = now.getMinutes();
		// int now_sec = now.getSeconds();

		int d_year = d.getYear();
		int d_month = d.getMonth();
		int d_day = d.getDate();
		int d_hour = d.getHours();
		int d_min = d.getMinutes();
		// int d_sec = d.getSeconds();

		if (now_year > d_year) {
			return now_year - d_year + YEAR_AGO;
		} else if (now_month > d_month) {
			return now_month - d_month + MONTH_AGO;
		} else if (now_day > d_day) {
			int offset = now_day - d_day;
			if (offset == 1) {
				return YESTERDAY + d_hour + HOUR + COLON + d_min + MIN;
			} else if (offset == 2) {
				return BEFOREY_YESTERDAY + d_hour + HOUR + COLON + d_min;
			} else {
				return offset + DAYS_AGO;
			}
		} else if (now_hour > d_hour) {
			return now_hour - d_hour + HOURS_AGO;
		} else if (now_min > d_min) {
			return now_min - d_min + MINS_AGO;
		} else {
			return JUST_MINS;
		}
	}

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

	public static final SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat chat = new SimpleDateFormat("HH:mm");

	/**
	 * 兼容评论回复时间是8位数的错误，去掉年字段
	 */
	public static final SimpleDateFormat sdf_withoutYear = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
	public static final SimpleDateFormat sdf_withoutYear2 = new SimpleDateFormat("MM月dd日", Locale.CHINA);

	public static final SimpleDateFormat sdf_witMonthweek = new SimpleDateFormat("yyyy年MM月dd日  EEEE", Locale.CHINA);
	public static final SimpleDateFormat sdf_witMonthweek2 = new SimpleDateFormat("yyyy-MM-dd HH:mm  EEEE", Locale.CHINA);

	public static String getDateString(long time) {
		Date d = new Date(time);
		return sdf.format(d);
	}

	public static String getBirthString(long time) {
		Date d = new Date(time);
		return birth.format(d);
	}	public static String getBirthString2() {
		Date d = new Date();
		return birth.format(d);
	}

	public static String getChatString(long time) {
		Date d = new Date(time);
		return chat.format(d);
	}

	public static String getMonthByMillis(long l) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
		String datetime = sdf.format(l * 1000L);
		return datetime;
	}

	public static String getDayByMillis(long l) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.CHINA);
		String datetime = sdf.format(l * 1000L);
		return datetime;
	}

	public static String getHourByMillis(long l) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.CHINA);
		String datetime = sdf.format(l * 1000L);
		return datetime;
	}

	// 将时间戳转为字符串
	public static String getStrTime(String cc_time, String format) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	// 将时间戳转为字符串
	public static String getStrTimeWithMonthAndDay(String dateStr) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		format1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Long time2 = new Long(Long.valueOf(dateStr) + "");

		String d = format1.format(time2);
		java.util.Date date2 = null;
		try {
			date2 = format1.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("时间戳转换成日期:"+format1.format(date2));
		return format1.format(date2);

	}

	public static String time2Ymd(long l) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
		format1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Long time2 = new Long(Long.valueOf(l) + "");

		String d = format1.format(time2);
		java.util.Date date2 = null;
		try {
			date2 = format1.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("时间戳转换成日期:"+format1.format(date2));
		return format1.format(date2);

	}

	public static String getDayOfWeekToday() {
		String day = "";
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			day = "星期日";
			break;
		case 2:
			day = "星期一";
			break;
		case 3:
			day = "星期二";
			break;
		case 4:
			day = "星期三";
			break;
		case 5:
			day = "星期四";
			break;
		case 6:
			day = "星期五";
			break;
		case 7:
			day = "星期六";
			break;

		}
		return day;

	}

	/**
	 * 获取当前的年份
	 */
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return year;

	}

	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		return month;
	}

	public static long getMillis2Data(String time) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			date = format.parse(time);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	// 判断时间date1是否在时间date2之前 时间格式 2005-4-21 16:16:34
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// 判断时间date1是否在时间date2之前 时间格式 2005-4
	public static boolean isDateBefore2(String date1, String date2) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static String getMillis2minute(long l) {
		long fen = l / 1000 / 60;
		long miao = l / 1000 % 60;
		StringBuffer sb = new StringBuffer();
		if (fen != 0) {
			sb.append(fen + "分");
		}
		if (miao != 0) {
			sb.append(miao + "秒");
		}
		return sb.toString();
	}

	

	// 字符串类型日期转化成date类型
	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String dateToStr(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}

	/**
	 * 时间戳转换为yyyy-MM-dd
	 * 
	 * @author: sky
	 * @param dateStr
	 * @return String
	 */
	public static String timestamp2String(String dateStr) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		format1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Long time2 = new Long(Long.valueOf(dateStr) + "");

		String d = format1.format(time2);
		java.util.Date date2 = null;
		try {
			date2 = format1.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("时间戳转换成日期:"+format1.format(date2));
		return format1.format(date2);
	}

	public static String timestamp2String2(long dateStr) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
		format1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// Long time2 = new Long(Long.valueOf(dateStr) + "");

		String d = format1.format(dateStr);
		java.util.Date date2 = null;
		try {
			date2 = format1.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("时间戳转换成日期:"+format1.format(date2));
		return format1.format(date2);
	}

	/**
	 * yyyy-MM-dd转换时间戳
	 * 
	 * @author: sky
	 * @param time
	 * @return long
	 */
	public static long String2Timestamp(String time) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

}
