
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
	    <% String czlb = request.getParameter("czlb");
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
              url:'servlet/OaAction?method=cx_sbxx&zj='+zj,
              dataType: "json",
              success: function(data){
            	  $("#sbmc").textbox('setValue',data[0].sbmc);
            	  $("#sbsl").textbox('setValue',data[0].sbsl);
            	  $("#gmrq").datebox('setValue',data[0].gmrq);
            	  $("#sbwz").textbox('setValue',data[0].sbwz);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var gmrq = $("#gmrq").datebox('getValue');
		  var sbmc = $("#sbmc").val();
		  var sbwz = $("#sbwz").val();
		  var sbsl = $("#sbsl").val();
		  var args={'zj':zj,'gmrq':gmrq,'sbsl':sbsl};
		  var method = 'xx_upd_sbxx';
		  if(czlb=='add'){
			  method='xx_add_sbxx';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/OaAction?method='+method+'&sbmc='+sbmc+'&sbwz='+sbwz,
              data: args,
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


		    	<div title="网络设备信息"  id="fksq">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">设备名称</td>
		    	     	  <td><input id="sbmc" class="easyui-textbox" name="sbmc"  style="width:220px;"/></td>
		    	     	  <td style="width:100px;text-align:center">购买日期</td>
		    	     	  <td><input id="gmrq" type="text" class="easyui-datebox" style="width:220px;"></input></td>
		    	     	</tr>
		    	     	<tr>
		    	     	  <td style="width:100px;">设备数量</td>
		    	     	  <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0" style="width:220px;" id="sbsl"></input> </td>
		    	     	  <td style="width:100px;">设备位置</td>
		    	     	  <td><input id="sbwz" class="easyui-textbox" name="sbwz"  style="width:220px;"/></td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div>  
				   
				
		    <div region="south" border="false" style="text-align:center;padding:13px 0px;">
             <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" id="tj" >提交</a>
            <!--a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="gb">关闭</a>  -->
            </div>

</body>  
</html> 