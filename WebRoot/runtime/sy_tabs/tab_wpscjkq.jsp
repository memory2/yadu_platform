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
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/copyhtmltoexcel.js"></script>
    <script type="text/javascript" src="js/tableToExcel.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String msg = (String) session.getAttribute("message");
        if (!"ok".equals(msg)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/my_login.jsp");
        }
        String bmmc = (String) session.getAttribute("bmmc") == null ? "" : (String) session.getAttribute("bmmc");
        String bmdm = (String) session.getAttribute("bmdm");
        String oaryid = (String) session.getAttribute("oaryid");

    %>


    <script type="text/javascript">
        var img;
        var mask;
        var byrq;
        var syrq;
        var bmmc = '<%=bmmc%>';
        var txm = '<%=oaryid%>';
        var bmdm = '<%=bmdm%>';
        $(function () {
            img = $("#progressImgage");
            mask = $("#maskOfProgressImage");
            var cx_flag = false;
            var tj_flag = false;
            $("#yf").combobox({
                onSelect: function (n, o) {
                    cx_flag = false;
                    tj_flag = false;
                }
            });

            $("#yf").combobox('setValue', '1');
            //点击查询按钮
            $('#btn_search_kqxx').bind('click', function () {
                tj_flag = true;
                if (!cx_flag && tj_flag) {
                    cx(getYf());
                }

                $('#tj_kqxx').hide();
                $('#btn_export_kqxx').hide();
                $('#dg_kqxx').show();
            });

            //点击统计按钮
            $('#btn_tj_kqxx').bind('click', function () {
                cx_flag = true;
                if (!tj_flag && cx_flag) {
                    cx(getYf());
                }
                $('#tj_kqxx').show();
                $('#btn_export_kqxx').show();
                $('#dg_kqxx').hide();
            });

            $('#btn_export_kqxx').bind('click', function () {
                method1('tjkq');
            });


            if (bmmc == "信息部" || bmmc == "威浦仕人资行政部"|| bmmc == '亚都人资行政部' || bmmc == "威浦仕口罩帽车间" || bmmc == "威浦仕口罩帽车间"|| txm=="10269") {
                bmdm = "7100000000";
            } else if (txm == "10715"||txm == "10855") { //10715-邓雪  10855 宋雪珂
                bmdm = "7102000000";
            }

            if (txm == "11472") {//11472-郭雪-棉制品生产中心-综合一车间
                bmdm = "7101030000";
            } else if (txm == "11159") { //11159-刘楠-棉制品生产中心-纱布片车间、离职了
                bmdm = "7101010000";
            } else if (txm == "11165") { //11165-孙淑飞-棉制品生产中心-棉纱垫车间、纱布车间
                bmdm = "7101000000";
            } else if (txm == "10778") { //10778-刘晓云-无纺布生产中心-口罩帽车间
                bmdm = "7101020000";
            } else if (txm=="11751") { //10855-宋雪珂-无纺布生产中心-组合包车间   11751 李冰洁（威浦仕）
                bmdm = "7102030000";
            } else if (txm == "11402"||txm == "11692" ) {//11402-马妍-无纺布生产中心-手术衣车间  11692 张爽
                bmdm = "7102020000";
            } else if (txm == "11118") {//11118-王九平-无纺布生产中心-裁剪车间
                bmdm = "7102010000";
            } else if (txm == "11320") {//11076-于利倩-无纺布生产中心-无纺布机折车间
                bmdm = "7102040000";
            } else if (txm == "11153") {//11153-朱梦奇-高分子生产中心-所有车间
                bmdm = "7103000000";
            } else if (txm == "10948"){ //10948-岳聪慧
                bmdm = "7101000000";
            } else if (txm == "11393") { //李薇
                bmdm = "7102030000";
            }

            getDept("bm", bmdm);
            $("#xm").combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#bm").combobox({
                onChange: function (n, o) {
                    if (txm == "10715" && $("#bm").combobox('getValue').substring(0, 4) == "7103") {
                        alert("您无此部门查询权限！");
                        $("#bm").combotree('setValue', bmdm);
                        return;
                    } else if (txm == "11165" && $("#bm").combobox('getValue') == "7101030000") {
                        alert("您无此部门查询权限！");
                        $("#bm").combotree('setValue', "");
                        return;
                    } else if (txm == "11165" && $("#bm").combobox('getValue') == "7101000000") {
                        alert("请选择子部门！");
                        $("#bm").combotree('setValue', "");
                        //$('#btn_search_kqxx').combobox({disabled:true});
                        //$('#btn_search_kqxx').combobox({disabled:true});
                        return;
                    }
                    yzry1("bm", "xm");
                    cx_flag = false;
                    tj_flag = false;

                }
            });

            $("#xm").combobox({
                onChange: function (n, o) {
                    cx_flag = false;
                    tj_flag = false;
                }
            });

            $('#bm').combobox({disabled: true});
            $('#xm').combobox({disabled: true});
            if ((bmmc == '信息部' || bmmc == '威浦仕人资行政部' || bmmc == '亚都人资行政部'|| bmmc == "威浦仕口罩帽车间" || txm == "10715" || txm == "11153" || txm == "11165" || txm == "10948"|| txm=="10269"|| txm == "10855")) {
                $('#bm').combobox({disabled: false});
                $('#xm').combobox({disabled: false});
            }

            if (txm == "10948"  || txm == "11402" ||txm == "11692"||txm=="11751"|| txm == "11118" || txm == "11320" || txm == "11159" || txm == "11165" || txm == "11472" || txm == "10778"||txm == "11393") {
                $('#xm').combobox({disabled: false});
            }

            if(bmdm=='6102030000'){
                bmdm=7102030000;
                $("#bm").combotree('setValue', bmdm);
            }else {
                $("#bm").combotree('setValue', bmdm);
            }

            if (txm != "11472"&&txm != "11665" && txm != "11159" && txm != "10778" && txm != "10855" && txm != "11402" &&txm != "11692" &&txm!="11751"&& txm != "11118" &&txm!="10269"&& txm != "10715" && txm != "11320" && txm != "11165" && txm != "11153" && bmmc != "信息部" && bmmc != "威浦仕口罩帽车间" && bmmc != "人力资源部" && bmmc != "威浦仕人资行政部"&&txm != "10948"&&txm != "11393") {
                $("#xm").combobox('setValue', txm);
                cx(getYf());
            }


        });


        /**
         * 获取上一个月
         *
         * @date 格式为yyyy-mm-dd的日期，如：2014-01-25
         */
        function getPreMonth(date) {
            var arr = date.split('-');
            var year = arr[0]; //获取当前日期的年份
            var month = arr[1]; //获取当前日期的月份
            var day = arr[2]; //获取当前日期的日
            var days = new Date(year, month, 0);
            days = days.getDate(); //获取当前日期中月的天数
            var year2 = year;
            var month2 = parseInt(month) - 1;
            if (month2 == 0) {//如果是1月份，则取上一年的12月份
                year2 = parseInt(year2) - 1;
                month2 = 12;
            }
            var day2 = day;
            var days2 = new Date(year2, month2, 0);
            days2 = days2.getDate();
            if (day2 > days2) {//如果原来日期大于上一月的日期，则取当月的最大日期。比如3月的30日，在2月中没有30
                day2 = days2;
            }
            if (month2 < 10) {
                month2 = '0' + month2;//月份填补成2位。
            }
            var t2 = year2 + '-' + month2 + '-' + day2;
            return t2;
        }

        function getYf() {
            var pdyf = $("#yf").combobox('getValue');
            var myDate = new Date();
            byrq = myDate.getFullYear() + "-" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
            if ((myDate.getMonth() + 1) < 10) {
                syrq = getPreMonth(byrq);
                byrq = myDate.getFullYear() + "-0" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
            } else {
                syrq = getPreMonth(byrq);
            }
            if (pdyf == 1) {
                return byrq.substring(0, 7);
            } else {
                return syrq.substring(0, 7);
            }
            return null;
        }

        //返回顶部
        window.onscroll = function () {
            if (document.documentElement.scrollTop) {
                $("#fhdb").show();
            } else if (document.body.scrollTop) {
                $("#fhdb").show();
            } else {
                $("#fhdb").hide();
            }
        }


        function cx(rq) {
            var kqy = rq.substring(0, 4) + "年" + rq.substring(5, 7) + "月";
            //if(cx_flag){
            var xm = $("#xm").combobox('getValue');
            var bm = $("#bm").combobox('getValue');
            if (bm == "" || bm == null || bm == undefined) {
                alert("请选择部门！");
                return;
            }


            // alert(bm+":"+xm);
            var url = 'servlet/KqAction?method=kqxx_wpscj_pc&cxsj=' + rq + '&cxxm=' + xm + '&cxbm=' + bm;
            $.ajax({
                type: 'POST',
                dataType: "json",
                url: url,
                beforeSend: function (xhr) {
                    img.show().css({
                        "position": "fixed",
                        "top": "40%",
                        "left": "45%",
                        "margin-top": function () {
                            return -1 * img.height() / 2;
                        },
                        "margin-left": function () {
                            return -1 * img.width() / 2;
                        }
                    });
                    mask.show().css("opacity", "0.1");
                },
                success: function (data) {
                    // alert(JSON.stringify(data));
                    //var jsonData  = JSON.stringify(data[0]);
                    //$("#dg_kqxx").html(JSON.stringify(data));
                    if (data.length > 0) {

                        var myfj = "";

                        var kqtj = "<table id='tjkq'><tr><td style='font-size:14px;font-weight:bold' colspan=18>威浦仕" + kqy + "车间考勤汇总表</td></tr><tr><td  class='xh' rowspan=2>序号</td><td class='nr_bt' rowspan=2>部门</td><td class='nr_bt' rowspan=2>工号</td><td class='nr_bt' rowspan=2>姓名</td><td class='nr_bt' colspan=2>出勤天数</td><td class='nr_bt' colspan=2>休假天数</td><td class='nr_bt' colspan=4>迟到、早退次数</td></tr><tr><td class='nr_bt'>应出勤</td><td class='nr_bt'>实际出勤</td><td class='nr_bt'>公司放假</td><td class='nr_bt'>星期日个数</td><td class='nr_bt'>迟到15分钟以内</td><td class='nr_bt'>迟到15分钟以上</td><td class='nr_bt'>早退</td><td class='nr_bt'>合计</td></tr>";
                        var kqnr = "<div id='kqy_tj'>" + kqy + "车间人员考勤记录</div>";
                        var mqts = "";
                        var byjb = 0;
                        var qjts = "";
                        var gxts = "";
                        var js = 0;
                        var cfjbms = "";
                        for (var key in data) {
                            var mykq = data[key];
                            var xq = "<td class='bt'>星期</td>";
                            var rq = "<td>日期</td>";
                            var swsb = "<td>上班</td>";
                            var xwxb = "<td>下班</td>";
                            var gh = "";
                            var xm = "";
                            var bm = "";
                            var ykqts = "";
                            var cdcs = "";
                            var cdcs_15 = "";
                            var ztcs = "";
                            js++;
                            //alert(JSON.stringify(mykq));
                            if (mykq != null && mykq != "" && mykq != undefined) {
                                //alert(JSON.stringify(data));
                                for (var i = 0; i < mykq.length; i++) {
                                    var kq = {};
                                    kq = mykq[i];
                                    xq += "<td class='nr'>" + kq['xq'] + "</td>";
                                    rq += "<td>" + kq['rq'] + "</td>";
                                    var s1 = kq['swsb'];
                                    var s4 = kq['xwxb'];
                                    //alert(s2);
                                    if (s1 != undefined) {
                                        if (s1 == '无') {
                                            swsb += "<td class='wqd'>" + s1 + "</td>";//灰色
                                        } else if (s1.substring(0, 1) == '正') {
                                            swsb += "<td class='zc'>" + s1 + "</td>";
                                        } else if (s1.substring(0, 1) == '迟' || s1.substring(0, 1) == '早') {
                                            swsb += "<td class='cdzt'>" + s1 + "</td>";
                                        } else {
                                            swsb += "<td class='qt'>" + s1 + "</td>";
                                        }
                                    }


                                    if (s4 != undefined) {
                                        if (s4 == '无') {
                                            xwxb += "<td class='wqd'>" + s4 + "</td>";//灰色
                                        } else if (s4.substring(0, 1) == '正' || s4 == '因公补签') {
                                            xwxb += "<td class='zc'>" + s4 + "</td>";
                                        } else if (s4.substring(0, 1) == '迟' || s4.substring(0, 1) == '早') {
                                            xwxb += "<td class='cdzt'>" + s4 + "</td>";
                                        } else {
                                            xwxb += "<td class='qt'>" + s4 + "</td>";
                                        }
                                    }


                                    gh = kq['gh'];
                                    xm = kq['xm'];
                                    bm = kq['bm'];
                                    mqts = kq['mqts'];
                                    ykqts = kq['ykqts'];
                                    cdcs = kq['cdcs'];
                                    cdcs_15 = kq['cdcs_15'];
                                    ztcs = kq['ztcs'];
                                    gxts = kq['gxts'];
                                    if (kq['cfjbms'] != "") {
                                        cfjbms += kq['cfjbms'] + "；";
                                    }
                                    myfj = kq['xqtgs'];
                                }

                            }


                            var hjcs = cdcs + cdcs_15 + ztcs;
                            if (xm != "" && xm != null && xm != undefined) {
                                kqnr += "<table id='ydkq'><tr><td colspan='" + (mykq.length + 1) + "' class='rycs'>No." + js + "、部门：" + bm + "，工号：" + gh + "，姓名：" + xm + "，" + kqy + "应出勤：" + mqts + "天，实际出勤：" + ykqts +
                                    "天，迟到15分钟内" + cdcs + "次，迟到15分钟以上" + cdcs_15 + "次，早退" + ztcs + "次</td></tr><tr>" + xq + "</tr><tr>" + rq + "</tr><tr>" + swsb + "</tr><tr>" + xwxb + "</tr></table><br/>";
                                kqtj += "<tr><td>" + js + "</td><td>" + bm + "</td><td>" + gh + "</td><td>" + xm + "</td><td>" + mqts + "</td><td>" + ykqts + "</td><td>" + gxts + "</td><td>" + myfj + "</td><td>" + cdcs + "</td><td>" + cdcs_15 + "</td><td>" + ztcs + "</td><td>" + hjcs + "</td></tr>";
                            } else {
                                js--;
                            }
                        }
                        //kqtj +="<tr><td colspan=18 class='bzsm'>备注："+cfjbms+"</td></tr>";
                        $("#dg_kqxx").html(kqnr);
                        $("#tj_kqxx").html(kqtj + "</table>");
                    } else {
                        $("#dg_kqxx").html("<div>无考勤记录！</div>");
                        $("#tj_kqxx").html("<div>无考勤记录！</div>");
                    }
                    img.hide();
                    mask.hide();
                },
                complete: function (xhr) {
                    //img.hide();
                    // mask.hide();
                }
            });
            // }

        }


        function method1(tableid) {

            var curTbl = document.getElementById(tableid);
            var oXL = new ActiveXObject("Excel.Application");
            var oWB = oXL.Workbooks.Add();
            var oSheet = oWB.ActiveSheet;
            var sel = document.body.createTextRange();
            sel.moveToElementText(curTbl);
            sel.select();
            sel.execCommand("Copy");
            oSheet.Paste();
            oXL.Visible = true;

        }


    </script>
    <style>
        tr td {
            border: solid #add9c0;
            border-width: 1px 1px 1px 1px;
            padding: 3px 0px;
            text-align: center;
            font-size: 12px;
            white-space: 0;
            width: 50px;
        }

        .bt_td {
            height: 35px;
        }

        .wqd {
            height: 35px;
            color: #CCD0FF
        }

        .zc {
            height: 35px;
            color: #008000
        }

        .gx {
            height: 35px;
            color: #800080
        }

        .cc {
            height: 35px;
            color: #0000FF
        }

        .cdzt {
            height: 35px;
            color: #FF4500
        }

        .bq {
            height: 35px;
            color: #0000FF
        }

        .qj {
            heght: 35px;
            color: #FF0000
        }

        .qt {
            heght: 35px;
            color: #FF8C00
        }

        .mq {
            height: 35px;
            color: #9932CC
        }

        #nf, #yf {
            width: 70px
        }

        #dg_kqxx {
            width: 1900px !important;
            border-collapse: collapse;
            padding-right: 20px;
        }

        #tj_kqxx {
            width: 100% !important;
            border-collapse: collapse;
            padding-right: 15px;
            overflow: auto
        }

        .rycs {
            text-align: left;
            padding-left: 5px;
            font-weight: bold;
        }

        .bt {
            width: 70px;
        }

        .nr {
            width: 60px;
        }

        .nr_bt {
            width: 100px;
        }

        .xh {
            width: 40px;
        }

        #kqy_tj {
            font-size: 14px;
            font-weight: bold;
            padding: 5px;
        }

        .progress {
            z-index: 2000
        }

        .mask {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            z-index: 1000;
            background-color: #2F2F2F
        }

        #fhdb {
            font-size: 12px;
            text-align: right;
            padding-right: 15px;
            display: none
        }

        .bzsm {
            text-align: left;
            padding-left: 5px;
            color: red
        }
    </style>
</head>


<body>
<div title="考勤信息" style="padding:10px;">
    <div>
        <form id="jbxxForm" method="post">
            <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;" id="cxtj">
                <tr>
                    <td style="width:100px;">选择月份</td>
                    <td style="width:180px;"><input id="yf" class="easyui-combobox" value="" name='yf'
                                                    data-options=" panelHeight:'auto', valueField:'id',textField:'text',data: [{id: '1',text: '本月'},{id: '2',text: '上月'}]"/>
                    </td>
                    <td style="width:100px;text-align:center">所属部门</td>
                    <td style="width:180px;"><input id="bm" class="easyui-combotree"
                                                    data-options="valueField:'id',panelHeight:'auto',textField:'text'"
                                                    name="ssbm" style="width:150px;"/>
                    </td>
                    <td style="width:100px;">姓名</td>
                    <td style="width:180px;"><input id="xm" class="easyui-combobox" name="xm" style="width:150px;"
                                                    data-options="panelHeight:'260'"/></td>
                    <td style="width:300px;">
                        <a id="btn_search_kqxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btn_tj_kqxx" class="easyui-linkbutton" data-options="iconCls:'icon-search'">统计</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btn_export_kqxx" class="easyui-linkbutton" data-options="iconCls:'icon-dload'"
                           style="display:none">导出统计</a>
                    </td>
                <tr>
            </table>
        </form>
    </div>
    <br/>

    <br/><br/>
    <div id="dg_kqxx"></div>
    <div id="tj_kqxx" style="display:none"></div>
</div>

<img id="progressImgage" class="progress" style="display:none" alt="" src="runtime/sy_tabs/ajax-loader.gif"/>
<div id="maskOfProgressImage" class="mask" style="display:none"></div>
<div id="fhdb"><a href="javascript:scroll(0,0)">返回顶部</a></div>

</body>
</html> 