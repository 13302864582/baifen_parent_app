package com.daxiong.fun.function.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.course.holder.CourseDetailsListItemHolder;
import com.daxiong.fun.function.course.model.CharpterModel;
import com.daxiong.fun.function.course.model.CourseDetailsModel;
import com.daxiong.fun.function.course.view.BuyCourseDialog;
import com.daxiong.fun.function.course.view.BuyCourseDialog.OnClickDialogListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程详情
 *
 */
public class CourseDetailsActivity extends BaseActivity implements OnClickListener, HttpListener, OnItemClickListener {

	private static final String TAG = CourseDetailsActivity.class.getSimpleName();

	public static final int TYPE_NOT_BAY = 0;
	public static final int TYPE_YET_BAY = 1;
	
	private int type;
	private boolean isSVIP; //SVIP 或者 啥啥啥

	private List<CharpterModel> mData = new ArrayList<CharpterModel>();
	private CourseDetailsAdapter adapter;

	private NetworkImageView item1_head_icon;
	private TextView item1_tv_stuname;
	private TextView item1_tv_stunumber;
	private TextView item1_tv_class;
	private TextView item1_tv_subject;
	private TextView item1_tv_course;
	private TextView item2_tv_body;
	private TextView item3_tv_course_count;
	private TextView item3_tv_course_cost;
	private int avatarSize;

	private CourseDetailsModel mDetailsData;

	private boolean isInit;

	private int courseid;

	private ListView item4_lv_catalogue;

	private View details_item3;

	private ScrollView mScrollView;

	private LinearLayout course_details_ll;

	private View viewLine;

	private int mScrollY;

	private int mScrollX;

	private TextView item4_lv_tilte;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_course_details);
		setWelearnTitle(R.string.course_details);

		findViewById(R.id.back_layout).setOnClickListener(this);

		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		courseid = intent.getIntExtra("courseid", -1);
		isSVIP = intent.getBooleanExtra("isSVIP", false);
		if (courseid != -1) {
			// 请求数据
			requestData(courseid);
		}
	}

	public void initView() {
		if (isInit) {
			return;
		}
		avatarSize = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.master_list_icon_size);
		
		mScrollView = (ScrollView) findViewById(R.id.course_details_sv);
		course_details_ll = (LinearLayout) findViewById(R.id.course_details_ll);
		
		
		View details_item1 = View.inflate(getApplication(), R.layout.details_item1, null);
		item1_head_icon = (NetworkImageView) details_item1.findViewById(R.id.details_item_iv_head_icon);
		item1_tv_stuname = (TextView) details_item1.findViewById(R.id.details_item_tv_stuname);
		item1_tv_stunumber = (TextView) details_item1.findViewById(R.id.details_item_tv_stunumber);
		item1_tv_class = (TextView) details_item1.findViewById(R.id.details_item_tv_class);
		item1_tv_subject = (TextView) details_item1.findViewById(R.id.details_item_tv_subject);
		item1_tv_course = (TextView) details_item1.findViewById(R.id.details_item_tv_course);
		item1_head_icon.setOnClickListener(this);
		
		View details_item2 = View.inflate(getApplication(), R.layout.details_item2, null);
		item2_tv_body = (TextView) details_item2.findViewById(R.id.details_item_tv_body);
		
		if(type == TYPE_NOT_BAY){
			details_item3 = View.inflate(getApplication(), R.layout.details_item3, null);
			item3_tv_course_count = (TextView) details_item3.findViewById(R.id.details_item_tv_course_count);
			item3_tv_course_cost = (TextView) details_item3.findViewById(R.id.details_item_tv_course_cost);
			details_item3.setOnClickListener(this);
			details_item3.findViewById(R.id.details_item_buycourse_tv_go).setOnClickListener(this);
		}
		
		View details_item4 = View.inflate(getApplication(), R.layout.details_item4, null);
		item4_lv_tilte = (TextView) details_item4.findViewById(R.id.dateils_item4_lv_tilte);
		item4_lv_catalogue = (ListView) details_item4.findViewById(R.id.details_item_lv_catalogue);
		adapter = new CourseDetailsAdapter(mData);
		item4_lv_catalogue.setAdapter(adapter);
		item4_lv_catalogue.setBackgroundColor(getResources().getColor(R.color.master_lv_bg));
		item4_lv_catalogue.setDividerHeight(DensityUtil.dip2px(this, 5));
		item4_lv_catalogue.setOnItemClickListener(this);
		setListViewHeightBasedOnChildren(item4_lv_catalogue);
		
		course_details_ll.addView(details_item1);
		course_details_ll.addView(createCuttingLine());
		course_details_ll.addView(details_item2);
		if(type == TYPE_NOT_BAY){
			viewLine = createCuttingLine();
			course_details_ll.addView(viewLine);
			course_details_ll.addView(details_item3);
		}
		course_details_ll.addView(createCuttingLine());
		course_details_ll.addView(details_item4);
		isInit = true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;
		case R.id.details_item_iv_head_icon: // 头像点击事件
			IntentManager.gotoPersonalPage(this, mDetailsData.getUserid(), GlobalContant.ROLE_ID_COLLEAGE);
			break;
		case R.id.details_item_buycourse_tv_go: // 购买课程
			goBuyCourse();
			break;
		case R.id.details_item3_ll: // 购买课程
			goBuyCourse();
			break;
		}
	}

	/** 去购买课程 */
	private void goBuyCourse() {
		Bundle data = new Bundle();
		if (mDetailsData != null) {
			data.putInt("courseid", courseid);
			data.putString("grade", mDetailsData.getGrade());
			data.putString("subject", mDetailsData.getSubject());
			data.putString("coursename", mDetailsData.getCoursename());
			data.putString("name", mDetailsData.getName());
			data.putFloat("price", mDetailsData.getPrice());
		}
		IntentManager.goToBuyCourseActivityForResult(this, data, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			type = data.getIntExtra("type", -1);
			if (type == TYPE_YET_BAY) {
				// 隐藏 Item3
				course_details_ll.removeView(viewLine);
				course_details_ll.removeView(details_item3);
			}
		}
	}

	private class CourseDetailsAdapter extends WBaseAdapter<CharpterModel> {

		public CourseDetailsAdapter(List<CharpterModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<CharpterModel> createHolder() {
			return new CourseDetailsListItemHolder();
		}
		
		@Override
		public boolean getBoolParam() {
			return isSVIP;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private void requestData(int courseid) {
		JSONObject json = new JSONObject();
		try {
			json.put("courseid", courseid);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "course","coursedetail",  json, this);
	}

	

	private void refreshData(CourseDetailsModel data) {
		ImageLoader.getInstance().loadImage(data.getAvatar(), item1_head_icon, R.drawable.ic_default_avatar,
				avatarSize, avatarSize / 10);
		item1_tv_stuname.setText(data.getName());
		item1_tv_stunumber.setText(data.getUserid() + "");
		item1_tv_class.setText(data.getGrade());
		item1_tv_subject.setText(data.getSubject());
		item1_tv_course.setText(data.getCoursename());

		item2_tv_body.setText(data.getContent());

		if (type == TYPE_NOT_BAY) {
			String charptercount = data.getCharptercount() + "";
			item3_tv_course_count.setText(addStyle("本课程共" + charptercount + "课时", 4, 4 + charptercount.length()));

			String price = data.getPrice() + "";
			item3_tv_course_cost.setText(addStyle("需花费" + price + "学点", 3, 3 + price.length()));
		} else {
			// 页面再次刷新的时候， 或许需要移除
			course_details_ll.removeView(viewLine);
			course_details_ll.removeView(details_item3);
		}
		
		int totalCount = data.getCharptercount();
		List<CharpterModel> charpter = data.getCharpter();
		int recordCount = 0;
		if(charpter != null){
			recordCount = charpter.size();
		}
		
		String text ="(共"+totalCount+"课时，已完成"+recordCount+"课时)";
		Spannable spannable = addStyle(text, 2, 2+String.valueOf(totalCount).length());
		int start = text.indexOf("成") + 1;
		item4_lv_tilte.setText(addStyle(spannable, start, start+String.valueOf(recordCount).length()));

		mData.clear();
		mData.addAll(data.getCharpter());
		adapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(item4_lv_catalogue);
		mScrollView.smoothScrollTo(0,0);
	}

	/** [a,b); */
	private Spannable addStyle(CharSequence text, int start, int end) {
		Spannable spannable = new SpannableString(text);
		spannable.setSpan(new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.details_number_color)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	private View createCuttingLine() {
		View view = new View(this);
		view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 1)));
		view.setBackgroundColor(getResources().getColor(R.color.details_lv_divider));
		return view;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mScrollView != null){
			mScrollX = mScrollView.getScrollX();
			mScrollY = mScrollView.getScrollY();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(mScrollView != null){
			mScrollView.smoothScrollTo(mScrollX, mScrollY);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO (已完成)课时 Item点击事件
		if(type == TYPE_NOT_BAY && position > 1){ 
			BuyCourseDialog dialog = new BuyCourseDialog(this, "请先购买该课程！", "去购买", "再看看");
			dialog.setOnClickButton1Listener(new OnClickDialogListener() {
				
				@Override
				public void onClick() {
					goBuyCourse();
				}
			});
			dialog.show();
		}else{
			CharpterModel model = mDetailsData.getCharpter().get(position);
			IntentManager.goToCharpterDetailActivity(this, model.getCharpterid(),
					model.getCharptername(),
					mDetailsData.getUserid(),
					mDetailsData.getAvatar(), 
					mDetailsData.getName() , type == TYPE_YET_BAY);
		}
	}
	
	@Override
	public void finish() {
		Intent intent = new Intent(this, MastersCourseActivity.class);
		intent.putExtra("type", type);
		setResult(RESULT_OK, intent); 
		super.finish();
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			try {
				CourseDetailsModel data = new Gson().fromJson(dataJson, new TypeToken<CourseDetailsModel>() {
				}.getType());
				if (data != null) {
					mDetailsData = data;
					if(isSVIP){
						type = TYPE_YET_BAY;
					}else{
						type = data.getBuystate();
					}
					initView();
					refreshData(data);
				}
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {		
		
	}
}