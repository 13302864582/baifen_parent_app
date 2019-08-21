
package com.daxiong.fun.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.adapter.MyListAdapter;
import com.daxiong.fun.function.study.StuHomeWorkSingleCheckActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.ExplainfeedbackreasonsModel;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎进入大熊作业提示dialog
 * 
 * @author: sky
 */
public class TousuDialog extends Dialog implements  View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

	private int checkpointid;

	private StuHomeWorkSingleCheckActivity context;

	private CheckBox rb1;

	private EditText tv_content;
	private TextView  btn_cancle, btn_ok;
	private int type;
	private int taskid;
	private MyListAdapter myListAdapter;
	private ExplainfeedbackreasonsModel model;
	private ListView lv;
	private List<ExplainfeedbackreasonsModel> list;
	private List<ExplainfeedbackreasonsModel> list2 = new ArrayList<ExplainfeedbackreasonsModel>();

	public TousuDialog(StuHomeWorkSingleCheckActivity context, List<ExplainfeedbackreasonsModel> list, int checkpointid,
			int taskid, int type) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
		this.list = list;
		this.taskid = taskid;
		this.type = type;
		this.checkpointid = checkpointid;
		setCustomDialog(context);

	}

	private void setCustomDialog(Context ctx) {
		View mView = View.inflate(ctx, R.layout.custom_welcome_dialog2, null);


		tv_content = (EditText) mView.findViewById(R.id.tv_content);

		rb1 = (CheckBox) mView.findViewById(R.id.rb1);

		lv = (ListView) mView.findViewById(R.id.lv);
		btn_ok = (TextView) mView.findViewById(R.id.btn_ok);
		btn_cancle = (TextView) mView.findViewById(R.id.btn_cancle);
		rb1.setOnCheckedChangeListener(this);
		lv.setOnItemClickListener(this);
		tv_content.setOnFocusChangeListener(this);
		for (ExplainfeedbackreasonsModel reasonsModel : list) {
			if (reasonsModel.getType() == 1) {
				model = reasonsModel;
				list.remove(reasonsModel);
				break;
			}
		}
		if (model == null) {
			model = new ExplainfeedbackreasonsModel();
			model.setContent("孩子是对的");
			model.setType(1);
		}
		rb1.setText(model.getContent());
		myListAdapter = new MyListAdapter(list, context, false);
		lv.setAdapter(myListAdapter);
		
		btn_ok.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);

		super.setContentView(mView);
	}

	@Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancle:
			dismiss();
			break;
		case R.id.btn_ok:
			if (type == 1) {
				if (rb1.isChecked()) {
					list2.clear();
					list2.add(model);

				} else {
					getCheckList();
				}
			} else
				getCheckList();

		{

		}

			JSONObject jobj = new JSONObject();
			JSONArray joay = new JSONArray();
			if(list2.size()==0){
				ToastUtils.show("必须有投诉理由");
				return;
			}
			try {
				for (ExplainfeedbackreasonsModel mode : list2) {
					JSONObject jobj2 = new JSONObject();
					jobj2.put("content", mode.getContent());
					jobj2.put("type", mode.getType());
					joay.put(jobj2);
				}

				jobj.put("complaint_type", 2);
				jobj.put("taskid", taskid);
				jobj.put("checkpointid", checkpointid);
				jobj.put("reasons", joay);
			} catch (Exception e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(context, "parents", "complainthomework", jobj, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						context.isFankui = true;
						context.onhidefankui(true);
						ToastUtils.show("反馈成功");
						dismiss();
					} else {
						ToastUtils.show(errMsg);
					}
				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
					ToastUtils.show("反馈失败");
				}
			});
			break;
		}
	}

	public void getCheckList() {
		SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
		list2.clear();
		String str = tv_content.getText().toString().trim();
		if (!TextUtils.isEmpty(str)) {

			ExplainfeedbackreasonsModel backreasonsModel = new ExplainfeedbackreasonsModel();
			backreasonsModel.setContent(str);
			backreasonsModel.setType(2);
			list2.add(backreasonsModel);
		}
		for (int i = 0; i < checkedItemPositions.size(); i++) {
			int keyint = checkedItemPositions.keyAt(i);
			boolean boo = checkedItemPositions.get(keyint);
			if (boo) {
				list2.add(list.get(keyint));
			}
		}
	}





	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		rb1.setChecked(false);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		rb1.setChecked(false);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {

			myListAdapter = new MyListAdapter(list, context, false);
			lv.setAdapter(myListAdapter);
			tv_content.setText("");
			tv_content.clearFocus();
		} else {
			myListAdapter = new MyListAdapter(list, context, false);
			lv.setAdapter(myListAdapter);

			tv_content.clearFocus();

		}
	}
}
