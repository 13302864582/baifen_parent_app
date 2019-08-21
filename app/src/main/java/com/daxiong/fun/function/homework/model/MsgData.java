package com.daxiong.fun.function.homework.model;

import java.io.Serializable;

public class MsgData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4574911488044845573L;
	
	private int action;// 1打开问题，2打开个人主页，3打开URL，4发起邀请，字段内容待定, 5 作业, 6单题,9班主任简评，10作业问题状态
	private int question_id;
	private String url;
	private int userid;// 打开用户主页时存在
	private int roleid;// 打开用户主页时存在
	
	private int taskid;// 打开作业
	private int checkpointid;// 打开单题
	private int isright;// 打开单题
	private String coordinate;// 打开单题
	private String imgpath;// 打开单题

	private int pageid;// 打开课程追问
	
	
	public int getPageid() {
		return pageid;
	}
	public void setPageid(int pageid) {
		this.pageid = pageid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action;
		result = prime * result + checkpointid;
		result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + ((imgpath == null) ? 0 : imgpath.hashCode());
		result = prime * result + isright;
		result = prime * result + pageid;
		result = prime * result + question_id;
		result = prime * result + roleid;
		result = prime * result + taskid;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + userid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MsgData other = (MsgData) obj;
		if (action != other.action)
			return false;
		if (checkpointid != other.checkpointid)
			return false;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (imgpath == null) {
			if (other.imgpath != null)
				return false;
		} else if (!imgpath.equals(other.imgpath))
			return false;
		if (isright != other.isright)
			return false;
		if (pageid != other.pageid)
			return false;
		if (question_id != other.question_id)
			return false;
		if (roleid != other.roleid)
			return false;
		if (taskid != other.taskid)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (userid != other.userid)
			return false;
		return true;
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
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getCheckpointid() {
		return checkpointid;
	}
	public void setCheckpointid(int checkpointid) {
		this.checkpointid = checkpointid;
	}
	@Override
	public String toString() {
		return "MsgData [action=" + action + ", question_id=" + question_id + ", url=" + url + ", userid=" + userid
				+ ", roleid=" + roleid + ", taskid=" + taskid + ", checkpointid=" + checkpointid + ", isright="
				+ isright + ", coordinate=" + coordinate + ", imgpath=" + imgpath + ", pageid=" + pageid + "]";
	}
	
}
