package com.daxiong.fun.dispatch;

import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.model.BaseModel;

public class CalendarController extends BaseController {

	public CalendarController(BaseModel model, INetWorkListener listner) {
		super(model, listner, CalendarController.class.getSimpleName());
	}
}
