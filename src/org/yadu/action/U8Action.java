package org.yadu.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.yadu.jdbc.JdbcU8xx;

public class U8Action extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JdbcU8xx ju;

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
        if ("rs".equals(method.substring(0, 2))) {
            try {
                rsfx(req, resp, method);
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

    private void rsfx(HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        req.setCharacterEncoding("GBK");
        resp.setContentType("text/html; charset=GBK");
        List<Map<String, Object>> list = null;
        list = ju.getRsData(req, method);

        JSONArray jsonArray = JSONArray.fromObject(list);
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toString());
    }

    //初始化方法
    public void init() throws ServletException {
        ju = new JdbcU8xx();
    }
}
