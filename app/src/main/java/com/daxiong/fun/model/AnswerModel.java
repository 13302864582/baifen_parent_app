package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 解答实体
 * @author:  sky
 */
public class AnswerModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2916660228112015466L;
	private String a_pic;
	private long answer_id;
	private String avatar;
	private int teach_id;
	private String schools;
	private String teach_name;
	private int praisecnt;
	private int a_state;
	private int roleid;
	
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getA_state() {
		return a_state;
	}
	public void setA_state(int a_state) {
		this.a_state = a_state;
	}
	public int getPraisecnt() {
		return praisecnt;
	}
	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}
	public String getSchools() {
		return schools;
	}
	public void setSchools(String schools) {
		this.schools = schools;
	}
	public String getTeach_name() {
		return teach_name;
	}
	public void setTeach_name(String teach_name) {
		this.teach_name = teach_name;
	}
	public String getA_pic() {
		return a_pic;
	}
	public void setA_pic(String a_pic) {
		this.a_pic = a_pic;
	}
	public long getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(long answer_id) {
		this.answer_id = answer_id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getTeach_id() {
		return teach_id;
	}
	public void setTeach_id(int teach_id) {
		this.teach_id = teach_id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a_pic == null) ? 0 : a_pic.hashCode());
		result = prime * result + a_state;
		result = prime * result + (int) (answer_id ^ (answer_id >>> 32));
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + praisecnt;
		result = prime * result + roleid;
		result = prime * result + ((schools == null) ? 0 : schools.hashCode());
		result = prime * result + teach_id;
		result = prime * result
				+ ((teach_name == null) ? 0 : teach_name.hashCode());
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
		AnswerModel other = (AnswerModel) obj;
		if (a_pic == null) {
			if (other.a_pic != null)
				return false;
		} else if (!a_pic.equals(other.a_pic))
			return false;
		if (a_state != other.a_state)
			return false;
		if (answer_id != other.answer_id)
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
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
		if (teach_id != other.teach_id)
			return false;
		if (teach_name == null) {
			if (other.teach_name != null)
				return false;
		} else if (!teach_name.equals(other.teach_name))
			return false;
		return true;
	}
}
