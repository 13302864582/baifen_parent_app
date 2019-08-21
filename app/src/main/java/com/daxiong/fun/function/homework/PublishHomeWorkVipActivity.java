
package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.ResponseCmdConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.homework.adapter.StuPublishHomeWorkAdapter;
import com.daxiong.fun.function.homework.adapter.StuPublishHomeWorkAdapter.OnImageDeleteClickListener;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkUploadFileModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkUploadModel;
import com.daxiong.fun.function.homework.teacher.TecHomeWorkDetail_OnlyReadActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.MyGridView;
import com.daxiong.fun.view.MyRadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类的描述：机构学生发布作业 @最后修改人： sky
 * 
 * @author: sky
 * @最后修改日期:2015年8月4日 上午9:28:48
 */
public class PublishHomeWorkVipActivity extends BaseActivity implements OnClickListener, OnItemClickListener,
		OnImageDeleteClickListener, OnCheckedChangeListener, OnUploadListener, HttpListener {

	private static final String TAG = PublishHomeWorkVipActivity.class.getSimpleName();

	private int BASE_PAGE_SIZE;

	private int MAX_IMAGE_SIZE = 4;

	private static final String UPLOAD_URL = AppConfig.GO_URL + "org/hwpicupload";

	public static final int REQUEST_CODE_GET_IMAGE_FROM_SYS = 0x1;

	public static final int REQUEST_CODE_GET_IMAGE_FROM_CROP = 0x2;

	public static final String ADD_IMAGE_TAG = "add_image_tag";

	private GradeModel grade;

	private ArrayList<SubjectModel> subList = new ArrayList<SubjectModel>();

	private ArrayList<StuPublishHomeWorkPageModel> hwImagePathList = new ArrayList<StuPublishHomeWorkPageModel>();

	private StuPublishHomeWorkAdapter mStuPublishHomeWorkAdapter;

	private StuPublishHomeWorkPageModel addHomeWorkPageModel;

	private ArrayList<StuPublishHomeWorkUploadModel> uploadList;

	private TextView nextStepTV;

	private RelativeLayout nextStepLayout;

	private MyGridView mMyGridView;

	private EditText homeWorkDescET;

	private TextView publishTitleTV;

	private TextView subjectsTitleTV;

	private MyRadioGroup subjectRG;

	private int gradeid;

	private int currentSubjectId = -1;

	/** 发布作业失败时，用来记录当前作业 */
	private StuPublishHomeWorkModel tempPm = null;

	private int homeWorkId = -1;

	/** 记录图片列表是否被修改 */
	private boolean fileListModified = false;

	private WelearnDialog mDialog;

	public static final int MSG_PUBLISH_OK = 0x1;

	public static final int MSG_UPLOAD_PROGRESS = 0x2;

	public static final int MSG_UPLOAD_FINISH = 0x3;

	public static final int MSG_COMMIT_FINISH = 0x4;

	public static final int MSG_COMMIT_FAILED = 0x5;

	private static final int REQUEST_PUBLISHER_CODE = 0x7;// 发题者

	private View choiceFudaoBtn;

	private TextView fudaoTv;

	private ArrayList<OrgModel> OrgList;

	private int orgid;// 机构id

	private String orgname;// 机构名称

	private RelativeLayout layout_choice_publisher;// 选择发题者layout

	private TextView tv_publisher;// 显示发题者

	private ImageView iv_publiser_arrow;

	private List<UserInfoModel> publiserList;// 发题者集合
	private int choice_userId = 0;// 选择发题者的id

	private String username = "";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PUBLISH_OK:
				fileListModified = false;
				if (homeWorkId > 0) {
					// 上传文件
					uploadImage(0);
				} else {
					ToastUtils.show(R.string.student_publish_home_work_publish_server_error);
				}
				break;
			case MSG_UPLOAD_PROGRESS:
				int index = msg.arg1;
				if (AppConfig.IS_DEBUG) {
					ToastUtils.show(getString(R.string.student_publish_home_work_publish_img_index, index + 1));
				}
				break;
			case MSG_UPLOAD_FINISH:
				if (AppConfig.IS_DEBUG) {
					ToastUtils.show(R.string.student_publish_home_work_publish_img_upload_finish);
				}
				uploadFinish();
				break;
			case MSG_COMMIT_FINISH:
				ToastUtils.show(R.string.student_publish_home_work_publish_success);
				// setResult(RESULT_OK);
				HomeWorkHallActivity.reFlesh = true;
				closeDialog();
				uMengEvent("homework_publish");

				tempPm = null;
				homeWorkId = -1;
				fileListModified = false;

				finish();
				break;
			case MSG_COMMIT_FAILED:
				closeDialog();
				ToastUtils.show(R.string.student_publish_home_work_publish_failed);
				break;
			}
		}
	};

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.stu_pulish_home_work_vip_activity);
		getExtraData();
		initView();
		initListener();
		initData();
		WeLearnFileUtil.clearQuestionFiles();
	}

	/**
	 * 此方法描述的是：得到传递过来的数据
	 * 
	 * @author: sky @最后修改人： sky
	 * @最后修改日期:2015年8月4日 上午10:55:49 getExtraData void
	 */
	private void getExtraData() {
		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			gradeid = uInfo.getGradeid();
			if (gradeid < 1) {
				ToastUtils.show(getString(R.string.student_publish_nograde_text));
			}
			grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(gradeid);
		}

		Intent intent = getIntent();
		if (intent != null) {
			orgid = intent.getIntExtra("orgid", 0);
			orgname = intent.getStringExtra("orgname");
			Serializable serializableExtra = intent.getSerializableExtra(OrgModel.TAG);
			if (serializableExtra instanceof ArrayList<?>) {
				OrgList = (ArrayList<OrgModel>) intent.getSerializableExtra(OrgModel.TAG);
			}
		}
	}

	public void initView() {
		super.initView();
		setWelearnTitle(R.string.student_publish_home_work_title);
		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setWidth(DensityUtil.dip2px(this, 60));
		// nextStepTV.setTextScaleX(1.8f);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.student_publish_home_work_publish);
		nextStepLayout.setOnClickListener(this);

		mMyGridView = (MyGridView) findViewById(R.id.home_work_gridview);

		homeWorkDescET = (EditText) findViewById(R.id.pay_answer_descript_et_homework);
		choiceFudaoBtn = findViewById(R.id.choice_fudao_btn_homework);
		fudaoTv = (TextView) findViewById(R.id.fudao_tv_homework);

		publishTitleTV = (TextView) findViewById(R.id.tip_title);
		subjectsTitleTV = (TextView) findViewById(R.id.home_work_subject_choose_title);
		subjectRG = (MyRadioGroup) findViewById(R.id.home_work_subjects_radiogroup);

		layout_choice_publisher = (RelativeLayout) this.findViewById(R.id.layout_choice_publisher);
		tv_publisher = (TextView) this.findViewById(R.id.tv_publisher);
		iv_publiser_arrow = (ImageView) this.findViewById(R.id.iv_publiser_arrow);

		publiserList = new ArrayList<UserInfoModel>();

		if (OrgList != null && OrgList.size() == 1) {
			OrgModel choiceListModel = OrgList.get(0);
			orgid = choiceListModel.getOrgid();
			orgname = choiceListModel.getOrgname();
		}

		// 调用接口是否是机构的特殊账号,决定了是否显示发题者layout
		if (OrgList != null && OrgList.size() == 1) {
			executeGetSpecialStudentList(this, OrgList.get(0).getOrgid());
		}

		if (orgid == 0) {
			choiceFudaoBtn.setOnClickListener(this);
		} else {
			fudaoTv.setText(orgname);
			findViewById(R.id.fudao_iv_homework).setVisibility(View.GONE);
			fudaoTv.setBackgroundColor(getResources().getColor(R.color.unclickable));
		}

		if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
			subjectsTitleTV.setVisibility(View.GONE);
			subjectRG.setVisibility(View.GONE);
			findViewById(R.id.subject_select_tips).setVisibility(View.GONE);
			MySharePerfenceUtil.getInstance().setSubject(String.valueOf(0));
		} else {
			subjectsTitleTV.setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
			initSubjects();
		}
	}

	public void initListener() {
		layout_choice_publisher.setOnClickListener(this);
		subjectRG.setOnCheckedChangeListener(this);
	}

	@SuppressLint("InflateParams")
	private void initSubjects() {
		subList = DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());

		subjectRG.removeAllViews();
		subjectRG.invalidate();
		UiUtil.initSubjects(this, subList, subjectRG, false);
	}

	private void initData() {
		// UserInfoModel uInfo =
		// WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

		addHomeWorkPageModel = getAddModel(ADD_IMAGE_TAG);
		hwImagePathList.add(addHomeWorkPageModel);
		mStuPublishHomeWorkAdapter = new StuPublishHomeWorkAdapter(this, hwImagePathList, this);
		mMyGridView.setAdapter(mStuPublishHomeWorkAdapter);
		mMyGridView.setOnItemClickListener(this);

		publishTitleTV.setText(getString(R.string.student_publish_home_work_add_img_title, MAX_IMAGE_SIZE));
	}

	private StuPublishHomeWorkPageModel getAddModel(String path) {
		StuPublishHomeWorkPageModel newModel = new StuPublishHomeWorkPageModel();
		newModel.setImgpath(path);
		return newModel;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			finish();
			break;
		case R.id.choice_fudao_btn_homework:// 选择辅导机构
			IntentManager.goToChoiceFudaoActivity(this, OrgList);
			break;
		case R.id.layout_choice_publisher:// 选择发题者
			if (orgid != 0 && !TextUtils.isEmpty(orgname)) {
				Intent intent = new Intent(this, ChoicePublisherActivity.class);
				if (publiserList != null && publiserList.size() > 0) {
					intent.putExtra("publiserList", (Serializable) publiserList);
				}
				startActivityForResult(intent, REQUEST_PUBLISHER_CODE);
			}
			break;
		case R.id.next_setp_layout:// 发布作业
			if (gradeid < 1) {
				ToastUtils.show(getString(R.string.student_publish_nograde_text));
				return;
			}

			if (hwImagePathList.size() == 1) {
				ToastUtils.show(R.string.student_publish_home_work_add_image);
				return;
			}

			// if (gradeid > 6) {
			// currentSubjectId = 0;
			// } else {
			// if (currentSubjectId == -1) {
			// ToastUtils.show(R.string.student_publish_home_work_choose_subject);
			// return;
			// }
			// }
			if (currentSubjectId == -1) {
				ToastUtils.show(R.string.student_publish_home_work_choose_subject);
				return;
			}
			showPublishHomeworkConfirmDialog();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		StuPublishHomeWorkPageModel model = hwImagePathList.get(pos);
		String path = model.getImgpath();
		if (ADD_IMAGE_TAG.equals(path)) {
			// startActivityForResult(new Intent(this,
			// SelectPicPopupWindow.class), REQUEST_CODE_GET_IMAGE_FROM_SYS);
			IntentManager.goToSelectPicPopupWindow(this);
		} else {
			Bundle data = new Bundle();
			data.putInt(AnswerListItemView.EXTRA_POSITION, pos);
			ArrayList<StuPublishHomeWorkPageModel> showList = new ArrayList<StuPublishHomeWorkPageModel>();
			for (int i = 0; i < hwImagePathList.size(); i++) {
				StuPublishHomeWorkPageModel tm = hwImagePathList.get(i);
				if (ADD_IMAGE_TAG.equals(tm.getImgpath())) {
					break;
				}
				showList.add(tm);
			}
			data.putSerializable(TecHomeWorkDetail_OnlyReadActivity.HOMEWROKDETAILPAGERLIST, showList);
			IntentManager.goToHomeWorkDetail_OnlyReadActivity(this, data, false);
		}
	}

	@Override
	public void onDeleteClick(final int pos) {
		if (null == mDialog) {
			mDialog = WelearnDialog.getDialog(PublishHomeWorkVipActivity.this);
		}
		mDialog.withMessage(R.string.student_publish_delete_home_work_confirm)
				.setOkButtonClick(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							mDialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
						int size = hwImagePathList.size();
						if (pos >= 0 && pos < size) {
							hwImagePathList.remove(pos);
						}

						size = hwImagePathList.size();
						if (size < MAX_IMAGE_SIZE) {
							StuPublishHomeWorkPageModel md = hwImagePathList.get(size - 1);
							if (!ADD_IMAGE_TAG.equals(md.getImgpath())) {
								hwImagePathList.add(addHomeWorkPageModel);
							}
						}

						mStuPublishHomeWorkAdapter.setData(hwImagePathList);
					}
				});
		mDialog.show();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && null != data) {
			if (requestCode == REQUEST_CODE_GET_IMAGE_FROM_SYS) {
				String path = data.getStringExtra("path");
				LogUtils.i(TAG, path);
				if (TextUtils.isEmpty(path)) {
					LogUtils.i(TAG, "path is null");
					return;
				}

				boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);

				String saveImagePath = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator
						+ "publish_" + System.currentTimeMillis() + ".jpg";

				Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path)));
				sendBroadcast(localIntent);

				localIntent.setClass(this, CropImageActivity.class);
				localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
				localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, saveImagePath);
				localIntent.putExtra("isFromPhotoList", isFromPhotoList);
				startActivityForResult(localIntent, REQUEST_CODE_GET_IMAGE_FROM_CROP);
			} else if (requestCode == REQUEST_CODE_GET_IMAGE_FROM_CROP) {
				String savePath = data.getStringExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG);

				LogUtils.i(TAG, "path=" + savePath);

				hwImagePathList.add(hwImagePathList.size() - 1, getAddModel(savePath));

				fileListModified = true;

				if (hwImagePathList.size() > MAX_IMAGE_SIZE) {
					hwImagePathList.remove(MAX_IMAGE_SIZE);
				}

				mStuPublishHomeWorkAdapter.setData(hwImagePathList);
			} else if (requestCode == 1002) {
				orgid = data.getIntExtra("orgid", 0);
				orgname = data.getStringExtra("orgname");
				fudaoTv.setText(orgname);
			} else if (requestCode == REQUEST_PUBLISHER_CODE) {// 选择发题者
				choice_userId = data.getIntExtra("userid", 0);
				username = data.getStringExtra("username");
				tv_publisher.setText(username);

			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WeLearnFileUtil.deleteQuestionFiles();
		if (null != subjectRG) {
			subjectRG.removeAllViews();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (gradeid == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
			// currentSubjectId = 0;
			currentSubjectId = checkedId;
		} else {
			currentSubjectId = checkedId;
		}
	}

	private void publishHomeWork() {
		showDialog(getString(R.string.student_publish_home_work_uploading));
		boolean isNew = false;

		StuPublishHomeWorkModel pm = new StuPublishHomeWorkModel();
		String description = homeWorkDescET.getText().toString();
		if (TextUtils.isEmpty(description)) {
			description = getString(R.string.text_question_description);
		}
		pm.setMemo(description);
		pm.setSubjectid(currentSubjectId);
		pm.setOrgid(orgid);
		// 判断是否是重复提交,判断图片是否有修改
		if (null != tempPm && pm.equals(tempPm) && !fileListModified) {
			isNew = false;
		} else {
			isNew = true;
		}

		tempPm = pm;
		fileListModified = false;

		if (isNew || homeWorkId == -1) {
			homeWorkId = -1;
			getUploadList();
			stepOnePublish(pm);
		} else {
			mHandler.obtainMessage(MSG_PUBLISH_OK).sendToTarget();
		}
	}

	private void stepOnePublish(StuPublishHomeWorkModel pm) {
		try {
			OkHttpHelper.post(this, "org", "hwpublish", new JSONObject(new Gson().toJson(pm)), new HttpListener() {

				@Override
				public void onFail(int code,String errMsg) {
					ToastUtils.show(R.string.student_publish_home_work_publish_failed);
					closeDialog();
					LogUtils.d(TAG, "onFail code = " + code);
				}

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == ResponseCmdConstant.CODE_RETURN_OK) {
						try {
							JSONObject jobj = new JSONObject(dataJson);
							homeWorkId = jobj.getInt("taskid");
							mHandler.obtainMessage(MSG_PUBLISH_OK).sendToTarget();
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.show(R.string.student_publish_home_work_publish_failed);
							String msg = null == e ? "Error" : e.getMessage();
							LogUtils.d(TAG, msg);
						}
					} else {
						closeDialog();
						if (!TextUtils.isEmpty(errMsg)) {
							ToastUtils.show(errMsg);
							LogUtils.d(TAG, errMsg);
						} else {
							ToastUtils.show(R.string.student_publish_home_work_publish_failed);
						}
					}

				}
			});
		} catch (JSONException e) {
			closeDialog();
			String error = e.getMessage() == null ? "error" : e.getMessage();
			ToastUtils.show(error);
			e.printStackTrace();
		}
	}

	private void getUploadList() {
		if (null == uploadList) {
			uploadList = new ArrayList<StuPublishHomeWorkUploadModel>();
		}
		uploadList.clear();
		StuPublishHomeWorkPageModel hwpm = null;
		StuPublishHomeWorkUploadModel ppm;
		StuPublishHomeWorkUploadFileModel spwufm;
		for (int i = 0; i < hwImagePathList.size(); i++) {
			hwpm = hwImagePathList.get(i);
			if (ADD_IMAGE_TAG.equals(hwpm.getImgpath())) {
				break;
			}
			ppm = new StuPublishHomeWorkUploadModel();
			spwufm = new StuPublishHomeWorkUploadFileModel();
			spwufm.setPicseqid(i + 1);
			spwufm.setMemo("");
			Point p = ImageUtil.getImageSize(hwpm.getImgpath());
			spwufm.setWidth(p.x);
			spwufm.setHeight(p.y);
			ppm.setPicinfo(spwufm);
			uploadList.add(ppm);
		}
	}

	private void uploadImage(int index) {
		if (index < uploadList.size()) {
			StuPublishHomeWorkUploadModel um = uploadList.get(index);
			um.setAction("add_homework");
			um.setActionid(homeWorkId);
			try {
				Map<String, List<File>> files = new HashMap<String, List<File>>();
				StuPublishHomeWorkPageModel hwpm = hwImagePathList.get(index);
				List<File> fList = new ArrayList<File>();
				fList.add(new File(hwpm.getImgpath()));
				files.put("picfile", fList);
				UploadManager.upload(UPLOAD_URL, RequestParamUtils.getParam(new JSONObject(new Gson().toJson(um))), files,
						PublishHomeWorkVipActivity.this, true, index);
			} catch (JSONException e) {
				e.printStackTrace();
				ToastUtils.show("error");
			}
		}
	}

	@Override
	public void onUploadSuccess(UploadResult result, int index) {
		mHandler.obtainMessage(MSG_UPLOAD_PROGRESS, index, 0).sendToTarget();
		int size = uploadList.size();
		if (index < size - 1) {
			uploadImage(index + 1);
		}
		if (index == size - 1) {
			mHandler.obtainMessage(MSG_UPLOAD_FINISH).sendToTarget();
		}
	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		ToastUtils.show(R.string.text_commit_failed_please_retry);
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		ToastUtils.show(R.string.text_commit_failed_please_retry);
	}

	private void uploadFinish() {
		try {
			JSONObject json = new JSONObject();
			json.put("taskid", homeWorkId);
			json.put("userid", choice_userId);
			// new JSONObject("{\"taskid\": " + homeWorkId + "}")
			OkHttpHelper.post(this, "org", "hwuploadfinish", json, new HttpListener() {
				@Override
				public void onFail(int code,String errMsg) {
					closeDialog();
					mHandler.obtainMessage(MSG_COMMIT_FAILED).sendToTarget();
				}

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_COMMIT_FINISH), 1000);
					} else {
						mHandler.obtainMessage(MSG_COMMIT_FAILED).sendToTarget();
					}
				}
			});
		} catch (Exception e) {
			closeDialog();
			e.printStackTrace();
			mHandler.obtainMessage(MSG_COMMIT_FAILED).sendToTarget();
		}
	}

	private void showPublishHomeworkConfirmDialog() {
		if (null == mDialog) {
			mDialog = WelearnDialog.getDialog(PublishHomeWorkVipActivity.this);
		}
		mDialog.withMessage(R.string.student_publish_home_work_confirm).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				publishHomeWork();
			}
		});
		mDialog.show();
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
		} else {
			ToastUtils.show(errMsg);
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		closeDialog();
	}

	// 获取特殊学生列表
	public void executeGetSpecialStudentList(final Activity context, int orggid) {
		try {
			JSONObject json = new JSONObject();
			json.put("orgid", orggid);
			OkHttpHelper.post(context, "org","specialstudentslist", json, new HttpListener() {

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					System.out.println("dataJson-->" + dataJson);
					try {
						JSONObject rootJson = new JSONObject(dataJson);
						// 最终是这样的，count代表它管理学生个个数，如果是０，则代表他不能替代别人发题，如果是大于０，代表他管理学生的个数，students是学生列表
						int count = rootJson.optInt("count");
						if (count > 1) {
							choiceFudaoBtn.setVisibility(View.GONE);
							layout_choice_publisher.setVisibility(View.VISIBLE);
							JSONArray array = rootJson.optJSONArray("students");
							UserInfoModel us = null;
							for (int i = 0; i < array.length(); i++) {
								us = new UserInfoModel();
								JSONObject eachObj = array.getJSONObject(i);
								us.setUserid(eachObj.optInt("userid"));
								us.setGrade(eachObj.optString("grade"));
								us.setName(eachObj.optString("name"));
								publiserList.add(us);
							}

							OrgModel choiceListModel = OrgList.get(0);
							orgid = choiceListModel.getOrgid();
							orgname = choiceListModel.getOrgname();
						} else {
							if (OrgList != null && OrgList.size() >= 1) {
								choiceFudaoBtn.setVisibility(View.VISIBLE);
							} else {
								choiceFudaoBtn.setVisibility(View.GONE);
							}
							layout_choice_publisher.setVisibility(View.GONE);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
