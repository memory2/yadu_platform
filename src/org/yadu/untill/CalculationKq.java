package org.yadu.untill;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * �Д������Ƿ����ϰ�
 */
public class CalculationKq {

    public List<Map<String, Object>>  calKq(List<Map<String, Object>> list, HttpServletRequest req) throws ParseException {

        //��ȡ��ѯ����
        String yearMonth = req.getParameter("cxsj");


        /**
         *  �����ϰ�
         */
        String swsb="";
        /**
         * �����°�
         */
        String swxb="";

        /**
         *�����ϰ�
         */
        String xwsb="";
        /**
         * �����°�
         */
        String xwxb="";
        /**
         * ���ڼ�
         */
        String xq="";
        /**
         * ����
         */
        String rq="";
        String rq1="";
        /**
         * �r�g
         */
        String time="";
        int intRq=0;
        /**
         *  ��ȡ��ǰʱ��
         */
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        String nowData = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        for (int i = 0; i < list.size(); i++) {

            List<Map<String, Object>> list2= (List<Map<String, Object>>) list.get(i);
            for (int j = 0; j < list2.size(); j++) {
                Map<String,Object> map = list2.get(j);
                rq=map.get("rq").toString();
                xq=map.get("xq").toString();
                intRq=Integer.parseInt(rq);
                if(intRq<10){
                    rq1="0"+rq;
                }else{
                    rq1=rq;
                }
                time=yearMonth+"-"+rq1;

                if (compareTime(nowData,time,df_rq)>0){
                    swsb=map.get("swsb").toString();
                    swxb=map.get("swxb").toString();
                    xwsb=map.get("xwsb").toString();
                    xwxb=map.get("xwxb").toString();
                    //�����ʱ���ϰ࣬ǰ̨����ʾ�޴򿪼�¼
                    if(!CalculateHoliday(time) && !"��".equals(xq)){
                        if ("��".equals(swsb)){
                            map.put("swsb","����");
                        }if ("��".equals(swxb) ){
                            map.put("swxb","����");
                        }if ("��".equals(xwsb) ){
                            map.put("xwsb","����");
                        }if("��".equals(xwxb) ){
                            map.put("xwxb","����");
                        }
                    }
                    if("��".equals(xq)&&CalculateSH(time)){
                        if ("��".equals(swsb)){
                            map.put("swsb","����");
                        }if ("��".equals(swxb) ){
                            map.put("swxb","����");
                        }if ("��".equals(xwsb) ){
                            map.put("xwsb","����");
                        }if("��".equals(xwxb) ){
                            map.put("xwxb","����");
                        }
                    }
                }
            }
        }
        return list;
    }


    /**
     * ����ڼ���ʱ�䴦��(��Ҫ�ϰ࣬���ܼӰ�)
     * @param dataTime
     * @return
     * @throws ParseException
     */
    public static boolean CalculateSH(String dataTime) throws ParseException{
        boolean flag=false;
        //�ϰ�����
        String[] holiday = new String[]{"09-08"};
        Calendar now = Calendar.getInstance();
        //��ȡ��ǰ���
        int year=now.get(Calendar.YEAR);
        List<String> list = new ArrayList<String>();
        for (String string : holiday) {
            string=year+"-"+string;
            list.add(string);
        }
        flag=list.contains(dataTime);
        return flag;
    }


    /**
     * ����ڼ���ʱ�䴦��(����Ҫ�ϰ�)
     * @param dataTime
     * @return
     * @throws ParseException
     */
    public static boolean CalculateHoliday(String dataTime) throws ParseException{
        boolean flag=false;
        //����Ҫ�ϰ�����
        String[] holiday = new String[]{"09-13","09-14"};
        Calendar now = Calendar.getInstance();
        //��ȡ��ǰ���
        int year=now.get(Calendar.YEAR);
        List<String> list = new ArrayList<String>();
        for (String string : holiday) {
            string=year+"-"+string;
            list.add(string);
        }
        flag=list.contains(dataTime);
        return flag;
    }

    //�ж��Ƿ�Ϊ��
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "��".equals(str)) {
            return true;
        }
        return false;
    }

    //����ʱ��Ƚϴ�С
    private long compareTime(String t1, String t2, DateFormat df)
            throws ParseException {
        long diff = 0L;
        if ((!isEmpty(t1)) && (!isEmpty(t2))) {
            Date d1 = df.parse(t1);
            Date d2 = df.parse(t2);
            diff = d1.getTime() - d2.getTime();
        }

        return diff;
    }




}
