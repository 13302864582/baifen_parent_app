package com.daxiong.fun.model;

import java.io.Serializable;

public class ExplainfeedbackreasonsModel implements Serializable {


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String content;
	
	private int type;
	

	

}
