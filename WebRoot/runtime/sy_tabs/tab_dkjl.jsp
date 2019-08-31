<%@ page language="java" contentType="text/html; charset=gbk"
         pageEncoding="gbk" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
    <title>打卡记录信息</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String bmdm = (String) session.getAttribute("bmdm");
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
        if (bmdm == null || "".equals(bmdm)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        var bmdm = '<%=bmdm%>';
        $(function () {
            //alert(bmdm);
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }

            $("#cxn").combobox('setValue', year);
            $("#cxy").combobox('setValue', month);

            $('#btn_search_ydtx').bind('click', function () {
                cx();
            });

        });

        function cx() {
            var cxn = $("#cxn").combobox('getValue');
            var cxy = $("#cxy").combobox('getValue');
            var cxr = $("#cxr").val();
            var rygh = $("#rygh").val();
            var ryxm = $("#ryxm").val();
            if (cxn == "") {
                alert("请选择年份！");
                return;
            }
            if (cxy == "") {
                alert("请选择月份！");
                return;
            }

            if (cxr != "" && cxr.indexOf("0") == -1) {
                if (cxr < 10) {
                    cxr = "0" + cxr;
                }
            }


            if (rygh == "" && ryxm == "") {
                alert("请输入工号或姓名！");
                return;
            }


            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/KqAction?method=sy_dkjl&cxn=' + cxn + '&cxy=' + cxy + '&cxr=' + cxr + '&rygh=' + rygh + '&ryxm=' + ryxm;
            var gridID = "dg_dkjl";
            var gridBT = "打卡记录";
            var xm = '<%=xm%>';

            if (bmdm != '4110000000' && bmdm != '4113000000') {
                alert("您无此权限，请联系管理员！");
                return;
            }


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


<div title="打卡记录查询" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">
                        选择年份：
                    </td>
                    <td><input id="cxn" name="cxn" class="easyui-combobox" required="true"
                               data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'2019','text':'2019年'},{'id':'2020','text':'2020年'},{'id':'2021','text':'2021年'}]"
                               style="width:100px;"/>
                    </td>
                    <td style="width:100px;">
                        选择月份：
                    </td>
                    <td><input id="cxy" name="cxy" class="easyui-combobox" required="true" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[
		    			{'id':'01','text':'1月'},{'id':'02','text':'2月'},{'id':'03','text':'3月'},{'id':'04','text':'4月'},{'id':'05','text':'5月'},{'id':'06','text':'6月'}
		    			,{'id':'07','text':'7月'},{'id':'08','text':'8月'},{'id':'09','text':'9月'},{'id':'10','text':'10月'},{'id':'11','text':'11月'},{'id':'12','text':'12月'}
		    			]" style="width:100px;"/>
                    </td>
                    <td style="width:100px;">输入日期</td>
                    <td><input id="cxr" class="easyui-textbox" name="cxr" style="width:100px;"/></td>
                    </td >
                    <td style="width:100px;">工号</td>
                    <td><input id="rygh" class="easyui-textbox" name="" rygh"" style="width:100px;"/></td>
                    <td style="width:100px;">姓名</td>
                    <td>
                        <input id="ryxm" class="easyui-textbox" name="ryxm" style="width:100px;"/>
                    </td>
                    <td><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <div><font style="font-size:12px;color:red">友情提示：1、“年份”和“月份”必选，“工号”和“姓名”必须输入其中一项！</font></div>
    <!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>-->


    <br/>

    <table id="dg_dkjl" title="打卡记录列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>主键ID</th>
            <th field="gslb" width=150px;>厂区</th>
            <th field="rygh" width=150px;>工号</th>
            <th field="ryxm" width=150px;>姓名</th>
            <th field="dksj" width=150px;>打卡时间</th>
            <th field="kqjbh" width=120px;>考勤机编号</th>
            <th field="kqjmc" width=120px;>考勤机名称</th>
            <th field="kqjip" width=130px;>考勤机IP</th>

        </tr>
        </thead>
    </table>
</div>

</body>
</html> 