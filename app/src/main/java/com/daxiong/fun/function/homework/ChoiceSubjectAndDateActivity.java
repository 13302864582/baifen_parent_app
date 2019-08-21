
package com.daxiong.fun.function.homework;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.homework.view.CollapsableLinearLayout;
import com.daxiong.fun.model.ChildBean;
import com.daxiong.fun.model.GroupBean;

import java.util.ArrayList;
import java.util.List;

public class ChoiceSubjectAndDateActivity extends PopupWindow {

    private ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();

    private List<CollapsableLinearLayout> collapsablelist = new ArrayList<CollapsableLinearLayout>();

    private LayoutInflater inflater;

    private int index = 0;

    private View cusView;

    private Activity context;

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.choice_subject_date_activity);
    //// getWindow().setLayout(LayoutParams.FILL_PARENT,
    // LayoutParams.FILL_PARENT);
    // inflater = LayoutInflater.from(this);
    // initData();
    // showView();
    // }

    public ChoiceSubjectAndDateActivity(Activity context, IExtraData iextraData) {
        super(context);
        this.context = context;
        this.iextraData = iextraData;        
        inflater = LayoutInflater.from(context);
        cusView = inflater.inflate(R.layout.choice_subject_date_activity, null);
        this.setContentView(cusView);
        initData();
        showView();

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(cusView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);

    }

    private void initData() {
        GroupBean group1 = new GroupBean();
        group1.setGroupName("科目");

        ChildBean childBean1 = new ChildBean();
        childBean1.childName = "数学";
        ChildBean childBean2 = new ChildBean();
        childBean2.childName = "英语";
        ChildBean childBean3 = new ChildBean();
        childBean3.childName = "语文";
        ChildBean childBean4 = new ChildBean();
        childBean4.childName = "物理";
        ChildBean childBean5 = new ChildBean();
        childBean5.childName = "化学";
        ChildBean childBean6 = new ChildBean();
        childBean6.childName = "生物";
        group1.getChildList().add(childBean1);
        group1.getChildList().add(childBean2);
        group1.getChildList().add(childBean3);
        group1.getChildList().add(childBean4);
        group1.getChildList().add(childBean5);
        group1.getChildList().add(childBean6);

        GroupBean group2 = new GroupBean();
        group2.setGroupName("2014年");

        for (int i = 1; i <= 12; i++) {
            ChildBean child1 = new ChildBean();
            child1.childName = i + "月";
            group2.getChildList().add(child1);
        }

        GroupBean group3 = new GroupBean();
        group3.setGroupName("2015年");

        for (int i = 1; i <= 12; i++) {
            ChildBean child1 = new ChildBean();
            child1.childName = i + "月";
            group3.getChildList().add(child1);
        }

        groupList.add(group1);
        groupList.add(group2);
        groupList.add(group3);
    }

    private void showView() {
        List<View> comboViews = createContentViews(groupList);
        LinearLayout comboInfoContainer = (LinearLayout)cusView
                .findViewById(R.id.combo_info_container_linear);
        for (View comboView : comboViews) {
            comboInfoContainer.addView(comboView);
            View dividerView = new View(context);
            dividerView.setBackgroundColor(Color.parseColor("#f8f8f8"));
            comboInfoContainer.addView(dividerView,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));// Util.dp2px(this,
                                                                                            // 30)
        }
    }

    private List<View> createContentViews(List<GroupBean> promInfo) {
        List<View> contentViews = new ArrayList<View>();
        int realIndex = 0;
        if (promInfo != null && promInfo.size() != 0) {
            for (int i = 0; i < promInfo.size(); i++) {
                GroupBean prom = promInfo.get(i);
                if (prom.getChildList() != null) {
                    if (prom.getChildList().size() != 0) { // 不展示status为4
                        View promInfoItem = createGroupItem(prom, realIndex);
                        contentViews.add(promInfoItem);
                        realIndex++;
                    }
                }
            }
        }
        return contentViews;
    }

    private View createGroupItem(GroupBean promInfo, int indexs) {
        // @formatter:off
        final View groupItemView = inflater.inflate(R.layout.group_layout, null);
        RelativeLayout combo_title_container = (RelativeLayout)groupItemView
                .findViewById(R.id.combo_title_container);
        TextView nickNameView = (TextView)groupItemView.findViewById(R.id.combo_nick_name_text);
        TextView comboNumText = (TextView)groupItemView.findViewById(R.id.combo_num_text_01);
        final ImageView arrowImage = (ImageView)groupItemView.findViewById(R.id.combo_arrow_image);
        final CollapsableLinearLayout childContainer = (CollapsableLinearLayout)groupItemView
                .findViewById(R.id.combo_products_container);
        childContainer.setToggleView(arrowImage);
        // create child product views
        if (promInfo.getChildList() != null && promInfo.getChildList().size() > 0) {
            for (ChildBean childBean : promInfo.getChildList()) {
                View childView = createChildItem(nickNameView,childContainer, promInfo, childBean);
                childContainer.addView(childView);
            }
        }

        collapsablelist.add(childContainer);

        if (index == indexs)
            childContainer.expand();
        else
            childContainer.collapse();
        nickNameView.setText(promInfo.getGroupName());
        comboNumText.setText((indexs + 1) + "");
        combo_title_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childContainer.toggle();
                // 收起其他的view
                for (int i = 0; i < collapsablelist.size(); i++) {
                    if (!childContainer.equals(collapsablelist.get(i))) {
                        if (collapsablelist.get(i).isExpanded()) {
                            collapsablelist.get(i).collapseOther();
                        }
                    }
                }
            }
        });
        // @formatter:on
        return groupItemView;
    }
    
    
      
    StringBuffer ss = new StringBuffer();
    boolean isFlag=false;
    private String subject="";
    private String date=""; 
    private View createChildItem(final TextView tv_group_name,final CollapsableLinearLayout childContainer,
            final GroupBean promInfo, final ChildBean childBean) {
        // @formatter:off
        View comboChildItem = inflater.inflate(R.layout.child_layout, null);
        LinearLayout product_linear = (LinearLayout)comboChildItem
                .findViewById(R.id.product_linear);
        TextView childNameView = (TextView)comboChildItem.findViewById(R.id.combo_product_name);

        childNameView.setText(childBean.childName);
        product_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  
                childContainer.collapse();
                String str = promInfo.getGroupName() + "-" + childBean.childName;
                Log.e("context", str);                
                ss.append(str+",");                
                //判断是否含有年月  
                if (ss.toString().contains("科目")) {
//                    tv_group_name.setBackgroundColor(Color.parseColor("#cccccc"));
                    tv_group_name.setText(str.split("-")[1]);
                }
                
                if (ss.toString().contains("年")) {
//                    tv_group_name.setBackgroundColor(Color.parseColor("#cccccc"));
                    tv_group_name.setText(str.split("-")[1]);
                }
                if (ss.toString().contains("科目") && ss.toString().contains("年")) {
                   isFlag=true;
                } else {
                    isFlag=false;
                }
                
                if (isFlag) {
                    String[] app = ss.toString().split(",");
                    String[] s = new String[2];
                    for (int i = app.length - 1; i >= 0; i--) {
                        if (app[i].contains("科目")) {
                            System.out.println(app[i]);
                            subject=app[i];
                            break;
                        }
                    }
                    
                    for (int i = app.length - 1; i >= 0; i--) {
                        if (app[i].contains("年")) {
                            System.out.println(app[i]);
                            date=app[i];
                            break;
                        }
                    }
                    
                    childContainer.collapse();
                    iextraData.dealData(subject,date);
                }else {
//                  ToastUtils.show("请选择科目和日期");
                }
                

            }
        });
        
        
        return comboChildItem;
    }

    

   

    public IExtraData iextraData;
    

    public interface IExtraData {
        public void dealData(String subject,String date);
    }

}
