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
    <title>人员信息管理</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/demo.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <%
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
    %>
    <script type="text/javascript">
        $(function () {
            cx();
            $('#btn_search_jsgl').bind('click', function () {
                cx();
            });
        });

        function cx() {
            var jsmc = $("#jsmc").val();
            var ymdz = $("#ymdz").val();
            var fid = $("#fid").combobox('getValue');

            var url = 'servlet/KqAction?method=sy_jsgl&jsmc=' + jsmc + '&ymdz=' + ymdz + '&fid=' + fid;
            var gridID = "dg_jsgl";
            var gridBT = "角色管理";
            var xm = '<%=xm%>';
            if (xm != '系统管理员') {
                getDataGird(url, gridID, gridBT);
            } else {
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl = 'runtime/sy_tabs/tab_jsgl_xx.jsp';
                var delUrl = 'servlet/KqAction?method=xx_del_jsgl';
                getDataGird1(url, gridID, gridBT, updUrl, delUrl, '900', '200');
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

<div title="角色管理信息" style="padding:10px;" id="dh">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">角色名称</td>
                    <td><input id="jsmc" class="easyui-textbox" name="jsmc" style="width:220px;"/></td>
                    <td style="width:100px;">页面地址</td>
                    <td><input id="ymdz" class="easyui-textbox" name="ymdz" style="width:220px;"/></td>
                    <td style="width:100px;text-align:center">父角色名称</td>
                    <td><input id="fid" class="easyui-combobox" name="fid"
                               data-options="panelHeight:'auto', valueField:'id',textField:'text',url:'servlet/KqAction?method=zd&zdmc=父角色名称&zdlb=0'"/>
                    </td>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_jsgl" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
    <br/><br/>

    <table id="dg_jsgl" title="角色信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=100px; sortable=true, checkbox=true>主键ID</th>
            <th field="jsmc" width=200px;>角色名称</th>
            <th field="fid" width=200px;>父角色名称</th>
            <th field="ymdz" width=300px;>页面地址</th>
            <th field="tb" width=200px;>角色图标</th>
            <th field="bz" width=100px;>备注</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html> 