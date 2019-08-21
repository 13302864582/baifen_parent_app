package com.daxiong.fun.model;

public class QuestionDetailGson {

	private String description;
	private String source;
	private String grade;
	private int studid;
	private String studname;
	private String q_sndurl;
	private int grabnum;
	private int state;
	private String avatar;
	private float bounty;
	private int praisecnt;
	private int praise;
	private int viewcnt;
	private String q_imgurl;
	private long question_id;
	private String subject;
	private int roleid;
	
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getStudid() {
		return studid;
	}
	public void setStudid(int studid) {
		this.studid = studid;
	}
	public String getStudname() {
		return studname;
	}
	public void setStudname(String studname) {
		this.studname = studname;
	}
	public String getQ_sndurl() {
		return q_sndurl;
	}
	public void setQ_sndurl(String q_sndurl) {
		this.q_sndurl = q_sndurl;
	}
	public int getGrabnum() {
		return grabnum;
	}
	public void setGrabnum(int grabnum) {
		this.grabnum = grabnum;
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
	public int getPraisecnt() {
		return praisecnt;
	}
	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public String getQ_imgurl() {
		return q_imgurl;
	}
	public void setQ_imgurl(String q_imgurl) {
		this.q_imgurl = q_imgurl;
	}
	public long getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Override
	public String toString() {
		return "QuestionDetailGson [description=" + description + ", source="
				+ source + ", grade=" + grade + ", studid=" + studid
				+ ", studname=" + studname + ", q_sndurl=" + q_sndurl
				+ ", grabnum=" + grabnum + ", state=" + state + ", avatar="
				+ avatar + ", bounty=" + bounty + ", praisecnt=" + praisecnt
				+ ", praise=" + praise + ", viewcnt=" + viewcnt + ", q_imgurl="
				+ q_imgurl + ", question_id=" + question_id + ", subject="
				+ subject + ", roleid=" + roleid + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + Float.floatToIntBits(bounty);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + grabnum;
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + praise;
		result = prime * result + praisecnt;
		result = prime * result
				+ ((q_imgurl == null) ? 0 : q_imgurl.hashCode());
		result = prime * result
				+ ((q_sndurl == null) ? 0 : q_sndurl.hashCode());
		result = prime * result + (int) (question_id ^ (question_id >>> 32));
		result = prime * result + roleid;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + state;
		result = prime * result + studid;
		result = prime * result
				+ ((studname == null) ? 0 : studname.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + viewcnt;
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
		QuestionDetailGson other = (QuestionDetailGson) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (Float.floatToIntBits(bounty) != Float.floatToIntBits(other.bounty))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (grabnum != other.grabnum)
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (praise != other.praise)
			return false;
		if (praisecnt != other.praisecnt)
			return false;
		if (q_imgurl == null) {
			if (other.q_imgurl != null)
				return false;
		} else if (!q_imgurl.equals(other.q_imgurl))
			return false;
		if (q_sndurl == null) {
			if (other.q_sndurl != null)
				return false;
		} else if (!q_sndurl.equals(other.q_sndurl))
			return false;
		if (question_id != other.question_id)
			return false;
		if (roleid != other.roleid)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (state != other.state)
			return false;
		if (studid != other.studid)
			return false;
		if (studname == null) {
			if (other.studname != null)
				return false;
		} else if (!studname.equals(other.studname))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (viewcnt != other.viewcnt)
			return false;
		return true;
	}
}
