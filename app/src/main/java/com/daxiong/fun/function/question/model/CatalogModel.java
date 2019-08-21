package com.daxiong.fun.function.question.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CatalogModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4817115872577246067L;
	public static final String TAG = CatalogModel.class.getSimpleName();
	private int groupid;
	private String groupname;
	private ArrayList<Subject> subjects;

	public CatalogModel() {
		super();
	}

	public CatalogModel(int groupid, String groupname) {
		super();
		this.groupid = groupid;
		this.groupname = groupname;
	}

	@Override
	public String toString() {
		return "CatalogModel [groupid=" + groupid + ", groupname=" + groupname + ", subjects=" + subjects + "]";
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public ArrayList<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}

	public class Subject implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3568278237234002671L;
		int subjectid;
		String subjectname;
		ArrayList<Chapter> chapter;

		public int getSubjectid() {
			return subjectid;
		}

		public void setSubjectid(int subjectid) {
			this.subjectid = subjectid;
		}

		public String getSubjectname() {
			return subjectname;
		}

		public void setSubjectname(String subjectname) {
			this.subjectname = subjectname;
		}

		public ArrayList<Chapter> getChapter() {
			return chapter;
		}

		public void setChapter(ArrayList<Chapter> chapter) {
			this.chapter = chapter;
		}

		@Override
		public String toString() {
			return "Subject [subjectid=" + subjectid + ", subjectname=" + subjectname + ", chapter=" + chapter + "]";
		}

	}

	public class Chapter  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 295566391379039L;
		int chapterid;
		String chaptername;
		ArrayList<Point> point;

		public int getChapterid() {
			return chapterid;
		}

		public void setChapterid(int chapterid) {
			this.chapterid = chapterid;
		}

		public String getChaptername() {
			return chaptername;
		}

		public void setChaptername(String chaptername) {
			this.chaptername = chaptername;
		}

		public ArrayList<Point> getPoint() {
			return point;
		}

		public void setPoint(ArrayList<Point> point) {
			this.point = point;
		}

		@Override
		public String toString() {
			return "Chapter [chapterid=" + chapterid + ", chaptername=" + chaptername + ", point=" + point + "]";
		}

	}

	public class Point  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3209320163994917228L;
		int id;
		String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Point [id=" + id + ", name=" + name + "]";
		}

	}

}
