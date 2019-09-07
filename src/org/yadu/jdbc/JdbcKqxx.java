package org.yadu.jdbc;

import org.yadu.dao.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class JdbcKqxx {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:mysql://10.0.10.30:3306/hwatt";
    private PoolManager pm = null;
    private Connection con_oa;
    private Connection con_kq;
    private Connection con_u8_005;
    private Connection con_u8_005_gfz;
    private Connection con_u8_009;
    private Connection con_u8_002;
    private Connection con_u8_010;

    //加载mysql驱动，OA数据库连接池
    public JdbcKqxx() {
        try {
            pm = new PoolManager();
        } catch (Exception localException) {
            System.out.println("创建连接池对象异常！");
        }
    }

    //获取考勤记录数据库连接
    public Connection getKqjlCon() throws ClassNotFoundException {
        try {
            Class.forName(DRIVER);
            this.con_kq = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("获取考勤数据库连接异常！");
            e.printStackTrace();
        }
        return this.con_kq;
    }

    //--------------------------亚都考勤----------------------------
    //亚都--行政人员考勤--电脑端
    public List ydKqxx_pc(HttpServletRequest req, String method) throws Exception {

        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }

        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String cxbm = req.getParameter("cxbm");
        String cxxm = req.getParameter("cxxm");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int myts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);

        //根据查询部门获取过滤条件
        String whereSql_oary = "";
        if (cxbm.endsWith("00000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 2) + "%'";
        } else if (cxbm.endsWith("000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 4) + "%'";
            //whereSql_oary+=" and b.bmid like '"+cxbm.substring(0,2)+"%'";
        } else if (cxbm.endsWith("0000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 6) + "%'";
        } else if (cxbm.endsWith("00")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 8) + "%'";
        } else {
            whereSql_oary += " and b.bmid = '" + cxbm + "'";
        }

        //根据查询姓名获取过滤条件
        if (!isEmpty(cxxm)) {
            whereSql_oary += " and p.id = '" + cxxm + "' ";
            //whereSql_oary += " and p.id = '10537' ";
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");
        System.out.println("开始1：" + df_rqsj.format(new Date()));

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_oary = "select p.stuff_id  as gh,p.per_sort as ryxh,b.bmmc,p.truename as ryxm,p.id as oaryid from person p,t_ydbm b where p.isaway = '0' and b.oa_bmid = p.dep_id  and p.id!='10554' " + whereSql_oary;
        Statement st_oary = this.con_oa.createStatement();
        ResultSet rs_oary = st_oary.executeQuery(sql_oary);


        int bs = 0;
        while (rs_oary.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_oary.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String bmmc = rs_oary.getString("bmmc");
            String ryxm = rs_oary.getString("ryxm");
            String oaryid = rs_oary.getString("oaryid");
            String ryxh = rs_oary.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            int ydxqt = 0;
            boolean zj_flag = false;
            for (int i = 1; i <= myts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                //String Sql_kqjl = "select e.employeeCode as kqh,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime like '" +  fdate + "%' " + whereSql_kqjl;
                String Sql_kqjl = "select e.employeeCode as kqh,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and   t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;
                //System.out.println(Sql_kqjl);
                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    map.put("xm", ryxm);
                    map.put("gh", gh);
                    map.put("bm", bmmc);
                    if ("日".equals(df_xq.format(date).substring(2, 3))) {
                        ydxqt++;
                    }

                    String dksj = rs_kqjl.getString("dksj");

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='41' and t.rq= '" + fdate + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("xwsb");
                                    String xw2 = (String) map.get("xwxb");
                                    if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }


                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("xwsb");
                                String xw2 = (String) map.get("xwxb");
                                if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }


                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    if (!"周末".equals(ts_swsb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        swsbwqd = (float) (swsbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    if (!"周末".equals(ts_swxb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        swxbwqd = (float) (swxbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    if (!"周末".equals(ts_xwsb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        xwsbwqd = (float) (xwsbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    if (!"周末".equals(ts_xwxb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        xwxbwqd = (float) (xwxbwqd + 0.25D);
                                    }

                                }
                            }


                        }

                    }


                    //总监免签
                    if (("02802".equals(gh)) || ("03598".equals(gh)) || ("00034".equals(gh)) || ("00003".equals(gh)) || ("03536".equals(gh)) || ("00004".equals(gh)) || ("03789".equals(gh)) || ("04560".equals(gh)) || ("00126".equals(gh)) ||
                            ("03893".equals(gh)) || ("00011".equals(gh)) || ("04498".equals(gh)) || ("02670".equals(gh) && compareTime(fdate, "2017-02-01", df_rq) >= 0L) || ("00102".equals(gh) && compareTime(fdate, "2017-02-01", df_rq) >= 0L)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, fdate + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if ("00102".equals(gh)) {
                            if (compareTime(currTime, fdate + " 18:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwxb"))) {
                                map.put("xwxb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (isSummer(df_rq.format(date), df_rq)) {
                                if (compareTime(currTime, fdate + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "免签");
                                    mqcs = (float) (mqcs + 0.25D);
                                }
                            } else {
                                if (compareTime(currTime, fdate + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "免签");
                                    mqcs = (float) (mqcs + 0.25D);
                                }
                            }

                        }
                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(fdate, df_rq)) { //begin 亚都夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "早退" + dk[j]);
                                        ztcs++;
                                        swxbztts = (float) (swxbztts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "14:01:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "14:01:00", df_sj) > 0L && compareTime(dk[j], "14:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "14:15:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "18:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "18:00:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            } else { // end 亚都夏季打卡时间/begin 亚都冬季打卡时间

                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "11:30:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        if ("2019-04-29".equals(fdate)) {//运动会提前下班
                                            map.put("swxb", "正常" + dk[j]);
                                            swxbzcts = (float) (swxbzcts + 0.25D);
                                        } else {
                                            map.put("swxb", "早退" + dk[j]);
                                            ztcs++;
                                            swxbztts = (float) (swxbztts + 0.25D);
                                        }

                                    }

                                } else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "15:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        if ("2019-01-28".equals(fdate) || "2019-01-31".equals(fdate) || "2019-02-19".equals(fdate)) {//祭灶提前下班
                                            map.put("xwxb", "正常" + dk[j]);
                                            xwxbzcts = (float) (xwxbzcts + 0.25D);
                                        } else {
                                            map.put("xwxb", "早退" + dk[j]);
                                            ztcs++;
                                            xwxbztts = (float) (xwxbztts + 0.25D);
                                        }

                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            }// end 亚都冬季打卡时间

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));

                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u where u.field1 = '"+oaryid+"' and u.field2 = '"+fdate+"' and u.flowid in (select id from document d where d.state='2' )";
                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u,document d where u.field1 = '"+oaryid+"' and u.field2 = '"+fdate+"' and d.state = '2' and u.flowid=d.id";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field2 = '" + fdate + "' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意')  and u.flowid=d.doc_id and n.mem_id = d.id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("1".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("2".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    //oa请假
                    //String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00145 u where u.field1 = '"+oaryid+"' and u.field6 <= '"+fdate+"' and u.field7>= '"+fdate+"' and u.flowid in (select id from document d where d.state='2')";
                    //String sql_oaqj = "select u.field4 as qjlb,u.field17 as kssj,u.field18 as jssj from utm_00145 u,document d where u.field1 = '"+oaryid+"' and u.field6 <= '"+fdate+"' and u.field7>= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj from utm_00145 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field6 <= '" + fdate + "' and u.field7>= '" + fdate + "' and  d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意')  and u.flowid = d.doc_id and n.mem_id = d.id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    // System.out.println(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //下午+上午
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                if ("10343".equals(oaryid) && "2017-12-14".equals(fdate)) {
                                    lbmc = "无";
                                } else {
                                    lbmc = "事假";
                                    qjts += 0.25f;
                                }

                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {

                                if ("10343".equals(oaryid) && "2017-12-14".equals(fdate)) {
                                    lbmc = "无";
                                } else {
                                    lbmc = "事假";
                                    qjts += 0.25f;
                                }

                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }

                    //System.out.println("**************"+qjts);
                    //oa出差
                    //String sql_oacc = "select u.field3,u.id from utm_00101 u where u.field3 = '"+oaryid+"' and u.field7 >= '"+fdate+"' and u.field6<= '"+fdate+"' and u.flowid in (select id from document d where d.state='2' )";
                    //String sql_oacc = "select u.field3,u.id from utm_00101 u,document d where u.field3 = '"+oaryid+"' and u.field7 >= '"+fdate+"' and u.field6<= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    // String sql_oacc = "select u.field3,u.id from utm_00101 u,flownode_member d where u.field3 = '"+oaryid+"' and u.field7 >= '"+fdate+"' and u.field6<= '"+fdate+"' and  d.entity = '10665' and d.workflag = '1' and u.flowid = d.doc_id";//最后审批后不同意也显示
                    String sql_oacc = "select u.field3,u.id from utm_00101 u,flownode_member d,nodetactics n where u.field3 = '" + oaryid + "' and u.field7 >= '" + fdate + "' and u.field6<= '" + fdate + "' and  d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意')  and u.flowid = d.doc_id and n.mem_id = d.id";

                    //System.out.println(sql_oacc);
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    //String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u where u.field2 = '"+oaryid+"' and u.field10 >= '"+fdate+" 00:00:01' and u.field10 <='"+fdate+" 23:59:59 ' and u.flowid in (select id from document d where d.state = '2')";
                    //  String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u,document d where u.field2 = '"+oaryid+"' and u.field10 >= '"+fdate+" 00:00:01' and u.field10 <='"+fdate+" 23:59:59 ' and d.state = '2' and u.flowid = d.id ";
                    String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u,flownode_member d,nodetactics n where u.field2 = '" + oaryid + "' and u.field10 >= '" + fdate + " 00:00:00' and u.field10 <='" + fdate + " 23:59:59 ' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意')  and u.flowid = d.doc_id and n.mem_id = d.id GROUP BY u.field10,u.field12";
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }

                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = myts - ydxqt - tdts - gxts;
                    if ("2019-04".equals(cxsj)) {
                        mqts = 25.5f;
                    }
                    if ("2019-05".equals(cxsj)) {
                        mqts = 25f;
                    }
                    if ("2019-06".equals(cxsj)) {
                        mqts = 24f;
                    }
                    // System.out.println(mqts+"--"+myts+"--"+tdts+"--"+gxts);
                    float kqzs = 0.0F;

                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = myts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":人员姓名："+ryxm+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));

                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println("开始4："+df_rqsj.format(new Date()));
        con_oa.close();
        con_kq.close();
        //System.out.println(lt);
        return lt;
    }

    //亚都--行政人员考勤--微信端
    public List ydKqxx_wx(HttpServletRequest req, String method) throws Exception {
        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }
        System.out.println("亚都微信1：");


        List lt = new ArrayList();
        String gfz_wxid = "'o3l2lxDKVihm8Z7VdYXR968KqjDw','o3l2lxAqF9md6wMTyZpjrXn30Q8Q','o3l2lxDJpq5IhNnQ6BkZRRTcKsgE','o3l2lxILKq4GTwynhkM7xqsTKw1E','o3l2lxIimHBzcrl7SR5DwWLdvlgQ','o3l2lxPRR3bgHvZGpWAyDsys5TUo'";

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String wxid = req.getParameter("wxid");
        String gslb = req.getParameter("gslb");
        if (gfz_wxid.contains(wxid)) {
            if ((this.con_u8_005_gfz == null) || (this.con_u8_005_gfz.isClosed())) {
                this.con_u8_005_gfz = new JdbcUtilsRy().getConnection("wps");
                System.out.println("亚都微信2_gfz：");
            }
        } else {
            if ((this.con_u8_005 == null) || (this.con_u8_005.isClosed())) {
                this.con_u8_005 = new JdbcUtilsRy().getConnection("ydsy");
                System.out.println("亚都微信2：");
            }
        }

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int ts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);
        int byts = ts;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        if (cxsj_y_int == month) {
            ts = day;
        }

        String whereSql_wxid = "";
        //根据微信ID获取过滤条件
        if (!isEmpty(wxid)) {
            whereSql_wxid += " and p.cPsnURL = '" + wxid + "' ";
            ;
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_u8ry = "select p.JobNumber as gh,p.ctitle as ryxh,p.cDept_num as dwid,t2.cDepName as dwmc from hr_hi_person p,Department t2 where p.rEmployState = '10' and  t2.cDepCode = p.cDept_num " + whereSql_wxid;
        Statement st_u8ry = null;
        if (!gfz_wxid.contains(wxid)) {
            st_u8ry = this.con_u8_005.createStatement();
        } else {
            st_u8ry = this.con_u8_005_gfz.createStatement();
        }
        ResultSet rs_u8ry = st_u8ry.executeQuery(sql_u8ry);
        int bs = 0;
        while (rs_u8ry.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_u8ry.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String dwmc = rs_u8ry.getString("dwmc");
            String dwid = rs_u8ry.getString("dwid");
            String ryxh = rs_u8ry.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            int ydxqt = 0;
            boolean zj_flag = false;
            for (int i = 1; i <= ts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,e.employeeName as xm,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;
                //System.out.println(Sql_kqjl);

                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    String ryxm = rs_kqjl.getString("xm");
                    map.put("gh", gh);
                    map.put("bm", dwmc);
                    map.put("xm", ryxm);
                    if ("日".equals(df_xq.format(date).substring(2, 3))) {
                        ydxqt++;
                    }

                    String dksj = rs_kqjl.getString("dksj");

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='41' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("xwsb");
                                    String xw2 = (String) map.get("xwxb");
                                    if ((!isEmpty(sw1) && sw1.contains("事假")) || (!isEmpty(sw2) && sw2.contains("事假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("事假")) || (!isEmpty(xw2) && xw2.contains("事假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }


                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("xwsb");
                                String xw2 = (String) map.get("xwxb");
                                if ((!isEmpty(sw1) && sw1.contains("事假")) || (!isEmpty(sw2) && sw2.contains("事假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("事假")) || (!isEmpty(xw2) && xw2.contains("事假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

	/*	  		              if (isEmpty(sw1)&&!isEmpty(ts_swsb)&&!"出差".equals((String)map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
			  		                map.put("swsb", ts_swsb);
			  		                gxts = (float)(gxts + 0.25D);
			  		              }
			  		              if (isEmpty(sw2)&&!isEmpty(ts_swxb)&&!"出差".equals((String)map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
			  		                map.put("swxb", ts_swxb);
			  		                gxts = (float)(gxts + 0.25D);
			  		              }
			  		              if (isEmpty(xw1)&&!isEmpty(ts_xwsb)&&!"出差".equals((String)map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
			  		                map.put("xwsb", ts_xwsb);
			  		                gxts = (float)(gxts + 0.25D);
			  		              }
			  		              if (isEmpty(xw2)&&!isEmpty(ts_xwxb)&&!"出差".equals((String)map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
			  		                map.put("xwxb", ts_xwxb);
			  		                gxts = (float)(gxts + 0.25D);
			  		              }*/

                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    if (!"周末".equals(ts_swsb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        swsbwqd = (float) (swsbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    if (!"周末".equals(ts_swxb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        swxbwqd = (float) (swxbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    if (!"周末".equals(ts_xwsb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        xwsbwqd = (float) (xwsbwqd + 0.25D);
                                    }

                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    if (!"周末".equals(ts_xwxb)) {
                                        gxts = (float) (gxts + 0.25D);
                                    } else {
                                        xwxbwqd = (float) (xwxbwqd + 0.25D);
                                    }

                                }
                            }


                        }

                    }

                    //总监免签
                    if (("02802".equals(gh)) || ("03598".equals(gh)) || ("00034".equals(gh)) || ("00003".equals(gh)) || ("03536".equals(gh)) || ("00004".equals(gh)) || ("03789".equals(gh)) || ("04560".equals(gh)) || ("00126".equals(gh)) ||
                            ("03893".equals(gh)) || ("00011".equals(gh)) || ("04498".equals(gh)) || ("02670".equals(gh) && compareTime(df_rq.format(date), "2017-02-01", df_rq) >= 0L) || ("00102".equals(gh) && compareTime(df_rq.format(date), "2017-02-01", df_rq) >= 0L)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if ("00102".equals(gh)) {
                            if (compareTime(currTime, df_rq.format(date) + " 18:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwxb"))) {
                                map.put("xwxb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (isSummer(df_rq.format(date), df_rq)) {
                                if (compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "免签");
                                    mqcs = (float) (mqcs + 0.25D);
                                }
                            } else {
                                if (compareTime(currTime, df_rq.format(date) + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "免签");
                                    mqcs = (float) (mqcs + 0.25D);
                                }
                            }

                        }


                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(df_rq.format(date), df_rq)) { //begin 亚都夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "早退" + dk[j]);
                                        ztcs++;
                                        swxbztts = (float) (swxbztts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "14:01:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "14:01:00", df_sj) > 0L && compareTime(dk[j], "14:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "14:15:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "18:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "18:00:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            } else { // end 亚都夏季打卡时间/begin 亚都冬季打卡时间

                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "11:30:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb"))) {

                                        if ("2019-04-29".equals(fdate)) {//运动会提前下班
                                            map.put("swxb", "正常" + dk[j]);
                                            swxbzcts = (float) (swxbzcts + 0.25D);
                                        } else {
                                            map.put("swxb", "早退" + dk[j]);
                                            ztcs++;
                                            swxbztts = (float) (swxbztts + 0.25D);
                                        }

                                    }

                                } else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "15:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        if ("2019-01-28".equals(fdate) || "2019-01-31".equals(fdate) || "2019-02-19".equals(fdate)) {//祭灶提前下班
                                            map.put("xwxb", "正常" + dk[j]);
                                            xwxbzcts = (float) (xwxbzcts + 0.25D);
                                        } else {
                                            map.put("xwxb", "早退" + dk[j]);
                                            ztcs++;
                                            xwxbztts = (float) (xwxbztts + 0.25D);
                                        }
                                    }


                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            }// end 亚都冬季打卡时间

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));

                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u,person p where u.field1 = p.id and p.stuff_id = '"+gh+"' and u.field2 = '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id = '" + gh + "' and u.field2 = '" + df_rq.format(date) + "' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("1".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("2".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    //oa请假
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj  from utm_00145 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field6 <= '" + df_rq.format(date) + "' and u.field7>= '" + df_rq.format(date) + "' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //多个全天
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                if ("03502".equals(gh) && "2017-12-14".equals(fdate)) {
                                    lbmc = "无";
                                } else {
                                    lbmc = "事假";
                                    qjts += 0.25f;
                                }
                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                if ("03502".equals(gh) && "2017-12-14".equals(fdate)) {
                                    lbmc = "无";
                                } else {
                                    lbmc = "事假";
                                    qjts += 0.25f;
                                }
                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }


                    //oa出差
                    //String sql_oacc = "select u.field3,u.id from utm_00101 u,person p where u.field3 = p.id and p.stuff_id ='"+gh+"' and u.field7 >= '"+df_rq.format(date)+"' and u.field6<= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oacc = "select u.field3,u.id from utm_00101 u,person p,flownode_member d,nodetactics n where u.field3 = p.id and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field7 >= '" + df_rq.format(date) + "' and u.field6<= '" + df_rq.format(date) + "' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    // String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u,person p, where u.field2 = p.id and p.stuff_id = '"+gh+"' and u.field10 like '"+df_rq.format(date)+"%' and u.flowid in (select id from document d where d.state = '2') ";
                    String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u,person p,flownode_member d,nodetactics n where u.field2 = p.id and p.stuff_id = '" + gh + "' and u.field10 >= '" + fdate + " 00:00:00' and u.field10 <='" + fdate + " 23:59:59 ' and d.entity in ('10665','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意')  and u.flowid = d.doc_id and n.mem_id = d.id GROUP BY u.field10,u.field12";
                    //System.out.println("******************"+sql_oajb);
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }


                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = byts - ydxqt - tdts - gxts;
                    if ("2019-04".equals(cxsj)) {
                        mqts = 25.5f;
                    }
                    if ("2019-05".equals(cxsj)) {
                        mqts = 25f;
                    }
                    float kqzs = 0.0F;


                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = ts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":"+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));

                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println(lt);
        return lt;
    }

    //--------------------------威浦仕考勤----------------------------
    //威浦仕--行政人员考勤--电脑端
    public List wpsKqxx_pc(HttpServletRequest req, String method) throws Exception {

        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }

        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String cxbm = req.getParameter("cxbm");
        String cxxm = req.getParameter("cxxm");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        //System.out.println(cxsj);
        //System.out.println(cxsj_y);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int myts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);

        //根据查询部门获取过滤条件
        String whereSql_oary = "";
        if (cxbm.endsWith("00000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 2) + "%'";
        } else if (cxbm.endsWith("000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 4) + "%'";
            //whereSql_oary+=" and b.bmid like '"+cxbm.substring(0,2)+"%'";
        } else if (cxbm.endsWith("0000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 6) + "%'";
        } else if (cxbm.endsWith("00")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 8) + "%'";
        } else {
            whereSql_oary += " and b.bmid = '" + cxbm + "'";
        }

        //根据查询姓名获取过滤条件
        if (!isEmpty(cxxm)) {
            whereSql_oary += " and p.id = '" + cxxm + "' ";
            //whereSql_oary += " and p.id = '10537' ";
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");
        //System.out.println("开始1："+df_rqsj.format(new Date()));

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_oary = "select p.stuff_id  as gh,p.per_sort as ryxh,b.bmmc,p.truename as ryxm,p.id as oaryid from person p,t_wpsbm b where p.isaway = '0' and b.oa_bmid = p.dep_id  and p.id!='10554' " + whereSql_oary;
        Statement st_oary = this.con_oa.createStatement();
        ResultSet rs_oary = st_oary.executeQuery(sql_oary);


        int bs = 0;
        while (rs_oary.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_oary.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String bmmc = rs_oary.getString("bmmc");
            String ryxm = rs_oary.getString("ryxm");
            String oaryid = rs_oary.getString("oaryid");
            String ryxh = rs_oary.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            int wpsxqt = 0;
            boolean zj_flag = false;
            for (int i = 1; i <= myts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;


                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    map.put("xm", ryxm);
                    map.put("gh", gh);
                    map.put("bm", bmmc);
                    if ("日".equals(df_xq.format(date).substring(2, 3))) {
                        wpsxqt++;
                    }

                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;
                    boolean wpspd1 = false;//19年5月1-3号，按老规则打卡
                    if ("2019-05-01".equals(fdate) || "2019-05-02".equals(fdate) || "2019-05-03".equals(fdate) || (compareTime(fdate, "2019-06-04", df_rq) >= 0L) && bmmc.contains("车间")) {
                        wpspd1 = true;
                    }


                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00206 u where u.field1 = '"+oaryid+"' and u.field2 = '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00206 u,document d where u.field1 = '"+oaryid+"' and u.field2 = '"+fdate+"' and d.state = '2' and u.flowid=d.id";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00206 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field2 = '" + fdate + "' and d.entity  in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid=d.doc_id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if (isEmpty((String) map.get("swsb"))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                            wfsswsbwdk = true;
                        } else if ("1".equals(bkcx)) {
                            if (isEmpty((String) map.get("swxb"))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                            wfsswsbwdk = true;
                        } else if ("2".equals(bkcx)) {
                            if (isEmpty((String) map.get("xwsb"))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if (isEmpty((String) map.get("xwxb"))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='61' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                        wfsswsbwdk = true;
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }else{
                                        map.put("swsb", ts_swsb);
                                        wfsswsbwdk = true;
                                        map.put("swxb", ts_swxb);
                                        map.put("xwsb", ts_xwsb);
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("swsb");
                                    String xw2 = (String) map.get("swxb");
                                    if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }

                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                    wfsswsbwdk = true;
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("swsb");
                                String xw2 = (String) map.get("swxb");
                                if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                            }


                        }

                    }

                    //总监免签
                    if ("600070".equals(gh) || "600288".equals(gh) || "600645".equals(gh)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        //if (isSummer(df_rq.format(date), df_rq)) {
                        if (compareTime(currTime, df_rq.format(date) + " 13:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                            map.put("xwsb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        //}

                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(fdate, df_rq)) { //begin 威浦仕夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (wpspd1) {
                                        if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                            map.put("swxb", "正常" + dk[j]);
                                            swxbzcts = (float) (swxbzcts + 0.25D);
                                            wfsswsbwdk = false;
                                        } else {
                                            if (isEmpty((String) map.get("xwsb"))) {
                                                map.put("xwsb", "正常" + dk[j]);
                                                xwsbzcts = (float) (xwsbzcts + 0.25D);
                                            }
                                        }
                                    } else {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    }


                                } else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:35:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } else { // end 威浦仕夏季打卡/冬季考勤
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:01:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                } else if (compareTime(dk[j], "13:01:00", df_sj) > 0L && compareTime(dk[j], "13:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:15:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:05:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } // end 威浦仕冬季考勤

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));


                    //oa请假
                    // String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00205 u where u.field1 = '"+oaryid+"' and u.field6 <= '"+df_rq.format(date)+"' and u.field7>= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    //String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00205 u,document d where u.field1 = '"+oaryid+"' and u.field6 <= '"+fdate+"' and u.field7>= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj from utm_00205 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field6 <= '" + fdate + "' and u.field7>= '" + fdate + "' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //多个全天
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }

                    //oa出差
                    //String sql_oacc = "select u.field3,u.id from utm_00209 u where u.field3 = '"+oaryid+"' and u.field7 >= '"+df_rq.format(date)+"' and u.field6<= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    //   String sql_oacc = "select u.field3,u.id from utm_00209 u,document d where u.field3 = '"+oaryid+"' and u.field7 >= '"+fdate+"' and u.field6<= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    String sql_oacc = "select u.field3,u.id from utm_00209 u,flownode_member d,nodetactics n where u.field3 = '" + oaryid + "' and u.field7 >= '" + fdate + "' and u.field6<= '" + fdate + "' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id";
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    //String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00207 u where u.field2 = '"+oaryid+"' and u.field10 like '"+df_rq.format(date)+"%' and u.flowid in (select id from document d where d.state = '2')";
                    // String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00207 u,document d where u.field2 = '"+oaryid+"' and u.field10 >= '"+fdate+" 00:00:01' and u.field10 <='"+fdate+" 23:59:59 ' and d.state = '2' and u.flowid = d.id ";
                    String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00207 u,flownode_member d,nodetactics n where u.field2 = '" + oaryid + "' and u.field10 >= '" + fdate + " 00:00:00' and u.field10 <='" + fdate + " 23:59:59 ' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id ";
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }

                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = myts - wpsxqt - tdts - gxts;
                    if ("2018-02".equals(cxsj)) {
                        mqts = myts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;

                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = myts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+"::"+gxts+"::"+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));
                    map.put("xqtgs", wpsxqt);
                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println("开始4："+df_rqsj.format(new Date()));
        //System.out.println(lt);
        return lt;
    }

    //威浦仕--行政人员考勤--微信端
    public List wpsKqxx_wx(HttpServletRequest req, String method) throws Exception {
        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }
        System.out.println("威浦仕微信1：");
        if ((this.con_u8_009 == null) || (this.con_u8_009.isClosed())) {
            this.con_u8_009 = new JdbcUtilsRy().getConnection("wps");
            System.out.println("威浦仕微信2：");
        }
        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String wxid = req.getParameter("wxid");
        String gslb = req.getParameter("gslb");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int ts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);
        int byts = ts;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        if (cxsj_y_int == month) {
            ts = day;
        }


        String whereSql_wxid = "";
        //根据微信ID获取过滤条件
        if (!isEmpty(wxid)) {
            whereSql_wxid += " and p.cPsnURL = '" + wxid + "' ";
            ;
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");

        int wpsxqt = 0;
        //判断每月有几个周日
        for (int k = 1; k <= byts; k++) {
            Date d = df_rq.parse(cxsj + "-" + k);
            if ("日".equals(df_xq.format(d).substring(2, 3))) {
                wpsxqt++;
            }
        }
        System.out.println(wpsxqt);

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_u8ry = "select p.JobNumber as gh,p.ctitle as ryxh,p.cDept_num as dwid,t2.cDepName as dwmc from hr_hi_person p,Department t2 where p.rEmployState = '10' and  t2.cDepCode = p.cDept_num " + whereSql_wxid;
        Statement st_u8ry = this.con_u8_009.createStatement();
        ResultSet rs_u8ry = st_u8ry.executeQuery(sql_u8ry);
        int bs = 0;
        while (rs_u8ry.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_u8ry.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String dwmc = rs_u8ry.getString("dwmc");
            String dwid = rs_u8ry.getString("dwid");
            String ryxh = rs_u8ry.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            boolean zj_flag = false;

            //System.out.println(ts);
            for (int i = 1; i <= ts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,e.employeeName as xm,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;
                //System.out.println(Sql_kqjl);

                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    String ryxm = rs_kqjl.getString("xm");

                    map.put("gh", gh);
                    map.put("bm", dwmc);
                    //区分品管部王利和无纺布机折车间王利
                    if ("600659".equals(gh)) {
                        ryxm = "王利(车间)";
                    }
                    map.put("xm", ryxm);
                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;
                    boolean wpspd1 = false;//19年5月1-3号，按老规则打卡
                    if ("2019-05-01".equals(fdate) || "2019-05-02".equals(fdate) || "2019-05-03".equals(fdate) || (compareTime(fdate, "2019-06-04", df_rq) >= 0L) && dwmc.contains("车间")) {
                        wpspd1 = true;
                    }

                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00206 u,person p where u.field1 = p.id and p.stuff_id = '"+gh+"' and u.field2 = '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00206 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id = '" + gh + "' and u.field2 = '" + df_rq.format(date) + "' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                            wfsswsbwdk = true;
                        } else if ("1".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                            wfsswsbwdk = true;
                        } else if ("2".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='61' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                        wfsswsbwdk = true;
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }else{
                                        map.put("swsb", ts_swsb);
                                        wfsswsbwdk = true;
                                        map.put("swxb", ts_swxb);
                                        map.put("xwsb", ts_xwsb);
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("xwsb");
                                    String xw2 = (String) map.get("xwxb");
                                    if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }


                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                    wfsswsbwdk = true;
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("xwsb");
                                String xw2 = (String) map.get("xwxb");
                                if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                            }


                        }

                    }

                    //总监免签
                    if ("600070".equals(gh) || "600288".equals(gh) || "600645".equals(gh)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        //if (isSummer(df_rq.format(date), df_rq)) {
                        if (compareTime(currTime, df_rq.format(date) + " 13:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                            map.put("xwsb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        //}

                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(fdate, df_rq)) { //begin 威浦仕夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (!wpspd1) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                    } else {

                                        if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                            map.put("swxb", "正常" + dk[j]);
                                            swxbzcts = (float) (swxbzcts + 0.25D);
                                            wfsswsbwdk = false;
                                        } else {
                                            if (isEmpty((String) map.get("xwsb"))) {
                                                map.put("xwsb", "正常" + dk[j]);
                                                xwsbzcts = (float) (xwsbzcts + 0.25D);
                                            }
                                        }
                                    }


                                } else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "正常" + dk[j]);
                                        xwsbzcts = (float) (xwsbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:35:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) < 0L && compareTime(dk[j], "16:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } else { // end 威浦仕夏季打卡/冬季考勤
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:01:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                } else if (compareTime(dk[j], "13:01:00", df_sj) > 0L && compareTime(dk[j], "13:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:15:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:05:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) < 0L && compareTime(dk[j], "16:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } // end 威浦仕冬季考勤

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));


                    //oa请假
                    //String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00205 u,person p where u.field1 = p.id and p.stuff_id ='"+gh+"' and u.field6 <= '"+df_rq.format(date)+"' and u.field7>= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2' )";
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj from utm_00205 u,person p,flownode_member d,nodetactics n where u.field1 = p.id  and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field6 <= '" + df_rq.format(date) + "' and u.field7>= '" + df_rq.format(date) + "' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //多个全天
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }


                    //oa出差
                    //String sql_oacc = "select u.field3,u.id from utm_00209 u,person p where u.field3 = p.id and p.stuff_id ='"+gh+"' and u.field7 >= '"+df_rq.format(date)+"' and u.field6<= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oacc = "select u.field3,u.id from utm_00209 u,person p,flownode_member d,nodetactics n where u.field3 = p.id and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field7 >= '" + df_rq.format(date) + "' and u.field6<= '" + df_rq.format(date) + "' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    //String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00207 u,person p where u.field2 = p.id and p.stuff_id = '"+gh+"' and u.field10 like '"+df_rq.format(date)+"%' and u.flowid in (select id from document d where d.state = '2')";
                    String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00207 u,person p,flownode_member d,nodetactics n where u.field2 = p.id and u.flowid = d.doc_id and p.stuff_id = '" + gh + "' and u.field10 like '" + df_rq.format(date) + "%' and d.entity in ('10665','10930','11048','10005') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }

                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = byts - wpsxqt - tdts - gxts;
                    if ("2018-02".equals(cxsj)) {
                        mqts = byts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;


                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = ts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":"+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));
                    map.put("xqtgs", wpsxqt);
                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println(lt);
        return lt;
    }

    //威浦仕--车间人员考勤--电脑端
    public List wpsCjKqxx_pc(HttpServletRequest req, String method) throws Exception {

        if ((this.con_u8_009 == null) || (this.con_u8_009.isClosed())) {
            this.con_u8_009 = new JdbcUtilsRy().getConnection("wps");
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }

        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String cxbm = req.getParameter("cxbm");
        String cxxm = req.getParameter("cxxm");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int myts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);

        //根据查询部门获取过滤条件
        String whereSql_u8ry = "";
        if (cxbm.endsWith("00000000")) {
            whereSql_u8ry += " and b.bmid like '" + cxbm.substring(0, 2) + "%'";
        } else if (cxbm.endsWith("000000")) {
            whereSql_u8ry += " and b.bmid like '" + cxbm.substring(0, 4) + "%'";
        } else {
            whereSql_u8ry += " and b.bmid = '" + cxbm + "'";
        }

        //根据查询姓名获取过滤条件
        if (!isEmpty(cxxm)) {
            whereSql_u8ry += " and p.cpsn_num = '" + cxxm + "' ";
            //whereSql_oary += " and p.id = '10537' ";
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");
        //System.out.println("开始1："+df_rqsj.format(new Date()));

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询U8车间人员
        String sql_u8ry = "select p.jobNumber  as gh,b.bmmc,p.cpsn_name as ryxm,p.cpsn_num as u8ryid from hr_hi_person p,t_wpscjbm b where p.rEmployState = '10' and b.u8bmid = p.cdept_num  " + whereSql_u8ry;
        Statement st_u8ry = this.con_u8_009.createStatement();
        ResultSet rs_u8ry = st_u8ry.executeQuery(sql_u8ry);


        int bs = 0;
        while (rs_u8ry.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_u8ry.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String bmmc = rs_u8ry.getString("bmmc");
            String ryxm = rs_u8ry.getString("ryxm");
            String u8ryid = rs_u8ry.getString("u8ryid");
            whereSql_kqjl = " where e.employeeCode = '" + gh_int + "'";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            int wpsxqt = 0;
            boolean zj_flag = false;
            for (int i = 1; i <= myts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;


                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    map.put("xm", ryxm);
                    map.put("gh", gh);
                    map.put("bm", bmmc);
                    if ("日".equals(df_xq.format(date).substring(2, 3))) {
                        wpsxqt++;
                    }

                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;


                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='71' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    // System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                }
                            }
                        }

                        if ("all".equals(ts_member)) {
                            if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                    gxts += 0.5f;
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                    gxts += 0.5f;
                                }
                            }

                        }

                    }

                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(fdate, df_rq)) { //begin 威浦仕夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.5D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } else { // end 威浦仕夏季打卡/冬季考勤
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.5D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } // end 威浦仕冬季考勤

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }

                    if (isEmpty((String) map.get("swsb"))) {
                        swsbwqd = (float) (swsbwqd + 0.5f);
                    }
                    if (isEmpty((String) map.get("xwxb"))) {
                        xwxbwqd = (float) (xwxbwqd + 0.5f);
                    }

                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));


                    map.put("gxts", Float.valueOf(gxts));

                    float mqts = myts - wpsxqt - tdts - gxts; //每月天数-星期天个数-停电天数-放假天数
                    if ("2018-02".equals(cxsj)) {
                        mqts = myts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;


                    kqzs = myts - gxts - qjts - tdts - swsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":"+qjts+":"+tdts+":"+":"+swsbwqd+":"+":"+":"+xwxbwqd+"--"+kqzs);

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));
                    map.put("xqtgs", wpsxqt);
                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println("开始4："+df_rqsj.format(new Date()));
        //System.out.println(lt);
        return lt;
    }

    //威浦仕--车间人员考勤--微信端
    public List wpsCjKqxx_wx(HttpServletRequest req, String method) throws Exception {
        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }
        if ((this.con_u8_009 == null) || (this.con_u8_009.isClosed())) {
            this.con_u8_009 = new JdbcUtilsRy().getConnection("wps");
        }
        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String wxid = req.getParameter("wxid");
        String gslb = req.getParameter("gslb");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int ts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);
        int byts = ts;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        if (cxsj_y_int == month) {
            ts = day;
        }


        String whereSql_wxid = "";
        //根据微信ID获取过滤条件
        if (!isEmpty(wxid)) {
            whereSql_wxid += " and p.cPsnURL = '" + wxid + "' ";
            ;
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");

        int wpsxqt = 0;
        //判断每月有几个周日
        for (int k = 1; k <= byts; k++) {
            Date d = df_rq.parse(cxsj + "-" + k);
            if ("日".equals(df_xq.format(d).substring(2, 3))) {
                wpsxqt++;
            }
        }

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_u8ry = "select p.JobNumber as gh,p.ctitle as ryxh,p.cDept_num as dwid,t2.cDepName as dwmc from hr_hi_person p,Department t2 where p.rEmployState = '10' and  t2.cDepCode = p.cDept_num " + whereSql_wxid;
        Statement st_u8ry = this.con_u8_009.createStatement();
        ResultSet rs_u8ry = st_u8ry.executeQuery(sql_u8ry);
        int bs = 0;
        while (rs_u8ry.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_u8ry.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String dwmc = rs_u8ry.getString("dwmc");
            String dwid = rs_u8ry.getString("dwid");
            String ryxh = rs_u8ry.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            boolean zj_flag = false;

            //System.out.println(ts);
            for (int i = 1; i <= ts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,e.employeeName as xm,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;
                //System.out.println(Sql_kqjl);

                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    String ryxm = rs_kqjl.getString("xm");

                    map.put("gh", gh);
                    map.put("bm", dwmc);
                    //区分品管部王利和无纺布机折车间王利
                    if ("600659".equals(gh)) {
                        ryxm = "王利(车间)";
                    }
                    map.put("xm", ryxm);
                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;


                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='71' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    // System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                }
                            }
                        }

                        if ("all".equals(ts_member)) {
                            if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                    gxts += 0.5f;
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                    gxts += 0.5f;
                                }
                            }

                        }

                    }

                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(fdate, df_rq)) { //begin 威浦仕夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:35:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.5D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } else { // end 威浦仕夏季打卡/冬季考勤
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.5D);
                                    }
                                } else if (compareTime(dk[j], "17:05:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.5D);
                                    }
                                }

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }
                            } // end 威浦仕冬季考勤

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }

                    if (isEmpty((String) map.get("swsb"))) {
                        swsbwqd = (float) (swsbwqd + 0.5f);
                    }
                    if (isEmpty((String) map.get("xwxb"))) {
                        xwxbwqd = (float) (xwxbwqd + 0.5f);
                    }

                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));

                    map.put("gxts", Float.valueOf(gxts));

                    float mqts = byts - wpsxqt - tdts - gxts;
                    if ("2018-02".equals(cxsj)) {
                        mqts = byts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;

                    kqzs = ts - gxts - -qjts - tdts - swsbwqd - xwxbwqd;


                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("xqtgs", wpsxqt);
                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println(lt);
        return lt;
    }


    //--------------------------迈迪科考勤----------------------------
    //迈迪科--行政人员考勤--电脑端
    public List mdkKqxx_pc(HttpServletRequest req, String method) throws Exception {
        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }

        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String cxbm = req.getParameter("cxbm");
        String cxxm = req.getParameter("cxxm");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int myts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);

        //根据查询部门获取过滤条件
        String whereSql_oary = "";
        if (cxbm.endsWith("00000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 2) + "%'";
        } else if (cxbm.endsWith("000000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 4) + "%'";
            //whereSql_oary+=" and b.bmid like '"+cxbm.substring(0,2)+"%'";
        } else if (cxbm.endsWith("0000")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 6) + "%'";
        } else if (cxbm.endsWith("00")) {
            whereSql_oary += " and b.bmid like '" + cxbm.substring(0, 8) + "%'";
        } else {
            whereSql_oary += " and b.bmid = '" + cxbm + "'";
        }

        //根据查询姓名获取过滤条件
        if (!isEmpty(cxxm)) {
            whereSql_oary += " and p.id = '" + cxxm + "' ";
            //whereSql_oary += " and p.id = '10537' ";
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");
        //System.out.println("开始1："+df_rqsj.format(new Date()));

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_oary = "select p.stuff_id  as gh,p.per_sort as ryxh,b.bmmc,p.truename as ryxm,p.id as oaryid from person p,t_mdkbm b where p.isaway = '0' and b.oa_bmid = p.dep_id  and p.id!='10554' " + whereSql_oary;
        Statement st_oary = this.con_oa.createStatement();
        ResultSet rs_oary = st_oary.executeQuery(sql_oary);


        int bs = 0;
        while (rs_oary.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_oary.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String bmmc = rs_oary.getString("bmmc");
            String ryxm = rs_oary.getString("ryxm");
            String oaryid = rs_oary.getString("oaryid");
            String ryxh = rs_oary.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            boolean zj_flag = false;
            for (int i = 1; i <= myts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;


                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    map.put("xm", ryxm);
                    map.put("gh", gh);
                    map.put("bm", bmmc);


                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='51' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("xwsb");
                                    String xw2 = (String) map.get("xwxb");
                                    if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }


                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("xwsb");
                                String xw2 = (String) map.get("xwxb");
                                if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                            }


                        }

                    }

                    //总监免签
                    if ("02670".equals(gh) || "800070".equals(gh) || "800193".equals(gh) || "800494".equals(gh) || "800157".equals(gh) || "800533".equals(gh)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if (isSummer(df_rq.format(date), df_rq)) {
                            if (compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (compareTime(currTime, df_rq.format(date) + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        }


                    } else if (compareTime(cxsj + "-01", "2019-06-01", df_rq) < 0L && ("800051".equals(gh) || "800112".equals(gh) || "800107".equals(gh) || "800014".equals(gh) || "800094".equals(gh))) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if (isSummer(df_rq.format(date), df_rq)) {
                            if (compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (compareTime(currTime, df_rq.format(date) + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        }
                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(df_rq.format(date), df_rq)) { //begin 亚都夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:31:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                }/*else if (compareTime(dk[j], "13:00:00", df_sj) >= 0L && compareTime(dk[j], "13:30:00", df_sj) < 0L) {
			   		                  if (isEmpty((String)map.get("xwsb"))) {
			   			                    map.put("xwsb", "正常" + dk[j]);
			   			                    xwsbzcts = (float)(xwsbzcts + 0.25D);
			   			              }
			                }*/ else if (compareTime(dk[j], "13:31:00", df_sj) >= 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "15:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            } else { // end 亚都夏季打卡时间/begin 亚都冬季打卡时间

                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                } else if (compareTime(dk[j], "13:00:00", df_sj) > 0L && compareTime(dk[j], "13:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:15:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:00:00", df_sj) < 0L && compareTime(dk[j], "15:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }


                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            }// end 亚都冬季打卡时间

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));

                    //  String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00163 u where u.field1 = '"+oaryid+"' and u.field2 = '"+df_rq.format(date)+"' and u.flowid in (select per_id from document d where d.state='2' )";
                    // String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00163 u,document d where u.field1 = '"+oaryid+"' and u.field2 = '"+fdate+"' and d.state = '2' and u.flowid=d.id";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00163 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field2 = '" + fdate + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid=d.doc_id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("1".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("2".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    //oa请假
                    //String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00162 u where u.field1 = '"+oaryid+"' and u.field6 <= '"+df_rq.format(date)+"' and u.field7>= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    // String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00162 u,document d where u.field1 = '"+oaryid+"' and u.field6 <= '"+fdate+"' and u.field7>= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj from utm_00162 u,flownode_member d,nodetactics n where u.field1 = '" + oaryid + "' and u.field6 <= '" + fdate + "' and u.field7>= '" + fdate + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //多个全天
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }


                    //oa出差
                    // String sql_oacc = "select u.field3,u.id from utm_00164 u where u.field3 = '"+oaryid+"' and u.field7 >= '"+df_rq.format(date)+"' and u.field6<= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    // String sql_oacc = "select u.field3,u.id from utm_00164 u,document d where u.field3 = '"+oaryid+"' and u.field7 >= '"+fdate+"' and u.field6<= '"+fdate+"' and d.state = '2' and u.flowid = d.id";
                    String sql_oacc = "select u.field3,u.id from utm_00164 u,flownode_member d,nodetactics n where u.field3 = '" + oaryid + "' and u.field7 >= '" + fdate + "' and u.field6<= '" + fdate + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id";
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    // String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00201 u where u.field2 = '"+oaryid+"' and u.field10 like '"+df_rq.format(date)+"%' and u.flowid in (select id from document d where d.state = '2' )";
                    //    String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00201 u,document d where u.field2 = '"+oaryid+"' and u.field10 >= '"+fdate+" 00:00:01' and u.field10 <='"+fdate+" 23:59:59 ' and d.state = '2' and u.flowid = d.id ";
                    String sql_oajb = "select distinct u.field10,u.field10 as jbkssj,u.field12 as jbzgs from utm_00201 u,flownode_member d,nodetactics n where u.field2 = '" + oaryid + "' and u.field10 >= '" + fdate + " 00:00:01' and u.field10 <='" + fdate + " 23:59:59 ' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id and u.flowid = d.doc_id ";
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }

                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = myts - 3 - tdts - gxts;
                    if ("2018-02".equals(cxsj)) {
                        mqts = myts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;


                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = myts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":"+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));

                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println(lt);
        return lt;
    }

    //迈迪科--行政人员考勤--微信端
    public List mdkKqxx_wx(HttpServletRequest req, String method) throws Exception {
        if ((this.con_oa == null) || (this.con_oa.isClosed())) {
            this.con_oa = this.pm.getConnection();
        }

        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }
        System.out.println("迈迪克微信1：");
        if ((this.con_u8_010 == null) || (this.con_u8_010.isClosed())) {
            this.con_u8_010 = new JdbcUtilsRy().getConnection("mdk");
            System.out.println("迈迪克微信2：");
        }

        List lt = new ArrayList();

        //获取查询参数
        String cxsj = req.getParameter("cxsj");
        String wxid = req.getParameter("wxid");
        String gslb = req.getParameter("gslb");

        //获取每月天数
        String cxsj_n = cxsj.substring(0, 4);
        String cxsj_y = cxsj.substring(5, 7);
        int cxsj_n_int = Integer.parseInt(cxsj_n);
        int cxsj_y_int = Integer.parseInt(cxsj_y);
        int ts = getDaysByYearMonth(cxsj_n_int, cxsj_y_int);
        int byts = ts;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        if (cxsj_y_int == month) {
            ts = day;
        }
        String whereSql_wxid = "";
        //根据微信ID获取过滤条件
        if (!isEmpty(wxid)) {
            whereSql_wxid += " and p.cPsnURL = '" + wxid + "' ";
            ;
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat df_xq = new SimpleDateFormat("EEE");

        String whereSql_kqjl = "";
        String qtWhere = "";


        //根据页面查询条件查询OA人员
        String sql_u8ry = "select p.JobNumber as gh,p.ctitle as ryxh,p.cDept_num as dwid,t2.cDepName as dwmc from hr_hi_person p,Department t2 where p.rEmployState = '10' and  t2.cDepCode = p.cDept_num " + whereSql_wxid;
        Statement st_u8ry = this.con_u8_010.createStatement();
        ResultSet rs_u8ry = st_u8ry.executeQuery(sql_u8ry);
        //System.out.println(sql_u8ry);

        int bs = 0;
        while (rs_u8ry.next()) {
            bs++;
            //每人每月的考勤存放一个List
            List mykq = new ArrayList();

            String gh = rs_u8ry.getString("gh");
            int gh_int = Integer.parseInt(gh);
            String dwmc = rs_u8ry.getString("dwmc");
            String dwid = rs_u8ry.getString("dwid");
            String ryxh = rs_u8ry.getString("ryxh");
            whereSql_kqjl = " where e.employeeCode in ('" + gh_int + "','" + ryxh + "')";

            int cdcs = 0;
            int ztcs = 0;
            int bqcs = 0;
            int cdcs_15 = 0;

            float ccts = 0.0F;
            float gxts = 0.0F;
            float qjts = 0.0F;
            float qjts_hj = 0.0F;
            float qjts_sj = 0.0F;
            float qjts_gs = 0.0F;

            float byjb = 0.0F;
            float swsbzcts = 0.0F;
            float swsbcdts = 0.0F;
            float swsbcdts_15 = 0.0F;
            float swxbzcts = 0.0F;
            float swxbztts = 0.0F;
            float xwsbzcts = 0.0F;
            float xwsbcdts = 0.0F;
            float xwsbcdts_15 = 0.0F;
            float xwxbzcts = 0.0F;
            float xwxbztts = 0.0F;
            float mqcs = 0.0F;
            float bqts = 0.0F;
            float tdts = 0.0F;
            float swsbwqd = 0.0F;
            float swxbwqd = 0.0F;
            float xwsbwqd = 0.0F;
            float xwxbwqd = 0.0F;
            boolean zj_flag = false;

            for (int i = 1; i <= ts; i++) {
                Date date = df_rq.parse(cxsj + "-" + i);
                String fdate = df_rq.format(date);

                //查询每个人每天的考勤记录
                String Sql_kqjl = "select e.employeeCode as kqh,e.employeeName as xm,group_concat(right(t.CardTime,8)) as dksj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and  t.CardTime >= '" + fdate + " 00:00:01' and t.cardtime <= '" + fdate + " 23:59:59' " + whereSql_kqjl;


                Statement st_kqjl = this.con_kq.createStatement();
                ResultSet rs_kqjl = st_kqjl.executeQuery(Sql_kqjl);

                while (rs_kqjl.next()) {
                    //每个人每天的考勤记录存放一个Map
                    Map map = new HashMap();
                    map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                    map.put("xq", df_xq.format(date).substring(2, 3));
                    String ryxm = rs_kqjl.getString("xm");
                    map.put("xm", ryxm);
                    map.put("gh", gh);
                    map.put("bm", dwmc);

                    String dksj = rs_kqjl.getString("dksj");
                    boolean wfsswsbwdk = false;

                    String Sql_tskq = "select t.rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tmember from t_team_tskq t,t_team t1 where t.teamid = t1.id  and t1.gslb='51' and t.rq= '" + df_rq.format(date) + "'";
                    Statement st_tskq = this.con_oa.createStatement();
                    ResultSet rs_tskq = st_tskq.executeQuery(Sql_tskq);
                    //System.out.println(Sql_tskq);
                    while (rs_tskq.next()) {
                        String ts_lb = rs_tskq.getString("lb"); //停电放假tdfj/停电补签tdbq/放假fj/喜宴xy
                        String ts_rq = rs_tskq.getString("rq");
                        String ts_member = rs_tskq.getString("tmember");
                        String ts_swsb = rs_tskq.getString("swsb");
                        String ts_swxb = rs_tskq.getString("swxb");
                        String ts_xwsb = rs_tskq.getString("xwsb");
                        String ts_xwxb = rs_tskq.getString("xwxb");
                        if (!"all".equals(ts_member)) { //针对特别人员的组
                            if (!isEmpty(ts_member) && ts_member.contains(ryxm)) {
                                if ("tdbq".equals(ts_lb) || "xy".equals(ts_lb)) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", ts_swsb);
                                    }
                                    if (isEmpty((String) map.get("swxb"))) {
                                        map.put("swxb", ts_swxb);
                                    }

                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", ts_xwsb);
                                    }

                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", ts_xwxb);
                                    }
                                } else if ("fj".equals(ts_lb)) {
                                    String sw1 = (String) map.get("swsb");
                                    String sw2 = (String) map.get("swxb");
                                    String xw1 = (String) map.get("xwsb");
                                    String xw2 = (String) map.get("xwxb");
                                    if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                        qjts = (float) (qjts - 0.5D);
                                    }

                                    if (!isEmpty(ts_swsb) && ts_member.contains(ryxm)) { //本来放假了，上午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_swxb) && ts_member.contains(ryxm)) { //本来放假了，上午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwsb) && ts_member.contains(ryxm)) { //本来放假了，下午上班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }
                                    if (!isEmpty(ts_xwxb) && ts_member.contains(ryxm)) { //本来放假了，下午下班个别人来了，也没有打卡，应出勤天数加上0.25天
                                        qjts = (float) (qjts - 0.25D);
                                    }

                                    if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                        map.put("swsb", ts_swsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                        map.put("swxb", ts_swxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                        map.put("xwsb", ts_xwsb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                    if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                        map.put("xwxb", ts_xwxb);
                                        gxts = (float) (gxts + 0.25D);
                                    }
                                }
                            }
                        }


                        if ("all".equals(ts_member)) { //针对全公司
                            if ("tdfj".equals(ts_lb)) {
                                if (!isEmpty(ts_swsb)) {
                                    map.put("swsb", ts_swsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_swxb)) {
                                    map.put("swxb", ts_swxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwsb)) {
                                    map.put("xwsb", ts_xwsb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                                if (!isEmpty(ts_xwxb)) {
                                    map.put("xwxb", ts_xwxb);
                                    tdts = (float) (tdts + 0.25D);
                                }
                            } else if ("tdbq".equals(ts_lb)) {
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", ts_swsb);
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", ts_swxb);
                                }

                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", ts_xwsb);
                                }

                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", ts_xwxb);
                                }
                            } else if ("fj".equals(ts_lb)) {
                                String sw1 = (String) map.get("swsb");
                                String sw2 = (String) map.get("swxb");
                                String xw1 = (String) map.get("xwsb");
                                String xw2 = (String) map.get("xwxb");
                                if ((!isEmpty(sw1) && sw1.contains("假")) || (!isEmpty(sw2) && sw2.contains("假"))) { //本来放假了，如果上午上班或上午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if ((!isEmpty(xw1) && sw1.contains("假")) || (!isEmpty(xw2) && xw2.contains("假"))) { //本来放假了，如果下午上班或下午下班请假了，应出勤加上半天
                                    qjts = (float) (qjts - 0.5D);
                                }

                                if (isEmpty(sw1) && !isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) { //本来放假了，上午上班也没有出差，应出勤天数减去0.25天
                                    map.put("swsb", ts_swsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(sw2) && !isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) { //本来放假了，上午下班也没有出差，应出勤天数减去0.25天
                                    map.put("swxb", ts_swxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw1) && !isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) { //本来放假了，下午上班也没有出差，应出勤天数减去0.25天
                                    map.put("xwsb", ts_xwsb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                                if (isEmpty(xw2) && !isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) { //本来放假了，下午下班也没有出差，应出勤天数减去0.25天
                                    map.put("xwxb", ts_xwxb);
                                    gxts = (float) (gxts + 0.25D);
                                }
                            }


                        }

                    }

                    //总监免签
                    if ("02670".equals(gh) || "800070".equals(gh) || "800193".equals(gh) || "800494".equals(gh) || "800157".equals(gh) || "800533".equals(gh)) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if (isSummer(df_rq.format(date), df_rq)) {
                            if (compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (compareTime(currTime, df_rq.format(date) + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        }


                    } else if (compareTime(cxsj + "-01", "2019-06-01", df_rq) < 0L && ("800051".equals(gh) || "800112".equals(gh) || "800107".equals(gh) || "800014".equals(gh) || "800094".equals(gh))) {
                        zj_flag = true;
                        String currTime = df_rqsj.format(new Date());
                        if (compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L && isEmpty((String) map.get("swxb"))) {
                            map.put("swxb", "免签");
                            mqcs = (float) (mqcs + 0.25D);
                        }
                        if (isSummer(df_rq.format(date), df_rq)) {
                            if (compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        } else {
                            if (compareTime(currTime, df_rq.format(date) + " 13:30:00", df_rqsj) >= 0L && isEmpty((String) map.get("xwsb"))) {
                                map.put("xwsb", "免签");
                                mqcs = (float) (mqcs + 0.25D);
                            }
                        }
                    }


                    if (!isEmpty(dksj)) {
                        String[] dk = dksj.split(",");

                        for (int j = 0; j < dk.length; j++) { // begin 循环每天的打卡记录
                            if (isSummer(df_rq.format(date), df_rq)) { //begin 亚都夏季打卡时间
                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:31:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                } else if (compareTime(dk[j], "13:31:00", df_sj) >= 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "15:30:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }
                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            } else { // end 亚都夏季打卡时间/begin 亚都冬季打卡时间

                                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "正常" + dk[j]);
                                        swsbzcts = (float) (swsbzcts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs++;
                                        swsbcdts = (float) (swsbcdts + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swsb"))) {
                                        map.put("swsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        swsbcdts_15 = (float) (swsbcdts_15 + 0.25D);
                                        wfsswsbwdk = true;
                                    }
                                } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("swxb")) && wfsswsbwdk) {
                                        map.put("swxb", "正常" + dk[j]);
                                        swxbzcts = (float) (swxbzcts + 0.25D);
                                        wfsswsbwdk = false;
                                    } else {
                                        if (isEmpty((String) map.get("xwsb"))) {
                                            map.put("xwsb", "正常" + dk[j]);
                                            xwsbzcts = (float) (xwsbzcts + 0.25D);
                                        }
                                    }
                                } else if (compareTime(dk[j], "13:00:00", df_sj) > 0L && compareTime(dk[j], "13:15:00", df_sj) <= 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs++;
                                        xwsbcdts = (float) (xwsbcdts + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "13:15:00", df_sj) > 0L && compareTime(dk[j], "14:30:00", df_sj) < 0L) {
                                    if (isEmpty((String) map.get("xwsb"))) {
                                        map.put("xwsb", "迟到" + dk[j]);
                                        cdcs_15++;
                                        xwsbcdts_15 = (float) (xwsbcdts_15 + 0.25D);
                                    }

                                } else if (compareTime(dk[j], "17:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "正常" + dk[j]);
                                        xwxbzcts = (float) (xwxbzcts + 0.25D);
                                    }
                                } else if (compareTime(dk[j], "17:00:00", df_sj) < 0L && compareTime(dk[j], "15:00:00", df_sj) >= 0L) {
                                    if (isEmpty((String) map.get("xwxb"))) {
                                        map.put("xwxb", "早退" + dk[j]);
                                        ztcs++;
                                        xwxbztts = (float) (xwxbztts + 0.25D);
                                    }
                                }
			                /*else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
			                    if (isEmpty((String)map.get("swxb"))) {
			                          map.put("swxb", "正常" + dk[j]);
			                          swxbzcts = (float)(swxbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
			                	if (isEmpty((String)map.get("swxb"))) {
				                      map.put("swxb", "早退" + dk[j]);
				                      ztcs++;
				                      swxbztts = (float)(swxbztts + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
			                    if (isEmpty((String)map.get("xwsb"))) {
			                          map.put("xwsb", "正常" + dk[j]);
			                          xwsbzcts = (float)(xwsbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L){
			                	if (isEmpty((String)map.get("xwsb"))) {
				                     map.put("xwsb", "迟到" + dk[j]);
				                     cdcs++;
				                     xwsbcdts = (float)(xwsbcdts + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L){
			                	if (isEmpty((String)map.get("xwsb"))) {
				                     map.put("xwsb", "迟到" + dk[j]);
				                     cdcs_15++;
				                     xwsbcdts_15 = (float)(xwsbcdts_15 + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
			                    if (isEmpty((String)map.get("xwxb"))) {
			                          map.put("xwxb", "正常" + dk[j]);
			                          xwxbzcts = (float)(xwxbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
			                    if(isEmpty((String)map.get("xwxb"))){
					                  map.put("xwxb", "早退" + dk[j]);
					                  ztcs++;
					                  xwxbztts = (float)(xwxbztts + 0.25D);
			                    }
			                }*/

                                if (isEmpty((String) map.get("swsb"))) {
                                    map.put("swsb", "无");
                                }
                                if (isEmpty((String) map.get("swxb"))) {
                                    map.put("swxb", "无");
                                }
                                if (isEmpty((String) map.get("xwsb"))) {
                                    map.put("xwsb", "无");
                                }
                                if (isEmpty((String) map.get("xwxb"))) {
                                    map.put("xwxb", "无");
                                }

                            }// end 亚都冬季打卡时间

                        } // end 循环每天的打卡记录

                    } else {   // end 判断打卡时间是否为空
                        if ((isEmpty((String) map.get("swsb")))) {
                            map.put("swsb", "无");
                        }
                        if ((isEmpty((String) map.get("swxb")))) {
                            map.put("swxb", "无");
                        }
                        if ((isEmpty((String) map.get("xwsb")))) {
                            map.put("xwsb", "无");
                        }
                        if ((isEmpty((String) map.get("xwxb")))) {
                            map.put("xwxb", "无");
                        }
                    }

		            	  /* if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
				            if (isEmpty((String)map.get("swsb"))) {
				                  map.put("swsb", "正常" + dk[j]);
				                  swsbzcts = (float)(swsbzcts + 0.25D);
		                    }
		                }else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L){
			                if (isEmpty((String)map.get("swsb"))) {
				                  map.put("swsb", "迟到" + dk[j]);
				                  cdcs++;
				                  swsbcdts = (float)(swsbcdts + 0.25D);
			                }
		                }else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L){
		                	if (isEmpty((String)map.get("swsb"))) {
				                  map.put("swsb", "迟到" + dk[j]);
				                  cdcs_15++;
				                  swsbcdts_15 = (float)(swsbcdts_15 + 0.25D);
		                	}
		                }else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
		                    if (isEmpty((String)map.get("swxb"))) {
		                          map.put("swxb", "正常" + dk[j]);
		                          swxbzcts = (float)(swxbzcts + 0.25D);
		                    }
		                }else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
		                	if (isEmpty((String)map.get("swxb"))) {
			                      map.put("swxb", "早退" + dk[j]);
			                      ztcs++;
			                      swxbztts = (float)(swxbztts + 0.25D);
		                	}

		                }else if (compareTime(dk[j], "14:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
		                    if (isEmpty((String)map.get("xwsb"))) {
		                          map.put("xwsb", "正常" + dk[j]);
		                          xwsbzcts = (float)(xwsbzcts + 0.25D);
		                    }
		                }else if (compareTime(dk[j], "14:31:00", df_sj) > 0L && compareTime(dk[j], "14:45:00", df_sj) <= 0L){
		                	if (isEmpty((String)map.get("xwsb"))) {
			                     map.put("xwsb", "迟到" + dk[j]);
			                     cdcs++;
			                     xwsbcdts = (float)(xwsbcdts + 0.25D);
		                	}

		                }else if (compareTime(dk[j], "14:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L){
		                	if (isEmpty((String)map.get("xwsb"))) {
			                     map.put("xwsb", "迟到" + dk[j]);
			                     cdcs_15++;
			                     xwsbcdts_15 = (float)(xwsbcdts_15 + 0.25D);
		                	}

		                }else if (compareTime(dk[j], "18:30:00", df_sj) >= 0L) {
		                    if (isEmpty((String)map.get("xwxb"))) {
		                          map.put("xwxb", "正常" + dk[j]);
		                          xwxbzcts = (float)(xwxbzcts + 0.25D);
		                    }
		                }else if (compareTime(dk[j], "18:30:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
		                    if(isEmpty((String)map.get("xwxb"))){
				                  map.put("xwxb", "早退" + dk[j]);
				                  ztcs++;
				                  xwxbztts = (float)(xwxbztts + 0.25D);
		                    }
		                }

		                if (isEmpty((String)map.get("swsb"))) {
		                  map.put("swsb", "无");
		                }
		                if (isEmpty((String)map.get("swxb"))) {
		                  map.put("swxb", "无");
		                }
		                if (isEmpty((String)map.get("xwsb"))) {
		                  map.put("xwsb", "无");
		                }
		                if (isEmpty((String)map.get("xwxb"))){
		                  map.put("xwxb", "无");
		                }

		              }else{ // end 亚都夏季打卡时间/begin 亚都冬季打卡时间

			                if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
					            if (isEmpty((String)map.get("swsb"))) {
					                  map.put("swsb", "正常" + dk[j]);
					                  swsbzcts = (float)(swsbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L){
				                if (isEmpty((String)map.get("swsb"))) {
					                  map.put("swsb", "迟到" + dk[j]);
					                  cdcs++;
					                  swsbcdts = (float)(swsbcdts + 0.25D);
				                }
			                }else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L){
			                	if (isEmpty((String)map.get("swsb"))) {
					                  map.put("swsb", "迟到" + dk[j]);
					                  cdcs_15++;
					                  swsbcdts_15 = (float)(swsbcdts_15 + 0.25D);
			                	}
			                }else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) {
			                    if (isEmpty((String)map.get("swxb"))) {
			                          map.put("swxb", "正常" + dk[j]);
			                          swxbzcts = (float)(swxbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
			                	if (isEmpty((String)map.get("swxb"))) {
				                      map.put("swxb", "早退" + dk[j]);
				                      ztcs++;
				                      swxbztts = (float)(swxbztts + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "13:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
			                    if (isEmpty((String)map.get("xwsb"))) {
			                          map.put("xwsb", "正常" + dk[j]);
			                          xwsbzcts = (float)(xwsbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "13:31:00", df_sj) > 0L && compareTime(dk[j], "13:45:00", df_sj) <= 0L){
			                	if (isEmpty((String)map.get("xwsb"))) {
				                     map.put("xwsb", "迟到" + dk[j]);
				                     cdcs++;
				                     xwsbcdts = (float)(xwsbcdts + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "13:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L){
			                	if (isEmpty((String)map.get("xwsb"))) {
				                     map.put("xwsb", "迟到" + dk[j]);
				                     cdcs_15++;
				                     xwsbcdts_15 = (float)(xwsbcdts_15 + 0.25D);
			                	}

			                }else if (compareTime(dk[j], "17:30:00", df_sj) >= 0L) {
			                    if (isEmpty((String)map.get("xwxb"))) {
			                          map.put("xwxb", "正常" + dk[j]);
			                          xwxbzcts = (float)(xwxbzcts + 0.25D);
			                    }
			                }else if (compareTime(dk[j], "17:30:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
			                    if(isEmpty((String)map.get("xwxb"))){
					                  map.put("xwxb", "早退" + dk[j]);
					                  ztcs++;
					                  xwxbztts = (float)(xwxbztts + 0.25D);
			                    }
			                }

			                if (isEmpty((String)map.get("swsb"))) {
			                  map.put("swsb", "无");
			                }
			                if (isEmpty((String)map.get("swxb"))) {
			                  map.put("swxb", "无");
			                }
			                if (isEmpty((String)map.get("xwsb"))) {
			                  map.put("xwsb", "无");
			                }
			                if (isEmpty((String)map.get("xwxb"))){
			                  map.put("xwxb", "无");
			                }

		               }// end 亚都冬季打卡时间

		            } // end 循环每天的打卡记录

		          }else{   // end 判断打卡时间是否为空
		        	  if((isEmpty((String)map.get("swsb")))){
		        		  map.put("swsb", "无");
		        	  }
		        	  if((isEmpty((String)map.get("swxb")))){
		        		  map.put("swxb", "无");
		        	  }
		        	  if((isEmpty((String)map.get("xwsb")))){
		        		  map.put("xwsb", "无");
		        	  }
		        	  if((isEmpty((String)map.get("xwxb")))){
		        		  map.put("xwxb", "无");
		        	  }
		          }*/


                    map.put("cdcs", Integer.valueOf(cdcs));
                    map.put("cdcs_15", Integer.valueOf(cdcs_15));
                    map.put("ztcs", Integer.valueOf(ztcs));

                    //String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00163 u,person p where u.field1 = p.id and p.stuff_id = '"+gh+"' and u.field2 = '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00163 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id = '" + gh + "' and u.field2 = '" + df_rq.format(date) + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oabq = this.con_oa.createStatement();
                    ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                    while (rs_oabq.next()) {
                        String bkcx = rs_oabq.getString("bqcx");
                        String bqlb = rs_oabq.getString("bqlb");
                        if ("0".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("1".equals(bkcx)) {
                            if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("swxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("swxb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("2".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwsb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwsb", "因私补签");
                                    bqcs++;
                                }
                            }
                        } else if ("3".equals(bkcx)) {
                            if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                                bqts = (float) (bqts + 0.25D);
                                if ("0".equals(bqlb)) {
                                    map.put("xwxb", "因公补签");
                                } else if ("1".equals(bqlb)) {
                                    map.put("xwxb", "因私补签");
                                    bqcs++;
                                }

                            }

                        }

                    }

                    map.put("bqcs", Integer.valueOf(bqcs));

                    //oa请假
                    //String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00162 u,person p where u.field1 = p.id and p.stuff_id ='"+gh+"' and u.field6 <= '"+df_rq.format(date)+"' and u.field7>= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2' )";
                    //  String sql_oaqj = "select u.field4 as qjlb,u.field17 as kssj,u.field18 as jssj from utm_00162 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id ='"+gh+"' and u.field6 <= '"+df_rq.format(date)+"' and u.field7>= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2' )";
                    String sql_oaqj = "select u.field4 as qjlb,u.field6 as ksrq,u.field7 as jsrq,u.field17 as kssj,u.field18 as jssj  from utm_00162 u,person p,flownode_member d,nodetactics n where u.field1 = p.id and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field6 <= '" + df_rq.format(date) + "' and u.field7>= '" + df_rq.format(date) + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oaqj = this.con_oa.createStatement();
                    ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                    while (rs_oaqj.next()) {
                        String qjlb = rs_oaqj.getString("qjlb");
                        String ksrq = rs_oaqj.getString("ksrq");
                        String jsrq = rs_oaqj.getString("jsrq");
                        String kssj = rs_oaqj.getString("kssj");
                        String jssj = rs_oaqj.getString("jssj");
                        boolean swbt = false; //上午请半天假
                        boolean xwbt = false; //下午请半天假
                        boolean sxw = false; //上下午请一天假

                        boolean qtsw = false; //全天+上午
                        boolean xwqt = false; //下午+全天
                        boolean dgqt = false; //多个全天
                        boolean xwsw = false;
                        if (ksrq.equals(jsrq)) {//开始日期等于结束日期
                            if ("0".equals(kssj) && "0".equals(jssj)) { //上午请半天假
                                swbt = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午请半天假
                                xwbt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //上下午请一天假
                                sxw = true;
                            }
                        } else {
                            if ("0".equals(kssj) && "0".equals(jssj)) { //全天+上午请假
                                qtsw = true;
                            } else if ("1".equals(kssj) && "1".equals(jssj)) { //下午+全天
                                xwqt = true;
                            } else if ("0".equals(kssj) && "1".equals(jssj)) { //多个全天
                                dgqt = true;
                            } else if ("1".equals(kssj) && "0".equals(jssj)) { //多个全天
                                xwsw = true;
                            }
                        }


                        if (isEmpty((String) map.get("swsb")) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swsb", lbmc);
                        }

                        if ((isEmpty((String) map.get("swxb")) || "免签".equals((String) map.get("swxb"))) && (swbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("swxb", lbmc);
                        }
                        if ((isEmpty((String) map.get("xwsb")) || "免签".equals((String) map.get("xwsb"))) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwsb", lbmc);
                        }

                        if (isEmpty((String) map.get("xwxb")) && (xwbt || sxw || qtsw || xwqt || dgqt || xwsw)) {
                            String lbmc = "";
                            if ("2".equals(qjlb)) {
                                lbmc = "婚假";
                                qjts_hj += 0.25f;
                            } else if ("3".equals(qjlb)) {
                                lbmc = "丧假";
                                qjts_sj += 0.25f;
                            } else if ("5".equals(qjlb)) {
                                lbmc = "工伤假";
                                qjts_gs += 0.25f;
                            } else {
                                lbmc = "事假";
                                qjts += 0.25f;
                            }
                            map.put("xwxb", lbmc);
                        }
                    }


                    //oa出差
                    //String sql_oacc = "select u.field3,u.id from utm_00164 u,person p where u.field3 = p.id and p.stuff_id ='"+gh+"' and u.field7 >= '"+df_rq.format(date)+"' and u.field6<= '"+df_rq.format(date)+"' and u.flowid in (select id from document d where d.state='2')";
                    String sql_oacc = "select u.field3,u.id from utm_00164 u,person p,flownode_member d,nodetactics n where u.field3 = p.id and u.flowid = d.doc_id and p.stuff_id ='" + gh + "' and u.field7 >= '" + df_rq.format(date) + "' and u.field6<= '" + df_rq.format(date) + "' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oacc = this.con_oa.createStatement();
                    ResultSet rs_oacc = st_oacc.executeQuery(sql_oacc);
                    while (rs_oacc.next()) {
                        if (isEmpty((String) map.get("swsb"))) {
                            ccts += 0.25f;
                            map.put("swsb", "出差");
                        }

                        if (isEmpty((String) map.get("swxb"))) {
                            ccts += 0.25f;
                            map.put("swxb", "出差");
                        }

                        if (isEmpty((String) map.get("xwsb"))) {
                            ccts += 0.25f;
                            map.put("xwsb", "出差");
                        }

                        if (isEmpty((String) map.get("xwxb"))) {
                            ccts += 0.25f;
                            map.put("xwxb", "出差");
                        }
                    }

                    //oa加班
                    //String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00201 u,person p where u.field2 = p.id and p.stuff_id = '"+gh+"' and u.field10 like '"+df_rq.format(date)+"%' and u.flowid in (select id from document d where d.state = '2')";
                    String sql_oajb = "select distinct u.field10, u.field10 as jbkssj,u.field12 as jbzgs from utm_00201 u,person p,flownode_member d,nodetactics n where u.field2 = p.id and u.flowid = d.doc_id and p.stuff_id = '" + gh + "' and u.field10 like '" + df_rq.format(date) + "%' and d.entity in ('10703','10966','11060','11019','11072') and d.workflag = '1' and  n.tac_id = '3' and n.attach_info in ('已阅','同意') and n.mem_id = d.id";
                    Statement st_oajb = this.con_oa.createStatement();
                    ResultSet rs_oajb = st_oajb.executeQuery(sql_oajb);

                    float jbzgs = 0.0f;
                    int cfjbcs = 0;
                    List jbList = new ArrayList();
                    while (rs_oajb.next()) {
                        jbList.add(rs_oajb.getString("jbkssj"));
                        String jbgs = rs_oajb.getString("jbzgs");
                        if (!isEmpty(jbgs)) {
                            jbzgs += Float.parseFloat(jbgs);
                        }

                    }

                    for (int m = 1; m < jbList.size(); m++) {
                        String f_jbkssj = (String) jbList.get(0);
                        String s_jbkssj = (String) jbList.get(m);
                        if ((compareTime(f_jbkssj, s_jbkssj, df_rqsj) <= 10L) && (compareTime(f_jbkssj, s_jbkssj, df_rqsj) >= -10L)) {
                            cfjbcs++;
                        }
                    }

                    String cfjbms = "";
                    if (cfjbcs != 0) {
                        cfjbms = ryxm + "在" + df_rq.format(date) + "重复申请加班" + cfjbcs + "次";
                    }

                    byjb += jbzgs;
                    map.put("jbzgs", Float.valueOf(jbzgs));
                    map.put("byjb", Float.valueOf(byjb));
                    map.put("cfjbcs", Integer.valueOf(cfjbcs));
                    map.put("cfjbms", cfjbms);


                    if (!zj_flag) { //非总监人员
                        if (isEmpty((String) map.get("swsb")) || isEmpty((String) map.get("swxb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5f);
                        }
                        if (isEmpty((String) map.get("xwsb")) || isEmpty((String) map.get("xwxb"))) {
                            xwsbwqd = (float) (xwsbwqd + 0.5f);
                        }
                    } else {
                        if (isEmpty((String) map.get("swsb"))) {
                            swsbwqd = (float) (swsbwqd + 0.5D);
                        }
                        if (isEmpty((String) map.get("xwxb"))) {
                            xwxbwqd = (float) (xwxbwqd + 0.5D);
                        }
                    }


                    map.put("gxts", Float.valueOf(gxts));


                    float mqts = byts - 3 - tdts - gxts;
                    if ("2018-02".equals(cxsj)) {
                        mqts = byts - 1 - tdts - gxts;
                    }

                    float kqzs = 0.0F;


                    if (((qjts + "").endsWith(".25")) || ((qjts + "").endsWith(".75"))) {
                        qjts = (float) (qjts + 0.25D);
                        kqzs = (float) (kqzs - 0.25D);
                    }
                    kqzs = ts - gxts - qjts - tdts - qjts_gs - qjts_hj - qjts_sj - swsbwqd - swxbwqd - xwsbwqd - xwxbwqd;
                    //System.out.println(df_rq.format(date)+":"+myts+":"+gxts+":"+qjts+":"+":"+tdts+":"+qjts_gs+":"+qjts_hj+":"+qjts_sj+":"+swsbwqd+":"+swxbwqd+":"+xwsbwqd+":"+xwxbwqd+"--"+kqzs);

                    if ((kqzs + "").endsWith(".25")) {
                        kqzs = (float) Math.floor(kqzs);
                    } else if ((kqzs + "").endsWith(".75")) {
                        kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                    }

                    if (kqzs < 0.0F) {
                        kqzs = 0.0F;
                    }
                    map.put("ccts", Float.valueOf(ccts));
                    map.put("qjts", Float.valueOf(qjts));
                    map.put("qjts_hj", Float.valueOf(qjts_hj));
                    map.put("qjts_sj", Float.valueOf(qjts_sj));
                    map.put("qjts_gs", Float.valueOf(qjts_gs));

                    map.put("ykqts", Float.valueOf(kqzs));
                    map.put("mqts", Float.valueOf(mqts));
                    mykq.add(map);
                }
            }

            lt.add(mykq);
        }
        //System.out.println(lt);
        return lt;
    }

    //--------------------------工具方法----------------------------
    //根据年月获取每月天数
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(1, year);
        a.set(2, month - 1);
        a.set(5, 1);
        a.roll(5, -1);
        int maxDate = a.get(5);
        return maxDate;
    }

    //判读是否为空
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "无".equals(str)) {
            return true;
        }
        return false;
    }

    //判断是否是夏半年
    private boolean isSummer(String dqsj, DateFormat df) throws ParseException {
        Date dt0 = df.parse(dqsj);
        Date dt1 = df.parse("2019-05-01");
        Date dt2 = df.parse("2019-10-01");
        long diff1 = dt0.getTime() - dt1.getTime();
        long diff2 = dt0.getTime() - dt2.getTime();

        Date dt3 = df.parse("2017-05-01");
        Date dt4 = df.parse("2017-10-01");
        long diff3 = dt0.getTime() - dt3.getTime();
        long diff4 = dt0.getTime() - dt4.getTime();
        if ((diff1 >= 0L && diff2 < 0L) || (diff3 >= 0L && diff4 < 0L)) {
            return true;
        }
        return false;
    }

    //日期时间比较大小
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

    //登陆后获取初始参数
    public List loginUser(HttpServletRequest req) throws Exception {
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }
        String userName = new String(req.getParameter("gh").getBytes("ISO-8859-1"), "gb2312");
        String password = req.getParameter("pwd");
        List params = new ArrayList();
        params.add(userName);
        params.add(password);
        String sql = "select p.id as oaryid,p.truename as oaryxm,p.stuff_id as oarygh,d.name as bmmc,d.id as bmid,d.dep_num as bmdm,p.IDENTITYNO as sfzh,p.oty_id as zwjb from person p,department d where P.dep_id = d.id and p.username=? and p.pinnumber=?";
        //System.out.println(sql);
        try {
            List<Map<String, Object>> list = findModeResult1(sql, params, "login", con_oa);
            //System.out.println(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //查询OA表单
    public Map<String, Object> findModeResult(String sql, List<Object> params, String method, Connection con) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        int count = 0;
        String sqlCount = sql.substring(0, sql.length() - 13);
        Map m = new HashMap();
        PreparedStatement pstmt = con.prepareStatement(sqlCount);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                if ("sy_sbxx".equals(method)) {
                    pstmt.setObject(index++, "%" + params.get(i) + "%");
                } else {
                    pstmt.setObject(index++, params.get(i));
                }

            }
        }
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            count++;
        }
        list = findModeResult1(sql, params, method, con);
        m.put("jbxx", list);
        m.put("count", count);
        if (con != null && !con.isClosed()) {
            resultSet.close();
            pstmt.close();
        }

        return m;
    }

    //查询发货及时率
    public Map<String, Object> findModeResult2(String sql, String sqlCount, List<Object> params, String method, Connection con) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        int count = 0;
        Map m = new HashMap();
        PreparedStatement pstmt = con.prepareStatement(sqlCount);

        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                if ("sy_sbxx".equals(method)) {
                    pstmt.setObject(index++, "%" + params.get(i) + "%");
                } else {
                    pstmt.setObject(index++, params.get(i));
                }

            }
        }
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            count++;
        }
        list = findModeResult1(sql, params, method, con);
        m.put("jbxx", list);
        m.put("count", count);
        if (con != null && !con.isClosed()) {
            resultSet.close();
            pstmt.close();
        }

        return m;
    }

    //查询OA表单1
    public List<Map<String, Object>> findModeResult1(String sql, List<Object> params, String method, Connection con) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        PreparedStatement pstmt = con.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                if ("sy_sbxx".equals(method)) {
                    pstmt.setObject(index++, "%" + params.get(i) + "%");
                } else {
                    pstmt.setObject(index++, params.get(i));
                }
            }
        }
        ResultSet resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }
        if (con != null && !con.isClosed()) {
            resultSet.close();
            pstmt.close();
        }
        return list;

    }

    //oa数据库增删改工具方法
    public boolean updateByPreparedStatement(String sql, List<Object> params) throws SQLException {
        boolean flag = false;
        int result = -1;
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }
        PreparedStatement pstmt = con_oa.prepareStatement(sql);
        int index = 1;
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                //System.out.println("^^^^^^^^"+index++);//输出1，2，3
                pstmt.setObject(index++, params.get(i));
            }
        }
        result = pstmt.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;
    }

    //获取非部门字典下拉列表
    public List<Map<String, Object>> queryCode(Map<String, Object> map) throws SQLException {
        //System.out.println("2************************");
        // TODO Auto-generated method stub
        //jdbcUtils.getConnection();
        List params = new ArrayList();
        String zdmc = map.get("zdmc").toString();
        String zdlb = map.get("zdlb").toString();

        Connection cn = null;

        String sql = "";
        if ("父角色名称".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.id,t.name as text from t_role t where t.pid = ?";
            if ((cn == null) || (cn.isClosed())) {
                cn = this.pm.getConnection();
            }
        } else if ("ry_yd".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.id as id,t.truename as text from person t,t_ydbm t1 where t.dep_id = t1.oa_bmid and t.isaway='0' and t1.bmid = ?";
            if ((cn == null) || (cn.isClosed())) {
                cn = this.pm.getConnection();
            }
        } else if ("ry_mdk".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.id as id,t.truename as text from person t,t_mdkbm t1 where t.dep_id = t1.oa_bmid and t.isaway='0' and t1.bmid = ?";
            if ((cn == null) || (cn.isClosed())) {
                cn = this.pm.getConnection();
            }
        } else if ("ry_wps".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.id as id,t.truename as text from person t,t_wpsbm t1 where t.dep_id = t1.oa_bmid and t.isaway='0' and t1.bmid = ?";
            if ((cn == null) || (cn.isClosed())) {
                cn = this.pm.getConnection();
            }
        } else if ("ry_wpscj".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.cpsn_num as id,t.cpsn_name as text from hr_hi_person t,t_wpscjbm t1 where t.cdept_num = t1.u8bmid and t.rEmployState='10' and t1.bmid = ?";
            if ((cn == null) || (cn.isClosed())) {
                cn = new JdbcUtilsRy().getConnection("wps");
            }
        } else {
            params.add(zdlb);
            params.add(zdmc);
            sql = "select t.value as id,t.name as text from udt_enumvalue t where enumid in (select id from udt_enumlist u where u.enum_type=? and u.name = ?)";
            if ((cn == null) || (cn.isClosed())) {
                cn = this.pm.getConnection();
            }
        }


        //System.out.println(sql);
        try {
            List<Map<String, Object>> list = findModeResult1(sql, params, "zd", cn);
            cn.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取行政部门字典下拉列表
    public Node getDept(HttpServletRequest req) throws Exception {
        List menuList = new ArrayList();
        try {
            if (con_oa == null || con_oa.isClosed()) {
                con_oa = this.pm.getConnection();
            }
            String deptid = req.getParameter("deptid");
            String tname = "";
            if (deptid.startsWith("41")) {
                tname = "t_ydbm";
            } else if (deptid.startsWith("51")) {
                tname = "t_mdkbm";
            } else {
                tname = "t_wpsbm";
            }

            String whereSql = "";
            //根据查询部门获取过滤条件
            if (deptid.startsWith("4110") || deptid.startsWith("4102") || deptid.startsWith("411201") || deptid.endsWith("00000000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 2) + "%'";
            } else if (deptid.endsWith("000000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 4) + "%'";
            } else if (deptid.endsWith("0000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 6) + "%'";
            } else if (deptid.endsWith("00")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 8) + "%'";
            } else {
                whereSql += " and t.bmid = '" + deptid + "'";
            }


            Statement st = con_oa.createStatement();
            String sql = "select t.bmid,t.pbmid,t.bmmc from " + tname + " t where t.off='0' " + whereSql + " order by bmid asc";
            //System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            // 节点列表（散列表，用于临时存储节点对象）
            HashMap nodeList = new HashMap();
            while (rs.next()) {
                Node node = new Node();
                node.id = rs.getString("bmid");
                node.text = rs.getString("bmmc");
                node.parentId = rs.getString("pbmid");
                nodeList.put(node.id, node);
            }
            // 根节点
            Node root = null;
            // 构造无序的多叉树
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                Node node = (Node) ((Map.Entry) it.next()).getValue();

	/*				if (node.parentId==null ||"".equals(node.parentId)) {
						root = node;
					} else {
						((Node) nodeList.get(node.parentId)).addChild(node);

					}*/

                String dw = deptid.substring(0, 4);
                if (deptid.startsWith("4110") || deptid.startsWith("4102") || deptid.startsWith("411201") || deptid.endsWith("00000000")) {
                    if (node.id.equals("4100000000") || node.id.equals("5100000000") || node.id.equals("6100000000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else if ("4102".equals(dw) || "4103".equals(dw) || "4104".equals(dw) || "4105".equals(dw) || "4106".equals(dw) || "4107".equals(dw) || "4108".equals(dw) || "4109".equals(dw) || "4111".equals(dw)
                        || "4112".equals(dw) || "4113".equals(dw) || "4114".equals(dw) || "4115".equals(dw) || "6108".equals(dw) || "6109".equals(dw)) {
                    if (deptid.endsWith("000000")) {
                        if (node.id.endsWith("000000")) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    } else if (deptid.endsWith("0000")) {
                        if (node.id.endsWith("0000")) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    } else {
                        if (node.id.endsWith("00")) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    }
                } else if ("4112020000".equals(deptid)) {
                    if (node.id.endsWith("0000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else {
                    if (node.id.endsWith("00")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                }


            }
            // 输出无序的树形菜单的JSON字符串
            //System.out.println(root);
            // 对多叉树进行横向排序
            root.sortChildren();
            // 输出有序的树形菜单的JSON字符串
            //System.out.println(root);
            if (con_kq != null && !con_kq.isClosed()) {
                rs.close();
                st.close();
            }

            return root;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    //获取车间部门字典下拉列表
    public Node getCjDept(HttpServletRequest req) throws Exception {
        List menuList = new ArrayList();
        try {
            if (con_u8_009 == null || con_u8_009.isClosed()) {
                con_u8_009 = new JdbcUtilsRy().getConnection("wps");
            }
            String deptid = req.getParameter("deptid");
            String tname = "";
            if (deptid.startsWith("41")) {
                tname = "t_ydcjbm";
            } else if (deptid.startsWith("51")) {
                tname = "t_mdkcjbm";
            } else {
                tname = "t_wpscjbm";
            }

            String whereSql = "";
            //根据查询部门获取过滤条件
            if (deptid.endsWith("00000000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 2) + "%'";
            } else if (deptid.endsWith("000000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 4) + "%'";
            } else if (deptid.endsWith("0000")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 6) + "%'";
            } else if (deptid.endsWith("00")) {
                whereSql += " and t.bmid like '" + deptid.substring(0, 8) + "%'";
            } else {
                whereSql += " and t.bmid = '" + deptid + "'";
            }


            Statement st = con_u8_009.createStatement();
            String sql = "select t.bmid,t.pbmid,t.bmmc from " + tname + " t where t.zfbs='0' " + whereSql + " order by bmid asc";
            //System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            // 节点列表（散列表，用于临时存储节点对象）
            HashMap nodeList = new HashMap();
            while (rs.next()) {
                Node node = new Node();
                node.id = rs.getString("bmid");
                node.text = rs.getString("bmmc");
                node.parentId = rs.getString("pbmid");
                nodeList.put(node.id, node);
            }
            // 根节点
            Node root = null;
            // 构造无序的多叉树
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                Node node = (Node) ((Map.Entry) it.next()).getValue();

	/*				if (node.parentId==null ||"".equals(node.parentId)) {
						root = node;
					} else {
						((Node) nodeList.get(node.parentId)).addChild(node);

					}*/

                String dw = deptid.substring(0, 4);
                //System.out.println("&&&&&&&&&&&&&&&&"+deptid);
                if (deptid.endsWith("00000000")) {
                    if (node.id.equals("7100000000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else if (deptid.endsWith("000000")) {
                    if (node.id.endsWith("000000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else if (deptid.endsWith("0000")) {
                    if (node.id.endsWith("0000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else {
                    if (node.id.endsWith("00")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                }


            }
            // 输出无序的树形菜单的JSON字符串
            //System.out.println(root);
            // 对多叉树进行横向排序
            root.sortChildren();
            // 输出有序的树形菜单的JSON字符串
            //System.out.println(root);
            if (con_kq != null && !con_kq.isClosed()) {
                rs.close();
                st.close();
            }

            return root;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    //--------------------------业务方法----------------------------

    //获取index.jsp首页左侧菜单
    public List getMenu(String userid) throws SQLException {
        try {
            List lt = new ArrayList();
            if (con_oa == null || con_oa.isClosed()) {
                con_oa = pm.getConnection();
            }
            Statement st = con_oa.createStatement();
            String sql = "select id,name,pid,icon from t_role t where pid=0 and t.id in (select roleid from t_person_role t1 where t1.personid ='" + userid + "' ) order by seq asc";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Map map = new HashMap();
                String id = rs.getString("id");
                String mainMeau = rs.getString("name");
                String icon = rs.getString("icon");
                map.put("mainMenu", mainMeau);
                map.put("icon", icon);
                List params = new ArrayList();
                params.add(id);
                String sql1 = "select id,name,pid,icon,url from t_role t where pid=?";
                List<Map<String, Object>> list = findModeResult1(sql1, params, "cd", con_oa);
                map.put("menus", list);
                lt.add(map);
            }

            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
            return lt;

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    //获取点击【修改】按钮角色、权限、通讯录、特殊考勤、特殊考勤组详情
    public Map getDetailed(String zj, String method) throws Exception {
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }
        Statement st = con_oa.createStatement();
        String sql = "";
        Map m = new HashMap();
        if ("cx_jsgl".equals(method)) { //查询角色列表
            sql = "select id as jsid,pid as fid,name as jsmc,icon as tb,url as ymdz,bz from t_role where id = '" + zj + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                m.put("jsid", rs.getString("jsid"));
                m.put("fid", rs.getString("fid"));
                m.put("jsmc", rs.getString("jsmc"));
                m.put("tb", rs.getString("tb"));
                m.put("ymdz", rs.getString("ymdz"));
                m.put("bz", rs.getString("bz"));
            }
            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
        } else if ("cx_qxgl".equals(method)) { //查询权限列表
            sql = "select t.bmid as bm,t1.personid as xm,t1.roleid as js,t1.gslb from t_ydbm t,t_person_role t1,person p  where t.oa_bmid=p.dep_id and t1.personid = p.id  and t.off = '0' and t1.id = '" + zj + "' " +
                    "union " +
                    "select t.bmid as bm,t1.personid as xm,t1.roleid as js,t1.gslb from t_mdkbm t,t_person_role t1,person p  where t.oa_bmid=p.dep_id and t1.personid = p.id and t.off = '0' and t1.id = '" + zj + "' " +
                    "union " +
                    "select t.bmid as bm,t1.personid as xm,t1.roleid as js,t1.gslb from t_wpsbm t,t_person_role t1,person p  where t.oa_bmid=p.dep_id and t1.personid = p.id and t.off = '0' and t1.id = '" + zj + "' ";
            //System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                m.put("bm", rs.getString("bm"));
                m.put("xm", rs.getString("xm"));
                m.put("js", rs.getString("js"));
                m.put("gslb", rs.getString("gslb"));
            }
            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
        } else if ("cx_ydtx".equals(method)) { //查询通讯录列表
            sql = "select t.dw,t.xm,t.sjhm,t.gh,t.dh,t.fjh from t_ydtx t where t.id = '" + zj + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                m.put("bm", rs.getString("dw"));
                m.put("lxr", rs.getString("xm"));
                m.put("fjh", rs.getString("fjh"));
                m.put("dh", rs.getString("dh"));
                m.put("sjhm", rs.getString("sjhm"));
                m.put("gh", rs.getString("gh"));
            }
            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
        } else if ("cx_tskq".equals(method)) { //查询特殊考勤列表
            sql = "select t1.rq,t1.swsb,t1.swxb,t1.xwsb,t1.xwxb,t1.lb,t.tname from t_team t,t_team_tskq t1 where t1.teamid = t.id and t1.id = '" + zj + "'";
            //System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                m.put("rq", rs.getString("rq"));
                m.put("swsb", rs.getString("swsb"));
                m.put("swxb", rs.getString("swxb"));
                m.put("xwsb", rs.getString("xwsb"));
                m.put("xwxb", rs.getString("xwxb"));
                m.put("lb", rs.getString("lb"));
                m.put("tname", rs.getString("tname"));
            }
            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
        } else if ("cx_team".equals(method)) { //查询特殊考勤组列表
            sql = "select t1.id,t1.tname,t1.tmember,t1.gslb from t_team t1 where t1.id = '" + zj + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                m.put("id", rs.getString("id"));
                m.put("tname", rs.getString("tname"));
                m.put("tmember", rs.getString("tmember"));
                m.put("gslb", rs.getString("gslb"));
            }
            if (con_oa != null && !con_oa.isClosed()) {
                rs.close();
                st.close();
            }
        }
        return m;
    }

    //增删改通讯录、角色、权限、特殊考勤、特殊考勤组
    public boolean updDetailed(List params, String method) {
        try {
            String sql = "";
            if ("xx_upd_jsgl".equals(method)) {
                sql = "update t_role set pid=?,name=?,pname=?,icon=?,url=?,bz=? where id = ?";
            } else if ("xx_add_jsgl".equals(method)) {
                sql = "insert into t_role (pid,name,pname,icon,url,bz) values(?,?,?,?,?,?)";
            } else if ("xx_del_jsgl".equals(method)) {
                sql = "delete from t_role where id = ?";

            } else if ("xx_upd_qxgl".equals(method)) {
                sql = "update t_person_role set personid=?,roleid=?,gslb=? where id = ?";
            } else if ("xx_add_qxgl".equals(method)) {
                sql = "insert into t_person_role (personid,roleid,gslb) values(?,?,?)";
            } else if ("xx_del_qxgl".equals(method)) {
                sql = "delete from t_person_role where id = ?";

            } else if ("xx_add_ydtx".equals(method)) {
                sql = "insert into t_ydtx (dw,sjhm,xm,fjh,gh,dh) values(?,?,?,?,?,?)";
            } else if ("xx_del_ydtx".equals(method)) {
                sql = "delete from t_ydtx where id = ?";
            } else if ("xx_upd_ydtx".equals(method)) {
                sql = "update t_ydtx set dw=?,sjhm=?,xm=?,fjh=?,gh=?,dh=? where id = ?";

            } else if ("xx_add_tskq".equals(method)) {
                sql = "insert into t_team_tskq (rq,swsb,swxb,xwsb,xwxb,lb,teamid) values(?,?,?,?,?,?,?)";
            } else if ("xx_del_tskq".equals(method)) {
                sql = "delete from t_team_tskq where id = ?";
            } else if ("xx_upd_tskq".equals(method)) {
                sql = "update t_team_tskq set rq=?,swsb=?,swxb=?,xwsb=?,xwxb=?,lb=?,teamid=? where id = ?";

            } else if ("xx_add_team".equals(method)) {
                sql = "insert into t_team (tname,tmember,gslb) values(?,?,?)";
            } else if ("xx_del_team".equals(method)) {
                sql = "delete from t_team where id = ?";
            } else if ("xx_upd_team".equals(method)) {
                sql = "update t_team set tname=?,tmember=?,gslb=? where id = ?";
            }
            return updateByPreparedStatement(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    //获取通讯录、角色、权限、特殊考勤、特殊考勤组列表
    public Map<String, Object> getCxlb(HttpServletRequest req, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }

        HttpSession session = req.getSession();
        String userid = (String) session.getAttribute("userid");
        String dept = (String) session.getAttribute("name");
        String pageNum = req.getParameter("page");

        int pageNo = 0;
        if (pageNum != null && !pageNum.trim().equals("")) {
            pageNo = Integer.parseInt(pageNum);
        }
        //每页显示记录数
        String pageSize = req.getParameter("rows");
        int size = 0;
        if (pageSize != null && !pageSize.trim().equals("")) {
            size = Integer.parseInt(pageSize);
        }
        //String sortName = req.getParameter("sort");//排序字段
        //String sortOrder = req.getParameter("order");//排序的顺序
        String whereSql = "";
        String sql = "";
        String seq = "";
        List params = new ArrayList();
        if ("sy_ydtx".equals(method)) { //查询电脑端通讯录列表
            String lxr = new String(req.getParameter("lxr").getBytes("ISO-8859-1"), "gb2312");
            String dw = req.getParameter("dw");
            String lxdh = req.getParameter("lxdh");

            if (!isEmpty(lxr)) {
                whereSql += " and t.xm like  '%" + lxr + "%' ";
            }

/*				if(!isEmpty(dw)){
					if(!"".equals(dw)){
						if(dw.endsWith("00000000")){
							whereSql+=" and d.id1 like '"+dw.substring(0,2)+"%'";
						}else if(dw.endsWith("000000")){
							whereSql+=" and d.id1 like '"+dw.substring(0,4)+"%'";
						}else if(dw.endsWith("0000")){
							whereSql+=" and d.id1 like '"+dw.substring(0,6)+"%'";
						}else{
							whereSql+=" and d.id1 like '"+dw.substring(0,8)+"%'";
						}
					}
				}*/

            if (!isEmpty(lxdh)) {
                whereSql += " and (t.sjhm = '" + lxdh + "' or t.dh = '" + lxdh + "' or t.gh = '" + lxdh + "')";
            }


            sql = "select t.id,t.dw,t.xm,t.sjhm,t.dh,t.gh,t.fjh from t_ydtx t where  t.off='0' " + whereSql + " order by t.id asc";
        } else if ("sy_ydtx_wx".equals(method)) { //查询微信端通讯录列表

            String lxr = new String(req.getParameter("lxr").getBytes("ISO-8859-1"), "gb2312");
            if (!isEmpty(lxr)) {
                whereSql += " and t.xm like  '%" + lxr + "%' ";
            }

            sql = "select t.id,t.dw,t.xm,t.sjhm,t.dh,t.gh,t.fjh from t_ydtx t where t.off='0' " + whereSql + " order by t.id asc";
            //System.out.println(sql+"++++");
        } else if ("sy_jsgl".equals(method)) { //查询角色列表
            String jsmc = new String(req.getParameter("jsmc").getBytes("ISO-8859-1"), "gb2312");
            String ymdz = req.getParameter("ymdz");
            String fid = req.getParameter("fid");

            if (!isEmpty(jsmc)) {
                whereSql += " and t.name like  '%" + jsmc + "%' ";
            }

            if (!isEmpty(ymdz)) {
                whereSql += " and t.url like  '%" + ymdz + "%' ";
            }

            if (!isEmpty(fid)) {
                whereSql += " and t.pid = '" + fid + "'";
            }


            sql = "select t.id,t.name as jsmc,t.pname as fid,case when t.icon='icon-menu1' then '一级菜单图标' when t.icon='icon-menu2' then '二级图标菜单' end as tb,t.url as ymdz,t.bz from t_role t where 1=1 " + whereSql + " order by t.id asc";
        } else if ("sy_qxgl".equals(method)) { //查询权限列表
            String bm = req.getParameter("bm");
            String xm = req.getParameter("xm");
            String js = req.getParameter("js");
            String gslb = req.getParameter("gslb");
            if (!isEmpty(bm)) {
                if (bm.endsWith("00000000")) {
                    whereSql += " and t2.bmid like '" + bm.substring(0, 2) + "%'";
                } else if (bm.endsWith("000000")) {
                    whereSql += " and t2.bmid like '" + bm.substring(0, 4) + "%'";
                    //whereSql_oary+=" and b.bmid like '"+cxbm.substring(0,2)+"%'";
                } else if (bm.endsWith("0000")) {
                    whereSql += " and t2.bmid like '" + bm.substring(0, 6) + "%'";
                } else if (bm.endsWith("00")) {
                    whereSql += " and t2.bmid like '" + bm.substring(0, 8) + "%'";
                } else {
                    whereSql += " and t2.bmid = '" + bm + "'";
                }
            }


            if (!isEmpty(xm)) {
                whereSql += " and p.id = '" + xm + "' ";
            }

            if (!isEmpty(js)) {
                js = " ('" + js.replace(",", "','") + "') ";
                whereSql += " and t.id in " + js;
            }

            if (!isEmpty(gslb)) {
                whereSql += " and t1.gslb = '" + gslb + "' ";
                if ("41".equals(gslb)) {
                    sql = "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_ydbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                            + whereSql;
                } else if ("51".equals(gslb)) {
                    sql = "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_mdkbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                            + whereSql;
                } else if ("61".equals(gslb)) {
                    sql = "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_wpsbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                            + whereSql;
                }
            } else {
                sql = "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_ydbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                        + whereSql +
                        " union " +
                        "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_wpsbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                        + whereSql
                        + " union " +
                        "select t1.id,t.name as js,p.truename as xm,t2.bmmc as dw,case when t1.gslb='41' then '亚都实业' when t1.gslb = '51' then '迈迪科' when t1.gslb='61' then '威浦仕' end as gslb from t_role t,person p,t_person_role t1,t_mdkbm t2 where t1.personid = p.id and t1.roleid = t.id and t2.oa_bmid = p.dep_id "
                        + whereSql;
            }
        } else if ("sy_tskq".equals(method)) { //查询特殊考勤列表
            String lb = req.getParameter("lb");
            String ksrq = req.getParameter("sqksrq");
            String jsrq = req.getParameter("sqjsrq");
            String tname = new String(req.getParameter("tname").getBytes("ISO-8859-1"), "gb2312");
            whereSql += "";
            if (!isEmpty(lb)) {
                whereSql += " and t.lb = '" + lb + "'";
            }

            if (!isEmpty(tname)) {
                whereSql += " and t1.tname like '%" + tname + "%' ";
            }

            if (!isEmpty(ksrq)) {
                whereSql += " and t.rq >= '" + ksrq + "'";
            }

            if (!isEmpty(jsrq)) {
                whereSql += " and t.rq <='" + jsrq + "'";
            }
            sql = "select t.id,date_format(t.rq,'%Y-%m-%d') as rq,t.swsb,t.swxb,t.xwsb,t.xwxb,t.lb,t1.tname from t_team_tskq t,t_team t1 where t.teamid = t1.id "
                    + whereSql + " order by rq desc";
            //System.out.println(sql);
        } else if ("sy_team".equals(method)) { //查询特殊考勤组列表
            String tmember = new String(req.getParameter("tmember").getBytes("ISO-8859-1"), "gb2312");
            String gslb = req.getParameter("gslb");
            String tname = new String(req.getParameter("tname").getBytes("ISO-8859-1"), "gb2312");
            whereSql = "where 1=1 ";
            if (!isEmpty(tmember)) {
                whereSql += " and t.tmember like '%" + tmember + "%'";
            }

            if (!isEmpty(tname)) {
                whereSql += " and t.tname = '" + tname + "' ";
            }

            if (!isEmpty(gslb)) {
                whereSql += " and t.gslb >= '" + gslb + "'";
            }

            sql = "select t.id,case when t.gslb='41' then '亚都实业' when t.gslb = '51' then '迈迪科' when t.gslb='61' then '威浦仕（行政）' when t.gslb='71' then '威浦仕（车间）' end as gslb,t.tname,t.tmember from t_team t "
                    + whereSql + " order by t.id desc";
            //System.out.println(sql);
        }
        sql += "      limit " + (pageNo - 1) * size + "," + size;
        Map<String, Object> m = findModeResult(sql, params, method, con_oa);
        return m;
    }

    //获取打卡记录列表
    public Map<String, Object> getDkjl(HttpServletRequest req, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        if (con_kq == null || con_kq.isClosed()) {
            con_kq = getKqjlCon();
        }

        HttpSession session = req.getSession();
        String userid = (String) session.getAttribute("userid");
        String dept = (String) session.getAttribute("name");
        String pageNum = req.getParameter("page");

        int pageNo = 0;
        if (pageNum != null && !pageNum.trim().equals("")) {
            pageNo = Integer.parseInt(pageNum);
        }
        //每页显示记录数
        String pageSize = req.getParameter("rows");
        int size = 0;
        if (pageSize != null && !pageSize.trim().equals("")) {
            size = Integer.parseInt(pageSize);
        }

        String whereSql = "";
        String sql = "";
        String seq = "";
        List params = new ArrayList();
        if ("sy_dkjl".equals(method)) { //查询打卡记录列表
            String ryxm = new String(req.getParameter("ryxm").getBytes("ISO-8859-1"), "gb2312");
            String rygh = req.getParameter("rygh");
            String cxn = req.getParameter("cxn");
            String cxy = req.getParameter("cxy");
            String cxr = req.getParameter("cxr");


            if (!isEmpty(ryxm)) {
                whereSql += " and t.employeeName =  '" + ryxm + "' ";
            }

            if (!isEmpty(rygh)) {
                whereSql += " and t.employeeCode like  '%" + rygh + "' ";
            }


            if (!isEmpty(cxr)) {
                whereSql += " and t1.cardtime like '" + cxn + "-" + cxy + "-" + cxr + "%'";
            } else {
                whereSql += " and t1.cardtime like '" + cxn + "-" + cxy + "%'";
            }


            sql = "select case when t2.DevName like'wps%' then '威浦仕' when t2.DevName like '迈迪科%' then '迈迪科' else '亚都' end as gslb,t.EmployeeCode as rygh,t.EmployeeName as ryxm,date_format(t1.cardtime, '%Y-%m-%d %H:%i:%s') as dksj,t2.devid as kqjbh,t2.DevName as kqjmc,t2.IPAddress as kqjip from kqz_employee t,kqz_card t1,kqz_devinfo t2 where" +
                    " t.employeeid = t1.EmployeeID and t1.DevID= t2.DevID " + whereSql + " order by t1.cardtime asc";
        }
        sql += "      limit " + (pageNo - 1) * size + "," + size;
        //System.out.println("****************"+sql);
        Map<String, Object> m = findModeResult(sql, params, method, con_kq);
        return m;
    }

    //发货及时率统计
    public Map<String, Object> getFhjls(HttpServletRequest req, String method) throws Exception {
        if (con_u8_002 == null || con_u8_002.isClosed()) {
            con_u8_002 = new JdbcUtilsU8().getConnection();
        }
        Map map = null;
        HttpSession session = req.getSession();
        String userid = (String) session.getAttribute("userid");
        String dept = (String) session.getAttribute("name");
/*			String pageNum = req.getParameter("page");

			int pageNo = 0;
			if(pageNum != null && !pageNum.trim().equals("")){
				pageNo =Integer.parseInt(pageNum);
			}
			//每页显示记录数
			String pageSize = req.getParameter("rows");
			int size = 0;
			if(pageSize != null && !pageSize.trim().equals("")){
				size =Integer.parseInt(pageSize);
			}

			int m = (pageNo-1)*size;
			int n = m+size;

			int ks = n-m;
			int js = m;*/

        String sqksrq = req.getParameter("sqksrq");
        String sqjsrq = req.getParameter("sqjsrq");
        String cxbs = req.getParameter("cxbs");
        String whereSql = "  ";
        if (!isEmpty(sqksrq)) {
            whereSql += " and 制单日期 >= '" + sqksrq + "'";
        }

        if (!isEmpty(sqjsrq)) {
            whereSql += " and 制单日期 <= '" + sqjsrq + "'";
        }

        if ("jsl".equals(cxbs)) {
/*				String sql_wfh = "select count(1) as wfhzs from fhjsl where 销售出库时间 is null"+whereSql;
				String sql_cfh = "select count(1) as cfhzs from fhjsl where 销售出库时间 is not null  and 发货天数 >2"+whereSql;
				String sql_zs = "select count(1) as zs from fhjsl where 1=1 "+whereSql;*/

            String sql = "select lb,sl from ( " +
                    "select 'wfh' as lb,count(1) as sl from fhjsl where 销售出库时间 is null" + whereSql +
                    " union all " +
                    "select 'cfh' as lb,count(1) as sl from fhjsl where 销售出库时间 is not null  and 发货天数 >2" + whereSql +
                    " union all " +
                    "select 'zs' as lb,count(1) as sl from fhjsl where 1=1 " + whereSql + " ) a";


            Statement st = con_u8_002.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int sl = 0;
            map = new HashMap();
            while (rs.next()) {
                String lb = rs.getString("lb");
                sl = rs.getInt("sl");
                map.put(lb, sl);
            }

        } else {
            if ("wfh".equals(cxbs)) {
                whereSql += " and 销售出库时间 is null ";
            } else if ("cfh".equals(cxbs)) {
                whereSql += " and 销售出库时间 is not null  and 发货天数 >2";
            }
				/*String sql = "select top "+ks+" 批号 as ph, 销售提货号 as xsthh, CONVERT(varchar(10), 制单日期, 120) as zdrq,存货编码 as chbm,存货名称 as chmc, 发货单数量 as fhdsl, 客户简称 as khjc,  CONVERT(varchar(10), 销售开票时间, 120) as xskpsj,CONVERT(varchar(10), 销售出库时间, 120) as xscksj," +
						" 销售出库单号 as xsckdh, 发货天数 as fhts from fhjsl   WHERE (批号  NOT IN (SELECT TOP "+js+" 批号 FROM fhjsl  where 1=1 "+whereSql+"))"+whereSql;*/

            String sql = "select  批号 as ph, 销售提货号 as xsthh, CONVERT(varchar(10), 制单日期, 120) as zdrq,存货编码 as chbm,存货名称 as chmc, 发货单数量 as fhdsl, 客户简称 as khjc,  CONVERT(varchar(10), 销售开票时间, 120) as xskpsj,CONVERT(varchar(10), 销售出库时间, 120) as xscksj," +
                    " 销售出库单号 as xsckdh, 发货天数 as fhts from fhjsl where 1=1 " + whereSql;
            String sqlCount = "select 批号  from fhjsl  where 1=1 " + whereSql;
            map = new HashMap();
            map = findModeResult2(sql, sqlCount, null, method, con_u8_002);
        }


        return map;
    }

}
