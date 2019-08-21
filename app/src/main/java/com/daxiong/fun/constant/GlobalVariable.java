package com.daxiong.fun.constant;


import android.app.Activity;

import com.daxiong.fun.function.RefuseAnswerPopWindow;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailActivity;
import com.daxiong.fun.view.AnswertextPopupWindow;
import com.daxiong.fun.view.BaseFragment;
import com.daxiong.fun.view.CameraPopupWindow;
import com.daxiong.fun.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    public static Activity mainActivity;
    public static Activity myQPadActivity;
    public static Activity QAHallActivity;
    public static Activity centerActivity;
    public static Activity loginActivity;
    public static Activity guideActivity;
    public static Activity oneQueMoreAnswActivity;
    // public static Activity resetPasswordActivity;
    // public static Activity phoneRegActivity;
    public static BaseFragment loginFragment;
    // public static PayFragment payFragment;
    // public static PhoneLoginFragment phoneLoginFragment;

    public static Activity getBackPswActivity;
    public static boolean doingGoldDB;

    public static MyViewPager mViewPager;
    public static OneQuestionMoreAnswersDetailActivity mOneQuestionMoreAnswersDetailActivity;
    //public static ChatMsgViewFragment mChatMsgViewFragment;

    public static ChatMsgViewActivity mChatMsgViewActivity;

    public static RefuseAnswerPopWindow mRefuseAnswerPopWindow;

    public static CameraPopupWindow mCameraPopupWindow;
    public static AnswertextPopupWindow answertextPopupWindow;

//	public static HomeWorkModel mHomeWorkModel;


    //public static  FunctionPopuWindow  mMainMenuPopuWindow;


    public  static List<Activity> tempList=new ArrayList<Activity>();


}
