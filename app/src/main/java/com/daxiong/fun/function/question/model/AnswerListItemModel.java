package com.daxiong.fun.function.question.model;

import com.daxiong.fun.model.AnswerModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 此类的描述：拍照提问实体 /作业额检查实体
 * @author:  sky
 * @最后修改人： sky
 * @最后修改日期:2015-7-30 下午6:25:38
 */
public class AnswerListItemModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -914966170169129558L;
	public static final String TAG = AnswerListItemModel.class.getSimpleName() ;
	private long qid;
	private int tasktype;// "tasktype": 任务类型, # 1问答 2作业
	private int paytype;//pay类型,  # 1 悬赏  2 包月  3 自建
	private String q_memo;
	private int state;
	private int a_state;//
	private int viewcnt;
	
	private long datatime;
	private long grabtime;
	private long answertime;
	private long confirmtime;
	
	private String studname;
	private int studid;
	private String grade;
	private int gradeid;
	private String subject;
	private int subjectid;
	private float bounty;
	private int newqtn;//是否为新人题, # 0 1
	private int confirmuserid;
	private int praisecnt;//收藏/赞次数
	private int praise;//0是未收藏,1是已经收藏
	private int supervip;//0不是会员
	private String avatar;//学生头像
	private int countamt;//发布问题次数,
	private int grabuserid;
	private String grabuser;
	private String grabavatar;
	private String grabschool;
	private int grabcountamt;
	private String imgpath;//问题图片
	private String answerthumbnail;//答案缩略图地址
	private ArrayList<AnswerModel> answerlist;
	

	@Override
	public String toString() {
		return "AnswerListItemModel [qid=" + qid + ", tasktype=" + tasktype + ", paytype=" + paytype + ", q_memo="
				+ q_memo + ", state=" + state + ", a_state=" + a_state + ", viewcnt=" + viewcnt + ", datatime="
				+ datatime + ", grabtime=" + grabtime + ", answertime=" + answertime + ", confirmtime=" + confirmtime
				+ ", studname=" + studname + ", studid=" + studid + ", grade=" + grade + ", gradeid=" + gradeid
				+ ", subject=" + subject + ", subjectid=" + subjectid + ", bounty=" + bounty + ", newqtn=" + newqtn
				+ ", confirmuserid=" + confirmuserid + ", praisecnt=" + praisecnt + ", praise=" + praise
				+ ", supervip=" + supervip + ", avatar=" + avatar + ", countamt=" + countamt + ", grabuserid="
				+ grabuserid + ", grabuser=" + grabuser + ", grabavatar=" + grabavatar + ", grabschool=" + grabschool
				+ ", grabcountamt=" + grabcountamt + ", imgpath=" + imgpath + ", answerthumbnail=" + answerthumbnail
				+ ", answerlist=" + answerlist + "]";
	}
	public ArrayList<AnswerModel> getAnswerlist() {
		return answerlist;
	}
	public void setAnswerlist(ArrayList<AnswerModel> answerlist) {
		this.answerlist = answerlist;
	}
	public long getQid() {
		return qid;
	}
	public void setQid(long qid) {
		this.qid = qid;
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
	public String getQ_memo() {
		return q_memo;
	}
	public void setQ_memo(String q_memo) {
		this.q_memo = q_memo;
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
	public long getGrabtime() {
		return grabtime;
	}
	public void setGrabtime(long grabtime) {
		this.grabtime = grabtime;
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
	public String getStudname() {
		return studname;
	}
	public void setStudname(String studname) {
		this.studname = studname;
	}
	public int getStudid() {
		return studid;
	}
	public void setStudid(int studid) {
		this.studid = studid;
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
	public int getNewqtn() {
		return newqtn;
	}
	public void setNewqtn(int newqtn) {
		this.newqtn = newqtn;
	}
	public int getConfirmuserid() {
		return confirmuserid;
	}
	public void setConfirmuserid(int confirmuserid) {
		this.confirmuserid = confirmuserid;
	}
	public int getPraisecnt() {
		return praisecnt;
	}
	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}
	public int getSupervip() {
		return supervip;
	}
	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getCountamt() {
		return countamt;
	}
	public void setCountamt(int countamt) {
		this.countamt = countamt;
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
	public String getGrabavatar() {
		return grabavatar;
	}
	public void setGrabavatar(String grabavatar) {
		this.grabavatar = grabavatar;
	}
	public String getGrabschool() {
		return grabschool;
	}
	public void setGrabschool(String grabschool) {
		this.grabschool = grabschool;
	}
	public int getGrabcountamt() {
		return grabcountamt;
	}
	public void setGrabcountamt(int grabcountamt) {
		this.grabcountamt = grabcountamt;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public String getAnswerthumbnail() {
		return answerthumbnail;
	}
	public void setAnswerthumbnail(String answerthumbnail) {
		this.answerthumbnail = answerthumbnail;
	}
	public int getA_state() {
		return a_state;
	}
	public void setA_state(int a_state) {
		this.a_state = a_state;
	}
	
	
}
