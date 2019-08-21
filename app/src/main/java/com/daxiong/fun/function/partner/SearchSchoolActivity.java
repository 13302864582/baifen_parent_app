package com.daxiong.fun.function.partner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.UnivGson;
import com.daxiong.fun.util.MySharePerfenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchSchoolActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private EditText search_school_edittext;
	private TextView search_school_text;
	private ListView search_school_listview;
	private ImageView clearIV;
	private List<UnivGson> list;
	private TextWatcher mWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			if (s.toString().length() > 0) {
				search_school_text.setVisibility(View.VISIBLE);
				search_school_listview.setVisibility(View.VISIBLE);

				/*
				 * 更改数据调用方式 modified by yh 2015-01-07 Start
				 * ---------------------- OLD CODE ---------------------- list =
				 * new
				 * UnivListDBHelper(mActivity).queryUnivListByName(s.toString
				 * ());
				 */
				list = DBHelper.getInstance().getWeLearnDB().queryUnivListByName(s.toString());
				// 更改数据调用方式 modified by yh 2015-01-07 End

				searchAdapter.setmList(list);
			} else {
				search_school_text.setVisibility(View.INVISIBLE);
				search_school_listview.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().length() > 0) {
				search_school_text.setVisibility(View.VISIBLE);
				search_school_listview.setVisibility(View.VISIBLE);
				clearIV.setVisibility(View.VISIBLE);

				/*
				 * 更改数据调用方式 modified by yh 2015-01-07 Start
				 * ---------------------- OLD CODE ---------------------- list =
				 * new
				 * UnivListDBHelper(mActivity).queryUnivListByName(s.toString
				 * ());
				 */
				list = DBHelper.getInstance().getWeLearnDB().queryUnivListByName(s.toString());
				// 更改数据调用方式 modified by yh 2015-01-07 End

				searchAdapter.setmList(list);
			} else {
				search_school_text.setVisibility(View.INVISIBLE);
				search_school_listview.setVisibility(View.INVISIBLE);
				clearIV.setVisibility(View.INVISIBLE);
			}
		}
	};
	private SearchAdapter searchAdapter;
	private int choic;
	private boolean isSearch;
	private String greamSchool = "";
	private int schoolId;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		choic = intent.getIntExtra("choic", 0);
		isSearch = intent.getBooleanExtra("isSearch", false);

		setContentView(R.layout.fragment_search_school);

		if (isSearch) {
			setWelearnTitle(R.string.text_search_school);
		}

		findViewById(R.id.back_layout).setOnClickListener(this);
		clearIV = (ImageView) findViewById(R.id.search_school_delete_bt);
		clearIV.setOnClickListener(this);

		search_school_edittext = (EditText) findViewById(R.id.search_school_edittext);

		getGreamSchool();
		if (isSearch) {
			search_school_edittext.addTextChangedListener(mWatcher);
			search_school_text = (TextView) findViewById(R.id.search_school_text);
			search_school_listview = (ListView) findViewById(R.id.search_school_listview);
			search_school_text.setVisibility(View.INVISIBLE);
			search_school_listview.setVisibility(View.INVISIBLE);
			searchAdapter = new SearchAdapter(list);
			search_school_listview.setAdapter(searchAdapter);
			search_school_listview.setOnItemClickListener(this);
		}
	}

	private void getGreamSchool() {
		switch (choic) {
		case 1:
			greamSchool = MySharePerfenceUtil.getInstance().getGreamSchool1();
			break;
		case 2:
			greamSchool = MySharePerfenceUtil.getInstance().getGreamSchool2();
			break;
		case 3:
			greamSchool = MySharePerfenceUtil.getInstance().getGreamSchool3();
			break;

		default:
			break;
		}
		search_school_edittext.setText(greamSchool);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		greamSchool = list.get(position).getName();
		schoolId = list.get(position).getId();
		saveGreamSchool();
		finish();
	}

	private void saveGreamSchool() {
		switch (choic) {
		case 1:
			MySharePerfenceUtil.getInstance().setGreamSchool1(greamSchool);
			MySharePerfenceUtil.getInstance().setGreamSchoolID1(schoolId);
			break;
		case 2:
			MySharePerfenceUtil.getInstance().setGreamSchool2(greamSchool);
			MySharePerfenceUtil.getInstance().setGreamSchoolID2(schoolId);
			break;
		case 3:
			MySharePerfenceUtil.getInstance().setGreamSchool3(greamSchool);
			MySharePerfenceUtil.getInstance().setGreamSchoolID3(schoolId);
			break;
		default:
			break;
		}

	}

	private class SearchAdapter extends BaseAdapter {
		private List<UnivGson> mList;

		public void setmList(List<UnivGson> mList) {
			this.mList = mList;
			notifyDataSetChanged();
		}

		public SearchAdapter(List<UnivGson> mList) {
			this.mList = mList;
			if (this.mList == null) {
				this.mList = new ArrayList<UnivGson>();
			}
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vv = View.inflate(SearchSchoolActivity.this, R.layout.search_school_list_item, null);
			TextView search_school_item_textview = (TextView) vv.findViewById(R.id.search_school_item_textview);
			/*
			 * if (position == mList.size()) {
			 * search_school_item_textview.setText("其他学校"); } else {
			 */
			search_school_item_textview.setText(mList.get(position).getName());
			// }
			return vv;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.search_school_delete_bt:
			search_school_edittext.setText("");
			break;
		}
	}

   
}
