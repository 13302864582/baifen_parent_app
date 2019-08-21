package com.daxiong.fun.model;


public class SortContactModel {

	private UserInfoModel contact; 
	private String sortLetters;
	private boolean endOfSectionFlag = true;
	
	public UserInfoModel getContactInfo(){
		return contact;
	}
	
	public void setContactInfo(UserInfoModel info){
		if(info != null){
			this.contact = info;
		}
	}
	
	public boolean getEndOfSectionFlag(){
		return endOfSectionFlag;
	}
	
	public void setEndOfSectionFlag(boolean flag){
		this.endOfSectionFlag = flag;
	}
	
	public String getSortLetters() {
		return sortLetters;
	}
	
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
