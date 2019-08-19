
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
<title>人员信息管理</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">

	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<%
   		String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
    %>
	<script type="text/javascript">
	  //先初始化对象
     $(function(){ 
     	$("#resetForm").click(function(){
	    	$("#ff").form("clear");
	    })
		   //设置配件包名   
          $("#packAge").combobox({
                panelHeight: "auto",
                url:"servlet/FitAction?method=queryResult&parentId=0",
                editable: false,
                valueField: "id",
                textField: "name",
                editable : true,
				onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "id") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       		 },
	        onChange: function () {
	            //更新下一级列表
	            $("#cateCory").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
	 
	        }
        });
        //设置配件类别
        $("#cateCory").combobox({
                panelHeight: "auto",
                editable: false,
                valueField: "id",
                textField: "name",
                editable : true,
                panelHeight:'200',
				onLoadSuccess: function () {
		            //加载完成后,设置选中第一项
		            var val = $(this).combobox("getData");
		            for (var item in val[0]) {
		                if (item == "id") {
		                    $(this).combobox("select", val[0][item]);
		                }
		            }
        	   },
		       onChange: function () {
		            //更新市列表
		            $("#name").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
		 
		       }
        });
		//设置配件名称	
        $("#name").combobox({
            panelHeight: "auto",
            editable: false,
            valueField: "id",
            textField: "name",
            editable : true,
			onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "id") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       	   },
	       onChange: function () {
	            //更新市列表
	            $("#size").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
	 
	       }
        });
        //配件规格
        $("#size").combobox({
            panelHeight: "auto",
            editable: false,
            valueField: "id",
            textField: "name",
            editable : true,
            onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "id") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       	   }
        });
        
        
        
        $("#addForm").click(function(){
	        $("#ff").form("submit",{
	            url:"servlet/FitAction?method=insertProduct",
	            onSubmit: function(){
	                var s= $("#ff").form("validate");
	                if(s){
	                    return true;
	                }else{
	                    $.messager.alert("提示","信息填写不完整","info"); 
	                    return false;
	                }
	            },    
	            success:function(data){  
	                    var data1 = eval("("+data+")");
	                    if(data1==0){
	                      $.messager.alert("提示","保存失败！","warning");
	                    }
	                    if(data1==1){
	                      $.messager.alert("提示","保存成功！","info");
	                    }
	                    if(data1==2){
	                      $.messager.alert("提示","该信息已存在，请重新选择录入！","warning");
	                    }
	                    if(data1.error){
	                      $.messager.alert("提示","保存错误","error");
	                    }
	                  /* if(data1.success){
	                       window.location.href="indexZhu.jsp";
	                   }else if(data1.error){
	                       $.messager.alert("提示","用户名或密码错误","info");
	                   }else if(data1.success1){
	                       $.messager.alert("提示","登录通道错误！","info");
	                   }else{
	                       $.messager.alert("提示","请输入完整的信息","info");
	                   } */
	            }
	        });
    	});
        
    });
	  
		
	</script>
</head> 
<body>
		       <div  style="padding:10px;" id="dh">		    	
		    	<div>
		    	  <form id="ff" method="post" >
		    	     <table style="margin: 0 auto">
		    	     	<tr>
		    	     	  <td >包名:</td>
		    	     	  <td>
		    	     	  	<input id="packAge" class="easyui-combobox " name="packAge"   required="true" />
		    	     	  </td>
		    	     	<tr>
		    	     	<tr>
	    	     		  <td >类别:</td>
	    	     	 	  <td>
	    	     	  		<input id="cateCory" class="easyui-combobox " name="cateCory" required="true"  />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >名称:</td>
	    	     	 	  <td>
	    	     	  		<input id="name" class="easyui-combobox " name="name"  required="true" />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >规格:</td>
	    	     	 	  <td>
	    	     	  		<input id="size" class="easyui-combobox " name="size" required="true"  />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >数量:</td>
	    	     	 	  <td>
	    	     	 	  	<input id="amount" name="amount" class="easyui-numberspinner " required="true" value="0"  min="0" data-options="increment:1" style="width:100px;"></input>个
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >备注:</td>
	    	     	 	  <td>
	    	     	  		<input id="remark" name="remark" class="easyui-textbox " name="message" required="true" data-options="multiline:true" style="height:60px"></input>
	    	     	  	  </td>
		    	     	</tr>
		    	     	
		    	     	<tr>
		    	     		<td></td>
							<td >
								<!-- <input type="submit" value="Submit" ></input> -->
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="addForm" >提交</a>
								<a href="javascript:void(0)" id="resetForm" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:80px">重置</a>
							</td>
						</tr>
		    	     </table>
		    	  </form>
		    	</div>
				
		    </div> 
</body>  
</html> 