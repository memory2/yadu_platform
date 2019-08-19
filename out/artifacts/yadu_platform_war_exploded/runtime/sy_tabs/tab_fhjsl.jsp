
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
<title>U8发货及时率统计</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/ydCommon.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script> 
	<script type="text/javascript">
	  $(function(){
		    sjsz('sqksrq','sqjsrq');
	        $('#btn_search_wfh').bind('click', function(){    
	            cx('wfh');
	        }); 
	        
	        $('#btn_search_cfh').bind('click', function(){    
	            cx('cfh');
	        }); 
	        
	        $('#btn_search_jsl').bind('click', function(){    
	            jsltj();
	        }); 
	        $('#btn_search_jsltj').bind('click', function(){    
	        	method1('fhjsltj');
	        }); 
	    });
	  
	  function cx(cxbs){
		  $("#fhjsl").show();
		  $("#fhtj").hide();
		  $("#btn_search_jsltj").hide();
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
		  //alert(sqksrq+"--"+jbzgs);
          var url = 'servlet/KqAction?method=u8_fhjsl&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&cxbs='+cxbs;
          var gridID = "dg";
          var gridBT = "发货及时率";
		  getDataFhjsl(url,gridID,gridBT);
 
	  }
	  
	  function getDataFhjsl(url,gridID,gridBT){
	       $('#'+gridID).datagrid({    
	            url:url,
	            pagination:false,
	        	toolbar: [
	        	  {
	        		text:'导出',
	        		iconCls: 'icon-dload',
	        		handler: function(){
	        			ExporterExcel(gridID,gridBT);
	        			}
	        	}
	        	]
	        });  
			  
	        var p1 = $('#'+gridID).datagrid('getPager');   
	        $(p1).pagination({   
	            total: 10,  
	        }); 
	  }
	  
	  function sjsz(ksrq,jsrq){
		  var myDate = new Date();
		  var y = myDate.getFullYear();
		  var m = myDate.getMonth();
		  var d = new Date(y,m,0).getDate();
		  $("#"+ksrq).datebox('setValue',y+"-"+m+"-01");
		  $("#"+jsrq).datebox('setValue',y+"-"+m+"-"+d);
	  }
	  
	  function jsltj(){
		  $("#fhjsl").hide();
		  $("#fhtj").show();
		  $("#btn_search_jsltj").show();
		  var sqksrq = $("#sqksrq").datebox('getValue');
		  var sqjsrq = $("#sqjsrq").datebox('getValue'); 
          var url = 'servlet/KqAction?method=u8tj_fhjsl&sqksrq='+sqksrq+'&sqjsrq='+sqjsrq+'&cxbs=jsl';
          $.ajax({  
              type: 'POST',
              dataType: "json",
              url:url,
              success: function(data){
            	      //alert(JSON.stringify(data));
            		  var jsonData  = JSON.stringify(data[0]);
            		  //alert(jsonData);
            		  //$("#dg_kqxx").html(JSON.stringify(data));
            		   if(data.length>0){
            		  var wfhzs = 0;
            		  var cfhzs = 0;
            		  var fhjsl = 0;
            		  var zs = 0;
            		  for(var key in data){	
            			  var fhtj = {};
            			  fhtj = data[key];
            			  wfhzs = fhtj['wfh'];
            			  cfhzs = fhtj['cfh'];
            			  zs = fhtj['zs'];
                		  fhjsl = (1-(wfhzs+cfhzs)/zs)*100;
                		  
            			  
            			}
            		  $("#fhjsl").hide();
            		  var bz ="<table id='fhjsltj'><tr><td colspan=6 id='tjsj'>统计时间："+getNowFormatDate()+"</td></tr><tr><td class='nr_bt'>制单开始日期</td><td class='nr_bt'>制单结束日期</td><td class='nr_bt'>未发货总数</td><td class='nr_bt'>超期发货总数</td><td class='nr_bt'>总数</td><td  class='nr_bt'>发货及时率</td></tr><tr><td>"+
            		  sqksrq+"</td><td>"+sqjsrq+"</td><td>"+wfhzs+"</td><td>"+cfhzs+"</td><td>"+zs+"</td><td>"+fhjsl.toFixed(2)+"%</td></tr><tr><td colspan=6>发货及时率公式：(1-(未发货总数+超期发货总数)/总数)*100</td></tr></table>";
            		  $("#fhtj").html(bz);
            		  
            		   
            		   }
	            		  
          }
          
          });
 
	  }
	  
      function method1(tableid) {
          var curTbl = document.getElementById(tableid);
          var oXL = new ActiveXObject("Excel.Application");
          var oWB = oXL.Workbooks.Add();
          var oSheet = oWB.ActiveSheet;
          var sel = document.body.createTextRange();
          sel.moveToElementText(curTbl);
          sel.select();
          sel.execCommand("Copy");
          oSheet.Paste();
          oXL.Visible = true;

      }
      
      
      function getNowFormatDate() {
    	    var date = new Date();
    	    var seperator1 = "-";
    	    var seperator2 = ":";
    	    var month = date.getMonth() + 1;
    	    var strDate = date.getDate();
    	    if (month >= 1 && month <= 9) {
    	        month = "0" + month;
    	    }
    	    if (strDate >= 0 && strDate <= 9) {
    	        strDate = "0" + strDate;
    	    }
    	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
    	            + " " + date.getHours() + seperator2 + date.getMinutes()
    	            + seperator2 + date.getSeconds();
    	    return currentdate;
    	}

	</script>
 	 <style>
	 tr td{border:solid #add9c0; border-width:1px 1px 1px 1px; padding:3px 0px;text-align:center;font-size:14px;}
	 #fhtj{width:100%;}
	 #zbt{font-size:20px;font-weight:bold;text-align:center;margin:5px;}
	 #jbxxForm{margin-top:20px;}
	 #tjsj{text-align:left;padding-left:10px;}
	 #fhjsltj{width:100%}
	 </style>
</head> 
<body>

		<div title="发货及时率" style="padding:10px;" >		
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:200px;text-align:center">制单日期</td>
		    	     	  <td style="width:450px;text-align:center"><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
		    	     	  <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
		    	     	  </td>
		    	     	  <td style="width:250px;"><a id="btn_search_wfh" class="easyui-linkbutton" data-options="iconCls:'icon-search'">未发货查询</a></td>
		    	     	  <td style="width:250px;"><a id="btn_search_cfh" class="easyui-linkbutton" data-options="iconCls:'icon-search'">超期发货查询</a></td>
		    	     	  <td style="width:350px;"><a id="btn_search_jsl" class="easyui-linkbutton" data-options="iconCls:'icon-search'">及时率统计</a>
		    	     	  <a id="btn_search_jsltj" class="easyui-linkbutton" data-options="iconCls:'icon-dload'" style="display:none;margin-left:30px;">导出统计</a>
		    	     	  </td>
		    	     	<tr>
		    	     </table>
		    	  </form>
		    	</div> <br/>  
				<div id="fhtj">
				
				</div>
				
				<div id="fhjsl">
						<table id="dg" title="发货明细列表" class="easyui-datagrid" style="width:100%;height:490px;" 
						  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"   singleSelect="true"> 
							<thead> 
								<tr> 
								<th field="zdrq"  width=120px;>制单日期</th> 
								<th field="xsthh"  width=120px;>销售提货号</th> 
								<th field="chbm"   width=120px;>存货编码</th> 
								<th field="chmc"   width=280px;>存货名称</th> 
								<th field="fhdsl"   width=120px;>发货单数量</th> 
								<th field="khjc"   width=110px;>客户简称</th> 
								<th field="xskpsj"   width=120px;>销售开票时间</th>
								<th field="ph"   width=110px;>批号</th>
								<th field="xscksj"   width=110px;>销售出库时间</th>  
								<th field="xsckdh"   width=150px;>销售出库单号</th> 
								<th field="fhts"   width=70px;>发货天数</th> 
								</tr> 
							</thead> 
						</table> 
				  </div>
				 
</div>
</body>  
</html> 