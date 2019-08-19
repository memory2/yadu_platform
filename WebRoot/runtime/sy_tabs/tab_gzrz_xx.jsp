
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
       String zdbm  = (String)session.getAttribute("zdbm");
       String userid = (String)session.getAttribute("userid");
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
		  setDefaultTime1("txsj");
		  $("#txsj").datetimebox('disable');
		  $("#xgsj").datetimebox('disable');
		  getDept("bm",<%=deptid%>);
		  getPerson('xm','<%=zdbm%>');
		  
		  $("#bm").combotree("setValue",'<%=zdbm%>');
		  $("#xm").combobox("setValue",'<%=userid%>');
		  $("#bm").combotree('disable');
		  $("#xm").combobox('disable');
		  
	        $('#gb').bind('click', function(){    
	        	parent.$('#myWindow').window('close');
	        }); 
	        
	        $('#tj').bind('click', function(){    
	        	tj(czlb);
	        }); 
          
	    });
	  
	  function cx(){
		  var zj = '<%=zj%>';
          $.ajax({  
              type: 'POST',
              url:'servlet/OaAction?method=cx_gzrz&zj='+zj,
              dataType: "json",
              success: function(data){
            	  alert
            	  $("#bm").combotree('disable');
            	  $("#xm").combobox({ disabled: true });
            	  $("#bm").combotree('setValue',data[0].ssbm);
            	  $("#xm").combobox('setValue',data[0].txr);
            	  $("#txsj").datetimebox('setValue',data[0].txsj);
    			  if(data[0].xgsj==null){
    				  setDefaultTime1("xgsj");
    			  }else{
    				  $("#xgsj").datetimebox('setValue',data[0].xgsj);
    			  }
            	  
            	  $("#rzbt").textbox('setValue',data[0].rzbt);
            	  $("#rznr").textbox('setValue',data[0].rznr);
              }
          });
	  }
	  
	  function tj(czlb){
		  var zj = '<%=zj%>';
		  var ssbm = $("#bm").combotree('getValue');
		  var txr = $("#xm").combobox('getValue');
		  var txsj = $("#txsj").datetimebox('getValue');
		  var xgsj = $("#xgsj").datetimebox('getValue');
		  var rzbt = $("#rzbt").val();
		  var rznr = $("#rznr").val();
		  var args={'zj':zj,'ssbm':ssbm,'txr':txr,'txsj':txsj,'xgsj':xgsj};
		  var method = 'xx_upd_gzrz';
		  if(czlb=='add'){
			  method='xx_add_gzrz';
		  }
          $.ajax({  
              type: 'POST',
              url:'servlet/OaAction?method='+method+'&rzbt='+rzbt+'&rznr='+rznr,
              data: args,
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

		         <div title="工作日志信息"  id="jsgl">
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;text-align:center">所属部门</td>
		    	     	  <td><input id="bm" class="easyui-combotree" data-options="valueField:'id',panelHeight:'150',textField:'text'"  name="bm" style="width:260px;"/>  
		    	     	  </td>
		    	     	  <td style="width:100px;">姓名</td>
		    	     	  <td><input id="xm" class="easyui-combobox" name="xm" style="width:260px;"  data-options="panelHeight:'150'" /></td>

		    	     	</tr>
		    	     	 <tr>
		    	     	  <td style="width:100px;">日志标题</td>
		    	     	  <td colspan=3><input id="rzbt" class="easyui-textbox" name="rzbt" required=true style="width:685px;"/></td>
		    	     	</tr>
		    	     	<tr>
		    	     	  <td style="width:100px;">日志内容</td>
		    	     	  <td colspan=3><input id="rznr" class="easyui-textbox" name="rznr" required=true data-options="multiline:true" style="width:685px;height:120px;"/></td>
		    	     	</tr>
		    	     	 <tr>
		    	     	  <td style="width:100px;text-align:center">填写时间</td>
		    	     	  <td><input id="txsj"  class="easyui-datetimebox" style="width:260px;"></input></td>
		    	     	  <td style="width:100px;text-align:center">修改时间</td>
		    	     	  <td><input id="xgsj"  class="easyui-datetimebox" style="width:260px;"></input></td>
		    	     	  
		    	     	</tr>
		    	     	
		    	     </table>
		    	  </form>
		    </div> 
			<div region="south" border="false" style="text-align:center;padding:13px 0px;">
             <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" id="tj" >提交</a>
             <!--a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="gb">关闭</a>  -->
            </div>
</body>  
</html> 