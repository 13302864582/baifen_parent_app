package com.daxiong.fun.function.account.model;

import java.io.Serializable;

/**
 * 性别实体类
 */

public class GenderModel implements Serializable {

    private static final long serialVersionUID = -8252621688578116533L;
    private int genderId;
    private String gender;
    private int checked;

    public GenderModel(int genderId, String gender, int checked) {
        this.genderId = genderId;
        this.gender = gender;
        this.checked = checked;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
