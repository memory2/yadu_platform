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
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript">
        $(function () {
            setDefaultDate('sqksrq', 'sqjsrq');
            cx();
            $('#btn_search_bgyp').bind('click', function () {
                cx();
            });


        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var sqr = $("#sqr").val();
            var jbzgs = $("#jbzgs").val();
            //alert(sqksrq+"--"+jbzgs);

            var url = 'servlet/OaAction?method=sy_bgyp&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&jbzgs=' + jbzgs + '&sqr=' + sqr;
            var gridID = "dg_bgyp";
            var gridBT = "办公用品";
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


<div title="派车信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">申请日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
                    </td>
                    <td style="width:100px;">申请人</td>
                    <td><input id="sqr" class="easyui-textbox" name="sqr" style="width:150px;"/></td>
                    <td style="width:100px;">物品名称</td>
                    <td><input id="jbzgs" class="easyui-textbox" style="width:150px;" name="jbzgs"></input></td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>-->

    <a id="btn_search_bgyp" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

    <br/><br/>

    <table id="dg_bgyp" title="办公用品信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true" nowrap="false"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=80px;>主键ID</th>
            <th field="wpmc" width=200px;>物品名称</th>
            <th field="wpsl" width=80px;>物品数量</th>
            <th field="jldw" width=80px;>计量单位</th>
            <th field="sqr" width=100px;>申请人</th>
            <th field="bm" width=100px;>部门</th>
            <th field="sqrq" width=100px;>申请日期</th>
            <th field="xqsm" width=200px;>需求说明</th>
            <th field="slqk" width=100px;>领取情况</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 