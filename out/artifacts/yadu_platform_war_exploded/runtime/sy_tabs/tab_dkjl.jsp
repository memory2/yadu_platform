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
    <title>�򿨼�¼��Ϣ</title>
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
                alert("��ѡ����ݣ�");
                return;
            }
            if (cxy == "") {
                alert("��ѡ���·ݣ�");
                return;
            }

            if (cxr != "" && cxr.indexOf("0") == -1) {
                if (cxr < 10) {
                    cxr = "0" + cxr;
                }
            }


            if (rygh == "" && ryxm == "") {
                alert("�����빤�Ż�������");
                return;
            }


            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/KqAction?method=sy_dkjl&cxn=' + cxn + '&cxy=' + cxy + '&cxr=' + cxr + '&rygh=' + rygh + '&ryxm=' + ryxm;
            var gridID = "dg_dkjl";
            var gridBT = "�򿨼�¼";
            var xm = '<%=xm%>';

            if (bmdm != '4110000000' && bmdm != '4113000000') {
                alert("���޴�Ȩ�ޣ�����ϵ����Ա��");
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


<div title="�򿨼�¼��ѯ" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">
                        ѡ����ݣ�
                    </td>
                    <td><input id="cxn" name="cxn" class="easyui-combobox" required="true"
                               data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'2019','text':'2019��'},{'id':'2020','text':'2020��'},{'id':'2021','text':'2021��'}]"
                               style="width:100px;"/>
                    </td>
                    <td style="width:100px;">
                        ѡ���·ݣ�
                    </td>
                    <td><input id="cxy" name="cxy" class="easyui-combobox" required="true" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[
		    			{'id':'01','text':'1��'},{'id':'02','text':'2��'},{'id':'03','text':'3��'},{'id':'04','text':'4��'},{'id':'05','text':'5��'},{'id':'06','text':'6��'}
		    			,{'id':'07','text':'7��'},{'id':'08','text':'8��'},{'id':'09','text':'9��'},{'id':'10','text':'10��'},{'id':'11','text':'11��'},{'id':'12','text':'12��'}
		    			]" style="width:100px;"/>
                    </td>
                    <td style="width:100px;">��������</td>
                    <td><input id="cxr" class="easyui-textbox" name="cxr" style="width:100px;"/></td>
                    </td >
                    <td style="width:100px;">����</td>
                    <td><input id="rygh" class="easyui-textbox" name="" rygh"" style="width:100px;"/></td>
                    <td style="width:100px;">����</td>
                    <td>
                        <input id="ryxm" class="easyui-textbox" name="ryxm" style="width:100px;"/>
                    </td>
                    <td><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">��ѯ</a>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>
    <div><font style="font-size:12px;color:red">������ʾ��1������ݡ��͡��·ݡ���ѡ�������š��͡�������������������һ�</font></div>
    <!--  a id="btn_add" href="<%=path%>/runtime/pass.jsp" class="easyui-linkbutton" data-options="iconCls:'icon-add'">���</a>
				<a id="btn_edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">�޸�</a>
				<a id="btn_remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">ɾ��</a>
				<a id="btn_save" href="otherpage.php" class="easyui-linkbutton" data-options="iconCls:'icon-save'">����</a>-->


    <br/>

    <table id="dg_dkjl" title="�򿨼�¼�б�" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>����ID</th>
            <th field="gslb" width=150px;>����</th>
            <th field="rygh" width=150px;>����</th>
            <th field="ryxm" width=150px;>����</th>
            <th field="dksj" width=150px;>��ʱ��</th>
            <th field="kqjbh" width=120px;>���ڻ����</th>
            <th field="kqjmc" width=120px;>���ڻ�����</th>
            <th field="kqjip" width=130px;>���ڻ�IP</th>

        </tr>
        </thead>
    </table>
</div>

</body>
</html> 