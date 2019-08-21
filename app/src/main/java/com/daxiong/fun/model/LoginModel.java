package com.daxiong.fun.model;

import java.io.Serializable;

public class LoginModel implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int os = 1;// 1(android)/2(ios)
	protected String source = "APP";// WEB/APP, #WEB(网站登录 web)
	protected String phonemodel = android.os.Build.MODEL;// android/ios/web
	//protected String phoneos = "android";// android/ios/web
	protected String phoneos=android.os.Build.VERSION.RELEASE;
	private String province;// 省
	private String city;// 城市

	private double latitude;//纬度
	private double longitude;//经度
	private String location;//定位地址
	
	
	

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPhonemodel() {
		return phonemodel;
	}

	public void setPhonemodel(String phonemodel) {
		this.phonemodel = phonemodel;
	}

	public String getPhoneos() {
		return phoneos;
	}

	public void setPhoneos(String phoneos) {
		this.phoneos = phoneos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
