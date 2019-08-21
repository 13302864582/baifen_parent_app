package com.daxiong.fun.function.account.model;

import java.io.Serializable;

/**
 * 订单实体
 *
 * @author Administrator
 */
public class MyOrderModel implements Serializable {

    private static final long serialVersionUID = 1053927499434334011L;

    private int buy_category;//购买类别: 1-辅导券 2-VIP
    private float changed_price;        //修改后价格
    private int coupon_count;    //	辅导券数量（用在辅导券类型）
    private int coupon_type;    //辅导券类型:1-难题答疑2-作业检查
    private String datatime; //下订时间
    private String description;    //购买物品的描述
    private String orderid;//订单号
    private float original_price;        //原价格
    private int server_type;    //服务类型购买使用
    private int state;//订单的状态0就付

    private String description_2;    //购买物品的描述，用在支付界面
    private String server_start_time;        //服务开始时间
    private String server_end_time;        //服务结束时间


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBuy_category() {
        return buy_category;
    }

    public void setBuy_category(int buy_category) {
        this.buy_category = buy_category;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public int getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(int coupon_count) {
        this.coupon_count = coupon_count;
    }

    public float getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(float original_price) {
        this.original_price = original_price;
    }

    public float getChanged_price() {
        return changed_price;
    }

    public void setChanged_price(float changed_price) {
        this.changed_price = changed_price;
    }

    public int getServer_type() {
        return server_type;
    }

    public void setServer_type(int server_type) {
        this.server_type = server_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_2() {
        return description_2;
    }

    public void setDescription_2(String description_2) {
        this.description_2 = description_2;
    }

    public String getServer_start_time() {
        return server_start_time;
    }

    public void setServer_start_time(String server_start_time) {
        this.server_start_time = server_start_time;
    }

    public String getServer_end_time() {
        return server_end_time;
    }

    public void setServer_end_time(String server_end_time) {
        this.server_end_time = server_end_time;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }
}
