
package com.daxiong.fun.function.partner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.glide.BlurTransformation;


/**
 * 此类的描述： 学伴详细信息
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月6日 下午2:32:19
 */
public class StudentInfoActivityNew extends BaseActivity implements OnClickListener, HttpListener {

    public static final String TAG = StudentInfoActivityNew.class.getSimpleName();

    private UserInfoModel uInfo = null;



    private int target_user_id;
    

    private ImageView mTec_info_bg_iv;
    private ImageView mIv_back;
    private ImageView mTec_info_head_iv;
    private TextView mTec_jiangjie;
    private TextView mTec_info_nick_tv;
    private TextView mTec_info_sex_tv;
    private TextView mUserid_tv_tec_cen;


    private TextView mTv_diqu;

    private TextView mTv_xuexiao;

    private TextView mTv_nianji;

    private Button mCommunicate_btn;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_student_info);



        initView();


        Intent intent = getIntent();
        target_user_id = intent.getIntExtra("userid", 0);

        if (target_user_id == 0) {
            ToastUtils.show(R.string.userid_error);
            finish();
        }

        updateUiInfo();

        WeLearnApi.getContactInfo(this, target_user_id, this);
    }

    public void initView() {
        mTec_info_bg_iv = (ImageView) findViewById(R.id.tec_info_bg_iv);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTec_info_head_iv = (ImageView) findViewById(R.id.tec_info_head_iv);
        mTec_jiangjie = (TextView) findViewById(R.id.tec_jiangjie);
        mTec_info_nick_tv = (TextView) findViewById(R.id.tec_info_nick_tv);
        mTec_info_sex_tv = (TextView) findViewById(R.id.tec_info_sex_tv);
        mUserid_tv_tec_cen = (TextView) findViewById(R.id.userid_tv_tec_cen);

        mTv_diqu = (TextView) findViewById(R.id.tv_diqu);

        mTv_xuexiao = (TextView) findViewById(R.id.tv_xuexiao);

        mTv_nianji = (TextView) findViewById(R.id.tv_nianji);

        mCommunicate_btn = (Button) findViewById(R.id.communicate_btn);
        mIv_back.setOnClickListener(this);
        mCommunicate_btn.setOnClickListener(this);
        mTec_jiangjie.setOnClickListener(this);
    }

    private void updateUiInfo() {

        uInfo = DBHelper.getInstance().getWeLearnDB().queryContactInfoById(target_user_id);

        if (null == uInfo) {
            LogUtils.e(TAG, "Contact info gson is null !");
            return;
        }

        int relationtype = uInfo.getRelation();
        if (relationtype == GlobalContant.ATTENTION) {
            mTec_jiangjie.setText(R.string.contact_unfocus);
        } else {
            mTec_jiangjie.setText(R.string.contact_focus);
        }




        Glide.with(StudentInfoActivityNew.this).load(uInfo.getAvatar_100())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(StudentInfoActivityNew.this))
                .placeholder(R.drawable.default_icon_circle_avatar).into(mTec_info_head_iv);

        Glide.with(StudentInfoActivityNew.this)
                .load(uInfo.getAvatar_100())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(StudentInfoActivityNew.this, 100))
                .placeholder(R.drawable.mohubg)
                .into(mTec_info_bg_iv);
        // 昵称
        String name = TextUtils.isEmpty(uInfo.getName()) ? getString(R.string.persion_info)
                : uInfo.getName();
        mTec_info_nick_tv.setText(name);
        mUserid_tv_tec_cen.setText("学号："+uInfo.getUserid());

        String grade = TextUtils.isEmpty(uInfo.getGrade()) ? "" : uInfo.getGrade();

        mTv_diqu.setText(uInfo.getProvince() + " " + uInfo.getCity());
        mTv_xuexiao.setText( uInfo.getSchools());
        mTv_nianji.setText(grade);
        int sex = uInfo.getSex();
        switch (sex) {
            case GlobalContant.SEX_TYPE_MAN:
                mTec_info_sex_tv.setText("男");
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                mTec_info_sex_tv.setText("女");
                break;

            default:
                mTec_info_sex_tv.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tec_jiangjie:
                MobclickAgent.onEvent(this, "AddContact");
                if (null == uInfo) {
                    return;
                }
                int relationtype = uInfo.getRelation();
                if (relationtype == GlobalContant.UNATTENTION
                        || relationtype == GlobalContant.STUTOTEA) {
                    MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_ATTENTION);
                    WeLearnApi.addFriend(this, target_user_id, new HttpListener() {
                        @Override
                        public void onSuccess(int code, String dataJson, String errMsg) {
                            WeLearnApi.getContactInfo(StudentInfoActivityNew.this, target_user_id,
                                    StudentInfoActivityNew.this);
                        }

                        @Override
                        public void onFail(int HttpCode,String errMsg) {

                        }
                    });
                } else if (relationtype == GlobalContant.ATTENTION
                        || relationtype == GlobalContant.TEATOSTU) {
                    MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_UNATTENTION);
                    WeLearnApi.deleteFriend(this, target_user_id, new HttpListener() {
                        @Override
                        public void onSuccess(int code, String dataJson, String errMsg) {
                            WeLearnApi.getContactInfo(StudentInfoActivityNew.this, target_user_id,
                                    StudentInfoActivityNew.this);
                        }

                        @Override
                        public void onFail(int HttpCode,String errMsg) {

                        }
                    });
                }
                break;
            case R.id.communicate_btn:
                if (null != uInfo && uInfo.getRoleid() != 0 && uInfo.getUserid() != 0) {
                    Bundle data = new Bundle();
                    data.putInt("userid", uInfo.getUserid());
                    data.putInt("roleid", uInfo.getRoleid());
                    data.putString("username", uInfo.getName());
                    MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_CHAT);
                    IntentManager.goToChatListView(this, data, false);
                }
                break;
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            if (TextUtils.isEmpty(dataJson)) {
                return;
            }
            UserInfoModel uInfo = null;
            try {

                uInfo = JSON.parseObject(dataJson, UserInfoModel.class);
            } catch (Exception e) {

            }
            if (null == uInfo) {
                return;
            }
            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(uInfo);

            DBHelper.getInstance().getWeLearnDB().insertorUpdate(uInfo.getUserid(),
                    uInfo.getRoleid(), uInfo.getName(), uInfo.getAvatar_100());

            updateUiInfo();
        } else {
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            }
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {

    }
}
