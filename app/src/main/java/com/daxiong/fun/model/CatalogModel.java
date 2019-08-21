package com.daxiong.fun.model;

import java.util.LinkedList;
import java.util.List;


class GroupAndState {

	private String groupName;
	private String groupState;

	public GroupAndState(String name, String state) {
		groupName = name;
		groupState = state;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String name) {
		if (name != null) {
			groupName = name;
		}
	}

	public String getGroupState() {
		return groupState;
	}

	public void setGroupState(String state) {
		if (state != null) {
			groupState = state;
		}
	}

}

class DetailModel {

	private int id;
	private String name;

	public DetailModel(int Id, String Name) {
		id = Id;
		name = Name;
	}

	public String getName() {
		return name;
	}

	public void setName(String Name) {
		if (name != null) {
			name = Name;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int Id) {
		id = Id;
	}
}

public class CatalogModel extends BaseModel {

	public static final String TAG = CatalogModel.class.getSimpleName();

	private LinkedList<GroupAndState> mGroups = null;
	private List<LinkedList<DetailModel>> mDetails = null;

	private LinkedList<DetailModel> groups = null;
	private LinkedList<DetailModel> subjects = null;
	private LinkedList<DetailModel> chapters = null;
	private LinkedList<DetailModel> nodes = null;

	public CatalogModel() {

		mGroups = new LinkedList<GroupAndState>();
		mGroups.add(new GroupAndState("筛选年级", "未选"));
		mGroups.add(new GroupAndState("筛选科目", "未选"));
		mGroups.add(new GroupAndState("筛选章节", "未选"));
		mGroups.add(new GroupAndState("筛选知识点", "未选"));

		mDetails = new LinkedList<LinkedList<DetailModel>>();
		groups = new LinkedList<DetailModel>();
		groups.add(new DetailModel(1, "初一"));
		groups.add(new DetailModel(1, "初二"));
		groups.add(new DetailModel(1, "初三"));
		groups.add(new DetailModel(2, "高一"));
		groups.add(new DetailModel(2, "高二"));
		groups.add(new DetailModel(2, "高三"));
		mDetails.add(groups);

		subjects = new LinkedList<DetailModel>();
		mDetails.add(subjects);
		chapters = new LinkedList<DetailModel>();
		mDetails.add(chapters);
		nodes = new LinkedList<DetailModel>();
		mDetails.add(nodes);
	}

	public int getGroupCount() {
		return mGroups.size();
	}

	public Object getGroupWithPos(int groupPosition) {
		if (groupPosition >= 0 && groupPosition < mGroups.size()) {
			return mGroups.get(groupPosition).getGroupName();
		}
		return null;
	}

	public Object getGroupStateWithPos(int groupPosition) {
		if (groupPosition >= 0 && groupPosition < mGroups.size()) {
			return mGroups.get(groupPosition).getGroupState();
		}
		return null;
	}

	public void updateGroupStateWithPos(int groupPosition, int childPosition) {
		if (groupPosition >= 0 && groupPosition < mDetails.size()) {
			LinkedList<DetailModel> linkedList = mDetails.get(groupPosition);
			if (linkedList.size() > 0 && mGroups.size() > groupPosition && linkedList.size() > childPosition) {
				if (childPosition == 0 && groupPosition != 0) {
					mGroups.get(groupPosition).setGroupState("全部");
				} else {
					mGroups.get(groupPosition).setGroupState(linkedList.get(childPosition).getName());
				}
			}
		}
	}

	public int getDetailCount(int groupPosition) {
		return mDetails.get(groupPosition).size();
	}

	public Object getDetailWithPos(int groupPosition, int childPosition) {
		if (groupPosition >= 0 && groupPosition < mGroups.size()) {
			if (mDetails.get(groupPosition).size() > 0) {
				return mDetails.get(groupPosition).get(childPosition).getName();
			}
		}
		return null;
	}

	public int getDetailObjectIdWithPos(int groupPosition, int childPosition) {
		try {
			if (groupPosition >= 0 && groupPosition < mGroups.size()) {
				if (groupPosition < mDetails.size() && mDetails.get(groupPosition).size() > 0) {
					return mDetails.get(groupPosition).get(childPosition).getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void cleanSubjects() {
		subjects.clear();
	}

	public int getSubjectsCount() {
		// TODO Auto-generated method stub
		return subjects.size();
	}

	public void pushSubject(int id, String subject) {
		subjects.add(new DetailModel(id, subject));
		// subjects.add(subject);
	}

	public void cleanChapters() {
		chapters.clear();
	}

	public void pushChapter(int id, String chapterName) {
		chapters.add(new DetailModel(id, chapterName));
	}

	public void cleanKnowledgeNodes() {
		nodes.clear();
	}

	public void pushKnowledgeNode(int id, String knowledge) {
		nodes.add(new DetailModel(id, knowledge));
	}

	public int getChapterCount() {
		// TODO Auto-generated method stub
		return chapters.size();
	}

	public int getKnowledgeNodesCount() {
		// TODO Auto-generated method stub
		return nodes.size();
	}
}
