package com.daxiong.fun.function.learninganalysis.model;

import java.io.Serializable;

/**
 * 学情分析轮播图
 * @author:  sky
 */
public class LearningPagerImageModel implements Serializable{
    
    private static final long serialVersionUID = 186204694664147039L;
    
    
    private String datetime;
    private String link;
    private String name;
    private String picurl;
    private int location;
    private int sequence;
    public String getDatetime() {
        return datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicurl() {
        return picurl;
    }
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
    public int getLocation() {
        return location;
    }
    public void setLocation(int location) {
        this.location = location;
    }
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    
    
    

}
