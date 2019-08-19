
<%@ page language="java" contentType="text/html; charset=gbk" 
pageEncoding="gbk"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=gbk"> 
    <title>OA数据查询</title>
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<%
	  String yhm =request.getParameter("gh");
	  String pwd = "";
	  if(yhm==null||"".equals(yhm)){
         yhm="gb";
	  }else{
		  yhm = new String (request.getParameter("gh").getBytes("ISO-8859-1"),"gb2312");
		  pwd = (String)request.getParameter("pwd");
	  }
	%>
<script type="text/javascript" charset="gbk">
		//登录
		$(function(){ 
			 var yhm = '<%=yhm%>';
			 var pwd = '<%=pwd%>';
			 if(yhm=="gb"){
				 window.close();
			 }else{
				 $("#gh").val(yhm);
				 $("#pwd").val(pwd);
				  var dlHref = "servlet/KqAction?method=dl&gh="+yhm+"&pwd="+pwd;
				  $("#loginForm").attr("action",dlHref); 
				  $("#loginForm").submit();
			 }
		}); 

</script>
 </head>
 <body>
  <div style="display:none!important;">
        <form id="loginForm" method="post" >
            <div class="input_div">
                <input type="text" name="gh" id="gh" value=""></input>
            </div>
            <div class="input_div">
                <input type="password" name="pwd" id="pwd" value=""></input>
            </div>
            <div id="showMsg"></div>
        </form>
      <a  href="javascript:void(0)" id="dl" >登录</a>
</div>
</body>

</html>