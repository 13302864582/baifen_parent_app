package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LearningSituationAnalysisActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, HttpListener {

	public static final String TAG = LearningSituationAnalysisActivity.class.getSimpleName();
	private RadioGroup subjectsRG;
	private CoordinateSystem kTypeCs;
	private CoordinateSystem sTypeCs;
	private GradeModel grade;
	private boolean isPrimary;
	private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();
	@SuppressLint("UseSparseArrays")
	private Map<Integer, AnalyzeModel> analyzeModelMap = new HashMap<Integer, AnalyzeModel>();

	private Gson gson = new Gson();
	private Type type = new TypeToken<int[]>() {
	}.getType();

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_learning_situation_analysis);
		setWelearnTitle(R.string.month_analysis_title_text);
		findViewById(R.id.back_layout).setOnClickListener(this);

		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(uInfo.getGradeid());
		}

		initView();

		if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
			isPrimary = true;
		} else {
			initSubjects();
		}

		kTypeCs.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
				new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
				new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
				new int[] {});
		sTypeCs.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
				new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
				new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
				new int[] {});

		OkHttpHelper.post(this, "homework", "analyzeall", null, this);
	}

	public void initView() {
		subjectsRG = (RadioGroup) findViewById(R.id.subjects_radiogroup);
		kTypeCs = (CoordinateSystem) findViewById(R.id.k_type_coordinate);
		sTypeCs = (CoordinateSystem) findViewById(R.id.s_type_coordinate);
		subjectsRG.setOnCheckedChangeListener(this);
	}

	@SuppressLint("InflateParams")
	private void initSubjects() {
		subList = DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());
		subjectsRG.removeAllViews();
		subjectsRG.invalidate();
		UiUtil.initSubjects(this, subList, subjectsRG, false);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		try {
			LogUtils.d(TAG, "checkedId=" + checkedId);
			RadioButton rb = (RadioButton) group.findViewById(checkedId);
			SubjectModel sm = (SubjectModel) rb.getTag();
			AnalyzeModel am = analyzeModelMap.get(sm.getId());
			setAnalyzeInfo(am);
		} catch (Exception e) {
			ToastUtils.show("" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void setAnalyzeInfo(AnalyzeModel am) {
		if (null == am) {
			return;
		}
		kTypeCs.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
				new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
				new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
				am.getKwrong());

		sTypeCs.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
				new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
				new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
				am.getSwrong());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			if (TextUtils.isEmpty(dataJson)) {
				ToastUtils.show("数据异常");
			}
			try {
				for (SubjectModel sm : subList) {
					String jsonStr = JsonUtil.getString(dataJson, "" + sm.getId(), "");
					if (TextUtils.isEmpty(jsonStr)) {
						continue;
					}

					JSONObject subObj = new JSONObject(jsonStr);
					AnalyzeModel am = new AnalyzeModel();
					am.setSubId(sm.getId());
					am.setName(subObj.getString("name"));
					am.setPublish(subObj.getInt("publish"));

					String kwrongs = subObj.getString("kwrong");
					int[] kwrongList = gson.fromJson(kwrongs, type);
					am.setKwrong(kwrongList);

					String swrongs = subObj.getString("swrong");
					int[] swrongList = gson.fromJson(swrongs, type);
					am.setSwrong(swrongList);
					analyzeModelMap.put(sm.getId(), am);
				}

				if (null != analyzeModelMap && analyzeModelMap.size() > 0) {
					if (isPrimary) {
						AnalyzeModel am = analyzeModelMap.get(grade.getSubjectIds());
						setAnalyzeInfo(am);
					} else {
						try {
							View v = subjectsRG.getChildAt(0);
							if (v instanceof RadioButton) {
								RadioButton r = (RadioButton) v;
								r.setChecked(true);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			ToastUtils.show("数据获取失败");
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		ToastUtils.show("网络异常");
	}

	class AnalyzeModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private int subId;
		private String name;
		private int publish;
		private int[] kwrong;
		private int[] swrong;

		public int getSubId() {
			return subId;
		}

		public void setSubId(int subId) {
			this.subId = subId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPublish() {
			return publish;
		}

		public void setPublish(int publish) {
			this.publish = publish;
		}

		public int[] getKwrong() {
			return kwrong;
		}

		public void setKwrong(int[] kwrong) {
			this.kwrong = kwrong;
		}

		public int[] getSwrong() {
			return swrong;
		}

		public void setSwrong(int[] swrong) {
			this.swrong = swrong;
		}

	}

}
