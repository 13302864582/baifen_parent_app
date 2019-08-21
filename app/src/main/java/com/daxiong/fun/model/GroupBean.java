package com.daxiong.fun.model;

import java.io.Serializable;
import java.util.ArrayList;


public class GroupBean implements Serializable {
	private static final long serialVersionUID = 1L;	
    private int groupId;	
    private String GroupName;	
    private ArrayList<ChildBean> childList;


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public String getGroupName() {
		return GroupName;
	}


	public void setGroupName(String groupName) {
		GroupName = groupName;
	}


	public ArrayList<ChildBean> getChildList() {
		if (childList==null) {
			childList=new ArrayList<ChildBean>();
		}
		return childList;
	}


	public void setChildList(ArrayList<ChildBean> childList) {
		this.childList = childList;
	}

	
}
