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
        String deptid = (String) session.getAttribute("bmid");
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
        if (deptid == null || "".equals(deptid)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        $(function () {
            setDefaultDate('sqksrq', 'sqjsrq');
            cx();
            $('#btn_search_tskq').bind('click', function () {
                cx();
            });

        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var kqbs = $("#lb").val();
            var tsry = $("#tname").val();
            //alert(sqksrq+"--"+jbzgs);

            var url = 'servlet/KqAction?method=sy_tskq&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&lb=' + kqbs + '&tname=' + tsry;
            var gridID = "dg_tskq";
            var gridBT = "���⿼�ڹ���";


            var xm = '<%=xm%>';
            if (xm != 'ϵͳ����Ա') {
                getDataGird(url, gridID, gridBT);
            } else {
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl = 'runtime/sy_tabs/tab_tskq_xx.jsp';
                var delUrl = 'servlet/KqAction?method=xx_del_tskq';
                getDataGird1(url, gridID, gridBT, updUrl, delUrl, '900', '300');
            }
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


<div title="���⿼�ڹ���" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">����</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> ��
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input></td>
                    <td style="width:100px;">�������</td>
                    <td><input id="lb" class="easyui-textbox" name="lb" style="width:200px;"/></td>
                    <td style="width:100px;">������</td>
                    <td>
                        <input id="tname" class="easyui-textbox" name="tname" style="width:200px;"/>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_tskq" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a>
    <font style="color:red;font-size:12px;">1.�������ڣ����°���д"�ż�"������ǰ���٣����żٵİ�����д���ޡ������������д"fj"��2.ͣ�粻�ż٣��޷��򿨵����°���д"ͣ��",���������д��tdbq��</font>
    <br/><br/>

    <table id="dg_tskq" title="�Ƕ����⿼��" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>����ID</th>
            <th field="rq" width=150px;>����</th>
            <th field="swsb" width=150px;>�����ϰ�</th>
            <th field="swxb" width=150px;>�����°�</th>
            <th field="xwsb" width=120px;>�����ϰ�</th>
            <th field="xwxb" width=120px;>�����°�</th>
            <th field="lb" width=130px;>�������</th>
            <th field="tname" width=130px;>������</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 