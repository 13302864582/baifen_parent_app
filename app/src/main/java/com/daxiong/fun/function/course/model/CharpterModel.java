package com.daxiong.fun.function.course.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CharpterModel implements Serializable{
	/*
	 "charpter":[
				{
					"charpterid": 课时id
					"charptername": 课时名称
					"kpoint": 知识点
				}
			]
	 */
	
	private static final long serialVersionUID = -914966170169129558L;
	private int charpterid; //课时id
	private String charptername; //课时名称
	private String kpoint; //知识点
	private ArrayList<CoursePageModel> page;

	@Override
	public String toString() {
		return "CharpterModel [charpterid=" + charpterid + ", charptername=" + charptername + ", kpoint=" + kpoint
				+ ", page=" + page + "]";
	}
	public ArrayList<CoursePageModel> getPage() {
		return page;
	}
	public void setPage(ArrayList<CoursePageModel> page) {
		this.page = page;
	}
	public CharpterModel(int charpterid, String charptername, String kpoint) {
		super();
		this.charpterid = charpterid;
		this.charptername = charptername;
		this.kpoint = kpoint;
	}
	public CharpterModel() {
		super();
	}
	public int getCharpterid() {
		return charpterid;
	}
	public void setCharpterid(int charpterid) {
		this.charpterid = charpterid;
	}
	public String getCharptername() {
		return charptername;
	}
	public void setCharptername(String charptername) {
		this.charptername = charptername;
	}
	public String getKpoint() {
		return kpoint;
	}
	public void setKpoint(String kpoint) {
		this.kpoint = kpoint;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
