<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">
    <title>参数管理</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/datagrid-export.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript">
        $(function () {

            $("#dlg").dialog({
                width: 400,
                closed: true,
                buttons: [{
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#ff').form('submit', {
                            url: 'servlet/TreeDataAction?method=insertAndUpdateFitData',
                            onSubmit: function () {
                                return $("#ff").form("validate");
                            },
                            success: function (data) {
                                var obj = $.parseJSON(data);
                                $.messager.show({
                                    title: '提示',
                                    msg: obj.msg
                                });
                                if (obj.success) {
                                    $("#dlg").dialog("close");
                                    //$("#dg").treegrid("reload");
                                }
                            }
                        });
                    }
                }, {
                    text: '重置',
                    iconCls: 'icon-cancel'
                }]
            });
            var toolbar = [{
                text: '添加顶级类别',
                iconCls: 'icon-add',
                handler: function () {
                    $("#ff").form("clear");
                    $("#parentId").val(0);
                    $("#dlg").dialog("setTitle", "添加顶级类别").dialog("open");
                }
            }, '-', {
                text: '修改',
                iconCls: 'icon-edit',
                handler: function () {
                    var row = $("#dg").datagrid("getSelected");
                    if (row) {
                        $("#ff").form("load", row);
                        $("#parentId").val(row._parentId);
                        $("#dlg").dialog("setTitle", "修改参数").dialog("open");
                    }
                }
            }, '-', {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var row = $("#dg").datagrid("getSelected");
                    if (row) {
                        $.messager.confirm('确认', '您确认删除数据吗？', function (r) {
                            if (r) {
                                $.post("servlet/TreeDataAction?method=delFitData", {id: row.id}, function (obj) {
                                    $.messager.show({
                                        title: '提示',
                                        timeout: 3000,
                                        msg: obj.msg
                                    });
                                    if (obj.success) {
                                        $("#dg").treegrid("reload");
                                    }
                                }, "json");
                            }
                        });
                    }
                }
            }, '-', {
                text: '折叠所有',
                handler: function () {
                    collapseAll();
                }
            }, '-', {
                text: '展开所有',
                handler: function () {
                    expandAll();
                }
            }, '-', {
                text: '刷新',
                iconCls: 'icon-reload',
                handler: function () {
                    //expandAll();
                    refresh();
                }
            }];
            $("#dg").treegrid({
                title: '参数列表',
                toolbar: toolbar,
                url: "servlet/TreeDataAction?method=queryFitData",
                fit: true,
                border: false,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                remoteSort: false,
                animate: true,
                idField: 'id',
                treeField: 'text',
                columns: [[{
                    field: 'text',
                    title: '名称',
                    width: 100
                }]],
                onContextMenu: createMenu,
                onLoadSuccess: function () {
                    $('#dg').treegrid('collapseAll');
                }

            });

            function createMenu(e, row) {
                e.preventDefault();
                $(this).treegrid('select', row.id);
                $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            }
        });

        function refresh() {
            $("#dg").treegrid("reload");
        }

        function collapseAll() {
            $('#dg').treegrid('collapseAll');
        }

        function expandAll() {
            $('#dg').treegrid('expandAll');
        }

        function collapse() {
            var node = $('#dg').treegrid('getSelected');
            if (node) {
                $('#dg').treegrid('collapse', node.id);
            }
        }

        function expand() {
            var node = $('#dg').treegrid('getSelected');
            if (node) {
                $('#dg').treegrid('expand', node.id);
            }
        }

        function removeIt() {
            var row = $("#dg").datagrid("getSelected");
            if (row) {
                $.messager.confirm('确认', '您确认删除数据吗？', function (r) {
                    if (r) {
                        $.post("servlet/TreeDataAction?method=delFitData", {id: row.id}, function (obj) {
                            $.messager.show({
                                title: '提示',
                                msg: obj.msg
                            });
                            if (obj.success) {
                                $("#dg").treegrid("reload");
                            }
                        }, "json");
                    }
                });
            }
        }

        function append() {
            var row = $('#dg').treegrid('getSelected');
            if (row) {
                $("#ff").form("clear");
                $("#parentId").val(row.id);
                $("#dlg").dialog("setTitle", "追加子类别").dialog("open");
            }
        }
    </script>
</head>

<body class="easyui-layout">
<table id="dg" data-options="region:'center'"></table>
<div id="dlg" style="padding: 5px;">
    <form id="ff" method="post">
        <input type="hidden" name="id"/>
        <input id="parentId" type="hidden" name="parentId"/>
        <table>
            <tr>
                <td>类别名称</td>
                <td>
                    <input name="text" class="easyui-textbox" style="width:200px;height:32px"
                           data-options="required:true,prompt:'请输入名称',iconCls:'myicon-key'"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="mm" class="easyui-menu" style="width:120px;">
    <div onclick="append()" data-options="iconCls:'icon-add'">添加子类别</div>
    <div onclick="removeIt()" data-options="iconCls:'icon-remove'">删除类别</div>
    <div onclick="refresh()" data-options="iconCls:'icon-reload'">刷新</div>
    <div class="menu-sep"></div>
    <div onclick="collapse()">折叠所有</div>
    <div onclick="expand()">展开所有</div>
</div>
</body>
</html>
