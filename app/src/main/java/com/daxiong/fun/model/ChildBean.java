package com.daxiong.fun.model;

import java.io.Serializable;


public class ChildBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int  childId;
	
	public String childName;

	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}
	
	
	
}
