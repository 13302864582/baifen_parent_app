package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

public class ChatMsgController extends BaseController {

	public ChatMsgController(BaseModel model, INetWorkListener listner) {
		super(model, listner, ChatMsgController.class.getSimpleName());
	}
}
