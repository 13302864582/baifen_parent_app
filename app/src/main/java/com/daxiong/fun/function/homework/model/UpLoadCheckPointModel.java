package com.daxiong.fun.function.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UpLoadCheckPointModel implements Serializable {
	public static final String TAG = UpLoadCheckPointModel.class.getSimpleName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 8813912137357284900L;


	/** 检查点唯一ID */
	private int checkpointid;
	
	/** 顺序ID, # 从1开始 */
	private int picid;
	
	/** 顺序ID, # 从1开始 */
	private int cpseqid;
	
	/** 对错 */
	private int isright;

	/** 坐标 */
	private String coordinate;

	/**
	 * 单点列表
	 */
	private ArrayList<UpLoadEXPointModel> explainlist;




	public int getCheckpointid() {
		return checkpointid;
	}

	public void setCheckpointid(int checkpointid) {
		this.checkpointid = checkpointid;
	}

	public int getPicid() {
		return picid;
	}

	public void setPicid(int picid) {
		this.picid = picid;
	}

	public int getCpseqid() {
		return cpseqid;
	}

	public void setCpseqid(int cpseqid) {
		this.cpseqid = cpseqid;
	}

	public int getIsright() {
		return isright;
	}

	public void setIsright(int isright) {
		this.isright = isright;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public ArrayList<UpLoadEXPointModel> getExplainlist() {
		return explainlist;
	}

	public void setExplainlist(ArrayList<UpLoadEXPointModel> explainlist) {
		this.explainlist = explainlist;
	}

	@Override
	public String toString() {
		return "UpLoadCheckPointModel [checkpointid=" + checkpointid + ", picid=" + picid + ", cpseqid=" + cpseqid
				+ ", isright=" + isright + ", coordinate=" + coordinate + ", explainlist=" + explainlist + "]";
	}

}
