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
<meta name="viewport" content="width=device-width,inital-scale=1.0,minimum-scale=0.5,maximum-scale=2.0,user-scalable=no"> 
<title>亚都通讯录</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>

	
	
	<script type="text/javascript">
	  $(function(){
		   var isPc = IsPC();
	        if(isPc){
	        	$("#ts").text("友情提醒：1、姓名支持模糊查询，如输入“刘”，可查询所有刘姓的人员。");
	        }else{
	        	$("#ts").text("友情提醒：1、姓名支持模糊查询，如输入“刘”，可查询所有刘姓的人员。2、在查询结果中点击号码可直接拨打电话。");
	        }
	        
	        $('#btn_search_ydtx').bind('click', function(){    
		        cx(isPc);
	        }); 
	        

	    });
	  
	  function IsPC() {
		    var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone",
		                "SymbianOS", "Windows Phone",
		                "iPad", "iPod"];
		    var flag = true;
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		            flag = false;
		            break;
		        }
		    }
		    return flag;
		}
	  
	  function cx(isPc){
		  var lxr = $("#lxr").val();
		  var url = 'servlet/KqAction?method=sy_ydtx_wx&lxr='+lxr;
          var gridID = "dg_ydtx_wx";
          var gridBT = "亚都通讯";
          //getGird(url,gridID,gridBT); 
          if(isPc){
        	  Lodedata_pc(url);
          }else{
        	  Lodedata(url); 
          }
          
	  }
	  
	  
	  
	  /**数据绑定加载**/
      function Lodedata(url) {
          $('#dg_ydtx_wx').datagrid({
              url: url, //数据加载路径
              pagination: true, //是否显示分页控件
              fitColumns: true, //自适应宽度
              singleSelect: true, //单选
              nowrap: true,//数据长度超出列宽时将会自动截取。
              columns: [
                        [
                            
                            {field:'dw', title: '部门', align: 'center'},
                            {field:'xm', title: '姓名', align: 'center'},
                            {
                                field:'sjhm', title: '手机号码', align: 'center', 
                                //value 当前列值   row 当前行    index 当前行数
                                formatter: function (value, row, index) {
                                    return "<a href='javascript:phone("+value+")' class='easyui-tooltip'   title=手机号码" + value + ">" + value + "</a>";
                                }
                            },
                            {
                                field:'dh', title: '短号', align: 'center',
                                //value 当前列值   row 当前行    index 当前行数
                                formatter: function (value, row, index) {
                                    return "<a href='javascript:phone("+value+")' class='easyui-tooltip'   title=短号" + value + ">" + value + "</a>";
                                }
                            },
                            {field:'gh', title: '固话', align: 'center'},
                            {field:'fjh', title: '办公室', align: 'center'}
                        ]
                    ]
          });
          
          var p = $('#dg_ydtx_wx').datagrid('getPager');   
	        $(p).pagination({   
	            pageSize: 10,//每页显示的记录条数，默认为10   
	            pageList: [5,10,20],//可以设置每页记录条数的列表   
	            beforePageText: '第',//页数文本框前显示的汉字   
	            afterPageText: '页    共 {pages} 页',   
	            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
	        }); 
      }
	  
	  /**数据绑定加载**/
      function Lodedata_pc(url) {
          $('#dg_ydtx_wx').datagrid({
              url: url, //数据加载路径
              pagination: true, //是否显示分页控件
              fitColumns: true, //自适应宽度
              singleSelect: true, //单选
              nowrap: true,//数据长度超出列宽时将会自动截取。
              columns: [
                        [
                            
                            {field:'dw', title: '部门', align: 'center',width:150},
                            {field:'xm', title: '姓名', align: 'center',width:150},
                            {field:'sjhm', title: '手机号码', align: 'center',width:200},
                            {field:'dh', title: '短号', align: 'center',width:150},
                            {field:'gh', title: '固话', align: 'center',width:150},
                            {field:'fjh', title: '办公室', align: 'center',width:150}
                        ]
                    ]
          });
          
          var p = $('#dg_ydtx_wx').datagrid('getPager');   
	        $(p).pagination({   
	            pageSize: 10,//每页显示的记录条数，默认为10   
	            pageList: [5,10,20],//可以设置每页记录条数的列表   
	            beforePageText: '第',//页数文本框前显示的汉字   
	            afterPageText: '页    共 {pages} 页',   
	            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',   
	        }); 
      }
	  
	  
      function phone(date){
    	  window.location.href = 'tel:' + date;
    	  }
	</script>
 	 <style>
	 #cxtj tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;}
	#ts{font-size:12px;color:red}
	#bt {color:#CC5522;font-size:24px;font-weight:bold;margin:15px;text-align:center;}
	 </style>
</head> 
<body>


		         <div title="亚都通讯" style="padding:10px;" >	
		         <p id="bt">亚都通讯录查询</p>	    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">姓名</td>
		    	     	  <td><input id="lxr" class="easyui-textbox" name="lxr"  style="width:150px; height:30px;"/></td>
		    	     	  <td style="width:100px;"><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
		    	     	</tr>
		    	     </table>
		    	  </form>
		    	</div> 
				<p id="ts"></p>
				<table id="dg_ydtx_wx" title="通讯录列表" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"  
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<!--  th field="id"  width=60px;  sortable=true, checkbox=true>主键ID</th>-->
						<th field="dw"   id="bm" >部门</th> 
 						<th field="xm"   id="xm">姓名</th> 
						<th field="sjhm" id="sjhm">手机号码</th> 
						<th field="dh"   id="dh">短号</th> 
						<th field="gh"   id="gh">固话</th> 
						<th field="fjh"  id="fjh">办公室</th>

						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 