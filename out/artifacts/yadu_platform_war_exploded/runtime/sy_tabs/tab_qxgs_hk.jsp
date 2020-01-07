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
    <script type="text/javascript">
        $(function () {
            setDefaultDate('sqksrq', 'sqjsrq');
            cx();
            $('#btn_search_qxgshk').bind('click', function () {
                cx();
            });


        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var sfhp = $("#sfhp").combobox('getValue');

            var jeqs = $("#jeqs").val();
            var jejs = $("#jejs").val();
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/OaAction?method=sy_qxgshk&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&jeqs=' + jeqs + '&jejs=' + jejs + '&sfhp=' + sfhp;
            var gridID = "dg_qxgshk";
            var gridBT = "器械公司采购汇款";
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
<div title="器械公司采购汇款信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">汇款日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:115px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:115px;"></input>
                    </td>
                    <td style="width:100px;">是否含票</td>
                    <td><input id="sfhp" class="easyui-combobox" name="sfhp"
                               data-options="panelHeight:'auto', valueField:'id',textField:'text',url:'servlet/OaAction?method=zd&zdmc=是否&zdlb=1'"/>
                    </td>
                    <td style="width:100px;">汇款金额</td>
                    <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2"
                               style="width:115px;" id="jeqs"></input> 至
                        <input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:2"
                               style="width:115px;" id="jejs">
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>-->

    <a id="btn_search_qxgshk" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

    <br/><br/>

    <table id="dg_qxgshk" title="费用报销信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="false" singleSelect="true" nowrap="false">
        <thead>
        <tr>
            <th field="id" width=50px;>主键ID</th>
            <th field="sqr" width=80px;>申请人</th>
            <th field="bm" width=80px;>部门</th>
            <th field="hkrq" width=100px;>汇款日期</th>
            <th field="cpmc" width=150px;>产品名称</th>
            <th field="dj" width=80px;>单价</th>
            <th field="cpsl" width=50px;>数量</th>
            <th field="jldw" width=50px;>计量单位</th>
            <th field="zj" width=80px;>总价</th>
            <th field="hkje" width=100px;>合计金额</th>
            <th field="ywy" width=80px;>业务员</th>
            <th field="sfczf" width=70px;>是否厂直发</th>
            <th field="sfhp" width=70px;>是否含票</th>
            <th field="skdw" width=250px;>收款单位</th>
            <th field="skkhh" width=200px;>收款开户行</th>
            <th field="skzh" width=200px;>收款账号</th>
            <th field="hkhm" width=80px;>汇款户名</th>
            <th field="jzmc" width=250px;>记账名称</th>
            <th field="fpys" width=80px;>发票预时</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 