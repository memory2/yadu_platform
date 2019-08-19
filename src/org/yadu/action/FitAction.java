package org.yadu.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.codehaus.jackson.map.ObjectMapper;
import org.yadu.jdbc.JdbcFit;

/**
 * 配件管理
 * @author yadu
 *
 */
public class FitAction extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private JdbcFit kh;
	
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
		if("queryResult".equals(method)){
			try {
				selectResult(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("insertProduct".equals(method)){
			try {
				insertProduct(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("delProduct".equals(method)){
			try {
				delProduct(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("searchProduct".equals(method)){
			try{
				searchProduct(request,response,method);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if("checkInsertAndUpdateProduct".equals(method)){
			try {
				checkInsertAndUpdateProduct(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("insertAndUpdateProduct".equals(method)){
			try {
				insertAndUpdateProduct(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("findSearchProductById".equals(method)){
			try {
				findSearchProductById(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("uploadExcel".equals(method)){
			try {
				//List<String[]> userList = POIUtil.readExcel(excel);  
				uploadExcel(request,response,method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public void uploadExcel(HttpServletRequest req,
			HttpServletResponse res, String method){
		
	}
	
	
	private void findSearchProductById(HttpServletRequest req,
			HttpServletResponse res, String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=GBK");
		Map a = kh.findSearchProductById(req, method);
		JSONArray jsonArray = JSONArray.fromObject(a); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
		
	}

	private void checkInsertAndUpdateProduct(HttpServletRequest req, HttpServletResponse res,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=GBK");
		Map a = kh.checkInsertAndUpdateProduct(req, method);
		JSONArray jsonArray = JSONArray.fromObject(a); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
	}
	
	
	private void insertAndUpdateProduct(HttpServletRequest req, HttpServletResponse res,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=GBK");
		Map a = kh.insertAndUpdateProduct(req, method);
		JSONArray jsonArray = JSONArray.fromObject(a); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
	}
	
	
	private void searchProduct(HttpServletRequest req, HttpServletResponse res,String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		Map<String, Object> m = kh.searchProduct(req, method);
		List<Map<String, Object>>  list = (List<Map<String, Object>>) m.get("jbxx");
		JSONArray jsonArray = JSONArray.fromObject(list); 
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		map.put("total", m.get("count")); 
		map.put("rows", jsonArray); 
		ObjectMapper objMap = new ObjectMapper(); 
		objMap.writeValue(res.getWriter(), map); 
	}
	
	private void selectResult(HttpServletRequest req, HttpServletResponse res,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		List<Map<String, Object>> list = kh.getSelectResult(req, method);
		JSONArray jsonArray = JSONArray.fromObject(list); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
	}
	/**
	 * 插入配件信息
	 * @param req
	 * @param res
	 * @param method
	 * @throws Exception
	 */
	private void insertProduct(HttpServletRequest req,HttpServletResponse res,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		int a = kh.updateFit(req, method);
		JSONArray jsonArray = JSONArray.fromObject(a); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
	}
	
	private void delProduct(HttpServletRequest req,HttpServletResponse res,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		int a = kh.updateFit(req, method);
		JSONArray jsonArray = JSONArray.fromObject(a); 
		PrintWriter out = res.getWriter();
		out.print(jsonArray.toString());
	}
	
	//初始化方法
	public void init() throws ServletException {
		kh = new JdbcFit(); 
	}
}
