package com.daxiong.fun.function.homework.model;

import java.io.Serializable;

public class UpLoadEXPointModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1132774187611041170L;
	/** 点顺序 */
	private int exseqid;
	
	/** 点类型, # 1 声音 2 文本 */
	private int explaintype;
	
	/** 点坐标 */
	private String coordinate;
	
	/** 文本内容 */
	private String text;

	public int getExseqid() {
		return exseqid;
	}

	public void setExseqid(int exseqid) {
		this.exseqid = exseqid;
	}

	public int getExplaintype() {
		return explaintype;
	}

	public void setExplaintype(int explaintype) {
		this.explaintype = explaintype;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "UpLoadEXPointModel [exseqid=" + exseqid + ", explaintype=" + explaintype + ", coordinate=" + coordinate
				+ ", text=" + text + "]";
	}

	
}
