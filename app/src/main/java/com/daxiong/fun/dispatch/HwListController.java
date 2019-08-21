package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

public class HwListController extends BaseController {

	public HwListController(BaseModel model, INetWorkListener listner) {
		super(model, listner, HwListController.class.getSimpleName());
	}
}
