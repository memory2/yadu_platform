
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
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript">
	  $(function(){
	        $('#btn_wjsc').bind('click', function(){    
	        	wjsc();
	        }); 
	    });
	  
	  function wjsc(){
		  var bm = $("#tbname").val();
		  var wjlj = $("#uploadFileid").filebox('getValue').replace('\\','/');
		  if(isEmpty(bm)){
			  alert("表名不能为空！");
			  return;
		  }
		  if(isEmpty(wjlj)){
			  alert("请选择Excel文件！");
			  return;
		  }
		  
		  var drms = $('input:radio:checked').val();
	         $.ajax({  
	              type: 'POST',
	              url:'servlet/OaAction?method=wjsc&bm='+bm+'&wjlj='+wjlj+'&drms='+drms,
	              success: function(data){
	            	  if(data=='ok'){
	            		  alert("上传成功！");
	            	  }else{
	            		  alert("上传失败！");
	            	  }
	              },error:function(){
	                 alert("系统报错，请联系管理员！");
	              }
	          });
	  }
	  

	</script>
 	 <style>
    .radioSpan {
      position: relative;
      border: 1px solid #95B8E7;
      background-color: #fff;
      vertical-align: middle;
      display: inline-block;
      overflow: hidden;
      white-space: nowrap;
      margin: 0;
      padding: 0;
      -moz-border-radius: 5px 5px 5px 5px;
      -webkit-border-radius: 5px 5px 5px 5px;
      border-radius: 5px 5px 5px 5px;
      display:block;
      height:23px;
    }
	 
	 </style>
</head> 
<body>
	<p style="color:red;">注：把选择的Excel文件内容导入到数据库对应的表(Excel中的标题头必须和表字段名一致)</p>
	<div style="margin:20px 0;"></div>
	<div style="margin-left:370px">
	<div class="easyui-panel" title="Excel数据上传" style="width:60%;padding:30px 70px 50px 70px">
		<div style="margin-bottom:20px">
			<div>表名:</div>
			<input class="easyui-textbox" style="width:100%" id="tbname" data-options="required:true">
		</div>
		<div style="margin-bottom:20px">
			<div>选择Excel文件:</div>
			<input class="easyui-filebox" name="uploadFile" id="uploadFileid" data-options="prompt:'选择文件',buttonText:'&nbsp;选&nbsp;择&nbsp;',required:true" style="width:100%">
		</div>
		<div style="margin-bottom:20px">
	        <tr>
		        <td style="text-align:right;">导入模式:</td>
		        <td style="text-align:left">
		            <span class="radioSpan">
		                <label><input type="radio" name="drms" value="1" checked="checked" id="ms1">添加记录到目标表</input></label>
		                <label style="margin-left:15px;"><input type="radio" name="drms" value="2" id="ms2">删除目标表全部记录，并从Excel重新导入</input></label>
		            </span>
		        </td>
	       </tr>
		</div>
		
		<div>
			<!--  a href="#" class="easyui-linkbutton" style="width:100%" id="btn_wjsc">上传</a>-->
			<a id="btn_wjsc" class="easyui-linkbutton" data-options="iconCls:'icon-save'">开始上传</a>
		</div>
	</div>
	</div>
</body>  
</html> 