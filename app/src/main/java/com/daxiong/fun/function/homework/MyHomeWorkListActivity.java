package com.daxiong.fun.function.homework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeWorkAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dispatch.HwListController;
import com.daxiong.fun.function.homepage.model.HomeListModel;
import com.daxiong.fun.function.homework.adapter.HomeWorkListAdapter;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyHomeWorkListActivity extends BaseActivity implements IXListViewListener, INetWorkListener {
	private AuToRunTask runTask;
	private XListView xListView;
	private LinearLayout ll_kongbai;
	 private HomeWorkAPI homeworkApi;
	 private TextView nextStepTV;
	 private HwListController hwListController;
	 private long errortime =SharePerfenceUtil.getLong("errortime", 0);
	    private RelativeLayout nextStepLayout;
	private HomeWorkListAdapter homeWorkListAdapter;
	private int LoadMore = 1;
	boolean flag;
	private List<HomeListModel> list = new ArrayList<HomeListModel>();
	private List<Integer> list2 = new ArrayList<Integer>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_homework_list);
		initView();
		initData();
	}
	public void initView() {
		setWelearnTitle("我的作业检查");
		xListView = (XListView) findViewById(R.id.answer_list);
		ll_kongbai = (LinearLayout) findViewById(R.id.ll_kongbai);
		nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
		nextStepLayout.setVisibility(View.VISIBLE);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setText("学情 >");
        nextStepTV.setVisibility(View.VISIBLE);

		homeWorkListAdapter = new HomeWorkListAdapter(this, list);
		xListView.setAdapter(homeWorkListAdapter);
		xListView.setXListViewListener(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		 findViewById(R.id.back_layout).setOnClickListener(this);
		 findViewById(R.id.bt).setOnClickListener(this);
		 nextStepLayout.setOnClickListener(this);   
		 homeworkApi = new HomeWorkAPI();
	}
	public void initData() {

		
		 homeworkApi.getHomeworkList(requestQueue, LoadMore, 10, this,
				RequestConstant.GET_HOMEWORK_LIST_CODE);
	}
	@Override
	public void onRefresh() {
		LoadMore = 1;
		initData();
	}

	@Override
	public void onLoadMore() {
		LoadMore ++;
		initData();
	}

	public void onLoadFinish() {
		// isRefresh = true;
		xListView.stopRefresh();
		xListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		xListView.setRefreshTime(time);
	}
	   @Override
	    public void onResume() {
	        super.onResume();
	        if (hwListController == null) {
	        	hwListController= new HwListController(null, MyHomeWorkListActivity.this);
	        }
	        if (runTask != null) {
	            runTask.start();
	        }
	       
	    }
	    @Override
	    public void onPause() {
	        if (runTask != null) {
	            runTask.stop();
	        }
	        super.onPause();
	    }
	@Override
	public void onDestroy() {
		if(hwListController!=null){
			hwListController.removeMsgInQueue();
			hwListController=null;
		}
		runTask = null;
		super.onDestroy();
	}
	public void updateListUI() {

		list2.clear();

		homeWorkListAdapter.notifyDataSetChanged();
		if (list2.size() > 0) {
			if (runTask == null) {

				runTask = new AuToRunTask();
			}
			runTask.start();
		}
	}
	   @Override
	    public void onClick(View v) {
	        switch (v.getId()) {
	            case R.id.back_layout:// 返回
	                finish();
	                break;
	            case R.id.bt:
	            	  MobclickAgent.onEvent(this,"Open_Homework");
	                  Intent hwintent = new Intent(this, PublishHwActivity.class);
	                  startActivity(hwintent);
	            	break;
	            case R.id.next_setp_layout:// 发布作业/作业分析
	                Intent intent=new Intent(this,MainActivity.class);
	                intent.putExtra("layout", "layout_analysis");
	                startActivity(intent);
	                finish();
	                break;
	         
	            default:
	                break;
	        }
	    }
	public class AuToRunTask implements Runnable {

		@Override
		public void run() {
			if (flag) {
				// 取消之前
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);

				// 延迟执行当前的任务
				MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 1000);// 递归调用
			}
		}

		public void start() {
			if (!flag) {
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this); // 取消之前
				flag = true;
				MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 1000);// 递归调用
			}
		}

		public void stop() {
			if (flag) {
				flag = false;
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);
			}
		}

	}
	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		onLoadFinish();

		int flag = ((Integer) param[0]).intValue();

		switch (flag) {
		case RequestConstant.GET_HOMEWORK_LIST_CODE:
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtil.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						
						List<HomeListModel> parseArray = JSON.parseArray(dataJson, HomeListModel.class);
						if (LoadMore == 1) {
							list.clear();
						}
						 if (parseArray.size()<10) {
                         	xListView.setPullLoadEnable(false);
                         }else{
                        	 xListView.setPullLoadEnable(true);
                        	 
                         }
						list.addAll(parseArray);

						if (list.size() > 0) {
							updateListUI();
							ll_kongbai.setVisibility(View.GONE);
						} else {
							ll_kongbai.setVisibility(View.VISIBLE);

						}
					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;

		}

	}
	@Override
	public void onPre() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onException() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAfter(String jsonStr, int msgDef) {
		if (msgDef == MessageConstant.MSG_DEF_HOME_LIST) {
			  String string = JsonUtil.getString(jsonStr, "content", "");
			  String dataJson = JsonUtil.getString(jsonStr, "data", "");
	             String dataJson2 = JsonUtil.getString(dataJson, "content", "");
	            if (!"".equals(dataJson2)) {
	                HomeListModel parseObject = JSON.parseObject(dataJson2, HomeListModel.class);
//	  				for (int i = 0; i < list.size(); i++) {
//	  					if (parseObject.getCreate_time() == list.get(i).getCreate_time()) {
//	  						HomeListModel remove = list.remove(i);
//	  						if (parseObject.getQuestion_state() == 5 | parseObject.getHomework_state() == 9) {
//							} else {
//								list.add(i, parseObject);
//
//							}
//	  						break;
//	  					}
//	  				}
	  				updateListUI();
	  			}

        }
	}
	@Override
	public void onDisConnect() {
		// TODO Auto-generated method stub
		
	}
}
