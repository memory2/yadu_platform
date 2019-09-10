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
         * ����
         */
        String bm="";
        /**
         * ����
         */
        String xm="";
        /**
         * ����
         */
        String gh="";

        /**
         *  ��ȡ��ǰʱ��
         */
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        String nowData = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        for (int i = 0; i < list.size(); i++) {
            /**
             * ���û����д��ٵ�����Ϊ�޸��������
             */
            float a=0;
            /**
             * �������
             */
            String qjts="";
            /**
             * ��������
             */
            float kgts=0;
            List<Map<String, Object>> list2= (List<Map<String, Object>>) list.get(i);
            for (int j = 0; j < list2.size(); j++) {
                Map<String,Object> map = list2.get(j);
                rq=map.get("rq").toString();
                xq=map.get("xq").toString();
                bm=map.get("bm").toString();
                gh=map.get("gh").toString();
                intRq=Integer.parseInt(rq);
                if(intRq<10){
                    rq1="0"+rq;
                }else{
                    rq1=rq;
                }
                time=yearMonth+"-"+rq1;
                if (compareTime(getLastDay(nowData),time,df_rq)>0){
                    swsb=map.get("swsb").toString();
                    swxb=map.get("swxb").toString();
                    xwsb=map.get("xwsb").toString();
                    xwxb=map.get("xwxb").toString();
                    xm=map.get("xm").toString();
                    qjts=map.get("qjts").toString();

                    //�����ʱ���ϰ࣬ǰ̨����ʾ�޴򿪼�¼
                    if(!CalculateHoliday(time) && !"��".equals(xq)){
                        if ("��".equals(swsb)){
                            kgts+=0.25;
                            map.put("swsb","����");
                            map.put("kgts",kgts);
                        }if ("��".equals(swxb) ){
                            kgts+=0.25;
                            map.put("swxb","����");
                            map.put("kgts",kgts);
                        }if ("��".equals(xwsb) ){
                            kgts+=0.25;
                            map.put("xwsb","����");
                            map.put("kgts",kgts);
                        }if("��".equals(xwxb) ){
                            kgts+=0.25;
                            map.put("xwxb","����");
                            map.put("kgts",kgts);
                        }
                    }
                    if("��".equals(xq)&&CalculateSH(time)){
                        if ("��".equals(swsb)){
                            kgts+=0.25;
                            map.put("swsb","����");
                            map.put("kgts",kgts);
                        }if ("��".equals(swxb) ){
                            kgts+=0.25;
                            map.put("swxb","����");
                            map.put("kgts",kgts);
                        }if ("��".equals(xwsb) ){
                            kgts+=0.25;
                            map.put("xwsb","����");
                            map.put("kgts",kgts);
                        }if("��".equals(xwxb) ){
                            kgts+=0.25;
                            map.put("xwxb","����");
                            map.put("kgts",kgts);
                        }
                    }

                    //������Ա�������ڴ���
                    if("��Ϣ��".equals(bm)){
                       if ("00167".equals(gh)){
                           boolean flag=false;
                           String[] holiday = new String[]{"08-13","08-14","08-15","08-16","08-17"};
                           Calendar now = Calendar.getInstance();
                           //��ȡ��ǰ���
                           int year=now.get(Calendar.YEAR);
                           List<String> li = new ArrayList<String>();
                           for (String string : holiday) {
                               string=year+"-"+string;
                               li.add(string);
                           }
                           if(li.contains(time)){
                               if ("��".equals(swsb)){
                                   a+=0.25;
                                   map.put("swsb","�¼�");
                               }if ("��".equals(swxb) ){
                                   a+=0.25;
                                   map.put("swxb","�¼�");
                               }if ("��".equals(xwsb) ){
                                   a+=0.25;
                                   map.put("xwsb","�¼�");
                               }if("��".equals(xwxb) ){
                                   a+=0.25;
                                   map.put("xwxb","�¼�");
                               }
                               kgts=0;
                           }
                       }
                    }

                }
                if ("00167".equals(gh)){
                    map.put("qjts",a);
                }
                map.put("kgts",kgts);
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


    /**
     * ��ȡǰ����
     * @param time
     * @return
     */
    public static String getLastDay(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date=null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        //                      �˴��޸�Ϊ+1���ǻ�ȡ��һ��
        calendar.set(Calendar.DATE,day-2);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * �ж��Ƿ�Ϊ��
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "��".equals(str)) {
            return true;
        }
        return false;
    }


    /**
     * ����ʱ��Ƚϴ�С
     * @param t1
     * @param t2
     * @param df
     * @return
     * @throws ParseException
     */
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
