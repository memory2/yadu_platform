package org.yadu.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class JdbcUtilsU8 {
	//���ݿ��û���
	private static final String USERNAME = "sa";
	//���ݿ�����
	private static final String PASSWORD = "1qaz2WSX";
	//������Ϣ 
	private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//���ݿ��ַ
	private static final String URL = "jdbc:sqlserver://168.192.20.101:1433;DatabaseName=UFDATA_002_2014";
	private Connection connection;
	public JdbcUtilsU8() {
		// TODO Auto-generated constructor stub
		try{
			Class.forName(DRIVER);
			//System.out.println("���ݿ����ӳɹ���");
		}catch(Exception e){

		}
	}
	
	/**
	 * ������ݿ������
	 * @return
	 */
	public Connection getConnection(){
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	
	private boolean isEmpty(String str){
		if(str==null||"".equals(str)||str=="undefined"||"undefined".equals(str)){
			return true;
		}
		return false;
	}


	/**��ѯOA��
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> findModeResult(String sql,String sqlCount, List<Object> params,String method) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		int count = 0;
		Map m = new HashMap();
		if(connection==null||connection.isClosed()){
			connection = getConnection();
		}
		PreparedStatement pstmt = connection.prepareStatement(sqlCount);
		
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
		list = findModeResult1(sql,params,method);
		m.put("jbxx", list);
		m.put("count", count);
		if(connection!=null && !connection.isClosed()){
			resultSet.close();
			pstmt.close();
		}

		return m;
	}
	
	/**
	 * ��ѯOA��1
	 */
	public List<Map<String, Object>> findModeResult1(String sql, List<Object> params,String method) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		if(connection==null||connection.isClosed()){
			connection = getConnection();
		}
		PreparedStatement pstmt = connection.prepareStatement(sql);
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
		if(connection!=null && !connection.isClosed()){
			resultSet.close();
			pstmt.close();
		}
		return list;
		
	}

	public Map<String, Object> getU8Qqfw(HttpServletRequest req,String method)throws Exception{
		if(connection==null||connection.isClosed()){
			connection = getConnection();
		}
		Map map = null;
		HttpSession session = req.getSession();		
		String userid = (String) session.getAttribute("userid");
		String dept = (String) session.getAttribute("name");
		String pageNum = req.getParameter("page");

		int pageNo = 0;
		if(pageNum != null && !pageNum.trim().equals("")){
			pageNo =Integer.parseInt(pageNum);
		}
		//ÿҳ��ʾ��¼��
		String pageSize = req.getParameter("rows");
		int size = 0;
		if(pageSize != null && !pageSize.trim().equals("")){ 
			size =Integer.parseInt(pageSize);
		}
		
		int m = (pageNo-1)*size;
		int n = m+size;
		
		int ks = n-m;
		int js = m;
		
		String sqksrq = req.getParameter("sqksrq");
		String sqjsrq = req.getParameter("sqjsrq");
		String cxbs = req.getParameter("cxbs");
		String whereSql = "  ";
		if(!isEmpty(sqksrq)){
			whereSql+=" and �Ƶ����� >= '"+sqksrq+"'";
		}
		
		if(!isEmpty(sqjsrq)){
			whereSql+=" and �Ƶ����� <= '"+sqjsrq+"'";
		}
		
		if("jsl".equals(cxbs)){
			String sql_wfh = "select count(1) as wfhzs from fhjsl where ���۳���ʱ�� is null"+whereSql;
			String sql_cfh = "select count(1) as cfhzs from fhjsl where ���۳���ʱ�� is not null  and �������� >2"+whereSql;
			String sql_zs = "select count(1) as zs from fhjsl where 1=1 "+whereSql;
			Statement st_wfh = connection.createStatement();
			Statement st_cfh = connection.createStatement();
			Statement st_zs = connection.createStatement();
			ResultSet rs_wfh = st_wfh.executeQuery(sql_wfh);
			ResultSet rs_cfh = st_cfh.executeQuery(sql_cfh);
			ResultSet rs_zs = st_zs.executeQuery(sql_zs);
			int wfh =0;
			int cfh = 0;
			int zs = 0;
			while(rs_wfh.next()){
				wfh = rs_wfh.getInt(1);
			}
			
			while(rs_cfh.next()){
				cfh = rs_cfh.getInt(1);
			}
			
			while(rs_zs.next()){
				zs = rs_zs.getInt(1);
			}
			map = new HashMap();
			map.put("wfh", wfh);
			map.put("cfh", cfh);
			map.put("zs", zs);
		}else{
			if("wfh".equals(cxbs)){
				whereSql+=" and ���۳���ʱ�� is null ";
			}else if("cfh".equals(cxbs)){
				whereSql+=" and ���۳���ʱ�� is not null  and �������� >2";
			}
			String sql = "select top "+ks+" ���� as ph, ��������� as xsthh, CONVERT(varchar(10), �Ƶ�����, 120) as zdrq,������� as chbm,������� as chmc, ���������� as fhdsl, �ͻ���� as khjc,  CONVERT(varchar(10), ���ۿ�Ʊʱ��, 120) as xskpsj,CONVERT(varchar(10), ���۳���ʱ��, 120) as xscksj," +
					" ���۳��ⵥ�� as xsckdh, �������� as fhts from fhjsl   WHERE (����  NOT IN (SELECT TOP "+js+" ���� FROM fhjsl  where 1=1 "+whereSql+"))"+whereSql;
			
			String sqlCount = "select ����  from fhjsl  where 1=1 "+whereSql;
			map = findModeResult(sql,sqlCount,null,method);
		}
		

		

		return map;
	}
	

	

}
