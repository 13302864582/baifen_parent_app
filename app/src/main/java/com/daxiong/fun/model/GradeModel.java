
package com.daxiong.fun.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类的描述：年级实体
 * 
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2015-7-28 下午2:22:53
 * @version: 2.0
 */
public class GradeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    // 年级id
    private int gradeId;

    // 年级名称
    private String name;

    // 年级的科目
    private String subjects;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public ArrayList<Integer> getSubjectIds() {
        if (null == subjects) {
            return null;
        } else {
            String[] subStrList = subjects.split(",");
            if (null != subStrList && subStrList.length > 0) {
                ArrayList<Integer> subIds = new ArrayList<Integer>();
                for (String s : subStrList) {
                    subIds.add(Integer.parseInt(s));
                }
                return subIds;
            } else {
                return null;
            }
        }
    }

}
