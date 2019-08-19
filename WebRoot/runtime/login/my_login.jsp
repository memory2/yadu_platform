
<%@ page language="java" contentType="text/html; charset=GBK" 
pageEncoding="GBK"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK"> 
    <title>登陆</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/demo.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/ydCommon.js"></script>
    <style>
      #gh,#pwd{width:200px;height:20px;}
      .input_div{padding:5px 0;margin-top:15px;}
      #p{width:300px;height:200px;padding:10px;background:#fafafa;text-align:center;}
      #showMsg{padding:5px 0;text-align: center;color: red;height:8px;}
    </style>
 </head>
 <body onload="javascript:$('#gh').focus()">

  <div style="text-align:left;margin-left:40%;margin-top:10%;width:400px;">
    
    <h3 style="color:#0099FF;"><font style="font-size:28px;font-weight:bold;">欢迎登陆</font></h3>
	<div id="p" class="easyui-panel" title="登陆    [同OA账号密码一样]"  data-options="iconCls:'icon-man'"> 
        <form id="loginForm" method="post" >
            <div class="input_div">
                <label for="login">账号:</label>
                <input type="text" name="gh" id="gh" value=""></input>
            </div>
            <div class="input_div">
                <label for="login">密码:</label>
                <input type="password" name="pwd" id="pwd" value=""></input>
            </div>
            <div id="showMsg"></div>
        </form>
     
        <div region="south" border="false" style="text-align:center;padding:13px 0px;">
             <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" id="dl" >登录</a>
             <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="cz">重置</a>
        </div>

   </div>
</div>
</body>
<script type="text/javascript">
 //登录验证
 function dlyz(dlfs){

			   var gh = $("#gh").val();

			   if (gh ==""||gh==null||gh==undefined){
			    	$("#showMsg").html("账号不能为空！");
			        $("#gh").focus();
			        return;
			   }

				 var pwd = $("#pwd").val();
				 if(pwd!=""&&pwd!=null&&pwd!=undefined){
				    	pwd = jsEncode(pwd);
				 }else{
					 $("#showMsg").html("密码不能为空！");
					 $("#pwd").focus();
					 return;
				 } 
				 
                  if("1"==dlfs){
    				  var dlHref = "servlet/KqAction?method=dl&gh="+gh+"&pwd="+pwd;
    				  $("#loginForm").attr("action",dlHref); 
    				  $("#loginForm").submit();
                	  
                  }else{
                      var dlHref = "javascript:loginForm.action='servlet/KqAction?method=dl&gh="+gh+"&pwd="+pwd+"';loginForm.submit()";
     			      $("#dl").attr("href",dlHref); 
                  }

			     

 }



 
 //登录
$(function(){ 
	//账号值改变
	$("#gh").change(function(){
		   var gh = $("#gh").val();

		   if (gh ==""||gh==null||gh==undefined){
		    	$("#showMsg").html("账号不能为空！");
		        $("#gh").focus();
		        return;
		   }else{
			   $("#showMsg").html("");
		   }
		
	});
	
	  //点击登录按钮
	  $("#dl").click(function(){ 
		  dlyz("2");
		  
     }); 
	  
	  //点击回车提交

	  
	  
	  $(document).keyup(function(event){
		  if(event.keyCode ==13){
			  dlyz("1");
		  }
		});
		 

	  
	 //点击重置按钮
	 $("#cz").click(function(){
		 $("#loginForm").form("clear");
		 $("#gh").focus();
	 });
	  
}); 



</script>
</html>