package com.daxiong.fun.function.account.model;

import java.io.Serializable;

/**
 * Created by Sky on 2016/7/7 0007.
 */

public class PayCardModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String product;

    private float pay;

    private String remark;

    private int defResId;

    private String content;// vip内容(列表界面用)

    private String content_2;// vip内容(支付界面用)

    private float money;// 购买化话费的价钱(单位元)

    private String server_time;// 服务时间

    private int type;// VIP服务类型(支付的时候需要发给服务器)

    private String  apple_pay_id;

    private String detail_content;

    private float preferential_price;

//    private int groupId;
//    private String GroupName;
//    private ArrayList<VipChildBean> childList;










    public float getPreferential_price() {
        return preferential_price;
    }

    public void setPreferential_price(float preferential_price) {
        this.preferential_price = preferential_price;
    }

   /* public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public ArrayList<VipChildBean> getChildList() {
        if (childList==null) {
            childList=new ArrayList<VipChildBean>();
        }
        return childList;
    }

    public void setChildList(ArrayList<VipChildBean> childList) {
        this.childList = childList;
    }*/

    public String getDetail_content() {
        return detail_content;
    }

    public void setDetail_content(String detail_content) {
        this.detail_content = detail_content;
    }

    public String getApple_pay_id() {
        return apple_pay_id;
    }

    public void setApple_pay_id(String apple_pay_id) {
        this.apple_pay_id = apple_pay_id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDefResId() {
        return defResId;
    }

    public void setDefResId(int defResId) {
        this.defResId = defResId;
    }

}
