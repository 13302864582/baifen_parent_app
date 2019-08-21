package com.daxiong.fun.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by w9474 on 2016/6/24.
 */
public class Firstuse implements Serializable {
    private static final long serialVersionUID = 2530437496531842678L;
    private int isbusy;

    private int hwcoupon;

    private int uncheck;

    private int couponed;

    private List<Teacher_infos> teacher_infos ;

    private int checked;

    private int checking;

    public void setIsbusy(int isbusy){
        this.isbusy = isbusy;
    }
    public int getIsbusy(){
        return this.isbusy;
    }
    public void setHwcoupon(int hwcoupon){
        this.hwcoupon = hwcoupon;
    }
    public int getHwcoupon(){
        return this.hwcoupon;
    }
    public void setUncheck(int uncheck){
        this.uncheck = uncheck;
    }
    public int getUncheck(){
        return this.uncheck;
    }
    public void setCouponed(int couponed){
        this.couponed = couponed;
    }
    public int getCouponed(){
        return this.couponed;
    }
    public void setTeacher_infos(List<Teacher_infos> teacher_infos){
        this.teacher_infos = teacher_infos;
    }
    public List<Teacher_infos> getTeacher_infos(){
        return this.teacher_infos;
    }
    public void setChecked(int checked){
        this.checked = checked;
    }
    public int getChecked(){
        return this.checked;
    }
    public void setChecking(int checking){
        this.checking = checking;
    }
    public int getChecking(){
        return this.checking;
    }
    public class Teacher_infos {
        private int uid;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private int type=0;

        private String avatar;

        private List<Tids> tids ;

        private String name;

        public void setUid(int uid){
            this.uid = uid;
        }
        public int getUid(){
            return this.uid;
        }
        public void setAvatar(String avatar){
            this.avatar = avatar;
        }
        public String getAvatar(){
            return this.avatar;
        }
        public void setTids(List<Tids> tids){
            this.tids = tids;
        }
        public List<Tids> getTids(){
            return this.tids;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }

    }
    public class Tids {
        private String studname;

        private int taskid;

        public void setStudname(String studname) {
            this.studname = studname;
        }

        public String getStudname() {
            return this.studname;
        }

        public void setTaskid(int taskid) {
            this.taskid = taskid;
        }

        public int getTaskid() {
            return this.taskid;
        }
    }
}
