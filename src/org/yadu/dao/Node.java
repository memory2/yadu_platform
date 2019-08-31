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
     * ���ӽڵ��б�
     */
    private List children = new ArrayList();

    // ��Ӻ��ӽڵ�
    public void addChild(Node node) {
        children.add(node);
    }

    // ���������ƴ��JSON�ַ���
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

    // �ֵܽڵ��������
    public void sortChildren() {
        if (children.size() != 0) {
            // �Ա���ڵ�������򣨿ɸ��ݲ�ͬ���������ԣ����벻ͬ�ıȽ��������� ����ID�Ƚ�����
            Collections.sort(children, new NodeIDComparator());
            // ��ÿ���ڵ����һ��ڵ��������
            for (Iterator it = children.iterator(); it.hasNext(); ) {
                ((Node) it.next()).sortChildren();
            }
        }
    }

}
