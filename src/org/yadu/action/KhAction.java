package org.yadu.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.codehaus.jackson.map.ObjectMapper;
import org.yadu.jdbc.JdbcKhxx;
import org.yadu.jdbc.JdbcKqxx;

public class KhAction extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JdbcKhxx kh;

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
        System.out.println(method);
        if ("sy_khjcxxcx".equals(method)) {
            try {
                search(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (method.startsWith("xx_")) {
            try {
                upd(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("cx_khgl".equals(method)) {
            try {
                cxxq(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("ydjkxx".equals(method)) { //监控信息查询
            try {
                cxydjkxx(req, resp, method);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //获取客户基础信息列表
    private void search(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {

        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        Map<String, Object> m = kh.getCxlb(req, method);
        List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("jbxx");
        JSONArray jsonArray = JSONArray.fromObject(list);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", m.get("count"));
        map.put("rows", jsonArray);
        //System.out.println(map.toString());
        ObjectMapper objMap = new ObjectMapper();
        objMap.writeValue(resp.getWriter(), map);
    }


    //获取详情
    private void cxxq(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        resp.setCharacterEncoding("utf-8");
        String zj = req.getParameter("zj");
        Map map = kh.getDetailed(zj, method);
        JSONArray jsonArray = JSONArray.fromObject(map);
        PrintWriter out = resp.getWriter();
        //System.out.println(jsonArray.toString());
        out.print(jsonArray.toString());
    }


    //增删改客户基础信息
    private void upd(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        resp.setContentType("text/html; charset=utf-8");
        List list = new ArrayList();
        //System.out.println("^^^"+list);
        boolean flag = kh.updDetailed(req, method);
        PrintWriter out = resp.getWriter();
        if (flag) {
            out.print("ok");
        } else {
            out.print("error");
        }

    }


    //获取亚都监控信息
    private void cxydjkxx(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        List<Map<String, Object>> list = kh.getYdjkxx(req, method);

        JSONArray jsonArray = JSONArray.fromObject(list);
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toString());
    }

    //初始化方法
    public void init() throws ServletException {
        kh = new JdbcKhxx();
    }
}
