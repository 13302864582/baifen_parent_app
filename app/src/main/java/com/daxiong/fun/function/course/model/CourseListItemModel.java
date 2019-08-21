package com.daxiong.fun.function.course.model;

import java.io.Serializable;

/**
 * 名师课程 item 实体数据
 *
 */
public class CourseListItemModel implements Serializable{
	
	/*
		"courseid": 课程id
		"coursename": 课程名称
		"charptercount": 课时数目
		"grade": 年级
		"gradeid": 年级id
		"subject": 科目
		"subjectid":科目id
		"teacherid": 老师学号
		"teachername": 老师姓名
		"teacheravatar": 老师头像
		"datatime": 订阅时间
		"price": 购买价格
		"process": 学习进度
		"lasttime": 上一次学习时间
	 */

	private static final long serialVersionUID = -914966170169129558L;
	public static final String TAG = CourseListItemModel.class.getSimpleName();
	private int courseid; // 课程id
	private String coursename; //课程名称
	private int charptercount; // 课时数目
	private String grade; // 年级
	private int gradeid; // 年级id
	private String subject; // 科目
	private int subjectid; // 科目id
	private int teacherid; // 老师学号
	private String teachername;// 老师姓名
	private String teacheravatar; // 老师头像
	private long datatime; // 订阅时间
	private float price; // 购买价格
	private int process; // 学习进度
	private long lasttime; // 上一次学习时间
	private String content; // 课程简介
	
	private boolean isBuy; //是否购买
	
	public CourseListItemModel() {
		super();
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
	public int getCourseid() {
		return courseid;
	}

	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	public int getCharptercount() {
		return charptercount;
	}

	public void setCharptercount(int charptercount) {
		this.charptercount = charptercount;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeid() {
		return gradeid;
	}

	public void setGradeid(int gradeid) {
		this.gradeid = gradeid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public int getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}

	public String getTeachername() {
		return teachername;
	}

	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}

	public String getTeacheravatar() {
		return teacheravatar;
	}

	public void setTeacheravatar(String teacheravatar) {
		this.teacheravatar = teacheravatar;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getTag() {
		return TAG;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}
}
