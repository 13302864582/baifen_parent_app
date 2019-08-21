package com.daxiong.fun.callback;


public interface INetWorkListener {

	public void onPre();
	
	public void onException();

	public void onAfter(String jsonStr, int msgDef);
	
	public void onDisConnect();
}
