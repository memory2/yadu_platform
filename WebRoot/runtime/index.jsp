<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="gbk" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">


    <title>OA信息管理</title>
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.5/themes/icon.css">
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/tjjs/FusionCharts/FusionCharts.js"></script>
    <script type="text/javascript" src="js/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="js/ydCommon.js"></script>
    <link rel="Shortcut Icon" href="oa.ico" type="image/x-icon"/>
    <link rel="icon" href="oa.ico" type="image/x-icon"/>
    <%
        String msg = (String) session.getAttribute("message");
        if (!"ok".equals(msg)) {
            response.sendRedirect(request.getContextPath() + "/runtime/login/login.jsp");
        }
        String bmmc = (String) session.getAttribute("bmmc") == null ? "" : (String) session.getAttribute("bmmc");
        String xm = (String) session.getAttribute("oaryxm") == null ? "" : (String) session.getAttribute("oaryxm");
        String kqh = (String) session.getAttribute("oarygh") == null ? "" : ((String) session.getAttribute("oarygh"));
        String sfzh = (String) session.getAttribute("sfzh") == null ? "" : (String) session.getAttribute("sfzh");
        String bmdm = (String) session.getAttribute("bmdm") == null ? "" : (String) session.getAttribute("bmdm");
    %>
    <script type="text/javascript">
        var bmdm = '<%=bmdm%>';
        var oaryxm = '<%=xm%>';
        $(function () {
            document.getElementById("cc").height = window.innerHeight - $('.top').height - $('.foot').height;
            var kqSrc = 'runtime/sy_tabs/tab_ydkq.jsp';
            if (bmdm.substring(0, 2) == "51") {
                kqSrc = 'runtime/sy_tabs/tab_mdkkq.jsp';
            } else if (bmdm.substring(0, 2) == "61") {
                kqSrc = 'runtime/sy_tabs/tab_wpskq.jsp';
            }
            if (oaryxm == "系统管理员" || oaryxm == "张正男") {
                var tab = $('#tabs').tabs('getTab', 0);  // 取得第一个tab
                $('#tabs').tabs('update', {
                    tab: tab,
                    options: {
                        title: '亚都行政考勤'
                    }
                });
                kqSrc = 'runtime/sy_tabs/tab_ydkq.jsp';
            }

            document.getElementById("mainFrame").src = kqSrc;


            // alert(kqSrc);
            getMenu();
            $('#tc').bind('click', function () {
                if (confirm("您确定要退出吗?")) {
                    $.ajax({
                        type: "post",
                        url: "servlet/KqAction?method=tc",
                        success: function (msg) {
                            //window.close();
                            window.location.href = "http://kq.yadugroup.com:443/yadu_OA/runtime/login.jsp";
                        }, error: function () {
                            alert("系统报错，请联系管理员！");
                            //错误处理
                        }
                    });

                }

            });

            //右键tab页签的click事件1
            $('#tabs').tabs({
                onContextMenu: function (e, title, index) {
                    e.preventDefault();
                    if (index > 0) {
                        $('#mm').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        }).data("tabTitle", title);
                    }
                }
            });
            //右键tab页签的click事件2
            $("#mm").menu({
                onClick: function (item) {
                    closeTab(this, item.name);
                }
            });

            tick("dqsj");
            getWeather();

            //new ajaxObj().sendRequest("http://10.0.6.39:38080/yyoa/download/test.html","mytest");

            //alert(today);
        });


        function getYy() {
            var nf = $("#nf").combobox('getValue');
            var yf = $("#yf").combobox('getValue');
            return nf + "-" + yf;
        }


        //获取天气
        function getWeather() {


            /* $.getScript("http://php.weather.sina.com.cn/js.php?" + $.param({
                   city :  "长垣", //城市
                   day : 0,
                   password : "DJOYnieT8234jlsK"
               }) , function(json){
                   var tq = status1;
                   var fl=direction1;
                   if(status1!=status2){
                       tq=status1+"转"+status2;
                   }

                   if(direction1=="无持续风向"){
                       fl="微风";
                   }else{
                       fl = direction1+power1+"级";
                   }

                   var jrtq = "长垣今日天气："+tq+" | 气温："+temperature1+"~"+temperature2+"<sup>○</sup>C | "+fl;
                   $("#jrtq").html(jrtq);
               }); */
            $("#jrtq").html('<iframe width="350" scrolling="no" height="18" frameborder="0" allowtransparency="true" src="http://i.tianqi.com/index.php?c=code&id=10&color=%2300B0F0&icon=1&py=changyuan"></iframe>');
        }


        function ajaxObj(url, obj) {
            jQuery.support.cors = true;
            var request = new ActiveXObject("Microsoft.XMLHTTP");
            var count = 0;
            var url = this.url;
            this.sendRequest = function (url, obj) {
                request.open("get", url, true);
                request.onreadystatechange = function () {
                    if (request.readyState == 4) {
                        if (request.status == 200 || request.status == 0) {
                            alert("1");
                            obj.bgColor = "#99FFB3";
                        } else {
                            alert("2");
                            obj.bgColor = "ED012D";
                        }
                    } else {
                        count++;
                        if (count == 1) {
                            obj.bgColor = "EFDF06";
                            alert("3");
                        } else if (count == 8)
                            obj.bgColor = "ED012D";
                        alert("4");
                    }
                };
                try {
                    request.send(null);
                } catch (failed) {
                }
            }
        }


        /* 打开一个标签 */
        function OpenTab(title, url) {
            /**    如果这个标题的标签存在，则选择该标签    否则添加一个标签到标签组    */
            if ($("#tabs").tabs('exists', title)) {
                $("#tabs").tabs('select', title);
            } else {
                $("#tabs").tabs('add', {
                    title: title,
                    content: createTabContent(url),
                    closable: true
                });
            }
        }

        /* 生成标签内容 */
        function createTabContent(url) {
            return '<iframe style="width:100%;height:100%;" scrolling="auto" frameborder="0" src="' + url + '"></iframe>';
        }


        //动态添加左侧菜单核心方法1
        function getMenu() {
            $.ajax({
                type: 'POST',
                dataType: "json",
                url: 'servlet/KqAction?method=cd',
                success: function (data) {
                    addNav(data);
                }
            });


        }

        //动态添加左侧菜单核心方法2
        function addNav(data) {
            $.each(data, function (i, sm) {
                var menulist = '';
                $.each(sm.menus, function (j, o) {
                    if (j == 0) {
                        menulist += '<div class="menu_a" style="margin-top:8px!important;"><img src="js/jquery-easyui-1.4.5/themes/icons/menu2.jpg" style="margin-top:4px;margin-left:10px;vertical-align:bottom;"></img>&nbsp;<label rel="'
                            + o.url + '">' + o.name + '</label></div>';
                    } else {
                        menulist += '<div class="menu_a"><img src="js/jquery-easyui-1.4.5/themes/icons/menu2.jpg" style="margin-top:4px;margin-left:10px;vertical-align:bottom;"></img>&nbsp;<label rel="'
                            + o.url + '">' + o.name + '</label></div>';
                    }

                });
                //alert(menulist);
                $('#wnav').accordion('add', {
                    title: sm.mainMenu,
                    content: menulist,
                    iconCls: sm.icon
                });

            });

            var pp = $('#wnav').accordion('panels');
            var t = pp[0].panel('options').title;
            $('#wnav').accordion('select', t);
            $(".menu_a").click(function () {
                var title = $(this).find("label").text();

                var url = $(this).find("label").attr("rel");
                OpenTab(title, url);
                //addTabs(title,url);
                return false;
            });

            $(".menu_a").hover(function () {
                    $(this).addClass('divOver');
                }, function () {
                    //鼠标离开时移除divOver样式
                    $(this).removeClass('divOver');
                }
            );

        }

        //点击左侧菜单，添加标签页另一种实现方式，这里暂没有用到
        function addTabs(title, url) {
            if (!$("#tabs").tabs('exists', title)) {
                $('#tabs').tabs('add', {
                    title: title,
                    selected: true,
                    href: url,
                    closable: true
                });
            } else $('#tabs').tabs('select', title);
        }


        //右键tab页签，关闭Tabs
        function closeTab(menu, type) {
            var allTabs = $("#tabs").tabs('tabs');
            var allTabtitle = [];
            $.each(allTabs, function (i, n) {
                var opt = $(n).panel('options');
                if (opt.closable)
                    allTabtitle.push(opt.title);
            });
            var curTabTitle = $(menu).data("tabTitle");
            var curTabIndex = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getTab", curTabTitle));
            type = parseInt(type, 10);
            //alert(type);
            switch (type) {
                case 1://关闭当前
                    $("#tabs").tabs("close", curTabIndex);
                    return false;
                    break;
                case 2://全部关闭
                    for (var i = 0; i < allTabtitle.length; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    break;
                case 3://除此之外全部关闭
                    for (var i = 0; i < allTabtitle.length; i++) {
                        if (curTabTitle != allTabtitle[i])
                            $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 4://当前侧面右边
                    for (var i = curTabIndex; i < allTabtitle.length; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 5: //当前侧面左边
                    for (var i = 0; i < curTabIndex - 1; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 6: //刷新
                    var panel = $("#tabs").tabs("getTab", curTabTitle).panel("refresh");
                    break;
            }

        }


    </script>

    <style>
        html, body {
            height: 100%;
            width: 100%;
            overflow: hidden;
            margin: 0;
            padding: 0;
        }

        .menu_a {
            font-size: 12px;
            margin-left: 5px;
            margin-right: 5px;
            text-decoration: none;
            color: #0099FF;
            cursor: hand;
            height: 25px;
        }

        .divOver {
            background: #FFE48D;
        }

        #jrtq {
            width: 500px;
            float: left;
            margin-left: 50px;
            font-weight: 100 !important;
            margin-top: -5px;
        }

        #dqry {
            font-weight: 100 !important;
            text-align: right !important;
            margin-right: 50px;
            color: #0099FF;
        }

        #yf {
            width: 50px;
        }

        #nf {
            width: 70px;
        }
    </style>
</head>
<body>

<div id="cc" class="easyui-layout" style="width:75%;height:100%;margin-left:200px;">
    <div data-options="region:'north'" style="height:130px;background-color:#68B2D7;" title="<div style='float:left;color:#0099FF;font-weight:100;' id='dqsj'></div>
            <div id=jrtq ></div><div  id=dqry> 工号：<%=kqh %> | 姓名：<%=xm %> | 部门：<%=bmmc %> | <font id='tc' style='cursor:pointer;font-weight:700;'>退出</font></div>">
        <div style="height:100px;">
            <div style="color:#00438D;font-size:32px;font-weight:bold;padding-top:25px;padding-left:50px;">
                <div>
                    <img src="images/yd.png" style="width:50px;height:50px;vertical-align:middle;"/>
                    <font style="vertical-align:middle;">企业信息管理系统<font style="font-size:20px">--亚都集团</font></font>
                </div>
            </div>
        </div>
        <!--  div  style="text-align:right;margin-top:5px;color:white;background-color:#387AAE;height:22px;">
          <div style="margin-right:30px;padding-top:5px; ">工号：<%=kqh %> | 姓名：<%=xm %> | 部门：<%=bmmc %> | <font id='tc'style="cursor:pointer;">退出</font></div>
         </div>-->
    </div>
    <div data-options="region:'west',title:'导航菜单'" style="width:150px">
        <div id="wnav" class="easyui-accordion" style="width:100%;height:100%;"
             data-options="fit:true,border:false,animate:true,plain:true">
        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" style="width:100%;height:100%;">
            <!--欢迎标签 START-->
            <div style="padding:5px;text-align:center;" id="zbt" title="行政人员考勤">
                <iframe src="" scrolling="yes" frameborder="0" id="mainFrame" width="100%" height="100%"></iframe>
            </div>

            <!--欢迎标签 END-->

            <div id="mm" class="easyui-menu" style="width:160px;display:none">
                <!--  div id="mm-tabclose" name="6">刷新</div>-->
                <div id="Div1" name="1">关闭</div>
                <div id="mm-tabcloseall" name="2">全部关闭</div>
                <div id="mm-tabcloseother" name="3"> 除此之外全部关闭</div>
                <div class="menu-sep"></div>
                <div id="mm-tabcloseright" name="4">当前页右侧全部关闭</div>
                <div id="mm-tabcloseleft" name="5"> 当前页左侧全部关闭</div>
            </div>
        </div>
    </div>
    <div style="height:30px;background-color:#E8F1FF;text-align:center;padding-top:7px;color:#0099FF;"
         data-options="region:'south'">技术支持：信息部 | 联系电话：83960
    </div>
</div>
</body>
</html> 