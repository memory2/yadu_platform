<%@ page language="java" contentType="text/html;charset=gb2312" pageEncoding ="gb2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
 <head>
 <base href="<%=basePath%>"> 
  <title>��ӭ</title>
 </head>
 <body>
  <%
   String userName = (String)session.getAttribute("username") ;

  %>
  <div align="center">
   <%=userName %>
   ��ӭ������½�ɹ���<br />
   <font color="blue">��½�û���Ϣ��</font>
   <table border =1 >
    <tr>
     <td>&nbsp;������&nbsp;</td>
     <td>&nbsp;&nbsp;<%=userName %>&nbsp;&nbsp;</td>
    </tr>
   </table>
   <a href="runtime/login/login.jsp">����</a>
  </div>
 </body>
</html>
