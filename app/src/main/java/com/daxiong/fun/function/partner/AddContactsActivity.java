package com.daxiong.fun.function.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.account.BanzhurenActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述：添加朋友
 *
 * @author: sky
 * @最后修改人： sky
 * @最后修改日期:2015年8月7日 下午5:13:27
 */
public class AddContactsActivity extends BaseActivity implements OnClickListener, HttpListener {

    private SearchConListAdapter adapter;

    private int avatarSize;

//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (isShowDialog) {
//				isShowDialog = false;
//				closeDialog();
//				ToastUtils.show(R.string.text_search_timeout);
//			}
//		}
//	};

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() > 0) {
                delete_iv.setVisibility(View.VISIBLE);

            } else {
                delete_iv.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private EditText et;
    private TextView search_ib;
    // private TextView resultText;
    private ListView resultList;
    private String search_et_str;


    private ImageView delete_iv;

    private void searchContacts() {
        // if (!(System.currentTimeMillis() - clickTime >500)) {
        // return;
        // }
        clickTime = System.currentTimeMillis();
        search_et_str = et.getText().toString().trim();
        if (TextUtils.isEmpty(search_et_str)) {
            ToastUtils.show(R.string.text_input_search_key);
            return;
        }
        MobclickAgent.onEvent(this, "searchContacts");
        adapter = null;
//		showDialog("请稍候");
//		handler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 15000);
//		isShowDialog = true;
        // resultText.setVisibility(View.GONE);
        resultList.setVisibility(View.GONE);

        pageindex = 1;
        userList = new ArrayList<UserInfoModel>();
        adapter = new SearchConListAdapter(userList);
        resultList.setAdapter(adapter);
        total = 0;
        WeLearnApi.searchFriend(this, search_et_str, pageindex++, pagecount, this);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.fragment_addcontacts);

        setWelearnTitle("查找好友");

        findViewById(R.id.back_layout).setOnClickListener(this);

        et = (EditText) findViewById(R.id.add_contacts_et);
        et.addTextChangedListener(mWatcher);
        et.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchContacts();
                    return true;
                }
                return false;
            }
        });

        avatarSize = getResources().getDimensionPixelSize(R.dimen.search_friends_list_avatar_size);

        search_ib = (TextView) findViewById(R.id.add_contacts_search_bt);
        delete_iv = (ImageView) findViewById(R.id.add_contacts_delete_iv);
        delete_iv.setOnClickListener(this);

        resultList = (ListView) findViewById(R.id.search_user_listview);
        resultList.setVisibility(View.GONE);

        search_ib.setOnClickListener(this);
        resultList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userList != null && userList.size() > position) {
                    UserInfoModel user = userList.get(position);
                    int tar_userid = user.getUserid();
                    int tar_roleid = user.getRoleid();
                    int userId = MySharePerfenceUtil.getInstance().getUserId();
                    // int roleId = WeLearnSpUtil.getInstance().getUserRoleId();
                    Bundle data = new Bundle();
                    data.putInt("userid", tar_userid);
                    data.putInt("roleid", tar_roleid);
                    MobclickAgent.onEvent(AddContactsActivity.this, "searched_gotoIndex");

                    switch (tar_roleid) {

                        case GlobalContant.ROLE_ID_STUDENT:
                        case GlobalContant.ROLE_ID_PARENTS:
                            IntentManager.gotoPersonalPage(AddContactsActivity.this, tar_userid, tar_roleid);
                            break;
                        case GlobalContant.ROLE_ID_COLLEAGE:
                            IntentManager.gotoPersonalPage(AddContactsActivity.this, tar_userid, tar_roleid);
                            break;
                        case GlobalContant.ROLE_ID_CLASSTEACHER:

                            Intent i = new Intent(AddContactsActivity.this, BanzhurenActivity.class);
                            i.putExtra("userid", tar_userid);
                            startActivity(i);
                            break;
                        case GlobalContant.ROLE_ID_ORG:
                            Bundle bundle = new Bundle();
                            bundle.putInt("orgid", tar_userid);
                            IntentManager.goToCramSchoolDetailsActivity(AddContactsActivity.this, bundle);
                            break;

                        default:
                            break;
                    }

//					if (tar_userid == userId) {// 进自己主页
//						IntentManager.goToStudentCenterView(AddContactsActivity.this, data);
//					} else {// 进他人空间
//						if (tar_roleid == GlobalContant.ROLE_ID_STUDENT) {
//							IntentManager.goToStudentInfoView(AddContactsActivity.this, data);
//						} else if(tar_roleid == GlobalContant.ROLE_ID_COLLEAGE) {
//							IntentManager.goToTeacherInfoView(AddContactsActivity.this, data);
//						}else{//机构
//							// TODO 进入机构主页
//							
//						}
//					}
                }
            }
        });

        resultList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Log.e("onScroll:",
                // "firstVisibleItem:"+firstVisibleItem+",	visibleItemCount:"+visibleItemCount+",	totalItemCount:"+totalItemCount);
                if (totalItemCount == total && (totalItemCount % pagecount == 0)) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        if (!TextUtils.isEmpty(search_et_str)) {
                            total += pagecount;
                            WeLearnApi.searchFriend(AddContactsActivity.this, search_et_str, pageindex++, pagecount, AddContactsActivity.this);
                        }
                    }
                }
            }
        });
        et.requestFocus();
    }

    private int total;
    private int pageindex;
    private int pagecount = 20;

    private List<UserInfoModel> userList;

    private class SearchConListAdapter extends BaseAdapter {
        private List<UserInfoModel> mUserList;

        public SearchConListAdapter(List<UserInfoModel> userList) {
            super();
            this.mUserList = userList;
        }

        public void setList(List<UserInfoModel> userList) {
            this.mUserList = userList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUserList == null ? 0 : mUserList.size();
        }

        @Override
        public UserInfoModel getItem(int position) {
            UserInfoModel user = null;
            if (mUserList != null && mUserList.size() > position) {
                user = mUserList.get(position);
            }
            return user;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            SearchFriendHold holder = null;
            if (convertView == null) {
                view = View.inflate(AddContactsActivity.this, R.layout.fragment_addcontacts_listitem, null);
                holder = new SearchFriendHold();
                holder.avatar_iv = (ImageView) view.findViewById(R.id.avatar_iv_search_friend);
                holder.name_tv = (TextView) view.findViewById(R.id.name_tv_search_friend);
                holder.userid_tv = (TextView) view.findViewById(R.id.userid_tv_search_friend);
                holder.city_tv = (TextView) view.findViewById(R.id.city_tv_search_friend);
                holder.grade_tv = (TextView) view.findViewById(R.id.grade_tv_search_friend);
                holder.v1 =  view.findViewById(R.id.v1);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (SearchFriendHold) view.getTag();
            }
            if(mUserList.size()==position+1){
                holder.v1.setVisibility(View.GONE);
            }else{
                holder.v1.setVisibility(View.VISIBLE);
            }
            if (mUserList != null && mUserList.size() > position) {
                UserInfoModel user = mUserList.get(position);

                Glide.with(AddContactsActivity.this).load(user.getAvatar_100()).asBitmap().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new CropCircleTransformation(AddContactsActivity.this)).placeholder(R.drawable.default_icon_circle_avatar)
                        .into(holder.avatar_iv);
                holder.name_tv.setText(user.getName());
                if (TextUtils.isEmpty(user.getCity())) {
                    holder.city_tv.setVisibility(View.GONE);
                } else {
                    holder.city_tv.setVisibility(View.VISIBLE);
                    holder.city_tv.setText(user.getCity());

                }

                int roleid = user.getRoleid();
                switch (roleid) {
                    case GlobalContant.ROLE_ID_STUDENT:
                        holder.grade_tv.setText(user.getGrade());
                        //holder.userid_tv.setText(getResources().getString(R.string.welearn_id_text, user.getUserid()));
                        holder.city_tv.setTextColor(getResources().getColor(R.color.GrayText));
                        break;
                    case GlobalContant.ROLE_ID_COLLEAGE:
                        holder.grade_tv.setText(R.string.text_teacher);
                        //	holder.userid_tv.setText(getResources().getString(R.string.welearn_id_text, user.getUserid()));
                        holder.city_tv.setTextColor(getResources().getColor(R.color.GrayText));
                        break;
                    case GlobalContant.ROLE_ID_ORG:
                        holder.grade_tv.setText("");
                        //	holder.userid_tv.setText(getResources().getString(R.string.org_id_text, user.getUserid()));
                        holder.city_tv.setTextColor(getResources().getColor(R.color.RedText));
                        break;
                    default:
                        break;
                }
            }
            return view;
        }

    }

    private static class SearchFriendHold {
        ImageView avatar_iv;
        TextView name_tv;
        TextView userid_tv;
        TextView city_tv;
        TextView grade_tv;
        View v1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.add_contacts_delete_iv:
                if (null != et) {
                    et.setText("");
                }
                break;
            case R.id.add_contacts_search_bt:
                MobclickAgent.onEvent(this, "SearchUser");
                searchContacts();
                break;
        }
    }

//	@Override
//	public void onSuccess(int code, String dataJson, String errMsg) {
//		closeDialog();
//		isShowDialog = false;
//		if (code == 0) {
//			if (null != dataJson) {
//				try {
//					JSONArray dataArray = new JSONArray(dataJson);
//					if (dataArray == null || dataArray.length() == 0) {
//						// ToastUtils.show(mActivity, "没有更多符合条件的用户", 0);
//						return;
//					}
//					InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
//							InputMethodManager.HIDE_NOT_ALWAYS);
//
//					resultList.setVisibility(View.VISIBLE);
//					line.setVisibility(View.VISIBLE);
//					if (userList == null) {
//						userList = new ArrayList<UserInfoModel>();
//					}
//					try {
//						for (int i = 0; i < dataArray.length(); i++) {
//							JSONObject userinfo = dataArray.getJSONObject(i);
//							UserInfoModel user = WeLearnGsonUtil.getModelFromGson(userinfo.toString(), UserInfoModel.class);
//							userList.add(user);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					if (adapter == null) {
//						adapter = new SearchConListAdapter(userList);
//						resultList.setAdapter(adapter);
//					} else {
//						adapter.setList(userList);
//					}
//					total = userList.size();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} else {
//			if (!TextUtils.isEmpty(errMsg)) {
//				ToastUtils.show(errMsg);
//			}
//		}
//	}
//
//	@Override
//	public void onFail(int HttpCode) {
//		closeDialog();
//		isShowDialog = false;
//		ToastUtils.show(R.string.invite_commit_failed_retry);
//	}


    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (!TextUtils.isEmpty(dataJson)) {
            String userJson = JsonUtil.getString(dataJson, "user", "");
            String orgJson = JsonUtil.getString(dataJson, "org", "");
            ArrayList<UserInfoModel> resultModels = new ArrayList<UserInfoModel>();
        //    ArrayList<EduModel> orgModels = null;
            ArrayList<UserInfoModel> userModels = null;
            try {
//                if (!TextUtils.isEmpty(orgJson)) {
//                    orgModels = new Gson().fromJson(orgJson, new TypeToken<ArrayList<EduModel>>() {
//                    }.getType());
//                }
                if (!TextUtils.isEmpty(userJson)) {
                    userModels = new Gson().fromJson(userJson, new TypeToken<ArrayList<UserInfoModel>>() {
                    }.getType());
                }
            } catch (Exception e) {
            }

//            if (orgModels != null) {
//                for (EduModel orgModel : orgModels) {
//                    String logo = orgModel.getLogo();
//                    int orgid = orgModel.getOrgid();
//                    String orgname = orgModel.getOrgname();
//                    UserInfoModel tempModel = new UserInfoModel();
//                    tempModel.setAvatar_100(logo);
//                    tempModel.setName(orgname);
//                    tempModel.setUserid(orgid);
//                    tempModel.setRoleid(GlobalContant.ROLE_ID_ORG);
//                    tempModel.setCity("辅导机构");
//                    resultModels.add(tempModel);
//                }
//            }
            if (userModels != null) {
                resultModels.addAll(userModels);
            }
            if (resultModels.isEmpty()) {
                Toast.makeText(AddContactsActivity.this, "没有符合条件的用户", 0).show();
                return;
            }

            resultList.setVisibility(View.VISIBLE);

            if (userList == null) {
                userList = new ArrayList<UserInfoModel>();
            }
            userList.addAll(resultModels);
            if (adapter == null) {
                adapter = new SearchConListAdapter(userList);
                resultList.setAdapter(adapter);
            } else {
                adapter.setList(userList);
            }
            total = userList.size();
//			try {
//				JSONArray dataArray = new JSONArray(dataJson);
//				if (dataArray == null || dataArray.length() == 0) {
//					// ToastUtils.show(mActivity, "没有更多符合条件的用户", 0);
//					return;
//				}
//			
//				resultList.setVisibility(View.VISIBLE);
//				line.setVisibility(View.VISIBLE);
//				if (userList == null) {
//					userList = new ArrayList<UserInfoModel>();
//				}
//			
//				try {
//					for (int i = 0; i < dataArray.length(); i++) {
//						JSONObject userinfo = dataArray.getJSONObject(i);
//						UserInfoModel user = WeLearnGsonUtil.getModelFromGson(userinfo.toString(), UserInfoModel.class);
//						userList.add(user);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (adapter == null) {
//					adapter = new SearchConListAdapter(userList);
//					resultList.setAdapter(adapter);
//				} else {
//					adapter.setList(userList);
//				}
//				total = userList.size();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
        }


    }

    @Override
    public void onFail(int HttpCode, String errMsg) {

    }

}
