package org.yadu.dao;

import java.util.Comparator;

public class NodeIDComparator implements Comparator {
	// 按照节点编号比较
	public int compare(Object o1, Object o2) {
		long j1 =  Long.parseLong(((Node) o1).id);
		long j2 =  Long.parseLong(((Node) o2).id);
		return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));
	}
}
