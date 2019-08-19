package org.yadu.excel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
 
/**
 * ����һ���ַ���,���ֵ����ӡ�����ַ������ַ����������С����������ַ���abc,���ӡ�����ַ�a,b,c�������г����������ַ���abc,acb,bac,
 * bca,cab��cba��
 * 
 *
 */
public class Solution_stringarrange
{
	public ArrayList<String> Permutation(String str)
	{
		if (str == null)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		char[] pStr = str.toCharArray();
 
		Permutation(pStr, 0, list);
		Collections.sort(list);
		return list;
	}
 
	static void Permutation(char[] str, int i, ArrayList<String> list)
	{
		// ���Ϊ��
		if (str == null)
			return;
		// ���iָ�������һ���ַ�
		if (i == str.length - 1)
		{
			if (list.contains(String.valueOf(str)))
				return;
			list.add(String.valueOf(str));
		} else{
			// iָ��ǰ���������в������ַ����ĵ�һ���ַ�
			for (int j = i; j < str.length; j++){
				// �������в������ַ����ĵ�һ���ַ��ͺ���������ַ�����
				char temp = str[j];
				str[j] = str[i];
				str[i] = temp;
				// �������i������ַ����ݹ������в���
				Permutation(str, i + 1, list);
				// ÿһ�ֽ����󻻻���������һ�����в���
				temp = str[j];
				str[j] = str[i];
				str[i] = temp;
			}
		}
 
	}
 
	public static void main(String[] args)
	{
		
		//StringUtil a=new StringUtil();
		//String str="123456 nullnullnullnull";
		//str=str.replaceAll(" ", "").replaceAll("null", "");
		
		String strNo="13315015115311331361391442133154155156113313413518711331471481491";
    	strNo=strNo.replaceAll(" ", "").replaceAll("null", "");
    	char[] arrayCh=strNo.toCharArray();
        Arrays.sort(arrayCh);
        String sortedStr=new String(arrayCh);  //�������
		
		//str.replaceAll("null", "");
		System.out.println(sortedStr);
		/*String str = "1331341351871133154155156113317817918341332072082091";
        char[] arrayCh=str.toCharArray();
        Arrays.sort(arrayCh);
        String sortedStr=new String(arrayCh);  //�������
        System.out.println(sortedStr);*/
		/*Solution_stringarrange changestring = new Solution_stringarrange();
		ArrayList<String> list = changestring.Permutation(str);
		for (int i = 0; i < list.size(); i++)
		{
			System.out.print(list.get(i) + " ");
		}*/
	}
}