package com.daxiong.fun.function.study.yeartopic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.question.model.CatalogModel;
import com.daxiong.fun.function.question.model.CatalogModel.Chapter;
import com.daxiong.fun.function.question.model.CatalogModel.Point;
import com.daxiong.fun.function.question.model.CatalogModel.Subject;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import java.util.ArrayList;

/**
 * 此类的描述：知识点搜索
 * @author:  sky
 */
public class SearchKnowledgeActivity extends BaseActivity implements TextWatcher, OnClickListener,
		OnKeyListener, HttpListener {
	public static final String CHOICE_TYPE = "choicetype";
	private EditText et;
	private View deleteBtn;
	private TextView gradeTv;
	private TextView subjectTv;
	private TextView chapterTv;
	private TextView kpointTv;
	private ArrayList<CatalogModel> catalogModels;
	public static ArrayList<Subject> subjects;
	public static ArrayList<Chapter> chapters;
	public static ArrayList<Point> points;
	private int gradeGroupId;
	private int subjectGroupId;
	private int chapterGroupId;
	private int knowPointGroupId;
	private int gradeid;
	
	
	private Handler mHandler=new Handler(){
	    public void handleMessage(android.os.Message msg) {
	        switch (msg.what) {
                case 1111:
                    setData();               
                    break;
               
            }
	    }
	};

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.fiter_knowledge_activity);
		initView();
		initListener();
		initData();

		
	}
	
	
	@Override
	public void initView() {
	    super.initView();	    
	    setWelearnTitle(R.string.fiter_title_text);      

        deleteBtn = (View) findViewById(R.id.delete_iv_timu);       
        et = (EditText) findViewById(R.id.search_et_timu);
        
       
        gradeTv = (TextView) findViewById(R.id.grade_choice_tv_timu);
        subjectTv = (TextView) findViewById(R.id.subject_choice_tv_timu);
        chapterTv = (TextView) findViewById(R.id.chapter_choice_tv_timu);
        kpointTv = (TextView) findViewById(R.id.kpoint_choice_tv_timu);
       
       
	}
	
	
	@Override
	public void initListener() {
	    super.initListener();
	    findViewById(R.id.back_layout).setOnClickListener(this);
	    deleteBtn.setOnClickListener(this);
	    et.addTextChangedListener(this);
	    et.setOnKeyListener(this);
	    findViewById(R.id.search_bt_timu).setOnClickListener(this);
        findViewById(R.id.grade_choice_container_timu).setOnClickListener(this);
        findViewById(R.id.subject_choice_container_timu).setOnClickListener(this);
        findViewById(R.id.chapter_choice_container_timu).setOnClickListener(this);
        findViewById(R.id.kpoint_choice_container_timu).setOnClickListener(this);
        findViewById(R.id.search_btn_timu).setOnClickListener(this);
	}
	
	public void initData(){
	    boolean knowledgeExis = DBHelper.getInstance().getWeLearnDB().isKnowledgeExis();
        showDialog("请稍候");
        if (knowledgeExis) {
            catalogModels = DBHelper.getInstance().getWeLearnDB().queryAllKonwledge();
            setData();
            closeDialog();
        } else {
            OkHttpHelper.post(this, "system", "getallkpoint", null, this);
        }
	}

	private void setData() {
		gradeid = MySharePerfenceUtil.getInstance().getGradeGroupId();
		if (gradeid == -1) {
			gradeid = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo().getGradeid();
		}
		subjectGroupId = MySharePerfenceUtil.getInstance().getSubjectGroupId();
		chapterGroupId = MySharePerfenceUtil.getInstance().getChapterGroupId();
		knowPointGroupId = MySharePerfenceUtil.getInstance().getKnowPointGroupId();
		gradeTv.setText(getString(R.string.all_text));
		subjectTv.setText(getString(R.string.all_text));
		chapterTv.setText(getString(R.string.all_text));
		kpointTv.setText(getString(R.string.all_text));
		subjects = null;
		chapters = null;
		points = null;
		switch (gradeid) {
		case -1:
			break;
		case 0:
			gradeTv.setText(getString(R.string.all_text));
			gradeGroupId = 0;
			break;
		case 1:
			gradeTv.setText(getString(R.string.grade_text1));
			gradeGroupId = 1;
			break;
		case 2:
			gradeGroupId = 1;
			gradeTv.setText(getString(R.string.grade_text2));
			break;
		case 3:
			gradeGroupId = 1;
			gradeTv.setText(getString(R.string.grade_text3));
			break;
		case 4:
			gradeGroupId = 2;
			gradeTv.setText(getString(R.string.grade_text4));
			break;
		case 5:
			gradeGroupId = 2;
			gradeTv.setText(getString(R.string.grade_text5));
			break;
		case 6:
			gradeGroupId = 2;
			gradeTv.setText(getString(R.string.grade_text6));
			break;
		default:
			break;
		}
		fiter();
	}

	private void fiter() {
		for (CatalogModel catalogModel : catalogModels) {
			if (catalogModel.getGroupid() == gradeGroupId) {
				subjects = catalogModel.getSubjects();
				switch (subjectGroupId) {
				case -1:
					break;
				case 0:
					subjectTv.setText(getString(R.string.all_text));
					break;
				default:
					for (Subject subject : subjects) {
						if (subject.getSubjectid() == subjectGroupId) {
							subjectTv.setText(subject.getSubjectname());
							chapters = subject.getChapter();
							switch (chapterGroupId) {
							case -1:
								break;
							case 0:
								chapterTv.setText(getString(R.string.all_text));
								break;
							default:
								for (Chapter chapter : chapters) {
									if (chapter.getChapterid() == chapterGroupId) {
										chapterTv.setText(chapter.getChaptername());
										points = chapter.getPoint();
										switch (knowPointGroupId) {
										case -1:
											break;
										case 0:
											kpointTv.setText(getString(R.string.all_text));
											break;
										default:
											for (Point point : points) {
												if (point.getId() == knowPointGroupId) {
													kpointTv.setText(point.getName());
												}
											}
											break;
										}

									}
								}
								break;
							}
						}
					}
					break;
				}

			}
		}
	}

	protected void search() {
		String keyword = et.getText().toString().trim();
		if (TextUtils.isEmpty(keyword)) {
			ToastUtils.show("请先输入您要搜索的关键字");
			return;
		}

		Intent intent = new Intent(this,SearchKnowLedgeResultActivity.class);
		intent.putExtra("type", 1);// 1是学生输关键字 2是搜知识点
		intent.putExtra("keyword", keyword);// 1是学生输关键字 2是搜知识点

//		setResult(RESULT_OK, intent);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View view) {

		Bundle data = new Bundle();
		switch (view.getId()) {
		case R.id.back_layout://返回
			finish();
			break;
		case R.id.search_bt_timu://搜索操作
			search();
			break;
		case R.id.delete_iv_timu:
			et.setText("");
			break;

		case R.id.grade_choice_container_timu:
			if (catalogModels == null) {
				return;
			}
			data.putInt(SearchKnowledgeActivity.CHOICE_TYPE, 1);
			IntentManager.goToChoiceListActivity(this, data);
			break;
		case R.id.subject_choice_container_timu:
			if (subjects == null) {
				ToastUtils.show("请先选择上一级目录");
				return;
			}
			data.putInt(SearchKnowledgeActivity.CHOICE_TYPE, 2);
			IntentManager.goToChoiceListActivity(this, data);
			break;
		case R.id.chapter_choice_container_timu:
			if (chapters == null) {
				ToastUtils.show("请先选择上一级目录");
				return;
			}
			data.putInt(SearchKnowledgeActivity.CHOICE_TYPE, 3);
			IntentManager.goToChoiceListActivity(this, data);
			break;
		case R.id.kpoint_choice_container_timu:
			if (points == null) {
				ToastUtils.show("请先选择上一级目录");
				return;
			}
			data.putInt(SearchKnowledgeActivity.CHOICE_TYPE, 4);
			IntentManager.goToChoiceListActivity(this, data);
			break;
		case R.id.search_btn_timu://查找考题
			Intent intent = new Intent(this,SearchKnowLedgeResultActivity.class);
			intent.putExtra("type", 2);// 1是学生输关键字 2是搜知识点
			intent.putExtra("gradeid", gradeid);
			intent.putExtra("subjectGroupId", subjectGroupId);
			intent.putExtra("chapterGroupId", chapterGroupId);
			intent.putExtra("knowPointGroupId", knowPointGroupId);
//			setResult(RESULT_OK, intent);
			startActivity(intent);
			finish();
			 
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				setData();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			try {
			    closeDialog();
				catalogModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CatalogModel>>() {
				}.getType());				
				new Thread(r).start();
			} catch (Exception e) {
			    e.printStackTrace();
			}			
			
		} else {
			closeDialog();
			ToastUtils.show(errMsg);
		}

	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		closeDialog();
		ToastUtils.show("网络错误：" + HttpCode);
	}
	
	
	private Runnable r= new Runnable() {
        public void run() {
            if (catalogModels != null) {
                DBHelper.getInstance().getWeLearnDB().insertKnowledge(catalogModels);                                  
                mHandler.sendEmptyMessage(1111);
                
            }
        }
    };

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.toString().length() > 0) {
			deleteBtn.setVisibility(View.VISIBLE);
		} else {
			deleteBtn.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			search();
			return true;
		}
		return false;
	}

}
