package com.daxiong.fun.function.partner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.TeacherCommentAdapter2;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.CommentModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

import java.util.ArrayList;

/**
 * 
 * 此类的描述： 学生对老师的评价
 * @author:  qhw
 * @最后修改人： qhw 
 * @最后修改日期:2015-7-23 下午3:02:33
 * @version: 2.0
 */
public class StudentAssessmentActivity extends BaseActivity implements OnClickListener, HttpListener{
	RatingBar starLvRb ;
	private int userid;
	private int pageindex = 1;
	private int pagecount = 999;
	private ListView pingjiaLv;
	private TeacherCommentAdapter2 adapter;
	
	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_student_assessment);
		findViewById(R.id.back_layout).setOnClickListener(this);
		setWelearnTitle(R.string.stu_evaluation_text);
		starLvRb = (RatingBar)findViewById(R.id.star_lv_rb_stupingjia);
		pingjiaLv = (ListView)findViewById(R.id.pingjia_lv_stupingjia);
		Intent intent = getIntent();
		
		if (intent!=null) {
			userid = intent.getIntExtra("userid", 0);
		}
		loadData();
	}

	private void loadData (){
		if (userid != 0) {
			WeLearnApi.getTeacherCommList(this, userid,pageindex, pagecount, this);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	    
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			if (!TextUtils.isEmpty(dataJson)) {
				int star = JsonUtil.getInt(dataJson, "star", 0);
				starLvRb.setProgress(star);
				starLvRb.setEnabled(false);
				String contents = JsonUtil.getString(dataJson, "contents", "");
				if (!TextUtils.isEmpty(contents)) {
					ArrayList<CommentModel> comms  = null;
					try {
						comms = new Gson().fromJson(contents, new TypeToken<ArrayList<CommentModel>>() {
						}.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (adapter == null) {
						adapter = new TeacherCommentAdapter2(this, comms);
						pingjiaLv.setAdapter(adapter);
					}else {
						adapter.setData(comms);
					}
					pageindex++;
				}
			}
		}else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		ToastUtils.show("连接失败:"+HttpCode +"");
		
	}

}
