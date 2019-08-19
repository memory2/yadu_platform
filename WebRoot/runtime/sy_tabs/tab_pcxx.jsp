
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
	        $('#btn_search_pcxx').bind('click', function(){    
	            cx();
	        }); 
	    });
	  
	  function cx(){
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  var sqr = $("#sqr").val();
		  var jbzgs = $("#jbzgs").val();
		  //alert(sqksrq+"--"+jbzgs);
		  
		  var url = 'servlet/OaAction?method=sy_pcxx&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&jbzgs='+jbzgs+'&sqr='+sqr;
          var gridID = "dg_pcxx";
          var gridBT = "派车信息";
		  getDataGird(url,gridID,gridBT);
		  

		  
	  }
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>


		         <div title="派车信息" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">派车日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
		    	     	  </td>
		    	     	  <td style="width:100px;">申请人</td>
		    	     	  <td><input id="sqr" class="easyui-textbox" name="sqr"  style="width:150px;"/></td>
		    	     	  <td style="width:100px;">所派车辆</td>
		    	     	  <td><input  id="jbzgs" class="easyui-textbox"  style="width:150px;"  name="jbzgs"></input> </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				<!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>--> 
				   
				<a id="btn_search_pcxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				
				<br/><br/>
				
				<table id="dg_pcxx" title="派车信息列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="false"  nowrap="false" 
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=80px;>主键ID</th>
						<th field="sqr"  width=100px;>申请人</th> 
						<th field="bm"   width=100px;>部门</th> 
						<th field="sj"   width=100px;>司机</th> 
						<th field="sqrq"   width=150px;>申请日期</th> 
						<th field="ycsy"   width=200px; >用车事由</th>
						<th field="spcl"   width=80px; >所派车辆</th> 
						<th field="xclx"   width=150px;>行车路线</th> 
						<th field="pcrq"   width=150px;>派车日期</th> 
						<th field="jhhcrq"   width=150px;>计划还车时间</th> 
						<th field="hcsj"   width=200px;>实际还车时间</th>
						<th field="hcqgls"   width=100px;>还车前公里数</th>
						<th field="hchgls"   width=100px;>还车后公里数</th> 
						<th field="cllx"   width=100px;>车辆类型</th>  
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 