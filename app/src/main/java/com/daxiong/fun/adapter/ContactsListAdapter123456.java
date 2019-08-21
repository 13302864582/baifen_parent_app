package com.daxiong.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.ContactListRowView;
import com.daxiong.fun.model.ContactsModel12345;
import com.daxiong.fun.model.SortContactModel;
import com.daxiong.fun.model.UserInfoModel;

public class ContactsListAdapter123456 extends BaseAdapter implements SectionIndexer {

	public static final String TAG = ContactsListAdapter123456.class.getSimpleName();
	private ContactsModel12345 mContactsModel = null;
	private Context mContext = null;

	public ContactsListAdapter123456(Context context) {
		this.mContext = context;
	}

	public void setContactsModel(ContactsModel12345 contactsModel) {
		this.mContactsModel = contactsModel;

		// update the view
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mContactsModel != null ? mContactsModel.getContactsCount() : 0;
	}

	protected void bindSectionHeader( View view, int position,
			String secheader, boolean displaySectionHeader) {

		if (displaySectionHeader) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			TextView SectionTitle = (TextView) view.findViewById(R.id.header)
					.findViewById(R.id.headerText);
			SectionTitle.setText(secheader);
			// res.header.setVisibility(View.VISIBLE);
			// res.SectionTitle.setText(secheader);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
			// res.header.setVisibility(View.GONE);
		}
	}

	public View getView(final int position, View view, ViewGroup viewgroup) {
		SortContactModel item = null;
		//ViewHolder viewHolder = null;
		int pos = position;
		if (mContactsModel.getContactsCount()<= position) {
			pos = mContactsModel.getContactsCount() - 1;
		}
		item = mContactsModel.getItem(pos);
		String secheader = item.getSortLetters();

		ContactListRowView row =null;
		// Log.i(TAG, "The request position is : " + position);

		UserInfoModel contact = item.getContactInfo();
		boolean flag = mContactsModel.checkFirstOfSection(pos);

		if (view == null) {
			row = new ContactListRowView(mContext, contact);
		} else {
			row = (ContactListRowView) view;
			
			row.setContactGson(contact);
		}

		bindSectionHeader(row, pos, secheader, flag);

		return row;

	}

	

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return mContactsModel.getItem(position);
	}

	@Override
	public int getPositionForSection(int section) {
		return mContactsModel.getContactsPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return mContactsModel.getContactsSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}

// public class ContactsListAdapter extends GroupListAdapter {
// public static final String TAG = ContactsListAdapter.class.getSimpleName();
//
// private ContactsModel mContactsModel = null;
// private Context mContext = null;
//
// public ContactsListAdapter(Context context) {
// this.mContext = context;
// }
//
// public void setContactsModel(ContactsModel contactsModel) {
// this.mContactsModel = contactsModel;
//
// //update the view
// notifyDataSetChanged();
// }
//
// @Override
// public int getCount() {
// return mContactsModel != null ? mContactsModel.getContactsCount() : 0;
// }
//
// @Override
// public long getItemId(int position) {
// return position;
// }
//
// @Override
// public Object getItem(int position) {
// return position;
// }
//
// @Override
// protected void onNextPageRequested(int page) {
// }
//
// @Override
// protected void bindSectionHeader(View view, int position,
// boolean displaySectionHeader) {
//
// if (displaySectionHeader) {
// view.findViewById(R.id.header).setVisibility(View.VISIBLE);
// TextView lSectionTitle = (TextView)
// view.findViewById(R.id.header).findViewById(R.id.headerText);
// lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
// } else {
// view.findViewById(R.id.header).setVisibility(View.GONE);
// }
// }
//
// @Override
// public View getAmazingView(int position, View convertView,
// ViewGroup parent) {
//
//
//
// ContactListRowView row = (ContactListRowView)convertView;
//
// Log.i(TAG, "The request position is : " + position);
//
// ContactInfoGson contact = mContactsModel.getItem(position);
// boolean flag = mContactsModel.isTheEndOfSection(position);
// Log.i(TAG, "current postion:"+position + "\tflag:"+flag);
// if (row == null) {
// row = new ContactListRowView(mContext, contact,flag);
// }else {
// row.setContactGson(contact,flag);
// }
// return row;
// }
//
// @Override
// public void configurePinnedHeader(View header, int position, int alpha) {
// TextView lSectionHeader = (TextView) header;
// lSectionHeader
// .setText(getSections()[getSectionForPosition(position)]);
// lSectionHeader.setTextColor(alpha << 24 | (0x000000));
// }
//
// @Override
// public int getPositionForSection(int section) {
// return mContactsModel.getContactsPositionForSection(section);
// }
//
// @Override
// public int getSectionForPosition(int position) {
// return mContactsModel.getContactsSectionForPosition(position);
// }
//
// @Override
// public String[] getSections() {
// return mContactsModel.getContactsSections();
// }
//
// }
