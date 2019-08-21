package com.daxiong.fun.function.cram.holder;

import java.io.Serializable;

public class EduModel implements Serializable{
	
 /*
  "logo": "",                    // 机构logo
  "orgid": 10001,                // 机构ID
  "orgname": "小莫补习班"        // 机构名称
  "relationtype":1               // 1关注 2成员
  */

	private static final long serialVersionUID = -914966170169129558L;
	
	public static final int TYPE_VIP = 2;
	public static final int TYPE_NORMAL = 1;
	
	private String logo;
	private int orgid;
	private String orgname;
	private int relationtype;
	
	public EduModel() {
		super();
	}

	public EduModel(String logo, int orgid, String orgname, int relationtype) {
		super();
		this.logo = logo;
		this.orgid = orgid;
		this.orgname = orgname;
		this.relationtype = relationtype;
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

	public int getRelationtype() {
		return relationtype;
	}

	public void setRelationtype(int relationtype) {
		this.relationtype = relationtype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
