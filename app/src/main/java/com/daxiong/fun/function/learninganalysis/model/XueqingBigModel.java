package com.daxiong.fun.function.learninganalysis.model;

import java.io.Serializable;
import java.util.List;

/**
 * 学情big model
 */

public class XueqingBigModel implements Serializable {


    //是否新用户 0否 1是
    private int isnew;
    //家长注册时间 unixtime 单位s
    private long regtime;
    //班主任userid
    private int htid;
    //班主任名字
    private String name;
    //班主任头像
    private String avatar;
    //学情主页背景颜色  1绿 2蓝 3橙 4红
    private int color;
    //评价字段
    private String level;
    //评价内容
    private String comment;
    //错题分布数
    private int wrongcnt;
    //正确率
    private double rate;
    //学情报告数
    private int reportcnt;

    //错题详情列表
    private List<WrongCalendarBean> wrongCalendarList;
    private List<RatedetailBean> ratedetail;
    private List<ReportDetailBen> reportdetail;

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    public long getRegtime() {
        return regtime;
    }

    public void setRegtime(long regtime) {
        this.regtime = regtime;
    }

    public int getHtid() {
        return htid;
    }

    public void setHtid(int htid) {
        this.htid = htid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getWrongcnt() {
        return wrongcnt;
    }

    public void setWrongcnt(int wrongcnt) {
        this.wrongcnt = wrongcnt;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getReportcnt() {
        return reportcnt;
    }

    public void setReportcnt(int reportcnt) {
        this.reportcnt = reportcnt;
    }

    public List<WrongCalendarBean> getList() {
        return wrongCalendarList;
    }

    public void setList(List<WrongCalendarBean> wrongCalendarList) {
        this.wrongCalendarList = wrongCalendarList;
    }

    public List<RatedetailBean> getRatedetail() {
        return ratedetail;
    }

    public void setRatedetail(List<RatedetailBean> ratedetail) {
        this.ratedetail = ratedetail;
    }

    public List<ReportDetailBen> getReportdetail() {
        return reportdetail;
    }

    public void setReportdetail(List<ReportDetailBen> reportdetail) {
        this.reportdetail = reportdetail;
    }

    /**
     * 错题分布modle
     */
    public static class WrongCalendarBean {
        //当天老师评价
        private String cm;
        //每日错题数量   -1:当日未发布  -2:当日未来临
        private int wcnt;

        public String getCm() {
            return cm;
        }

        public void setCm(String cm) {
            this.cm = cm;
        }

        public int getWcnt() {
            return wcnt;
        }

        public void setWcnt(int wcnt) {
            this.wcnt = wcnt;
        }



        /**************************************************************/


        private String dataStr = "";

        private String emptyStr = "";

        private int monthTag;

        private int yearTag;

        private int isRight = -1;

        private int isFlagg = -1;





        public String getDataStr() {
            return dataStr;
        }

        public void setDataStr(String dataStr) {
            this.dataStr = dataStr;
        }

        public String getEmptyStr() {
            return emptyStr;
        }

        public void setEmptyStr(String emptyStr) {
            this.emptyStr = emptyStr;
        }

        public int getMonthTag() {
            return monthTag;
        }

        public void setMonthTag(int monthTag) {
            this.monthTag = monthTag;
        }

        public int getYearTag() {
            return yearTag;
        }

        public void setYearTag(int yearTag) {
            this.yearTag = yearTag;
        }



        public int getIsRight() {
            return isRight;
        }

        public void setIsRight(int isRight) {
            this.isRight = isRight;
        }

        public int getIsFlagg() {
            return isFlagg;
        }

        public void setIsFlagg(int isFlagg) {
            this.isFlagg = isFlagg;
        }



    }

    /**
     * 正确率modle
     */
    public static class RatedetailBean {
        //总数
        private int cnt;
        //正确数
        private int rcnt;
        //科目
        private String subject;

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getRcnt() {
            return rcnt;
        }

        public void setRcnt(int rcnt) {
            this.rcnt = rcnt;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }


    /**
     * 学情报告modle
     */
    public static class ReportDetailBen {
        //学情报告时间
        private long time;
        //学情信息
        private String content;
        //是否已读 0否 1是
        private int isread;
        //学情报告url
        private String url;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsread() {
            return isread;
        }

        public void setIsread(int isread) {
            this.isread = isread;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
