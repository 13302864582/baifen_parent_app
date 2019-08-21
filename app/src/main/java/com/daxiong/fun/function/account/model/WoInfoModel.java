package com.daxiong.fun.function.account.model;

import java.io.Serializable;

/**
 * Created by Sky on 2016/6/29 0029.
 */

public class WoInfoModel implements Serializable {

    private int coupon_count;//辅导券个数
    private String expired_coupon_infos;//过期辅导券信息
    private HeadteacherInfosBean headteacher_infos;//老师信息, json数据
    private int homework_count;//	作业个数
    private MyInfosBean my_infos;//我自己的信息, json数据
    private int question_count;//难题答疑个数
    private int unpay_order_count;//未支付订单个数

    public int getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(int coupon_count) {
        this.coupon_count = coupon_count;
    }

    public String getExpired_coupon_infos() {
        return expired_coupon_infos;
    }

    public void setExpired_coupon_infos(String expired_coupon_infos) {
        this.expired_coupon_infos = expired_coupon_infos;
    }

    public HeadteacherInfosBean getHeadteacher_infos() {
        return headteacher_infos;
    }

    public void setHeadteacher_infos(HeadteacherInfosBean headteacher_infos) {
        this.headteacher_infos = headteacher_infos;
    }

    public int getHomework_count() {
        return homework_count;
    }

    public void setHomework_count(int homework_count) {
        this.homework_count = homework_count;
    }

    public MyInfosBean getMy_infos() {
        return my_infos;
    }

    public void setMy_infos(MyInfosBean my_infos) {
        this.my_infos = my_infos;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public int getUnpay_order_count() {
        return unpay_order_count;
    }

    public void setUnpay_order_count(int unpay_order_count) {
        this.unpay_order_count = unpay_order_count;
    }

    public static class HeadteacherInfosBean {
        private int age;
        private String avatar_100;
        private String name;
        private String remind;
        private String schools;
        private int sex;
        private int userid;
        private String work_descriptions;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAvatar_100() {
            return avatar_100;
        }

        public void setAvatar_100(String avatar_100) {
            this.avatar_100 = avatar_100;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemind() {
            return remind;
        }

        public void setRemind(String remind) {
            this.remind = remind;
        }

        public String getSchools() {
            return schools;
        }

        public void setSchools(String schools) {
            this.schools = schools;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getWork_descriptions() {
            return work_descriptions;
        }

        public void setWork_descriptions(String work_descriptions) {
            this.work_descriptions = work_descriptions;
        }
    }

    public static class MyInfosBean {
        private String avatar_100;
        private String grade;
        private String name;
        private int supervip;//是否为VIP: 0-不是1-是
        private String vip_content;
        private int vip_left_time;
        private int vip_type;

        public String getAvatar_100() {
            return avatar_100;
        }

        public void setAvatar_100(String avatar_100) {
            this.avatar_100 = avatar_100;
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

        public int getSupervip() {
            return supervip;
        }

        public void setSupervip(int supervip) {
            this.supervip = supervip;
        }

        public String getVip_content() {
            return vip_content;
        }

        public void setVip_content(String vip_content) {
            this.vip_content = vip_content;
        }

        public int getVip_left_time() {
            return vip_left_time;
        }

        public void setVip_left_time(int vip_left_time) {
            this.vip_left_time = vip_left_time;
        }

        public int getVip_type() {
            return vip_type;
        }

        public void setVip_type(int vip_type) {
            this.vip_type = vip_type;
        }
    }
}
