
<%@ page language="java" contentType="text/html; charset=UTF-8" 
pageEncoding="UTF-8"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
    <title>登录失败</title> 
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/demo.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	  <style>
      #p{width:300px;height:200px;padding:20px;background:#fafafa;text-align:center;}
      P{color:red;font-weight:bold;font-size:16px;}
    </style>
    <script type="text/javascript">
    $(function(){
      $('#fh').bind('click', function(){    
    	  window.history.go(-1);
      }); 
    })
    </script>
 </head>
 <body>
  <%
   String userName = (String)session.getAttribute("username") ;
   String msg  = (String)session.getAttribute("message") ;
   if(!"fail".equals(msg)){
	   //request.getRequestDispatcher("/runtime/login/login.jsp").forward(request, response); 
	   response.sendRedirect(request.getContextPath()+"/runtime/login.jsp");
   }
  %>
<div style="text-align:left;margin-left:40%;margin-top:10%;width:400px;height:300px">
    <div id="p" class="easyui-panel" title="登录失败"      
        data-options="iconCls:'icon-man'">   
    <p>用户名或密码不对，请重新登陆！</p>   
    <p>5秒后自动返回登陆界面。</p>  
    <div style="margin-top:55px;">
    <p><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" id="fh" >返回</a> </p></div>
    <!--p>查询失败，请联信息部管理员</p>
    <p>管理员联系方式：83960</p>-->
</div> 
</div>
   <%
     response.setHeader("Refresh","5;URL=../login.jsp");   
    %>
 </body>
</html>
