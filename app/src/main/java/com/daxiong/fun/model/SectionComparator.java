package com.daxiong.fun.model;

import java.util.Comparator;

/**
 * 
 * @author Milo.Xiao
 *
 */
public class SectionComparator implements Comparator<SortContactModel> {

	public int compare(SortContactModel o1, SortContactModel o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
