
package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 辅导券实体
 * 
 * @author: sky
 */
public class FudaoquanModel implements Serializable {


	private String avatar;

	private String address;

	private int couponleft;

	private int sendcoupon;

	private String orginfourl;


	public void setAvatar(String avatar){
		this.avatar = avatar;
	}
	public String getAvatar(){
		return this.avatar;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public String getAddress(){
		return this.address;
	}
	public void setCouponleft(int couponleft){
		this.couponleft = couponleft;
	}
	public int getCouponleft(){
		return this.couponleft;
	}
	public void setSendcoupon(int sendcoupon){
		this.sendcoupon = sendcoupon;
	}
	public int getSendcoupon(){
		return this.sendcoupon;
	}
	public void setOrginfourl(String orginfourl){
		this.orginfourl = orginfourl;
	}
	public String getOrginfourl(){
		return this.orginfourl;
	}



	public int getCoupon_type() {
		return coupon_type;
	}

	public void setCoupon_type(int coupon_type) {
		this.coupon_type = coupon_type;
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

	private static final long serialVersionUID = -1794154692784777436L;

	private int id;
	/**
	 * 1 用户购买 2 官方的赠送大熊券 3 机构的赠送大熊券 4 机构购买的券
	 */
	private int coupon_type;
	private int orgid;

	private String orgname;
	private String orgurl;
	public String getOrgurl() {
		return orgurl;
	}

	public void setOrgurl(String orgurl) {
		this.orgurl = orgurl;
	}

	private String name;

	private String expireDate;

	private int count;

	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
