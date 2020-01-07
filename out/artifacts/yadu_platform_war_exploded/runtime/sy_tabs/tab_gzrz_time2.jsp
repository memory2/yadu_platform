<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
    <title>亚都工作日志</title>
    <link href="css/time_css/chinaz.css" rel="stylesheet">

    <script src="js/time_js/jquery-1.9.1.min.js"></script>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->

</head>

<body>
<div class="wrapper">

    <div class="history">
        <div class="start-history">
            <p class="cc_history">工作日志</p>
            <p class="next_history">DAILY LOG</p>
            <div class="history_left">
                <p class="history_L year2006">
                    <span class="history_2006_span">2006</span>
                    <b class="history_2006_b">
                        <span class="history_l_month">10<br/>月</span>
                        <span class="history_l_text">开展基于互联网的视频服务业务<br/>CC视频联盟正式公测</span>
                    </b>
                </p>
                <p class="history_L yearalmost">
                    <span class="history_2006_span">2008</span>
                    <b class="history_2006_b">
                        <span class="history_l_month">10<br/>月</span>
                        <span class="history_l_text">收购康盛创想<br/>旗下视频建站系supev</span>
                    </b>
                </p>
                <p class="history_L year2009">
                    <span class="history_2006_span">2009</span>
                    <b class="history_2006_b">
                        <span class="history_l_month">04<br/>月</span>
                        <span class="history_l_text">推出CMedia视频广告平台，<br/>对旗下视频媒体进行整合营销</span>
                    </b>
                </p>
                <p class="history_L yearalmost">
                    <span class="history_2006_span blue">2010</span>
                    <b class="history_2006_b blue">
                        <span class="history_l_month">01<br/>月</span>
                        <span class="history_l_text">CC视频获得广电总局颁发的<br/>音视频许可证</span>
                    </b>
                </p>
                <p class="history_L yearalmost">
                    <span class="history_2006_span blue">2011</span>
                    <b class="history_2006_b blue">
                        <span class="history_l_month">08<br/>月</span>
                        <span class="history_l_text smalltext">CC视频获得由迪士尼旗下思伟投资、IDG、江苏高科技投资集团的<br/>B轮融资2000万美元</span>
                    </b>
                </p>
                <p class="history_L year2011">
                    <span class="history_2006_span blue">2011</span>
                    <b class="history_2006_b blue">
                        <span class="history_l_month">09<br/>月</span>
                        <span class="history_l_text">CC视频荣获“2011年<br/>中国云计算最佳应用实践奖”</span>
                    </b>
                </p>
                <p class="history_L year2011">
                    <span class="history_2006_span blue">2012</span>
                    <b class="history_2006_b blue">
                        <span class="history_l_month">03<br/>月</span>
                        <span class="history_l_text">CC视频成功举办2012中国<br/>远程教育技术创新与应用论坛</span>
                    </b>
                </p>
                <p class="history_L year2011">
                    <span class="history_2006_span yellow">2012</span>
                    <b class="history_2006_b yellow">
                        <span class="history_l_month">09<br/>月</span>
                        <span class="history_l_text">CC视频发布视频云<br/>开放战略及API2.0</span>
                    </b>
                </p>
                <p class="history_L year2013">
                    <span class="history_2006_span yellow">2013</span>
                    <b class="history_2006_b yellow">
                        <span class="history_l_month">04<br/>月</span>
                        <span class="history_l_text smalltxt">CC视频成功举办2013（第二届）中国<br/>远程教育技术创新与应用论坛</span>
                    </b>
                </p>
                <p class="history_L yearalmost">
                    <span class="history_2006_span yellow">2015</span>
                    <b class="history_2006_b yellow">
                        <span class="history_l_month">01<br/>月</span>
                        <span class="history_l_text full">视频直播CC Live发布</span>
                    </b>
                </p>
            </div>
            <div class="history-img">
                <img class="history_img" src="images/time_images/history.png" alt="">
            </div>
            <div class="history_right">
                <p class="history_R history_r_2005">
                    <span class="history_2005_span">2005</span>
                    <b class="history_2005_b">
                        <span class="history_r_month">04<br/>月</span>
                        <span class="history_r_text">CC视频成立<br/>并入住北京大学留学生创业园</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span">2007</span>
                    <b class="history_2005_b">
                        <span class="history_r_month">12<br/>月</span>
                        <span class="history_r_text">获得国际著名投资商<br/>IDG VC投资 </span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span">2009</span>
                    <b class="history_2005_b">
                        <span class="history_r_month">01<br/>月</span>
                        <span class="history_r_text">整合Pocle和SupeV后<br/>推出CCVMS视频建站系统</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span">2009</span>
                    <b class="history_2005_b">
                        <span class="history_r_month">12<br/>月</span>
                        <span class="history_r_text">CC视频成为Google在大陆地<br/>区第一家视频广告合作伙伴</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span blue">2010</span>
                    <b class="history_2005_b blue_R">
                        <span class="history_r_month">10<br/>月</span>
                        <span class="history_r_text">CC视频推出基于视频云计算的<br/>第三方服务平台</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span blue">2011</span>
                    <b class="history_2005_b blue_R">
                        <span class="history_r_month">08<br/>月</span>
                        <span class="history_r_text">CC视频参展2011年（第十届）<br/>中国互联网大会</span>
                    </b>
                </p>
                <p class="history_R year211">
                    <span class="history_2005_span blue">2011</span>
                    <b class="history_2005_b blue_R">
                        <span class="history_r_month">11<br/>月</span>
                        <span class="history_r_text">CC视频荣获<br/>“最佳云服务模式奖”</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span yellow">2012</span>
                    <b class="history_2005_b yellow_R">
                        <span class="history_r_month">04<br/>月</span>
                        <span class="history_r_text">CC视频荣获<br/>“最佳视频服务提供商”</span>
                    </b>
                </p>
                <p class="history_R year211">
                    <span class="history_2005_span yellow">2012</span>
                    <b class="history_2005_b yellow_R">
                        <span class="history_r_month">10<br/>月</span>
                        <span class="history_r_text">CC视频荣获“清科集团中国<br/>最具投资价值50强”荣誉</span>
                    </b>
                </p>
                <p class="history_R yearalmostr">
                    <span class="history_2005_span yellow">2014</span>
                    <b class="history_2005_b yellow_R">
                        <span class="history_r_month">10<br/>月</span>
                        <span class="history_r_text">CC视频荣获<br/>“最佳教育技术提供商”荣誉</span>
                    </b>
                </p>
            </div>
            <div class="clear"></div>
        </div>
        <div class="clear"></div>
    </div>
</div>

<script src="js/time_js/jquery.slidy.js"></script>
<script>
    $(window).scroll(function () {
        var msg = $(".history-img");
        var item = $(".history_L");
        var items = $(".history_R");
        var windowHeight = $(window).height();
        var Scroll = $(document).scrollTop();
        if ((msg.offset().top - Scroll - windowHeight) <= 0) {
            msg.fadeIn(1500);
        }
        for (var i = 0; i < item.length; i++) {
            if (($(item[i]).offset().top - Scroll - windowHeight) <= -100) {
                $(item[i]).animate({marginRight: '0px'}, '50', 'swing');
            }
        }
        for (var i = 0; i < items.length; i++) {
            if (($(items[i]).offset().top - Scroll - windowHeight) <= -100) {
                $(items[i]).animate({marginLeft: '0px'}, '50', 'swing');
            }
        }
    });

</script>

<div style="text-align:center;margin:50px 0; font:normal 14px/24px 'MicroSoft YaHei';">
    <p>适用浏览器：360、FireFox、Chrome、Safari、Opera、傲游、搜狗、世界之窗. 不支持IE8及以下浏览器。</p>
</div>
</body>
</html>
