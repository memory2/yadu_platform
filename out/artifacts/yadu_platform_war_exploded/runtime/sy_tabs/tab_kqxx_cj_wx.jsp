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
    <title>车间人员考勤</title>
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
        String gslb = request.getParameter("gs");
    %>

    <script type="text/javascript">
        var img;
        var mask;
        var byrq;
        var syrq;
        var wxid;
        var url = "";

        $(function () {

            img = $("#progressImgage");
            mask = $("#maskOfProgressImage");
            wxid = '<%=wxid%>';
            var sy_flag = false;
            var by_flag = false;
            var gslb = '<%=gslb%>';


            $('#sykq').bind('click', function () {
                if ($('#dwbs').combobox('getValue') == "") {
                    alert("请选择公司类别！");
                } else {
                    $('#sykq').hide();
                    $('#bykq').show();
                    cx(getYf(2));
                    sy_flag = true;
                    by_flag = false;
                }
            });

            $('#bykq').bind('click', function () {
                if ($('#dwbs').combobox('getValue') == "") {
                    alert("请选择公司类别！");
                } else {
                    $('#bykq').hide();
                    $('#sykq').show();
                    cx(getYf(1));
                    sy_flag = false;
                    by_flag = true;
                }
            });


            $("#dwbs").combobox({
                onChange: function (n, o) {
                    if (sy_flag) {
                        cx(getYf(2));
                    } else {
                        cx(getYf(1));
                    }

                }
            });

            if (gslb == '5') {
                $("#dwbs").combobox('setValue', '41');
            } else if (gslb == '7') {
                $("#dwbs").combobox('setValue', '51');
            } else if (gslb == '6') {
                $("#dwbs").combobox('setValue', '61');
            }
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

        function getYf(pdyf) {
            var myDate = new Date();
            if ((myDate.getMonth() + 1) < 10) {
                byrq = myDate.getFullYear() + "-0" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
                syrq = getPreMonth(byrq);

                //var lastMonthDate = new Date(); //上月日期
                //lastMonthDate.setDate(1);
                //lastMonthDate.setMonth(lastMonthDate.getMonth()-1);
                // syrq = lastMonthDate.getFullYear()+"-0"+(lastMonthDate.getMonth()+1);
            } else {
                byrq = myDate.getFullYear() + "-" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
                syrq = getPreMonth(byrq);
                //byrq = myDate.getFullYear()+"-"+(myDate.getMonth()+1);
                //syrq = myDate.getFullYear()+"-"+myDate.getMonth();
            }

            // alert("本月考勤"+byrq+"--"+syrq);

            if (pdyf == 1) {
                return byrq.substring(0, 7);
            } else {
                return syrq.substring(0, 7);
            }

            return null;
        }


        function cx(rq) {
            //alert(rq);
            var kqy = rq.substring(0, 4) + "年" + rq.substring(5, 7) + "月";
            var gs = $("#dwbs").combobox('getValue');
            if (gs == 41) {
                url = 'servlet/KqAction?method=kqxx_yd_wx&cxsj=' + rq + '&wxid=' + wxid + '&gslb=41';
            } else if (gs == 51) {
                url = 'servlet/KqAction?method=kqxx_mdk_wx&cxsj=' + rq + '&wxid=' + wxid + '&gslb=51';
            } else if (gs == 61) {
                url = 'servlet/KqAction?method=kqxx_wpscj_wx&cxsj=' + rq + '&wxid=' + wxid + '&gslb=61';
            } else {
                alert("服务异常，请联系信息部！");
            }
            //alert(url);
            //if(cx_flag){
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


                        var kqnr = "<div id='kqy_tj'>" + kqy + "车间人员考勤记录</div>";
                        var mqts = "";
                        var gxts = "";
                        var js = 0;
                        var kqnr_wx = "<tr><td class='nr_bt'>日期</td><td class='nr_bt'>星期</td><td class='nr'>上班</td><td class='nr'>下班</td></tr>";
                        for (var key in data) {
                            var mykq = data[key];
                            var xq = "";
                            var rq = "";
                            var swsb = "";
                            var xwxb = "";
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
                                    var s4 = kq['xwxb'];
                                    //alert(s2);
                                    if (s1 != undefined) {
                                        if (s1 == '无') {
                                            swsb = "<td class='wqd'>" + s1 + "</td>";//灰色
                                        } else if (s1.substring(0, 1) == '正') {
                                            swsb = "<td class='zc'>" + s1 + "</td>";
                                        } else if (s1.substring(0, 1) == '迟' || s1.substring(0, 1) == '早') {
                                            swsb = "<td class='cdzt'>" + s1 + "</td>";
                                        } else {
                                            swsb = "<td class='qt'>" + s1 + "</td>";
                                        }
                                    }


                                    if (s4 != undefined) {
                                        if (s4 == '无') {
                                            xwxb = "<td class='wqd'>" + s4 + "</td>";//灰色
                                        } else if (s4.substring(0, 1) == '正') {
                                            xwxb = "<td class='zc'>" + s4 + "</td>";
                                        } else if (s4.substring(0, 1) == '迟' || s4.substring(0, 1) == '早') {
                                            xwxb = "<td class='cdzt'>" + s4 + "</td>";
                                        } else {
                                            xwxb = "<td class='qt'>" + s4 + "</td>";
                                        }
                                    }


                                    kqnr_wx += "<tr>" + rq + xq + swsb + xwxb + "</tr>";
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

                                }

                            }
                            var hjcs = cdcs + cdcs_15 + ztcs;

                            if (xm != "" && xm != null && xm != undefined) {
                                kqnr += "<table id='ydkq'><tr><td colspan='4' >工号：" + gh + "，部门：" + bm + "，姓名：" + xm + "</td></tr>" + kqnr_wx +
                                    "</table><table id='ydkq1'><tr><td >本月应出勤</td><td>" + mqts + "</td></tr><tr><td>实际出勤</td><td>" + ykqts + "</td></tr><tr><td>迟到15分钟以内</td><td>" + cdcs + "</td></tr><tr><td>迟到15分钟以上</td><td>" + cdcs_15 + "</td></tr><tr><td>早退</td><td>" + ztcs + "</td></tr></table>";
                            } else {
                                js--;
                            }
                        }
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

        #ydkq, #ydkq1 {
            width: 350px;
        }

        #nf, #yf {
            width: 70px
        }

        #dg_kqxx {
            border-collapse: collapse;
            padding-right: 20px;
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
            width: 100px;
        }

        .nr_bt {
            width: 60px;
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
                                                                 data-options="panelHeight:'auto', valueField:'id',textField:'text',data:[{'id':'41','text':'亚都实业'},{'id':'61','text':'威浦仕'},{'id':'51','text':'迈迪科'}]"
                                                                 style="width:100px;"/>
            </td>
            <td>
                <a id="sykq" class="easyui-linkbutton" data-options="iconCls:'icon-search'">上月考勤</a>
                <a id="bykq" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   style="display:none">本月考勤</a></td>
        </tr>
    </table>
    <!--  div style="font-size：12px;color:red;width:100%;">友情提醒：选择公司类别后即可查看考勤！</div>-->

    <div id="dg_kqxx" style="width:100%"></div>
</div>
<img id="progressImgage" class="progress" style="display:none" alt="" src="runtime/sy_tabs/ajax-loader.gif"/>
<div id="maskOfProgressImage" class="mask" style="display:none"></div>
<div id="fhdb"><a href="javascript:scroll(0,0)">返回顶部</a></div>

</body>
</html> 