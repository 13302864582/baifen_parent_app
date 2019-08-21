package com.daxiong.fun.model;

public class QuestionModelGson {

	private String description;
	private String grade;
	private String name;
	private float credit;
	private int state;
	private String avatar;
	private float bounty;
	private String sndurl;//语音url
	private int question_id;
	private String imgurl;
	private String subject;
	private long answer_id;
	private int roleid;
	private int student_id;
	private int supervip;
	private boolean isNewUser;
	private String grabtime;
	private int limit_time;
	
	
	
	public String getGrabtime() {
		return grabtime;
	}
	public void setGrabtime(String grabtime) {
		this.grabtime = grabtime;
	}
	public int getLimit_time() {
		return limit_time;
	}
	public void setLimit_time(int limit_time) {
		this.limit_time = limit_time;
	}
	public int getSupervip() {
		return supervip;
	}
	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public long getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(long answer_id) {
		this.answer_id = answer_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCredit() {
		return credit;
	}
	public void setCredit(float credit) {
		this.credit = credit;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public float getBounty() {
		return bounty;
	}
	public void setBounty(float bounty) {
		this.bounty = bounty;
	}
	public String getSndurl() {
		return sndurl;
	}
	public void setSndurl(String sndurl) {
		this.sndurl = sndurl;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public boolean isNewUser() {
		return isNewUser;
	}
	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (answer_id ^ (answer_id >>> 32));
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + Float.floatToIntBits(bounty);
		result = prime * result + Float.floatToIntBits(credit);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((grabtime == null) ? 0 : grabtime.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((imgurl == null) ? 0 : imgurl.hashCode());
		result = prime * result + (isNewUser ? 1231 : 1237);
		result = prime * result + limit_time;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + question_id;
		result = prime * result + roleid;
		result = prime * result + ((sndurl == null) ? 0 : sndurl.hashCode());
		result = prime * result + state;
		result = prime * result + student_id;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + supervip;
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
		QuestionModelGson other = (QuestionModelGson) obj;
		if (answer_id != other.answer_id)
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (Float.floatToIntBits(bounty) != Float.floatToIntBits(other.bounty))
			return false;
		if (Float.floatToIntBits(credit) != Float.floatToIntBits(other.credit))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (grabtime == null) {
			if (other.grabtime != null)
				return false;
		} else if (!grabtime.equals(other.grabtime))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (imgurl == null) {
			if (other.imgurl != null)
				return false;
		} else if (!imgurl.equals(other.imgurl))
			return false;
		if (isNewUser != other.isNewUser)
			return false;
		if (limit_time != other.limit_time)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (question_id != other.question_id)
			return false;
		if (roleid != other.roleid)
			return false;
		if (sndurl == null) {
			if (other.sndurl != null)
				return false;
		} else if (!sndurl.equals(other.sndurl))
			return false;
		if (state != other.state)
			return false;
		if (student_id != other.student_id)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (supervip != other.supervip)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QuestionModelGson [description=" + description + ", grade="
				+ grade + ", name=" + name + ", credit=" + credit + ", state="
				+ state + ", avatar=" + avatar + ", bounty=" + bounty
				+ ", sndurl=" + sndurl + ", question_id=" + question_id
				+ ", imgurl=" + imgurl + ", subject=" + subject
				+ ", answer_id=" + answer_id + ", roleid=" + roleid
				+ ", student_id=" + student_id + ", supervip=" + supervip
				+ ", isNewUser=" + isNewUser + ", grabtime=" + grabtime
				+ ", limit_time=" + limit_time + "]";
	}
	
}
