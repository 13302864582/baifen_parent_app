package com.daxiong.fun.callback;

import com.daxiong.fun.model.FudaoquanModel;

import java.util.List;

//定义一个接口便于fragment和fragmentactivity传递数据
public interface IFragmentDataListener {  
    public void transferMessage(List<FudaoquanModel> items);   
}
