
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
	var bool = false;
	var j=10;
	  $(function(){
		    cx();
	        $('#btn_search').bind('click', function(){    
	            cx();
	        }); 
	        
	        sx();
	    });
	  
	  function cx(){
		  var gh = $("#gh").val();
		  var xm = $("#xm").val();
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue');
		  var ryzt = $("#ryzt").combobox('getValue');
		  var xb = $("#xb").combobox('getValue');
		  var hyzk = $("#hyzk").combobox('getValue');
          var url = 'servlet/OaAction?method=rsxt&gh='+gh+'&xm='+xm+'&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&xb='+xb+'&ryzt='+ryzt+'&hyzk='+hyzk;
          var gridID = "dg_ymzq";
          var gridBT = "在职人员信息";
		  getDataGird(url,gridID,gridBT);
 
	  }

	  function sx(){
		  if (bool == false) {
			    bool = true;
			    xh();
		  }
	  }

	  
	//刷新方法
	function xh(){
		
	  j = j - 1;
	  $('#span1').text("["+j+"]");
	  if (j == 0) {
	    j = 10;
	    cx();
	  }
	  if (document.getElementById("xzz").checked==true) {
	    setTimeout("xh()", 1000);
	  } else {
	    bool = false;
	  }
	}
	  
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
     #sx{color:blue;font-weight:bold;font-size:13px;}
	 #xzz{vertical-align:text-bottom;}
	 </style>
</head> 
<body>


		         <div title="人员信息" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">姓名</td>
		    	     	  <td><input id="xm" class="easyui-textbox" name="xm"  style="width:170px;"/></td>
		    	     	  <td style="width:100px;">工号</td>
		    	     	 <td><input id="gh" class="easyui-textbox" name="gh"  style="width:170px;"/></td>
		    	     	 	<td style="width:100px;text-align:center">入职日期</td>
		    	     	  <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:170px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:170px;"></input>
		    	     	  </td>
		    	     	<tr>
		    	     	<tr>
		    	     	  <td style="width:100px;">性别</td>
		    	     	  <td><input id="xb" name="xb"  class="easyui-combobox" data-options="multiple:'false', panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':1,'text':'男'},{'id':2,'text':'女'}]"  style="width:170px;"/></td>
		    	     	  <td style="width:100px;">婚姻状况</td>
		    	     	 <td><input id="hyzk" name="hyzk" class="easyui-combobox" data-options="multiple:'false', panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':1,'text':'未婚'},{'id':2,'text':'已婚'}]"  style="width:170px;"/></td>
		    	     	 	<td style="width:100px;text-align:center">人员状态</td>
		    	     	  <td><input id="ryzt" name="ryzt" class="easyui-combobox" data-options="multiple:'false', panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':10,'text':'在职'},{'id':30,'text':'离职'}]"  style="width:170px;"/></td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;<font id="sx">自动刷新</font>&nbsp;<span id="span1">10</span>
				<input type="checkbox"  id="xzz" checked onclick="sx(this);"></input>&nbsp;
				<br/><br/>
				
				<table id="dg_ymzq" title="在职人员信息列表" class="easyui-datagrid" style="width:100%;height:370px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"   singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="bm"   width=100px;>部门</th>
						<th field="gh"   width=70px;>工号</th>  
						<th field="xm"  width=70px;>姓名</th>
						<th field="xb"   width=60px;>性别</th> 
						<th field="xzz"   width=200px;>家庭住址</th> 
						<th field="sfzh"   width=150px;>身份证号</th> 
						<th field="sjhm"   width=110px;>手机号</th>
						<th field="hyzk"   width=110px;>婚姻状况</th> 
						<th field="ryzt"   width=80px;>人员状态</th>
						<th field="rzrq"   width=110px;>入职日期</th>
						<th field="gl"   width=80px;>工龄(年)</th>
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 