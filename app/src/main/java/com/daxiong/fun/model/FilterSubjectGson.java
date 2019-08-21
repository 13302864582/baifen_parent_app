package com.daxiong.fun.model;

import java.util.List;

public class FilterSubjectGson {
	private int subjectid;
	private String subjectname;
	private List<FilterChapterGson> chapter;
	
	public int getSubjectId() {
		return subjectid;
	}
	public void setSubjectId(int id) {
		this.subjectid = id;
	}	
	
	public String getSubjectName(){
		return subjectname;
	}
	
	public void setSubjectName(String name){
		if(name != null){
			subjectname = name;
		}
	}
	
	public List<FilterChapterGson> getChapters() {
		return chapter;
	}
	
	public void setChapters(List<FilterChapterGson> chapters) {
		this.chapter = chapters;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + subjectid;
		result = prime * result + ((subjectname == null) ? 0 : subjectname.hashCode());		
		result = prime * result + ((chapter == null) ? 0 : chapter.hashCode());
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
		FilterSubjectGson other = (FilterSubjectGson) obj;
		if(subjectid != other.subjectid)
			return false;
		if(subjectname == null) {
			if(other.subjectname != null)
				return false;
		}else if(!subjectname.equals(other.subjectname))
			return false;
		if (chapter == null) {
			if (other.chapter != null)
				return false;
		} else if (!chapter.equals(other.chapter))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FilterSubjectGson [subjectid=" + subjectid + ", subjectname=" + subjectname + ", chapter=" + chapter + "]";
	}	
}
