package org.yadu.untill;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 判斷日期是否是上班
 */
public class CalculationKq {

    public List<Map<String, Object>>  calKq(List<Map<String, Object>> list, HttpServletRequest req) throws ParseException {

        //获取查询参数
        String yearMonth = req.getParameter("cxsj");


        /**
         *  上午上班
         */
        String swsb="";
        /**
         * 上午下班
         */
        String swxb="";

        /**
         *下午上班
         */
        String xwsb="";
        /**
         * 下午下班
         */
        String xwxb="";
        /**
         * 星期几
         */
        String xq="";
        /**
         * 日期
         */
        String rq="";
        String rq1="";
        /**
         * 時間
         */
        String time="";
        int intRq=0;
        /**
         * 部门
         */
        String bm="";
        /**
         * 姓名
         */
        String xm="";
        /**
         * 工号
         */
        String gh="";

        /**
         *  获取当前时间
         */
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        String nowData = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        for (int i = 0; i < list.size(); i++) {
            /**
             * 如果没有填写请假单，人为修改请假天数
             */
            float a=0;
            /**
             * 请假天数
             */
            String qjts="";
            /**
             * 旷工天数
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

                    //如果该时间上班，前台则显示无打开记录
                    if(!CalculateHoliday(time) && !"日".equals(xq)){
                        if ("无".equals(swsb)){
                            kgts+=0.25;
                            map.put("swsb","旷工");
                            map.put("kgts",kgts);
                        }if ("无".equals(swxb) ){
                            kgts+=0.25;
                            map.put("swxb","旷工");
                            map.put("kgts",kgts);
                        }if ("无".equals(xwsb) ){
                            kgts+=0.25;
                            map.put("xwsb","旷工");
                            map.put("kgts",kgts);
                        }if("无".equals(xwxb) ){
                            kgts+=0.25;
                            map.put("xwxb","旷工");
                            map.put("kgts",kgts);
                        }
                    }
                    if("日".equals(xq)&&CalculateSH(time)){
                        if ("无".equals(swsb)){
                            kgts+=0.25;
                            map.put("swsb","旷工");
                            map.put("kgts",kgts);
                        }if ("无".equals(swxb) ){
                            kgts+=0.25;
                            map.put("swxb","旷工");
                            map.put("kgts",kgts);
                        }if ("无".equals(xwsb) ){
                            kgts+=0.25;
                            map.put("xwsb","旷工");
                            map.put("kgts",kgts);
                        }if("无".equals(xwxb) ){
                            kgts+=0.25;
                            map.put("xwxb","旷工");
                            map.put("kgts",kgts);
                        }
                    }

                    //特殊人员特殊日期处理
                    if("信息部".equals(bm)){
                       if ("00167".equals(gh)){
                           boolean flag=false;
                           String[] holiday = new String[]{"08-13","08-14","08-15","08-16","08-17"};
                           Calendar now = Calendar.getInstance();
                           //获取当前年份
                           int year=now.get(Calendar.YEAR);
                           List<String> li = new ArrayList<String>();
                           for (String string : holiday) {
                               string=year+"-"+string;
                               li.add(string);
                           }
                           if(li.contains(time)){
                               if ("无".equals(swsb)){
                                   a+=0.25;
                                   map.put("swsb","事假");
                               }if ("无".equals(swxb) ){
                                   a+=0.25;
                                   map.put("swxb","事假");
                               }if ("无".equals(xwsb) ){
                                   a+=0.25;
                                   map.put("xwsb","事假");
                               }if("无".equals(xwxb) ){
                                   a+=0.25;
                                   map.put("xwxb","事假");
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
     * 特殊节假日时间处理(需要上班，可能加班)
     * @param dataTime
     * @return
     * @throws ParseException
     */
    public static boolean CalculateSH(String dataTime) throws ParseException{
        boolean flag=false;
        //上班日期
        String[] holiday = new String[]{"09-08"};
        Calendar now = Calendar.getInstance();
        //获取当前年份
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
     * 特殊节假日时间处理(不需要上班)
     * @param dataTime
     * @return
     * @throws ParseException
     */
    public static boolean CalculateHoliday(String dataTime) throws ParseException{
        boolean flag=false;
        //不需要上班日期
        String[] holiday = new String[]{"09-13","09-14"};
        Calendar now = Calendar.getInstance();
        //获取当前年份
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
     * 获取前两天
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
        //                      此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE,day-2);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * 判读是否为空
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "无".equals(str)) {
            return true;
        }
        return false;
    }


    /**
     * 日期时间比较大小
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
