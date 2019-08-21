package com.daxiong.fun.function.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

public class StuPublishHomeWorkPageModel implements Serializable {

	public static final String TAG = StuPublishHomeWorkPageModel.class.getSimpleName();
	private static final long serialVersionUID = 1L;


	/** 单页唯一ID */
	private int id;
	
	/** 作业ID */
	private int taskid;

	/** 顺序ID, # 从1开始 */
	private int picseqid;

	/** 作业页缩略图地址 */
	private String thumbpath;

	/** 作业页图片地址 */
	private String imgpath;

//	/** 宽 */
//	private int width;
//
//	/** 高 */
//	private int height;

	/** 数据时间 */
	private long datatime;

	/** 单题检查点列表 */
	private ArrayList<HomeWorkCheckPointModel> checkpointlist;


	public void setCheckpointlist(ArrayList<HomeWorkCheckPointModel> checkpointlist) {
		this.checkpointlist = checkpointlist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}

	public String getThumbpath() {
		return thumbpath;
	}

	public void setThumbpath(String thumbpath) {
		this.thumbpath = thumbpath;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
//
//	public int getWidth() {
//		return width;
//	}
//
//	public void setWidth(int width) {
//		this.width = width;
//	}
//
//	public int getHeight() {
//		return height;
//	}
//
//	public void setHeight(int height) {
//		this.height = height;
//	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public ArrayList<HomeWorkCheckPointModel> getCheckpointlist() {
		return checkpointlist;
	}


	public int getPicseqid() {
		return picseqid;
	}

	public void setPicseqid(int picseqid) {
		this.picseqid = picseqid;
	}

	@Override
	public String toString() {
		return "StuPublishHomeWorkPageModel [id=" + id + ", taskid=" + taskid + ", picseqid=" + picseqid
				+ ", thumbpath=" + thumbpath + ", imgpath=" + imgpath/* + ", width=" + width + ", height=" + height*/
				+ ", datatime=" + datatime + ", checkpointlist=" + checkpointlist + "]";
	}



}
