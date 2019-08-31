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


    <title>OA��Ϣ����</title>
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
            if (oaryxm == "ϵͳ����Ա" || oaryxm == "������") {
                var tab = $('#tabs').tabs('getTab', 0);  // ȡ�õ�һ��tab
                $('#tabs').tabs('update', {
                    tab: tab,
                    options: {
                        title: '�Ƕ���������'
                    }
                });
                kqSrc = 'runtime/sy_tabs/tab_ydkq.jsp';
            }

            document.getElementById("mainFrame").src = kqSrc;


            // alert(kqSrc);
            getMenu();
            $('#tc').bind('click', function () {
                if (confirm("��ȷ��Ҫ�˳���?")) {
                    $.ajax({
                        type: "post",
                        url: "servlet/KqAction?method=tc",
                        success: function (msg) {
                            //window.close();
                            window.location.href = "http://kq.yadugroup.com:443/yadu_OA/runtime/login.jsp";
                        }, error: function () {
                            alert("ϵͳ��������ϵ����Ա��");
                            //������
                        }
                    });

                }

            });

            //�Ҽ�tabҳǩ��click�¼�1
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
            //�Ҽ�tabҳǩ��click�¼�2
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


        //��ȡ����
        function getWeather() {


            /* $.getScript("http://php.weather.sina.com.cn/js.php?" + $.param({
                   city :  "��ԫ", //����
                   day : 0,
                   password : "DJOYnieT8234jlsK"
               }) , function(json){
                   var tq = status1;
                   var fl=direction1;
                   if(status1!=status2){
                       tq=status1+"ת"+status2;
                   }

                   if(direction1=="�޳�������"){
                       fl="΢��";
                   }else{
                       fl = direction1+power1+"��";
                   }

                   var jrtq = "��ԫ����������"+tq+" | ���£�"+temperature1+"~"+temperature2+"<sup>��</sup>C | "+fl;
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


        /* ��һ����ǩ */
        function OpenTab(title, url) {
            /**    ����������ı�ǩ���ڣ���ѡ��ñ�ǩ    �������һ����ǩ����ǩ��    */
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

        /* ���ɱ�ǩ���� */
        function createTabContent(url) {
            return '<iframe style="width:100%;height:100%;" scrolling="auto" frameborder="0" src="' + url + '"></iframe>';
        }


        //��̬������˵����ķ���1
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

        //��̬������˵����ķ���2
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
                    //����뿪ʱ�Ƴ�divOver��ʽ
                    $(this).removeClass('divOver');
                }
            );

        }

        //������˵�����ӱ�ǩҳ��һ��ʵ�ַ�ʽ��������û���õ�
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


        //�Ҽ�tabҳǩ���ر�Tabs
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
                case 1://�رյ�ǰ
                    $("#tabs").tabs("close", curTabIndex);
                    return false;
                    break;
                case 2://ȫ���ر�
                    for (var i = 0; i < allTabtitle.length; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    break;
                case 3://����֮��ȫ���ر�
                    for (var i = 0; i < allTabtitle.length; i++) {
                        if (curTabTitle != allTabtitle[i])
                            $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 4://��ǰ�����ұ�
                    for (var i = curTabIndex; i < allTabtitle.length; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 5: //��ǰ�������
                    for (var i = 0; i < curTabIndex - 1; i++) {
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                    $('#tabs').tabs('select', curTabTitle);
                    break;
                case 6: //ˢ��
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
            <div id=jrtq ></div><div  id=dqry> ���ţ�<%=kqh %> | ������<%=xm %> | ���ţ�<%=bmmc %> | <font id='tc' style='cursor:pointer;font-weight:700;'>�˳�</font></div>">
        <div style="height:100px;">
            <div style="color:#00438D;font-size:32px;font-weight:bold;padding-top:25px;padding-left:50px;">
                <div>
                    <img src="images/yd.png" style="width:50px;height:50px;vertical-align:middle;"/>
                    <font style="vertical-align:middle;">��ҵ��Ϣ����ϵͳ<font style="font-size:20px">--�Ƕ�����</font></font>
                </div>
            </div>
        </div>
        <!--  div  style="text-align:right;margin-top:5px;color:white;background-color:#387AAE;height:22px;">
          <div style="margin-right:30px;padding-top:5px; ">���ţ�<%=kqh %> | ������<%=xm %> | ���ţ�<%=bmmc %> | <font id='tc'style="cursor:pointer;">�˳�</font></div>
         </div>-->
    </div>
    <div data-options="region:'west',title:'�����˵�'" style="width:150px">
        <div id="wnav" class="easyui-accordion" style="width:100%;height:100%;"
             data-options="fit:true,border:false,animate:true,plain:true">
        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" style="width:100%;height:100%;">
            <!--��ӭ��ǩ START-->
            <div style="padding:5px;text-align:center;" id="zbt" title="������Ա����">
                <iframe src="" scrolling="yes" frameborder="0" id="mainFrame" width="100%" height="100%"></iframe>
            </div>

            <!--��ӭ��ǩ END-->

            <div id="mm" class="easyui-menu" style="width:160px;display:none">
                <!--  div id="mm-tabclose" name="6">ˢ��</div>-->
                <div id="Div1" name="1">�ر�</div>
                <div id="mm-tabcloseall" name="2">ȫ���ر�</div>
                <div id="mm-tabcloseother" name="3"> ����֮��ȫ���ر�</div>
                <div class="menu-sep"></div>
                <div id="mm-tabcloseright" name="4">��ǰҳ�Ҳ�ȫ���ر�</div>
                <div id="mm-tabcloseleft" name="5"> ��ǰҳ���ȫ���ر�</div>
            </div>
        </div>
    </div>
    <div style="height:30px;background-color:#E8F1FF;text-align:center;padding-top:7px;color:#0099FF;"
         data-options="region:'south'">����֧�֣���Ϣ�� | ��ϵ�绰��83960
    </div>
</div>
</body>
</html> 