package org.yadu.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.yadu.module.Msg;
import org.yadu.module.ProductModule;

import Decoder.BASE64Decoder;

public class JdbcFit {
	private PoolManager pm = null;

	private Connection con_oa;
	//加载mysql驱动，OA数据库连接池
	public JdbcFit() {
		try {
			pm = new PoolManager();
		}catch (Exception localException){
			System.out.println("创建连接池对象异常！");
		}
	}

	//查询通用方法1
	public Map<String, Object> findModeResult(String sql, List<Object> params,String method,Connection con) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		int count = 0;
		String sqlCount = sql.substring(0,sql.length()-23);
		Map m = new HashMap();
		PreparedStatement pstmt = con.prepareStatement(sqlCount);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				if("sy_sbxx".equals(method)){
					pstmt.setObject(index++, "%"+params.get(i)+"%");
				}else{
					pstmt.setObject(index++, params.get(i));
				}
			}
		}
		ResultSet resultSet = pstmt.executeQuery();
		while(resultSet.next()){
			count++;
		}
		list = findModeResult1(sql,params,method,con);
		m.put("jbxx", list);
		m.put("count", count);
		if(con!=null && !con.isClosed()){
			resultSet.close();
			pstmt.close();
		}

		return m;
	}


	//查询通用方法1
	public Map<String, Object> findModeResult3(String sql, String sqlCount,List<Object> params,String method,Connection con) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int count = 0;
		Map<String, Object> map3=findModeResult2(sqlCount,params,null,con_oa);
		if(map3!=null){
			count=Integer.parseInt(String.valueOf(map3.get("count(1)")));
		}
		Map m = new HashMap();
		list = findModeResult1(sql,params,method,con);
		m.put("jbxx", list);
		m.put("count", count);
		return m;
	}




	public Map getFitName(String parm) throws SQLException{
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		List params = new ArrayList();
		params.add(parm);
		String sql = "select tf.id,tf.name from t_fit_data tf where tf.id="+parm+" and tf.status=1";
		Map<String, Object> m = findModeResult(sql, params,null,con_oa);
		return m;
	}

	/***
	 * 获取下拉结果集
	 * @param req
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSelectResult(HttpServletRequest req,String method)throws Exception{
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String sql = "";
		List params = new ArrayList();
		if("queryResult".equals(method)){
			String parentId = req.getParameter("parentId");
			sql = "select tf.id productid,tf.name proname from t_fit_data tf where tf.parentId="+parentId+" and tf.status=1";
		}
		List<Map<String, Object>> m = findModeResult1(sql, params,method,con_oa);
		return m;
	}

	public Map<String,Object> searchProduct(HttpServletRequest req,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");

		//请求参数
		  	/*String packAgeNo=req.getParameter("packAge");
		  	String cateCoryNo=req.getParameter("cateCory");
		  	String nameNo=req.getParameter("name");
		  	String sizeNo=req.getParameter("size");*/
		String pageNum = req.getParameter("page");
		String productNo=req.getParameter("productNo");


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

		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String sql = "";
		String sqlCount="";
		String whereSql="";
		List params = new ArrayList();
		if("searchProduct".equals(method)){
			//String parentId = req.getParameter("parentId");
			sql = " SELECT p.id,p.productNo,t.name packName,t2.name cateName,t3.name productName,t4.name sizeName,p.amount,p.remark FROM t_product p "
					+" left join t_fit_data t on p.packAge=t.id "
					+" left join  t_fit_data t2 on p.category=t2.id "
					+" left join  t_fit_data t3 on p.name=t3.id "
					+" left join  t_fit_data t4 on p.size=t4.id  where 1=1 ";
			sqlCount= " SELECT count(1) FROM t_product p "
					+" left join t_fit_data t on p.packAge=t.id "
					+" left join  t_fit_data t2 on p.category=t2.id "
					+" left join  t_fit_data t3 on p.name=t3.id "
					+" left join  t_fit_data t4 on p.size=t4.id  where 1=1 ";
		}
			/*if(StringUtils.isNotBlank(packAgeNo)){
				whereSql=" and t.id= '"+packAgeNo+"' ";
			} if(StringUtils.isNotBlank(cateCoryNo)){
				whereSql+=" and t2.id='"+cateCoryNo+"' ";
			} if(StringUtils.isNotBlank(nameNo)){
				whereSql+=" and t3.id='"+nameNo+"' ";
			} if(StringUtils.isNotBlank(sizeNo)){
				whereSql+=" and t4.id='"+sizeNo+"' ";
			}*/
		if(StringUtils.isNotBlank(productNo)){
			whereSql+=" and  p.productNo like  '%"+productNo+"%' ";
		}
		sql += whereSql+" limit "+(pageNo-1)*size+","+size;
		sqlCount+=whereSql;
		Map<String, Object> m = findModeResult3(sql,sqlCount, params,method,con_oa);
		return m;
	}

	public Map findSearchProductById(HttpServletRequest req, String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		//请求参数
		String packAgeNo=req.getParameter("packAge");
		String sql = " SELECT * FROM t_product p  where p.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(packAgeNo);
		Map<String, Object> map2=findModeResult2(sql,params,null,con_oa);
		return map2;
	}


	public List<Map<String,Object>> getSearchResult(HttpServletRequest req,String method) throws Exception{
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String sql = "";
		List params = new ArrayList();
		if("queryResult".equals(method)){
			String parentId = req.getParameter("parentId");
			sql = "select tf.id,tf.name from t_fit_data tf where tf.parentId="+parentId+" and tf.status=1";
		}
		List<Map<String, Object>> m = findModeResult1(sql, params,method,con_oa);
		return m;
	}

	public Map checkInsertAndUpdateProduct(HttpServletRequest req, String method) throws SQLException, UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");

		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String str="";
		Map<String,String> mapMsg=new HashMap<String,String>();
		try {

			String data= req.getParameter("inserted");
			String updateData=req.getParameter("updated");
			if(StringUtils.isNotEmpty(data)){
				JSONArray JSONArray= new JSONArray();
				JSONArray jsonObject = JSONArray.fromObject(data);
				@SuppressWarnings("unchecked")
				List<ProductModule> list = (List<ProductModule>)JSONArray.toList(jsonObject, ProductModule.class);
				StringBuffer sb=new StringBuffer();
				List<Object> params_jcxx = new ArrayList<Object>();
				if(list!=null&&list.size()>0){
					for (ProductModule productModule : list) {
						sb.append(productModule.getPackName()+productModule.getCateName()+productModule.getProductName()+productModule.getSizeName()+productModule.getAmount());
					}
					String strNo=sb.toString();
					strNo=strNo.replaceAll(" ", "").replaceAll("null", "");
					char[] arrayCh=strNo.toCharArray();
					Arrays.sort(arrayCh);
					String sortedStr=new String(arrayCh);  //加上这句
					//查询编号是否存在
					String sql="SELECT tp.productNo from t_packproduct tp where tp.packageNum=? ";
					params_jcxx.add(sortedStr);
					Map<String, Object> map=findModeResult2(sql,params_jcxx,null,con_oa);
					if(map!=null&&map.size()>0){
						String productNo=String.valueOf(map.get("productNo"));
						if(StringUtils.isNotBlank(productNo)){
							str="保存失败！该包和"+productNo+"存在重复，请修改后再提交！";
							mapMsg.put("msg", str);
							mapMsg.put("status","1");
							return mapMsg; //提醒该包和哪个包重复，并返回重复包的编号
						}
					}else{
						//List<Object> params = null;	
						str="保存成功！";
						mapMsg.put("msg", str);
						mapMsg.put("status","2");
						return mapMsg;
					}
				}
			}else if(StringUtils.isNotEmpty(updateData)){
				JSONArray JSONArray= new JSONArray();
				JSONArray jsonObject = JSONArray.fromObject(updateData);
				@SuppressWarnings("unchecked")
				List<ProductModule> updateList = (List<ProductModule>)JSONArray.toList(jsonObject, ProductModule.class);
				String sql = "update t_product set packAge=?,cateGory=?,name=?,size=?,amount=?,remark=? where id = ?";
				List<Object> params = new ArrayList<Object>();
				if(updateList!=null&&updateList.size()>0){
					for (ProductModule productModule : updateList) {
						params.add(productModule.getPackName());
						params.add(productModule.getCateName());
						params.add(productModule.getProductName());
						params.add(productModule.getSizeName());
						params.add(productModule.getAmount());
						params.add(productModule.getRemark());
						params.add(productModule.getId());
						updateByPreparedStatement(sql,params);
					}
				}

				List<Object> params1 = new ArrayList<Object>();
				List<Object> params2 = new ArrayList<Object>();
				sql = "SELECT * FROM t_product p where p.productNo=? ";
				params1.add(updateList.get(0).getProductNo());
				List<Map<String, Object>> m = findModeResult1(sql, params1,method,con_oa);
				StringBuffer sb=new StringBuffer();
				for (int i = 0; i < m.size(); i++) {
					sb.append(String.valueOf(m.get(i).get("packAge"))+String.valueOf(m.get(i).get("cateGory"))
							+String.valueOf(m.get(i).get("name"))+String.valueOf(m.get(i).get("size"))+String.valueOf(m.get(i).get("amount")));
					// String.valueOf(m.get("count(1)"))
				}
				String strNo=sb.toString();
				strNo=strNo.replaceAll(" ", "").replaceAll("null", "");
				char[] arrayCh=strNo.toCharArray();
				Arrays.sort(arrayCh);
				String sortedStr=new String(arrayCh);  //加上这句
				sql = "update t_packproduct set packAgeNum=? where productNo = ?";
				params2.add(sortedStr);
				params2.add(updateList.get(0).getProductNo());
				updateByPreparedStatement(sql,params2);
				str="更新成功！";
				mapMsg.put("msg", str);
				mapMsg.put("status","3");
				return mapMsg;
			}else{
				str="没有检测到可提交数据！";
				mapMsg.put("msg", str);
				mapMsg.put("status","3");
				return mapMsg;
			}
		} catch (Exception e) {
			str="程序异常！请联系管理员";
			mapMsg.put("msg", str);
			mapMsg.put("status","4");
			e.printStackTrace();
			return mapMsg;
		}
		return null;
	}
	public static String stringReplace(String obj) {
		//去掉" "号
		String str= obj.replace("\"", "");
		return str ;

	}

	public Map<String,String> insertAndUpdateProduct(HttpServletRequest req, String method) throws SQLException {
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String str="";
		String sql="";
		Map<String,String> mapMsg=new HashMap<String,String>();
		try {
			String productNo=req.getParameter("productNo");//用户输入的产品编号
			String data= req.getParameter("inserted"); //获取插入数据
			if(StringUtils.isNotEmpty(data)){
				JSONArray JSONArray= new JSONArray();
				JSONArray jsonObject = JSONArray.fromObject(data);
				@SuppressWarnings("unchecked")
				List<ProductModule> list = (List<ProductModule>)JSONArray.toList(jsonObject, ProductModule.class);
				StringBuffer sb=new StringBuffer();
				if(list!=null&&list.size()>0){
					List<Object> params = null;
					List<Object> params1 =new ArrayList<Object>();
					for (ProductModule productModule : list) {
						sb.append(productModule.getPackName()+productModule.getCateName()+productModule.getProductName()+productModule.getSizeName()+productModule.getAmount());
						params = new ArrayList<Object>();
						params.add(stringReplace(productNo));
						params.add(productModule.getPackName());
						params.add(productModule.getCateName());
						params.add(productModule.getProductName());
						params.add(productModule.getSizeName());
						params.add(productModule.getAmount());
						params.add(productModule.getRemark());
						sql = "insert into t_product (productNo,packAge,cateGory,name,size,amount,remark) values(?,?,?,?,?,?,?)";
						updateByPreparedStatement(sql,params);
					}
					String strNo=sb.toString();
					strNo=strNo.replaceAll(" ", "").replaceAll("null", "");
					char[] arrayCh=strNo.toCharArray();
					Arrays.sort(arrayCh);
					String sortedStr=new String(arrayCh);  //加上这句
					params1.add(sortedStr);
					params1.add(stringReplace(productNo));
					sql = "insert into t_packproduct (packageNum,productNo) values(?,?)";
					updateByPreparedStatement(sql,params1);
					str="保存成功！";
					mapMsg.put("msg", str);
					mapMsg.put("status","3");
					return mapMsg;
				}
			}else{
				str="保存失败！";
				mapMsg.put("msg", str);
				mapMsg.put("status","4");
				return mapMsg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param req
	 * @param method 调用方法
	 * @return 0 保存失败 1 保存成功  2 信息重复提交  3 删除成功
	 * @throws SQLException
	 */
	public int updateFit(HttpServletRequest req,String method) throws SQLException{
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		try {
			List<Object> params_jcxx = new ArrayList<Object>();
			String sql="";
			if("updateProduct".equals(method)){
				params_jcxx.add(req.getParameter("packAge"));
				params_jcxx.add(req.getParameter("cateCory"));
				params_jcxx.add(req.getParameter("name"));
				params_jcxx.add(req.getParameter("size"));
				params_jcxx.add(req.getParameter("id"));
				sql = "update t_product set packAge=?,cateGory=?,name=?,size=?,amount=?,remark=? where id = ?";
				updateByPreparedStatement(sql,params_jcxx);
				return 0;
			}else if("delProduct".equals(method)){
				String[] array = req.getParameterValues("array[]");
				List<String> stringB = Arrays.asList(array);
				//先查询一次该删除配件的编号，记录，删除后如果包不存在，全部删掉了，则应该讲t_packproduct记录信息也删除掉
				List<Object> params = null;
				String pro=null;
				if(stringB!=null&&stringB.size()>0){
					for (String str : stringB) {
						params = new ArrayList<Object>();
						params.add(str);
						sql="SELECT p.id,p.productNo  FROM t_product p where p.id=? ";
						Map<String, Object> map2=findModeResult2(sql,params,null,con_oa);
						if(map2!=null&&map2.size()>0){
							pro=String.valueOf(map2.get("productNo"));
						}
					}
				}

				sql ="DELETE FROM t_product WHERE id=?";
				batchUpdateAndInsert(sql,stringB);
				sql="SELECT count(1) FROM t_product p where p.id=? ";
				Map<String, Object> map3=findModeResult2(sql,params,null,con_oa);
				if(map3!=null){
					long count=Long.parseLong(String.valueOf(map3.get("count(1)")));
					if(count>0){
						return 3;
					}else{
						if(StringUtils.isNotEmpty(pro)){
							List<String> listP=new ArrayList<String>();
							listP.add(pro);
							sql ="DELETE FROM t_packproduct  where productNo=?";
							batchUpdateAndInsert(sql,listP);
						}

						return 3;
					}
				}
			}else if("insertProduct".equals(method)){
				params_jcxx.add(req.getParameter("packAge"));
				params_jcxx.add(req.getParameter("cateCory"));
				params_jcxx.add(req.getParameter("name"));
				params_jcxx.add(req.getParameter("size"));
				//查询数据库是否存在该信息
				sql="SELECT  count(1) FROM t_product p where p.packAge=? and cateGory=? and name=? and size=? ";
				Map<String, Object> map=findModeResult2(sql,params_jcxx,null,con_oa);
				if(map!=null){
					long count=Long.parseLong(String.valueOf(map.get("count(1)")));
					if(count>0){
						return 2;
					}else{
						params_jcxx.add(req.getParameter("amount"));
						params_jcxx.add(req.getParameter("remark"));
						sql = "insert into t_product (packAge,cateGory,name,size,amount,remark) values(?,?,?,?,?,?)";
						updateByPreparedStatement(sql,params_jcxx);
						return 1;
					}
				}else{
					params_jcxx.add(req.getParameter("amount"));
					params_jcxx.add(req.getParameter("remark"));
					sql = "insert into t_product (packAge,cateGory,name,size,amount,remark) values(?,?,?,?,?,?)";
					updateByPreparedStatement(sql,params_jcxx);
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//oa数据库增删改工具方法
	public boolean updateByPreparedStatement(String sql, List<Object> params)throws SQLException{
		boolean flag = false;
		int result = -1;
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		PreparedStatement pstmt = con_oa.prepareStatement(sql);
		int index = 1;
		if(params != null && !params.isEmpty()){
			for(int i=0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		result = pstmt.executeUpdate();
		flag = result > 0 ? true : false;
		return flag;
	}
	/***
	 * 批量删除
	 * @param sql
	 * @param stringB
	 * @return
	 * @throws SQLException
	 */
	public boolean batchUpdateAndInsert(String sql,List<String> stringB) throws SQLException{
		boolean flag=false;
		int[] result1 = null;
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		try {
			PreparedStatement pstmt = con_oa.prepareStatement(sql);
			for (int i = 0; i < stringB.size(); i++) {
				pstmt.setString(1, stringB.get(i));
				pstmt.addBatch();
			}
			result1=pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		flag = result1.length > 0 ? true : false;
		return flag;
	}



	public Map<String, Object> findModeResult2(String sql, List<Object> params,String method,Connection con) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		PreparedStatement pstmt = con.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				if("sy_sbxx".equals(method)){
					pstmt.setObject(index++, "%"+params.get(i)+"%");
				}else{
					pstmt.setObject(index++, params.get(i));
				}

			}
		}
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		Map<String, Object> map = new HashMap<String, Object>();
		while(resultSet.next()){
			for(int i=0; i<cols_len; i++){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
		}
		if(con!=null && !con.isClosed()){
			resultSet.close();
			pstmt.close();
		}

		return map;
	}

	//查询通用方法2
	public List<Map<String, Object>> findModeResult1(String sql, List<Object> params,String method,Connection con) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		PreparedStatement pstmt = con.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				if("sy_sbxx".equals(method)){
					pstmt.setObject(index++, "%"+params.get(i)+"%");
				}else{
					pstmt.setObject(index++, params.get(i));
				}
			}
		}
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while(resultSet.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0; i<cols_len; i++){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			list.add(map);
		}
		if(con!=null && !con.isClosed()){
			resultSet.close();
			pstmt.close();
		}
		return list;

	}

	public List<Map<String, Object>> queryFitData(HttpServletRequest req,
												  String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		List<Map<String, Object>> map=null;
		String	sql = "select id as id,name as text,parentId as pid from t_fit_data";
		if("queryFitData".equals(method)){
			List<Map<String, Object>> list = findModeResult1(sql, null,method,con_oa);
			List<Integer> p = null;
			map = createTree(list, p, "0", "text", "pid", "id");
		}


		return map;
	}


	// 递归生成树
	public  List<Map<String, Object>> createTree(List<Map<String, Object>> list, List<Integer> p,
												 String pidStart, String pname, String pid, String id) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (Map f : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			String sPid = f.get(pid).toString();// 父id
			Object fid = f.get(id);// id
			String text = f.get(pname).toString();
			map.put("id", fid);
			map.put("text", text);
			if (pidStart.toString().equals(sPid.toString().trim())) {
				List<Map<String, Object>> childMap = createTree(list, p, fid.toString(), pname,
						pid, id);
				if (childMap.size() > 0) {
					map.put("children", childMap);
				} else {
					if (p != null && p.contains(fid) && !"0".equals(sPid)) {
						map.put("checked", true);
					} else {
						map.put("checked", false);
					}
				}
				retList.add(map);
			}
		}
		return retList;
	}

	public Msg delPower(HttpServletRequest req, String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String bid = req.getParameter("id");
		String sql =" DELETE FROM t_fit_data WHERE id=?";
		Msg msg=new Msg();
		if(StringUtils.isNotEmpty(bid)){
			List<String> list=new ArrayList<>();
			list.add(bid);
			boolean r=batchUpdateAndInsert(sql,list);
			if(r){
				msg.setMsg("删除成功");
			}else{
				msg.setSuccess(false);
				msg.setMsg("删除失败");
			}
		}else{
			msg.setSuccess(false);
			msg.setMsg("删除失败");
		}
		return msg;
	}

	public Msg insertAndUpdateFitData(HttpServletRequest req,
									  HttpServletResponse res, String method) throws Exception {
		req.setCharacterEncoding("UTF-8");
		if(con_oa==null||con_oa.isClosed()){
			con_oa = pm.getConnection();
		}
		String id = req.getParameter("id");
		String parentId = req.getParameter("parentId");
		String name = req.getParameter("text");
		String sql="";
		boolean a = false;
		List<Object> params= new ArrayList<Object>();
		if(StringUtils.isEmpty(id)){
			sql="insert into t_fit_data (name, parentId,status) values (?,?,?)";
			params.add(name);
			params.add(parentId);
			params.add("1");
			a=updateByPreparedStatement(sql,params);
		}if(StringUtils.isNotEmpty(id)){
			sql="update t_fit_data set name=?,parentId=? where id=? ";
			params.add(name);
			params.add(parentId);
			params.add(id);
			a=updateByPreparedStatement(sql,params);
		}
		Msg msg=new Msg();
		if(a){
			msg.setMsg("操作成功");
		}
		else{
			msg.setSuccess(false);
			msg.setMsg("操作失败");
		}
		return msg;
	}


	/**
	 * base64 解密
	 * @param s
	 * @return
	 */
	public static String base64DeCode(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(base64DeCode("MDMxMDUw"));
	}

}
