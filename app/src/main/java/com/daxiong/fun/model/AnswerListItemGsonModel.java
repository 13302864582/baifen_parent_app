package com.daxiong.fun.model;

import java.util.List;

/**
 * 解答问题的实体
 * @author:  sky
 */
public class AnswerListItemGsonModel {

	private long questionid;
	private int q_state;
	private String name;
	private List<AnswerModel> answerlist;
	private int student_id;
	private String u_pic;//学生头像
	private float bounty;
	private String q_pic;//问题图片
	private int viewcnt;//看过题目的人数
	private int praisecnt;//赞次数
	private int praise;//0是未收藏,1是已经收藏
	private String datatime;
	private String grabtime;
	private int duration;
	private String grade;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public int getPraisecnt() {
		return praisecnt;
	}
	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}
	public long getQuestionid() {
		return questionid;
	}
	public void setQuestionid(long questionid) {
		this.questionid = questionid;
	}
	public int getQ_state() {
		return q_state;
	}
	public void setQ_state(int q_state) {
		this.q_state = q_state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AnswerModel> getAnswerlist() {
		return answerlist;
	}
	public void setAnswerlist(List<AnswerModel> answerlist) {
		this.answerlist = answerlist;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getU_pic() {
		return u_pic;
	}
	public void setU_pic(String u_pic) {
		this.u_pic = u_pic;
	}
	public float getBounty() {
		return bounty;
	}
	public void setBounty(float bounty) {
		this.bounty = bounty;
	}
	public String getQ_pic() {
		return q_pic;
	}
	public void setQ_pic(String q_pic) {
		this.q_pic = q_pic;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public String getGrabtime() {
		return grabtime;
	}
	public void setGrabtime(String grabtime) {
		this.grabtime = grabtime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerlist == null) ? 0 : answerlist.hashCode());
		result = prime * result + Float.floatToIntBits(bounty);
		result = prime * result + ((datatime == null) ? 0 : datatime.hashCode());
		result = prime * result + duration;
		result = prime * result + ((grabtime == null) ? 0 : grabtime.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + praise;
		result = prime * result + praisecnt;
		result = prime * result + ((q_pic == null) ? 0 : q_pic.hashCode());
		result = prime * result + q_state;
		result = prime * result + (int) (questionid ^ (questionid >>> 32));
		result = prime * result + roleid;
		result = prime * result + student_id;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((u_pic == null) ? 0 : u_pic.hashCode());
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
		AnswerListItemGsonModel other = (AnswerListItemGsonModel) obj;
		if (answerlist == null) {
			if (other.answerlist != null)
				return false;
		} else if (!answerlist.equals(other.answerlist))
			return false;
		if (Float.floatToIntBits(bounty) != Float.floatToIntBits(other.bounty))
			return false;
		if (datatime == null) {
			if (other.datatime != null)
				return false;
		} else if (!datatime.equals(other.datatime))
			return false;
		if (duration != other.duration)
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (praise != other.praise)
			return false;
		if (praisecnt != other.praisecnt)
			return false;
		if (q_pic == null) {
			if (other.q_pic != null)
				return false;
		} else if (!q_pic.equals(other.q_pic))
			return false;
		if (q_state != other.q_state)
			return false;
		if (questionid != other.questionid)
			return false;
		if (roleid != other.roleid)
			return false;
		if (student_id != other.student_id)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (u_pic == null) {
			if (other.u_pic != null)
				return false;
		} else if (!u_pic.equals(other.u_pic))
			return false;
		if (viewcnt != other.viewcnt)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AnswerListItemGson [questionid=" + questionid + ", q_state=" + q_state + ", name=" + name
				+ ", answerlist=" + answerlist + ", student_id=" + student_id + ", u_pic=" + u_pic + ", bounty="
				+ bounty + ", q_pic=" + q_pic + ", viewcnt=" + viewcnt + ", praisecnt=" + praisecnt + ", praise="
				+ praise + ", datatime=" + datatime + ", grabtime=" + grabtime + ", duration=" + duration + ", grade="
				+ grade + ", subject=" + subject + ", roleid=" + roleid + "]";
	}
	
	
}
