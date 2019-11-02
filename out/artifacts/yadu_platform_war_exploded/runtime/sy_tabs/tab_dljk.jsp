<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>人员信息管理</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/demo.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String deptid = (String) session.getAttribute("deptid");
        if (deptid == null || "".equals(deptid)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        $(function () {
            setDefaultTime('dlksrq', 'dljsrq');
            getDept("bm", <%=deptid%>);
            cx();
            $('#btn_search_dljk').bind('click', function () {
                cx();
            });

            $('#xm').combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#bm").combobox({
                onChange: function (n, o) {
                    yzry1("bm", "xm");
                }
            });
        });

        function cx() {
            var dlksrq = $("#dlksrq").datetimebox('getValue');
            var dljsrq = $("#dljsrq").datetimebox('getValue');
            var bm = $("#bm").combobox('getValue');
            var xm = $("#xm").combobox('getValue');
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/OaAction?method=sy_dljk&dlksrq=' + dlksrq + '&dljsrq=' + dljsrq + '&bm=' + bm + '&xm=' + xm;
            var gridID = "dg_dljk";
            var gridBT = "登陆监控信息";
            getDataGird(url, gridID, gridBT);

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


<div title="登陆信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">所属部门</td>
                    <td><input id="bm" class="easyui-combotree"
                               data-options="valueField:'id',panelHeight:'auto',textField:'text'" name="ssbm"
                               style="width:160px;"/>
                    </td>
                    <td style="width:100px;">姓名</td>
                    <td><input id="xm" class="easyui-combobox" name="xm" style="width:160px;"
                               data-options="panelHeight:'auto'"/></td>
                    <td style="width:100px;text-align:center">登陆时间</td>
                    <td><input id="dlksrq" type="text" class="easyui-datetimebox" style="width:150px;"></input> 至
                        <input id="dljsrq" type="text" class="easyui-datetimebox" style="width:150px;"></input>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_dljk" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

    <br/><br/>

    <table id="dg_dljk" title="登陆监控信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true" singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=80px;>主键ID</th>
            <th field="bm" width=180px;>所属部门</th>
            <th field="xm" width=150px;>姓名</th>
            <th field="dlsj" width=200px;>登陆时间</th>
            <th field="tcsj" width=200px;>退出时间</th>
            <th field="zxsj" width=120px;>本次在线分钟数</th>
            <th field="ip" width=120px;>ip地址</th>
            <th field="dlfs" width=100px;>登陆类型</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 