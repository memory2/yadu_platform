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
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <!--  script type="text/javascript" language="Javascript" src="js/tjjs/js/jquery.min.js"></script>-->
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String xm = (String) session.getAttribute("truename") == null ? "" : (String) session.getAttribute("truename");
    %>
    <script type="text/javascript">
        $(function () {
            //setDefaultDate('sqksrq','sqjsrq');
            cx();
            $('#btn_search_sbxx').bind('click', function () {
                cx();
            });

            $('#btn_tj_sbxx').bind('click', function () {
                $("#sb").hide();
                $("#ss").show();
                tj();
                //$("body").html('<iframe style="width:100%;height:500px" scrolling="auto" frameborder="0" src="runtime/sy_tabs/tj_tab_sbxx.jsp"></iframe>');
            });


        });

        function cx() {
            var fjh = $("#fjh").val();
            var sbmc = $("#sbmc").val();
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');

            var url = 'servlet/OaAction?method=sy_sbxx&fjh=' + fjh + '&sbmc=' + sbmc + '&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq;
            var gridID = "dg_sbxx";
            var gridBT = "设备信息";
            var xm = '<%=xm%>';
            if (xm != '系统管理员') {
                getDataGird(url, gridID, gridBT);
            } else {
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl = 'runtime/sy_tabs/tab_sbxx_xx.jsp';
                var delUrl = 'servlet/OaAction?method=xx_del_sbxx';
                getDataGird1(url, gridID, gridBT, updUrl, delUrl, '800', '280');
            }

        }

        function fhcx() {
            $("#sb").show();
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
<div id="ss" style="display:none;margin-bottom:10px;margin-top:10px;"><a id="btn_cx_sbxx" class="easyui-linkbutton"
                                                                         data-options="iconCls:'icon-back'"
                                                                         onclick="fhcx()">返回查询</a></div>
<div id="chartContainer"></div>
<script type="text/javascript">
    var content = '<chart caption="网络设备统计饼状图" baseFontSize="14" showNames="1" showBorder="0" outCnvBaseFont="华文新魏"  outCnvBaseFontSize="20" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="0" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="1" numDivLines="0"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="台" plotSpacePercent="50">';
    var myChart = null;
    var sm = {};

    function tj() {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: 'servlet/OaAction?method=tj_sbxx',
            success: function (data) {
                //var d = eval("("+data+")");//效果等同于加dataType
                var dyj = JSON.stringify(data[0].dyj);
                var czj = JSON.stringify(data[0].czj);
                var lyq = JSON.stringify(data[0].lyq);
                var smy = JSON.stringify(data[0].smy);
                var jhj = JSON.stringify(data[0].jhj);
                content += '<set label="打印机"  value=' + dyj + ' /><set label="路由器"  value=' + lyq + ' /><set label="扫描仪"  value=' + smy + ' /><set label="传真机"  value=' + czj + ' /><set label="交换机"  value=' + jhj + ' /></chart>';
                if (myChart == null) {
                    myChart = new FusionCharts("js/tjjs/FusionCharts/Pie2D.swf", "myChartId", "100%", "450");
                    myChart.setXMLData(content);
                    myChart.render("chartContainer");
                } else {
                    $("#chartContainer").show();
                }

            }
        });
    }

</script>

<div title="设备信息" style="padding:10px;" id="sb">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">购买日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:140px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:140px;"></input></td>
                    <td style="width:100px;">设备名称</td>
                    <td><input id="sbmc" class="easyui-textbox" name="sbmc" style="width:170px;"/></td>
                    <td style="width:100px;text-align:center">设备位置</td>
                    <td><input id="fjh" class="easyui-textbox" name="fjh" style="width:170px;"/>
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

    <a id="btn_search_sbxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
    <a id="btn_tj_sbxx" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">统计图</a>
    <!--  font style="font-size:12px;">[打印机73台，路由器11个，交换机3个]</font>-->
    <br/><br/>

    <table id="dg_sbxx" title="网络设备信息列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=100px; sortable=true, checkbox=true>主键ID</th>
            <th field="fjh" width=200px;>设备位置</th>
            <th field="sbmc" width=400px;>设备名称</th>
            <th field="sbsl" width=100px;>设备数量</th>
            <th field="gmrq" width=300px;>购买日期</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 