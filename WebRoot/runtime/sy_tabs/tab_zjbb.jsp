

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
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/demo.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript">
	  $(function(){
		    setDefaultDate('sqksrq','sqjsrq');
		    cx();
	        $('#btn_search_zjbb').bind('click', function(){    
	            cx();
	        }); 
	    });
	  
	  function cx(){
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  var sqr = $("#sqr").val();
		  var jeqs = $("#jeqs").val();
		  var jejs = $("#jejs").val();
		  //alert(sqksrq+"--"+jbzgs);
          var url = 'servlet/OaAction?method=sy_zjbb&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&jeqs='+jeqs+'&jejs='+jejs+'&sqr='+sqr;
          var gridID = "dg_zjbb";
          var gridBT = "资金日报表信息";
		  getDataGird(url,gridID,gridBT);
 
	  }
	  

	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
	 #btn_search_zjbb{margin-left:22px;}
	 
	 </style>
</head> 
<body>


		         <div title="资金日报表信息" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">填报日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:95px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:95px;"></input>
		    	     	  </td>
		    	     	  <td style="width:100px;">昨日余额</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jeqs"></input> 至
		    	     	  <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jejs"></td>
		    	     	  <td style="width:100px;">今日收入</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jeqs"></input> 至
		    	     	  <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jejs">
		    	     	   </td>
		    	     	<tr>
		    	     	  <tr>
		    	     	  <td colspan=2 style="text-align:left;">
		    	     	  <a id="btn_search_zjbb" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		    	     	   </td>
		    	     	  <td style="width:100px;text-align:center">今日支出</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jeqs"></input> 至
		    	     	  <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jejs">
		    	     	  </td>
		    	     	  <td style="width:100px;">今日余额</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jeqs"></input> 至
		    	     	  <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2" style="width:95px;" id="jejs"></td>

		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> 
				<!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>--> 
				   
				
				
				<br/>
				
				<table id="dg_zjbb" title="资金日报表信息列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"   singleSelect="true" nowrap="false"> 
					<thead> 
						<tr> 
						<th field="id"  width=80px;>主键ID</th> 
						<th field="bxr"  width=300px;>昨日余额</th> 
						<th field="ccsy"   width=300px;>今日收入</th> 
						<th field="djys"   width=300px;>今日支出</th> 
						<th field="cwshyj"   width=300px;>今日余额</th>
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 