package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

/**
 * 消息列表页的控制器
 * @author parsonswang
 *
 */
public class HomeController extends BaseController {

	public HomeController(BaseModel model, INetWorkListener listner) {
		super(model, listner, HomeController.class.getSimpleName());
	}
}
