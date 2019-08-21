package com.daxiong.fun.function.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeWorkModel implements Serializable {
	public static final String TAG = HomeWorkModel.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	
	/** 作业ID */
	private int taskid;
	
	/** 任务类型, # 1问答 2作业 */
	private int tasktype;
	
	/** pay类型, # 1 悬赏 2 包月 3 自建 */
	private int paytype;
	
	/** 文字描述 */
	private String memo;
	
	/** 作业状态 */
	private int state;
	
	/** 发布时间 */
	private long datatime;
	
	/** 回答时间 */
	private long answertime;
	
	/** 确认时间 */
	private long confirmtime;
	
	/** 限制时间 分钟 */
	private int limittime;
	
	/** 抢题时间  */
	private long grabtime;
	/** 发题者ID */
	private int studid;
	
	/** 发题者姓名 */
	private String studname;
	
	/** 发题者头像url */
	private String avatar;
	
	/** 发题者年级 */
	private String grade;
	
	/** 发题者年级ID */
	private int gradeid;
	
	/** 科目 */
	private String subject;
	
	/** 科目ID */
	private int subjectid;
	
	/** 悬赏金额 */
	private float bounty;
	
	/** 系统补助, # 注意最后给到老师的金额为 bounty+givegold */
	private float givegold;
	
	/** 老师ID */
	private int grabuserid;
	
	/** 是否为新人作业, # 0 1 */
	private int isnew;
	
	/** 是否vip用户, # 0非vip  大于0为VIP等级 */
	private int supervip;
	
	/** 是否抢过, # 0没有  非0抢过 */
	private int grabed;

	/** 确认人ID, # 用户 或者 系统 */
	private int confirmuserid;
	
	/** 作业是否被赞过 */
	private int praise;
	
	/** 点赞次数 */
	private int praisecnt;
	
	/** 作业星级*/
	private int satisfaction;
	
	/** 提问用户信用 */
	private int credit;
	
	/** 发布作业次数 */
	private int homeworkcnt;
	
	/** 老师姓名 */
	private String teachername;
	
	/** 老师头像url */
	private String teacheravatar;
	
	/** 老师学校 */
	private String teacherschool;
	
	/** 老师解答次数 */
	private int teacherhomeworkcnt;
	
	/** 作业页列表 */
	private ArrayList<StuPublishHomeWorkPageModel> pagelist;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (answertime ^ (answertime >>> 32));
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + Float.floatToIntBits(bounty);
		result = prime * result + (int) (confirmtime ^ (confirmtime >>> 32));
		result = prime * result + confirmuserid;
		result = prime * result + credit;
		result = prime * result + (int) (datatime ^ (datatime >>> 32));
		result = prime * result + Float.floatToIntBits(givegold);
		result = prime * result + grabed;
		result = prime * result + (int) (grabtime ^ (grabtime >>> 32));
		result = prime * result + grabuserid;
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + gradeid;
		result = prime * result + homeworkcnt;
		result = prime * result + isnew;
		result = prime * result + limittime;
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result + ((pagelist == null) ? 0 : pagelist.hashCode());
		result = prime * result + paytype;
		result = prime * result + praise;
		result = prime * result + praisecnt;
		result = prime * result + satisfaction;
		result = prime * result + state;
		result = prime * result + studid;
		result = prime * result + ((studname == null) ? 0 : studname.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + subjectid;
		result = prime * result + supervip;
		result = prime * result + taskid;
		result = prime * result + tasktype;
		result = prime * result + ((teacheravatar == null) ? 0 : teacheravatar.hashCode());
		result = prime * result + teacherhomeworkcnt;
		result = prime * result + ((teachername == null) ? 0 : teachername.hashCode());
		result = prime * result + ((teacherschool == null) ? 0 : teacherschool.hashCode());
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
		HomeWorkModel other = (HomeWorkModel) obj;
		if (answertime != other.answertime)
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (Float.floatToIntBits(bounty) != Float.floatToIntBits(other.bounty))
			return false;
		if (confirmtime != other.confirmtime)
			return false;
		if (confirmuserid != other.confirmuserid)
			return false;
		if (credit != other.credit)
			return false;
		if (datatime != other.datatime)
			return false;
		if (Float.floatToIntBits(givegold) != Float.floatToIntBits(other.givegold))
			return false;
		if (grabed != other.grabed)
			return false;
		if (grabtime != other.grabtime)
			return false;
		if (grabuserid != other.grabuserid)
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (gradeid != other.gradeid)
			return false;
		if (homeworkcnt != other.homeworkcnt)
			return false;
		if (isnew != other.isnew)
			return false;
		if (limittime != other.limittime)
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (pagelist == null) {
			if (other.pagelist != null)
				return false;
		} else if (!pagelist.equals(other.pagelist))
			return false;
		if (paytype != other.paytype)
			return false;
		if (praise != other.praise)
			return false;
		if (praisecnt != other.praisecnt)
			return false;
		if (satisfaction != other.satisfaction)
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
		if (subjectid != other.subjectid)
			return false;
		if (supervip != other.supervip)
			return false;
		if (taskid != other.taskid)
			return false;
		if (tasktype != other.tasktype)
			return false;
		if (teacheravatar == null) {
			if (other.teacheravatar != null)
				return false;
		} else if (!teacheravatar.equals(other.teacheravatar))
			return false;
		if (teacherhomeworkcnt != other.teacherhomeworkcnt)
			return false;
		if (teachername == null) {
			if (other.teachername != null)
				return false;
		} else if (!teachername.equals(other.teachername))
			return false;
		if (teacherschool == null) {
			if (other.teacherschool != null)
				return false;
		} else if (!teacherschool.equals(other.teacherschool))
			return false;
		return true;
	}

	public String getTeacherschool() {
		return teacherschool;
	}

	public void setTeacherschool(String teacherschool) {
		this.teacherschool = teacherschool;
	}

	public int getHomeworkcnt() {
		return homeworkcnt;
	}

	public void setHomeworkcnt(int homeworkcnt) {
		this.homeworkcnt = homeworkcnt;
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

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public int getTasktype() {
		return tasktype;
	}

	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public long getAnswertime() {
		return answertime;
	}

	public void setAnswertime(long answertime) {
		this.answertime = answertime;
	}

	public long getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(long confirmtime) {
		this.confirmtime = confirmtime;
	}

	public int getLimittime() {
		return limittime;
	}

	public void setLimittime(int limittime) {
		this.limittime = limittime;
	}

	public long getGrabtime() {
		return grabtime;
	}

	public void setGrabtime(long grabtime) {
		this.grabtime = grabtime;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeid() {
		return gradeid;
	}

	public void setGradeid(int gradeid) {
		this.gradeid = gradeid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public float getBounty() {
		return bounty;
	}

	public void setBounty(float bounty) {
		this.bounty = bounty;
	}

	public float getGivegold() {
		return givegold;
	}

	public void setGivegold(float givegold) {
		this.givegold = givegold;
	}

	public int getGrabuserid() {
		return grabuserid;
	}

	public void setGrabuserid(int grabuserid) {
		this.grabuserid = grabuserid;
	}

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	public int getSupervip() {
		return supervip;
	}

	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}

	public int getGrabed() {
		return grabed;
	}

	public void setGrabed(int grabed) {
		this.grabed = grabed;
	}

	public int getConfirmuserid() {
		return confirmuserid;
	}

	public void setConfirmuserid(int confirmuserid) {
		this.confirmuserid = confirmuserid;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public int getPraisecnt() {
		return praisecnt;
	}

	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}

	public ArrayList<StuPublishHomeWorkPageModel> getPagelist() {
		return pagelist;
	}

	public void setPagelist(ArrayList<StuPublishHomeWorkPageModel> pagelist) {
		this.pagelist = pagelist;
	}

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HomeWorkModel [taskid=" + taskid + ", tasktype=" + tasktype + ", paytype=" + paytype + ", memo=" + memo
				+ ", state=" + state + ", datatime=" + datatime + ", answertime=" + answertime + ", confirmtime="
				+ confirmtime + ", limittime=" + limittime + ", grabtime=" + grabtime + ", studid=" + studid
				+ ", studname=" + studname + ", avatar=" + avatar + ", grade=" + grade + ", gradeid=" + gradeid
				+ ", subject=" + subject + ", subjectid=" + subjectid + ", bounty=" + bounty + ", givegold=" + givegold
				+ ", grabuserid=" + grabuserid + ", isnew=" + isnew + ", supervip=" + supervip + ", grabed=" + grabed
				+ ", confirmuserid=" + confirmuserid + ", praise=" + praise + ", praisecnt=" + praisecnt
				+ ", satisfaction=" + satisfaction + ", credit=" + credit + ", homeworkcnt=" + homeworkcnt
				+ ", teachername=" + teachername + ", teacheravatar=" + teacheravatar + ", teacherschool="
				+ teacherschool + ", teacherhomeworkcnt=" + teacherhomeworkcnt + ", pagelist=" + pagelist + "]";
	}




}
