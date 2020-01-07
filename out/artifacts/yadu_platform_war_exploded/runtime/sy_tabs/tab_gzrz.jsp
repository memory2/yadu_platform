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
        String xm = (String) session.getAttribute("truename") == null ? "" : (String) session.getAttribute("truename");
        String zdbm = (String) session.getAttribute("zdbm");
        String userid = (String) session.getAttribute("userid");
        if (deptid == null || "".equals(deptid)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        $(function () {

            $('#xm').combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#bm").combobox({
                onChange: function (n, o) {
                    yzry1("bm", "xm");
                }
            });
            getDept("bm", <%=deptid%>);
            setDefaultDate("txksrq", "txjsrq");
            var xm = '<%=xm%>';
            if (xm == '系统管理员') {
                getPerson('xm', '<%=zdbm%>');
                $("#bm").combotree('setValue', '<%=zdbm%>');
                $("#xm").combobox("setValue", '<%=userid%>');
                $("#bm").combotree('disable');
                $("#xm").combobox('disable');
            }


            cx();
            $('#btn_search_gzrz').bind('click', function () {
                cx();
            });


        });


        function cx() {
            var bm = $("#bm").combobox('getValue');
            var xm = $("#xm").combobox('getValue');
            var txksrq = $("#txksrq").datebox('getValue');
            var txjsrq = $("#txjsrq").datebox('getValue');
            var rzbt = $("#rzbt").val();

            //alert(sqksrq+"--"+jbzgs);

            var url = 'servlet/OaAction?method=sy_gzrz&bm=' + bm + '&xm=' + xm + '&txksrq=' + txksrq + '&txjsrq=' + txjsrq + '&rzbt=' + rzbt;
            var gridID = "dg_gzrz";
            var gridBT = "工作日志";
            var xm = '<%=xm%>';
            if (xm != '系统管理员') {
                getDataGird(url, gridID, gridBT);
            } else {
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl = 'runtime/sy_tabs/tab_gzrz_xx.jsp';
                var delUrl = 'servlet/OaAction?method=xx_del_gzrz';
                getDataGird1(url, gridID, gridBT, updUrl, delUrl, '900', '380');
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


<div title="工作日志" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:80px;">填写日期</td>
                    <td><input id="txksrq" type="text" class="easyui-datebox" style="width:110px;"></input> 至
                        <input id="txjsrq" type="text" class="easyui-datebox" style="width:110px;"></input>
                    </td>
                    <td style="width:80px;">日志标题</td>
                    <td><input id="rzbt" class="easyui-textbox" name="rzbt" style="width:120px;"/></td>
                    <td style="width:80px;text-align:center">所属部门</td>
                    <td><input id="bm" class="easyui-combotree"
                               data-options="valueField:'id',panelHeight:'auto',textField:'text'" name="ssbm"
                               style="width:120px;"/>
                    </td>
                    <td style="width:80px;">姓名</td>
                    <td><input id="xm" class="easyui-combobox" name="xm" style="width:120px;"
                               data-options="panelHeight:'auto'"/></td>

                </tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_gzrz" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

    <br/><br/>

    <table id="dg_gzrz" title="工作日志信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true" nowrap="false"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>主键ID</th>
            <th field="ssbm" width=100px;>部门</th>
            <th field="txr" width=80px;>姓名</th>
            <th field="rzbt" width=150px;>日志标题</th>
            <th field="rznr" width=300px;>日志内容</th>
            <th field="txsj" width=120px;>填写时间</th>
            <th field="xgsj" width=120px;>最近修改时间</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 