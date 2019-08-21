package com.daxiong.fun.model;

import java.util.List;

public class FilterInfoGson {
	private int groupid;
	private String groupname;
	private List<FilterSubjectGson> subjects;

	public int getGroupId() {
		return groupid;
	}
	public void setGroupId(int level) {
		this.groupid = level;
	}	
	public String getGroupName(){
		return groupname;
	}
	public void setGroupName(String name){
		if(name != null){
			this.groupname = name;
		}
	}
	public List<FilterSubjectGson> getSubject() {
		return subjects;
	}
	public void setSubject(List<FilterSubjectGson> subs) {
		this.subjects = subs;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupid;
		result = prime * result + ((groupname == null) ? 0 : groupname.hashCode());
		result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterInfoGson other = (FilterInfoGson) obj;
		if(groupid != other.groupid)
			return false;
		if(groupname == null) {
			if(other.groupname != null)
				return false;
		}else if(!groupname.equals(other.groupname))
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FilterInfoGson [groupid=" + groupid + ",groupname=" + groupname + ", subjects=" + subjects + "]";
	}	
}
