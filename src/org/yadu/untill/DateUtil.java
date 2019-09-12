package org.yadu.untill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 */
public class DateUtil {
    public static void main(String[] args) {
        try {
            System.out.println("有" + getMondayNumber("2019-09-01","2019-09-30",6) + "个"+getMonthFirstday("2019-09-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查询日期间有几天一周中的某一天
     * 日期格式 yyyy-MM-dd yyyy-MM-dd 1-7(表示周一到周日) 
     * @param startDay 准备查询的起始日期
     * @param endDay 准备查询的结束日期
     * @param dayOfWeek 准备查的一周中的某一天(准备查周几？)
     * @return 包含所查周几的天数
     * @throws ParseException 不支持跨年查询、不支持结束日期早于起始日期、周几输入错误等
     */
    public static int getMondayNumber(String startDay,String endDay,int dayOfWeek) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int differenceDay = 0;
        //转换起始日期
        Date startDate = sdf.parse(startDay);
        //转换结束日期
        Date endDate = sdf.parse(endDay); 
        //实例化起始和结束Calendar对象
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        //分别设置Calendar对象的时间
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);

        //定义起始日期和结束日期分别属于第几周
        int startWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
        int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);
        
        //拿到起始日期是星期几
        int startDayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
        if(startDayOfWeek == 1)    {
            startDayOfWeek = 7;
            startWeek--;
        }else{
            startDayOfWeek--;
        }
        
        //拿到结束日期是星期几
        int endDayOfWeek = endCalendar.get(Calendar.DAY_OF_WEEK);
        if(endDayOfWeek == 1) {
            endDayOfWeek = 7;
            endWeek--;
        }else{
            endDayOfWeek--;
        }
        
        //计算相差的周数
        int differenceWeek = endWeek - startWeek;
        
        //开始计算
        if(startDayOfWeek <= dayOfWeek) {
            if(endDayOfWeek >= dayOfWeek){
                differenceDay = differenceWeek + 1;
            }
        }if(startDayOfWeek > dayOfWeek) {
            if(endDayOfWeek < dayOfWeek){
                differenceDay = differenceWeek-1;
            }
        }else {
            differenceDay = differenceWeek;
        }
        return differenceDay;
    }


    /**
     * 获取指定月份第一天
     * @return
     */
    public static String getMonthFirstday(String time){
        int year=Integer.parseInt(time.substring(0,4));
        int month=Integer.parseInt(time.substring(5,7));
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        //获取指定年月第一天?
        Calendar calstar= Calendar.getInstance();
        calstar.set(Calendar.YEAR, year);
        calstar.set(Calendar.MONTH, month-1);
        calstar.set(Calendar.DAY_OF_MONTH, 1);
        String star =sm.format(calstar.getTime());
        return star;
    }

    /**
     * 获取指定月份最后一天
     * @return
     */
    public static String getMonthlastday(String time){
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        int year=Integer.parseInt(time.substring(0,4));
        int month=Integer.parseInt(time.substring(5,7));
        //获取指定年月第一天?
        Calendar calstar= Calendar.getInstance();
        calstar.set(Calendar.YEAR, year);
        calstar.set(Calendar.MONTH, month);
        calstar.set(Calendar.DAY_OF_MONTH, 1);
        //最后一天
        calstar.set(Calendar.DAY_OF_MONTH, 0);
        String end =sm.format(calstar.getTime());
        return  end;
    }

}