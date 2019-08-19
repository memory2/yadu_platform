
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
              url:'servlet/KqAction?method=cx_jsgl&zj='+zj,
              dataType: "json",
              success: function(data){
            	  $("#fid").combobox('setValue',data[0].fid);
            	  $("#jsmc").textbox('setValue',data[0].jsmc);
            	  $("#tb").combobox('setValue',data[0].tb);
            	  $("#ymdz").textbox('setValue',data[0].ymdz);
            	  $("#bz").textbox('setValue',data[0].bz);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var fid = $("#fid").combobox('getValue');
		  var fname = $("#fid").combobox('getText');
		  var jsmc = $("#jsmc").val();
		  var tb = $("#tb").combobox('getValue');
		  var ymdz = $("#ymdz").val();
		  var bz = $("#bz").val();
		  var args={'zj':zj,'fid':fid,'ymdz':ymdz,'tb':tb};
		  var method = 'xx_upd_jsgl';
		  if(czlb=='add'){
			  method='xx_add_jsgl';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method='+method+'&jsmc='+jsmc+'&bz='+bz+'&fname='+fname,
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

		         <div title="角色管理信息"  id="jsgl">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;margin:8px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">角色名称</td>
		    	     	  <td><input id="jsmc" class="easyui-textbox" name="jsmc"  style="width:130px;"/></td>	    	     	  
		    	     	  <td style="width:100px;text-align:center">父角色名称</td>
		    	     	  <td><input id="fid" class="easyui-combobox" name="fid" style="width:130px;"  data-options="panelHeight:'100', valueField:'id',textField:'text',url:'servlet/KqAction?method=zd&zdmc=父角色名称&zdlb=0'" /></td>
		    	     	  <td style="width:100px;">角色图标</td>
		    	     	  <td><input class="easyui-combobox" id="tb" style="width:130px;" data-options="panelHeight:'auto',valueField: 'id',textField: 'text',data: [{text: '一级菜单图标',id: 'icon-menu1'},{text: '二级菜单图标',id: 'icon-menu2'}]" />
						  </td>
		    	     	</tr>
		    	     	<tr>
		    	     	  <td style="width:100px;">页面地址</td>
		    	     	  <td colspan=3><input id="ymdz" class="easyui-textbox" name="ymdz" style="width:360px;" /> </td>
		    	     	  <td style="width:100px;text-align:center">备注</td>
		    	     	  <td><input id="bz" class="easyui-textbox" name="bz"  style="width:130px;"/></td>
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