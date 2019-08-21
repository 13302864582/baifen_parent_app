
package com.daxiong.fun.base;

import android.content.Context;
import android.view.View;

/**
 * 
 * 此类的描述： Fragement基本接口
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2015年7月14日 上午2:16:05
 * @version: 2.0
 */
public interface IBaseFragment {
    
    
    public  void initView();

	/**
	 * 此方法描述的是：初始化视图
	 * @author:  Sky
	 * @最后修改人： Sky 
	 * @最后修改日期:2015年7月14日 上午10:45:41	
	 */
    public  void initView(View view);
    /**
	 * 此方法描述的是：初始化监听
	 * @author:  Sky
	 * @最后修改人： Sky 
	 * @最后修改日期:2015年7月14日 上午10:45:41	
	 */
    void initListener();
    /**
	 * 此方法描述的是：返回接口的数据
	 * @author:  Sky
	 * @最后修改人： Sky 
	 * @最后修改日期:2015年7月14日 上午10:45:41	
	 */
    void resultBack(Object... param);
    /**
	 * 此方法描述的是：初始化上下文
	 * @author:  Sky
	 * @最后修改人： Sky 
	 * @最后修改日期:2015年7月14日 上午10:45:41	
	 */
    Context getContext();// 获取上下文
}
