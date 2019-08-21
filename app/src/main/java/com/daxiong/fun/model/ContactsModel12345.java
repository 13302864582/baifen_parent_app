package com.daxiong.fun.model;

import android.annotation.SuppressLint;

import com.daxiong.fun.util.NameUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ContactsModel12345 extends BaseModel {
	public static final String TAG = ContactsModel12345.class.getSimpleName();

	private UserInfoModel mUserInfo = null;
	private List<SortContactModel> mContants;
	private SectionComparator mComparator = null;

	public ContactsModel12345() {
		mContants = new LinkedList<SortContactModel>();
		mComparator = new SectionComparator();
	}

	public UserInfoModel getmUserInfo() {
		return mUserInfo;
	}

	public void setmUserInfo(UserInfoModel mUserInfo) {
		if (mUserInfo != null)
			this.mUserInfo = mUserInfo;
	}

	@SuppressLint("DefaultLocale")
	public void push_back(UserInfoModel contact) {
		String sortString = NameUtil.getFirstLetter(contact.getName());

		SortContactModel model = new SortContactModel();

		if (sortString.matches("[A-Z]")) {
			model.setSortLetters(sortString.toUpperCase());
			// model.setSortLetters(sortString);
		} else {
			model.setSortLetters("#");
		}
		model.setContactInfo(contact);
		mContants.add(model);
		Collections.sort(mContants, mComparator);
	}

	public int getContactsCount() {
		return mContants.size();
	}

	public SortContactModel getItem(int position) {
		return mContants.get(position);
	}

	public void clearnContactsList() {
		if (mContants != null) {
			mContants.clear();
		}
	}

	/**
	 * get first char ascii of name with postion
	 */
	public int getContactsSectionForPosition(int position) {
		if (position >= mContants.size())
			return -1;
		else
			return mContants.get(position).getSortLetters().charAt(0);
	}

	/**
	 * get postion with the char ascii of name
	 */
	@SuppressLint("DefaultLocale")
	public int getContactsPositionForSection(int section) {
		for (int i = 0; i < mContants.size(); i++) {
			String sortStr = mContants.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public boolean checkFirstOfSection(int position) {
		int section = getContactsSectionForPosition(position);
		return (getContactsPositionForSection(section) == position);
	}

	public boolean checkEndOfSection(int position) {

		int section = getContactsSectionForPosition(position);
		if (section == -1)
			return true;

		int position_pos = position;

		while (section == getContactsSectionForPosition(position_pos)) {
			if (position_pos == mContants.size() - 1)
				return true;
			position_pos++;
		}

		if (position == (position_pos - 1))
			return true;
		else
			return false;
	}

	// private void filterData(String filterStr) {
	// List<SortContactModel> filterDateList = new
	// ArrayList<SortContactModel>();
	//
	// if (TextUtils.isEmpty(filterStr)) {
	// filterDateList = mContants;
	// } else {
	// filterDateList.clear();
	// for (SortContactModel sortModel : mContants) {
	// String name = sortModel.getContactInfo().getName();
	// if (name.toUpperCase().indexOf(
	// filterStr.toString().toUpperCase()) != -1
	// || NameUtil.getFirstLetter(name).startsWith(
	// filterStr.toString().toUpperCase())) {
	// filterDateList.add(sortModel);
	// }
	// }
	// }
	//
	// Collections.sort(filterDateList, mComparator);
	// }

}

// public class ContactsModel extends Model{
// public static final String TAG = ContactsModel.class.getSimpleName();
//
// public ContactsModel() {
// mContants = new LinkedList<Pair<String, List<ContactInfoGson>>>();
// }
//
// public ContactInfoGson getmUserInfo() {
// return mUserInfo;
// }
//
// public void setmUserInfo(ContactInfoGson mUserInfo) {
// if (mUserInfo != null)
// this.mUserInfo = mUserInfo;
// }
//
// public void push_back(ContactInfoGson contact) {
// String firstNameLetter = NameUtil.getFirstLetter(contact.getName());
// List<ContactInfoGson> contacts = findContants(firstNameLetter);
// if (contacts != null)
// contacts.add(contact);
// }
//
// private List<ContactInfoGson> findContants(String key) {
// int i = 0;
// if (key == null)
// return null;
//
// for (Pair<String, List<ContactInfoGson>> contactPair : mContants) {
// if (contactPair.first.equals(key)) {
// return contactPair.second;
// }
// }
//
// Pair<String, List<ContactInfoGson>> contantPair = new Pair<String,
// List<ContactInfoGson>>(key, new LinkedList<ContactInfoGson>());
// for (i = 0; i < mContants.size(); ++i) {
// String contantKey = ((Pair<String, List<ContactInfoGson>>)
// mContants.get(i)).first;
// if (contantKey.charAt(0) > key.charAt(0))
// break;
// }
// mContants.add(i, contantPair);
// return contantPair.second;
// }
//
// public int getContactsCount() {
// int res = 0;
// for (int i = 0; i < mContants.size(); i++) {
// res += mContants.get(i).second.size();
// }
// return res;
// }
//
// public void clearnContactsList(){
// if (mContants != null){
// mContants.clear();
// }
// }
//
// public ContactInfoGson getItem(int position) {
// int c = 0;
// for (int i = 0; i < mContants.size(); i++) {
// if (position >= c && position < c + mContants.get(i).second.size()) {
// return mContants.get(i).second.get(position - c);
// }
// c += mContants.get(i).second.size();
// }
// return null;
// }
//
// public int getContactsPositionForSection(int section) {
// if (section < 0)
// section = 0;
// if (section >= mContants.size())
// section = mContants.size() - 1;
// int c = 0;
// for (int i = 0; i < mContants.size(); i++) {
// if (section == i) {
// return c;
// }
// c += mContants.get(i).second.size();
// }
// return 0;
// }
//
// public int getContactsSectionForPosition(int position) {
// int c = 0;
// for (int i = 0; i < mContants.size(); i++) {
// if (position >= c && position < c + mContants.get(i).second.size()) {
// return i;
// }
// c += mContants.get(i).second.size();
// }
// return -1;
// }
//
// public boolean isTheEndOfSection(int position){
// int c = 0;
// for (int i = 0; i < mContants.size(); i++) {
// if (position >= c && position == c + mContants.get(i).second.size()-1) {
// return true;
// }
// c += mContants.get(i).second.size();
// }
// return false;
// }
//
// public String[] getContactsSections() {
// String[] res = new String[mContants.size()];
// for (int i = 0; i < mContants.size(); i++) {
// res[i] = mContants.get(i).first;
// }
// return res;
// }
//
// private ContactInfoGson mUserInfo;
// private List<Pair<String, List<ContactInfoGson>>> mContants;
// }
