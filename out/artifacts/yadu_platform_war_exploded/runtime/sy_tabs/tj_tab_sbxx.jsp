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
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <script type="text/javascript" language="Javascript" src="js/tjjs/js/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>

</head>
<body>
<a id="btn_cx_sbxx" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返回查询</a>
<div id="chartContainer"></div>
<script type="text/javascript">
    var content = '<chart caption="" showNames="1" showBorder="0" outCnvBaseFont="华文新魏"  outCnvBaseFontSize="20" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="0" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="1" numDivLines="0"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="台" plotSpacePercent="50">';
    $(function () {
        tj();
    });

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
                var myChart = new FusionCharts("js/tjjs/FusionCharts/Pie2D.swf", "myChartId", "100%", "450");
                myChart.setXMLData(content);
                myChart.render("chartContainer");
            }
        });
    }

</script>
</body>
</html> 