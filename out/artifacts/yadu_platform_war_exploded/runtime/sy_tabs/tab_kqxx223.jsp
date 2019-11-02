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
        String zwjb = (String) session.getAttribute("zwjb");

    %>


    <script type="text/javascript">
        var img;
        var mask;
        var byrq;
        var syrq;
        var bmmc = '<%=bmmc%>';
        var txm = '<%=oaryid%>';
        var zwjb = '<%=zwjb%>';
        var bmdm = '<%=bmdm%>';
        $(function () {

            img = $("#progressImgage");
            mask = $("#maskOfProgressImage");
            var cx_flag = false;
            var tj_flag = false;


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
            //cx(byrq);

            $("#yf").combobox({
                onChange: function (n, o) {
                    var pdyf = $("#yf").combobox('getValue');
                    if (pdyf == 1) {
                        cx(byrq);
                    } else {
                        cx(syrq);
                    }
                }
            });

            //点击查询按钮
            $('#btn_search_kqxx').bind('click', function () {
                cx(getYy());
                //tj_flag = true;
                //if(!cx_flag&&tj_flag){
                //cx(getYy());
                //}

                //$('#tj_kqxx').hide();
                //$('#btn_export_kqxx').hide();
                //$('#dg_kqxx').show();
            });

            //点击统计按钮
            $('#btn_tj_kqxx').bind('click', function () {
                cx_flag = true;
                if (!tj_flag && cx_flag) {
                    cx(getYy());
                }
                $('#tj_kqxx').show();
                $('#btn_export_kqxx').show();
                $('#dg_kqxx').hide();
            });

            //点击统计导出按钮
            $('#btn_export_kqxx').bind('click', function () {
                method1('tjkq');

            });

            getDept_yd("bm", bmdm);

            $('#xm').combobox('textbox').bind('focus', function () {
                yzry("bm", "xm");
            });

            $("#bm").combobox({
                onChange: function (n, o) {
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
            if ((bmmc == '信息部' || bmmc == '人力资源部' || txm == "10703" || txm == "10665" || txm == "10269" || txm == "10838") || (zwjb != "10012" && zwjb !== "10013" && zwjb !== "10014" && zwjb !== "10016" && zwjb !== "10018" && zwjb !== "10022")) {
                $('#bm').combobox({disabled: false});
                $('#xm').combobox({disabled: false});
            }


            $("#bm").combotree('setValue', bmdm);
            if (txm != "10554" && txm != "10703") {
                $("#xm").combobox('setValue', txm);
            }
            $("#yf").combobox('setValue', 1);
            cx(getYy());
        });


        function getYy() {
            var nf = $("#nf").combobox('getValue');
            var yf = $("#yf").combobox('getValue');
            //alert(nf+"-"+yf);
            return nf + "-" + yf;
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


            //alert(bm+":"+xm);
            var url = 'servlet/KqAction?method=kqxx_yd_pc&cxsj=' + rq + '&cxxm=' + xm + '&cxbm=' + bm;
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
                        var myfj = 3;
                        var kqtj = "<table id='tjkq'><tr><td style='font-size:14px;font-weight:bold' colspan=18>亚都公司" + kqy + "考勤汇总表</td></tr><tr><td  class='xh' rowspan=2>序号</td><td class='nr_bt' rowspan=2>部门</td><td class='nr_bt' rowspan=2>工号</td><td class='nr_bt' rowspan=2>姓名</td><td class='nr_bt' colspan=2>出勤天数</td><td class='nr_bt' colspan=6>休假天数</td><td class='nr_bt' rowspan=2>加班小时数</td><td class='nr_bt' colspan=5>迟到、早退、补签次数</td></tr><tr><td class='nr_bt'>应出勤</td><td class='nr_bt'>实际出勤</td><td class='nr_bt'>公司放假</td><td class='nr_bt'>可调休天数</td><td class='nr_bt'>事假</td><td class='nr_bt'>婚假</td><td class='nr_bt'>丧假</td><td class='nr_bt'>工伤</td><td class='nr_bt'>迟到15分钟以内</td><td class='nr_bt'>迟到15分钟以上</td><td class='nr_bt'>早退</td><td class='nr_bt'>补签</td><td class='nr_bt'>合计</td></tr>";
                        var kqnr = "<div id='kqy_tj'>" + kqy + "考勤记录</div>";
                        var kqnr1 = "";
                        var kqnr2 = "<tr>";
                        var kqnr_ry = "";
                        var kqnr_ry_kl = 0;//考勤人员跨列
                        var kqnr_xq = "<tr><td>星期</td>";
                        var kqnr_rq = "<tr><td>日期</td>";
                        var kqnr_swsb = "<tr><td>上午上班</td>";
                        var kqnr_swxb = "<tr><td>上午下班</td>";
                        var kqnr_xwsb = "<tr><td>下午上班</td>";
                        var kqnr_xwxb = "<tr><td>下午下班</td>";
                        var kqnr_znr = "";

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
                        var ry = "";
                        var xq = "";
                        var rq = "";
                        var swsb = "";
                        var swxb = "";
                        var xwsb = "";
                        var xwxb = "";
                        var jb = "<td>加班时长</td>";

                        for (var k1 in data) {//begin 获取每日全部人员的map列表,mrkq循环后结果{"2017-*08-01",[{甲},{乙},{丙}]}
                            kqnr_ry_kl++;
                            var mrkq = data[k1];
                            var kqnr4 = "";
                            //alert(JSON.stringify(mrkq));
                            for (var k2 in mrkq) {//begin 获取每日全部人员的List列表,kqs循环后结果[{甲},{乙},{丙}]
                                var kqs = mrkq[k2];
                                //alert(JSON.stringify(kqs));

                                for (var i = 0; i < kqs.length; i++) {//begin 获取每日每人的考勤列表,kq循环后结果{甲}
                                    var kq = {};
                                    kq = kqs[i];


                                    var s1 = kq['swsb'];
                                    var s2 = kq['swxb'];
                                    var s3 = kq['xwsb'];
                                    var s4 = kq['xwxb'];
                                    //alert(s2);
                                    if (s1 != undefined) {
                                        if (s1 == '无') {
                                            swsb += "<td class='wqd'>" + s1 + "</td>";//灰色
                                        } else if (s1.substring(0, 1) == '正' || s1 == '因公补签') {
                                            swsb += "<td class='zc'>" + s1 + "</td>";
                                        } else if (s1 == '事假' || s1 == '婚假' || s1 == '丧假' || s1 == '工伤') {
                                            swsb += "<td class='qj'>" + s1 + "</td>";
                                        } else if (s1 == '出差') {
                                            swsb += "<td class='cc'>" + s1 + "</td>";
                                        } else if (s1 == '免签') {
                                            swsb += "<td class='mq'>" + s1 + "</td>";
                                        } else if (s1.substring(0, 1) == '迟' || s1.substring(0, 1) == '早') {
                                            swsb += "<td class='cdzt'>" + s1 + "</td>";
                                        } else {
                                            swsb += "<td class='qt'>" + s1 + "</td>";
                                        }
                                    }

                                    if (s2 != undefined) {
                                        if (s2 == '无') {
                                            swxb += "<td class='wqd'>" + s2 + "</td>";//灰色
                                        } else if (s2.substring(0, 1) == '正' || s2 == '因公补签') {
                                            swxb += "<td class='zc'>" + s2 + "</td>";
                                        } else if (s2 == '事假' || s2 == '婚假' || s2 == '丧假' || s2 == '工伤') {
                                            swxb += "<td class='qj'>" + s2 + "</td>";
                                        } else if (s2 == '出差') {
                                            swxb += "<td class='cc'>" + s2 + "</td>";
                                        } else if (s2 == '免签') {
                                            swxb += "<td class='mq'>" + s2 + "</td>";
                                        } else if (s2.substring(0, 1) == '迟' || s2.substring(0, 1) == '早') {
                                            swxb += "<td class='cdzt'>" + s2 + "</td>";
                                        } else {
                                            swxb += "<td class='qt'>" + s2 + "</td>";
                                        }
                                    }


                                    if (s3 != undefined) {
                                        if (s3 == '无') {
                                            xwsb += "<td class='wqd'>" + s3 + "</td>";//灰色
                                        } else if (s3.substring(0, 1) == '正' || s3 == '因公补签') {
                                            xwsb += "<td class='zc'>" + s3 + "</td>";
                                        } else if (s3 == '事假' || s3 == '婚假' || s3 == '丧假' || s3 == '工伤') {
                                            xwsb += "<td class='qj'>" + s3 + "</td>";
                                        } else if (s3 == '出差') {
                                            xwsb += "<td class='cc'>" + s3 + "</td>";
                                        } else if (s3 == '免签') {
                                            xwsb += "<td class='mq'>" + s3 + "</td>";
                                        } else if (s3.substring(0, 1) == '迟' || s3.substring(0, 1) == '早') {
                                            xwsb += "<td class='cdzt'>" + s3 + "</td>";
                                        } else {
                                            xwsb += "<td class='qt'>" + s3 + "</td>";
                                        }
                                    }


                                    if (s4 != undefined) {
                                        if (s4 == '无') {
                                            xwxb += "<td class='wqd'>" + s4 + "</td>";//灰色
                                        } else if (s4.substring(0, 1) == '正' || s4 == '因公补签') {
                                            xwxb += "<td class='zc' >" + s4 + "</td>";
                                        } else if (s4 == '事假' || s4 == '婚假' || s4 == '丧假' || s4 == '工伤') {
                                            xwxb += "<td class='qj'>" + s4 + "</td>";
                                        } else if (s4 == '出差') {
                                            xwxb += "<td class='cc'>" + s4 + "</td>";
                                        } else if (s4 == '免签') {
                                            xwxb += "<td class='mq'>" + s4 + "</td>";
                                        } else if (s4.substring(0, 1) == '迟' || s4.substring(0, 1) == '早') {
                                            xwxb += "<td class='cdzt'>" + s4 + "</td>";
                                        } else {
                                            xwxb += "<td class='qt'>" + s4 + "</td>";
                                        }
                                    }

                                    if (kq['cfjbcs'] == 0) {
                                        jb += "<td>" + kq['jbzgs'] + "</td>";
                                    } else {
                                        jb += "<td>" + kq['jbzgs'] + "(重" + kq['cfjbcs'] + ")</td>";
                                    }
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
                                    xq += "<td class='nr'>" + kq['xq'] + "</td>";
                                    rq += "<td>" + kq['rq'] + "</td>";

                                    ry += "<td>部门:" + bm + ",姓名：" + xm + "</td>";


                                }//end 获取每日每人的考勤列表,kq循环后结果{甲}

                                kqnr_ry = ry;
                                kqnr_xq = xq;

                                kqnr_rq = kqnr_r;
                                kqnr_swsb = swsb;
                                kqnr_swxb = swxb;
                                kqnr_xwsb = xwsb;
                                kqnr_xwxb = xwxb;
                                kqnr += "<table id='ydkq'><tr>" + ry + "</tr>" + xq + "</tr>" + rq + "</tr>" + swsb + "</tr>" + swxb + "</tr>" + xwsb + "</tr>" + xwxb + "</tr></table></br>";
                            }//end 获取每日全部人员的List列表,kqs循环后结果[{甲},{乙},{丙}]


                        } //end 获取每日全部人员的map列表,mrkq循环后结果{"2017-*08-01",[{甲},{乙},{丙}]}
                        kqnr_znr += kqnr;
                        $("#dg_kqxx").html(kqnr_znr);
                        //$("#dg_kqxx").html(kqnr+kqnr1+"</tr>"+kqnr2+"</tr><tr>"+kqnr3+"</table>");

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
            width: 39000px !important;
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
            width: 79px;
        }

        .xh {
            width: 40px;
        }

        #kqy_tj {
            font-size: 14px;
            font-weight: bold;
            padding: 5px;
            width: 100000px
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
                                                    data-options="valueField:'id',panelHeight:'260',textField:'text'"
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