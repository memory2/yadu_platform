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
<title>�Ƕ�ͨѶ¼</title> 
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>

	
	
	<script type="text/javascript">
	  $(function(){
		   var isPc = IsPC();
	        if(isPc){
	        	$("#ts").text("�������ѣ�1������֧��ģ����ѯ�������롰�������ɲ�ѯ�������յ���Ա��");
	        }else{
	        	$("#ts").text("�������ѣ�1������֧��ģ����ѯ�������롰�������ɲ�ѯ�������յ���Ա��2���ڲ�ѯ����е�������ֱ�Ӳ���绰��");
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
          var gridBT = "�Ƕ�ͨѶ";
          //getGird(url,gridID,gridBT); 
          if(isPc){
        	  Lodedata_pc(url);
          }else{
        	  Lodedata(url); 
          }
          
	  }
	  
	  
	  
	  /**���ݰ󶨼���**/
      function Lodedata(url) {
          $('#dg_ydtx_wx').datagrid({
              url: url, //���ݼ���·��
              pagination: true, //�Ƿ���ʾ��ҳ�ؼ�
              fitColumns: true, //����Ӧ���
              singleSelect: true, //��ѡ
              nowrap: true,//���ݳ��ȳ����п�ʱ�����Զ���ȡ��
              columns: [
                        [
                            
                            {field:'dw', title: '����', align: 'center'},
                            {field:'xm', title: '����', align: 'center'},
                            {
                                field:'sjhm', title: '�ֻ�����', align: 'center', 
                                //value ��ǰ��ֵ   row ��ǰ��    index ��ǰ����
                                formatter: function (value, row, index) {
                                    return "<a href='javascript:phone("+value+")' class='easyui-tooltip'   title=�ֻ�����" + value + ">" + value + "</a>";
                                }
                            },
                            {
                                field:'dh', title: '�̺�', align: 'center',
                                //value ��ǰ��ֵ   row ��ǰ��    index ��ǰ����
                                formatter: function (value, row, index) {
                                    return "<a href='javascript:phone("+value+")' class='easyui-tooltip'   title=�̺�" + value + ">" + value + "</a>";
                                }
                            },
                            {field:'gh', title: '�̻�', align: 'center'},
                            {field:'fjh', title: '�칫��', align: 'center'}
                        ]
                    ]
          });
          
          var p = $('#dg_ydtx_wx').datagrid('getPager');   
	        $(p).pagination({   
	            pageSize: 10,//ÿҳ��ʾ�ļ�¼������Ĭ��Ϊ10   
	            pageList: [5,10,20],//��������ÿҳ��¼�������б�   
	            beforePageText: '��',//ҳ���ı���ǰ��ʾ�ĺ���   
	            afterPageText: 'ҳ    �� {pages} ҳ',   
	            displayMsg: '��ǰ��ʾ {from} - {to} ����¼   �� {total} ����¼',  
	        }); 
      }
	  
	  /**���ݰ󶨼���**/
      function Lodedata_pc(url) {
          $('#dg_ydtx_wx').datagrid({
              url: url, //���ݼ���·��
              pagination: true, //�Ƿ���ʾ��ҳ�ؼ�
              fitColumns: true, //����Ӧ���
              singleSelect: true, //��ѡ
              nowrap: true,//���ݳ��ȳ����п�ʱ�����Զ���ȡ��
              columns: [
                        [
                            
                            {field:'dw', title: '����', align: 'center',width:150},
                            {field:'xm', title: '����', align: 'center',width:150},
                            {field:'sjhm', title: '�ֻ�����', align: 'center',width:200},
                            {field:'dh', title: '�̺�', align: 'center',width:150},
                            {field:'gh', title: '�̻�', align: 'center',width:150},
                            {field:'fjh', title: '�칫��', align: 'center',width:150}
                        ]
                    ]
          });
          
          var p = $('#dg_ydtx_wx').datagrid('getPager');   
	        $(p).pagination({   
	            pageSize: 10,//ÿҳ��ʾ�ļ�¼������Ĭ��Ϊ10   
	            pageList: [5,10,20],//��������ÿҳ��¼�������б�   
	            beforePageText: '��',//ҳ���ı���ǰ��ʾ�ĺ���   
	            afterPageText: 'ҳ    �� {pages} ҳ',   
	            displayMsg: '��ǰ��ʾ {from} - {to} ����¼   �� {total} ����¼',   
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


		         <div title="�Ƕ�ͨѶ" style="padding:10px;" >	
		         <p id="bt">�Ƕ�ͨѶ¼��ѯ</p>	    	
		    	<div>
		    	  <form id="jbxxForm" method="post" >
		    	     <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
		    	     	<tr>
		    	     	  <td style="width:100px;">����</td>
		    	     	  <td><input id="lxr" class="easyui-textbox" name="lxr"  style="width:150px; height:30px;"/></td>
		    	     	  <td style="width:100px;"><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a></td>
		    	     	</tr>
		    	     </table>
		    	  </form>
		    	</div> 
				<p id="ts"></p>
				<table id="dg_ydtx_wx" title="ͨѶ¼�б�" class="easyui-datagrid" style="width:100%;height:390px;" 
				  collapsible="true" rownumbers="true"  striped="true" fitColumns="true"  
				 singleSelect="true"> 
					<thead> 
						<tr> 
						<!--  th field="id"  width=60px;  sortable=true, checkbox=true>����ID</th>-->
						<th field="dw"   id="bm" >����</th> 
 						<th field="xm"   id="xm">����</th> 
						<th field="sjhm" id="sjhm">�ֻ�����</th> 
						<th field="dh"   id="dh">�̺�</th> 
						<th field="gh"   id="gh">�̻�</th> 
						<th field="fjh"  id="fjh">�칫��</th>

						</tr> 
					</thead> 
				</table>   
		    </div> 

</body>  
</html> 