package com.daxiong.fun.function.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * 新版本vip model
 */

public class VipModel implements Serializable {

    private static final long serialVersionUID = 5346983464197020350L;


    private VipStatusInfosBean vip_status_infos;

    private List<BuyVipInfosBean> buy_vip_infos;

    public VipStatusInfosBean getVip_status_infos() {
        return vip_status_infos;
    }

    public void setVip_status_infos(VipStatusInfosBean vip_status_infos) {
        this.vip_status_infos = vip_status_infos;
    }

    public List<BuyVipInfosBean> getBuy_vip_infos() {
        return buy_vip_infos;
    }

    public void setBuy_vip_infos(List<BuyVipInfosBean> buy_vip_infos) {
        this.buy_vip_infos = buy_vip_infos;
    }



    public static class VipStatusInfosBean {
        private int homework_coupon_count;
        private int question_coupon_count;
        private int type;
        private int vip_left_time;
        //所属辅导班id
        private int orgid;

        public int getOrgid() {
            return orgid;
        }

        public void setOrgid(int orgid) {
            this.orgid = orgid;
        }

        public int getHomework_coupon_count() {
            return homework_coupon_count;
        }

        public void setHomework_coupon_count(int homework_coupon_count) {
            this.homework_coupon_count = homework_coupon_count;
        }

        public int getQuestion_coupon_count() {
            return question_coupon_count;
        }

        public void setQuestion_coupon_count(int question_coupon_count) {
            this.question_coupon_count = question_coupon_count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVip_left_time() {
            return vip_left_time;
        }

        public void setVip_left_time(int vip_left_time) {
            this.vip_left_time = vip_left_time;
        }
    }

    public static class BuyVipInfosBean implements Serializable{
        private int buy_category;//	购买类别: 1-辅导券 2-VIP
        private int buy_type;//	购买类型
        private String content;//内容(列表界面用)
        private String content_2;//内容(用在微信和支付宝购买界面)
        private String detail_content;//vip内容详情json，辅导券以{{}}
        private int is_preferential;//是否有优惠: 0 -不是 1-是
        private int is_recommend;//是否为推荐: 0 -不是 1-是
        private float money;//价钱(单位元),最多两个小数点
        private String server_time;//	服务时间
        private int packageid;//套餐id


        public int getPackageid() {
            return packageid;
        }

        public void setPackageid(int packageid) {
            this.packageid = packageid;
        }

        public int getBuy_category() {
            return buy_category;
        }

        public void setBuy_category(int buy_category) {
            this.buy_category = buy_category;
        }

        public int getBuy_type() {
            return buy_type;
        }

        public void setBuy_type(int buy_type) {
            this.buy_type = buy_type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_2() {
            return content_2;
        }

        public void setContent_2(String content_2) {
            this.content_2 = content_2;
        }

        public String getDetail_content() {
            return detail_content;
        }

        public void setDetail_content(String detail_content) {
            this.detail_content = detail_content;
        }

        public int getIs_preferential() {
            return is_preferential;
        }

        public void setIs_preferential(int is_preferential) {
            this.is_preferential = is_preferential;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }

        public String getServer_time() {
            return server_time;
        }

        public void setServer_time(String server_time) {
            this.server_time = server_time;
        }
    }

}
