package com.daxiong.fun.model;

public class FilterKnowledge {

	private int id;
	private String name;
	
	public int getKnowledgeId() {
		return id;
	}
	
	public void setKnowledgeId(int knowledgeId) {
		this.id = knowledgeId;
	}	
	
	public String getName(){
		return name;
	}
	
	public void setName(String knowledgeName){
		if(name != null){
			name = knowledgeName;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FilterKnowledge other = (FilterKnowledge) obj;
		if(id != other.id)
			return false;
		if(name == null) {
			if(other.name != null)
				return false;
		}else if(!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FilterKnowledge [id=" + id + ", name=" + name + "]";
	}
	
}
