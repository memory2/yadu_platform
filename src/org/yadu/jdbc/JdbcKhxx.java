package org.yadu.jdbc;

import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class JdbcKhxx {
    private PoolManager pm = null;
    private Connection con_oa;

    //加载mysql驱动，OA数据库连接池
    public JdbcKhxx() {
        try {
            pm = new PoolManager();
        } catch (Exception localException) {
            System.out.println("创建连接池对象异常！");
        }
    }

    //获取客户信息列表
    public Map<String, Object> getCxlb(HttpServletRequest req, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }

        HttpSession session = req.getSession();


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
        if ("sy_khjcxxcx".equals(method)) { //查询电脑端通讯录列表
            String khxm = new String(req.getParameter("khxm").getBytes("ISO-8859-1"), "gb2312");
            String khxb = req.getParameter("khxb");
            String khsjh = req.getParameter("khsjh");
            String glryxm = new String(req.getParameter("glryxm").getBytes("ISO-8859-1"), "gb2312");

            if (!isEmpty(khxm)) {
                whereSql += " and t.xm like  '%" + khxm + "%' ";
            }

            if (!isEmpty(khxb)) {
                whereSql += " and t.xb =  '" + khxb + "' ";
            }

            if (!isEmpty(khsjh)) {
                whereSql += " and t.sjh =  '" + khsjh + "' ";
            }

            if (!isEmpty(glryxm)) {
                whereSql += " and t.id in (select t1.glkhid from t_khglry t1 where t1.xm like  '%" + glryxm + "%') ";
            }


            sql = "select t.id,t.xm,case when t.xb='0' then '男' else '女' end as xb,t.csrq,t.jg,t.sjh,t.wx,t.qq,t.zz,t.txr,t.txsj,t.gxr,t.gxsj from t_khjcxx t where  t.zfbs='0' " + whereSql + " order by t.txsj desc";
        }

        //System.out.println(sql);
        sql += "      limit " + (pageNo - 1) * size + "," + size;
        Map<String, Object> m = findModeResult(sql, params, method, con_oa);
        return m;
    }

    //获取详情
    public Map getDetailed(String zj, String method) throws Exception {
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }
        Statement st = con_oa.createStatement();

        Map m = new HashMap();
        if ("cx_khgl".equals(method)) { //查询角色列表
            String sql_khxx = "select id,xm,xb ,csrq,jg,sjh,wx,qq,zz from t_khjcxx t where t.id = '" + zj + "'";
            //System.out.println(sql_khxx);
            ResultSet rs_khxx = st.executeQuery(sql_khxx);
            while (rs_khxx.next()) {
                m.put("xm", rs_khxx.getString("xm"));
                m.put("xb", rs_khxx.getString("xb"));
                m.put("csrq", rs_khxx.getString("csrq"));
                m.put("jg", rs_khxx.getString("jg"));
                m.put("sjh", rs_khxx.getString("sjh"));
                m.put("wx", rs_khxx.getString("wx"));
                m.put("qq", rs_khxx.getString("qq"));
                m.put("zz", rs_khxx.getString("zz"));
            }

            String sql_glry = "select id,xm,lxfs,gx,ywfc from t_khglry t where t.glkhid = '" + zj + "'";
            ResultSet rs_glry = st.executeQuery(sql_glry);
            int i = 1;
            while (rs_glry.next()) {
                if (i == 1) {
                    m.put("glryid1", rs_glry.getString("id"));
                    m.put("glryxm1", rs_glry.getString("xm"));
                    m.put("lxfs1", rs_glry.getString("lxfs"));
                    m.put("gx1", rs_glry.getString("gx"));
                    m.put("ywfc1", rs_glry.getString("ywfc"));

                }
                if (i == 2) {
                    m.put("glryid2", rs_glry.getString("id"));
                    m.put("glryxm2", rs_glry.getString("xm"));
                    m.put("lxfs2", rs_glry.getString("lxfs"));
                    m.put("gx2", rs_glry.getString("gx"));
                    m.put("ywfc2", rs_glry.getString("ywfc"));

                }

                if (i == 3) {
                    m.put("glryid3", rs_glry.getString("id"));
                    m.put("glryxm3", rs_glry.getString("xm"));
                    m.put("lxfs3", rs_glry.getString("lxfs"));
                    m.put("gx3", rs_glry.getString("gx"));
                    m.put("ywfc3", rs_glry.getString("ywfc"));

                }
                if (i == 4) {
                    m.put("glryid4", rs_glry.getString("id"));
                    m.put("glryxm4", rs_glry.getString("xm"));
                    m.put("lxfs4", rs_glry.getString("lxfs"));
                    m.put("gx4", rs_glry.getString("gx"));
                    m.put("ywfc4", rs_glry.getString("ywfc"));
                }
                i++;
            }

            String sql_glyw = "select ywqy,sxed,ywnl,khzt,glgs,wlxx,khms from t_khglyw t where t.glkhid = '" + zj + "'";
            ResultSet rs_glyw = st.executeQuery(sql_glyw);
            while (rs_glyw.next()) {
                m.put("ywqy", rs_glyw.getString("ywqy"));
                m.put("sxed", rs_glyw.getString("sxed"));
                m.put("ywnl", rs_glyw.getString("ywnl"));
                m.put("khzt", rs_glyw.getString("khzt"));
                m.put("glgs", rs_glyw.getString("glgs"));
                m.put("wlxx", rs_glyw.getString("wlxx"));
                m.put("khms", rs_glyw.getString("khms"));
            }


            if (con_oa != null && !con_oa.isClosed()) {
                st.close();
            }
        }
        return m;
    }

    //增删改客户信息
    public boolean updDetailed(HttpServletRequest req, String method) {
        try {
            HttpSession session = req.getSession();
            String dqry = (String) session.getAttribute("oaryxm");
            String dqsj = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

            List params_jcxx = new ArrayList();
            params_jcxx.add(req.getParameter("xm"));
            params_jcxx.add(req.getParameter("xb"));
            params_jcxx.add(req.getParameter("csrq"));
            params_jcxx.add(req.getParameter("jg"));
            params_jcxx.add(req.getParameter("sj"));
            params_jcxx.add(req.getParameter("wx"));
            params_jcxx.add(req.getParameter("qq"));
            params_jcxx.add(req.getParameter("zz"));
            params_jcxx.add(dqry);
            params_jcxx.add(dqsj);

            List params_glyw = new ArrayList();
            params_glyw.add(req.getParameter("ywqy"));
            params_glyw.add(req.getParameter("sxed"));
            params_glyw.add(req.getParameter("ywnl"));
            params_glyw.add(req.getParameter("khzt"));
            params_glyw.add(req.getParameter("glgs"));
            params_glyw.add(req.getParameter("wlxx"));
            params_glyw.add(req.getParameter("khms"));
            params_glyw.add(dqry);
            params_glyw.add(dqsj);


            if ("xx_upd_khgl".equals(method)) {

                params_jcxx.add(req.getParameter("zj"));
                String sql_jcxx = "update t_khjcxx set xm=?,xb=?,csrq=?,jg=?,sjh=?,wx=?,qq=?,zz=?,gxr=?,gxsj=? where id = ?";
                updateByPreparedStatement(sql_jcxx, params_jcxx);

                String sql_glry = "update t_khglry set xm=?,lxfs=?,gx=?,ywfc=?,gxr=?,gxsj=? where id = ?";
                String glryid1 = req.getParameter("glryid1");
                String glryid2 = req.getParameter("glryid2");
                String glryid3 = req.getParameter("glryid3");
                String glryid4 = req.getParameter("glryid4");

                if (!isEmpty(glryid1)) {
                    List params_glry1 = new ArrayList();
                    params_glry1.add(req.getParameter("glryxm1"));
                    params_glry1.add(req.getParameter("lxfs1"));
                    params_glry1.add(req.getParameter("gx1"));
                    params_glry1.add(req.getParameter("ywfc1"));
                    params_glry1.add(dqry);
                    params_glry1.add(dqsj);
                    params_glry1.add(req.getParameter("glryid1"));
                    updateByPreparedStatement(sql_glry, params_glry1);
                }

                if (!isEmpty(glryid2)) {
                    List params_glry2 = new ArrayList();
                    params_glry2.add(req.getParameter("glryxm2"));
                    params_glry2.add(req.getParameter("lxfs2"));
                    params_glry2.add(req.getParameter("gx2"));
                    params_glry2.add(req.getParameter("ywfc2"));
                    params_glry2.add(dqry);
                    params_glry2.add(dqsj);
                    params_glry2.add(req.getParameter("glryid2"));
                    updateByPreparedStatement(sql_glry, params_glry2);
                }
                if (!isEmpty(glryid3)) {
                    List params_glry3 = new ArrayList();
                    params_glry3.add(req.getParameter("glryxm3"));
                    params_glry3.add(req.getParameter("lxfs3"));
                    params_glry3.add(req.getParameter("gx3"));
                    params_glry3.add(req.getParameter("ywfc3"));
                    params_glry3.add(dqry);
                    params_glry3.add(dqsj);
                    params_glry3.add(req.getParameter("glryid3"));
                    updateByPreparedStatement(sql_glry, params_glry3);
                }
                if (!isEmpty(glryid4)) {
                    List params_glry4 = new ArrayList();
                    params_glry4.add(req.getParameter("glryxm4"));
                    params_glry4.add(req.getParameter("lxfs4"));
                    params_glry4.add(req.getParameter("gx4"));
                    params_glry4.add(req.getParameter("ywfc4"));
                    params_glry4.add(dqry);
                    params_glry4.add(dqsj);
                    params_glry4.add(req.getParameter("glryid4"));
                    updateByPreparedStatement(sql_glry, params_glry4);
                }

                params_glyw.add(req.getParameter("zj"));
                String sql_glyw = "update t_khglyw set ywqy=?,sxed=?,ywnl=?,khzt=?,glgs=?,wlxx=?,khms=?,gxr=?,gxsj=? where glkhid = ?";
                updateByPreparedStatement(sql_glyw, params_glyw);


            } else if ("xx_add_khgl".equals(method)) {
                String khzj = JdbcKhxx.getUUID();
                params_jcxx.add(khzj);
                String sql_jcxx = "insert into t_khjcxx (xm,xb,csrq,jg,sjh,wx,qq,zz,txr,txsj,id) values(?,?,?,?,?,?,?,?,?,?,?)";
                updateByPreparedStatement(sql_jcxx, params_jcxx);

                String sql_glry = "insert into t_khglry (xm,lxfs,gx,ywfc,txr,txsj,glkhid) values(?,?,?,?,?,?,?)";
                String glryxm1 = req.getParameter("glryxm1");
                String glryxm2 = req.getParameter("glryxm2");
                String glryxm3 = req.getParameter("glryxm3");
                String glryxm4 = req.getParameter("glryxm4");

                if (!isEmpty(glryxm1)) {
                    List params_glry = new ArrayList();
                    params_glry.add(glryxm1);
                    params_glry.add(req.getParameter("lxfs1"));
                    params_glry.add(req.getParameter("gx1"));
                    params_glry.add(req.getParameter("ywfc1"));
                    params_glry.add(dqry);
                    params_glry.add(dqsj);
                    params_glry.add(khzj);
                    updateByPreparedStatement(sql_glry, params_glry);
                }

                if (!isEmpty(glryxm2)) {
                    List params_glry = new ArrayList();
                    params_glry.add(glryxm2);
                    params_glry.add(req.getParameter("lxfs2"));
                    params_glry.add(req.getParameter("gx2"));
                    params_glry.add(req.getParameter("ywfc2"));
                    params_glry.add(dqry);
                    params_glry.add(dqsj);
                    params_glry.add(khzj);
                    updateByPreparedStatement(sql_glry, params_glry);
                }
                if (!isEmpty(glryxm3)) {
                    List params_glry = new ArrayList();
                    params_glry.add(glryxm2);
                    params_glry.add(req.getParameter("lxfs3"));
                    params_glry.add(req.getParameter("gx3"));
                    params_glry.add(req.getParameter("ywfc3"));
                    params_glry.add(dqry);
                    params_glry.add(dqsj);
                    params_glry.add(khzj);
                    updateByPreparedStatement(sql_glry, params_glry);
                }
                if (!isEmpty(glryxm4)) {
                    List params_glry = new ArrayList();
                    params_glry.add(glryxm4);
                    params_glry.add(req.getParameter("lxfs4"));
                    params_glry.add(req.getParameter("gx4"));
                    params_glry.add(req.getParameter("ywfc4"));
                    params_glry.add(dqry);
                    params_glry.add(dqsj);
                    params_glry.add(khzj);
                    updateByPreparedStatement(sql_glry, params_glry);
                }

                params_glyw.add(khzj);
                String sql_glyw = "insert into  t_khglyw (ywqy,sxed,ywnl,khzt,glgs,wlxx,khms,txr,txsj,glkhid) values (?,?,?,?,?,?,?,?,?,?)";
                updateByPreparedStatement(sql_glyw, params_glyw);

            } else if ("xx_del_khgl".equals(method)) {
                List params = new ArrayList();
                params.add(req.getParameter("zj"));
                String sql_jcxx = "update t_khjcxx set zfbs = '1' where id = ? ";
                String sql_glry = "update t_khglry set zfbs = '1' where glkhid = ?";
                String sql_glyw = "update t_khglyw set zfbs = '1' where glkhid = ?";
                updateByPreparedStatement(sql_jcxx, params);
                updateByPreparedStatement(sql_glry, params);
                updateByPreparedStatement(sql_glyw, params);
            }


            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }


    public List getYdjkxx(HttpServletRequest req, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        if (con_oa == null || con_oa.isClosed()) {
            con_oa = pm.getConnection();
        }

        List lt = new ArrayList();
        String sql = "select dz,mc from t_ydjkxx t where t.off = '0'";
        Statement st = con_oa.createStatement();
        ResultSet rs = st.executeQuery(sql);
        Socket socket = new Socket();
        while (rs.next()) {
            Map m = new HashMap();
            String dz = rs.getString("dz");
            String mc = rs.getString("mc");
            System.out.println(dz);


            boolean status = InetAddress.getByName(dz).isReachable(200);


            if (status) {
                m.put(mc, "0");
            } else {
                m.put(mc, "1");
            }
            lt.add(m);
        }

        return lt;

    }


    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
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
                //System.out.println(index);
                //System.out.println(params.get(i));
                pstmt.setObject(index++, params.get(i));
            }
        }
        //System.out.println("____________");
        result = pstmt.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;
    }

    //查询通用方法1
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


    //查询通用方法2
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

    //判读是否为空
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "无".equals(str)) {
            return true;
        }
        return false;
    }
}
