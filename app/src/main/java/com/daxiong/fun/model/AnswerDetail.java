package com.daxiong.fun.model;

import java.util.List;

public class AnswerDetail {

	private int answer_id;
	private String t_avatar;
	private String grabtime;
	private int grabuserid;
	private String grabuser;
	private String a_imgurl;
	private int praisecnt;
	private int praise;
	private String schools;
	private int height;
	private int width;
	private int a_state;
	private int roleid;
	private List<AnswerSound> answer_snd;
	
	
	@Override
	public String toString() {
		return "AnswerDetail [answer_id=" + answer_id + ", t_avatar="
				+ t_avatar + ", grabtime=" + grabtime + ", grabuserid="
				+ grabuserid + ", grabuser=" + grabuser + ", a_imgurl="
				+ a_imgurl + ", praisecnt=" + praisecnt + ", praise=" + praise
				+ ", schools=" + schools + ", height=" + height + ", width="
				+ width + ", a_state=" + a_state + ", roleid=" + roleid
				+ ", answer_snd=" + answer_snd + "]";
	}
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}
	public String getSchools() {
		return schools;
	}
	public void setSchools(String schools) {
		this.schools = schools;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getPraisecnt() {
		return praisecnt;
	}
	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}
	public int getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(int answer_id) {
		this.answer_id = answer_id;
	}
	public String getT_avatar() {
		return t_avatar;
	}
	public void setT_avatar(String t_avatar) {
		this.t_avatar = t_avatar;
	}
	public String getGrabtime() {
		return grabtime;
	}
	public void setGrabtime(String grabtime) {
		this.grabtime = grabtime;
	}
	public int getGrabuserid() {
		return grabuserid;
	}
	public void setGrabuserid(int grabuserid) {
		this.grabuserid = grabuserid;
	}
	public String getGrabuser() {
		return grabuser;
	}
	public void setGrabuser(String grabuser) {
		this.grabuser = grabuser;
	}
	public String getA_imgurl() {
		return a_imgurl;
	}
	public void setA_imgurl(String a_imgurl) {
		this.a_imgurl = a_imgurl;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getA_state() {
		return a_state;
	}
	public void setA_state(int a_state) {
		this.a_state = a_state;
	}
	public List<AnswerSound> getAnswer_snd() {
		return answer_snd;
	}
	public void setAnswer_snd(List<AnswerSound> answer_snd) {
		this.answer_snd = answer_snd;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((a_imgurl == null) ? 0 : a_imgurl.hashCode());
		result = prime * result + a_state;
		result = prime * result + answer_id;
		result = prime * result
				+ ((answer_snd == null) ? 0 : answer_snd.hashCode());
		result = prime * result
				+ ((grabtime == null) ? 0 : grabtime.hashCode());
		result = prime * result
				+ ((grabuser == null) ? 0 : grabuser.hashCode());
		result = prime * result + grabuserid;
		result = prime * result + height;
		result = prime * result + praise;
		result = prime * result + praisecnt;
		result = prime * result + roleid;
		result = prime * result + ((schools == null) ? 0 : schools.hashCode());
		result = prime * result
				+ ((t_avatar == null) ? 0 : t_avatar.hashCode());
		result = prime * result + width;
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
		AnswerDetail other = (AnswerDetail) obj;
		if (a_imgurl == null) {
			if (other.a_imgurl != null)
				return false;
		} else if (!a_imgurl.equals(other.a_imgurl))
			return false;
		if (a_state != other.a_state)
			return false;
		if (answer_id != other.answer_id)
			return false;
		if (answer_snd == null) {
			if (other.answer_snd != null)
				return false;
		} else if (!answer_snd.equals(other.answer_snd))
			return false;
		if (grabtime == null) {
			if (other.grabtime != null)
				return false;
		} else if (!grabtime.equals(other.grabtime))
			return false;
		if (grabuser == null) {
			if (other.grabuser != null)
				return false;
		} else if (!grabuser.equals(other.grabuser))
			return false;
		if (grabuserid != other.grabuserid)
			return false;
		if (height != other.height)
			return false;
		if (praise != other.praise)
			return false;
		if (praisecnt != other.praisecnt)
			return false;
		if (roleid != other.roleid)
			return false;
		if (schools == null) {
			if (other.schools != null)
				return false;
		} else if (!schools.equals(other.schools))
			return false;
		if (t_avatar == null) {
			if (other.t_avatar != null)
				return false;
		} else if (!t_avatar.equals(other.t_avatar))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
}
