
package com.daxiong.fun.function.homework;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.agent.adapter.PublisherAdapter;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.view.XListView;
import com.daxiong.fun.view.XListView.IXListViewListener;

import java.util.List;

/**
 * 此类的描述：选择发题者 
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2015-7-27 下午6:34:59
 * @version: 2.0
 */
public class ChoicePublisherActivity extends BaseActivity implements IXListViewListener,
        OnItemClickListener {

    private XListView listview;

    private ImageView iv_left;

    private TextView tv_right;

    private TextView tv_title;

    private Handler mHandler = null;

    private int pageIndex = 0;

    private int pageSize = 10;

    private int offset = 0;


    private PublisherAdapter publisherAdapter;
    private List<UserInfoModel> publisherList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_publisher_activity);
        getExtraData();
        initView();
        initListener();
        initData();
    }

    private void getExtraData() {
        Intent intent = getIntent();
        if (intent != null) {
            publisherList = (List<UserInfoModel>) intent.getSerializableExtra("publiserList");
        }
        
    }

    @Override
    public void initView() {
        super.initView();
        this.iv_left = (ImageView)this.findViewById(R.id.iv_left);
        this.tv_right = (TextView)this.findViewById(R.id.tv_right);
        this.tv_title = (TextView)this.findViewById(R.id.tv_title);
        tv_right.setVisibility(View.GONE);
        this.listview = (XListView)this.findViewById(R.id.listview);
        mHandler = new Handler();       
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        listview.setOnItemClickListener(this);

        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(this);
    }

    public void initData() {       
        publisherAdapter = new PublisherAdapter(this, publisherList);
        listview.setAdapter(publisherAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:// 返回
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        if (publisherList != null && publisherList.size() > position-1) {
            UserInfoModel entity = publisherList.get(position-1);       
            Intent intent = new Intent();
            intent.putExtra("userid", entity.getUserid());
            intent.putExtra("username", entity.getName());
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageIndex++;
                publisherList.clear();
                initData();
                publisherAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                publisherAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    // 或的数据后一定要加onLoad()方法，否则刷新会一直进行，根本停不下来
    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime("刚刚");
    }
}
