
package com.daxiong.fun.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述：我的机构实体
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月8日 下午4:18:41
 */
public class MyOrgModel implements Serializable {

    private static final long serialVersionUID = -8531214811019350621L;

    private ArrayList<Specialuser> specialuser;

    private ArrayList<OrgModel> orglist;

    

    public List<Specialuser> getSpecialuser() {
        if (specialuser==null) {
            specialuser=new ArrayList<Specialuser>();
        }
        return specialuser;
    }

    public void setSpecialuser(ArrayList<Specialuser> specialuser) {
        this.specialuser = specialuser;
    }

    public ArrayList<OrgModel> getOrgList() {
        if (orglist==null) {
            orglist=new ArrayList<>();
        }
        return orglist;
    }

    public void setOrgList(ArrayList<OrgModel> orgList) {
        this.orglist = orgList;
    }

}
