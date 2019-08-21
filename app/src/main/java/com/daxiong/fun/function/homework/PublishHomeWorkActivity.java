package com.daxiong.fun.function.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.FudaoquanAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GradeConstant;
import com.daxiong.fun.constant.RequestConstant;
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
import com.daxiong.fun.function.myfudaoquan.ExpireFudaoquanActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.FudaoquanModel;
import com.daxiong.fun.model.GradeModel;
import com.daxiong.fun.model.HomeWorkRuleModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.SubjectModel;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.ImageUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.UiUtil;
import com.daxiong.fun.util.WeLearnFileUtil;
import com.daxiong.fun.view.MyGridView;
import com.daxiong.fun.view.MyRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishHomeWorkActivity extends BaseActivity implements OnClickListener, OnItemClickListener,
		OnImageDeleteClickListener, OnCheckedChangeListener, OnUploadListener, HttpListener {

	private static final String TAG = PublishHomeWorkActivity.class.getSimpleName();

	private int MAX_IMAGE_SIZE;

	private float MIN_GOLD;

	private float SINGLE_PAGE_GOLD;

	private int BASE_PAGE_SIZE;

	private static final String UPLOAD_URL = AppConfig.GO_URL + "homework/upload";

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
	private TextView goldTV;
	private ImageButton goldMinusBtn;
	private ImageButton goldplusBtn;
	private TextView useGoldTV;

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
	
	
	private LinearLayout layout_fudaoquan;

    private ImageView iv_choice_radio;

    private LinearLayout view_fudaoquan;
    
    private TextView fudaoquan_tv_type;

    private TextView fudaoquan_tv_data_str;

    private TextView fudaoquan_tv_des;
    
    private LinearLayout layout_xuanshangxuedian;

    private FudaoquanModel fudaoquanmodel;
    
    private static final int REQUEST_EXPIRE_FUDAOQUAN_CODE = 21312;
    private int chooseFudaoquanTag=-1;
    
    private int gradeid;

    private FudaoquanAPI fudaoquanApi;
    
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
		setContentView(R.layout.activity_stu_pulish_home_work);

		setWelearnTitle(R.string.student_publish_home_work_title);

		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			gradeid = uInfo.getGradeid();
			if (gradeid <1) {
				ToastUtils.show(getString(R.string.student_publish_nograde_text));
			}
			grade = DBHelper.getInstance().getWeLearnDB().queryGradeByGradeId(gradeid);
		}

//		getMyOrgs();
		initObject();
		
		initView();

		initData();

		WeLearnFileUtil.clearQuestionFiles();
	}
	
	public void initObject(){
	    
	    fudaoquanApi = new FudaoquanAPI();
	    
	    
	}

    private void getMyOrgs() {
        JSONObject data = new JSONObject();
		try {
			data.put("type", 1);
			data.put("pageindex", 1);
			data.put("pagecount", 1000);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		OkHttpHelper.post(this, "org","myorgs",  data , new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (!TextUtils.isEmpty(dataJson)) {
					ArrayList<OrgModel> listModels = null;
					try {
						listModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<OrgModel> >(){}.getType());
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (listModels != null && listModels.size() > 0) {
						MySharePerfenceUtil.getInstance().setOrgVip();
						IntentManager.goToStuPublishHomeWorkVipActivity(PublishHomeWorkActivity.this, "", 0, listModels);
					}
				}		
				
			}	
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
    }

	public void initView() {
		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setWidth(DensityUtil.dip2px(this, 60));
		// nextStepTV.setTextScaleX(1.8f);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.student_publish_home_work_publish);
		nextStepLayout.setOnClickListener(this);

		layout_xuanshangxuedian=(LinearLayout)this.findViewById(R.id.layout_xuanshangxuedian);
		mMyGridView = (MyGridView) findViewById(R.id.home_work_gridview);
		homeWorkDescET = (EditText) findViewById(R.id.home_work_descript_et);
		publishTitleTV = (TextView) findViewById(R.id.tip_title);
		subjectsTitleTV = (TextView) findViewById(R.id.home_work_subject_choose_title);
		subjectRG = (MyRadioGroup) findViewById(R.id.home_work_subjects_radiogroup);
		goldTV = (TextView) findViewById(R.id.gold_title_tv);
		goldMinusBtn = (ImageButton) findViewById(R.id.home_work_gold_minus_ibtn);
		goldplusBtn = (ImageButton) findViewById(R.id.home_work_gold_plus_ibtn);
		useGoldTV = (TextView) findViewById(R.id.home_work_gold_tv);
		findViewById(R.id.chongzhi_btn).setOnClickListener(this);

		// 辅导券
        layout_fudaoquan = (LinearLayout)findViewById(R.id.layout_fudaoquan);
        iv_choice_radio = (ImageView)findViewById(R.id.iv_choice_radio);
        view_fudaoquan = (LinearLayout)findViewById(R.id.view_fudaoquan);
        fudaoquan_tv_type = (TextView)findViewById(R.id.tv_type);
        fudaoquan_tv_data_str = (TextView)findViewById(R.id.tv_data_str);
        fudaoquan_tv_des = (TextView)findViewById(R.id.tv_des);
        layout_fudaoquan.setOnClickListener(this);
		
		goldMinusBtn.setOnClickListener(this);
		goldplusBtn.setOnClickListener(this);

		subjectRG.setOnCheckedChangeListener(this);

		if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
//			subjectsTitleTV.setVisibility(View.GONE);
//			subjectRG.setVisibility(View.GONE);
//			findViewById(R.id.subject_select_tips).setVisibility(View.GONE);
//			WeLearnSpUtil.getInstance().setSubject(String.valueOf(0));
		    subjectsTitleTV.setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
            initSubjects();
		} else {
			subjectsTitleTV.setText(getString(R.string.text_subject_choice_with_grade, grade.getName()));
			initSubjects();
		}
	}

	@SuppressLint("InflateParams")
	private void initSubjects() {
		subList = DBHelper.getInstance().getWeLearnDB().querySubjectByIdList(grade.getSubjectIds());

		subjectRG.removeAllViews();
		subjectRG.invalidate();
		UiUtil.initSubjects(this, subList, subjectRG, false);
	}

	private void initData() {
		double gold = 0;
		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			gold = uInfo.getGold();
		}

		String goldStr = GoldToStringUtil.GoldToString(gold);
		String goldTitleStr = getString(R.string.student_publish_home_work_pay_title, goldStr);

		SpannableStringBuilder builder = new SpannableStringBuilder(goldTitleStr);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		builder.setSpan(redSpan, goldTitleStr.length() - 1 - goldStr.length(), goldTitleStr.length() - 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		goldTV.setText(builder);

		addHomeWorkPageModel = getAddModel(ADD_IMAGE_TAG);
		hwImagePathList.add(addHomeWorkPageModel);
		mStuPublishHomeWorkAdapter = new StuPublishHomeWorkAdapter(this, hwImagePathList, this);
		mMyGridView.setAdapter(mStuPublishHomeWorkAdapter);
		mMyGridView.setOnItemClickListener(this);

		initRuleInfo(true);
		//判断是否有作业辅导券
		fudaoquanApi.getFudaoquanList(requestQueue, 2, this, RequestConstant.GET_HOMEWORK_QUAN_CODE);
	}

	private void initRuleInfo(boolean retry) {
		boolean isNoData = false;
		HomeWorkRuleModel hwrm = DBHelper.getInstance().getWeLearnDB().queryHomeworkRuleInfoByGradeId(grade.getId());
		if (null != hwrm) {
			BASE_PAGE_SIZE = hwrm.getDefault_pic_count();
			MIN_GOLD = hwrm.getDefault_pic_money();
			MAX_IMAGE_SIZE = hwrm.getMax_pic_count();
			SINGLE_PAGE_GOLD = hwrm.getSingle_pic_money();
		}

		if (BASE_PAGE_SIZE <= 0 || MIN_GOLD <= 0 || MAX_IMAGE_SIZE <= 0 || SINGLE_PAGE_GOLD <= 0) {
			isNoData = true;
		}

		if (isNoData) {
			if (retry) {
//				WeLearnApi.getRuleInfo(this, this);
			} else {
				ToastUtils.show(R.string.server_error);
				finish();
			}
		}

		publishTitleTV.setText(getString(R.string.student_publish_home_work_add_img_title, MAX_IMAGE_SIZE));
		setGlodByImageListSize(MIN_GOLD);
	}

	private StuPublishHomeWorkPageModel getAddModel(String path) {
		StuPublishHomeWorkPageModel newModel = new StuPublishHomeWorkPageModel();
		newModel.setImgpath(path);
		return newModel;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
		    if (MyApplication.getFudaoquanmodel()!=null) {
                MyApplication.setFudaoquanmodel(null);
            }   
			finish();
			break;
		case R.id.chongzhi_btn:
			IntentManager.goPayActivity(this);
			break;
		case R.id.next_setp_layout:
			if (gradeid <1) {
				ToastUtils.show(getString(R.string.student_publish_nograde_text));
				return;
			}
			
			if (hwImagePathList.size() == 1) {
				ToastUtils.show(R.string.student_publish_home_work_add_image);
				return;
			}

			if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
//				currentSubjectId = 0;
				if (currentSubjectId == -1) {
                    ToastUtils.show(R.string.student_publish_home_work_choose_subject);
                    return;
                }
			} else {
				if (currentSubjectId == -1) {
					ToastUtils.show(R.string.student_publish_home_work_choose_subject);
					return;
				}
			}
			showPublishHomeworkConfirmDialog();
			break;
		case R.id.home_work_gold_minus_ibtn:
			String tmPrice = useGoldTV.getText().toString();
			float priceMinus = Float.parseFloat(tmPrice);

			float minGold = setGlodByImageListSize(priceMinus);

			if (priceMinus <= minGold) {
				return;
			}

			priceMinus -= 0.5;
			useGoldTV.setText(GoldToStringUtil.GoldToString(priceMinus));
			break;
		case R.id.home_work_gold_plus_ibtn:
			float pricePlus = Float.parseFloat(useGoldTV.getText().toString());
			pricePlus += 0.5;
			useGoldTV.setText(GoldToStringUtil.GoldToString(pricePlus));
			break;
		 case R.id.layout_fudaoquan:// 辅导券                
             if (MyApplication.getFudaoquanmodel()==null) {
                 Intent fudaoquanIntent = new Intent(this, ExpireFudaoquanActivity.class);
                 fudaoquanIntent.putExtra("tag", "choice_tag_homework");
                 startActivityForResult(fudaoquanIntent, REQUEST_EXPIRE_FUDAOQUAN_CODE);
             }else {
                 view_fudaoquan.setVisibility(View.GONE);
                 iv_choice_radio.setBackgroundResource(R.drawable.choice_no_icon);
                 MyApplication.setFudaoquanmodel(null);
                 layout_xuanshangxuedian.setVisibility(View.VISIBLE);                 
             }
             break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		StuPublishHomeWorkPageModel model = hwImagePathList.get(pos);
		String path = model.getImgpath();
		if (ADD_IMAGE_TAG.equals(path)) {
//			startActivityForResult(new Intent(this, SelectPicPopupWindow.class), REQUEST_CODE_GET_IMAGE_FROM_SYS);
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
			mDialog = WelearnDialog.getDialog(PublishHomeWorkActivity.this);
		}
		mDialog.withMessage(R.string.student_publish_delete_home_work_confirm).setOkButtonClick(
				new View.OnClickListener() {
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
				Bundle extras = data.getExtras();
				if (extras != null) {
					for (String key : extras.keySet()) {
						Object object = extras.get(key);
						LogUtils.e(TAG,"key = " + key);
						if (object != null) {
							String value =  object.toString();
							LogUtils.e(TAG,"value = " +value);
						}else {
							LogUtils.e(TAG,"value = null" );
						}
					}
				}
				LogUtils.i(TAG, path);
				if (TextUtils.isEmpty(path)) {
					LogUtils.e(TAG, "path is null");
					return;
				}
				
//				if (TextUtils.isEmpty(path)) {
//					path = WeLearnSpUtil.getInstance().getPhotoPath();
//					if (TextUtils.isEmpty(path)) {
//						LogUtils.e(TAG, "path is null");
//						return;
//					}
//				}
//				WeLearnSpUtil.getInstance().setPhotoPath("");

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

				setGlodByImageListSize(Float.parseFloat(useGoldTV.getText().toString()));

				mStuPublishHomeWorkAdapter.setData(hwImagePathList);
			}
			
			else if (requestCode == REQUEST_EXPIRE_FUDAOQUAN_CODE) {// 取得辅导券的返回值
	            if (resultCode == RESULT_OK) {
	                if (data != null) {
	                    fudaoquanmodel = (FudaoquanModel)data.getSerializableExtra("fudaoquanmodel");
	                    if (fudaoquanmodel != null) {
	                        MyApplication.setFudaoquanmodel(fudaoquanmodel);
	                        chooseFudaoquanTag=1;
	                        iv_choice_radio.setBackgroundResource(R.drawable.choice_icon);
	                        view_fudaoquan.setVisibility(View.VISIBLE);
	                        fudaoquan_tv_data_str.setText(fudaoquanmodel.getExpireDate());
	                        if (fudaoquanmodel.getType() == 1) {
	                            fudaoquan_tv_type.setText("难题券");
	                            fudaoquan_tv_des.setText("限发布难题使用");
	                        } else {
	                            fudaoquan_tv_type.setText("作业券");
	                            fudaoquan_tv_des.setText("限发布作业使用");
	                        }
	                        
	                        //不显示悬赏学点                        
	                        layout_xuanshangxuedian.setVisibility(View.GONE);
	                    }

	                }
	            }

	        }
		}
	}

	private float setGlodByImageListSize(float currentGold) {
		float result = currentGold;
		int size = hwImagePathList.size();
		float returnMin = MIN_GOLD;

		if (ADD_IMAGE_TAG.equals(hwImagePathList.get(size - 1).getImgpath())) {
			size = size - 1;
		}

		if (size <= BASE_PAGE_SIZE) {
			returnMin = MIN_GOLD;
			if (currentGold < MIN_GOLD) {
				result = MIN_GOLD;
			}
		} else {
			float min = MIN_GOLD + (size - BASE_PAGE_SIZE) * SINGLE_PAGE_GOLD;
			returnMin = min;
			if (currentGold < min) {
				result = min;
			}
		}
		useGoldTV.setText(GoldToStringUtil.GoldToString(result));
		return returnMin;
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
		if (grade.getGradeId() == GradeConstant.GRADE_PARENT_PRIMARY_ID) {
//			currentSubjectId = 0;
		    currentSubjectId = checkedId;
		} else {
			currentSubjectId = checkedId;
		}
	}

	private void publishHomeWork() {
		showDialog(getString(R.string.student_publish_home_work_uploading));
		boolean isNew = false;

		StuPublishHomeWorkModel pm = new StuPublishHomeWorkModel();
		pm.setTasktype(2);
		pm.setPaytype(1);
		String desc = homeWorkDescET.getText().toString();
		pm.setMemo(desc);
		pm.setSubjectid(currentSubjectId);
		pm.setBounty(Float.parseFloat(useGoldTV.getText().toString()));

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

	private void stepOnePublish(StuPublishHomeWorkModel pm){
		try {
		    if (chooseFudaoquanTag==1) {
                pm.setCouponid(fudaoquanmodel.getId());
                
            }
			OkHttpHelper.post(this, "parents","homeworkpublish", new JSONObject(new Gson().toJson(pm)), new HttpListener() {

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
						PublishHomeWorkActivity.this, true, index);
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
			OkHttpHelper.post(this, "parents","homeworkuploadfinish", new JSONObject("{\"taskid\": " + homeWorkId + "}"), new HttpListener() {
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
			mDialog = WelearnDialog.getDialog(PublishHomeWorkActivity.this);
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
			initRuleInfo(false);
		} else {
			ToastUtils.show(errMsg);
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		closeDialog();
	}
	
	
	
	@Override
	public void resultBack(Object... param) {
	    super.resultBack(param);
	    int flag = ((Integer)param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_HOMEWORK_QUAN_CODE:// 获取作业辅导券
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtil.getInt(datas, "Code", -1);
                    String msg = JsonUtil.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            String dataJson = JsonUtil.getString(datas, "Data", "");                            
                            if (!TextUtils.isEmpty(dataJson)) {
                                List<FudaoquanModel> subList = new Gson().fromJson(dataJson,
                                        new TypeToken<List<FudaoquanModel>>() {
                                        }.getType());
                                
                                if (subList!=null&&subList.size()>0) {
                                    layout_fudaoquan.setVisibility(View.VISIBLE);
                                }else {
                                    layout_fudaoquan.setVisibility(View.GONE);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }
	}
	
	
	
}
