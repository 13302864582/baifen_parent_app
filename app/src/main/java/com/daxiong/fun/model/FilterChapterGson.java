package com.daxiong.fun.model;

import java.util.List;

public class FilterChapterGson {
	private int chapterid;
	private String chaptername;
	private List<FilterKnowledge> point;
	
	public int getChapterId() {
		return chapterid;
	}
	public void setChapterId(int id) {
		this.chapterid = id;
	}	
	
	public String getChapterName(){
		return chaptername;
	}
	
	public void setChapterName(String name){
		if(name != null){
			chaptername = name;
		}
	}
	
	public List<FilterKnowledge> getPoints() {
		return point;
	}
	
	public void setPoints(List<FilterKnowledge> points) {
		this.point = points;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chapterid;
		result = prime * result + ((chaptername == null) ? 0 : chaptername.hashCode());		
		result = prime * result + ((point == null) ? 0 : point.hashCode());
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
		FilterChapterGson other = (FilterChapterGson) obj;
		if(chapterid != other.chapterid)
			return false;
		if(chaptername == null) {
			if(other.chaptername != null)
				return false;
		}else if(!chaptername.equals(other.chaptername))
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FilterChapterGson [chapterid=" + chapterid + ", chaptername=" + chaptername + ", point=" + point + "]";
	}	
}
