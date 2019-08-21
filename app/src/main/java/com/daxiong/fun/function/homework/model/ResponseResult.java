package com.daxiong.fun.function.homework.model;

public class ResponseResult {
	private int code = -1;//0成功
	private String msg;//错误信息
	private String data;//json数据
	private int statuscode;
	
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseResult [code=" + code + ", msg=" + msg + ", data=" + data + ", statuscode=" + statuscode + "]";
	}
	public ResponseResult(int code, String msg, String data, int statuscode) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.statuscode = statuscode;
	}
	
	
	
}
