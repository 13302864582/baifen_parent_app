
package com.daxiong.fun.function.learninganalysis.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.learninganalysis.model.LearningPagerImageModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.AppUtils;
import com.daxiong.fun.util.PackageManagerUtils;

import java.util.LinkedList;
import java.util.List;

public class LearningAdapter extends PagerAdapter {
    private BaseActivity activity;

    private List<LearningPagerImageModel> viewPagerList;

    public LearningAdapter(BaseActivity activity, List<LearningPagerImageModel> viewPagerList) {
        super();
        this.activity = activity;
        this.viewPagerList = viewPagerList;
    }

    // 当前viewPager里面有多少个条目
    LinkedList<ImageView> convertView = new LinkedList<ImageView>();

    // ArrayList
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /* 判断返回的对象和 加载view对象的关系 */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView view = (ImageView)object;
        convertView.add(view);// 把移除的对象 添加到缓存集合中
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view=null;
        if (viewPagerList!=null&&viewPagerList.size()>0) {
            final int index = position % viewPagerList.size();
           
            if (convertView.size() > 0) {
                view = convertView.remove(0);
            } else {

                view = new ImageView(activity);
            }
            view.setScaleType(ScaleType.FIT_XY);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {                    
                    MobclickAgent.onEvent(activity,"Open_ActivityOne");
                    AppUtils.clickevent("s_banner", activity);
                    UserInfoModel    userinfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                    String codeUrl="";                    
                    String  baseUrl=viewPagerList.get(index).getLink();
                    if (baseUrl.contains("fudaotuan.com")) {//如果是大熊作业的网址
                        if (baseUrl.contains("?")) {
                              codeUrl=baseUrl+"&userid={0}&phoneos={1}&appversion={2}";
                              codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");               
                             
                        }else {
                          codeUrl=baseUrl+"?userid={0}&phoneos={1}&appversion={2}";
                          codeUrl=codeUrl.replace("{0}", userinfo.getUserid()+"").replace("{1}", "android").replace("{2}", PackageManagerUtils.getVersionCode()+"");  
                        }
                        Intent intent2 = new Intent(activity, WebViewActivity.class);  
                        intent2.putExtra("title", "大熊作业");
                        intent2.putExtra("url", codeUrl);
                        activity.startActivity(intent2);
                    }else {
                        Toast.makeText(activity, "网址不是火焰作业的网址", 4).show();
                    }
                   

                }
            });
            view.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            Glide.with(activity).load(viewPagerList.get(index).getPicurl())
            .placeholder(R.drawable.default_contact_image).diskCacheStrategy(DiskCacheStrategy.ALL).into( view);
            container.addView(view); // 加载的view对象
        }
       
        return view; // 返回的对象
    }

}
