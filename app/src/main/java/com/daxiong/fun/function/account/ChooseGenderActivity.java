package com.daxiong.fun.function.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.model.GenderModel;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.UploadManager2;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.okhttp.callback.StringCallback;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

import static com.daxiong.fun.R.id.back_layout;

/**
 * 家长端 选择性别界面
 *
 * @author Administrator
 */
public class ChooseGenderActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final String TAG = ChooseGenderActivity.class.getSimpleName();

    @Bind(back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.next_setp_layout)
    RelativeLayout nextSetpLayout;
    @Bind(R.id.next_step_btn)
    TextView next_step_btn;
    @Bind(R.id.listview)
    ListView listview;


    private int chooseGender;
    private MyAdapter myAdapter;
    private List<GenderModel> genderList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gender);
        ButterKnife.bind(this);
        initGenderData();
        initView();
        initListener();
    }


    private void initGenderData() {
        genderList = new ArrayList<GenderModel>();
        GenderModel model = new GenderModel(1, "男", 1);
        GenderModel model2 = new GenderModel(2, "女", 2);
        genderList.add(model);
        genderList.add(model2);
    }


    @Override
    public void initView() {
        super.initView();
        nextSetpLayout.setVisibility(View.VISIBLE);
        next_step_btn.setVisibility(View.GONE);
        next_step_btn.setText("完成");
        setWelearnTitle(R.string.gender);
        Intent intent = getIntent();
        myAdapter = new MyAdapter(this, genderList);
        listview.setAdapter(myAdapter);
        chooseGender = intent.getIntExtra("genderid", -1);
        myAdapter.setChooseGender(chooseGender);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void initListener() {
        super.initListener();
        backLayout.setOnClickListener(this);
        nextSetpLayout.setOnClickListener(this);
        listview.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.next_setp_layout://确定按钮
                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (chooseGender == -1) {
            ToastUtils.show(R.string.text_toast_select_gender);
        } else {

            GenderModel genderModel = genderList.get(position);
            chooseGender = genderModel.getGenderId();
            myAdapter.setChooseGender(chooseGender);
            myAdapter.notifyDataSetChanged();


            showDialog("正在提交...");
            try {
                JSONObject updata_data = new JSONObject();
                updata_data.put("sex", chooseGender);
                UploadManager2.upload(AppConfig.GO_URL + "parents/upuserinfos", RequestParamUtils.getMapParam(updata_data),
                        null, new MyCallback(), true, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    class MyCallback extends StringCallback {
        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            // ToastUtils.show("onBefore");
        }


        @Override
        public void onAfter() {
            super.onAfter();
            // ToastUtils.show("onAfter");

        }

        @Override
        public void onResponse(String response) {
            closeDialog();
            int code = JsonUtil.getInt(response, "Code", -1);
            String msg = JsonUtil.getString(response, "Msg", "");
            final String responseStr = JsonUtil.getString(response, "Data", "");
            if (code == 0) {
                UserInfoModel uInfo = JSON.parseObject(responseStr, UserInfoModel.class);
                DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);

                ToastUtils.show(R.string.modifyinfosuccessful);
                Intent intentxxx = new Intent();
                intentxxx.putExtra("genderid", chooseGender);
                setResult(RESULT_OK, intentxxx);
                finish();

            } else {
                ToastUtils.show(msg);
            }

        }

        @Override
        public void onError(Call call, Exception e) {
            closeDialog();
            String errorMsg = "";
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                errorMsg = e.getMessage();
            } else {
                errorMsg = e.getClass().getSimpleName();
            }
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("onError:" + errorMsg);
            } else {
                ToastUtils.show("网络异常");
            }

        }
    }


    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<GenderModel> genderList;

        private int genderIndex = 0;

        private void setChooseGender(int chooseGender) {
            genderIndex = chooseGender;
        }

        public MyAdapter(Context context, List<GenderModel> genderList) {
            this.context = context;
            this.genderList = genderList;
        }

        @Override
        public int getCount() {
            return genderList.size();
        }

        @Override
        public Object getItem(int position) {
            return genderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.activity_choose_gender_item, null);
                TextView tv_gradename = (TextView) convertView.findViewById(R.id.tv_gender);
                ImageView iv_choice = (ImageView) convertView.findViewById(R.id.iv_arrow);
                View view_line = (View) convertView.findViewById(R.id.view_line);
                holder = new ViewHolder(tv_gradename, iv_choice, view_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            GenderModel item = (GenderModel) getItem(position);
            holder.tv_gender.setText(item.getGender());
            if (item.getChecked() == genderIndex) {
                holder.imageview.setVisibility(View.VISIBLE);
            } else {
                holder.imageview.setVisibility(View.GONE);
            }
            if (position == genderList.size() - 1) {
                holder.view_line.setVisibility(View.GONE);
            } else {
                holder.view_line.setVisibility(View.VISIBLE);
            }


            return convertView;
        }

        final class ViewHolder {
            TextView tv_gender;
            ImageView imageview;
            View view_line;

            public ViewHolder(TextView tv_gender, ImageView imageview, View view_line) {
                this.tv_gender = tv_gender;
                this.imageview = imageview;
                this.view_line = view_line;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


}
