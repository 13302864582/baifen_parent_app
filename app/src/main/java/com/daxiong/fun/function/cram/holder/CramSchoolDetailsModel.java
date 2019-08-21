package com.daxiong.fun.function.cram.holder;

import java.io.Serializable;

public class CramSchoolDetailsModel implements Serializable{

	/*
	"address": 机构地址,                        
    "info": 机构介绍, 
    "logo": 机构logo地址,                      
    "orgid": 机构代号,                 
    "orgname": 机构名称,             
    "tel": 联系电话
	 */
	
	private static final long serialVersionUID = -914966170169129558L;
	
	private String address;
	private String info;
	private String logo;
	private int orgid;
	private String orgname;
	private String tel;
	public CramSchoolDetailsModel() {
		super();
	}
	public CramSchoolDetailsModel(String address, String info, String logo, int orgid, String orgname, String tel) {
		super();
		this.address = address;
		this.info = info;
		this.logo = logo;
		this.orgid = orgid;
		this.orgname = orgname;
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getOrgid() {
		return orgid;
	}
	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
