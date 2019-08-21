package com.daxiong.fun.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by w9474 on 2016/7/6.
 */

public class Teacher_info implements Serializable {
    private Teacher_infos teacher_infos;

    private List<Comments> comments ;

    public void setTeacher_infos(Teacher_infos teacher_infos){
        this.teacher_infos = teacher_infos;
    }
    public Teacher_infos getTeacher_infos(){
        return this.teacher_infos;
    }
    public void setComments(List<Comments> comments){
        this.comments = comments;
    }
    public List<Comments> getComments(){
        return this.comments;
    }
    public class Teacher_infos {
        private float adopt_rate;

        private int age;

        private float attitude_index;

        private String avatar;

        private int homework_cnt;

        private String name;

        private int quesiton_cnt;

        private float responsibility_index;

        private String school;

        private int sex;

        private int relationtype;

        private String skill_subjects;

        public void setAdopt_rate(float adopt_rate){
            this.adopt_rate = adopt_rate;
        }
        public float getAdopt_rate(){
            return this.adopt_rate;
        }
        public void setAge(int age){
            this.age = age;
        }
        public int getAge(){
            return this.age;
        }
        public void setAttitude_index(float attitude_index){
            this.attitude_index = attitude_index;
        }
        public float getAttitude_index(){
            return this.attitude_index;
        }
        public void setAvatar(String avatar){
            this.avatar = avatar;
        }
        public String getAvatar(){
            return this.avatar;
        }
        public void setHomework_cnt(int homework_cnt){
            this.homework_cnt = homework_cnt;
        }
        public int getHomework_cnt(){
            return this.homework_cnt;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setQuesiton_cnt(int quesiton_cnt){
            this.quesiton_cnt = quesiton_cnt;
        }
        public int getQuesiton_cnt(){
            return this.quesiton_cnt;
        }
        public void setResponsibility_index(float responsibility_index){
            this.responsibility_index = responsibility_index;
        }
        public float getResponsibility_index(){
            return this.responsibility_index;
        }
        public void setSchool(String school){
            this.school = school;
        }
        public String getSchool(){
            return this.school;
        }
        public void setSex(int sex){
            this.sex = sex;
        }
        public int getSex(){
            return this.sex;
        }
        public void setRelationtype(int relationtype){
            this.relationtype = relationtype;
        }
        public int getRelationtype(){
            return this.relationtype;
        }
        public void setSkill_subjects(String skill_subjects){
            this.skill_subjects = skill_subjects;
        }
        public String getSkill_subjects(){
            return this.skill_subjects;
        }

    }

    public class Comments {
        private String avatar;

        private String content;

        private int studid;

        private String studname;

        private String time;

        public void setAvatar(String avatar){
            this.avatar = avatar;
        }
        public String getAvatar(){
            return this.avatar;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setStudid(int studid){
            this.studid = studid;
        }
        public int getStudid(){
            return this.studid;
        }
        public void setStudname(String studname){
            this.studname = studname;
        }
        public String getStudname(){
            return this.studname;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }

    }
}
