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
    <title>��Ա��Ϣ����</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/demo.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String deptid = (String) session.getAttribute("bmdm");
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
        if (deptid == null || "".equals(deptid)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }%>
    <script type="text/javascript">
        var bmdm = '<%=deptid%>';
        $(function () {
            //getDept("bm",bmdm);
            cx();
            $('#btn_search_qxgl').bind('click', function () {
                cx();
            });


            $('#xm').combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#gs").combobox({
                onChange: function (n, o) {
                    var gs = $("#gs").combobox('getValue');
                    if (gs == 41) {
                        getDept("bm", "4100000000");
                    } else if (gs == 51) {
                        getDept("bm", "5100000000");
                    } else {
                        getDept("bm", "6100000000");
                    }
                }
            });


            $("#bm").combobox({
                onChange: function (n, o) {
                    yzry1("bm", "xm");
                }
            });
        });


        function cx() {
            var bm = $("#bm").combobox('getValue');
            var xm = $("#xm").combobox('getValue');
            var js = $("#js").combobox('getValue');
            var gslb = $("#gs").combobox('getValue');
            //alert(sqksrq+"--"+jbzgs);

            var url = 'servlet/KqAction?method=sy_qxgl&bm=' + bm + '&xm=' + xm + '&js=' + js + '&gslb=' + gslb;
            var gridID = "dg_qxgl";
            var gridBT = "Ȩ�޹���";
            var xm = '<%=xm%>';
            if (xm != 'ϵͳ����Ա') {
                getDataGird(url, gridID, gridBT);
            } else {
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl = 'runtime/sy_tabs/tab_qxgl_xx.jsp';
                var delUrl = 'servlet/KqAction?method=xx_del_qxgl';
                getDataGird1(url, gridID, gridBT, updUrl, delUrl, '98%', '400');
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


<div title="Ȩ�޹���" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">������˾</td>
                    <td><input id="gs" style="width:150px;" class="easyui-combobox" value="" name='gs'
                               data-options=" panelHeight:'auto', valueField:'id',textField:'text',data: [{id: '41',text: '�Ƕ�'},{id: '51',text: '���Ͽ�'},{id: '61',text: '������'}]"/>
                    </td>
                    <td style="width:100px;text-align:center">��������</td>
                    <td><input id="bm" class="easyui-combotree"
                               data-options="valueField:'id',panelHeight:'260',textField:'text'" name="ssbm"
                               style="width:150px;"/>
                    </td>
                    <td style="width:100px;">����</td>
                    <td><input id="xm" class="easyui-combobox" name="xm" style="width:150px;"
                               data-options="panelHeight:'260'"/></td>
                    <td style="width:100px;">��ɫ</td>
                    <td>
                        <input id="js" class="easyui-combobox" name="js" style="width:150px;"
                               data-options="panelHeight:'auto', valueField:'id',textField:'text',url:'servlet/KqAction?method=zd&zdmc=����ɫ����&zdlb=0'"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <br/>

    <a id="btn_search_qxgl" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a>

    <br/><br/>

    <table id="dg_qxgl" title="Ȩ�޹�����Ϣ�б�" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>����ID</th>
            <th field="gslb" width=150px;>������˾</th>
            <th field="dw" width=150px;>��������</th>
            <th field="xm" width=150px;>����</th>
            <th field="js" width=150px;>��ɫ����</th>

        </tr>
        </thead>
    </table>
</div>

</body>
</html> 