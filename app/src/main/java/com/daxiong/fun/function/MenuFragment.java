
package com.daxiong.fun.function;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.cram.MyCramSchoolActivity;
import com.daxiong.fun.function.account.vip.SelectRechargeCardActivity;
import com.daxiong.fun.function.homework.HomeWorkHallActivity;
import com.daxiong.fun.function.myfudaoquan.MyFudaoquanActivity;
import com.daxiong.fun.function.question.MyQuestionActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.manager.UpdateManagerForDialog;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GoldToStringUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

/**
 * 此类的描述： * 侧边栏
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 上午11:25:10
 */
public class MenuFragment extends Fragment implements OnClickListener {

    public static final String TAG = MenuFragment.class.getSimpleName();

    /** 账号名字 */
    private TextView mAccNameTv, mAccNumTv, mMoney, mCredit;

    /** 账号头像 */
    private NetworkImageView mAccHeadImv;

    private UserInfoModel userInfo;

    /** 学点充值 */
    private RelativeLayout mRechargeLayout;

    // 我的提问
    private RelativeLayout mAskLayout;

    // 我的作业检查
    private RelativeLayout mCheckedHomeworkLayout;

    private RelativeLayout checkUpdateLayout;

    private View updateTips;

    private int latestVersion;

    private UpdateManagerForDialog mUpdateManager;

    // private ImageView qrIV;

    public static boolean isDoInDb = false;

    // 我的补习班View
    private View cramSchoolView;
    //我的辅导券
    private RelativeLayout layout_my_fudaoquan;

    private boolean orgVip;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateManager = new UpdateManagerForDialog(getActivity());
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welearn_menu, null);
        initView(view);
        initListener(view);
        return view;
    }

    /**
     * 此方法描述的是：初始化视图
     * 
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015-7-31 下午6:00:31 initView
     * @param view void
     */
    public void initView(View view) {
        mAccHeadImv = (NetworkImageView)view.findViewById(R.id.menu_acc_head_icon);

        //mAccHeadImv.setDefaultImageResId(R.drawable.default_contact_image);

        mRechargeLayout = (RelativeLayout)view.findViewById(R.id.menu_recharge);

        mAskLayout = (RelativeLayout)view.findViewById(R.id.menu_my_ask);

        mCheckedHomeworkLayout = (RelativeLayout)view.findViewById(R.id.menu_my_homework);

        orgVip = MySharePerfenceUtil.getInstance().isOrgVip();
        cramSchoolView = view.findViewById(R.id.menu_cramschool);
        cramSchoolView.setOnClickListener(this);
        if (orgVip) {
            cramSchoolView.setVisibility(View.VISIBLE);
        } else {
            cramSchoolView.setVisibility(View.GONE);
        }
        layout_my_fudaoquan = (RelativeLayout)view.findViewById(R.id.menu_my_fudaoquan);

        checkUpdateLayout = (RelativeLayout)view.findViewById(R.id.menu_check_update);

        mAccNameTv = (TextView)view.findViewById(R.id.menu_acc_name);
        mAccNumTv = (TextView)view.findViewById(R.id.menu_stu_num);
        mMoney = (TextView)view.findViewById(R.id.menu_money);
        mCredit = (TextView)view.findViewById(R.id.menu_credit);
        updateTips = view.findViewById(R.id.tips_update_iv_setting);
        // qrIV = (ImageView) view.findViewById(R.id.qr_imageview);

    }

    private void initListener(View view) {
        mAccHeadImv.setOnClickListener(this);
        mRechargeLayout.setOnClickListener(this);
        mAskLayout.setOnClickListener(this);
        mCheckedHomeworkLayout.setOnClickListener(this);
        view.findViewById(R.id.menu_microcoach).setOnClickListener(this);
        view.findViewById(R.id.menu_learning_situation_analysis).setOnClickListener(this);
        view.findViewById(R.id.menu_settings).setOnClickListener(this);
        view.findViewById(R.id.menu_persion_info).setOnClickListener(this);
        view.findViewById(R.id.menu_about_us).setOnClickListener(this);
        checkUpdateLayout.setOnClickListener(this);
        layout_my_fudaoquan.setOnClickListener(this);
        

    }

    @Override
    public void onResume() {
        super.onResume();
        updateLeftUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_recharge:// 学点充值
                MobclickAgent.onEvent(getActivity(), "learnpointrecharge");
                if (getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), SelectRechargeCardActivity.class);
                    getActivity().startActivity(intent);
                }                
                break;
            case R.id.menu_my_ask:// 我的提问
                if (getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
                    getActivity().startActivity(intent);  
                }
                break;
            case R.id.menu_my_homework:// 我的作业检查
                Intent i = new Intent(getActivity(), HomeWorkHallActivity.class);
                i.putExtra("packtype", 2);
                getActivity().startActivity(i);
                break;
            case R.id.menu_settings:// 系统设置
                IntentManager.goToSystemSetting(getActivity());
                break;
            case R.id.menu_learning_situation_analysis:// 作业行情分析
                IntentManager.goToLearningSituationAnalysis(getActivity());
                break;
            case R.id.menu_check_update:
                WeLearnApi.checkUpdate();
                latestVersion = MySharePerfenceUtil.getInstance().getLatestVersion();
                if (latestVersion > MyApplication.versionCode) {
                    if (latestVersion > MyApplication.versionCode) {
                        updateTips.setVisibility(View.VISIBLE);
                    } else {
                        updateTips.setVisibility(View.GONE);
                    }
                    if (mUpdateManager == null) {
                        if (!getActivity().isFinishing()) {
                            mUpdateManager = new UpdateManagerForDialog(getActivity());
                        }
                    }
                    if (mUpdateManager != null) {
                        mUpdateManager.cloesNoticeDialog();
                        mUpdateManager.showNoticeDialog(false);
                    }
                } else {
                    ToastUtils.show("没有发现新版本");
                }
                break;
            case R.id.menu_about_us:
                IntentManager.goToAbout(getActivity());
                break;
            case R.id.menu_acc_head_icon:
            case R.id.menu_persion_info:// 个人主页
                Bundle data = new Bundle();
                data.putInt("userid", userInfo.getUserid());
                data.putInt("roleid", userInfo.getRoleid());
//                IntentManager.goToStudentCenterView(getActivity(), data);
                break;
            case R.id.menu_microcoach: // 微课辅导
                IntentManager.goToMastersCourseActivity(getActivity(), false, -1);
                break;
            case R.id.menu_cramschool: // 我的补习班
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MyCramSchoolActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.menu_my_fudaoquan://我的辅导券
                if (getActivity()!=null) {
                    Intent intent = new Intent(getActivity(), MyFudaoquanActivity.class);
                    getActivity().startActivity(intent);
                }
                break;

        }
    }

    /**
     * 此方法描述的是：更新左边的侧边栏信息Ui
     * 
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015-7-31 下午6:02:07 updateLeftUI void
     */
    private void updateLeftUI() {
        userInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == userInfo) {
//            ToastUtils.show(R.string.text_unlogin);
//            return;
        }

        if (userInfo.getRoleid() == GlobalContant.ROLE_ID_COLLEAGE) {
            mRechargeLayout.setVisibility(View.GONE);
            mAskLayout.setVisibility(View.GONE);
            mCheckedHomeworkLayout.setVisibility(View.GONE);
        } else {
        }

        orgVip = MySharePerfenceUtil.getInstance().isOrgVip();
        if (orgVip) {
            cramSchoolView.setVisibility(View.VISIBLE);
        } else {
            cramSchoolView.setVisibility(View.GONE);
        }
        // ImageLoader.getInstance().loadImage(userInfo.getAvatar_100(),
        // mAccHeadImv, R.drawable.default_contact_image);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
        ImageLoader.getInstance().loadImage(userInfo.getAvatar_100(), mAccHeadImv,
                R.drawable.default_contact_image, R.drawable.default_contact_image, imageSize,
                imageSize / 10);
    
        mAccNameTv.setText(userInfo.getName() == null ? "" : userInfo.getName());
        mAccNumTv.setText(getString(R.string.student_id, userInfo.getUserid()));

        double gold = userInfo.getGold();
        String goldStr = GoldToStringUtil.GoldToString(gold);
        mMoney.setText(goldStr);
        mCredit.setText(String.valueOf(userInfo.getCredit()));
    }

    /**
     * 此方法描述的是：展开左边的侧边栏
     * 
     * @author: sky @最后修改人： sky
     * @最后修改日期:2015-7-31 下午6:04:21 expandLeftMenu void
     */
    public void expandLeftMenu() {
        if (null != updateTips) {
            latestVersion = MySharePerfenceUtil.getInstance().getLatestVersion();
            if (latestVersion > MyApplication.versionCode) {
                updateTips.setVisibility(View.VISIBLE);
            } else {
                updateTips.setVisibility(View.GONE);
            }
        }

        // TODO 更新用户信息
//        WeLearnApi.getUserInfoFromServer(getActivity(), new HttpListener() {
//            @Override
//            public void onSuccess(int code, String dataJson, String errMsg) {
//                updateLeftUI();
//            }
//
//            @Override
//            public void onFail(int HttpCode) {
//            }
//        });
    }

}
