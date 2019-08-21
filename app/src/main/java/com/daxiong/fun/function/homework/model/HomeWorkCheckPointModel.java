package com.daxiong.fun.function.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeWorkCheckPointModel implements Serializable {
	
	public static final String TAG = HomeWorkCheckPointModel.class.getSimpleName();


	/** 检查点唯一ID */
	private int id;
	
//	
	/** 顺序ID, # 从1开始 */
	private int picid;
	
	/** 顺序ID, # 从1开始 */
	private int cpseqid;
	
	/** 对错 */
	private int isright;
	/** 坐标 */
	private String coordinate;
	/** 检查时间 */
	private long checktime;
	/** 解答图片path */
	private String imgpath;
//	
	/** 知识点 */
	private String wrongtype;
	
	/** 知识点 */
	private String kpoint;
	
	/** 老师id */
	private int grabuserid;
	
	/** 老师姓名 */
	private String teachername;
	
	/** 老师头像url */
	private String teacheravatar;
	
	/** 老师解答次数 */
	private int teacherhomeworkcnt;
	
	

	
	
	/**投诉类型 */
	private int complainttype;
	/**显示投诉类型 */
	private int showcomplainttype;
	/**修正类型 */
	private int revisiontype;
	
	private int state;//点的状态
	
	

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	

	public int getComplainttype() {
		return complainttype;
	}

	public void setComplainttype(int complainttype) {
		this.complainttype = complainttype;
	}

	public int getShowcomplainttype() {
		return showcomplainttype;
	}

	public void setShowcomplainttype(int showcomplainttype) {
		this.showcomplainttype = showcomplainttype;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8583978909534839927L;




	public int getRevisiontype() {
		return revisiontype;
	}

	public void setRevisiontype(int revisiontype) {
		this.revisiontype = revisiontype;
	}

	private boolean allowAppendAsk;
	
	
	public int getGrabuserid() {
		return grabuserid;
	}

	public void setGrabuserid(int grabuserid) {
		this.grabuserid = grabuserid;
	}

	public String getWrongtype() {
		return wrongtype;
	}

	public void setWrongtype(String wrongtype) {
		this.wrongtype = wrongtype;
	}

	public String getKpoint() {
		return kpoint;
	}

	public void setKpoint(String kpoint) {
		this.kpoint = kpoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getTeacherhomeworkcnt() {
		return teacherhomeworkcnt;
	}

	public void setTeacherhomeworkcnt(int teacherhomeworkcnt) {
		this.teacherhomeworkcnt = teacherhomeworkcnt;
	}

	public boolean isAllowAppendAsk() {
		return allowAppendAsk;
	}

	public void setAllowAppendAsk(boolean allowAppendAsk) {
		this.allowAppendAsk = allowAppendAsk;
	}

	@Override
	public String toString() {
		return "HomeWorkCheckPointModel [id=" + id + ", picid=" + picid + ", cpseqid=" + cpseqid + ", isright="
				+ isright + ", coordinate=" + coordinate + ", checktime=" + checktime + ", imgpath=" + imgpath
				/*+ ", isLocal=" + isLocal*/ + ", wrongtype=" + wrongtype + ", kpoint=" + kpoint + ", grabuserid="
				+ grabuserid + ", teachername=" + teachername + ", teacheravatar=" + teacheravatar
				+ ", teacherhomeworkcnt=" + teacherhomeworkcnt/* + ", clickAction=" + clickAction*/ + ", allowAppendAsk="
				+ allowAppendAsk + ", explianlist=" + explianlist + "]";
	}

//	public int getClickAction() {
//		return clickAction;
//	}
//
//	public void setClickAction(int clickAction) {
//		this.clickAction = clickAction;
//	}

//	public int getTaskid() {
//		return taskid;
//	}
//
//	public void setTaskid(int taskid) {
//		this.taskid = taskid;
//	}
//
//	public int getPicseqid() {
//		return picseqid;
//	}
//
//	public void setPicseqid(int picseqid) {
//		this.picseqid = picseqid;
//	}

	public int getPicid() {
		return picid;
	}

	public void setPicid(int picid) {
		this.picid = picid;
	}

//	public boolean isLocal() {
//		return isLocal;
//	}
//
//	public void setLocal(boolean isLocal) {
//		this.isLocal = isLocal;
//	}

	private ArrayList<HomeWorkSinglePoint>  explianlist;
	

	public ArrayList<HomeWorkSinglePoint> getExplianlist() {
		return explianlist;
	}

	public void setExplianlist(ArrayList<HomeWorkSinglePoint> explianlist) {
		this.explianlist = explianlist;
	}


	public int getIsright() {
		return isright;
	}

	public void setIsright(int isright) {
		this.isright = isright;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public long getChecktime() {
		return checktime;
	}

	public void setChecktime(long checktime) {
		this.checktime = checktime;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

//	public int getWidth() {
//		return width;
//	}
//
//	public void setWidth(int width) {
//		this.width = width;
//	}
//
//	public int getHeight() {
//		return height;
//	}
//
//	public void setHeight(int height) {
//		this.height = height;
//	}

	public int getCpseqid() {
		return cpseqid;
	}

	public void setCpseqid(int cpseqid) {
		this.cpseqid = cpseqid;
	}

}
