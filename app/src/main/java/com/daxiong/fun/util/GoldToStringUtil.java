
package com.daxiong.fun.util;

import android.content.Context;

import com.google.gson.Gson;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.PayAnswerGoldGson;
import com.daxiong.fun.model.SubjectId;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GoldToStringUtil {
    public static String GoldToString(float gold) {
        gold += 0.0005;
        DecimalFormat df = new DecimalFormat("#.0");
        String goldStr = df.format(gold);
        if (goldStr.startsWith(".")) {
            goldStr = "0" + goldStr;
        }
        return goldStr;
    }

    public static String GoldToString(double gold) {
        gold += 0.0005;
        DecimalFormat df = new DecimalFormat("#.0");
        String goldStr = df.format(gold);
        if (goldStr.startsWith(".")) {
            goldStr = "0" + goldStr;
        }
        return goldStr;
    }

    public static void updataPayGoldInfo(final Context mActivity, JSONObject subjects) {
        Gson gson = new Gson();

        PayAnswerGoldGson chu_1 = new PayAnswerGoldGson();
        PayAnswerGoldGson chu_2 = new PayAnswerGoldGson();
        PayAnswerGoldGson chu_3 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_1 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_2 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_3 = new PayAnswerGoldGson();

        List<SubjectId> chu1subs = new ArrayList<SubjectId>();
        List<SubjectId> chu2subs = new ArrayList<SubjectId>();
        List<SubjectId> chu3subs = new ArrayList<SubjectId>();
        List<SubjectId> gao1subs = new ArrayList<SubjectId>();
        List<SubjectId> gao2subs = new ArrayList<SubjectId>();
        List<SubjectId> gao3subs = new ArrayList<SubjectId>();

        // 英语
        JSONObject english = JsonUtil.getJSONObject(subjects, "english", null);
        chu1subs.add(gson.fromJson(JsonUtil.getString(english, "chu_1", null), SubjectId.class));
        chu2subs.add(gson.fromJson(JsonUtil.getString(english, "chu_2", null), SubjectId.class));
        chu3subs.add(gson.fromJson(JsonUtil.getString(english, "chu_3", null), SubjectId.class));
        gao1subs.add(gson.fromJson(JsonUtil.getString(english, "gao_1", null), SubjectId.class));
        gao2subs.add(gson.fromJson(JsonUtil.getString(english, "gao_2", null), SubjectId.class));
        gao3subs.add(gson.fromJson(JsonUtil.getString(english, "gao_3", null), SubjectId.class));

        // 数学
        JSONObject math = JsonUtil.getJSONObject(subjects, "math", null);
        chu1subs.add(gson.fromJson(JsonUtil.getString(math, "chu_1", null), SubjectId.class));
        chu2subs.add(gson.fromJson(JsonUtil.getString(math, "chu_2", null), SubjectId.class));
        chu3subs.add(gson.fromJson(JsonUtil.getString(math, "chu_3", null), SubjectId.class));
        gao1subs.add(gson.fromJson(JsonUtil.getString(math, "gao_1", null), SubjectId.class));
        gao2subs.add(gson.fromJson(JsonUtil.getString(math, "gao_2", null), SubjectId.class));
        gao3subs.add(gson.fromJson(JsonUtil.getString(math, "gao_3", null), SubjectId.class));

        // 物理
        JSONObject physics = JsonUtil.getJSONObject(subjects, "physics", null);
        chu1subs.add(gson.fromJson(JsonUtil.getString(physics, "chu_1", null), SubjectId.class));
        chu2subs.add(gson.fromJson(JsonUtil.getString(physics, "chu_2", null), SubjectId.class));
        chu3subs.add(gson.fromJson(JsonUtil.getString(physics, "chu_3", null), SubjectId.class));
        gao1subs.add(gson.fromJson(JsonUtil.getString(physics, "gao_1", null), SubjectId.class));
        gao2subs.add(gson.fromJson(JsonUtil.getString(physics, "gao_2", null), SubjectId.class));
        gao3subs.add(gson.fromJson(JsonUtil.getString(physics, "gao_3", null), SubjectId.class));

        // 化学
        JSONObject chemistry = JsonUtil.getJSONObject(subjects, "chemistry", null);
        chu1subs.add(gson.fromJson(JsonUtil.getString(chemistry, "chu_1", null), SubjectId.class));
        chu2subs.add(gson.fromJson(JsonUtil.getString(chemistry, "chu_2", null), SubjectId.class));
        chu3subs.add(gson.fromJson(JsonUtil.getString(chemistry, "chu_3", null), SubjectId.class));
        gao1subs.add(gson.fromJson(JsonUtil.getString(chemistry, "gao_1", null), SubjectId.class));
        gao2subs.add(gson.fromJson(JsonUtil.getString(chemistry, "gao_2", null), SubjectId.class));
        gao3subs.add(gson.fromJson(JsonUtil.getString(chemistry, "gao_3", null), SubjectId.class));

        // 生物
        JSONObject biology = JsonUtil.getJSONObject(subjects, "biology", null);
        chu1subs.add(gson.fromJson(JsonUtil.getString(biology, "chu_1", null), SubjectId.class));
        chu2subs.add(gson.fromJson(JsonUtil.getString(biology, "chu_2", null), SubjectId.class));
        chu3subs.add(gson.fromJson(JsonUtil.getString(biology, "chu_3", null), SubjectId.class));
        gao1subs.add(gson.fromJson(JsonUtil.getString(biology, "gao_1", null), SubjectId.class));
        gao2subs.add(gson.fromJson(JsonUtil.getString(biology, "gao_2", null), SubjectId.class));
        gao3subs.add(gson.fromJson(JsonUtil.getString(biology, "gao_3", null), SubjectId.class));

        LogUtils.e("subject:", gson
                .fromJson(JsonUtil.getString(biology, "gao_3", null), SubjectId.class).toString());
        chu_1.setSubjects(chu1subs);
        chu_2.setSubjects(chu2subs);
        chu_3.setSubjects(chu3subs);
        gao_1.setSubjects(gao1subs);
        gao_2.setSubjects(gao2subs);
        gao_3.setSubjects(gao3subs);

        final List<PayAnswerGoldGson> gsonlist = new ArrayList<PayAnswerGoldGson>();
        gsonlist.add(chu_1);
        gsonlist.add(chu_2);
        gsonlist.add(chu_3);
        gsonlist.add(gao_1);
        gsonlist.add(gao_2);
        gsonlist.add(gao_3);

        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                GlobalVariable.doingGoldDB = true;
                /*
                 * 更改数据调用方式 modified by yh 2015-01-07 Start
                 * ---------------------- OLD CODE ---------------------- new
                 * PayAnswerGoldDBHelper(mActivity).insert(gsonlist);
                 */
                DBHelper.getInstance().getWeLearnDB().insertGold(gsonlist);
                // 更改数据调用方式 modified by yh 2015-01-07 End
            }
        });
    }
}
