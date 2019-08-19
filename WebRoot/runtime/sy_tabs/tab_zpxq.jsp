
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
	<script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript">
	  $(function(){
		    setDefaultDate('sqksrq','sqjsrq');
		    cx();
	        $('#btn_search_zpxq').bind('click', function(){    
	            cx();
	        });
	    });
	  
	  function cx(){
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  var sqr = $("#sqr").val();
		  var qsrs = $("#qsrs").val();
		  var jsrs = $("#jsrs").val();
		  //alert(sqksrq+"--"+jbzgs);
          var url = 'servlet/OaAction?method=sy_zpxq&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&qsrs='+qsrs+'&jsrs='+jsrs+'&sqr='+sqr;
          var gridID = "dg_zpxq";
          var gridBT = "招聘需求信息";
		  getDataGird(url,gridID,gridBT);
 
	  }
	</script>
 	<style>
	 	#cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
	</style>
</head> 
<body>

		         <div title="招聘需求信息" style="padding:10px;" id="fy">		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">填写日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:115px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:115px;"></input>
		    	     	  </td>
		    	     	  <td style="width:100px;">申请人</td>
		    	     	  <td><input id="sqr" class="easyui-textbox" name="sqr"  style="width:115px;"/></td>
		    	     	  <td style="width:100px;">招聘人数</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0" style="width:115px;" id="qsrs"></input> 至
		    	     	  <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0" style="width:115px;" id="jsrs">
		    	     	   </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search_zpxq" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				<br/><br/>
				
				<table id="dg_zpxq" title="招聘需求信息列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="false"   singleSelect="true" nowrap="false"> 
					<thead> 
						<tr> 
						<th field="id"  width=50px;>主键ID</th> 
						<th field="sqr"  width=80px;>申请人</th> 
						<th field="bm"   width=100px;>部门</th> 
						<th field="sqrq"   width=100px;>填报日期</th> 
						<th field="zpgw"   width=100px;>招聘岗位</th> 
						<th field="nzprs"   width=80px;>拟招人数</th> 
						<th field="qwdgsj"   width=120px;>期望到岗时间</th>
						<th field="zplx"   width=120px;>招聘类型</th>  
						<th field="zpyy"   width=200px;>招聘原因</th> 
						<th field="gwzz"   width=300px;>岗位职责</th> 
						<th field="xl"   width=100px;>学历要求</th> 
						<th field="nl"   width=100px;>年龄要求(岁)</th> 
						<th field="xb"   width=80px;>性别要求</th> 
						<th field="zy"   width=100px;>专业要求</th>
						<th field="gzjy"   width=120px;>工作经验要求</th>  
						<th field="zysz"   width=150px;>专业素质要求</th> 
						<th field="dybz"   width=200px;>待遇标准</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 