
package com.daxiong.fun.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.view.CropCircleTransformation;

public class ContactListRowView extends FrameLayout {
    private UserInfoModel mContactInfo = null;

    private View mView = null;

    private int avatarSize;

    private Context context;

    public ContactListRowView(Context context, AttributeSet attrs, UserInfoModel contact) {
        super(context, attrs);
        this.context = context;
        mContactInfo = contact;
        setupView(context);
    }

    public ContactListRowView(Context context, UserInfoModel contact) {
        super(context);
        this.context = context;
        mContactInfo = contact;
        setupView(context);
    }

    public void setContactGson(UserInfoModel contact) {
        mContactInfo = contact;
        setContactInfo(mView);
    }

    private void setContactInfo(View view) {
        ViewHolder holder = (ViewHolder)view.getTag();

        // ImageLoader.getInstance().loadImage(mContactInfo.getAvatar_100(),
        // holder.contactImage,
        // R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);

        Glide.with(context).load(mContactInfo.getAvatar_100()).placeholder(R.drawable.default_icon_circle_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.contactImage);

        holder.contactName.setText(mContactInfo.getName());
        int sexId = R.string.sextype_unknown;
        switch (mContactInfo.getSex()) {
            case GlobalContant.SEX_TYPE_MAN:
                sexId = R.string.sextype_man;
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                sexId = R.string.sextype_women;
                break;
        }

        if (mContactInfo.getRoleid() == GlobalContant.ROLE_ID_STUDENT) {// 学生
            String grade = mContactInfo.getGrade() == null ? "" : mContactInfo.getGrade();
            holder.extraInfo.setText(context.getString(R.string.contact_list_extraInfo,
                    context.getString(sexId), mContactInfo.getSchools(), grade));
            holder.techSchoolInfo.setText("");
        } else if (mContactInfo.getRoleid() == GlobalContant.ROLE_ID_COLLEAGE) {// 老师
            String major = mContactInfo.getMajor() == null ? "" : mContactInfo.getMajor();
            holder.extraInfo.setText(context.getString(R.string.contact_list_extraInfo,
                    context.getString(sexId), mContactInfo.getSchools(), major));
            holder.techSchoolInfo.setText("");
        }
        if (mContactInfo.getSupervip() == 0) {
            holder.viptag.setVisibility(View.GONE);
        } else if (mContactInfo.getSupervip() > 0) {
//            holder.viptag.setVisibility(View.VISIBLE);
            holder.viptag.setVisibility(View.GONE);
        }
    }

    final static class ViewHolder {
        ImageView viptag;

        TextView contactName;

        TextView rowSegLine;
        TextView extraInfo;

        TextView techSchoolInfo;

       ImageView contactImage;
    }

    @SuppressLint("InflateParams")
    private void setupView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.group_contacts_list_row, null);
        ViewHolder holder = new ViewHolder();
        avatarSize = getResources().getDimensionPixelSize(R.dimen.group_contacts_list_avatar_size);
        holder.contactImage = (ImageView)mView.findViewById(R.id.contactImage);
        holder.rowSegLine = (TextView)mView.findViewById(R.id.rowSegLine);
        holder.contactName = (TextView)mView.findViewById(R.id.contactName);
        holder.viptag = (ImageView)mView.findViewById(R.id.relationType);
        holder.extraInfo = (TextView)mView.findViewById(R.id.extraInfo);
        holder.techSchoolInfo = (TextView)mView.findViewById(R.id.techSchoolInfo);
        addView(mView);
        mView.setTag(holder);
        setContactInfo(mView);
    }

}
