
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
    <title>��ӭ��½</title>
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
            <div class="title-zh">��ҵ��Ϣ����ϵͳ</div>
            <div class="title-en" style="">Enterprise Information Manage System</div>
            <table border="0" style="width:300px;">
                <tr>
                    <td style="white-space:nowrap; padding-bottom: 5px;width:55px;">�û�����</td>
                    <td colspan="2"><input type="text" id="gh" class="login"  /></td>
                </tr>
                <tr>
                    <td class="lable" style="white-space:nowrap; letter-spacing: 0.5em; vertical-align: middle">���룺</td>
                    <td colspan="2"><input type="password" id="pwd" class="login"  /></td>
                </tr>
            
            </table>
            
                <div  style="margin-top:25px;margin-left:80px;">
                        <input type="submit" value="��¼" class="login_button" id="dl" />
                        <input type="button" value="����" class="reset_botton"  id="cz" />
                </div>
        </form>
    </div>
        
 </body>
<script type="text/javascript">
 //��¼��֤
 function dlyz(){

			   var gh = $("#gh").val();

			   if (gh ==""||gh==null||gh==undefined){
			    	alert("�˺Ų���Ϊ�գ�");
			        $("#gh").focus();
			        return;
			   }

				 var pwd = $("#pwd").val();
				 if(pwd!=""&&pwd!=null&&pwd!=undefined){
				    	pwd = jsEncode(pwd);
				 }else{
					 alert("���벻��Ϊ�գ�");
					 $("#pwd").focus();
					 return;
				 } 
				 
    				  var dlHref = "servlet/KqAction?method=dl&gh="+gh+"&pwd="+pwd;
    				  $("#loginForm").attr("action",dlHref); 
    				  $("#loginForm").submit();
                	  

			     

 }



 
 //��¼
$(function(){ 
	//�˺�ֵ�ı�
	$("#gh").change(function(){
		   var gh = $("#gh").val();

		   if (gh ==""||gh==null||gh==undefined){
		    	alert("�˺Ų���Ϊ�գ�");
		        $("#gh").focus();
		        return;
		   }else{
			  // $("#showMsg").html("");
		   }
		
	});
	
	  //�����¼��ť
	  $("#dl").click(function(){ 
		  dlyz();
		  
     }); 
	  
	  //����س��ύ

	  $(document).keyup(function(event){
		  if(event.keyCode ==13){
			  dlyz();
		  }
		});
		 

	  
	 //������ð�ť
	 $("#cz").click(function(){
		 $("#loginForm").form("clear");
		 $("#gh").focus();
	 });
	  
}); 



</script>
</html>