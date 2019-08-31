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
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String deptid = (String) session.getAttribute("deptid");
        String xm = (String) session.getAttribute("truename") == null ? "" : (String) session.getAttribute("truename");
        if (deptid == null || "".equals(deptid)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        $(function () {
            getDept("jbzgs", <%=deptid%>);
            setDefaultDate('sqksrq', 'sqjsrq');
            cx();
            $('#btn_search_xtpqd').bind('click', function () {
                cx();
            });


        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var sqr = $("#sqr").val();
            var jbzgs = $("#jbzgs").combobox('getValues');
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/OaAction?method=sy_xtpqd&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&jbzgs=' + jbzgs + '&sqr=' + sqr;
            var gridID = "dg_xtpqd";
            var gridBT = "协调派遣单";
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


<div title="协同派遣单信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">报修日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:140px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:140px;"></input>
                    </td>
                    <td style="width:100px;">维修人员</td>
                    <td><input id="sqr" class="easyui-textbox" name="sqr" style="width:140px;"/></td>
                    <td style="width:100px;">使用部门</td>
                    <td><input id="jbzgs" class="easyui-combotree"
                               data-options="valueField:'id',panelHeight:'400',textField:'text'" name="ssbm"
                               style="width:180px;"/></td>
                <tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_xtpqd" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

    <br/><br/>

    <table id="dg_xtpqd" title="协调派遣单信息列表" class="easyui-datagrid" style="width:100%;height:400px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true" singleSelect="true" nowrap="false">
        <thead>
        <tr>
            <th field="id" width=50px; halign="left" align="left">主键ID</th>
            <th field="sybm" width=100px;>使用部门</th>
            <th field="bxrq" width=100px;>报修日期</th>
            <th field="sy" width=300px;>事由</th>
            <th field="wxry" width=100px;>维修人员</th>
            <th field="ysry" width=100px;>验收人员</th>
            <th field="sybmyj" width=100px;>使用部门意见</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 