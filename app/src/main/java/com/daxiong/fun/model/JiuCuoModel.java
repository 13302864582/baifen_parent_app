package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 纠错实体
 * @author:  sky
 */
public class JiuCuoModel implements Serializable {   
   
        
    private static final long serialVersionUID = -7652007199763343957L;
    private int userid;
    private String name;
    private String avatar_100;
    private String content;
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAvatar_100() {
        return avatar_100;
    }
    public void setAvatar_100(String avatar_100) {
        this.avatar_100 = avatar_100;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    
    

}
