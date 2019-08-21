package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

/**
 * 消息列表页的控制器
 * @author parsonswang
 *
 */
public class CommunicateController extends BaseController {

	public CommunicateController(BaseModel model, INetWorkListener listner) {
		super(model, listner, CommunicateController.class.getSimpleName());
	}
}
