package com.daxiong.fun.function.homework.model;

import java.io.Serializable;

public class StuPublishHomeWorkUploadModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int actionid;
	private String action;
	private StuPublishHomeWorkUploadFileModel picinfo;
	
	private  int  num;
	
	
	
	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid = actionid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public StuPublishHomeWorkUploadFileModel getPicinfo() {
		return picinfo;
	}

	public void setPicinfo(StuPublishHomeWorkUploadFileModel picinfo) {
		this.picinfo = picinfo;
	}

}
