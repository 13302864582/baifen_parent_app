package com.daxiong.fun.util;

public class GradeUtil {
	public static String getGradeString(int gradeId) {
		if (gradeId == 1) {
			return new String("初一");
		}else if(gradeId == 2) {
			return new String("初二");
		}else if(gradeId == 3) {
			return new String("初三");
		}else if(gradeId == 4) {
			return new String("高一");
		}else if(gradeId == 5) {
			return new String("高二");
		}else if(gradeId == 6) {
			return new String("高三");
		}else if(gradeId == 7) {
			return new String("一年级");
		}else if(gradeId == 8) {
			return new String("二年级");
		}else if(gradeId == 9) {
			return new String("三年级");
		}else if(gradeId == 10) {
			return new String("四年级");
		}else if(gradeId == 11) {
			return new String("五年级");
		}else if(gradeId == 12) {
			return new String("六年级");
		}
		return new String("");
	}
}
