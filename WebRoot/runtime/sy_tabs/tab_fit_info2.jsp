<%@ page language="java" contentType="text/html; charset=UTF-8" 
pageEncoding="UTF-8"%> 
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
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/demo.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/datagrid-export.js"></script> 
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<%
   		String xm = (String)session.getAttribute("oaryxm")==null?"":(String)session.getAttribute("oaryxm");
    %>
	<script type="text/javascript">
	   var aa=[];
	   var bb=[];
	   var cc=[];
	   var dd=[];
	   
	   $(function(){
	   	     //设置配件包名   
         /*  $("#packAge").combobox({
                panelHeight: "auto",
                url:"servlet/FitAction?method=queryResult&parentId=0",
                editable: false,
                valueField: "productid",
                textField: "proname",
                editable : true,
				onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "productid") {
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
                valueField: "productid",
                textField: "proname",
                editable : true,
                panelHeight:'200',
				onLoadSuccess: function () {
		            //加载完成后,设置选中第一项
		            var val = $(this).combobox("getData");
		            for (var item in val[0]) {
		                if (item == "productid") {
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
            valueField: "productid",
            textField: "proname",
            editable : true,
			onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "productid") {
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
            valueField: "productid",
            textField: "proname",
            editable : true,
            onLoadSuccess: function () {
	            //加载完成后,设置选中第一项
	            var val = $(this).combobox("getData");
	            for (var item in val[0]) {
	                if (item == "productid") {
	                    $(this).combobox("select", val[0][item]);
	                }
	            }
       	   }
        }); */
	   		
	   		function synchroAjaxByUrl(url){
				var temp;
				$.ajax({
				url:url,
				type:"get",
				async:false,
				dataType:"json",
				success:function(data){
				temp = data;
				}
				});
				return temp;
			}

	   		
		   $("#tt").datagrid({   
		    url:"servlet/FitAction?method=searchProduct",   
		    title:"配件信息列表",
		    toolbar:"#tb",
		    striped: true,       //交替条纹
		    rownumbers : true,	 //显示行数
		    loadMsg: '正在拼命加载,请稍后...',	
		    fit : true,//使表格自适应    
			fitColumns : true,
			singleSelect : true,
			onClickCell:onClickCell,//双击启动编辑
			onEndEdit: onEndEdit,
			pagination : true,
			height : 400,
			pageSize : 10, //默认显示几条数据
			pageList : [ 5, 10, 15 ], //制定显示几条
	        columns:[[   
	          {field:"id",title:"id",width:100,checkbox:true},
	          {field:"productNo",title:"编号",width:200},
	          {field:"packName",title:"包名",width:200,
	          		 formatter:function(value,rowData,rowIndex){
						    for(var i=0; i<aa.length; i++){
							    if(aa[i].productid==value){
								    return aa[i].proname;
							    }
						    }
                            return value;
					},
					editor:{
						type:'combobox',
						options:{
							data:synchroAjaxByUrl("servlet/FitAction?method=queryResult&parentId=0"),
							valueField:"productid",
							textField:"proname",
							required:true,
							onSelect:function(data){
								var row = $("#tt").datagrid("getSelected");
								var rowIndex = $("#tt").datagrid("getRowIndex",row);//获取行号
								var thisTarget = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"packName"}).target;
								var value = thisTarget.combobox("getValue");
								var target = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"cateName"}).target;
								target.combobox("clear"); //清除原来的数据
								var url = "servlet/FitAction?method=queryResult&parentId="+value;
								target.combobox("reload", url);//联动下拉列表重载
							},
							onLoadSuccess:function(data,value){
							    aa=$(this).combobox("getData");
							}
						}
					}
	          
	           },
	         
	          {field:"cateName",title:"类别",width:200,
	          	 formatter:function(value,rowData,rowIndex){
				    for(var i=0; i<bb.length; i++){
					    if(bb[i].productid==value){
						    return bb[i].proname;
					    }
				    }
                          return value;
				},
				editor:{
					type:'combobox',
					options:{
						data:synchroAjaxByUrl("servlet/FitAction?method=queryResult"),
						valueField:"productid",
						textField:"proname",
						required:true,
						onSelect:function(data){
							var row = $("#tt").datagrid("getSelected");
							var rowIndex = $("#tt").datagrid("getRowIndex",row);//获取行号
							var thisTarget = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"cateName"}).target;
							var value = thisTarget.combobox("getValue");
							var target = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"productName"}).target;
							target.combobox("clear"); //清除原来的数据
							var url = "servlet/FitAction?method=queryResult&parentId="+value;
							target.combobox("reload", url);//联动下拉列表重载
						},
						onLoadSuccess:function(data,value){
							    bb=$(this).combobox("getData");
						}
					}
				}
	          
	          },
	          {field:"productName",title:"产品名称",width:200,
	          	formatter:function(value,rowData,rowIndex){
				    for(var i=0; i<cc.length; i++){
					    if(cc[i].productid==value){
						    return cc[i].proname;
					    }
				    }
                          return value;
				},
				editor:{
					type:'combobox',
					options:{
						data:synchroAjaxByUrl("servlet/FitAction?method=queryResult"),
						valueField:"productid",
						textField:"proname",
						required:true,
						onSelect:function(data){
							var row = $("#tt").datagrid("getSelected");
							var rowIndex = $("#tt").datagrid("getRowIndex",row);//获取行号
							var thisTarget = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"productName"}).target;
							var value = thisTarget.combobox("getValue");
							var target = $("#tt").datagrid("getEditor", {"index":rowIndex,"field":"sizeName"}).target;
							target.combobox("clear"); //清除原来的数据
							var url = "servlet/FitAction?method=queryResult&parentId="+value;
							target.combobox("reload", url);//联动下拉列表重载
						},
						onLoadSuccess:function(data,value){
							    cc=$(this).combobox("getData");
						}
					}
				}
	          },
	          {field:"sizeName",title:"规格",width:200,
	            formatter:function(value,rowData,rowIndex){
				    for(var i=0; i<dd.length; i++){
					    if(dd[i].productid==value){
						    return dd[i].proname;
					    }
				    }
                          return value;
				},
				editor:{
					type:'combobox',
					options:{
						data:synchroAjaxByUrl("servlet/FitAction?method=queryResult"),
						valueField:"productid",
						textField:"proname",
						required:true,
						onLoadSuccess:function(data,value){
							    dd=$(this).combobox("getData");
						}
					}
				}
	          },
	          {field:"amount",title:"数量",width:200,
	           editor:{
	            	type:"numberbox",
	            	options: {
               			required:true,
               			min:1
            		}
	             } 
	          },
	          {field:"remark",title:"备注",width:300,editor:"textbox"}
		          
	       ]]
	       		
	  		});  
	       /*  $('#tt').datagrid('hideColumn', 'id'); */
	       
	        var p = $('#tt').datagrid('getPager');   
	        $(p).pagination({   
	            pageSize: 10,//每页显示的记录条数，默认为10   
	            pageList: [5,10,15,1000],//可以设置每页记录条数的列表   
	            beforePageText: '第',//页数文本框前显示的汉字   
	            afterPageText: '页    共 {pages} 页',   
	            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',   
	        }); 
	        //搜索框
	        $("#doSearch").bind("click", function(){
				$("#tt").datagrid("load",{
					/* packAge: $("#packAge").combobox("getValue"),
					cateCory:$("#cateCory").combobox("getValue"),
					name: $("#name").combobox("getValue"),
					size: $("#size").combobox("getValue") */
					productNo:$("#productNo").textbox('getValue')
				}); 
		    });
			 //删除
	        $("#delFun").bind("click",function(){
	        		// 得到选中的行
					var selRow =  $("#tt").datagrid("getSelections");//返回选中多行
					if(selRow.length==0){
						$.messager.alert('提示', '请至少选择一行数据!',"warning");
						return false;
					}
					var ids=[];
					for (var i = 0; i < selRow.length; i++) {
		               //获取自定义table 的中的checkbox值
		               var id=selRow[i].id;   //OTRECORDID这个是你要在列表中取的单个id 
		               ids.push(id); //然后把单个id循环放到ids的数组中
					}
					 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
						if (r){
							$.ajax({  
							      type:'POST',
							      url:"servlet/FitAction?method=delProduct",
							      data:{"array[]":ids},
							      success: function(data){
							       	  var data = eval("("+data+")");
								      if('3'==data){
								      	$.messager.alert('提示', '删除成功!',"info");
										$("#tt").datagrid("reload");
								      }else{
								      	$.messager.alert('提示', '删除失败,请联系管理员！',"error");
								      }
							     }
							 }); 
						}
					}); 
					 
	        });		   
		    
		   
	    });       
		//编辑的行
		var editIndex = undefined;
        function endEditing() {
            if (editIndex == undefined){return true}
            $('#tt').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        }
	  	//点击某一行
	  	function onClickCell(index, field){
            if (editIndex != index) {
                if (endEditing()) {
                    $('#tt').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    var ed = $('#tt').datagrid('getEditor', { index: index, field: field });
                    if (ed) {
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    editIndex = index;
                } else {
                    setTimeout(function () {
                        $('#tt').datagrid('selectRow', editIndex);
                    }, 0);
                }
            }
        }
        
        function onEndEdit(index, row){
            var ed = $(this).datagrid('getEditor', {index:editIndex,field:'packName'});
			var cn= $(this).datagrid('getEditor', {index:editIndex,field:'cateName'}); 
			
			var pn= $(this).datagrid('getEditor', {index:editIndex,field:'productName'}); 
			var sn=$(this).datagrid('getEditor',{index:editIndex,field:'sizeName' });
			if(row.id!=null&&row.id!=undefined&&row.id!=""){
				$.ajax({
				url:"servlet/FitAction?method=findSearchProductById&packAge="+row.id,
				type:"post",
				async:false,
				dataType:"json",
				success:function(data){
					if(ed!=null){
						//row.packName = $(ed.target).combobox('getValue');
						row.packName =data[0].packAge;
					}
					if(cn!=null){
						row.cateName= data[0].cateGory;
					}
					if(pn!=null){
						row.productName = data[0].name;
					}
					if(sn!=null){
						row.sizeName= data[0].size;
					}
				}
				});
			}else{
				if(ed!=null){
					row.packName = $(ed.target).combobox('getValue');
				}
				if(cn!=null){
					row.cateName= $(cn.target).combobox('getValue');
				}
				if(pn!=null){
					row.productName = $(pn.target).combobox('getValue');
				}
				if(sn!=null){
					row.sizeName= $(sn.target).combobox('getValue');
				}
			
			}
            
        }
	  	function append(){
            var index = $('#tt').datagrid('getRowIndex', $('#tt').datagrid('getSelected'));
            if (index == -1)
                index = 0;
            $("#tt").datagrid("insertRow", {
                index: index+1,
                row: {}
                });
        }
	  	
	  	
		function removeit(){
			if (editIndex == undefined){return}
			$('#tt').datagrid('cancelEdit', editIndex)
					.datagrid('deleteRow', editIndex);
			editIndex = undefined;
		}
		//保存
		function accept(){
            if (endEditing()){
                var $dg = $('#tt');
                var rows = $dg.datagrid('getChanges');
                if (rows.length) {
                    var inserted = $dg.datagrid('getChanges', "inserted");
                    var deleted = $dg.datagrid('getChanges', "deleted");
                    var updated = $dg.datagrid('getChanges', "updated");
                    var effectRow = new Object();
                    if (inserted.length) {
                        effectRow["inserted"] = JSON.stringify(inserted);
                    }
                    if (deleted.length) {
                        effectRow["deleted"] = JSON.stringify(deleted);
                    }
                    if (updated.length) {
                        effectRow["updated"] = JSON.stringify(updated);
                    }
                    console.info("effectRow值是"+effectRow["inserted"]);
                    $.post("servlet/FitAction?method=checkInsertAndUpdateProduct", effectRow, function(data) {
                    	console.info("data值是"+data+"===="+data[0]);
	                     if(data[0].status==2){
	                     	 $.messager.prompt('编号', '请输入产品编号', function(r){
								if (r){
									 effectRow["productNo"] = JSON.stringify(r);
									 $.post("servlet/FitAction?method=insertAndUpdateProduct", effectRow, function(data1) {
									 	var data1 = eval('(' + data1 + ')');
									 	 $.messager.alert("提示", data1[0].msg,"info");
									 	 $('#tt').datagrid('acceptChanges');
									 	 $("#tt").datagrid("reload");
									 })
								}
							});
	                     }else{
	                     	 $.messager.alert("提示", data[0].msg,"info");
	                     	 $('#tt').datagrid('acceptChanges');
	                     	 $("#tt").datagrid("reload");
	                     }
	                    
		             }, "JSON").error(function() {
		                 $.messager.alert("提示", "提交错误！");
		             }); 
                }
            }
      	 
            
        }
        
		function reject(){
			$('#tt').datagrid('rejectChanges');
			editIndex = undefined;
		}
		function getChanges(){
			var rows = $('#tt').datagrid('getChanges');
			alert(rows.length+' rows are changed!');
		}
	   function refresh(){
	   	 $("#tt").datagrid("reload");
	   }
	  
	  function ExporterExcel(){
	 	 var getTimestamp=new Date().getTime();
	  	 $('#tt').datagrid('toExcel','配件信息列表'+getTimestamp+'.xls');	// export to excel
	  }
	  function printExcel(){
	  	$('#tt').datagrid('print', '配件信息列表');
	  }
	  
	  function importData(){
	  	$('#importExcel').dialog('open');

	  }
	  
	  
	
		/* 配置导入框 */
		
	
	  
	</script>
 	 <style>
	 	#cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
	 </style>
</head> 
<body>
	 <table id="tt"></table>  
	 <div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" id="addFun"  class="easyui-linkbutton" iconCls="icon-add"    plain="true" onclick="append()">添加</a>
			<a href="javascript:void(0)" id="delFun"  class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" id="saveFun" class="easyui-linkbutton" iconCls="icon-save"   plain="true" onclick="accept()">保存</a>
			<!-- <a href="javascript:void(0)" id="saveFun" class="easyui-linkbutton" iconCls="icon-redo"   plain="true" onclick="importData()">导入</a> -->
			<a href="javascript:void(0)" id="saveFun" class="easyui-linkbutton" iconCls="icon-dload"   plain="true" onclick="ExporterExcel()">导出</a>
			<a href="javascript:void(0)" id="saveFun" class="easyui-linkbutton" iconCls="icon-print"   plain="true" onclick="printExcel()">打印</a>
			
			<a href="javascript:void(0)" class="easyui-linkbutton" id="reject"  iconCls="icon-undo"  plain="true" onclick="reject()" >撤销</a> 
			<a href="javascript:void(0)" id="refreshFun" class="easyui-linkbutton" iconCls="icon-reload"   plain="true"  onclick="refresh()">刷新</a>
			
			<!-- <a href="javascript:void(0)" id="editFun" class="easyui-linkbutton" iconCls="icon-edit"   plain="true">编辑</a> -->
		</div>
		<div>
		          编号: <input id="productNo" class="easyui-textbox" name="productNo"/>
		        <!--    包名:<input id="packAge" class="easyui-combobox " name="packAge"    />
		           类别:<input id="cateCory" class="easyui-combobox " name="cateCory"   />
		           名称:<input id="name" class="easyui-combobox " name="name"   />
		           规格:<input id="size" class="easyui-combobox " name="size"   /> -->
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" id="doSearch">Search</a>
		</div>
	</div>
	
	
	<!-- 配置导入框 -->
	<div id="importExcel"  closed="true" class="easyui-dialog" title="导入excel文件"
		style="width: 400px; height: 200px;" data-options="modal:true">
		<form id="uploadExcel"  method="post" enctype="multipart/form-data" style="padding:10px;">  
   			选择文件：　<input id = "excel" name = "excel" class="easyui-filebox" style="width:200px" data-options="prompt:'请选择文件...'">  
		</form>  
		<div style="text-align: center; padding: 5px 0;">
			<a  href="javascript:void(0)" id="booten" class="easyui-linkbutton"  plain="true" οnclick="uploadExcel1()" style="width: 80px" >导入</a>
		</div>
	</div>
	
	 
	
	
</body>  
</html> 