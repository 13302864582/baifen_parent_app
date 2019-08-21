package com.daxiong.fun.function.homework;

import java.io.Serializable;

public class SingleHwAnalysisModel implements Serializable{
    
    
    private static final long serialVersionUID = 3323314182569695658L;
    private  String avatar;//答题老师头像URL
    private  int  grabuserid;//抢题老师id
    private  String datatime;//时间
    private int day;//当月日期
    private int id;//作业id
    private String kpoint;//知识点
    private int pointcnt;//总批改打点数
    private String remark_snd_url;//语音评论url
    private String remark_txt="";//文字评语
    private int rpointcnt;//正确批改打点数
    private double rate;
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public int getGrabuserid() {
        return grabuserid;
    }
    public void setGrabuserid(int grabuserid) {
        this.grabuserid = grabuserid;
    }
    public String getDatatime() {
        return datatime;
    }
    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getKpoint() {
        return kpoint;
    }
    public void setKpoint(String kpoint) {
        this.kpoint = kpoint;
    }
    public int getPointcnt() {
        return pointcnt;
    }
    public void setPointcnt(int pointcnt) {
        this.pointcnt = pointcnt;
    }
    public String getRemark_snd_url() {
        return remark_snd_url;
    }
    public void setRemark_snd_url(String remark_snd_url) {
        this.remark_snd_url = remark_snd_url;
    }
    public String getRemark_txt() {
        return remark_txt;
    }
    public void setRemark_txt(String remark_txt) {
        this.remark_txt = remark_txt;
    }
    public int getRpointcnt() {
        return rpointcnt;
    }
    public void setRpointcnt(int rpointcnt) {
        this.rpointcnt = rpointcnt;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    
    
    

}
