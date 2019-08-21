package com.daxiong.fun.base;

/**
 * 
 * 此类的描述： Activity的基本接口
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午2:15:37
 * @version: 2.0
 */

public interface IBaseActivity {
	/**
	 * 此方法描述的是：初始化控件
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:46:53
	 */
	public void initView();

	/**
	 * 此方法描述的是：初始化监听
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:47:20
	 */
	public void initListener();// 设置点击事件

	/**
	 * 此方法描述的是：数据回调
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:47:55
	 */
	public void resultBack(Object... param);// 网络回调
}
