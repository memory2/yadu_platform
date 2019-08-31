package org.yadu.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class Node {
    public String id;
    public String parentId;
    public String text;
    /**
     * 孩子节点列表
     */
    private List children = new ArrayList();

    // 添加孩子节点
    public void addChild(Node node) {
        children.add(node);
    }

    // 先序遍历，拼接JSON字符串
    public String toString() {
        String result = "{" + "id:" + id + "" + ",text:'" + text + "'";
        if (children.size() != 0) {
            result += ",children:[";
            for (Iterator it = children.iterator(); it.hasNext(); ) {
                result += ((Node) it.next()).toString() + ",";
            }
            result = result.substring(0, result.length() - 1);
            result += "]";
        } else {
            //result += ", leaf:true";
        }
        return result + "}";
    }

    // 兄弟节点横向排序
    public void sortChildren() {
        if (children.size() != 0) {
            // 对本层节点进行排序（可根据不同的排序属性，传入不同的比较器，这里 传入ID比较器）
            Collections.sort(children, new NodeIDComparator());
            // 对每个节点的下一层节点进行排序
            for (Iterator it = children.iterator(); it.hasNext(); ) {
                ((Node) it.next()).sortChildren();
            }
        }
    }

}
