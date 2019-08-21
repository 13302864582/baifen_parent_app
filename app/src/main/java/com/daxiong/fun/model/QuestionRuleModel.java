package com.daxiong.fun.model;

import java.io.Serializable;

public class QuestionRuleModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int gradeid;
	private String grade;
	private int time;
	private float money;

	public int getGradeId() {
		return gradeid;
	}

	public void setGradeId(int gradeId) {
		this.gradeid = gradeId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

}
