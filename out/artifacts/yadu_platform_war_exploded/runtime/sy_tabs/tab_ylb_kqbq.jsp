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
            $('#btn_search_kqbq').bind('click', function () {
                cx();
            });

        });

        function cx() {
            var sqksrq = $("#sqksrq").datebox('getValue');
            var sqjsrq = $("#sqjsrq").datebox('getValue');
            var sqr = $("#sqr").val();
            var jbzgs = $("#jbzgs").combobox('getValues');
            //alert(jbzgs);

            var url = 'servlet/OaAction?method=sy_ylb_kqbq&sqksrq=' + sqksrq + '&sqjsrq=' + sqjsrq + '&jbzgs=' + jbzgs + '&sqr=' + sqr;
            var gridID = "dg_ylb_kqbq";
            var gridBT = "原料部考勤补签";
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


<div title="考勤补签信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;text-align:center">补签日期</td>
                    <td><input id="sqksrq" type="text" class="easyui-datebox" style="width:150px;"></input> 至
                        <input id="sqjsrq" type="text" class="easyui-datebox" style="width:150px;"></input>
                    </td>
                    <td style="width:100px;">申请人</td>
                    <td><input id="sqr" class="easyui-textbox" name="sqr" style="width:150px;"/></td>
                    <td style="width:100px;">补签次序</td>
                    <td>
                        <!--  select id="jbzgs" class="easyui-combobox" name="jbzgs" style="width:150px;">
                         <option value="0">上午上班</option>
                         <option value="1">上午下班</option>
                         <option value="2">下午上班</option>
                         <option value="3">下午下班</option>
                      </select> -->

                        <input id="jbzgs" class="easyui-combobox" name="dept"
                               data-options="multiple:'true', panelHeight:'auto', valueField:'id',textField:'text',url:'servlet/OaAction?method=zd&zdmc=具体补卡次序&zdlb=1'"/>

                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <a id="btn_search_kqbq" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
    <font style="font-size:12px;color:#0099FF;">[ 2016年6月2号前的数据请查看人力资源部的考勤补签 ]</font>
    <br/><br/>

    <table id="dg_ylb_kqbq" title="考勤补签信息列表" class="easyui-datagrid" style="width:100%;height:390px"
           collapsible="true" rownumbers="true" fitColumns="true" striped="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=80px;>主键ID</th>
            <th field="bqr" width=100px;>申请人</th>
            <th field="bm" width=150px;>部门</th>
            <th field="bkrq" width=120px;>补卡日期</th>
            <th field="bkcx" width=120px;>补卡次序</th>
            <th field="bkyy" width=400px;>补卡原因</th>
            <th field="bklb" width=100px;>补卡类别</th>
        </tr>
        </thead>
    </table>
</div>

</body>
</html> 