package com.daxiong.fun.model;

import java.io.Serializable;

public class QQLoginModel extends LoginModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/** iemi */
	private String iemi;
	private String openid;
	/** sole_key",# 唯一认证KEY #1.3新增 */
	private String solekey;
	private QQUserInfo userinfo;

	public String getIemi() {
		return iemi;
	}

	public void setIemi(String iemi) {
		this.iemi = iemi;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSolekey() {
		return solekey;
	}

	public void setSolekey(String solekey) {
		this.solekey = solekey;
	}

	public QQUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(QQUserInfo userinfo) {
		this.userinfo = userinfo;
	}

}
