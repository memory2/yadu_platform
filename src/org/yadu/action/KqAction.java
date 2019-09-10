package org.yadu.action;

import net.sf.json.JSONArray;
import org.codehaus.jackson.map.ObjectMapper;
import org.yadu.dao.Node;
import org.yadu.jdbc.JdbcKqxx;
import org.yadu.untill.CalculationKq;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class KqAction extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JdbcKqxx kq;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("gb2312");
        resp.setCharacterEncoding("gb2312");

        String method = req.getParameter("method");
        HttpSession session = req.getSession(true);
        if ("sy".equals(method.substring(0, 2))) {
            try {
                cxlb(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (method.startsWith("kqxx")) {
            try {
                ydkq(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (method.startsWith("bm")) {
            try {
                bmlb(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("dl".equals(method)) {
            try {
                login(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("zd".equals(method)) {
            try {
                zdlb(req, resp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("cd".equals(method)) {
            try {
                getMenu(req, resp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if ("cx".equals(method.substring(0, 2))) {
            try {
                cxxq(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("xx".equals(method.substring(0, 2))) {
            try {
                upd(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("u8".equals(method.substring(0, 2))) {
            try {
                fhjsl(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            //resp.sendRedirect(req.getContextPath()+"/runtime/login/login.jsp");
            session.setAttribute("message", "tc");
            //registerDao.loginOut();
        }
    }

    //获取登陆信息
    private void login(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        String gh = new String(req.getParameter("gh").getBytes("ISO-8859-1"), "gb2312");
        String pwd = req.getParameter("pwd");
        HttpSession session = req.getSession();
        if (gh != null && !"".equals(gh.trim())) {
            session.setAttribute("gh", gh);
            List list = new ArrayList();
            list.add(gh);
            list.add(pwd);
            List userList = kq.loginUser(req);
            if (userList != null && userList.size() > 0) {
                session.setAttribute("message", "ok");
                //System.out.println(userList);
                Map map = (Map) userList.get(0);
                session.setAttribute("oaryid", map.get("oaryid").toString());
                session.setAttribute("oaryxm", map.get("oaryxm"));
                session.setAttribute("oarygh", map.get("oarygh"));
                session.setAttribute("bmmc", map.get("bmmc"));
                session.setAttribute("bmid", map.get("bmid").toString());
                String sfzh = map.get("sfzh").toString();
                session.setAttribute("bmdm", map.get("bmdm"));
                session.setAttribute("sfzh", sfzh);
                session.setAttribute("zwjb", map.get("zwjb").toString());
                Date day = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(df.format(day) + "/" + map.get("oaryxm"));
                //resp.sendRedirect(req.getContextPath()+"/runtime/sy_tabs/tab_kqxx2.jsp");
                resp.sendRedirect(req.getContextPath() + "/runtime/index.jsp");
            } else {
                session.setAttribute("message", "fail");
                resp.sendRedirect(req.getContextPath() + "/runtime/login/fail.jsp");
            }
        }
    }

    //亚都集团考勤
    private void ydkq(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        CalculationKq ck=new CalculationKq();
        List<Map<String, Object>> list = null;
        List<Map<String, Object>> list1 = null;
        //亚都--行政人员考勤--电脑端
        if ("kqxx_yd_pc".equals(method)) {
            list = kq.ydKqxx_pc(req, method);
        } else if ("kqxx_yd_wx".equals(method)) {
            //亚都--行政人员考勤--微信端
            list = kq.ydKqxx_wx(req, method);
        } else if ("kqxx_wps_pc".equals(method)) {
            //威浦仕--行政人员考勤--电脑端
            list = kq.wpsKqxx_pc(req, method);
        } else if ("kqxx_wps_wx".equals(method)) {
            //威浦仕--行政人员考勤--微信端
            list = kq.wpsKqxx_wx(req, method);
        } else if ("kqxx_wpscj_pc".equals(method)) {
            //威浦仕--车间人员考勤--电脑端
            list = kq.wpsCjKqxx_pc(req, method);
        } else if ("kqxx_wpscj_wx".equals(method)) {
            //威浦仕--车间人员考勤-微信端
            list = kq.wpsCjKqxx_wx(req, method);
        } else if ("kqxx_mdk_pc".equals(method)) {
            //迈迪科--行政人员考勤--电脑端
            list = kq.mdkKqxx_pc(req, method);
        } else if ("kqxx_mdk_wx".equals(method)) {
            //迈迪科--行政人员考勤--微信端
            list = kq.mdkKqxx_wx(req, method);
        }
        JSONArray jsonArray = null;
        jsonArray = JSONArray.fromObject(list);
       /* try {
            list1=ck.calKq(list,req);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        PrintWriter out = resp.getWriter();
        //System.out.println("*111***************"+jsonArray.toString());
        out.print(jsonArray.toString());
    }

    //获取部门字典下拉列表
    private void bmlb(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        Node node = null;
        if ("bm_wpscj".equals(method)) {
            node = kq.getCjDept(req);
        } else {
            node = kq.getDept(req);
        }

        String treeData = "[" + node.toString() + "]";
        PrintWriter out = resp.getWriter();
        //System.out.println(treeData);
        out.print(treeData);
    }

    //获取非部门字典下拉列表
    private void zdlb(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");

        HttpSession session = req.getSession();
        Map mymap = new HashMap();

        String oaryid = (String) session.getAttribute("oaryid");
        mymap.put("oaryid", oaryid);
        String zdmc = new String(req.getParameter("zdmc").getBytes("ISO-8859-1"), "gb2312");
        String zdlb = req.getParameter("zdlb");
        mymap.put("zdmc", zdmc);
        mymap.put("zdlb", zdlb);

        List<Map<String, Object>> list = kq.queryCode(mymap);
        JSONArray jsonArray = JSONArray.fromObject(list);
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toString());
    }

    //获取index.jsp首页左侧菜单
    private void getMenu(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        HttpSession session = req.getSession();
        String userid = (String) session.getAttribute("oaryid");
        List<Map<String, Object>> list = kq.getMenu(userid);
        JSONArray jsonArray = JSONArray.fromObject(list);
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toString());
    }

    //获取点击【修改】按钮角色、权限、通讯录、特殊考勤、特殊考勤组详情
    private void cxxq(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        resp.setCharacterEncoding("utf-8");
        String zj = req.getParameter("zj");
        Map map = kq.getDetailed(zj, method);
        JSONArray jsonArray = JSONArray.fromObject(map);
        PrintWriter out = resp.getWriter();
        //System.out.println(jsonArray.toString());
        out.print(jsonArray.toString());
    }

    //增删改通讯录、角色、权限、特殊考勤
    private void upd(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        resp.setContentType("text/html; charset=utf-8");
        List list = new ArrayList();
        if (method.endsWith("jsgl")) {
            if (!"xx_del".equals(method.substring(0, 6))) {
                list.add(req.getParameter("fid"));
                list.add(new String(req.getParameter("jsmc").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("fname").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(req.getParameter("tb"));
                list.add(new String(req.getParameter("ymdz").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("bz").getBytes("ISO-8859-1"), "UTF-8"));
            }

            if (!"xx_add".equals(method.substring(0, 6))) {
                list.add(req.getParameter("zj"));
            }
        } else if (method.endsWith("qxgl")) {
            if (!"xx_del".equals(method.substring(0, 6))) {
                list.add(req.getParameter("xm"));
                list.add(req.getParameter("js"));
                list.add(req.getParameter("gslb"));
            }

            if (!"xx_add".equals(method.substring(0, 6))) {
                list.add(req.getParameter("zj"));
            }
        } else if (method.endsWith("ydtx")) {
            if (!"xx_del".equals(method.substring(0, 6))) {
                list.add(new String(req.getParameter("bm").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(req.getParameter("sjhm"));
                list.add(new String(req.getParameter("lxr").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("fjh").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(req.getParameter("gh"));
                list.add(req.getParameter("dh"));

            }

            if (!"xx_add".equals(method.substring(0, 6))) {
                list.add(req.getParameter("zj"));
            }
        } else if (method.endsWith("tskq")) {
            if (!"xx_del".equals(method.substring(0, 6))) {
                list.add(req.getParameter("rq"));
                list.add(new String(req.getParameter("swsb").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("swxb").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("xwsb").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("xwxb").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(req.getParameter("lb"));
                list.add(new String(req.getParameter("tname").getBytes("ISO-8859-1"), "UTF-8"));
            }

            if (!"xx_add".equals(method.substring(0, 6))) {
                list.add(req.getParameter("zj"));
            }
        } else if (method.endsWith("team")) {
            if (!"xx_del".equals(method.substring(0, 6))) {
                list.add(new String(req.getParameter("tname").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(new String(req.getParameter("tmember").getBytes("ISO-8859-1"), "UTF-8"));
                list.add(req.getParameter("gslb"));
            }

            if (!"xx_add".equals(method.substring(0, 6))) {
                list.add(req.getParameter("zj"));

            }
        }
        //System.out.println("^^^"+list);
        boolean flag = kq.updDetailed(list, method);
        PrintWriter out = resp.getWriter();
        if (flag) {
            out.print("ok");
        } else {
            out.print("error");
        }

    }

    //获取通讯录、角色、权限、特殊考勤、特殊考勤组列表
    private void cxlb(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        Map<String, Object> m = null;
        if ("sy_dkjl".equals(method)) {
            m = kq.getDkjl(req, method);
        } else {
            m = kq.getCxlb(req, method);
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("jbxx");
        JSONArray jsonArray = JSONArray.fromObject(list);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", m.get("count"));
        map.put("rows", jsonArray);
        //System.out.println(map.toString());
        ObjectMapper objMap = new ObjectMapper();
        objMap.writeValue(resp.getWriter(), map);
    }

    //发货及时率统计
    private void fhjsl(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        if (method.startsWith("u8tj")) {
            Map<String, Object> m = kq.getFhjls(req, method);
            JSONArray jsonArray = JSONArray.fromObject(m);
            PrintWriter out = resp.getWriter();
            //System.out.println("********"+jsonArray.toString());
            out.print(jsonArray.toString());
        } else {
            Map<String, Object> m = kq.getFhjls(req, method);
            List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("jbxx");
            //System.out.println("++++++"+list);
            JSONArray jsonArray = JSONArray.fromObject(list);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("total", m.get("count"));
            map.put("rows", jsonArray);
            //System.out.println(map.toString());
            ObjectMapper objMap = new ObjectMapper();
            objMap.writeValue(resp.getWriter(), map);
        }


    }

    //初始化方法
    public void init() throws ServletException {
        kq = new JdbcKqxx();
    }
}
