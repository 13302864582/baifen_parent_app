package com.daxiong.fun.function.cram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.model.OrgModel;

import java.util.ArrayList;

public class ChoiceFudaoActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	private ListView listView;
//	public static class ChoiceListModel implements Serializable{
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -7785172378346890901L;
//		public static final String TAG = ChoiceListModel.class.getSimpleName();
//		String orgname ;
//		int orgid;
//		public String getOrgname() {
//			return orgname;
//		}
//		public void setOrgname(String orgname) {
//			this.orgname = orgname;
//		}
//		public int getOrgid() {
//			return orgid;
//		}
//		public void setOrgid(int orgid) {
//			this.orgid = orgid;
//		}
//		@Override
//		public String toString() {
//			return "ChoiceListModel [orgname=" + orgname + ", orgid=" + orgid + "]";
//		}
//		
//		
//	}
	
	private ArrayList<OrgModel>listModels ;
	
	@SuppressWarnings("unchecked")
	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.choice_list_activity);
		findViewById(R.id.back_layout).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.list_lv_choice);
		setWelearnTitle(R.string.choice_fudao_text);
		ChoiceListAdapter adapter;

		Intent intent = getIntent();
		if (intent != null) {
			listModels = (ArrayList<OrgModel>) intent.getSerializableExtra(OrgModel.TAG);
		}
		adapter = new ChoiceListAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	class ChoiceListAdapter extends BaseAdapter {
		private int size;

		public ChoiceListAdapter() {
			if (listModels != null) {
				size = listModels.size();
			}
		}

		@Override
		public int getCount() {
			return size ;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			TextView view = null;
			if (convertView == null) {
				view = (TextView) View.inflate(ChoiceFudaoActivity.this, R.layout.fiter_choice_know_item, null);
			} else {
				view = (TextView) convertView;
			}
			OrgModel model = listModels.get(position);
			String orgname = "";
			int orgid = 0;
			if (model != null) {
				orgname = model.getOrgname();
				orgid = model.getOrgid();
			}
			
			view.setText(orgname);
			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if (listModels != null && listModels.size()>position) {
			OrgModel model = listModels.get(position);
			int orgid = model.getOrgid();
			String orgname = model.getOrgname();
			Intent intent = new Intent();
			intent.putExtra("orgid", orgid);
			intent.putExtra("orgname", orgname);
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
}
