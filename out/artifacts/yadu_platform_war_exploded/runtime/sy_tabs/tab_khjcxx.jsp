<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="GBK" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>�ͻ�������Ϣ</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <% String czlb = request.getParameter("czlb");
        String deptid = (String) session.getAttribute("deptid");
        String zj = "ss";
        if ("upd".equals(czlb) || "xq".equals(czlb)) {
            zj = request.getParameter("zj");
        }
    %>
    <script type="text/javascript">
        var zj;
        $(function () {
            var czlb = '<%=czlb%>';
            var url;
            if ('upd' == czlb) {//�޸�
                cx();
                url = 'servlet/KhAction?method=xx_upd_khgl&zj=' + zj;
                $('#tjbjx').hide();
            } else if ('add' == czlb) {
                url = 'servlet/KhAction?method=xx_add_khgl';
            } else {
                cx();
                $('#tjbjx').hide();
                $('#tjbtc').hide();
                //$('input,select,textarea',$('form[name="khglform"]')).attr('readonly',true);
                $('input,select,textarea', $('form[name="khglform"]')).attr('disabled', true);
            }
            //����ύ��������ť��ť
            $('#tjbjx').bind('click', function () {
                if ($("#xm").val() == "") {
                    alert("�ͻ���������Ϊ�գ�");
                    return;
                }
                if ($("#glryxm1").val() == "") {
                    alert("������Ա��������Ϊ�գ�");
                    return;
                }

                if ($("#xm").val() == "") {
                    alert("ҵ��������Ϊ�գ�");
                    return;
                }

                $('#khglform').form('submit', {
                    url: url,
                    onSubmit: function () {

                        // do some check
                        // return false to prevent submit;
                    },
                    success: function (data) {
                        alert("�ύ�ɹ���");
                        $('#khglform')[0].reset();
                        $("#khglform").form("clear");
                    }
                });
            });

            //����ύ���˳���ť
            $('#tjbtc').bind('click', function () {
                if ($("#xm").val() == "") {
                    alert("�ͻ���������Ϊ�գ�");
                    return;
                }

                if ($("#glryxm1").val() == "") {
                    alert("������Ա��������Ϊ�գ�");
                    return;
                }

                if ($("#ywqy").val() == "") {
                    alert("ҵ��������Ϊ�գ�");
                    return;
                }
                $('#khglform').form('submit', {
                    url: url,
                    onSubmit: function () {
                        // do some check
                        // return false to prevent submit;
                    },
                    success: function (data) {
                        alert("�ύ�ɹ���");
                        parent.$('#myWindow').window('close');
                        parent.$('#thewin').window('close');
                        window.parent.location.reload(true);
                    }
                });
            });

            //����˳���ť
            $('#tc').bind('click', function () {
                if ('upd' == czlb || 'add' == czlb) {
                    parent.$('#myWindow').window('close');
                    window.parent.location.reload(true);
                } else {
                    parent.$('#thewin').window('close');
                }


            });
        });

        function cx() {
            zj = '<%=zj%>';
            $.ajax({
                type: 'POST',
                url: 'servlet/KhAction?method=cx_khgl&zj=' + zj,
                dataType: "json",
                success: function (data) {
                    //alert(JSON.stringify(data));
                    $("#xm").textbox('setValue', data[0].xm);
                    $("#xb").combobox('setValue', data[0].xb);
                    $("#csrq").textbox('setValue', data[0].csrq);
                    $("#sj").textbox('setValue', data[0].sjh);
                    $("#jg").textbox('setValue', data[0].jg);
                    $("#wx").textbox('setValue', data[0].wx);
                    $("#qq").textbox('setValue', data[0].qq);
                    $("#zz").textbox('setValue', data[0].zz);

                    $("#glryid1").textbox('setValue', data[0].glryid1);
                    $("#glryxm1").textbox('setValue', data[0].glryxm1);
                    $("#lxfs1").textbox('setValue', data[0].lxfs1);
                    $("#gx1").textbox('setValue', data[0].gx1);
                    $("#ywfc1").textbox('setValue', data[0].ywfc1);

                    $("#glryid2").textbox('setValue', data[0].glryid2);
                    $("#glryxm2").textbox('setValue', data[0].glryxm2);
                    $("#lxfs2").textbox('setValue', data[0].lxfs2);
                    $("#gx2").textbox('setValue', data[0].gx2);
                    $("#ywfc2").textbox('setValue', data[0].ywfc2);

                    $("#glryid3").textbox('setValue', data[0].glryid3);
                    $("#glryxm3").textbox('setValue', data[0].glryxm3);
                    $("#lxfs3").textbox('setValue', data[0].lxfs3);
                    $("#gx3").textbox('setValue', data[0].gx3);
                    $("#ywfc3").textbox('setValue', data[0].ywfc3);

                    $("#glryid4").textbox('setValue', data[0].glryid4);
                    $("#glryxm4").textbox('setValue', data[0].glryxm4);
                    $("#lxfs4").textbox('setValue', data[0].lxfs4);
                    $("#gx4").textbox('setValue', data[0].gx4);
                    $("#ywfc4").textbox('setValue', data[0].ywfc4);

                    $("#ywqy").textbox('setValue', data[0].ywqy);
                    $("#sxed").textbox('setValue', data[0].sxed);
                    $("#ywnl").textbox('setValue', data[0].ywnl);
                    $("#khzt").textbox('setValue', data[0].khzt);
                    $("#glgs").textbox('setValue', data[0].glgs);
                    $("#wlxx").textbox('setValue', data[0].wlxx);
                    $("#khms").textbox('setValue', data[0].khms);
                }
            });
        }


    </script>
    <style>
        tr td {
            border: solid #add9c0;
            border-width: 1px 1px 1px 1px;
            height: 30px;
            text-align: center;
        }

        .yincang {
            display: none
        }
    </style>
</head>
<body>

<div title="khjcxx" id="khjcxx">
    <div style="font-size:20px;font-weight:bold;text-align:center;margin:20px;">�ͻ�������Ϣ��</div>
    <form id="khglform" method="post" name="khglform">
        <table style="width:90%;border-collapse:collapse;font-size:14px;margin-left:5%;" id="khjcxx">
            <tr>
                <td style="font-size:14px;font-weight:bold;text-align:left;padding-left:10px;" colspan='8'>�ͻ�������Ϣ</td>
                </td>
            </tr>
            <tr>
                <td style="width:10%;">����</td>
                <td style="width:15%;"><input id="xm" class="easyui-textbox" name="xm"/></td>
                <td style="width:10%;">�Ա�</td>
                <td><input id="xb" style="width:100px;" class="easyui-combobox" value="" name='xb'
                           data-options=" panelHeight:'auto', valueField:'id',textField:'text',data: [{id: '0',text: '��'},{id: '1',text: 'Ů'}]"/>
                </td>
                <td style="width:10%;">��������</td>
                <td style="width:15%;"><input id="csrq" class="easyui-textbox" name="csrq"/></td>
                <td style="width:10%;">����</td>
                <td style="width:15%;"><input id="jg" class="easyui-textbox" name="jg"/></td>
            </tr>
            <tr>
                <td>�ֻ�</td>
                <td><input id="sj" class="easyui-textbox" name="sj"/></td>
                <td>΢��</td>
                <td><input id="wx" class="easyui-textbox" name="wx"/></td>
                <td>QQ/����</td>
                <td colspan='3'><input id="qq" class="easyui-textbox" name="qq" style="width:50%"/></td>
            </tr>
            <tr>
                <td>��ͥסַ</td>
                <td colspan='7'><input id="zz" class="easyui-textbox" name="zz" style="width:60%"/></td>
            </tr>
        </table>
        <table style="width:90%;border-collapse:collapse;font-size:14px;margin-left:5%"
        " id="glryxx">
        <tr>
            <td style="font-size:14px;font-weight:bold; text-align:left;padding-left:10px;" colspan='8'>������Ա��Ϣ</td>
            </td>
        </tr>


        <tr>
            <td style="width:15%">����</td>
            <td style="width:15%">��ϵ��ʽ</td>
            <td style="width:15%">��ϵ</td>
            <td style="width:55%">ҵ�񷶳�</td>
        </tr>
        <tr>
            <td class="yincang"><input id="glryid1" class="easyui-textbox" name="glryid1"/></td>
            <td><input id="glryxm1" class="easyui-textbox" name="glryxm1"/></td>
            <td><input id="lxfs1" class="easyui-textbox" name="lxfs1"/></td>
            <td><input id="gx1" class="easyui-textbox" name="gx1"/></td>
            <td><input id="ywfc1" class="easyui-textbox" name="ywfc1" style="width:80%"/></td>
        </tr>
        <tr>
            <td class="yincang"><input id="glryid2" class="easyui-textbox" name="glryid2"/></td>
            <td><input id="glryxm2" class="easyui-textbox" name="glryxm2"/></td>
            <td><input id="lxfs2" class="easyui-textbox" name="lxfs2"/></td>
            <td><input id="gx2" class="easyui-textbox" name="gx2"/></td>
            <td><input id="ywfc2" class="easyui-textbox" name="ywfc2" style="width:80%"/></td>
        </tr>
        <tr>
            <td class="yincang"><input id="glryid3" class="easyui-textbox" name="glryid3"/></td>
            <td><input id="glryxm3" class="easyui-textbox" name="glryxm3"/></td>
            <td><input id="lxfs3" class="easyui-textbox" name="lxfs3"/></td>
            <td><input id="gx3" class="easyui-textbox" name="gx3"/></td>
            <td><input id="ywfc3" class="easyui-textbox" name="ywfc3" style="width:80%"/></td>
        </tr>
        <tr>
            <td class="yincang"><input id="glryid4" class="easyui-textbox" name="glryid4"/></td>
            <td><input id="glryxm4" class="easyui-textbox" name="glryxm4"/></td>
            <td><input id="lxfs4" class="easyui-textbox" name="lxfs4"/></td>
            <td><input id="gx4" class="easyui-textbox" name="gx4"/></td>
            <td><input id="ywfc4" class="easyui-textbox" name="ywfc4" style="width:80%"/></td>
        </tr>
        </table>
        <table style="width:90%;border-collapse:collapse;font-size:14px;margin-left:5%"
        " id="glywxx">
        <tr>
            <td style="font-size:14px;font-weight:bold; text-align:left;padding-left:10px;" colspan='8'>����ҵ����Ϣ</td>
            </td>
        </tr>


        <tr>
            <td style="width:15%">ҵ������</td>
            <td colspan='7'><input id="ywqy" class="easyui-textbox" name="ywqy" style="width:60%"/></td>
        </tr>
        <tr>
            <td>���Ŷ��</td>
            <td style="width:20%"><input id="sxed" class="easyui-textbox" name="sxed"/></td>
            <td style="width:15%">ҵ������</td>
            <td style="width:20%"><input id="ywnl" class="easyui-textbox" name="ywnl"/></td>
            <td style="width:15%">�ͻ�״̬</td>
            <td><input id="khzt" class="easyui-textbox" name="khzt"/></td>
        </tr>

        <tr>
            <td style="width:15%">������˾</td>
            <td colspan='7'><input id="glgs" class="easyui-textbox" name="glgs" style="width:60%"/></td>
        </tr>

        <tr>

            <td colspan='6' style="text-align:left;padding:5px;">
                <div style="text-align:left;margin:5px;"><font>������Ϣ</font></div>
                <input id="wlxx" class="easyui-textbox" data-options="multiline:true" name="wlxx"
                       style="width:80%;height:100px;"/></td>
        </tr>
        <tr>
            <td colspan='6' style="text-align:left;padding:5px;">
                <div style="text-align:left;margin:5px;"><font>�ͻ�������Ϣ</font></div>
                <input id="khms" class="easyui-textbox" data-options="multiline:true" name="khms"
                       style="width:80%;height:150px;"/></td>
        </tr>
        </table>
    </form>
</div>
<div region="south" border="false" style="text-align:center;padding:13px 0px;">
    <a class="easyui-linkbutton" iconCls="icon-ok" id="tjbjx">�ύ������</a>
    <a class="easyui-linkbutton" iconCls="icon-ok" id="tjbtc">�ύ���˳�</a>
    <a class="easyui-linkbutton" iconCls="icon-ok" id="tc">�˳�</a>
    <!--a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  style="margin-left:10px" id="gb">�ر�</a>  -->
</div>
</body>
</html> 