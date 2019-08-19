
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
	        $('#btn_search').bind('click', function(){    
	            cx();
	        }); 
	    });
	  
	  function cx(){
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  var sqr = $("#sqr").val();
		  var jbzgs = $("#jbzgs").val();
		  //alert(sqksrq+"--"+jbzgs);
          var url = 'servlet/OaAction?method=sy_qjxx&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&jbzgs='+jbzgs+'&sqr='+sqr;
          var gridID = "dg";
          var gridBT = "请假信息";
		  getDataGird(url,gridID,gridBT);
 
	  }

	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>


		         <div title="请假信息" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">请假日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
		    	     	  </td>
		    	     	  <td style="width:100px;">申请人</td>
		    	     	  <td><input id="sqr" class="easyui-textbox" name="sqr"  style="width:150px;"/></td>
		    	     	  <td style="width:100px;">请假天数</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:1" style="width:150px;" id="jbzgs"></input> </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				
				<br/><br/>
				
				<table id="dg" title="请假信息列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"   singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=70px;>主键ID</th> 
						<th field="qjr"  width=100px;>请假人</th> 
						<th field="bm"   width=150px;>部门</th> 
						<th field="ksrq"   width=110px;>开始日期</th> 
						<th field="kssj"   width=120px;>开始上/下午</th> 
						<th field="jsrq"   width=110px;>结束日期</th> 
						<th field="jssj"   width=120px;>结束上/下午</th>
						<th field="jsts"   width=110px;>计算天数</th>
						<th field="qjts"   width=110px;>请假天数</th>  
						<th field="qjsy"   width=200px;>请假事由</th> 
						<th field="tbsj"   width=190px;>填报时间</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 