package com.daxiong.fun.function.course.model;

import java.io.Serializable;
import java.util.List;

public class CourseDetailsModel implements Serializable{


	/*
	"userid": 老师学号
	"avatar": 老师头像
	"name": 老师姓名
	"coursename": 课程名称
	"content": 课程内容
	"grade": 年级
	"gradeid": 年级id
	"subject": 科目
	"subjectid": 科目id
	"price": 价格
	"charptercount": 课时数
	"state": 课程录制状态
	"buystate": 购买状态,   0 没有购买，1购买
	"charpter":[
		{
			"charpterid": 课时id
			"charptername": 课时名称
			"kpoint": 知识点
		}
	]
	*/
	private static final long serialVersionUID = -914966170169129558L;
	private int userid; //老师学号
	private String avatar; //老师头像
	private String name; //老师姓名
	private String coursename; //课程名称
	private String content; //课程内容
	private String grade; //年级
	private int gradeid; //年级id
	private String subject; //科目
	private int subjectid; //科目id
	private float price; //价格
	private int charptercount; //课时数
	private int state; //课程录制状态
	private int buystate; //购买课程
	
	private List<CharpterModel> charpter;
	
	public CourseDetailsModel() {
		super();
	}
	public CourseDetailsModel(int userid, String avatar, String name, String coursename, String content, String grade,
			int gradeid, String subject, int subjectid, float price, int charptercount, int state,
			List<CharpterModel> charpter) {
		super();
		this.userid = userid;
		this.avatar = avatar;
		this.name = name;
		this.coursename = coursename;
		this.content = content;
		this.grade = grade;
		this.gradeid = gradeid;
		this.subject = subject;
		this.subjectid = subjectid;
		this.price = price;
		this.charptercount = charptercount;
		this.state = state;
		this.charpter = charpter;
	}
	public int getBuystate() {
		return buystate;
	}
	public void setBuystate(int buystate) {
		this.buystate = buystate;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCharptercount() {
		return charptercount;
	}
	public void setCharptercount(int charptercount) {
		this.charptercount = charptercount;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<CharpterModel> getCharpter() {
		return charpter;
	}
	public void setCharpter(List<CharpterModel> charpter) {
		this.charpter = charpter;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
