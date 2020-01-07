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
        String bmdm = (String) session.getAttribute("bmdm");
        String zj = "ss";
        if ("upd".equals(czlb)) {
            zj = request.getParameter("zj");
        }
    %>
    <script type="text/javascript">
        var bmdm = '<%=bmdm%>';
        $(function () {
            var czlb = '<%=czlb%>';

            if ('upd' == czlb) {
                cx();
            } else {
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
            }


            $('#gb').bind('click', function () {
                parent.$('#myWindow').window('close');
            });

            $('#tj').bind('click', function () {
                tj(czlb);
            });


            $('#xm').combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#bm").combobox({
                onChange: function (n, o) {
                    yzry1("bm", "xm");
                }
            });

        });

        function cx() {
            var zj = '<%=zj%>';
            $.ajax({
                type: 'POST',
                url: 'servlet/KqAction?method=cx_qxgl&zj=' + zj,
                dataType: "json",
                success: function (data) {
                    $("#bm").combotree('disable');
                    $("#xm").combobox({disabled: true});
                    $("#bm").combotree('setValue', data[0].bm);
                    $("#js").combobox('setValue', data[0].js);
                    $("#xm").combobox('setValue', data[0].xm);
                    $("#gs").combobox('setValue', data[0].gslb);

                }
            });
        }

        function tj(czlb) {
            var zj = '<%=zj%>';
            var xm = $("#xm").combobox('getValue');
            var js = $("#js").combobox('getValue');
            var gslb = $("#gs").combobox('getValue');
            var method = 'xx_upd_qxgl';
            if (czlb == 'add') {
                method = 'xx_add_qxgl';
            }
            $.ajax({
                type: 'POST',
                url: 'servlet/KqAction?method=' + method + '&xm=' + xm + '&js=' + js + '&zj=' + zj + '&gslb=' + gslb,
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

<div title="权限管理信息" id="jsgl">
    <form id="jbxxForm" method="post">
        <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
            <tr>
                <td style="width:100px;">所属公司</td>
                <td><input id="gs" style="width:180px;" class="easyui-combobox" value="" name='gs'
                           data-options=" panelHeight:'auto', valueField:'id',textField:'text',data: [{id: '41',text: '亚都'},{id: '51',text: '迈迪克'},{id: '61',text: '威浦仕'}]"/>
                </td>

                <td style="width:100px;text-align:center">所属部门</td>
                <td><input id="bm" class="easyui-combotree"
                           data-options="valueField:'id',panelHeight:'auto',textField:'text'" name="bm"
                           style="width:180px;"/>
                </td>
                <td style="width:100px;">姓名</td>
                <td><input id="xm" class="easyui-combobox" name="xm" style="width:180px;"
                           data-options="panelHeight:'auto'"/></td>
                <td style="width:100px;">角色</td>
                <td>
                    <input id="js" class="easyui-combobox" name="js" style="width:180px;"
                           data-options="panelHeight:'auto', valueField:'id',textField:'text',url:'servlet/KqAction?method=zd&zdmc=父角色名称&zdlb=0'"/>
                </td>
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