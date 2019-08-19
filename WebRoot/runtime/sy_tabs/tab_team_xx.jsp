
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
    <% 
       String czlb = request.getParameter("czlb");
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
              url:'servlet/KqAction?method=cx_team&zj='+zj,
              dataType: "json",
              success: function(data){
            	  $("#tname").textbox('setValue',data[0].tname);
            	  $("#tmember").textbox('setValue',data[0].tmember);
            	  $("#gslb").combobox('setValue',data[0].gslb);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var tname = $("#tname").val();
		  var tmember = $("#tmember").val();
		  var gslb = $("#gslb").combobox('getValue');
		  var args={'zj':zj,'gslb':gslb};
		  var method = 'xx_upd_team';
		  if(czlb=='add'){
			  method='xx_add_team';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method='+method+'&tname='+tname+'&tmember='+tmember,
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

		         <div title="特殊考勤组信息"  id="team">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	 <tr>
		    	     	  <td style="width:100px;">组名</td>
		    	     	  <td colspan=3><input id="tname" class="easyui-textbox" name="tname" required=true style="width:685px;"/></td>
		    	     	</tr>
		    	     	<tr>
		    	     	  <td style="width:100px;">组成员</td>
		    	     	  <td colspan=3><input id="tmember" class="easyui-textbox" name="tmember" required=true data-options="multiline:true" style="width:685px;height:120px;"/></td>
		    	     	</tr>
		    	     	 <tr>
		    	     	  <td style="width:100px;text-align:center">公司类别</td>
		    	     	  <td>		    	     	  
		    	     	   <input id="gslb" name="gslb"  class="easyui-combobox" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'亚都实业'},{'id':'61','text':'威浦仕(行政)'},{'id':'71','text':'威浦仕(车间)'},{'id':'51','text':'迈迪克'}]"  style="width:685px;"/>
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