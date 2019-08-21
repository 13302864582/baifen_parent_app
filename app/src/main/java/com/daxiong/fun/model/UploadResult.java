package com.daxiong.fun.model;

import java.io.Serializable;

public class UploadResult implements Serializable {

	// {
	// "Code": 0,
	// "Msg": "",
	// "Data": null
	// }
	private static final long serialVersionUID = 1L;
	private int Code;
	private String Msg;
	private String Data;

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}


}
