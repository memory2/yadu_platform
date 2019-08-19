package org.yadu.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class JdbcU8xx {
	  private Connection con_u8;
	  
	  public List getRsData(HttpServletRequest req, String method)throws Exception{

		    String gslb = req.getParameter("gslb");
		    String kssj = req.getParameter("kssj");
		    String jssj = req.getParameter("jssj");
		    String ryzt = req.getParameter("ryzt");
		    
		    String whereSql = "";
		    List lt = new ArrayList();
		    Map map = null;
		    if(!isEmpty(kssj)){
		    	whereSql +=" and p.dEnterUnitDate >='"+kssj+"' ";
		    }
		    if(!isEmpty(jssj)){
		    	whereSql +=" and p.dEnterUnitDate <='"+jssj+"' ";
		    }
		    if(!isEmpty(ryzt)){
		    	whereSql +=" and p.rEmployState ='"+ryzt+"' ";
		    }
		    
		    
		    if("1".equals(gslb)){
			    if ((this.con_u8 == null) || (this.con_u8.isClosed())) {
			        this.con_u8 = new JdbcUtilsRy().getConnection("ydsy");
			     }
                whereSql+=" and p.cDept_num !='17' ";
		    }else if("2".equals(gslb)){
			    if ((this.con_u8 == null) || (this.con_u8.isClosed())) {
			        this.con_u8 = new JdbcUtilsRy().getConnection("wps");
			     }
			    whereSql+=" and p.cDept_num !='21' ";
		    }else if("3".equals(gslb)){
			    if ((this.con_u8 == null) || (this.con_u8.isClosed())) {
			        this.con_u8 = new JdbcUtilsRy().getConnection("mdk");
			    }
			    whereSql+=" and p.cDept_num not in ('02','03') ";
		    }
		    
		    if("rsnl".equals(method)){
		    	map = new HashMap();
		    	String sql = "select a.lb,a.age from ( " +
		    			"select 'lb1' as lb,count(1) as age from hr_hi_person p where dBirthDate is not null and FLOOR(datediff(day,dBirthDate,getdate())/365.25)<25 " +whereSql+
		    			" UNION ALL " +
		    			"select 'lb2' as lb, count(1) as age from hr_hi_person p where dBirthDate is not null and FLOOR(datediff(day,dBirthDate,getdate())/365.25) >=25 and FLOOR(datediff(day,dBirthDate,getdate())/365.25) <35 "  +whereSql+
		    			" UNION ALL  " +
		    			"select 'lb3' as lb, count(1) as age from hr_hi_person p where dBirthDate is not null and  FLOOR(datediff(day,dBirthDate,getdate())/365.25) >=35 and FLOOR(datediff(day,dBirthDate,getdate())/365.25) <45 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb4' as lb, count(1) as age from hr_hi_person p where dBirthDate is not null and  FLOOR(datediff(day,dBirthDate,getdate())/365.25) >=45 and FLOOR(datediff(day,dBirthDate,getdate())/365.25) <55 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb5' as lb, count(1) as age from hr_hi_person p where dBirthDate is not null and  FLOOR(datediff(day,dBirthDate,getdate())/365.25) >=55 and FLOOR(datediff(day,dBirthDate,getdate())/365.25) <65 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb6' as lb, count(1) as age from hr_hi_person p where dBirthDate is not null and  FLOOR(datediff(day,dBirthDate,getdate())/365.25) >=65   "+whereSql+
		    			" UNION ALL " +
		    			"select 'zs' as lb, count(1) as age from hr_hi_person p where  dBirthDate is not null "+whereSql+") a";
			    Statement st_u8ry = this.con_u8.createStatement();
			    ResultSet rs_u8ry = st_u8ry.executeQuery(sql);
			    while (rs_u8ry.next()) {
			    	String lb = rs_u8ry.getString("lb");
			    	String age = rs_u8ry.getString("age");
			    	map.put(lb, age);
			    }
			    rs_u8ry.close();
			    st_u8ry.close();
			    this.con_u8.close();
		    }else if("rsxl".equals(method)){
		    	map = new HashMap();
		    	String sql = "select a.lb,a.edu  from ( " +
		    			"select 'lb1' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('5','6')  " +whereSql+
		    			"UNION ALL " +
		    			"select 'lb2' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('3','4')   " +whereSql+
		    			"UNION ALL " +
		    			"select 'lb3' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('2','8')   " +whereSql+
		    			"UNION ALL " +
		    			"select 'lb4' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('1','7')   " +whereSql+
		    			"UNION ALL " +
		    			"select 'lb5' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('0','9')  " +whereSql+
		    			"UNION ALL " +
		    			"select 'zs' as lb,count(1) as edu from hr_hi_person p where rhealthStatus in ('0','1','2','3','4','5','6','7','8','9') " +whereSql+
		    			") a";
			    Statement st_u8ry = this.con_u8.createStatement();
			    ResultSet rs_u8ry = st_u8ry.executeQuery(sql);
			    while (rs_u8ry.next()) {
			    	String lb = rs_u8ry.getString("lb");
			    	String edu = rs_u8ry.getString("edu");
			    	map.put(lb, edu);
			    }
			    rs_u8ry.close();
			    st_u8ry.close();
			    this.con_u8.close();
		    }else if("rsgl".equals(method)){
		    	map = new HashMap();
		    	String sql = "select a.lb,a.gl from ( " +
		    			"select 'lb1' as lb,count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25)<1 " +whereSql+
		    			" UNION ALL " +
		    			"select 'lb2' as lb, count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) >=1 and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) <2 "  +whereSql+
		    			" UNION ALL  " +
		    			"select 'lb3' as lb, count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and  FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) >=2 and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) <3 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb4' as lb, count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and  FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) >=3 and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) <4 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb5' as lb, count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and  FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) >=4 and FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) <5 "  +whereSql+
		    			" UNION ALL " +
		    			"select 'lb6' as lb, count(1) as gl from hr_hi_person p where dEnterUnitDate is not null and  FLOOR(datediff(day,dEnterUnitDate,getdate())/365.25) >=5   "+whereSql+
		    			" UNION ALL " +
		    			"select 'zs' as lb, count(1) as gl from hr_hi_person p where  dEnterUnitDate is not null "+whereSql+") a";
			    Statement st_u8ry = this.con_u8.createStatement();
			    ResultSet rs_u8ry = st_u8ry.executeQuery(sql);
			    while (rs_u8ry.next()) {
			    	String lb = rs_u8ry.getString("lb");
			    	String gl = rs_u8ry.getString("gl");
			    	map.put(lb, gl);
			    }
			    rs_u8ry.close();
			    st_u8ry.close();
			    this.con_u8.close();
		    }
	    	
		    lt.add(map);
		    
		  return lt;
	  }
	  
	  //ÅÐ¶ÁÊÇ·ñÎª¿Õ
	  private boolean isEmpty(String str)
	  {
	    if (str == null || "".equals(str) || str == "undefined"||"ÎÞ".equals(str)) {
	      return true;
	    }
	    return false;
	  }
}
