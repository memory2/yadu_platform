<%@ page language="java" contentType="text/html; charset=gb2312" 
pageEncoding="gb2312"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"> 
<title>客户信息管理</title> 
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>js/jquery-easyui-1.4.5/demo.css">
	<script type="text/javascript" charset="gbk" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" charset="gbk" src="<%=basePath%>js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript"  charset="gbk" src="<%=basePath%>js/ydCommon.js"></script>
	<script type="text/javascript" charset="gbk" src="<%=basePath%>js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<%
	   String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
	   if(xm==null||"".equals(xm)){
		   response.sendRedirect(request.getContextPath()+"/runtime/login/my_login.jsp");
	   }%>
	<script type="text/javascript">
	  $(function(){
		    //cx();
	        $('#btn_search_khgl').bind('click', function(){    
	            cx();
	        }); 


	    });
	  
	  
	  function cx(){
		  
		  var khxm = $("#khxm").val();
		  var khxb = $("#khxb").combobox('getValue');
		  var khsjh = $("#khsjh").val();
		  var glryxm = $("#glryxm").val();
		 
		  //alert(khxm+"--"+khxb);
		  var xm = '<%=xm%>';
		  var url = 'servlet/KhAction?method=sy_khjcxxcx&khxm='+khxm+'&khxb='+khxb+'&khsjh='+khsjh+'&glryxm='+glryxm;
          var gridID = "dg_khgl";
          var gridBT = "客户投诉信息";
         //alert(xm);
          
          if(xm!='辛鹏'&&xm!='系统管理员'){
        	  getDataGird(url,gridID,gridBT);  
          }else{
              $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
              var updUrl= 'runtime/sy_tabs/tab_khjcxx.jsp';
              var delUrl = 'servlet/KhAction?method=xx_del_khgl';
    		  getDataGird1(url,gridID,gridBT,updUrl,delUrl,'98%','98%');  
          }
		  
	  }
	  
	  
	  function formatOper(val,row,index){
		 // return  "<a href='javascript:xq("+row.id+")'>详情</a>";
		 return "<a  href=\"javascript:xq('" + row.id + "')\" >详情</a>";
	  }
	  
  	  function xq(zj){
  		  
          $('body').append('<div id="thewin" class="easyui-dialog" closed="true"></div>');
          var thehref =  'runtime/sy_tabs/tab_khjcxx.jsp?zj='+zj+'&czlb=xq';  
      	  $('#thewin').window({
					title : '客户详情信息',
					width : '98%',
					height : '98%',
					content : '<iframe scrolling="yes" frameborder="0"  src="'
							+ thehref
							+ '" style="width:98%;height:98%;"></iframe>',
					shadow : false,
					cache : false,
					closed : false,
					collapsible : false,
					resizable : false,
					loadingMessage : '正在加载数据，请稍等片刻......'
				});
		  
		  
  		/*$('#winxq').dialog({    
  		    title: '客户信息详情',    
  		    width: '98%',    
  		    height: '98%',    
  		    closed: false,    
  		    cache: false,    
  		    href: 'runtime/sy_tabs/tab_khjcxx.jsp?zj='+zj,    
  		    modal: true   
  		}); */
	  }


	</script>
 	 <style>
	#cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; height:30px;text-align:center;}
     #winxq tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; height:30px;text-align:center;}
	 
	 </style>
</head> 
<body>

<div style="text-align:center"><img src="images/kaifazhong.jpg"></img></div>
		         <div title="客户投诉信息" style="padding:10px;display:none" >		    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">客户姓名</td>
		    	     	  <td><input id="khxm" class="easyui-textbox" name="khxm"  style="width:150px;"/> </td>
		    	     	  </td>
		    	     	  <td style="width:100px;text-align:center">客户性别</td>
		    	     	  <td > <input id="khxb" style="width:100px;" class="easyui-combobox" value="" name='khxb' data-options=" panelHeight:'auto', valueField:'id',textField:'text',data: [{id: '0',text: '男'},{id: '1',text: '女'}]" /></td>
		    	     	  
		    	     	  <td style="width:100px;">客户手机号</td>
		    	     	  <td><input id="khsjh" class="easyui-textbox" name="khsjh"  style="width:150px;"/> </td>
		    	     	  <td style="width:100px;">关联人员姓名</td>
		    	     	<td><input id="glryxm" class="easyui-textbox" name="glryxm"  style="width:150px;"/> </td>
		    	     	</tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				   
				<a id="btn_search_khgl" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				
				<br/><br/>
				
				<table id="dg_khgl" title="客户基础信息列表" class="easyui-datagrid" style="width:100%;height:550px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"  
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<th field="id"  width=60px;  sortable=true, checkbox=true>主键ID</th>
						<th field="xm"   width=120px; >客户姓名</th> 
						<th field="xb"   width=80px; >性别</th> 
 						<th field="csrq"   width=120px; >出生日期</th> 
						<th field="jg"  width=150px;>籍贯</th> 
						<th field="sjh"  width=100px;>手机</th> 
						<th field="wx"  width=100px;>微信</th> 
						<th field="qq"  width=100px;>QQ/邮箱</th> 
						<th field="zz"  width=150px;>家庭住址</th> 
						<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
						</tr> 
					</thead> 
				</table>   
		    </div> 
		    <div id='winxq'></div>
</body>  
</html> 