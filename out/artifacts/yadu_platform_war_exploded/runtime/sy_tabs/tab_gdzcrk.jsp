
<%@ page language="java" contentType="text/html; charset=utf-8" 
pageEncoding="utf-8"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>人员信息管理</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript">
	  $(function(){
		    setDefaultDate('sqksrq','sqjsrq');
		    cx();
	        $('#btn_search_gdzcrk').bind('click', function(){    
	            cx();
	        }); 


	    });
	  
	  function cx(){
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  var sqr = $("#sqr").val();
		  var jbzgs = $("#jbzgs").val();
		  //alert(sqksrq+"--"+jbzgs);
          var url = 'servlet/OaAction?method=sy_gdzcrk&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&jbzgs='+jbzgs+'&sqr='+sqr;
          var gridID = "dg_gdzcrk";
          var gridBT = "固定资产入库";
		  getDataGird(url,gridID,gridBT);
 
	  }
	  

	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>


		         <div title="固定资产入库信息" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">申请日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
		    	     	  </td>
		    	     	  <td style="width:100px;">申请人</td>
		    	     	  <td><input id="sqr" class="easyui-textbox" name="sqr"  style="width:150px;"/></td>
		    	     	  <td style="width:100px;">资产名称</td>
		    	     	  <td><input class="easyui-textbox" name="jbzgs"  style="width:150px;" id="jbzgs"></input></td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search_gdzcrk" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				
				<br/><br/>
				
				<table id="dg_gdzcrk" title="固定资产信息列表" class="easyui-datagrid" style="width:100%;height:400px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"   singleSelect="true" nowrap="false"> 
					<thead> 
						<tr> 
						<th field="id"  width=80px; halign="left" align="left" >主键ID</th> 
						<th field="sqr"  width=100px;>申请人</th> 
						<th field="bm"   width=120px;>部门</th> 
						<th field="sqrq"   width=120px;>申请日期</th> 
						<th field="zcmc"   width=120px;>资产名称</th> 
						<th field="ggxh"   width=150px;>规格型号</th>
						<th field="gys"   width=260px;>供应商</th>  
						<th field="dj"   width=80px;>单价</th> 
						<th field="sl"   width=80px;>数量</th> 
						<th field="je"   width=80px;>金额</th> 
						<th field="cfdd"   width=200px;>存放地点</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 