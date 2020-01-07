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
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String bmmc = (String) session.getAttribute("name") == null ? "" : (String) session.getAttribute("name");
        String kqh = (String) session.getAttribute("stuff_id") == null ? "" : ((String) session.getAttribute("stuff_id")).substring(1, 5);
    %>
    <script type="text/javascript">
        $(function () {
            var bm = '<%=bmmc%>';
            var kqh = '<%=kqh%>';
            if (bm != '信息部' && bm != '财务部' && bm != '财务中心' && kqh != '00002') {
                $("#btn_tj_jkxx").hide();
            }
            setDefaultDate('sqksrq', 'sqjsrq');
            cx();
            $('#btn_search_jkxx').bind('click', function () {
                cx();
            });

            $('#btn_tj_jkxx').bind('click', function () {
                $("#chartContainer").show();
                $("#jk").hide();
                $("#ss").show();
                tj();
                //$("body").html('<iframe style="width:100%;height:500px" scrolling="auto" frameborder="0" src="runtime/sy_tabs/tj_tab_sbxx.jsp"></iframe>');
            });
        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var sqr = $("#sqr").val();
            var jeqs = $("#jeqs").val();
            var jejs = $("#jejs").val();
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/OaAction?method=sy_jkxx&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&jeqs=' + jeqs + '&jejs=' + jejs + '&sqr=' + sqr;
            var gridID = "dg_jkxx";
            var gridBT = "借款信息";
            getDataGird(url, gridID, gridBT);

        }

        function fhcx() {
            $("#jk").show();
            $("#ss").hide();
            $("#chartContainer").hide();
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
<div id="ss" style="display:none;margin-bottom:10px;margin-top:10px;"><a id="btn_cx_dhxx" class="easyui-linkbutton"
                                                                         data-options="iconCls:'icon-back'"
                                                                         onclick="fhcx()">返回查询</a></div>
<div id="chartContainer" style="text-align:center;"></div>
<script type="text/javascript">
    var myChart = null;

    function tj() {
        var sqksrq = $("#sqksrq").datebox('getValue') != "" ? $("#sqksrq").datebox('getValue').substring(0, 4) : "";
        var sqjsrq = $("#sqjsrq").datebox('getValue') != "" ? $("#sqksrq").datebox('getValue').substring(0, 4) : "";
        var lb = "tj_jkxx&ksrq=" + sqksrq + "&jsrq=" + sqjsrq;
        getTjt(lb, "年借款信息统计", "Column3D", "20000");
    }


</script>

<div title="借款信息" style="padding:10px;" id="jk">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">借款日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:115px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:115px;"></input>
                    </td>
                    <td style="width:100px;">借款人</td>
                    <td><input id="sqr" class="easyui-textbox" name="sqr" style="width:115px;"/></td>
                    <td style="width:100px;">借款金额</td>
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

    <a id="btn_search_jkxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
    <a id="btn_tj_jkxx" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">统计图</a>
    <br/><br/>

    <table id="dg_jkxx" title="借款信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true" singleSelect="true" nowrap="false">
        <thead>
        <tr>
            <th field="id" width=80px;>主键ID</th>
            <th field="sqr" width=100px;>借款人</th>
            <th field="bm" width=120px;>部门</th>
            <th field="sqrq" width=120px;>申请日期</th>
            <th field="jksy" width=300px;>借款事由</th>
            <th field="jkje" width=120px;>借款金额</th>
            <th field="yjhkrq" width=120px;>预计还款日期</th>
            <th field="bz" width=120px;>备注</th>


        </tr>
        </thead>
    </table>
</div>

</body>
</html> 