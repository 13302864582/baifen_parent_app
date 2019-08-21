package com.daxiong.fun.function.question.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment;
import com.daxiong.fun.util.JsonUtil;

import org.json.JSONArray;

public class QADetailAdapter extends FragmentStatePagerAdapter {

	private String mJsonStr;
	private Boolean mIsQpad;
	private final SparseArray<OneQuestionMoreAnswersDetailItemFragment> mFragmentRef = new SparseArray<OneQuestionMoreAnswersDetailItemFragment>();

	public void setData(String jsonStr, boolean isQpad) {
		this.mJsonStr = jsonStr;
		this.mIsQpad = isQpad;
		notifyDataSetChanged();
	}

	public QADetailAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		OneQuestionMoreAnswersDetailItemFragment fragment = OneQuestionMoreAnswersDetailItemFragment.newInstance(
				position, mJsonStr, mIsQpad);
		mFragmentRef.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentRef.remove(position);
	}

	public OneQuestionMoreAnswersDetailItemFragment getFragment(int pos) {
		OneQuestionMoreAnswersDetailItemFragment fragment = null;
		fragment = mFragmentRef.get(pos);
		return fragment;
	}

	@Override
	public int getCount() {
		JSONArray answerArray = JsonUtil.getJSONArray(mJsonStr, "answer", new JSONArray());
		return answerArray.length() + 1;
	}
}
