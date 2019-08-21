package com.daxiong.fun.function.account.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 年级选择的model
 * 
 * @author Administrator
 *
 */
public class BigGradeModel implements Serializable {

	private static final long serialVersionUID = -1162258982707557165L;

	private List<SubGradeModel> junior_high_school;
	private List<SubGradeModel> primary_school;
	private List<SubGradeModel> senior_high_school;

	public List<SubGradeModel> getJunior_high_school() {
		if (junior_high_school == null) {
			junior_high_school = new ArrayList<SubGradeModel>();
		}
		return junior_high_school;
	}

	public void setJunior_high_school(List<SubGradeModel> junior_high_school) {
		this.junior_high_school = junior_high_school;
	}

	public List<SubGradeModel> getPrimary_school() {
		if (primary_school == null) {
			primary_school = new ArrayList<SubGradeModel>();
		}
		return primary_school;
	}

	public void setPrimary_school(List<SubGradeModel> primary_school) {
		this.primary_school = primary_school;
	}

	public List<SubGradeModel> getSenior_high_school() {
		if (senior_high_school == null) {
			senior_high_school = new ArrayList<SubGradeModel>();
		}
		return senior_high_school;
	}

	public void setSenior_high_school(List<SubGradeModel> senior_high_school) {
		this.senior_high_school = senior_high_school;
	}

	public class SubGradeModel implements Serializable {

		private static final long serialVersionUID = -8252621688578116533L;
		private int gradeid;
		private String grade_name;
		private int checked;

		public int getChecked() {
			return checked;
		}

		public void setChecked(int checked) {
			this.checked = checked;
		}

		public int getGradeid() {
			return gradeid;
		}

		public void setGradeid(int gradeid) {
			this.gradeid = gradeid;
		}

		public String getGrade_name() {
			return grade_name;
		}

		public void setGrade_name(String grade_name) {
			this.grade_name = grade_name;
		}

	}

}
