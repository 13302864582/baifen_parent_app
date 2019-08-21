package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

public class QuListController extends BaseController {

	public QuListController(BaseModel model, INetWorkListener listner) {
		super(model, listner, QuListController.class.getSimpleName());
	}
}
