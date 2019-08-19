
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
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript" src="js/ydCommon.js"></script>
    <% String czlb = request.getParameter("czlb");
       String deptid  = (String)session.getAttribute("bmid");
       String zj="ss";
       if("upd".equals(czlb)){
    	   zj = request.getParameter("zj"); 
       }
    %> 
	<script type="text/javascript">
	  $(function(){
		  var czlb = '<%=czlb%>';
		  if('upd'==czlb){
			  cx();
		  }
          
	        $('#gb').bind('click', function(){    
	        	parent.$('#myWindow').window('close');
	        }); 
	        
	        $('#tj').bind('click', function(){    
	        	tj(czlb);
	        }); 
	    });
	  
	  function cx(){
		  var zj = '<%=zj%>';
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method=cx_tskq&zj='+zj,
              dataType: "json",
              success: function(data){
            	  $("#rq").datebox('setValue',data[0].rq);
            	  $("#swsb").textbox('setValue',data[0].swsb);
            	  $("#swxb").textbox('setValue',data[0].swxb);
            	  $("#xwsb").textbox('setValue',data[0].xwsb);
            	  $("#xwxb").textbox('setValue',data[0].xwxb);
            	  $("#tname").textbox('setValue',data[0].tname);
            	  $("#lb").textbox('setValue',data[0].lb);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var rq = $("#rq").datebox('getValue'); 
		  var kqbs = $("#lb").val();
		  var tsry = $("#tname").val();
		  var swsb = $("#swsb").val();
		  var swxb = $("#swxb").val();
		  var xwxb = $("#xwxb").val();
		  var xwsb = $("#xwsb").val();
		  var method = 'xx_upd_tskq';
		  if(czlb=='add'){
			  method='xx_add_tskq';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method='+method+'&rq='+rq+'&lb='+kqbs+'&tname='+tsry+'&swsb='+swsb+'&swxb='+swxb+'&xwsb='+xwsb+'&xwxb='+xwxb+'&zj='+zj,
              success: function(data){
            	  if('ok'==data){
            		  alert("提交成功！");
            	  }else{
            		  alert("提交失败,请联系管理员！");
            	  }
            	  parent.cx();
            	  
              }
          });
	  }
	  
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
	 </style>
</head> 
<body>

		         <div title="特殊考勤信息"  id="jsgl">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">日期</td>
		    	     	   <td><input id="rq" type="text" class="easyui-datebox" style="width:150px;"></input></td>
		    	     	  <td style="width:100px;">上午上班</td>
		    	     	  <td><input id="swsb" class="easyui-textbox" name="swsb"  style="width:150px;"/></td>
		    	     	  <td style="width:100px;">上午下班</td>
		    	     	  <td>
		    	     	    <input id="swxb" class="easyui-textbox" name="swxb"  style="width:150px;"/> 
		    	     	  </td>
		    	     	</tr>
		    	     		<tr>
		    	     	  <td style="width:100px;text-align:center">下午上班</td>
		    	     	  <td><input id="xwsb" class="easyui-textbox" name="xwsb"  style="width:150px;"/> 
		    	     	  </td>
		    	     	  <td style="width:100px;">下午下班</td>
		    	     	  <td><input id="xwxb" class="easyui-textbox" name="xwxb"  style="width:150px;"/></td>
		    	     	  <td style="width:100px;">考勤类别</td>
		    	     	  <td>
		    	     	    <input id="lb" class="easyui-textbox" name="lb"  style="width:150px;"/> 
		    	     	  </td>
		    	     	</tr>
		    	     	 <tr>
		    	     	  <td style="width:100px;text-align:center">特殊组</td>
		    	     	  <td colspan=5><input id="tname" class="easyui-textbox" name=""tname""  style="width:570px; "/> 
		    	     	  </td>
		    	     	</tr>
		    	     </table>
		    	  </form>
		    </div> 
			<div region="south" border="false" style="text-align:center;padding:13px 0px;">
             <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" id="tj" >提交</a>
             <!--a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="gb">关闭</a>  -->
            </div>
</body>  
</html> 