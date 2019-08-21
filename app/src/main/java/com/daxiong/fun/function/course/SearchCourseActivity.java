package com.daxiong.fun.function.course;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.course.adapter.WBaseAdapter;
import com.daxiong.fun.function.course.holder.BaseHolder;
import com.daxiong.fun.function.course.holder.MoreHolder;
import com.daxiong.fun.function.course.holder.SearchListItemHolder;
import com.daxiong.fun.function.course.model.SearchListItemModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索课程
 */
public class SearchCourseActivity extends BaseActivity implements OnClickListener, OnItemClickListener, HttpListener {

	private final String TAG = SearchCourseActivity.class.getSimpleName();

	private String mSerachText = "";
	private int gradeid = 0;
	private int serachtype = 0; // 代表搜索所有类型
	private int pageindex = 1;
	private final int pagecount = 10;

	private ListView lv_result;
	private TextView tv_result1;
	private TextView tv_result2;
	private List<SearchListItemModel> mData = new ArrayList<SearchListItemModel>();

	private EditText et_input;
	private MoreHolder mMoreHolder;
	private SearchCourseAdapter adapter;

	private int hasMore = MoreHolder.HAS_MORE;

	private boolean isRefresh;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_search_course);
		setWelearnTitle(R.string.search_course);

		findViewById(R.id.back_layout).setOnClickListener(this);

		// 搜索信息
		et_input = (EditText) findViewById(R.id.search_course_et_input);
		findViewById(R.id.search_course_bt).setOnClickListener(this);

		// 提示信息
		TextView tv_warn = (TextView) findViewById(R.id.search_course_tv_warn);

		Spannable spannable = new SpannableString(getResources().getString(R.string.search_course_tv_warn));
		spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.search_text_hint)), 7, 29,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);// [a,b);
		tv_warn.setText(spannable);

		// 搜索结果
		tv_result1 = (TextView) findViewById(R.id.search_course_tv_result1);
		tv_result2 = (TextView) findViewById(R.id.search_course_tv_result2);

		lv_result = (ListView) findViewById(R.id.search_course_xlv);

		adapter = new SearchCourseAdapter(mData);
		lv_result.setAdapter(adapter);
		lv_result.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.search_course_bt:
			isRefresh = true;
			String text = et_input.getText().toString();
			if (TextUtils.isEmpty(text)) {
				Toast.makeText(getApplication(), getResources().getString(R.string.search_input_null_hint), 0).show();
				return;
			}
			mSerachText = text;

			pageindex = 1;
			requestData();
			break;
		}
	}

	/** 请求数据 */
	private void requestData() {
		if (gradeid == 0) {
			UserInfoModel userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			if (userInfo != null) {
				gradeid = userInfo.getGradeid();
			}
		}
		JSONObject json = new JSONObject();
		try {
			json.put("gradeid", gradeid);
			json.put("content", mSerachText);
			json.put("serachtype", serachtype);
			json.put("pageindex", pageindex);
			json.put("pagecount", pagecount);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json：", e1);
		}
		OkHttpHelper.post(getApplication(), "course", "search", json, this);
	}

	private class SearchCourseAdapter extends WBaseAdapter<SearchListItemModel> {

		public static final int MORE_VIEW_TYPE = 0;
		public static final int ITEM_VIEW_TYPE = 1;

		public SearchCourseAdapter(List<SearchListItemModel> data) {
			super(data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BaseHolder holder = null;
			if (null != convertView && convertView.getTag() instanceof SearchListItemHolder) {
				holder = (SearchListItemHolder) convertView.getTag();
			} else {
				if (getItemViewType(position) == MORE_VIEW_TYPE) {
					holder = createMoreHolder();
				} else {
					holder = createHolder();
				}
			}
			if (getItemViewType(position) == ITEM_VIEW_TYPE) {
				((SearchListItemHolder) holder).setSerachText(mSerachText);
				holder.setData(mData.get(position));
			} else {
				System.out.println();
				holder.setData(hasMore);
			}
			return holder.getRootView();
		}

		@Override
		public SearchListItemHolder createHolder() {
			return new SearchListItemHolder();
		}

		@Override
		public int getCount() {
			if (mData != null) {
				return mData.size() + 1;// 加1是为了最后加载更多的布局
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mData != null && position < mData.size()) {
				return mData.get(position);
			}
			return null;
		}

		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == getCount() - 1) {
				return MORE_VIEW_TYPE;// 加载更多的布局
			} else {
				return ITEM_VIEW_TYPE;
			}
		}
	}

	private MoreHolder createMoreHolder() {
		if (mMoreHolder == null) {
			mMoreHolder = new MoreHolder(SearchCourseActivity.this);
		}
		return mMoreHolder;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position < mData.size()) {
			SearchListItemModel model = mData.get(position);
			Bundle data = new Bundle();
			data.putInt("courseid", model.getCourseid());
			IntentManager.goToCourseDetailsActivity(this, data);
		}
	}

	/** [a,b); */
	private Spannable addStyle(String text, int start, int end) {
		Spannable spannable = new SpannableString(text);
		spannable.setSpan(
				new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.search_text_hint)),
				start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	public void loadMore() {
		isRefresh = false;
		++pageindex;
		requestData();
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			try {
				ArrayList<SearchListItemModel> listData = new Gson().fromJson(dataJson,
						new TypeToken<ArrayList<SearchListItemModel>>() {
						}.getType());
				if (listData != null) {
					if (isRefresh) { // 重新加载， 清空数据
						mData.clear(); // 清空自身数据
					}
					mData.addAll(listData);

					hasMore = MoreHolder.NO_MORE;
					if (listData.size() == pagecount) { // 新请求的数据满一页，可能还有
						hasMore = MoreHolder.HAS_MORE;
					}
				}
				((View) lv_result.getParent()).setVisibility(View.VISIBLE);

				int size = mData.size();
				tv_result1.setText(addStyle("搜索 “" + mSerachText + "” 课程", 3, mSerachText.length() + 4));
				if (size == 0) {
					tv_result2.setText("无结果");
					lv_result.setVisibility(View.GONE);
				} else {
					tv_result2.setText("");
					lv_result.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				hasMore = MoreHolder.ERROR;
				LogUtils.e(TAG, "数据请求失败！", e);
			}
		}

	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}
}
