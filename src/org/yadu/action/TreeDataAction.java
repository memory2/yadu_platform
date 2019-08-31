package org.yadu.action;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import org.yadu.jdbc.JdbcFit;
import org.yadu.module.Msg;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class TreeDataAction extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private JdbcFit kh;

    //初始化方法
    public void init() throws ServletException {
        kh = new JdbcFit();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        if ("queryFitData".equals(method)) {
            try {
                queryFitData1(request, response, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("delFitData".equals(method)) {
            try {
                delFitData(request, response, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("insertAndUpdateFitData".equals(method)) {
            try {
                insertAndUpdateFitData(request, response, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void insertAndUpdateFitData(HttpServletRequest req, HttpServletResponse res, String method) throws Exception {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        Msg msg = kh.insertAndUpdateFitData(req, res, method);
        PrintWriter out = res.getWriter();
        out.print(JSON.toJSONString(msg));
        out.close();
    }

    private void queryFitData1(HttpServletRequest req, HttpServletResponse res, String method) throws Exception {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        List<Map<String, Object>> list = kh.queryFitData(req, method);
        JSONArray jsonArray = JSONArray.fromObject(list);
        PrintWriter out = res.getWriter();
        out.print(jsonArray.toString());
    }


    private void delFitData(HttpServletRequest req, HttpServletResponse res, String method) throws Exception {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        Msg msg = kh.delPower(req, method);
        PrintWriter out = res.getWriter();
        out.print(JSON.toJSONString(msg));
        out.close();
    }

}
