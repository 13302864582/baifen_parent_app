package com.daxiong.fun.function.cram.holder;

import java.io.Serializable;

public class GoodsNotesModel implements Serializable{
	/*
	  "courseid": 课程ID,
      "coursename": 课程名,
      "grade": 年级,
      "gradeid": 年级ID,
      "orgname": 补习班名字,
      "subject": 科目,
      "subjectid": 科目ID,
      "teacheravatar": 老师头像,
      "teacherid": 老师的userid,
      "teachername": 老师名             
	 */
    
	private static final long serialVersionUID = -914966170169129558L;
	
	private int courseid;
	private String coursename;
	private String grade;
	private int gradeid;
	private String orgname;
	private String subject;
	private int subjectid;
	private String teacheravatar;
	private int teacherid;
	private String teachername;
	public GoodsNotesModel() {
		super();
	}
	public GoodsNotesModel(int courseid, String coursename, String grade, int gradeid, String orgname, String subject,
			int subjectid, String teacheravatar, int teacherid, String teachername) {
		super();
		this.courseid = courseid;
		this.coursename = coursename;
		this.grade = grade;
		this.gradeid = gradeid;
		this.orgname = orgname;
		this.subject = subject;
		this.subjectid = subjectid;
		this.teacheravatar = teacheravatar;
		this.teacherid = teacherid;
		this.teachername = teachername;
	}
	public int getCourseid() {
		return courseid;
	}
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
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
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
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
	public String getTeacheravatar() {
		return teacheravatar;
	}
	public void setTeacheravatar(String teacheravatar) {
		this.teacheravatar = teacheravatar;
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
}
