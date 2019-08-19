<%@ page language="java" contentType="text/html;charset=gb2312" pageEncoding ="gb2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
 <head>
 <base href="<%=basePath%>"> 
  <title>欢迎</title>
 </head>
 <body>
  <%
   String userName = (String)session.getAttribute("username") ;

  %>
  <div align="center">
   <%=userName %>
   欢迎您，登陆成功！<br />
   <font color="blue">登陆用户信息：</font>
   <table border =1 >
    <tr>
     <td>&nbsp;姓名：&nbsp;</td>
     <td>&nbsp;&nbsp;<%=userName %>&nbsp;&nbsp;</td>
    </tr>
   </table>
   <a href="runtime/login/login.jsp">返回</a>
  </div>
 </body>
</html>
