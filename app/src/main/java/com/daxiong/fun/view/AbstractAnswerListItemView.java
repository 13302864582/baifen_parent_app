package com.daxiong.fun.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.model.AnswerListItemGsonModel;

import java.util.Random;

public abstract class AbstractAnswerListItemView extends FrameLayout implements OnClickListener {

	protected NetworkImageView mAnswerAvatar;
	protected TextView mAnswerNick;
	protected TextView mColleage;
	protected TextView mPublishTime;
	protected TextView mAnswerViewCnt;
	
	protected NetworkImageView mQuestionPic;
	
	protected FrameLayout mAnswerPic1Container;
	protected NetworkImageView mAnswerPic1;
	//protected TextView mAnswerUp1;
	
	protected FrameLayout mAnswerPic2Container;
	protected NetworkImageView mAnswerPic2;
	//protected TextView mAnswerUp2;
	
	protected FrameLayout mAnswerPic3Container;
	protected NetworkImageView mAnswerPic3;
	//protected TextView mAnswerUp3;
	
	protected TextView mGradeSubject;
	protected TextView mStatus;
	protected ImageView mBgIcon;
	
	protected View view;
	
	protected LinearLayout mAnswerPicContainer;
	protected SparseArray<String> status = new SparseArray<String>();
	
	private int[] drawabes = new int[]{R.drawable.ic_pin_1, R.drawable.ic_pin_2, 
			R.drawable.ic_pin_3, R.drawable.ic_pin_4};
	
	protected View collectionContainer;//收藏
	protected ImageView collectionIconIv;
	protected TextView collectionCountTv;
	protected boolean isCollected;
	protected int praisecnt;//赞的次数
	protected AnswerListItemGsonModel mAnswerListItemGson;

	protected int avatarSize;
	
	public AbstractAnswerListItemView(Context context) {
		super(context);
		setUpViews();
	}

/*	public AbstractAnswerListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
	}*/
	
	private void setUpViews() {
		view = null;
		view = View.inflate(getContext(), R.layout.answer_list_item, null);
		//view = LayoutInflater.from(getContext()).inflate(R.layout.answer_list_item, null);
		mAnswerAvatar = (NetworkImageView) view.findViewById(R.id.answerer_avatar);
		mAnswerNick = (TextView) view.findViewById(R.id.answerer_nick);
		mColleage = (TextView) view.findViewById(R.id.answerer_colleage);

		avatarSize = getResources().getDimensionPixelSize(R.dimen.question_list_avatar_size);
		
		collectionContainer = view.findViewById(R.id.collection_container_linearlayout_answerlistitem);
		collectionIconIv =  (ImageView) view.findViewById(R.id.collection_icon_iv_answerlistitem);
		collectionCountTv = (TextView) view.findViewById(R.id.collection_count_tv_answerlistitem);
		collectionContainer.setOnClickListener(this);
		
		
		mPublishTime = (TextView) view.findViewById(R.id.publish_time);
		mAnswerViewCnt = (TextView) view.findViewById(R.id.answer_viewed_record);
		
		mQuestionPic = (NetworkImageView) view.findViewById(R.id.question_pic);
		mQuestionPic.setOnClickListener(this);
		
		mAnswerPicContainer = (LinearLayout) view.findViewById(R.id.answer_pic_container);
		mAnswerPic1Container = (FrameLayout) view.findViewById(R.id.answer_pic_1_container);
		mAnswerPic1 = (NetworkImageView) view.findViewById(R.id.answer_pic_1);
		//mAnswerUp1 = (TextView) view.findViewById(R.id.answer_pic_1_up);
		mAnswerPic1.setOnClickListener(this);
		
		mAnswerPic2Container = (FrameLayout) view.findViewById(R.id.answer_pic_2_container);
		mAnswerPic2 = (NetworkImageView) view.findViewById(R.id.answer_pic_2);
		//mAnswerUp2 = (TextView) view.findViewById(R.id.answer_pic_2_up);
		mAnswerPic2.setOnClickListener(this);
		
		mAnswerPic3Container = (FrameLayout) view.findViewById(R.id.answer_pic_3_container);
		mAnswerPic3 = (NetworkImageView) view.findViewById(R.id.answer_pic_3);
		//mAnswerUp3 = (TextView) view.findViewById(R.id.answer_pic_3_up);
		mAnswerPic3.setOnClickListener(this);
		
		mAnswerAvatar.setOnClickListener(this);
		mGradeSubject = (TextView) view.findViewById(R.id.answer_grade_subject);
		mStatus = (TextView) view.findViewById(R.id.question_status);
		
		mBgIcon = (ImageView) view.findViewById(R.id.bg_icon);
		int index = new Random().nextInt(4);
		mBgIcon.setImageResource(drawabes[index]);
		addView(view);
		
		initStatusMap();
	}
	
	private void initStatusMap() {
		status.put(0, "答题中");
		status.put(1, "已回答");
		//hahahahh
		//new Button(getContext());
		status.put(2, "已采纳");
		status.put(3, "已拒绝");
		status.put(4, "仲裁中");
		status.put(5, "被删除");
		status.put(6, "追问中");
		status.put(8, "仲裁完成");
		status.put(9, "被举报");
	}

	@Override
	public void onClick(View view) {
		onClickCallback(view);
	}

	protected abstract void onClickCallback(View view);
}
