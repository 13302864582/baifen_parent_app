package com.daxiong.fun.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class ContactsModel extends BaseModel {
	public static final String TAG = ContactsModel.class.getSimpleName();

	private List<UserInfoModel> mContacts;

	public ContactsModel() {
		mContacts = new ArrayList<UserInfoModel>();
	}

	public void setContactList(List<UserInfoModel> contacts) {
		this.mContacts = contacts;
	}

	public void addContact(UserInfoModel contact) {
		this.mContacts.add(contact);
	}

	public int getContactsCount() {
		return mContacts.size();
	}

	public UserInfoModel getItem(int position) {
		if (position >= getContactsCount()) {
			return null;
		}
		return mContacts.get(position);
	}

	public void clearnContactsList() {
		if (mContacts != null) {
			mContacts.clear();
		}
	}

	/**
	 * get first char ascii of name with postion
	 */
	public int getContactsSectionForPosition(int position) {
		if (position >= mContacts.size())
			return -1;
		else {
			String namepinyin = mContacts.get(position).getNamepinyin();
			if (namepinyin != null && namepinyin.length() > 0) {
				return namepinyin.charAt(0);
			} else {
				return -1;
			}
		}
	}

	/**
	 * get postion with the char ascii of name
	 */
	@SuppressLint("DefaultLocale")
	public int getContactsPositionForSection(int section) {
		for (int i = 0; i < mContacts.size(); i++) {
            UserInfoModel userInfoModel = mContacts.get(i);
            String sortStr = userInfoModel.getNamepinyin();

			// 增加为空判断 modified by yh 2015-01-07 Start
			if (TextUtils.isEmpty(sortStr)) {
				return -1;
			}
			// 增加为空判断 modified by yh 2015-01-07 End

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
			if (position_pos == mContacts.size() - 1)
				return true;
			position_pos++;
		}

		if (position == (position_pos - 1))
			return true;
		else
			return false;
	}

}
