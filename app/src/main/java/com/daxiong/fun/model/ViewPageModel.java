package com.daxiong.fun.model;

import java.io.Serializable;

public class ViewPageModel implements Serializable {

	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	private static final long serialVersionUID = 1L;

	private String imageurl;
	private String text;
	private String weburl;
	

	

}
