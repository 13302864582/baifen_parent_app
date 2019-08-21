package com.daxiong.fun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment;
import com.daxiong.fun.util.JsonUtil;

import org.json.JSONArray;

public class AnswerDetailAdapter extends FragmentStatePagerAdapter {

	private String mJsonStr;
	private Boolean mIsQpad;
	private final SparseArray<OneQuestionMoreAnswersDetailItemFragment> mFragmentList = new SparseArray<OneQuestionMoreAnswersDetailItemFragment>();
	
	private int goTag=-1;
	

    public void setGoTag(int goTag) {
        this.goTag = goTag;
    }

    public void setData(String jsonStr, boolean isQpad) {
		this.mJsonStr = jsonStr;
		this.mIsQpad = isQpad;
		notifyDataSetChanged();
	}

	public AnswerDetailAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		OneQuestionMoreAnswersDetailItemFragment fragment = OneQuestionMoreAnswersDetailItemFragment.newInstance(
				position, mJsonStr, mIsQpad);
		fragment.setGoTag(goTag);
		mFragmentList.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentList.remove(position);
	}

	public OneQuestionMoreAnswersDetailItemFragment getFragment(int pos) {
		OneQuestionMoreAnswersDetailItemFragment fragment = null;
		fragment = mFragmentList.get(pos);
		return fragment;
	}

	@Override
	public int getCount() {
		JSONArray answerArray = JsonUtil.getJSONArray(mJsonStr, "answer", new JSONArray());
		return answerArray.length() + 1;
	}
}
