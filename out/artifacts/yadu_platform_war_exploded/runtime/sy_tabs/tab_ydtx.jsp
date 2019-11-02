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
    <title>人员信息管理</title>
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
            //getDept("ssbm",bmdm);
            //setDefaultDate('sqksrq','sqjsrq');
            //$("#gslb").combobox('setValue',bmdm.substring(0,2));
            cx();
            $('#btn_search_ydtx').bind('click', function () {
                cx();
            });
            /* $("#gslb").combobox({
              onChange: function (n,o) {
                  var gs = $("#gslb").combobox('getValue');
                  if(gs==41){
                      getDept("ssbm","4100000000");
                  }else if(gs==51){
                      getDept("ssbm","5100000000");
                  }else{
                      getDept("ssbm","6100000000");
                  }
              }
            });*/
        });

        function cx() {
            var lxdh = $("#lxdh").val();
            var lxr = $("#lxr").val();
            //alert(sqksrq+"--"+jbzgs);
            var url = 'servlet/KqAction?method=sy_ydtx&lxr=' + lxr + '&lxdh=' + lxdh;
            var gridID = "dg_ydtx";
            var gridBT = "亚都通讯";


            var xm = '<%=xm%>';
            getDataGird(url, gridID, gridBT);
            /*if(xm!='系统管理员'){
                getDataGird(url,gridID,gridBT);
            }else{
                $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
                var updUrl= 'runtime/sy_tabs/tab_ydtx_xx.jsp';
                var delUrl = 'servlet/KqAction?method=xx_del_ydtx';
                getDataGird1(url,gridID,gridBT,updUrl,delUrl,'900','200');
            }*/
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


<div title="亚都通讯" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <!--  <td style="width:100px;">
                   公司类别：</td>
                   <td><input id="gslb" name="gslb"  class="easyui-combobox" data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'亚都实业'},{'id':'61','text':'威浦仕'},{'id':'51','text':'原料事业部'}]"  style="width:150px;"/>
                   </td >
                      <td style="width:100px;text-align:center">所属部门</td>
                      <td><input id="ssbm" class="easyui-combotree" data-options="valueField:'id',panelHeight:'260',textField:'text'"  name="ssbm" style="width:150px;"/>
                      </td>-->
                    <td style="width:100px;">联系人</td>
                    <td><input id="lxr" class="easyui-textbox" name="lxr" style="width:150px;"/></td>
                    <td style="width:100px;">联系电话</td>
                    <td>
                        <input id="lxdh" class="easyui-textbox" name="lxdh" style="width:150px;"/>
                    </td>
                    <td><a id="btn_search_ydtx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
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


    <br/>

    <table id="dg_ydtx" title="亚都通讯列表" class="easyui-datagrid" style="width:100%;height:390px;"
           collapsible="true" rownumbers="true" striped="true" fitColumns="true"
           singleSelect="true">
        <thead>
        <tr>
            <th field="id" width=60px; sortable=true, checkbox=true>主键ID</th>
            <th field="dw" width=150px;>部门</th>
            <th field="xm" width=150px;>姓名</th>
            <th field="sjhm" width=150px;>手机号码</th>
            <th field="dh" width=120px;>短号</th>
            <th field="gh" width=120px;>固话</th>
            <th field="fjh" width=130px;>办公室</th>

        </tr>
        </thead>
    </table>
</div>

</body>
</html> 