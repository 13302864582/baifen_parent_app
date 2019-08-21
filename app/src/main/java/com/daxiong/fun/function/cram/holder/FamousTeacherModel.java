package com.daxiong.fun.function.cram.holder;

import java.io.Serializable;

public class FamousTeacherModel implements Serializable{
	/*
	"avatar":老师头像路径,
	"major": 主修科目,                       
    "name": 姓名,                                                                                   
    "sex": 性别,                                                     
    "userid": 用户ID
    "orgname":辅导机构名                             
	 */
    
	private static final long serialVersionUID = -914966170169129558L;
	
	private String avatar;
	private String major;
	private String name;
	private String sex;
	private int userid;
	private String orgname;
	public FamousTeacherModel() {
		super();
	}
	public FamousTeacherModel(String avatar, String major, String name, String sex, int userid, String orgname) {
		super();
		this.avatar = avatar;
		this.major = major;
		this.name = name;
		this.sex = sex;
		this.userid = userid;
		this.orgname = orgname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
}
