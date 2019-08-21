package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

public class HomepageController extends BaseController {

	public HomepageController(BaseModel model, INetWorkListener listner) {
		super(model, listner, HomepageController.class.getSimpleName());
	}
}
