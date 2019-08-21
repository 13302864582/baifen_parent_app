package com.daxiong.fun.model;

public class ContactSubject {

	private String xiao;
	private String chu;
	private String gao;
	
	public String getXiaoSubs() {
		return xiao;
	}

	public void setXiaoSubs(String xiao) {
		this.xiao = xiao;
	}

	public String getMidLowSubs() {
		return chu;
	}
	
	public void setMidLowSubs(String lowSubs) {
		this.chu = lowSubs;
	}	
	
	public String getMidHighSubs() {
		return gao;
	}
	
	public void setMidHighSubs(String highSubs) {
		this.gao = highSubs;
	}		
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;	
		result = prime * result + ((xiao == null) ? 0 : xiao.hashCode());
		result = prime * result + ((chu == null) ? 0 : chu.hashCode());	
		result = prime * result + ((gao == null) ? 0 : gao.hashCode());
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
		ContactSubject other = (ContactSubject) obj;
		if (chu == null) {
			if (other.chu != null)
				return false;
		} else if (!chu.equals(other.chu))
			return false;		
		
		if(chu == null){
			if (other.chu != null){
				return false;
			}
		} else if (!chu.equals(other.chu))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "contactSubject [xiao= " + xiao + ", chu=" + chu  +  ", gao=" + gao + "]";
	}			
}