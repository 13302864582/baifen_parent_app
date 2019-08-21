package com.daxiong.fun.function.homework.model;

import java.io.Serializable;

public class HomeWorkSinglePoint implements Serializable {
	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	public static final String TAG = HomeWorkSinglePoint.class.getSimpleName();

	private static final long serialVersionUID = 1L;

	/** 点顺序 */
	private int exseqid;
	/** 打点用户ID */
	private int userid;
	/** 打点用户角色ID, # 1 学生 2 老师 */
	private int roleid;
	/** 点类型, # 1 声音 2 文本 */
	private int explaintype;
	/** 点坐标 */
	private String coordinate;
	/** 语音url */
	private String sndpath;
	/** 文本内容 */
	private String text;
	/** 打点时间 */
	private long datatime;
	/** 录音时长    */
	public float audioTime;

	private int subtype;


	@Override
	public String toString() {
		return "HomeWorkSinglePoint [exseqid=" + exseqid + ", userid=" + userid + ", roleid=" + roleid
				+ ", explaintype=" + explaintype + ", coordinate=" + coordinate + ", sndpath=" + sndpath + ", text="
				+ text + ", datatime=" + datatime + ", audioTime=" + audioTime + "]";
	}

	public float getAudioTime() {
		return audioTime;
	}

	public void setAudioTime(float audioTime) {
		this.audioTime = audioTime;
	}

	public int getExseqid() {
		return exseqid;
	}

	public void setExseqid(int exseqid) {
		this.exseqid = exseqid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public int getExplaintype() {
		return explaintype;
	}

	public void setExplaintype(int explaintype) {
		this.explaintype = explaintype;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getSndpath() {
		return sndpath;
	}

	public void setSndpath(String sndpath) {
		this.sndpath = sndpath;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public float getPointX() {
		return getPoint(0);
	}

	public float getPonitY() {
		return getPoint(1);
	}

	private float getPoint(int pos) {
		if (null != coordinate) {
			String[] point = coordinate.split(",");
			if (point.length >= 2) {
				return Float.parseFloat(point[pos]);
			} else {
				return 0F;
			}
		} else {
			return 0F;
		}
	}
}
