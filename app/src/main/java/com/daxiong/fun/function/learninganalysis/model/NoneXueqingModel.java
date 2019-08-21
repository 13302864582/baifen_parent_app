package com.daxiong.fun.function.learninganalysis.model;

import java.util.List;

/**
 * 没有学情model
 */

public class NoneXueqingModel {


    private int isnew;
    private String remind;

    private List<ReportsBean> reports;

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public List<ReportsBean> getReports() {
        return reports;
    }

    public void setReports(List<ReportsBean> reports) {
        this.reports = reports;
    }

    public static class ReportsBean {
        private String name;
        private String avatar;
        private String url;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
