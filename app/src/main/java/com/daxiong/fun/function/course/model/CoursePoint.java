package com.daxiong.fun.function.course.model;

import java.io.Serializable;

public class CoursePoint implements Serializable{
	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1761046249921358865L;

	/** 点坐标 */
	private String coordinate;
	
	/** 文本内容 */
	private String text;
	
	/** 语音url */
	private String sndurl;
	
	/** 点类型, # 1 声音 2 文本 */
	private int type;
	
	/** 顺序 1 2 3 4 ... */
	private int seqid;
	
	/** 角色Id. */
	private int roleid;
	private int subtype;
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSndurl() {
		return sndurl;
	}

	public void setSndurl(String sndurl) {
		this.sndurl = sndurl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Point [coordinate=" + coordinate + ", text=" + text + ", sndurl=" + sndurl + ", type=" + type
				+ ", seqid=" + seqid + ", roleid=" + roleid + "]";
	}

	public int getSeqid() {
		return seqid;
	}

	public void setSeqid(int seqid) {
		this.seqid = seqid;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	
	
}
