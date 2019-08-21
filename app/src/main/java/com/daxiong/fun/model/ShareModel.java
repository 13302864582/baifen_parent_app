package com.daxiong.fun.model;

import java.io.Serializable;

public class ShareModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shareTitle;
	private String shareDesc;
	private String shareUrl;

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareDesc() {
		return shareDesc;
	}

	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

}
