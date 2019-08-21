package com.daxiong.fun.function.account.model;

import java.io.Serializable;

/**
 * 购买详情model.
 */

public class BuyDetailModel implements Serializable {

    private String  content;
    private  String start_time;
    private  String end_time;
    private  float price;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
