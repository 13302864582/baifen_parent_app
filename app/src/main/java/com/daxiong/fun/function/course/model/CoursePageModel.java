package com.daxiong.fun.function.course.model;

import java.io.Serializable;

public class CoursePageModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8583896756956134805L;
	public static final String TAG = CoursePageModel.class.getSimpleName();
	private int pageid;
	private String imgurl;
	private String thumbnail;

	public int getPageid() {
		return pageid;
	}

	public void setPageid(int pageid) {
		this.pageid = pageid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return "CoursePageModel [pageid=" + pageid + ", imgurl=" + imgurl + ", thumbnail=" + thumbnail + "]";
	}

}
