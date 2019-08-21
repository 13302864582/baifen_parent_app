package com.daxiong.fun.model;

import java.util.LinkedHashSet;



/**
 * 问题model
 * @author parsonswang
 *
 */
public class AddAnswerRequestModel {

	private long answer_id;
	private long question_id;
	private int teach_id;
	private String extension;
	private String base64;
	private int width;
	private int height;
	private int stuid;
	private LinkedHashSet<ExplainPoint> points;
	
	public AddAnswerRequestModel(long answer_id, long question_id,int stuid,
			int teach_id, String extension, String base64, int width,
			int height, LinkedHashSet<ExplainPoint> points) {
		super();
		this.answer_id = answer_id;
		this.question_id = question_id;
		this.teach_id = teach_id;
		this.extension = extension;
		this.base64 = base64;
		this.width = width;
		this.height = height;
		this.points = points;
		this.stuid = stuid;
	}
	
	public int getStuid() {
		return stuid;
	}

	public void setStuid(int stuid) {
		this.stuid = stuid;
	}
	
	public long getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(long answer_id) {
		this.answer_id = answer_id;
	}
	public long getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}
	public int getTeach_id() {
		return teach_id;
	}
	public void setTeach_id(int teach_id) {
		this.teach_id = teach_id;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public LinkedHashSet<ExplainPoint> getPoints() {
		return points;
	}
	public void setPoints(LinkedHashSet<ExplainPoint> points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "AddAnswerRequestModel [answer_id=" + answer_id
				+ ", question_id=" + question_id + ", teach_id=" + teach_id
				+ ", extension=" + extension + ",  width=" + width + ", height=" + height + ", points="
				+ points + "]";
	} 
}
