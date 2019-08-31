package org.yadu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.yadu.dao.Node;

public class JdbcKqxx_bak {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:mysql://10.0.10.30:3306/hwatt";
    private static final String WPS_URL = "jdbc:mysql://10.0.10.31:3306/hwatt";
    private PoolManager pm = null;
    private Connection con_oa;
    private Connection con_kq;

    //加载mysql驱动，OA数据库连接池
    public JdbcKqxx_bak() {
        try {
            Class.forName(DRIVER);
            pm = new PoolManager();
        } catch (Exception localException) {
            System.out.println("加载考勤数据库驱动异常！");
        }
    }


    //获取考勤记录数据库连接
    public Connection getKqjlCon() {
        try {
            this.con_kq = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("获取考勤数据库连接异常！");
            e.printStackTrace();
        }
        return this.con_kq;
    }

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
            whereSql_oary += " where e.bmid like '" + cxbm.substring(0, 2) + "%'";
        } else if (cxbm.endsWith("000000")) {
            whereSql_oary += " where e.bmid like '" + cxbm.substring(0, 4) + "%'";
            //whereSql_oary+=" where e.bmid like '"+cxbm.substring(0,2)+"%'";
        } else if (cxbm.endsWith("0000")) {
            whereSql_oary += " where e.bmid like '" + cxbm.substring(0, 6) + "%'";
        } else if (cxbm.endsWith("00")) {
            whereSql_oary += " where e.bmid like '" + cxbm.substring(0, 8) + "%'";
        } else {
            whereSql_oary += " where e.bmid = '" + cxbm + "'";
        }

        //根据查询姓名获取过滤条件
        if (!isEmpty(cxxm)) {
            //whereSql_oary += " and e.oaryid = '" + cxxm + "' ";
        }

        //处理日期时间
        DateFormat df_rqsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df_rq = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_sj = new SimpleDateFormat("HH:mm:ss");
        df_rq.setLenient(false);
        SimpleDateFormat sdf_xq = new SimpleDateFormat("EEE");


        //序号
        int bs = 0;
        String whereSql_kqjl = "";
        bs++;

        int cdcs = 0;
        int cdcs_15 = 0;
        int ztcs = 0;
        int bqcs = 0;
        float qjts_hj = 0.0f;
        float qjts_gs = 0.0f;
        float qjts_sj = 0.0f;
        float qjts_qt = 0.0f;

        float byjb = 0.0f;
        float tdfjts = 0.0f;

        float ccts = 0.0f;
        float mqts = 0.0f;
        float dkts = 0.0f;
        float bqts = 0.0f;
        //Map mrqbry = null;

        for (int i = 1; i <= myts; i++) {
            // mrqbry = new HashMap();
            Date date = df_rq.parse(cxsj + "-" + i);
            String fdate = df_rq.format(date);

            //查询每个人每天的考勤记录
            String sql_kqjl = "select d.bmmc as bm,e.ryxm as xm,e.ryid as kqh,e.oaryid,left(t.CardTime,10) as dkrq,group_concat(right(t.CardTime,8)) as dksj from t_ydry e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) and t.CardTime like '" +
                    fdate + "%' left join  t_ydbm d on e.bmid = d.bmid " + whereSql_oary + " and e.employeeid!='' group by e.ryid ";

            Statement st_kqjl = this.con_kq.createStatement();
            ResultSet rs_kqjl = st_kqjl.executeQuery(sql_kqjl);
            List mrmtkq = new ArrayList();
            while (rs_kqjl.next()) {
                //rs_kqjl.first();
                //每个人每天的考勤记录存放一个Map
                Map map = new HashMap();
                map.put("rq", Integer.valueOf(Integer.parseInt(df_rq.format(date).substring(8, 10))));
                map.put("xq", sdf_xq.format(date).substring(2, 3));
                String ryxm = rs_kqjl.getString("xm");
                String gh = rs_kqjl.getString("kqh");
                String bm = rs_kqjl.getString("bm");
                String ryid = rs_kqjl.getString("oaryid");
                map.put("xm", ryxm);
                map.put("gh", gh);
                map.put("bm", bm);
                whereSql_kqjl = " where e.employeeCode = '" + gh + "'";
                String dksj = rs_kqjl.getString("dksj");

                if (!isEmpty(dksj)) {
                    String[] dk = dksj.split(",");
                    for (int j = 0; j < dk.length; j++) {
                        if (isSummer(df_rq.format(date), df_rq)) { //夏半年
                            if (compareTime(dk[j], "08:01:00", df_sj) <= 0L) {
                                map.put("swsb", "正常");
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "08:01:00", df_sj) > 0L && compareTime(dk[j], "08:15:00", df_sj) <= 0L) { //上午上班迟到小于15分钟
                                map.put("swsb", "迟到");
                                cdcs++;
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "08:15:00", df_sj) > 0L && compareTime(dk[j], "10:00:00", df_sj) < 0L) { //上午上班迟到大于15分钟
                                map.put("swsb", "迟到");
                                cdcs_15++;
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "12:00:00", df_sj) >= 0L && compareTime(dk[j], "13:00:00", df_sj) < 0L) { //上午下班正常
                                map.put("swxb", "正常");
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "11:00:00", df_sj) >= 0L && compareTime(dk[j], "12:00:00", df_sj) < 0L) {
                                map.put("swxb", "早退");
                                ztcs++;
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "14:31:00", df_sj) <= 0L && compareTime(dk[j], "13:00:00", df_sj) >= 0L) {
                                map.put("xwsb", "正常");
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "14:31:00", df_sj) > 0L && compareTime(dk[j], "14:45:00", df_sj) <= 0L) {
                                map.put("xwsb", "迟到");
                                cdcs++;
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "14:45:00", df_sj) > 0L && compareTime(dk[j], "15:30:00", df_sj) < 0L) {
                                map.put("xwsb", "迟到");
                                cdcs_15++;
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "18:30:00", df_sj) >= 0L) {
                                map.put("xwxb", "正常");
                                dkts += 0.25f;
                            } else if (compareTime(dk[j], "18:30:00", df_sj) < 0L && compareTime(dk[j], "16:30:00", df_sj) >= 0L) {
                                map.put("xwxb", "早退");
                                ztcs++;
                                dkts += 0.25f;
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
                        } else { //冬半年

                        }

                    } //for循环end
                } else { //打卡时间为空
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
                }

                map.put("cdcs", Integer.valueOf(cdcs));
                map.put("cdcs_15", Integer.valueOf(cdcs_15));
                map.put("ztcs", Integer.valueOf(ztcs));
                //OA补签
                String sql_oabq = "select u.field4 as bqcx,u.field8 as bqlb from utm_00103 u where u.field1 = '" + ryid + "' and u.field2 = '" + df_rq.format(date) + "' and u.field1 in (select per_id from document d where d.state='2' and d.per_id = '" + ryid + "')";
                Statement st_oabq = this.con_oa.createStatement();
                ResultSet rs_oabq = st_oabq.executeQuery(sql_oabq);
                while (rs_oabq.next()) {
                    String bqcx = rs_oabq.getString("bqcx");
                    String bqlb = rs_oabq.getString("bqlb");
                    if ("0".equals(bqcx)) {
                        if (isEmpty((String) map.get("swsb"))) {
                            if ("0".equals(bqlb)) {
                                map.put("swsb", "因公补签");
                            } else if ("1".equals(bqlb)) {
                                map.put("swsb", "因私补签");
                                bqcs++;
                            }
                            bqts += 0.25f;
                        }
                    } else if ("1".equals(bqcx)) {
                        if (isEmpty((String) map.get("swxb"))) {
                            if ("0".equals(bqlb)) {
                                map.put("swxb", "因公补签");
                            } else if ("1".equals(bqlb)) {
                                map.put("swxb", "因私补签");
                                bqcs++;
                            }
                            bqts += 0.25f;
                        }
                    } else if ("2".equals(bqcx)) {
                        if (isEmpty((String) map.get("xwsb"))) {
                            if ("0".equals(bqlb)) {
                                map.put("xwsb", "因公补签");
                            } else if ("1".equals(bqlb)) {
                                map.put("xwsb", "因私补签");
                                bqcs++;
                            }
                            bqts += 0.25f;
                        }
                    } else if ("3".equals(bqcx)) {
                        if (isEmpty((String) map.get("xwxb"))) {
                            if ("0".equals(bqlb)) {
                                map.put("xwxb", "因公补签");
                            } else if ("1".equals(bqlb)) {
                                map.put("xwxb", "因私补签");
                                bqcs++;
                            }
                            bqts += 0.25f;
                        }

                    }

                }

                map.put("bqcs", Integer.valueOf(bqcs));


                //oa请假
                String sql_oaqj = "select u.field4 as qjlb,u.id from utm_00145 u where u.field1 = '" + ryid + "' and u.field6 <= '" + df_rq.format(date) + "' and u.field7>= '" + df_rq.format(date) + "' and u.field1 in (select per_id from document d where d.state='2' and d.per_id = '" + ryid + "')";
                Statement st_oaqj = this.con_oa.createStatement();
                ResultSet rs_oaqj = st_oaqj.executeQuery(sql_oaqj);
                while (rs_oaqj.next()) {
                    String qjlb = rs_oaqj.getString("qjlb");
                    if (isEmpty((String) map.get("swsb"))) {
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
                            qjts_qt += 0.25f;
                        }
                        map.put("swsb", lbmc);
                    }

                    if (isEmpty((String) map.get("swxb"))) {
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
                            qjts_qt += 0.25f;
                        }
                        map.put("swxb", lbmc);
                    }
                    if (isEmpty((String) map.get("xwsb"))) {
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
                            qjts_qt += 0.25f;
                        }
                        map.put("xwsb", lbmc);
                    }

                    if (isEmpty((String) map.get("xwxb"))) {
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
                            qjts_qt += 0.25f;
                        }
                        map.put("xwxb", lbmc);
                    }
                }


                //oa出差
                String sql_oacc = "select u.field3,u.id from utm_00101 u where u.field1 = '" + ryid + "' and u.field6 <= '" + df_rq.format(date) + "' and u.field7>= '" + df_rq.format(date) + "' and u.field3 in (select per_id from document d where d.state='2' and d.per_id = '" + ryid + "')";
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
                String sql_oajb = "select u.field10 as jbkssj,u.field12 as jbzgs from utm_00156 u where u.field2 = '" + ryid + "' and u.field10 like '" + df_rq.format(date) + "%' and u.field2 in (select per_id from document d where d.state = '2' and d.per_id = '" + ryid + "')";
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

                //判断新入职人员
/*		              	             String tdSql = "select min(t.cardid),left(t.CardTime,10) as rzsj from kqz_employee e left join  kqz_card t on (e.EmployeeID=t.EmployeeID) " ;
		              	          Statement st_kqry = this.con_kq.createStatement();
		              	          ResultSet rs_kqry = st_kqry.executeQuery(tdSql);
		              	        //System.out.println("td:"+tdSql); 
		              	          String rzsj = "";
		              	          while (rs_kqry.next()) {
		              	            rzsj = rs_kqry.getString("rzsj");
		              	          }
*/
                String tsSql = "select rq,swsb,swxb,xwsb,xwxb,kqbs,tsry from t_tskq where rq = '" + df_rq.format(date) + "'";

                Statement st_oatskq = this.con_oa.createStatement();
                ResultSet rs_oatskq = st_oatskq.executeQuery(tsSql);
                while (rs_oatskq.next()) {
                    String ts_kqbs = rs_oatskq.getString("kqbs");
                    String ts_rq = rs_oatskq.getString("rq");
                    String ts_ry = rs_oatskq.getString("tsry");
                    String ts_swsb = rs_oatskq.getString("swsb");
                    String ts_swxb = rs_oatskq.getString("swxb");
                    String ts_xwsb = rs_oatskq.getString("xwsb");
                    String ts_xwxb = rs_oatskq.getString("xwxb");
                    if ("tdts".equals(ts_kqbs)) {
                        if (!isEmpty(ts_swsb)) {
                            map.put("swsb", ts_swsb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_swxb)) {
                            map.put("swxb", ts_swxb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_xwsb)) {
                            map.put("xwsb", ts_xwsb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_xwxb)) {
                            map.put("xwxb", ts_xwxb);
                            tdfjts += 0.25f;
                        }
                    } else if ("gxts".equals(ts_kqbs)) {
                        if (map.get("swsb").toString().contains("假") || map.get("swxb").toString().contains("假")) {
                            qjts_qt = qjts_qt - 0.5f;
                        }

                        if (map.get("xwsb").toString().contains("假") || map.get("xwxb").toString().contains("假")) {
                            qjts_qt = qjts_qt - 0.5f;
                        }

                        if (!isEmpty(ts_swsb) && !"出差".equals((String) map.get("swsb"))) {
                            map.put("swsb", ts_swsb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_swxb) && !"出差".equals((String) map.get("swxb"))) {
                            map.put("swxb", ts_swxb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_xwsb) && !"出差".equals((String) map.get("xwsb"))) {
                            map.put("xwsb", ts_xwsb);
                            tdfjts += 0.25f;
                        }
                        if (!isEmpty(ts_xwxb) && !"出差".equals((String) map.get("xwxb"))) {
                            map.put("xwxb", ts_xwxb);
                            tdfjts += 0.25f;
                        }
                    } else {
                        if ((isEmpty((String) map.get("swsb"))) || ("无".equals((String) map.get("swsb")))) {
                            map.put("swsb", ts_swsb);
                        }
                        if ((isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb")))) {
                            map.put("swxb", ts_swxb);
                        }

                        if ((isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb")))) {
                            map.put("xwsb", ts_xwsb);
                        }

                        if ((isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb")))) {
                            map.put("xwxb", ts_xwxb);
                        }

                    }

                }

                if (("02802".equals(gh)) || ("03598".equals(gh)) || ("00034".equals(gh)) || ("00003".equals(gh)) || ("03536".equals(gh)) || ("00004".equals(gh)) || ("03789".equals(gh)) || ("04560".equals(gh)) ||
                        ("03893".equals(gh)) || ("00011".equals(gh)) || ("04498".equals(gh)) || ("02670".equals(gh) && compareTime(df_rq.format(date), "2017-02-01", df_rq) >= 0L) || ("00102".equals(gh) && compareTime(df_rq.format(date), "2017-02-01", df_rq) >= 0L)) {
                    String currTime = df_rqsj.format(new Date());
                    if ((compareTime(currTime, df_rq.format(date) + " 12:00:00", df_rqsj) >= 0L) && (
                            (isEmpty((String) map.get("swxb"))) || ("无".equals((String) map.get("swxb"))))) {
                        map.put("swxb", "免签");
                    }
                    if ("00102".equals(gh)) {
                        if ((compareTime(currTime, df_rq.format(date) + " 18:30:00", df_rqsj) >= 0L) && (
                                (isEmpty((String) map.get("xwxb"))) || ("无".equals((String) map.get("xwxb"))))) {
                            map.put("xwxb", "免签");
                        }
                    } else {
                        if ((compareTime(currTime, df_rq.format(date) + " 14:30:00", df_rqsj) >= 0L) && (
                                (isEmpty((String) map.get("xwsb"))) || ("无".equals((String) map.get("xwsb"))))) {
                            map.put("xwsb", "免签");
                        }

                    }

                }


                map.put("gxts", Float.valueOf(tdfjts));

                float mymqts = myts - 3 - tdfjts;
                if (df_rq.format(date).startsWith("2017-01") || df_rq.format(date).startsWith("2017-02")) {
                    mymqts = myts - 2 - tdfjts;
                }
                float kqzs = 0.0F;

                kqzs = dkts + bqts + ccts;

                if ((kqzs + "").endsWith(".25"))
                    kqzs = (float) Math.floor(kqzs);
                else if ((kqzs + "").endsWith(".75")) {
                    kqzs = (float) ((float) Math.floor(kqzs) + 0.5D);
                }

                if (kqzs < 0.0F) {
                    kqzs = 0.0F;
                }

                map.put("ccts", Float.valueOf(ccts));
                map.put("qjts", Float.valueOf(qjts_qt));
                map.put("qjts_hj", Float.valueOf(qjts_hj));
                map.put("qjts_sj", Float.valueOf(qjts_sj));
                map.put("qjts_gs", Float.valueOf(qjts_gs));

                map.put("ykqts", Float.valueOf(kqzs));
                map.put("mqts", Float.valueOf(mymqts));
                // mrmtkq.add(map);
                lt.add(map);

            }
            // mrqbry.put(fdate, mrmtkq);
            // lt.add(mrmtkq);
        }

        System.out.println(lt);
        return lt;
    }


    public List ydKqxx_wx(HttpServletRequest req, String method) throws Exception {
        return null;
    }

    public List wpsKqxx_pc(HttpServletRequest req, String method) throws Exception {
        return null;
    }

    public List wpsKqxx_wx(HttpServletRequest req, String method) throws Exception {
        return null;
    }

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
        Date dt1 = df.parse("2016-05-01");
        Date dt2 = df.parse("2016-10-01");
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

    public List loginUser(HttpServletRequest req) throws SQLException {
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }
        String userName = req.getParameter("gh");
        String password = req.getParameter("pwd");
        List params = new ArrayList();
        params.add(userName);
        params.add(password);
        String sql = "select p.id as oaryid,p.truename as oaryxm,p.stuff_id as oarygh,d.name as bmmc,d.id as bmid,d.dep_num as bmdm,p.IDENTITYNO as sfzh,p.oty_id as zwjb from person p,department d where P.dep_id = d.id and p.username=? and p.pinnumber=?";
        try {
            List<Map<String, Object>> list = findModeResult1(sql, params, "login", con_oa);
            //System.out.println(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询OA表单
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
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

    /**
     * 查询OA表单1
     */
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

    //获取字典下拉列表
    public List<Map<String, Object>> queryCode(Map<String, Object> map) throws SQLException {
        //System.out.println("2************************");
        // TODO Auto-generated method stub
        //jdbcUtils.getConnection();
        List params = new ArrayList();
        String zdmc = map.get("zdmc").toString();
        String zdlb = map.get("zdlb").toString();
        String sql = "";
        if ("父角色名称".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.id,t.name as text from t_role t where t.pid = ?";
        } else if ("人员".equals(zdmc)) {
            params.add(zdlb);
            sql = "select t.oaryid as id,t.ryxm as text from t_ydry t,t_ydbm t1 where t.bmid = t1.bmid and t.off='0' and t1.bmid = ?";
        } else {
            params.add(zdlb);
            params.add(zdmc);
            sql = "select t.value as id,t.name as text from udt_enumvalue t where enumid in (select id from udt_enumlist u where u.enum_type=? and u.name = ?)";
        }
        if ((this.con_kq == null) || (this.con_kq.isClosed())) {
            this.con_kq = this.getKqjlCon();
        }
        try {
            List<Map<String, Object>> list = findModeResult1(sql, params, "zd", con_kq);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取部门字典
    public Node getDept(HttpServletRequest req) throws Exception {
        List menuList = new ArrayList();
        try {
            if (con_kq == null || con_kq.isClosed()) {
                con_kq = getKqjlCon();
            }
            String deptid = req.getParameter("deptid");
            String whereSql = "";
            //根据查询部门获取过滤条件
            if (deptid.startsWith("4110") || deptid.startsWith("4102") || deptid.startsWith("411201")) {
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


            Statement st = con_kq.createStatement();
            String sql = "select t.bmid,t.pbmid,t.bmmc from t_ydbm t where t.off='0' " + whereSql + " order by bmid asc";
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
                if (deptid.startsWith("4110") || deptid.startsWith("4102") || deptid.startsWith("411201")) {
                    if (node.id.equals("4100000000")) {
                        root = node;
                    } else {
                        ((Node) nodeList.get(node.parentId)).addChild(node);
                    }
                } else if ("4102".equals(dw) || "4103".equals(dw) || "4104".equals(dw) || "4105".equals(dw) || "4106".equals(dw) || "4107".equals(dw) || "4108".equals(dw) || "4109".equals(dw) || "4111".equals(dw)
                        || "4112".equals(dw) || "4113".equals(dw) || "4114".equals(dw) || "4115".equals(dw)) {
                    if (deptid.endsWith("000000")) {
                        if (node.id.endsWith("000000")) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    } else {
                        if (node.id.endsWith("0000")) {
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
}
