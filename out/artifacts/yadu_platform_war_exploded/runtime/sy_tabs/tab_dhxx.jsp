
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
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<%
   		String xm = (String)session.getAttribute("truename")==null?"":(String)session.getAttribute("truename");
    %>
	<script type="text/javascript">
	  $(function(){
		    cx();
	        $('#btn_search_dhxx').bind('click', function(){    
	            cx();
	        }); 
	        
	        $('#btn_tj_dhxx').bind('click', function(){ 
	        	$("#dh").hide();
	        	$("#ss").show(); 
	        	tj();
	        	//$("body").html('<iframe style="width:100%;height:500px" scrolling="auto" frameborder="0" src="runtime/sy_tabs/tj_tab_sbxx.jsp"></iframe>');
	        }); 
	    });
	  
	  function cx(){
		  var fjh = $("#fjh").val();
		  var dhhm = $("#dhhm").val();
		  var hjsl = $("#hjsl").val();

		  var url = 'servlet/OaAction?method=sy_dhxx&fjh='+fjh+'&dhhm='+dhhm+'&hjsl='+hjsl;
          var gridID = "dg_dhxx";
          var gridBT = "电话信息";
          var xm = '<%=xm%>';
          if(xm!='系统管理员'){
        	  getDataGird(url,gridID,gridBT);  
          }else{
              $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
              var updUrl= 'runtime/sy_tabs/tab_dhxx_xx.jsp';
              var delUrl = 'servlet/OaAction?method=xx_del_dhxx';
    		  getDataGird1(url,gridID,gridBT,updUrl,delUrl,'800','200');  
          }
          
 

		  
	  }
	  
	  function fhcx(){
		  $("#dh").show();
      	  $("#ss").hide(); 
      	  $("#chartContainer").hide();
	  }
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>
<div id="ss" style="display:none;margin-bottom:10px;margin-top:10px;"><a id="btn_cx_dhxx" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onclick="fhcx()">返回查询</a></div>
<div id="chartContainer" style="text-align:center;"></div>
	<script type="text/javascript">
	  var content = '<chart caption="电话号码统计饼状图" baseFontSize="14" showNames="1" showBorder="0" outCnvBaseFont="华文新魏"  outCnvBaseFontSize="20" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="0" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="1" numDivLines="0"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="个" plotSpacePercent="50">';
	  var myChart=null;
	  var sm={};
	  function tj(){
          $.ajax({  
              type: 'POST',
              dataType: "json",
              url:'servlet/OaAction?method=tj_dhxx',
              success: function(data){
            	  //var d = eval("("+data+")");//效果等同于加dataType
            	  //alert(JSON.stringify(data[0]));
            	  var lt = JSON.stringify(data[0].lt);
            	  var yd = JSON.stringify(data[0].yd);
            	  var tt = JSON.stringify(data[0].tt);
            	  content+='<set label="联通"  value='+lt+' /><set label="移动"  value='+yd+' /><set label="铁通"  value='+tt+' /></chart>';
            	  if(myChart==null){
                	  myChart = new FusionCharts("js/tjjs/FusionCharts/Doughnut2D.swf", "myChartId", "100%", "450");
                   	  myChart.setXMLData(content);
                   	  myChart.render("chartContainer");
            	  }else{
            		  $("#chartContainer").show();
            	  }

              }
          });
	  }

	</script>

		         <div title="电话信息" style="padding:10px;" id="dh">		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">电话位置</td>
		    	     	  <td><input id="fjh" class="easyui-textbox" name="fjh"  style="width:220px;"/> </td>
		    	     	  <td style="width:100px;">电话号码</td>
		    	     	  <td><input id="dhhm" class="easyui-textbox" name="dhhm"  style="width:220px;"/></td>
		    	     	  <td style="width:100px;text-align:center">话机数量</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0" style="width:220px;" id="hjsl">
		    	     	  </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				<!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>--> 
				   
				<a id="btn_search_dhxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				<a id="btn_tj_dhxx" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">统计图</a>
				<font style="font-size:12px;">[电话号码128个(含迈科防腐2个)，话机数量167部]</font>
				<br/><br/>
				
				<table id="dg_dhxx" title="电话信息列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=100px; sortable=true, checkbox=true >主键ID</th> 
						<th field="fjh"  width=200px;>电话位置</th> 
						<th field="dhhm"   width=300px;>电话号码</th> 
						<th field="hjsl"   width=200px;>话机数量</th> 
						<th field="yys"   width=200px;>运营商</th> 
						<th field="cz"   width=100px;>传真</th> 
						<th field="bz"   width=100px;>备注</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 
</body>  
</html> 