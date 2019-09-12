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
            System.out.println("��" + getMondayNumber("2019-09-01","2019-09-30",6) + "��"+getMonthFirstday("2019-09-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ��ѯ���ڼ��м���һ���е�ĳһ��
     * ���ڸ�ʽ yyyy-MM-dd yyyy-MM-dd 1-7(��ʾ��һ������) 
     * @param startDay ׼����ѯ����ʼ����
     * @param endDay ׼����ѯ�Ľ�������
     * @param dayOfWeek ׼�����һ���е�ĳһ��(׼�����ܼ���)
     * @return ���������ܼ�������
     * @throws ParseException ��֧�ֿ����ѯ����֧�ֽ�������������ʼ���ڡ��ܼ���������
     */
    public static int getMondayNumber(String startDay,String endDay,int dayOfWeek) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int differenceDay = 0;
        //ת����ʼ����
        Date startDate = sdf.parse(startDay);
        //ת����������
        Date endDate = sdf.parse(endDay); 
        //ʵ������ʼ�ͽ���Calendar����
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        //�ֱ�����Calendar�����ʱ��
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);

        //������ʼ���ںͽ������ڷֱ����ڵڼ���
        int startWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
        int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);
        
        //�õ���ʼ���������ڼ�
        int startDayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
        if(startDayOfWeek == 1)    {
            startDayOfWeek = 7;
            startWeek--;
        }else{
            startDayOfWeek--;
        }
        
        //�õ��������������ڼ�
        int endDayOfWeek = endCalendar.get(Calendar.DAY_OF_WEEK);
        if(endDayOfWeek == 1) {
            endDayOfWeek = 7;
            endWeek--;
        }else{
            endDayOfWeek--;
        }
        
        //������������
        int differenceWeek = endWeek - startWeek;
        
        //��ʼ����
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
     * ��ȡָ���·ݵ�һ��
     * @return
     */
    public static String getMonthFirstday(String time){
        int year=Integer.parseInt(time.substring(0,4));
        int month=Integer.parseInt(time.substring(5,7));
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        //��ȡָ�����µ�һ��?
        Calendar calstar= Calendar.getInstance();
        calstar.set(Calendar.YEAR, year);
        calstar.set(Calendar.MONTH, month-1);
        calstar.set(Calendar.DAY_OF_MONTH, 1);
        String star =sm.format(calstar.getTime());
        return star;
    }

    /**
     * ��ȡָ���·����һ��
     * @return
     */
    public static String getMonthlastday(String time){
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        int year=Integer.parseInt(time.substring(0,4));
        int month=Integer.parseInt(time.substring(5,7));
        //��ȡָ�����µ�һ��?
        Calendar calstar= Calendar.getInstance();
        calstar.set(Calendar.YEAR, year);
        calstar.set(Calendar.MONTH, month);
        calstar.set(Calendar.DAY_OF_MONTH, 1);
        //���һ��
        calstar.set(Calendar.DAY_OF_MONTH, 0);
        String end =sm.format(calstar.getTime());
        return  end;
    }

}