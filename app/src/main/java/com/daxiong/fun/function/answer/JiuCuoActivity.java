package com.daxiong.fun.function.answer;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.model.JiuCuoModel;

/**
 * 纠错 Activity
 * @author:  sky
 */
public class JiuCuoActivity extends BaseActivity {
    
    private RelativeLayout back_layout;
    
    private JiuCuoModel jiuCuoModel;
    private NetworkImageView iv_avatar;
    private TextView tv_nick;
    private TextView tv_xuehao;
    private TextView et_content;
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.jiucuo_activity);
        initView();
        getExtraData();
        initListener();
    }
    
    
    public void getExtraData(){
        jiuCuoModel=(JiuCuoModel)getIntent().getSerializableExtra("jiucuomodel");
        if (jiuCuoModel!=null) {
            ImageLoader.getInstance().loadImage(jiuCuoModel.getAvatar_100(), iv_avatar, R.drawable.default_loading_img);
            tv_nick.setText(jiuCuoModel.getName());
            tv_xuehao.setText(jiuCuoModel.getUserid()+"");
            et_content.setText(jiuCuoModel.getContent());
            
        }
        
    }
    
    public void initView() {
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        this.iv_avatar=(NetworkImageView)findViewById(R.id.iv_avatar);
        this.tv_nick=(TextView)findViewById(R.id.tv_nick);
        this.tv_xuehao=(TextView)findViewById(R.id.tv_xuehao);
        this.et_content=(TextView)findViewById(R.id.et_content);
    }
    
    
    
    @Override
    public void initListener() {
        super.initListener();
        back_layout.setOnClickListener(this);
    }

    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout://返回
                finish();
                
                break;

            default:
                break;
        }
    }
    
}
