
package com.daxiong.fun.function.question.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.question.model.AnswerListItemModel;
import com.daxiong.fun.view.MyCommonListItemView;

import java.util.ArrayList;

/**
 * 此类的描述： 我的提问adapter
 * 
 * @author: sky
 */
public class MyQuestionAdapter extends BaseAdapter {

    private Activity mActivity;

    private ArrayList<AnswerListItemModel> mList;

    private boolean isScrolling;

    private int goTag = -1;

    public void setGoTag(int goTag) {
        this.goTag = goTag;
    }

    public void setScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    public void setData(ArrayList<AnswerListItemModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public MyQuestionAdapter(Activity context) {
        super();
        this.mActivity = context;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int postion) {
        return postion;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup container) {
        MyCommonListItemView itemView = null;
        if (convertView == null) {
            itemView = new MyCommonListItemView(mActivity);
        } else {
            itemView = (MyCommonListItemView)convertView;
        }
        ImageView iv_collect = (ImageView)itemView
                .findViewById(R.id.collection_icon_iv_answerlistitem);
        iv_collect.setVisibility(View.GONE);
        if (mList.size() > postion) {
            AnswerListItemModel itemModel = mList.get(postion);
            itemView.showData(itemModel, isScrolling);
            itemView.setGoTag(goTag);
        }
        return itemView;
    }
}
