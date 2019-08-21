
package com.daxiong.fun.function.partner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.cram.fragment.EduFragment;
import com.daxiong.fun.util.LogUtils;

/**
 * 学伴Fragment 此类的描述：
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015-7-31 上午10:01:56
 */
public class PartnerFragment extends Fragment implements OnClickListener {

    public static final String FRAGEMT_1 = "GroupFragment";

    public static final String FRAGEMT_2 = "EduFragment";

    private String CurrentPage = FRAGEMT_1;

    private TextView tv_edu;

    private TextView tv_friend;

    private GroupFragment groupFragment;

    private EduFragment eduFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_partner, null);
        tv_friend = (TextView)view.findViewById(R.id.partner_tv_friend);
        tv_edu = (TextView)view.findViewById(R.id.partner_tv_edu);

        tv_friend.setOnClickListener(this);
        tv_edu.setOnClickListener(this);

        tv_friend.performClick();
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = null;
        switch (v.getId()) {
            case R.id.partner_tv_friend: // 好友
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                }
                CurrentPage = FRAGEMT_1;
                tv_friend.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
                tv_edu.setTextColor(getResources().getColor(R.color.master_tab_losefocus));
                if (!groupFragment.isAdded()) {
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.partner_fl_content, groupFragment, FRAGEMT_1);
                    ft.commit();
                }
                break;
            case R.id.partner_tv_edu: // 机构
                if (eduFragment == null) {
                    eduFragment = new EduFragment();
                } else {
                    eduFragment.onRefresh();
                }
                CurrentPage = FRAGEMT_2;
                tv_edu.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
                tv_friend.setTextColor(getResources().getColor(R.color.master_tab_losefocus));
                if (!eduFragment.isAdded()) {
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.partner_fl_content, eduFragment, FRAGEMT_2);
                    ft.commit();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("par-->", "par");
    }

    public void onSelected(boolean isSelected) {
        if (CurrentPage.equals(FRAGEMT_1) && groupFragment != null) {
            groupFragment.onSelected(isSelected);
        }
    }
}
