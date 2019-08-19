
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
    <title>欢迎登陆</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/demo.css">
	<link rel="stylesheet" type="text/css" href="css/page/login.css" />
	<link rel="Shortcut Icon" href="oa.ico" type="image/x-icon"/> 
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/ydCommon.js"></script>

 </head>
 <body>
     <div class="second_body">
        <form id="loginForm" method="post">
            <div class="logo"></div>
            <div class="title-zh">企业信息管理系统</div>
            <div class="title-en" style="">Enterprise Information Manage System</div>
            <table border="0" style="width:300px;">
                <tr>
                    <td style="white-space:nowrap; padding-bottom: 5px;width:55px;">用户名：</td>
                    <td colspan="2"><input type="text" id="gh" class="login"  /></td>
                </tr>
                <tr>
                    <td class="lable" style="white-space:nowrap; letter-spacing: 0.5em; vertical-align: middle">密码：</td>
                    <td colspan="2"><input type="password" id="pwd" class="login"  /></td>
                </tr>
            
            </table>
            
                <div  style="margin-top:25px;margin-left:80px;">
                        <input type="submit" value="登录" class="login_button" id="dl" />
                        <input type="button" value="重置" class="reset_botton"  id="cz" />
                </div>
        </form>
    </div>
        
 </body>
<script type="text/javascript">
 //登录验证
 function dlyz(){

			   var gh = $("#gh").val();

			   if (gh ==""||gh==null||gh==undefined){
			    	alert("账号不能为空！");
			        $("#gh").focus();
			        return;
			   }

				 var pwd = $("#pwd").val();
				 if(pwd!=""&&pwd!=null&&pwd!=undefined){
				    	pwd = jsEncode(pwd);
				 }else{
					 alert("密码不能为空！");
					 $("#pwd").focus();
					 return;
				 } 
				 
    				  var dlHref = "servlet/KqAction?method=dl&gh="+gh+"&pwd="+pwd;
    				  $("#loginForm").attr("action",dlHref); 
    				  $("#loginForm").submit();
                	  

			     

 }



 
 //登录
$(function(){ 
	//账号值改变
	$("#gh").change(function(){
		   var gh = $("#gh").val();

		   if (gh ==""||gh==null||gh==undefined){
		    	alert("账号不能为空！");
		        $("#gh").focus();
		        return;
		   }else{
			  // $("#showMsg").html("");
		   }
		
	});
	
	  //点击登录按钮
	  $("#dl").click(function(){ 
		  dlyz();
		  
     }); 
	  
	  //点击回车提交

	  $(document).keyup(function(event){
		  if(event.keyCode ==13){
			  dlyz();
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