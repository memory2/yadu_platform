<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,inital-scale=1.0,minimum-scale=0.5,maximum-scale=2.0,user-scalable=no">
    <title>考勤查询</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <script type="text/javascript" src="js/copyhtmltoexcel.js"></script>
    <script type="text/javascript" src="js/tableToExcel.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <%
        String wxid = request.getParameter("wxid");
        String gs = request.getParameter("gs");
    %>

    <script type="text/javascript">
        var img;
        var mask;
        var byrq;
        var syrq;
        var wxid;
        var gs;
        $(function () {

            img = $("#progressImgage");
            mask = $("#maskOfProgressImage");
            wxid = '<%=wxid%>';
            gs = '<%=gs%>';

            var myDate = new Date();
            if ((myDate.getMonth() + 1) < 10) {
                byrq = myDate.getFullYear() + "-0" + (myDate.getMonth() + 1);
                var lastMonthDate = new Date(); //上月日期
                lastMonthDate.setDate(1);
                lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
                syrq = lastMonthDate.getFullYear() + "-0" + (lastMonthDate.getMonth() + 1);
                //syrq="2017-01";
            } else {

                byrq = myDate.getFullYear() + "-" + (myDate.getMonth() + 1);
                syrq = myDate.getFullYear() + "-" + myDate.getMonth();
            }


            cx(byrq);

            $('#sykq').bind('click', function () {
                $('#sykq').hide();
                $('#bykq').show();

                cx(syrq);
            });

            $('#bykq').bind('click', function () {
                $('#bykq').hide();
                $('#sykq').show();
                cx(byrq);
            });


            $("#dwbs").combobox({
                onChange: function (n, o) {
                    dwbs = $('#dwbs').combobox('getValue');
                    cx(byrq);
                }
            });
            //$("#dwbs").combobox('setValue',1);
            //dwbs=$('#dwbs').combobox('getValue');
            $("#dwbs").combobox('setValue', gs);
        });


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
            //alert(bm+":"+xm);
            var url = 'servlet/OaAction?method=kqxx_wx&kqsj=' + rq + '&wxid=' + wxid + '&dwbs=' + gs;
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
                    //alert(JSON.stringify(data));
                    //var jsonData  = JSON.stringify(data[0]);
                    //$("#dg_kqxx").html(JSON.stringify(data));
                    if (data.length > 0) {


                        var kqtj = "<table id='tjkq'><tr><td style='font-size:14px;font-weight:bold' colspan=18>亚都公司" + kqy + "考勤汇总表</td></tr><tr><td  class='xh' rowspan=2>序号</td><td class='nr_bt' rowspan=2>部门</td><td class='nr_bt' rowspan=2>工号</td><td class='nr_bt' rowspan=2>姓名</td><td class='nr_bt' colspan=2>出勤天数</td><td class='nr_bt' colspan=6>休假天数</td><td class='nr_bt' rowspan=2>加班小时数</td><td class='nr_bt' colspan=5>迟到、早退、补签次数</td></tr><tr><td class='nr_bt'>应出勤</td><td class='nr_bt'>实际出勤</td><td class='nr_bt'>公休</td><td class='nr_bt'>放假</td><td class='nr_bt'>事假</td><td class='nr_bt'>婚假</td><td class='nr_bt'>丧假</td><td class='nr_bt'>工伤</td><td class='nr_bt'>迟到15分钟以内</td><td class='nr_bt'>迟到15分钟以上</td><td class='nr_bt'>早退</td><td class='nr_bt'>补签</td><td class='nr_bt'>合计</td></tr>";
                        var kqnr = "<div id='kqy_tj'>" + kqy + "考勤记录</div>";
                        var mqts = "";
                        var byjb = 0;
                        var ccts = "";
                        var qjts = "";
                        var qjts_hj = "";
                        var qjts_sj = "";
                        var qjts_gs = "";
                        var bqcs = "";
                        var gxts = "";
                        var js = 0;
                        var cfjbms = "";
                        var kqnr_wx = "<tr><td>日期</td><td >星期</td><td class='nr'>上午上班</td><td class='nr'>上午下班</td><td class='nr'>下午上班</td><td class='nr'>下午下班</td><td>加班时长</td></tr>";
                        for (var key in data) {
                            var mykq = data[key];
                            var xq = "";
                            var rq = "";
                            var swsb = "";
                            var swxb = "";
                            var xwsb = "";
                            var xwxb = "";
                            var jb = "";
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
                                    xq = "<td >" + kq['xq'] + "</td>";
                                    rq = "<td>" + kq['rq'] + "</td>";
                                    var s1 = kq['swsb'];
                                    var s2 = kq['swxb'];
                                    var s3 = kq['xwsb'];
                                    var s4 = kq['xwxb'];
                                    //alert(s2);
                                    if (s1 != undefined) {
                                        if (s1 == '无') {
                                            swsb = "<td class='wqd'>" + s1 + "</td>";//灰色
                                        } else if (s1.substring(0, 1) == '正') {
                                            swsb = "<td class='zc'>" + s1 + "</td>";
                                        } else if (s1 == '事假' || s1 == '婚假' || s1 == '丧假' || s1 == '工伤') {
                                            swsb = "<td class='qj'>" + s1 + "</td>";
                                        } else if (s1 == '出差') {
                                            swsb = "<td class='cc'>" + s1 + "</td>";
                                        } else if (s1 == '补签') {
                                            swsb = "<td class='bq'>" + s1 + "</td>";
                                        } else if (s1 == '免签') {
                                            swsb = "<td class='mq'>" + s1 + "</td>";
                                        } else if (s1.substring(0, 1) == '迟' || s1.substring(0, 1) == '早') {
                                            swsb = "<td class='cdzt'>" + s1 + "</td>";
                                        } else {
                                            swsb = "<td class='qt'>" + s1 + "</td>";
                                        }
                                    }

                                    if (s2 != undefined) {
                                        if (s2 == '无') {
                                            swxb = "<td class='wqd'>" + s2 + "</td>";//灰色
                                        } else if (s2.substring(0, 1) == '正') {
                                            swxb = "<td class='zc'>" + s2 + "</td>";
                                        } else if (s2 == '事假' || s2 == '婚假' || s2 == '丧假' || s2 == '工伤') {
                                            swxb = "<td class='qj'>" + s2 + "</td>";
                                        } else if (s2 == '出差') {
                                            swxb = "<td class='cc'>" + s2 + "</td>";
                                        } else if (s2 == '补签') {
                                            swxb = "<td class='bq'>" + s2 + "</td>";
                                        } else if (s2 == '免签') {
                                            swxb = "<td class='mq'>" + s2 + "</td>";
                                        } else if (s2.substring(0, 1) == '迟' || s2.substring(0, 1) == '早') {
                                            swxb = "<td class='cdzt'>" + s2 + "</td>";
                                        } else {
                                            swxb = "<td class='qt'>" + s2 + "</td>";
                                        }
                                    }


                                    if (s3 != undefined) {
                                        if (s3 == '无') {
                                            xwsb = "<td class='wqd'>" + s3 + "</td>";//灰色
                                        } else if (s3.substring(0, 1) == '正') {
                                            xwsb = "<td class='zc'>" + s3 + "</td>";
                                        } else if (s3 == '事假' || s3 == '婚假' || s3 == '丧假' || s3 == '工伤') {
                                            xwsb = "<td class='qj'>" + s3 + "</td>";
                                        } else if (s3 == '出差') {
                                            xwsb = "<td class='cc'>" + s3 + "</td>";
                                        } else if (s3 == '补签') {
                                            xwsb = "<td class='bq'>" + s3 + "</td>";
                                        } else if (s3 == '免签') {
                                            xwsb = "<td class='mq'>" + s3 + "</td>";
                                        } else if (s3.substring(0, 1) == '迟' || s3.substring(0, 1) == '早') {
                                            xwsb = "<td class='cdzt'>" + s3 + "</td>";
                                        } else {
                                            xwsb = "<td class='qt'>" + s3 + "</td>";
                                        }
                                    }


                                    if (s4 != undefined) {
                                        if (s4 == '无') {
                                            xwxb = "<td class='wqd'>" + s4 + "</td>";//灰色
                                        } else if (s4.substring(0, 1) == '正') {
                                            xwxb = "<td class='zc'>" + s4 + "</td>";
                                        } else if (s4 == '事假' || s4 == '婚假' || s4 == '丧假' || s4 == '工伤') {
                                            xwxb = "<td class='qj'>" + s4 + "</td>";
                                        } else if (s4 == '出差') {
                                            xwxb = "<td class='cc'>" + s4 + "</td>";
                                        } else if (s4 == '补签') {
                                            xwxb = "<td class='bq'>" + s4 + "</td>";
                                        } else if (s4 == '免签') {
                                            xwxb = "<td class='mq'>" + s4 + "</td>";
                                        } else if (s4.substring(0, 1) == '迟' || s4.substring(0, 1) == '早') {
                                            xwxb = "<td class='cdzt'>" + s4 + "</td>";
                                        } else {
                                            xwxb = "<td class='qt'>" + s4 + "</td>";
                                        }
                                    }


                                    //swsb += "<td class='bt_td'>"+kq['swsb']+"</td>";
                                    //swxb += "<td class='bt_td'>"+kq['swxb']+"</td>";
                                    // xwsb += "<td class='bt_td'>"+kq['xwsb']+"</td>";
                                    // xwxb += "<td class='bt_td'>"+kq['xwxb']+"</td>";

                                    if (kq['cfjbcs'] == 0) {
                                        jb = "<td>" + kq['jbzgs'] + "</td>";
                                    } else {
                                        jb = "<td>" + kq['jbzgs'] + "(重" + kq['cfjbcs'] + ")</td>";
                                    }

                                    kqnr_wx += "<tr>" + rq + xq + swsb + swxb + xwsb + xwxb + jb + "</tr>";
                                    gh = kq['gh'];
                                    xm = kq['xm'];
                                    bm = kq['bm'];
                                    mqts = kq['mqts'];
                                    ykqts = kq['ykqts'];
                                    cdcs = kq['cdcs'];
                                    cdcs_15 = kq['cdcs_15'];
                                    ztcs = kq['ztcs'];
                                    byjb = kq['byjb'];
                                    gxts = kq['gxts'];
                                    qjts = kq['qjts'];
                                    qjts_hj = kq['qjts_hj'];
                                    qjts_sj = kq['qjts_sj'];
                                    qjts_gs = kq['qjts_gs'];
                                    bqcs = kq['bqcs'];
                                    ccts = kq['ccts'];
                                    if (kq['cfjbms'] != "") {
                                        cfjbms += kq['cfjbms'] + "；";
                                    }

                                }

                            }
                            var hjcs = 0;
                            hjcs = cdcs + cdcs_15 + bqcs + ztcs;

                            if (xm != "" && xm != null && xm != undefined) {
                                kqnr += "<table id='ydkq'><tr><td colspan='7' >工号：" + gh + "，部门：" + bm + "，姓名：" + xm + "</td></tr>" + kqnr_wx +
                                    "</table><table style='width:344px;'><tr><td >本月应出勤</td><td>" + mqts + "</td><td>实际出勤</td><td>" + ykqts + "</td></tr><tr><td>请假天数</td><td>" + (qjts + qjts_hj + qjts_sj + qjts_gs) + "</td><td>加班小时</td><td>" + byjb + "</td></tr><tr><td>迟到15分钟以内</td><td>" + cdcs + "</td><td>迟到15分钟以上</td><td>" + cdcs_15 + "</td></tr><tr><td>早退</td><td>" + ztcs + "</td><td>补签次数</td><td>" + bqcs + "</td></tr></table>";
                            } else {
                                js--;
                            }
                        }
                        kqtj += "<tr><td colspan=18 class='bzsm'>备注：" + cfjbms + "</td></tr>";
                        $("#dg_kqxx").html(kqnr);
                    } else {
                        $("#dg_kqxx").html("<div>无考勤记录！</div>");
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


    </script>
    <style>
        tr td {
            border: solid #add9c0;
            border-width: 1px 1px 1px 1px;
            padding: 3px 0px;
            text-align: center;
            font-size: 12px;
            white-space: 0;
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
            color: #DAA520
        }

        .mq {
            height: 35px;
            color: #9932CC
        }

        #nf, #yf {
            width: 70px
        }

        #dg_kqxx {
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
            width: 100px;
        }

        .nr {
            width: 60px;
        }

        .nr_bt {
            width: 79px;
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
            text-align: left;
            padding-left: 15px;
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

    <table style="width:100%;border-collapse:collapse;text-align:center;font-size:12px;">
        <tr>
            <td style="display:none1">
                <font style="font-size:12px;">公司类别：</font><input id="dwbs" name="dwbs" class="easyui-combobox"
                                                                 data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':5,'text':'亚都实业'},{'id':6,'text':'威浦仕'}]"
                                                                 style="width:100px;"/>
            </td>
            <td>
                <a id="sykq" class="easyui-linkbutton" data-options="iconCls:'icon-search'">上月考勤</a>
                <a id="bykq" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   style="display:none">本月考勤</a></td>
        </tr>
    </table>


    <div id="dg_kqxx" style="width:100%"></div>
</div>
<img id="progressImgage" class="progress" style="display:none" alt="" src="runtime/sy_tabs/ajax-loader.gif"/>
<div id="maskOfProgressImage" class="mask" style="display:none"></div>
<div id="fhdb"><a href="javascript:scroll(0,0)">返回顶部</a></div>

</body>
</html> 