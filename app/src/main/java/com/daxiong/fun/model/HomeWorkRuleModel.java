package com.daxiong.fun.model;

import java.io.Serializable;

public class HomeWorkRuleModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int gradeid;
	private String grade;
	private int default_pic_count;
	private int max_pic_count;
	private float default_pic_money;
	private float single_pic_money;
	private int time;

	public int getGradeId() {
		return gradeid;
	}

	public void setGradeId(int gradeId) {
		this.gradeid = gradeId;
	}

	public String getGarde() {
		return grade;
	}

	public void setGrade(String garde) {
		this.grade = garde;
	}

	public int getDefault_pic_count() {
		return default_pic_count;
	}

	public void setDefault_pic_count(int default_pic_count) {
		this.default_pic_count = default_pic_count;
	}

	public int getMax_pic_count() {
		return max_pic_count;
	}

	public void setMax_pic_count(int max_pic_count) {
		this.max_pic_count = max_pic_count;
	}

	public float getDefault_pic_money() {
		return default_pic_money;
	}

	public void setDefault_pic_money(float default_pic_money) {
		this.default_pic_money = default_pic_money;
	}

	public float getSingle_pic_money() {
		return single_pic_money;
	}

	public void setSingle_pic_money(float single_pic_money) {
		this.single_pic_money = single_pic_money;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
