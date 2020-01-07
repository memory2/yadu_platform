package org.yadu.jdbc;


import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtilsRy {
    //���ݿ��û���
    private static final String USERNAME = "sa";
    //���ݿ�����
    private static final String PASSWORD = "1qaz2WSX";
    //������Ϣ
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //���ݿ��ַ
    private static final String YDSY_URL = "jdbc:sqlserver://10.0.10.2:1433;DatabaseName=UFDATA_005_2016";
    private static final String WPS_URL = "jdbc:sqlserver://10.0.10.2:1433;DatabaseName=UFDATA_009_2017";
    private static final String MDK_URL = "jdbc:sqlserver://10.0.10.2:1433;DatabaseName=UFDATA_010_2017";
    private Connection connection;

    public JdbcUtilsRy() {
        // TODO Auto-generated constructor stub
        try {
            Class.forName(DRIVER);
            //System.out.println("���ݿ����ӳɹ���");
        } catch (Exception e) {

        }
    }

    /**
     * ������ݿ������
     *
     * @return
     */
    public Connection getConnection(String bs) {
        try {

            if ("ydsy".equals(bs)) {
                connection = DriverManager.getConnection(YDSY_URL, USERNAME, PASSWORD);
            } else if ("wps".equals(bs)) {
                connection = DriverManager.getConnection(WPS_URL, USERNAME, PASSWORD);
            } else if ("mdk".equals(bs)) {
                connection = DriverManager.getConnection(MDK_URL, USERNAME, PASSWORD);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public Map getRyxx(HttpServletRequest req, String method) throws Exception {

        if (connection == null || connection.isClosed()) {
            connection = getConnection("ydsy");
        }

        String pageNum = req.getParameter("page");

        int pageNo = 0;
        if (pageNum != null && !pageNum.trim().equals("")) {
            pageNo = Integer.parseInt(pageNum);
        }
        //ÿҳ��ʾ��¼��
        String pageSize = req.getParameter("rows");
        int size = 0;
        if (pageSize != null && !pageSize.trim().equals("")) {
            size = Integer.parseInt(pageSize);
        }

        String xm_temp = new String(req.getParameter("xm").getBytes("ISO-8859-1"), "gb2312");
        String gh_temp = req.getParameter("gh");
        String sqksrq = req.getParameter("sqksrq");
        String sqjsrq = req.getParameter("sqjsrq");
        String ryzt = req.getParameter("ryzt");
        String hyzk = req.getParameter("hyzk");
        String xb = req.getParameter("xb");

        String whereSql = "";
        if (!isEmpty(gh_temp)) {
            whereSql += " and p.JobNumber like '%" + gh_temp + "%' ";
        }

        if (!isEmpty(xm_temp)) {
            whereSql += " and p.cpsn_name like '%" + xm_temp + "%' ";
        }
        if (!isEmpty(ryzt)) {
            whereSql += " and p.rEmployState = '" + ryzt + "' ";
        }

        if (!isEmpty(xb)) {
            whereSql += " and p.rSex = '" + xb + "' ";
        }

        if (!isEmpty(hyzk)) {
            whereSql += " and p.rMarriStatus = '" + hyzk + "' ";
        }

        if (!isEmpty(sqksrq)) {
            whereSql += " and p.dEnterDate >= '" + sqksrq + "'";
        }

        if (!isEmpty(sqjsrq)) {
            whereSql += " and p.dEnterDate <= '" + sqjsrq + "'";
        }

        int m = (pageNo - 1) * size;
        int n = m + size;

        int ks = n - m;
        int js = m;

        String sql = "select top " + ks + " cpsn_name as xm,vidno as sfzh,dEnterDate as rzrq,cPsnMobilePhone as sjhm,cPsnFAddr as xzz,case when rSex = '1' then '��' else 'Ů' end as xb,case when rMarriStatus = '1' then 'δ��' else '�ѻ�' end as hyzk," +
                "JobNumber as gh,case when rEmployState = '10' then '��ְ' else '��ְ' end as ryzt,d.cDepName as bm,DATEDIFF(DAY,dEnterDate,GETDATE())/365 as gl from hr_hi_person p,Department d " +
                " WHERE (JobNumber  NOT IN (SELECT TOP " + js + " JobNumber FROM hr_hi_person p,Department d where p.cDept_num = d.cDepCode  " + whereSql + ")) and p.cDept_num = d.cDepCode " +
                whereSql;
        String sqlCount = "select JobNumber from  hr_hi_person p,Department d where p.cDept_num = d.cDepCode " + whereSql;
        //System.out.println(sql);
        Map map = findModeResult(sql, sqlCount, null, method);
        return map;
    }


    public Map getRyxxRsxt(HttpServletRequest req, String method) throws Exception {

        if (connection == null || connection.isClosed()) {
            connection = getConnection("ydsy");
        }

        String pageNum = req.getParameter("page");

        int pageNo = 0;
        if (pageNum != null && !pageNum.trim().equals("")) {
            pageNo = Integer.parseInt(pageNum);
        }
        //ÿҳ��ʾ��¼��
        String pageSize = req.getParameter("rows");
        int size = 0;
        if (pageSize != null && !pageSize.trim().equals("")) {
            size = Integer.parseInt(pageSize);
        }

        String xm_temp = new String(req.getParameter("xm").getBytes("ISO-8859-1"), "gb2312");
        String gh_temp = req.getParameter("gh");
        String sqksrq = req.getParameter("sqksrq");
        String sqjsrq = req.getParameter("sqjsrq");

        String whereSql = "";
        if (!isEmpty(gh_temp)) {
            whereSql += " and ��� like '%" + gh_temp + "%' ";
        }

        if (!isEmpty(xm_temp)) {
            whereSql += " and ���� like '%" + xm_temp + "%' ";
        }


        if (!isEmpty(sqksrq)) {
            whereSql += " and ��ְʱ�� >= '" + sqksrq + "'";
        }

        if (!isEmpty(sqjsrq)) {
            whereSql += " and ��ְʱ�� <= '" + sqjsrq + "'";
        }

        int m = (pageNo - 1) * size;
        int n = m + size;

        int ks = n - m;
        int js = m;

        String sql = "select top " + ks + " ��� as gh,���� as bm,���� as xm,���֤�� as sfzh,�ֻ����� as sjhm,CONVERT(varchar(10), ��ְʱ��, 120) as rzsj,ְλ as zw,DATEDIFF(DAY,��ְʱ��,GETDATE())/365 as gl from ���µ���   WHERE (���  NOT IN (SELECT TOP " + js + " ��� FROM ���µ���  where 1=1 " + whereSql + ")) " +
                whereSql;
        String sqlCount = "select ���  from ���µ���  where 1=1 " + whereSql;
        //System.out.println(sql);
        Map map = findModeResult(sql, sqlCount, null, method);
        return map;
    }

    private boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str == "undefined" || "undefined".equals(str)) {
            return true;
        }
        return false;
    }


    /**1
     * ��ѯOA��
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findModeResult(String sql, String sqlCount, List<Object> params, String method) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        int count = 0;
        Map m = new HashMap();
        if (connection == null || connection.isClosed()) {
            connection = getConnection("ydsy");
        }
        PreparedStatement pstmt = connection.prepareStatement(sqlCount);

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
        list = findModeResult1(sql, params, method);
        m.put("jbxx", list);
        m.put("count", count);
        if (connection != null && !connection.isClosed()) {
            resultSet.close();
            pstmt.close();
        }

        return m;
    }

    /**
     * ��ѯOA��1
     */
    public List<Map<String, Object>> findModeResult1(String sql, List<Object> params, String method) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        if (connection == null || connection.isClosed()) {
            connection = getConnection("ydsy");
        }
        PreparedStatement pstmt = connection.prepareStatement(sql);
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
        if (connection != null && !connection.isClosed()) {
            resultSet.close();
            pstmt.close();
        }
        return list;

    }


}
