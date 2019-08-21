package com.daxiong.fun.model;

import java.util.List;


public class UnivListGson {
	List<UnivGson> univList;

	@Override
	public String toString() {
		return "UnivListGson [univList=" + univList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((univList == null) ? 0 : univList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnivListGson other = (UnivListGson) obj;
		if (univList == null) {
			if (other.univList != null)
				return false;
		} else if (!univList.equals(other.univList))
			return false;
		return true;
	}

	public UnivListGson() {
		super();
	}

	public UnivListGson(List<UnivGson> univList) {
		super();
		this.univList = univList;
	}

	public List<UnivGson> getUnivList() {
		return univList;
	}

	public void setUnivList(List<UnivGson> univList) {
		this.univList = univList;
	}

}
