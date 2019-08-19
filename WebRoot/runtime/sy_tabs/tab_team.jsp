

<%@ page language="java" contentType="text/html; charset=gbk" 
pageEncoding="gbk"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=gbk"> 
<title>特殊考勤组管理</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<%
	   String deptid  = (String)session.getAttribute("bmid");
	   String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
	   if(deptid==null||"".equals(deptid)){
		   response.sendRedirect(request.getContextPath()+"/runtime/login/login.jsp");
	   }%>
	<script type="text/javascript">
	  $(function(){
		    cx();
	        $('#btn_search_team').bind('click', function(){    
	            cx();
	        }); 

	    });
	  
	  function cx(){
		  var tname = $("#tname").val();
		  var tmember = $("#tmember").val();
		  var gslb = $("#gslb").combobox('getValue');
		  //alert(sqksrq+"--"+jbzgs);
		  
		  var url = 'servlet/KqAction?method=sy_team&tname='+tname+'&tmember='+tmember+'&gslb='+gslb;
          var gridID = "dg_team";
          var gridBT = "特殊考勤组管理";
		  
		  
          var xm = '<%=xm%>';
          if(xm!='系统管理员'){
        	  getDataGird(url,gridID,gridBT);  
          }else{
              $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
              var updUrl= 'runtime/sy_tabs/tab_team_xx.jsp';
              var delUrl = 'servlet/KqAction?method=xx_del_team';
    		  getDataGird1(url,gridID,gridBT,updUrl,delUrl,'900','300');  
          }
	  }
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>


		         <div title="特殊考勤组管理" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">组名</td>
		    	     	   <td><input id="tname" class="easyui-textbox" name="tname"  style="width:200px;"/> </td>
		    	     	  <td style="width:100px;">组成员</td>
		    	     	  <td><input id="tmember" class="easyui-textbox" name="tmember"  style="width:200px;"/></td>
		    	     	  <td style="width:100px;">公司类别</td>
		    	     	  <td>
		    	     	   <input id="gslb" name="gslb"  class="easyui-combobox" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'亚都实业'},{'id':'61','text':'威浦仕(行政)'},{'id':'71','text':'威浦仕(车间)'},{'id':'51','text':'迈迪克'}]"  style="width:100px;"/>
		    	     	  </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search_team" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				<br/><br/>
				
				<table id="dg_team" title="特殊考勤组" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"  
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=60px;  >主键ID</th>
 						<th field="tname"   width=70px;>组名</th> 
						<th field="tmember"  width=300px;>组成员</th> 
						<th field="gslb"   width=50px;>公司类别</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 