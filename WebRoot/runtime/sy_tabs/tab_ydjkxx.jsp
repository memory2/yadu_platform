
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
<title>亚都监控信息</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript">
	  $(function(){
		    cx();
	    });
	  
	  function cx(){
		  alert();
          $.ajax({  
              type: 'POST',
              dataType: "json",
              url:'servlet/KhAction?method=ydjkxx',
            
              success: function(data){
            	      alert(JSON.stringify(data));
            		  //var jsonData  = JSON.stringify(data[0]);
            		  //$("#dg_kqxx").html(JSON.stringify(data));
            		      
              },
			   complete:function(xhr){ 
            		  //img.hide(); 
            		 // mask.hide(); 
               }
          });
	  }
	  
	  
	
	  
	</script>
 	 <style>
	tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; height:30px;text-align:center;}
	 </style>
</head> 
<body>

		       	<div id="dg_jkxx" ></div> 
		        </div>
		        
		        <img id="progressImgage" class="progress" style="display:none" alt="" src="runtime/sy_tabs/ajax-loader.gif"/> 
				<div id="maskOfProgressImage" class="mask" style="display:none"></div> 
                <div id="fhdb"><a href="javascript:scroll(0,0)">返回顶部</a></div>
</body>  
</html> 