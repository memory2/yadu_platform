
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
<title>工龄结构分析</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/copyhtmltoexcel.js"></script>
	<script type="text/javascript" src="js/tableToExcel.js"></script>
	<script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	 <%
   String msg  = (String)session.getAttribute("message");
   if(!"ok".equals(msg)){
	   response.sendRedirect(request.getContextPath()+"/runtime/login/my_login.jsp");
   }
   String bmmc = (String)session.getAttribute("bmmc")==null?"":(String)session.getAttribute("bmmc");
   String bmdm  = (String)session.getAttribute("bmdm");
   String oaryid  = (String)session.getAttribute("oaryid");
   String zwjb  = (String)session.getAttribute("zwjb");
   String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
  %>
  
  
	<script type="text/javascript">
	

	var img;
	  var mask;
	
      var bmmc = '<%=bmmc%>';
      var txm = '<%=oaryid%>';
      var zwjb = '<%=zwjb%>';
      var bmdm = '<%=bmdm%>';
      var xm = '<%=xm%>';
	  $(function(){
		 img = $("#progressImgage"); 
		 mask = $("#maskOfProgressImage");
		 $("#gslb").combobox('setValue','1');
		 $("#ryzt").combobox('setValue','10');
		 cx();
	        $('#btn_cx').bind('click', function(){   
	            cx();
	        }); 
	        $('#btn_dc').bind('click', function(){   
	        	method1('rsgl');
	        });
	    });
	  
	   
	    
	  
      function method1(tableid) {

          var curTbl = document.getElementById(tableid);
          var oXL = new ActiveXObject("Excel.Application");
          var oWB = oXL.Workbooks.Add();
          var oSheet = oWB.ActiveSheet;
          var sel = document.body.createTextRange();
          sel.moveToElementText(curTbl);
          sel.select();
          sel.execCommand("Copy");
          oSheet.Paste();
          oXL.Visible = true;

      }
	      
  


	</script>
  	 <style>
  	 #rsgl{width:98%;margin-right:20px;}
  	 
	tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;text-align:center;font-size:12px;white-space:0;height:28px;}
	.nr{height:36px;font-size:14px;}
	#dg_rsxx{border-collapse:collapse; float:left;width:50%;font-family:微软雅黑}
	.bt{font-size:14px;font-weight:bold;height:36px;}
	.zbt{font-size:16px;font-weight:bold;height:36px;border-width:}
	.xh{font-size:14px;width:50px!important;font-weight:bold;}
	.rs{text-align:right;padding-right:8px;font-weight:bold;height:36px;font-size:14px;}
	#chartContainer{margin-left:50px;}
	 </style>
</head> 


 
<body>

<div title="人员信息" style="padding:10px;width:98%;margin-top:20px;" >	
		        <div class="table-a">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	 <td style="width:150px;text-align:center">公司类别</td>
		    	     	   <td style="width:250px;">
		    	     	   <input id="gslb" name="gslb"  class="easyui-combobox"  style="width:150px" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'1','text':'亚都实业'},{'id':'2','text':'威浦仕'},{'id':'3','text':'迈迪科'}]"  style="width:100px;"/>
		    	     	  </td>
		    	     	  	<td style="width:150px;text-align:center">到职日期</td>
		    	     	  <td style="width:400px;text-align:center"><input id="kssj" type="text" class="easyui-datebox" style="width:150px;"></input> 至
		    	     	  <input id="jssj" type="text" class="easyui-datebox" style="width:150px;"></input>
		    	     	  </td>
		    	     	  <td style="width:150px;text-align:center">雇佣状态</td>
		    	     	  <td style="width:250px;">
		    	     	   <input id=ryzt name="ryzt"  class="easyui-combobox" style="width:150px" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'10','text':'在职'},{'id':'20','text':'离退'},{'id':'30','text':'辞职'}]"  style="width:100px;"/>
		    	     	  </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/> 
		    	<div style="text-align:center">  
				<a id="btn_cx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				<a id="btn_dc" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:20px;">导出表格</a></div>
				<br/>
				<div><font style="color:red;font-size:14px;font-family:微软雅黑">备注：人员统计以【到职日期】不为空且数据合法为准。</font></div>
				<br/> <br/>  
		        <div id="dg_rsxx" ></div> 
		        <div id="chartContainer" ></div>	 
		        </div>
		        
		        
<script type="text/javascript">

function cx(){
	 
	 var gslb = $("#gslb").combobox('getValue');
	 var Tgslb = $("#gslb").combobox('getText');
	 var ryzt = $("#ryzt").combobox('getValue');
	 var Tryzt = $("#ryzt").combobox('getText');
	  var kssj = $("#kssj").datebox('getValue');
	  var jssj = $("#jssj").datebox('getValue');
	  if(gslb==null||gslb==undefined ||gslb==""){
		  alert("请选择公司类别！");
		  return;
	  }
	  var dzrq = "";
	  if(kssj!=""&&jssj!=""){
		  if((new Date(kssj.replace(/-/g,"\/"))) > (new Date(jssj.replace(/-/g,"\/")))){
			  alert("开始时间不能大于结束时间！");
			  $("#kssj").datebox('setValue','');
			  $("#jssj").datebox('setValue','');
			  return;
		  }
		  dzrq = kssj+"至"+jssj+"入职，";
	  }else if(kssj==""&&jssj!=""){
		  dzrq = jssj+"之前入职，";
	  }else if(kssj!=""&&jssj==""){

		  dzrq = kssj+"之后入职，";
	  }else{
		  dzrq = '截至目前，';
	  }
	  
		var content = '<chart caption="'+Tgslb+Tryzt+'员工工龄结构饼状图" baseFontSize="16" showNames="1" showBorder="0" baseFont="微软雅黑"  outCnvBaseFontSize="100" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="0" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="1" numDivLines="0"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="人" plotSpacePercent="50">';
		var myChart=null;   
		  
		 var url = "servlet/U8Action?method=rsgl&gslb="+gslb+"&kssj="+kssj+"&jssj="+jssj+"&ryzt="+ryzt;
		  $.ajax({  
             type: 'POST',
             dataType: "json",
             url:url,
             beforeSend:function(xhr){ 
           	  img.show().css({ 
           	  	"position": "fixed", 
           	  	"top": "40%", 
           	  	"left": "45%", 
           	  	"margin-top": function () { return -1 * img.height() / 2; }, 
           	  	"margin-left": function () { return -1 * img.width() / 2; } 
           	  }); 
           	  mask.show().css("opacity", "0.1"); 
           	  }, 
             success: function(data){
           	     // alert(JSON.stringify(data));
           		  //var jsonData  = JSON.stringify(data[0]);
           		  //$("#dg_kqxx").html(JSON.stringify(data));
           		   if(data.length>0){
           			   var lb1 = data[0]['lb1'];
           			   var lb2 = data[0]['lb2'];
           			   var lb3 = data[0]['lb3'];
           			   var lb4 = data[0]['lb4'];
           			   var lb5 = data[0]['lb5'];
           			   var lb6 = data[0]['lb6'];
           			   var zs = data[0]['zs'];
           		   var nljg = "<table id='rsgl'><tr><td  colspan=4 class='zbt'>"+Tgslb+Tryzt+"员工工龄结构表格</td></tr>"+
           		   "<tr><td  colspan=4 class='rs'>"+dzrq+Tryzt+"员工总数："+zs+"人</td></tr>"+
           		   "<tr><td  class='xh'>序号</td><td  class='bt'>工龄范围</td><td  class='bt'>总人数</td><td  class='bt'>百分比</td></tr>"+
           		   "<tr><td class='nr'>1</td><td class='nr'>1年以下</td><td class='nr'>"+lb1+"</td><td class='nr'>"+(lb1/zs*100).toFixed(2) +"%</td></tr>"+
           		   "<tr><td class='nr'>2</td><td class='nr'>1-2年</td><td class='nr'>"+lb2+"</td><td class='nr'>"+(lb2/zs*100).toFixed(2) +"%</td></tr>"+
           		   "<tr><td class='nr'>3</td><td class='nr'>2-3年</td><td class='nr'>"+lb3+"</td><td class='nr'>"+(lb3/zs*100).toFixed(2) +"%</td></tr>"+
           		   "<tr><td class='nr'>4</td><td class='nr'>3-4年</td><td class='nr'>"+lb4+"</td><td class='nr'>"+(lb4/zs*100).toFixed(2) +"%</td></tr>"+
           		   "<tr><td class='nr'>5</td><td class='nr'>4-5年</td><td class='nr'>"+lb5+"</td><td class='nr'>"+(lb5/zs*100).toFixed(2) +"%</td></tr>"+
           		   "<tr><td class='nr'>6</td><td class='nr'>5年以上</td><td class='nr'>"+lb6+"</td><td class='nr'>"+(lb6/zs*100).toFixed(2) +"%</td></tr>"+
           		   "</table>";
           		   $("#dg_rsxx").html(nljg);
           		   
                	  content+='<set label="1年以下"  value="'+lb1+'" /><set label="1-2年"  value="'+lb2+'" /><set label="2-3年"  value="'+lb3+'" /><set label="3-4年"  value="'+lb4+'" /><set label="4-5年"  value="'+lb5+'" /><set label="5年以上"  value="'+lb6+'" /></chart>';
                	  myChart = new FusionCharts("js/tjjs/FusionCharts/Pie2D.swf", "myChartId", "50%", "440");
                      	  myChart.setXMLData(content);
                      	  myChart.render("chartContainer");
               	      
               }
             },
			   complete:function(xhr){ 
           		  img.hide(); 
           		  mask.hide(); 
              }
         });
	 // }

 }
</script>
	       
		        <img id="progressImgage" class="progress" style="display:none" alt="" src="runtime/sy_tabs/ajax-loader.gif"/> 
				<div id="maskOfProgressImage" class="mask" style="display:none"></div> 
                
		        
</body>  
</html> 