package com.daxiong.fun.function.learninganalysis.model;

import java.io.Serializable;

/**
 * 学情报告列表model
 * @author:  sky
 */
public class LearningReportModel implements Serializable{    
        
    private static final long serialVersionUID = -6271679263112820419L;
    
    
    private String datatime;//报告时间
    
    private int htid;//班主任ID
    
    private  String title;//报告标题
    
    private String url;//报告H5 URL地址
    
    private String name;
    private String avatar;
    
    private  int isnew;
    
    
    

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

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public int getHtid() {
        return htid;
    }

    public void setHtid(int htid) {
        this.htid = htid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    

   
    
    
    

}
