package com.daxiong.fun.model;

import java.io.Serializable;

public class QQUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nick_name;
	private String vip;
	private String level;
	private String province;
	private String gender;
	private String figureurl_qq_40;
	private String figureurl_qq_100;

	public QQUserInfo() {

	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFigureurl_qq_40() {
		return figureurl_qq_40;
	}

	public void setFigureurl_qq_40(String figureurl_qq_40) {
		this.figureurl_qq_40 = figureurl_qq_40;
	}

	public String getFigureurl_qq_100() {
		return figureurl_qq_100;
	}

	public void setFigureurl_qq_100(String figureurl_qq_100) {
		this.figureurl_qq_100 = figureurl_qq_100;
	}
}
