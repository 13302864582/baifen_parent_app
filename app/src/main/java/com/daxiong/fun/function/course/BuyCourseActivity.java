package com.daxiong.fun.function.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.course.view.BuyCourseDialog;
import com.daxiong.fun.function.course.view.BuyCourseDialog.OnClickDialogListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyCourseActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = BuyCourseActivity.class.getSimpleName();
	private TextView tv_mylearn;
	private TextView tv_costlearn;
	private TextView tv_teachername;
	private TextView tv_course;
	private TextView tv_subject;
	private TextView tv_class;
	private int courseid;
	private Button bt_buy;
	private int but_state = CourseDetailsActivity.TYPE_NOT_BAY;
	private float gold;
	private float price;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_buy_course);
		setWelearnTitle(R.string.buy_course);
		
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		
		courseid = intent.getIntExtra("courseid", -1);
		String grade = intent.getStringExtra("grade");
		String subject = intent.getStringExtra("subject");
		String coursename = intent.getStringExtra("coursename");
		String name = intent.getStringExtra("name");
		price = intent.getFloatExtra("price", -1);
		
		initView();
		
		tv_class.setText(grade);
		tv_subject.setText(subject);
		tv_course.setText(coursename);
		tv_teachername.setText(name);
		tv_costlearn.setText(price+"");
	
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		UserInfoModel userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if(userInfo != null){
			gold = userInfo.getGold();
			String goldStr = GoldToStringUtil.GoldToString(gold);
			tv_mylearn.setText(goldStr);
		}
	}
	
	public void initView() {
		tv_class = (TextView) findViewById(R.id.buycourse_tv_class);
		tv_subject = (TextView) findViewById(R.id.buycourse_tv_subject);
		tv_course = (TextView) findViewById(R.id.buycourse_tv_course);
		tv_teachername = (TextView) findViewById(R.id.buycourse_tv_teachername);
		tv_costlearn = (TextView) findViewById(R.id.buycourse_tv_costlearn);
		tv_mylearn = (TextView) findViewById(R.id.buycourse_tv_mylearn);
		
		findViewById(R.id.buycourse_tv_gorecharge).setOnClickListener(this);
		bt_buy = (Button) findViewById(R.id.buycourse_bt_buy);
		bt_buy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout: 
			finish();
			break;
		case R.id.buycourse_tv_gorecharge:
			IntentManager.goPayActivity(this);
			break;
		case R.id.buycourse_bt_buy: 
			if(bitIsZero(price)){
				buyCourse();
			}else{
				BuyCourseDialog dialog = new BuyCourseDialog(BuyCourseActivity.this);
				dialog.setOnClickButton1Listener(new OnClickDialogListener() {
					
					@Override
					public void onClick() {
						buyCourse();
					}
				});
				dialog.show();
			}
			break;
		}
	}
	
	public void buyCourse() {
		JSONObject json = new JSONObject();
		try {
			json.put("courseid", courseid);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		showDialog("正在请求数据...");
		OkHttpHelper.post(this, "course", "buy", json, new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (code == 0) {
					BuyCourseDialog dialog = new BuyCourseDialog(BuyCourseActivity.this, "购买成功", "查看", "返回");
					dialog.setOnClickButton1Listener(new OnClickDialogListener() {
						
						@Override
						public void onClick() {
							refreshView();
							Bundle data = new Bundle();
							data.putInt("choose", MastersCourseActivity.CHOOSE_MY_COURSE);
							IntentManager.goToMastersCourseActivity(BuyCourseActivity.this, data, true, Intent.FLAG_ACTIVITY_CLEAR_TOP);
						}
					});
					dialog.setOnClickButton2Listener(new OnClickDialogListener() {
						
						@Override
						public void onClick() {
							refreshView();
						}
					});
					dialog.show();
					but_state = CourseDetailsActivity.TYPE_YET_BAY;
				} else {
					ToastUtils.show(errMsg);
				}
				closeDialog();
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				closeDialog();
				ToastUtils.show("请求失败，失败码：" + HttpCode);
			}
		});
	}

	public void refreshView() {
		
		float newGold = gold - price; //已购买之后。。。
		UserInfoModel userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if(userInfo != null){
			userInfo.setGold(newGold);
			DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(userInfo);
		}
		if(tv_mylearn!=null && bt_buy!=null){
			String goldStr = GoldToStringUtil.GoldToString(newGold);
			tv_mylearn.setText(goldStr);
			bt_buy.setEnabled(false);
		}
	}
	@Override
	public void finish() {
		Intent intent = new Intent(this, CourseDetailsActivity.class);
		intent.putExtra("type", but_state);
		setResult(RESULT_OK, intent); 
		super.finish();
	}
	
	/** 是否为零 */
	private static boolean bitIsZero(float value) {
		final float EPSINON = 0.00001f;
		if ((value >= -EPSINON) && (value <= EPSINON)) {
			return true;
		}
		return false;
	}
}
