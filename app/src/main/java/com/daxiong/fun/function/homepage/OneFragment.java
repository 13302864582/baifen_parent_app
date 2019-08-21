
package com.daxiong.fun.function.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.callback.INetWorkListener;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dispatch.HomeController;
import com.daxiong.fun.function.communicate.MessageMainListActivity;
import com.daxiong.fun.function.homepage.adapter.OneRecyclerAdapter;
import com.daxiong.fun.function.homework.PublishHwActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.model.ChatInfo;
import com.daxiong.fun.model.Firstuse;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 六个老师Fragmnet
 *
 * @author: sky
 */
public class OneFragment extends BaseFragment implements OkHttpHelper.HttpListener ,INetWorkListener {

    public static final String TAG = OneFragment.class.getSimpleName();

    private View view;

    private MainActivity activity;

    private RecyclerView my_recycler_view;
    private Button bt_jiancha;
    private TextView tv_tian, tv_quan;
    private List<Firstuse.Teacher_infos> teacher_infos=new ArrayList<>(), list;



    private RelativeLayout backLayout;
    private RelativeLayout titleLayout;
    public ImageView iv_choose;
    private RelativeLayout nextLayout;
    private TextView tv_red;
    private static boolean isDoInDB;
    private static long reflashTime;
    private List<ChatInfo> infos;
    private int currentIndex = 0;
    public static View unReadMsgPointIv;
    private HomeController homeController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        initView(view);
        initListener();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        OkHttpHelper.post(activity, "parents", "firstuse", null, this);

    }

    @Override
    public void initView(View view) {

        // 左边的按钮
        backLayout = (RelativeLayout) view.findViewById(R.id.back_layout);
        ImageView backImageView = (ImageView) view.findViewById(R.id.back_iv);
        backImageView.setScaleType(ImageView.ScaleType.CENTER);
        // backImageView.setImageResource(R.drawable.icon_stu_home_menu_persion_btn_selector);
        backLayout.setVisibility(View.GONE);
        //右边按钮
        nextLayout = (RelativeLayout)view. findViewById(R.id.next_setp_layout);
        nextLayout.setVisibility(View.VISIBLE);
        ImageView nextImageView = (ImageView) view.findViewById(R.id.next_step_img);
        nextImageView.setVisibility(View.VISIBLE);
        nextImageView.setBackgroundResource(R.drawable.form_message_btn_normal);
        unReadMsgPointIv = view.findViewById(R.id.unread_msg_point_iv_main);
        tv_red = (TextView)view.findViewById(R.id.tv_red);
        //中间的部分
        titleLayout = (RelativeLayout) view.findViewById(R.id.title_layout);
        iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
        iv_choose.setVisibility(View.GONE);


        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        bt_jiancha = (Button) view.findViewById(R.id.bt_jiancha);
        tv_quan = (TextView) view.findViewById(R.id.tv_quan);
        tv_tian = (TextView) view.findViewById(R.id.tv_tian);
        GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 3);
        my_recycler_view.setLayoutManager(mLayoutManager);
        my_recycler_view.setHasFixedSize(true);

    }


    @Override
    public void initListener() {
        super.initListener();
        nextLayout.setOnClickListener(this);
        bt_jiancha.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (homeController == null) {
            homeController = new HomeController(null, this);
        }
        showMessageList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_setp_layout:// 右边按钮
                MobclickAgent.onEvent(getActivity(), "Open_ChatList");
                Intent intent = new Intent(getActivity(), MessageMainListActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_jiancha:
                Intent hwintent = new Intent(activity, PublishHwActivity.class);
                startActivity(hwintent);
                break;
        }
    }


    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            if(TextUtils.isEmpty(dataJson)){
                return;
            }
            Firstuse firstuse = new Gson().fromJson(dataJson, Firstuse.class);
            teacher_infos = firstuse.getTeacher_infos();

            int anInt = new Random().nextInt(6);
            int anInt2 = (anInt + 3) % 6;
            if (firstuse.getIsbusy() == 0) {
                int anInt3 = (anInt + 5) % 6;
                teacher_infos.get(anInt).setType(1);
                teacher_infos.get(anInt2).setType(2);
                teacher_infos.get(anInt3).setType(2);
            } else {
                teacher_infos.get(anInt).setType(1);
                teacher_infos.get(anInt2).setType(2);
            }


            tv_quan.setText(firstuse.getHwcoupon()+"");
            tv_tian.setText(firstuse.getCouponed()+"");
            OneRecyclerAdapter oneAdapter = new OneRecyclerAdapter(activity, teacher_infos, firstuse.getUncheck(), firstuse.getChecking(),firstuse.getChecked());
            my_recycler_view.setAdapter(oneAdapter);
        } else {
            ToastUtils.show(errMsg);
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {
        ToastUtils.show(errMsg);
    }





    private void showMessageList() {
        new MyAsyncTask() {
            @Override
            public void preTask() {

            }

            @Override
            public void postTask() {

                if (infos != null && infos.size() > 0) {
                    for (int i = 0; i < infos.size(); i++) {
                        final ChatInfo chat = infos.get(i);
                        boolean successed = queryAndSetUserData(chat);
                        if (!successed) {
                            currentIndex = i;
                            break;
                        }

                    }
                }
                int count = 0;
                for (ChatInfo info : infos) {
                    count += info.getUnReadCount();
                }
                String str = count + "";
                if (count > 99) {
                    str = "99+";
                }
                tv_red.setVisibility(View.VISIBLE);
               // tv_red.setText(str);
                if (count == 0) {
                    tv_red.setText("");
                    tv_red.setVisibility(View.GONE);
                }

            }

            @Override
            public void doInBack() {
                if (!isDoInDB) {
                    isDoInDB = true;
                    MainActivity.isShowPoint = false;
                    infos = DBHelper.getInstance().getWeLearnDB().queryMessageList();
                    isDoInDB = false;
                }
            }
        }.excute();
    }



    private boolean queryAndSetUserData(ChatInfo chat) {
        UserInfoModel user = DBHelper.getInstance().getWeLearnDB().queryByUserId(chat.getFromuser(), true);

        boolean flag = true;
        if (user != null) {
            chat.setUser(user);
            // mAdapter.setData(infos);
            flag = true;
        } else {
            flag = false;

            WeLearnApi.getContactInfo(getActivity(), chat.getFromuser(), new OkHttpHelper.HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        UserInfoModel user = JSON.parseObject(dataJson, UserInfoModel.class);

                        DBHelper.getInstance().getWeLearnDB().insertorUpdate(user.getUserid(), user.getRoleid(),
                                user.getName(), user.getAvatar_100());

                        if (currentIndex < infos.size()) {
                            LogUtils.i(TAG, user.toString());
                            infos.get(currentIndex).setUser(user);
                        }
                        setUser();

                    } else {
                        // ToastUtils.show(mActivity, errMsg +"----是我弹的");
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                }
            });
        }
        return flag;
    }

    private void setUser() {
        currentIndex++;
        if (currentIndex < infos.size()) {
            ChatInfo chat = infos.get(currentIndex);
            boolean successed = queryAndSetUserData(chat);
            if (successed) {
                setUser();
            }
        }
    }


    @Override
    public void onPre() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onException() {
        // TODO Auto-generated method stub

    }




    @Override
    public void onAfter(String jsonStr, int msgDef) {
        switch (msgDef){
            /*case MessageConstant.MSG_DEF_HOME_LIST:
                String dataJson = JsonUtil.getString(jsonStr, "data", "");
                String dataJson2 = JsonUtil.getString(dataJson, "content", "");
                if (!"".equals(dataJson2)) {
                    HomeListModel parseObject = JSON.parseObject(dataJson2, HomeListModel.class);

                    for (int i = 0; i < list.size(); i++) {
                        if (parseObject.getCreate_time() == list.get(i).getCreate_time()) {
                            HomeListModel remove = list.remove(i);
                            if ( parseObject.getHomework_state() == 9) {
                            } else {
                                list.add(i, parseObject);

                            }
                            break;
                        }
                    }
                    updateListUI();
                }
                break;*/
            case MessageConstant.MSG_DEF_MSGS:
                showMessageList();
                reflashTime = System.currentTimeMillis();// 更新消息收到的时间
                break;
        }




    }


    @Override
    public void onDisConnect() {
        // TODO Auto-generated method stub

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeController != null) {
            homeController.removeMsgInQueue();
        }

    }
}















