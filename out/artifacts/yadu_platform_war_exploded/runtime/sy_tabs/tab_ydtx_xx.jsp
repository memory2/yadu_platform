
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
       String deptid  = (String)session.getAttribute("deptid");
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
	        
	        
			getDept("bm",<%=deptid%>);

          
	    });
	  
	  function cx(){
		  var zj = '<%=zj%>';
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method=cx_ydtx&zj='+zj,
              dataType: "json",
              success: function(data){
            	  $("#bm").combotree('setText',data[0].bm);
            	  $("#lxr").textbox('setValue',data[0].lxr);
            	  $("#sjhm").textbox('setValue',data[0].sjhm);
            	  $("#dh").textbox('setValue',data[0].dh);
            	  $("#gh").textbox('setValue',data[0].gh);
            	  $("#fjh").textbox('setValue',data[0].fjh);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var bm = $("#bm").combobox('getText');
		  var fjh = $("#fjh").val();
		  var lxr = $("#lxr").val();
		  var sjhm = $("#sjhm").val();
		  var gh = $("#gh").val();
		  var dh = $("#dh").val();
		  var method = 'xx_upd_ydtx';
		  if(czlb=='add'){
			  method='xx_add_ydtx';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/KqAction?method='+method+'&lxr='+lxr+'&bm='+bm+'&sjhm='+sjhm+'&gh='+gh+'&dh='+dh+'&fjh='+fjh+'&zj='+zj,
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

		         <div title="亚都通讯信息"  id="jsgl">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">部门</td>
		    	     	  <td><input id="bm" class="easyui-combotree" data-options="valueField:'id',panelHeight:'100',textField:'text'"  name="bm" style="width:170px;"/>  
		    	     	  </td>
		    	     	  <td style="width:100px;">姓名</td>
		    	     	  <td><input id="lxr" class="easyui-textbox" name="lxr"  style="width:170px;"/></td>
		    	     	  <td style="width:100px;">手机号码</td>
		    	     	  <td>
		    	     	    <input id="sjhm" class="easyui-textbox" name="sjhm"  style="width:170px;"/> 
		    	     	  </td>
		    	     	<tr>
		    	     			    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">短号</td>
		    	     	  <td><input id="dh" class="easyui-textbox" name="dh"  style="width:170px;"/> 
		    	     	  </td>
		    	     	  <td style="width:100px;">固话</td>
		    	     	  <td><input id="gh" class="easyui-textbox" name="gh"  style="width:170px;"/></td>
		    	     	  <td style="width:100px;">房间号</td>
		    	     	  <td>
		    	     	    <input id="fjh" class="easyui-textbox" name="fjh"  style="width:170px;"/> 
		    	     	  </td>
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