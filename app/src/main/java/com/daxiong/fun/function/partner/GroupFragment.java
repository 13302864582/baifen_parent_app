
package com.daxiong.fun.function.partner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.ContactsListAdapter;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.partner.SideBar.OnTouchingLetterChangedListener;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ContactsModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.ThreadPoolUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements HttpListener, OnItemClickListener {

    private static final int REFLASH_TIME = 2000;

    private ListView mContactsListView;

    private ContactsListAdapter mContactsListAdapter;

    private View mView;
    private TextView tv_meiyou;
    private FrameLayout fl;

    private Activity mActivity;

    public static final String TAG = GroupFragment.class.getSimpleName();

    private ContactsModel mContactsModel;

    private static long reflashTime;

    private boolean isClearn;

    private SideBar sideBar;

    private TextView dialog;

    private boolean isThisFragmentSelected = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void createContactListModel() {
        if (mContactsModel == null) {
            mContactsModel = new ContactsModel();
        }

        new MyAsyncTask() {
            List<UserInfoModel> infos;

            @Override
            public void preTask() {

            }

            @Override
            public void postTask() {
                if (infos != null && infos.size() > 0) {
                    if (mContactsModel != null && mContactsModel.getContactsCount() > 0) {
                        mContactsModel.clearnContactsList();
                    }
                    int sum=0;
                    for (int i = 0; i < infos.size(); i++) {
                        UserInfoModel userInfoModel = infos.remove(i);
                        if ( "".equals(userInfoModel.getNamepinyin())) {
                            sum++;
                            infos.add(0, userInfoModel);
                        } else {
                            infos.add(i, userInfoModel);
                        }
                    }
//                    if(infos.size()-sum>0){
//                        String[] b =new String[infos.size()-sum];
//                        for (int i = sum; i < infos.size(); i++) {
//                            UserInfoModel userInfoModel = infos.get(i);
//                            if ( "".equals(userInfoModel.getNamepinyin())) {
//
//                            } else {
//                                b[i-sum]=userInfoModel.getNamepinyin();
//                            }
//                        }
//                        SideBar.setB(b);
//                        sideBar.postInvalidate();
//                    }

                    mContactsModel.setContactList(infos);
                }
                if (infos.size() == 0) {
                    tv_meiyou.setVisibility(View.VISIBLE);
                    fl.setVisibility(View.GONE);
                } else {
                    tv_meiyou.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                }
                if (mContactsListView.getAdapter() == null) {
                    mContactsListView.setAdapter(mContactsListAdapter);
                }
                mContactsListAdapter.setContactsModel(mContactsModel);

                if (System.currentTimeMillis() - reflashTime >= 5000) {
                    WeLearnApi.getContactsList(GroupFragment.this.getActivity(),
                            GroupFragment.this);
                    reflashTime = System.currentTimeMillis();
                }
            }

            @Override
            public void doInBack() {
                infos = DBHelper.getInstance().getWeLearnDB().queryAllContactInfo();
            }
        }.excute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_group, container, false);

        mContactsListView = (ListView) mView.findViewById(R.id.contactsList);
        tv_meiyou = (TextView) mView.findViewById(R.id.tv_meiyou);
        fl = (FrameLayout) mView.findViewById(R.id.fl);

        if (mContactsModel == null) {
            mContactsModel = new ContactsModel();
        }
        mContactsListAdapter = new ContactsListAdapter(mActivity);

        sideBar = (SideBar) mView.findViewById(R.id.sidrbar);
        dialog = (TextView) mView.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                int position = mContactsListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mContactsListView.setSelectionFromTop(position, 0);
                }
            }
        });

        mContactsListView.setOnItemClickListener(this);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        createContactListModel();
        mContactsListView.setVisibility(View.VISIBLE);
        if (System.currentTimeMillis() - reflashTime >= REFLASH_TIME && isThisFragmentSelected) {
            // if (Config.DEBUG) {
            // ToastUtils.show("Get Contact List");
            // }
            WeLearnApi.getContactsList(getActivity(), GroupFragment.this);
            reflashTime = System.currentTimeMillis();
        }
        MobclickAgent.onEventBegin(mActivity, EventConstant.CUSTOM_EVENT_GROUP);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(mActivity, EventConstant.CUSTOM_EVENT_GROUP);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfoModel contact = mContactsModel.getItem(position);
        if (contact == null) {
            return;
        }
        Bundle data = new Bundle();
        data.putInt("userid", contact.getUserid());
        data.putInt("roleid", contact.getRoleid());
        if (contact.getRoleid() == 1 | contact.getRoleid() == 3) {
            IntentManager.goToStudentInfoView(mActivity, data);
        } else if (contact.getRoleid() == 2 | contact.getRoleid() == 4 | contact.getRoleid() == 10) {
            IntentManager.goToTeacherInfoView(mActivity, data);
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {

            JSONArray contactsJsonArray = null;
            try {
                contactsJsonArray = new JSONArray(dataJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (contactsJsonArray == null) {
                if (mContactsListView.getAdapter() == null) {
                    mContactsListView.setAdapter(mContactsListAdapter);
                }

                mContactsListAdapter.setContactsModel(mContactsModel);
                return;
            }
            isClearn = false;
            if (mContactsModel != null && mContactsModel.getContactsCount() > 0) {
                isClearn = true;
                mContactsModel.clearnContactsList();
            }

            final List<UserInfoModel> records = new Gson().fromJson(contactsJsonArray.toString(),
                    new TypeToken<ArrayList<UserInfoModel>>() {
                    }.getType());
            List<UserInfoModel> contactlist = new ArrayList<UserInfoModel>(records);
            for (int i = 0; i < contactlist.size(); i++) {
                UserInfoModel userInfoModel = contactlist.remove(i);
                if ( "".equals(userInfoModel.getNamepinyin())) {
                    contactlist.add(0, userInfoModel);
                } else {
                    contactlist.add(i, userInfoModel);
                }
            }
//            int sum=0;
//            if(contactlist.size()-sum>0){
//                String[] b =new String[contactlist.size()-sum];
//                for (int i = sum; i < contactlist.size(); i++) {
//                    UserInfoModel userInfoModel = contactlist.get(i);
//                    if ( "".equals(userInfoModel.getNamepinyin())) {
//
//                    } else {
//                        b[i-sum]=userInfoModel.getNamepinyin();
//                    }
//                }
//                SideBar.setB(b);
//                sideBar.postInvalidate();
//            }

            mContactsModel.setContactList(contactlist);
            if (contactlist.size() == 0) {
                tv_meiyou.setVisibility(View.VISIBLE);
                fl.setVisibility(View.GONE);
            } else {
                tv_meiyou.setVisibility(View.GONE);
                fl.setVisibility(View.VISIBLE);
            }
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    if (isClearn) {
                        DBHelper.getInstance().getWeLearnDB().clearContactUserInfo();
                    }
                    if (records.size() > 0) {
                        for (UserInfoModel u : records) {
                            DBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(u);
                        }
                    }
                }
            });

            if (mContactsListView.getAdapter() == null) {
                mContactsListView.setAdapter(mContactsListAdapter);
            }
            mContactsListAdapter.setContactsModel(mContactsModel);
        } else {
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            }
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {

    }

    public void onSelected(boolean isSelected) {
        isThisFragmentSelected = isSelected;
    }

}
