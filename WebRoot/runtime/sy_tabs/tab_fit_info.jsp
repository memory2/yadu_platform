
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
<title>��Ա��Ϣ����</title> 
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
	  //�ȳ�ʼ������
     $(function(){ 
     	$("#resetForm").click(function(){
	    	$("#ff").form("clear");
	    })
		   //�����������   
          $("#packAge").combobox({
                panelHeight: "auto",
                url:"servlet/FitAction?method=queryResult&parentId=0",
                editable: false,
                valueField: "id",
                textField: "name",
                editable : true,
				onLoadSuccess: function () {
	            //������ɺ�,����ѡ�е�һ��
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "id") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       		 },
	        onChange: function () {
	            //������һ���б�
	            $("#cateCory").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
	 
	        }
        });
        //����������
        $("#cateCory").combobox({
                panelHeight: "auto",
                editable: false,
                valueField: "id",
                textField: "name",
                editable : true,
                panelHeight:'200',
				onLoadSuccess: function () {
		            //������ɺ�,����ѡ�е�һ��
		            var val = $(this).combobox("getData");
		            for (var item in val[0]) {
		                if (item == "id") {
		                    $(this).combobox("select", val[0][item]);
		                }
		            }
        	   },
		       onChange: function () {
		            //�������б�
		            $("#name").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
		 
		       }
        });
		//�����������	
        $("#name").combobox({
            panelHeight: "auto",
            editable: false,
            valueField: "id",
            textField: "name",
            editable : true,
			onLoadSuccess: function () {
	            //������ɺ�,����ѡ�е�һ��
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "id") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       	   },
	       onChange: function () {
	            //�������б�
	            $("#size").combobox("reload", "servlet/FitAction?method=queryResult&parentId=" + $(this).combobox("getValue"));
	 
	       }
        });
        //������
        $("#size").combobox({
            panelHeight: "auto",
            editable: false,
            valueField: "id",
            textField: "name",
            editable : true,
            onLoadSuccess: function () {
	            //������ɺ�,����ѡ�е�һ��
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
	                    $.messager.alert("��ʾ","��Ϣ��д������","info"); 
	                    return false;
	                }
	            },    
	            success:function(data){  
	                    var data1 = eval("("+data+")");
	                    if(data1==0){
	                      $.messager.alert("��ʾ","����ʧ�ܣ�","warning");
	                    }
	                    if(data1==1){
	                      $.messager.alert("��ʾ","����ɹ���","info");
	                    }
	                    if(data1==2){
	                      $.messager.alert("��ʾ","����Ϣ�Ѵ��ڣ�������ѡ��¼�룡","warning");
	                    }
	                    if(data1.error){
	                      $.messager.alert("��ʾ","�������","error");
	                    }
	                  /* if(data1.success){
	                       window.location.href="indexZhu.jsp";
	                   }else if(data1.error){
	                       $.messager.alert("��ʾ","�û������������","info");
	                   }else if(data1.success1){
	                       $.messager.alert("��ʾ","��¼ͨ������","info");
	                   }else{
	                       $.messager.alert("��ʾ","��������������Ϣ","info");
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
		    	     	  <td >����:</td>
		    	     	  <td>
		    	     	  	<input id="packAge" class="easyui-combobox " name="packAge"   required="true" />
		    	     	  </td>
		    	     	<tr>
		    	     	<tr>
	    	     		  <td >���:</td>
	    	     	 	  <td>
	    	     	  		<input id="cateCory" class="easyui-combobox " name="cateCory" required="true"  />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >����:</td>
	    	     	 	  <td>
	    	     	  		<input id="name" class="easyui-combobox " name="name"  required="true" />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >���:</td>
	    	     	 	  <td>
	    	     	  		<input id="size" class="easyui-combobox " name="size" required="true"  />
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >����:</td>
	    	     	 	  <td>
	    	     	 	  	<input id="amount" name="amount" class="easyui-numberspinner " required="true" value="0"  min="0" data-options="increment:1" style="width:100px;"></input>��
	    	     	  	  </td>
		    	     	</tr>
		    	     	<tr>
	    	     		  <td >��ע:</td>
	    	     	 	  <td>
	    	     	  		<input id="remark" name="remark" class="easyui-textbox " name="message" required="true" data-options="multiline:true" style="height:60px"></input>
	    	     	  	  </td>
		    	     	</tr>
		    	     	
		    	     	<tr>
		    	     		<td></td>
							<td >
								<!-- <input type="submit" value="Submit" ></input> -->
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="addForm" >�ύ</a>
								<a href="javascript:void(0)" id="resetForm" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="width:80px">����</a>
							</td>
						</tr>
		    	     </table>
		    	  </form>
		    	</div>
				
		    </div> 
</body>  
</html> 