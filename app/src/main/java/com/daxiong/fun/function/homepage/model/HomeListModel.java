
package com.daxiong.fun.function.homepage.model;

import java.io.Serializable;
import java.util.List;

public class HomeListModel implements Serializable {

    private int answer_state;
    private int question_id;
    private int question_state;
    private String question_thumbnail;
    private int studid;
    private String complaint_tip_content;
    private int complaint_tip_show_time;
    private String remark;
    private String superscript_url;
    private List<Tags> tags;
    private String subject_name;
    private int wrong_count;
    private int percent;
    private String homewrok_thumbnail;
    private int subject_id;
    private int task_id;
    private int task_type;
    private long answer_time;
    private String teacher_pic;
    private long grab_time;
    private String teacher_name;
    private int homework_state;
    private long create_time;
    private long avg_cost_time;
    private int teacher_id;
    private int right_count;
    private String report_reason;

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }

    public String getReport_reason(){
        return this.report_reason;
    }
    public void setAnswer_state(int answer_state) {
        this.answer_state = answer_state;
    }

    public int getAnswer_state() {
        return this.answer_state;
    }


    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_id() {
        return this.question_id;
    }

    public void setQuestion_state(int question_state) {
        this.question_state = question_state;
    }

    public int getQuestion_state() {
        return this.question_state;
    }

    public void setQuestion_thumbnail(String question_thumbnail) {
        this.question_thumbnail = question_thumbnail;
    }

    public String getQuestion_thumbnail() {
        return this.question_thumbnail;
    }

    public void setStudid(int studid) {
        this.studid = studid;
    }

    public int getStudid() {
        return this.studid;
    }


    public void setComplaint_tip_content(String complaint_tip_content) {
        this.complaint_tip_content = complaint_tip_content;
    }

    public String getComplaint_tip_content() {
        return this.complaint_tip_content;
    }

    public void setComplaint_tip_show_time(int complaint_tip_show_time) {
        this.complaint_tip_show_time = complaint_tip_show_time;
    }

    public int getComplaint_tip_show_time() {
        return this.complaint_tip_show_time;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }


    public void setSuperscript_url(String superscript_url) {
        this.superscript_url = superscript_url;
    }

    public String getSuperscript_url() {
        return this.superscript_url;
    }






    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<Tags> getTags() {
        return this.tags;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_name() {
        return this.subject_name;
    }

    public void setWrong_count(int wrong_count) {
        this.wrong_count = wrong_count;
    }

    public int getWrong_count() {
        return this.wrong_count;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return this.percent;
    }

    public void setHomewrok_thumbnail(String homewrok_thumbnail) {
        this.homewrok_thumbnail = homewrok_thumbnail;
    }

    public String getHomewrok_thumbnail() {
        return this.homewrok_thumbnail;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSubject_id() {
        return this.subject_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTask_id() {
        return this.task_id;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public int getTask_type() {
        return this.task_type;
    }

    public void setAnswer_time(long answer_time) {
        this.answer_time = answer_time;
    }

    public long getAnswer_time() {
        return this.answer_time;
    }

    public void setTeacher_pic(String teacher_pic) {
        this.teacher_pic = teacher_pic;
    }

    public String getTeacher_pic() {
        return this.teacher_pic;
    }

    public void setGrab_time(long grab_time) {
        this.grab_time = grab_time;
    }

    public long getGrab_time() {
        return this.grab_time;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_name() {
        return this.teacher_name;
    }

    public void setHomework_state(int homework_state) {
        this.homework_state = homework_state;
    }

    public int getHomework_state() {
        return this.homework_state;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getCreate_time() {
        return this.create_time;
    }

    public void setAvg_cost_time(long avg_cost_time) {
        this.avg_cost_time = avg_cost_time;
    }

    public long getAvg_cost_time() {
        return this.avg_cost_time;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getTeacher_id() {
        return this.teacher_id;
    }

    public void setRight_count(int right_count) {
        this.right_count = right_count;
    }

    public int getRight_count() {
        return this.right_count;
    }


    public class Tags {
        private String content;

        private String color;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return this.color;
        }

    }

}
