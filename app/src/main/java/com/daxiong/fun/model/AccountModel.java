package com.daxiong.fun.model;

public class AccountModel {

	private long userid;
	private String name;
	private String grade;
	private String subjects;
	private int role;
	private int isuse;
	private String avatar_100;
	private String tokenid;
	private int supervip;
	
	public int getSupervip() {
		return supervip;
	}
	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}
	public String getTokenid() {
		return tokenid;
	}
	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubjects() {
		return subjects;
	}
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getIsuse() {
		return isuse;
	}
	public void setIsuse(int isuse) {
		this.isuse = isuse;
	}
	public String getAvatar_100() {
		return avatar_100;
	}
	public void setAvatar_100(String avatar_100) {
		this.avatar_100 = avatar_100;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((avatar_100 == null) ? 0 : avatar_100.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + isuse;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + role;
		result = prime * result
				+ ((subjects == null) ? 0 : subjects.hashCode());
		result = prime * result + supervip;
		result = prime * result + ((tokenid == null) ? 0 : tokenid.hashCode());
		result = prime * result + (int) (userid ^ (userid >>> 32));
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
		AccountModel other = (AccountModel) obj;
		if (avatar_100 == null) {
			if (other.avatar_100 != null)
				return false;
		} else if (!avatar_100.equals(other.avatar_100))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (isuse != other.isuse)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (role != other.role)
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		if (supervip != other.supervip)
			return false;
		if (tokenid == null) {
			if (other.tokenid != null)
				return false;
		} else if (!tokenid.equals(other.tokenid))
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AccountModel [userid=" + userid + ", name=" + name + ", grade="
				+ grade + ", subjects=" + subjects + ", role=" + role
				+ ", isuse=" + isuse + ", avatar_100=" + avatar_100
				+ ", tokenid=" + tokenid + ", supervip=" + supervip + "]";
	}
	
}
