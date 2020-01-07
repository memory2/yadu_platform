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
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <% String czlb = request.getParameter("czlb");
        String zj = "ss";
        if ("upd".equals(czlb)) {
            zj = request.getParameter("zj");
        }
    %>
    <script type="text/javascript">
        $(function () {
            var czlb = '<%=czlb%>';
            if ('upd' == czlb) {
                cx();
            }

            $('#gb').bind('click', function () {
                parent.$('#myWindow').window('close');
            });

            $('#tj').bind('click', function () {
                tj(czlb);
            });

        });

        function cx() {
            var zj = '<%=zj%>';
            $.ajax({
                type: 'POST',
                url: 'servlet/OaAction?method=cx_dhxx&zj=' + zj,
                dataType: "json",
                success: function (data) {
                    $("#fjh").textbox('setValue', data[0].fjh);
                    $("#dhhm").textbox('setValue', data[0].dhhm);
                    $("#yys").textbox('setValue', data[0].yys);
                    $("#cz").textbox('setValue', data[0].cz);
                    $("#bz").textbox('setValue', data[0].bz);
                    $("#hjsl").numberbox('setValue', data[0].hjsl);
                }
            });
        }

        function tj(czlb) {
            var zj = '<%=zj%>';
            var fjh = $("#fjh").val();
            var dhhm = $("#dhhm").val();
            var hjsl = $("#hjsl").val();
            var yys = $("#yys").val();
            var cz = $("#cz").val();
            var bz = $("#bz").val();
            var args = {'zj': zj, 'fjh': fjh, 'dhhm': dhhm, 'hjsl': hjsl, 'cz': cz};
            var method = 'xx_upd_dhxx';
            if (czlb == 'add') {
                method = 'xx_add_dhxx';
            }
            $.ajax({
                type: 'POST',
                url: 'servlet/OaAction?method=' + method + '&yys=' + yys + '&bz=' + bz,
                data: args,
                success: function (data) {
                    if ('ok' == data) {
                        alert("提交成功！");
                    } else {
                        alert("提交失败,请联系管理员！");
                    }
                    parent.cx();

                }
            });
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

<div title="电话详细信息" id="dhxx">
    <form id="jbxxForm" method="post">
        <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;margin:8px;" id="cxtj">
            <tr>
                <td style="width:100px;">电话位置</td>
                <td><input id="fjh" class="easyui-textbox" name="fjh" style="width:130px;"/></td>
                <td style="width:100px;">电话号码</td>
                <td><input id="dhhm" class="easyui-textbox" name="dhhm" style="width:130px;"/></td>
                <td style="width:100px;text-align:center">话机数量</td>
                <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0"
                           style="width:130px;" id="hjsl">
                </td>
            <tr>
            <tr>
                <td style="width:100px;">运营商</td>
                <td><input id="yys" class="easyui-textbox" name="yys" style="width:130px;"/></td>
                <td style="width:100px;">传真</td>
                <td><input type="text" class="easyui-numberbox" value="" data-options="min:0,precision:0"
                           style="width:130px;" id="cz">
                <td style="width:100px;text-align:center">备注</td>
                <td><input id="bz" class="easyui-textbox" name="bz" style="width:130px;"/></td>
            <tr>
        </table>
    </form>
</div>
<div region="south" border="false" style="text-align:center;padding:13px 0px;">
    <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" id="tj">提交</a>
    <!--a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="gb">关闭</a>  -->
</div>
</body>
</html> 