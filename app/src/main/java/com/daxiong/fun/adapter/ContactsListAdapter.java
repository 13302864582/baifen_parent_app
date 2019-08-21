package com.daxiong.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.ContactListRowView;
import com.daxiong.fun.model.ContactsModel;
import com.daxiong.fun.model.UserInfoModel;

public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

    public static final String TAG = ContactsListAdapter.class.getSimpleName();
    private ContactsModel mContactsModel = null;
    private Context mContext = null;

    public ContactsListAdapter(Context context) {
        this.mContext = context;
    }

    public void setContactsModel(ContactsModel contactsModel) {
        this.mContactsModel = contactsModel;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContactsModel != null ? mContactsModel.getContactsCount() : 0;
    }

    protected void bindSectionHeader(View view, int position, String secheader,
                                     boolean displaySectionHeader) {

        if (displaySectionHeader) {
            view.findViewById(R.id.header).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rowSegLine).setVisibility(View.GONE);
            TextView SectionTitle = (TextView) view.findViewById(R.id.header)
                    .findViewById(R.id.headerText);
            SectionTitle.setText(secheader);
        } else {
            view.findViewById(R.id.header).setVisibility(View.GONE);
            view.findViewById(R.id.rowSegLine).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewgroup) {
        int pos = position;
        int count = mContactsModel.getContactsCount();
        if (count <= position) {
            pos = count - 1;
        }
        ContactListRowView row = null;
        String secheader2 = "";
        UserInfoModel contact = mContactsModel.getItem(pos);// item.getContactInfo();
        String secheader = contact.getNamepinyin();
        if (pos - 1 > -1) {
            secheader2 = mContactsModel.getItem(pos - 1).getNamepinyin();
        }
//		boolean flag = false;
//		if (secheader != null) {
//			if (!secheader.matches("[A-Z]")) {
//				secheader = "#";
//			}
//			flag = mContactsModel.checkFirstOfSection(pos);
//		}

        if (view == null) {
            row = new ContactListRowView(mContext, contact);
        } else {
            row = (ContactListRowView) view;
            row.setContactGson(contact);
        }
        if (secheader != null) {
            if ("".equals(secheader)|secheader2.equals(secheader)) {
                bindSectionHeader(row, pos, secheader, false);
            } else {
                bindSectionHeader(row, pos, secheader, true);
            }

        }

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
