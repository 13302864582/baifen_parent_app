package com.daxiong.fun.function.course.model;

import java.io.Serializable;

/**
 * 搜索结果 item 实体数据
 *
 */
public class SearchListItemModel implements Serializable{
	
	/*
		"courseid":课程id
		"coursename":课程名称
		"teacherid": 老师学号
		"teachername": 老师昵称
		"teacheravatar": 老师头像
		"gradeid":年级id	  	
		"grade":年级
		"subjectid":科目id
		"subject":科目
	 */

	private static final long serialVersionUID = -914966170169129558L;
	public static final String TAG = SearchListItemModel.class.getSimpleName();
	private int courseid; // 课程id
	private String coursename; //课程名称
	private int teacherid; // 老师学号
	private String teachername;// 老师姓名
	private String teacheravatar; // 老师头像
	private String gradeid; // 年级id
	private String grade; // 年级
	private int subjectid; // 科目id
	private String subject; // 科目
	
	public SearchListItemModel() {
		super();
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeid() {
		return gradeid;
	}

	public void setGradeid(String gradeid) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getTag() {
		return TAG;
	}
}
