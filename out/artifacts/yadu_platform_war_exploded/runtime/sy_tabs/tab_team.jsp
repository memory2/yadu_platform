

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
<title>���⿼�������</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<%
	   String deptid  = (String)session.getAttribute("bmid");
	   String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
	   if(deptid==null||"".equals(deptid)){
		   response.sendRedirect(request.getContextPath()+"/runtime/login/login.jsp");
	   }%>
	<script type="text/javascript">
	  $(function(){
		    cx();
	        $('#btn_search_team').bind('click', function(){    
	            cx();
	        }); 

	    });
	  
	  function cx(){
		  var tname = $("#tname").val();
		  var tmember = $("#tmember").val();
		  var gslb = $("#gslb").combobox('getValue');
		  //alert(sqksrq+"--"+jbzgs);
		  
		  var url = 'servlet/KqAction?method=sy_team&tname='+tname+'&tmember='+tmember+'&gslb='+gslb;
          var gridID = "dg_team";
          var gridBT = "���⿼�������";
		  
		  
          var xm = '<%=xm%>';
          if(xm!='ϵͳ����Ա'){
        	  getDataGird(url,gridID,gridBT);  
          }else{
              $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
              var updUrl= 'runtime/sy_tabs/tab_team_xx.jsp';
              var delUrl = 'servlet/KqAction?method=xx_del_team';
    		  getDataGird1(url,gridID,gridBT,updUrl,delUrl,'900','300');  
          }
	  }
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}

	 
	 </style>
</head> 
<body>


		         <div title="���⿼�������" style="padding:10px;" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">����</td>
		    	     	   <td><input id="tname" class="easyui-textbox" name="tname"  style="width:200px;"/> </td>
		    	     	  <td style="width:100px;">���Ա</td>
		    	     	  <td><input id="tmember" class="easyui-textbox" name="tmember"  style="width:200px;"/></td>
		    	     	  <td style="width:100px;">��˾���</td>
		    	     	  <td>
		    	     	   <input id="gslb" name="gslb"  class="easyui-combobox" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'�Ƕ�ʵҵ'},{'id':'61','text':'������(����)'},{'id':'71','text':'������(����)'},{'id':'51','text':'���Ͽ�'}]"  style="width:100px;"/>
		    	     	  </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search_team" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a>
				<br/><br/>
				
				<table id="dg_team" title="���⿼����" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"  
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=60px;  >����ID</th>
 						<th field="tname"   width=70px;>����</th> 
						<th field="tmember"  width=300px;>���Ա</th> 
						<th field="gslb"   width=50px;>��˾���</th> 
						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 