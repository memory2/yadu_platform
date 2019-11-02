<%@ page language="java" contentType="text/html; charset=gbk"
         pageEncoding="gbk" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String bmdm = (String) session.getAttribute("bmdm");
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
        if (bmdm == null || "".equals(bmdm)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        var bmdm = '<%=bmdm%>';
        $(function () {
            //getDept("ssbm",bmdm);
            //setDefaultDate('sqksrq','sqjsrq');
            //$("#gslb").combobox('setValue',bmdm.substring(0,2));
            cx();
            $('#btn_search_ydtx').bind('click', function () {
                cx();
            });
            /* $("#gslb").combobox({
              onChange: function (n,o) {
                  var gs = $("#gslb").combobox('getValue');
                  if(gs==41){
                      getDept("ssbm","4100000000");
                  }else if(gs==51){
                      getDept("ssbm","5100000000");
                  }else{
                      getDept("ssbm","6100000000");
                  }
              }
            });*/
        });

        function cx() {
            var lxdh = $("#lxdh").val();
            var lxr = $("#lxr").val();
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/KqAction?method=sy_ydtx&lxr=' + lxr + '&lxdh=' + lxdh;
            var gridID = "dg_ydtx";
            var gridBT = "�Ƕ�ͨѶ";


            var xm = '<%=xm%>';
            getDataGird(url, gridID, gridBT);
            /*if(xm!='ϵͳ����Ա'){
                getDataGird(url,gridID,gridBT);
            }else{
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl= 'runtime/sy_tabs/tab_ydtx_xx.jsp';
                var delUrl = 'servlet/KqAction?method=xx_del_ydtx';
                getDataGird1(url,gridID,gridBT,updUrl,delUrl,'900','200');
            }*/
        }
    </script>
    <style>
        #cxtj tr td {
            border: solid #add9c0;
            border-width: 1px 1px 1px 1px;
            padding: 3px 0px;
        }


    </style>
</head>
<body>


<div title="�Ƕ�ͨѶ" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <!--  <td style="width:100px;">
                   ��˾���</td>
                   <td><input id="gslb" name="gslb"  class="easyui-combobox" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'�Ƕ�ʵҵ'},{'id':'61','text':'������'},{'id':'51','text':'ԭ����ҵ��'}]"  style="width:150px;"/>
                   </td >
                      <td style="width:100px;text-align:center">��������</td>
                      <td><input id="ssbm" class="easyui-combotree" data-options="valueField:'id',panelHeight:'260',textField:'text'"  name="ssbm" style="width:150px;"/>
                      </td>-->
                    <td style="width:100px;">��ϵ��</td>
                    <td><input id="lxr" class="easyui-textbox" name="lxr" style="width:150px;"/></td>
                    <td style="width:100px;">��ϵ�绰</td>
                    <td>
                        <input id="lxdh" class="easyui-textbox" name="lxdh" style="width:150px;"/>
                    </td>
                    <td><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">���</a>
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">�޸�</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">ɾ��</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">����</a>-->


    <br/>

    <table id="dg_ydtx" title="�Ƕ�ͨѶ�б�" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>����ID</th>
            <th field="dw" width=150px;>����</th>
            <th field="xm" width=150px;>����</th>
            <th field="sjhm" width=150px;>�ֻ�����</th>
            <th field="dh" width=120px;>�̺�</th>
            <th field="gh" width=120px;>�̻�</th>
            <th field="fjh" width=130px;>�칫��</th>

        </tr>
        </thead>
    </table>
</div>

</body>
</html> 