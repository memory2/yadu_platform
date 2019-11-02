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
    <link href="css/time_css/history.css" rel="stylesheet"/>

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
<!-- 代码 开始 -->
<div class="head-warp">
    <div class="head">
        <div class="nav-box">
            <ul>
                <li class="cur" style="text-align:center; font-size:36px; font-family:'微软雅黑', '宋体';">亚都工作日志</li>
            </ul>
        </div>
    </div>
</div>
<div class="main">
    <div class="history">
        <div class="history-date">
            <ul>
                <h2 class="first"><a href="#nogo">2016年</a></h2>


                <li class="green">
                    <h3>10.08<span>2016</span></h3>
                    <dl>
                        <dt>发布全新的极速浏览器6.0版本
                            <span>升级极速内核到21.0；全新默认界面；新增小窗口播放功能</span>
                        </dt>
                    </dl>
                </li>

                <li>
                    <h3>07.19<span>2016</span></h3>
                    <dl>
                        <dt>升级极速内核到20.0
                            <span>HTML5支持度全球最好，达到469分，测试页面： </span>
                        </dt>
                    </dl>
                </li>

                <li>
                    <h3>07.02<span>2016</span></h3>
                    <dl>
                        <dt>升级极速内核到19.0
                            <span>支持网络摄像头，浏览器可直接访问摄像头</span>
                        </dt>
                    </dl>
                </li>

                <li class="green">
                    <h3>06.27<span>2016</span></h3>
                    <dl>
                        <dt>发布国内首个HTML5实验室
                            <span>大力推广HTML5</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>06.15<span>2016</span></h3>
                    <dl>
                        <dt>新增了下载文件前扫描下载链接安全性的功能</dt>
                    </dl>
                </li>
                <li>
                    <h3>06.05<span>2016</span></h3>
                    <dl>
                        <dt>W3C联盟首席执行官访华，首站访问360公司
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>05.12<span>2016</span></h3>
                    <dl>
                        <dt>360受邀出席W3C联盟成员见面会议</dt>
                    </dl>
                </li>
                <li>
                    <h3>05.11<span>2016</span></h3>
                    <dl>
                        <dt>升级极速内核到18.0
                            <span>新增多用户使用浏览器的功能</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>05.03<span>2016</span></h3>
                    <dl>
                        <dt>360极速浏览器用户数突破5000万，活跃用户超2000万
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>03.08<span>2016</span></h3>
                    <dl>
                        <dt>升级极速内核到17.0，提升浏览器速度、增强安全性
                            <span>新增HTTP管线化技术，大幅提升网页加载速度</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>01.29<span>2016</span></h3>
                    <dl>
                        <dt>国内率先加入W3C联盟HTML工作组，参与HTML5标准制定</span>
                        </dt>
                    </dl>
                </li>
            </ul>
        </div>
        <div class="history-date">
            <ul>
                <h2 class="date02"><a href="#nogo">2015年</a></h2>
                <li>
                    <h3>12.12<span>2015</span></h3>
                    <dl>
                        <dt>升级极速内核到16.0，提升浏览器速度、增强安全性
                            <span>新增对360网购保镖支持，保护网上交易安全</span>
                        </dt>
                    </dl>
                </li>
                <li class="green">
                    <h3>11.24<span>2015</span></h3>
                    <dl>
                        <dt>发布国内首个双核浏览器实验室
                            <span>轻松测试浏览器性能</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>11.01<span>2015</span></h3>
                    <dl>
                        <dt>升级极速内核到15.0
                            <span>提升浏览器速度、增强安全性</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>10.27<span>2015</span></h3>
                    <dl>
                        <dt>作为国内唯一受邀参展的浏览器厂商，参展2015谷歌开发者日大会
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>09.22<span>2015</span></h3>
                    <dl>
                        <dt>升级极速内核到14.0
                            <span>加入Canvas 2D的GPU加速等特性</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>09.21<span>2015</span></h3>
                    <dl>
                        <dt>360极速浏览器用户量超千万，宣布与Chromium社区版本同步
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>08.30<span>2015</span></h3>
                    <dl>
                        <dt>升级极速内核到13.0
                            <span>新增更丰富的皮肤自定义支持</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>06.22<span>2015</span></h3>
                    <dl>
                        <dt>新增对crx格式的关联
                            <span>双击crx文件即可安装扩展、皮肤</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>05.19<span>2015</span></h3>
                    <dl>
                        <dt>升级极速内核到10.0
                            <span>极速浏览器与安全浏览器网络收藏夹互通</span>
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>04.07<span>2015</span></h3>
                    <dl>
                        <dt>推出应用开放平台，与开发者共享用户资源
                        </dt>
                    </dl>
                </li>
                <li>
                    <h3>03.27<span>2015</span></h3>
                    <dl>
                        <dt>新增360云安全网址拦截
                            <span>新增IE9高速模式，支持GPU硬件加速</span>
                        </dt>
                    </dl>
                </li>
            </ul>
        </div>
        <div class="history-date">
            <ul>
                <h2 class="date02"><a href="#nogo">2014年</a></h2>
                <li>
                    <h3>12.13<span>2014</span></h3>
                    <dl>
                        <dt>升级极速内核到7.0<span>提升浏览器速度、增强安全性</span></dt>
                    </dl>
                </li>
                <li>
                    <h3>10.20<span>2014</span></h3>
                    <dl>
                        <dt>升级极速内核到6.0<span>新增360帐户，同步网络收藏夹</span></dt>
                    </dl>
                </li>
                <li>
                    <h3>09.15<span>2014</span></h3>
                    <dl>
                        <dt>首款双核安全浏览器 - 360极速浏览器发布
                            <span>首个包含沙箱、系统级防注入、完整多进程隔离架构等安全机制的双核浏览器<br><br></span>
                        </dt>
                        <br><br><br><br>
                    </dl>
                </li>
            </ul>
        </div>
    </div>
</div>

<script src="js/time_js/jquery.js"></script>
<script src="js/time_js/main.js"></script>

<div style="text-align:center;margin:50px 0; font:normal 14px/24px 'MicroSoft YaHei';">
    <p align="center">适用浏览器：IE8、360、FireFox、Chrome、Safari、Opera、傲游、搜狗、世界之窗. </p>
</div>
</body>
</html>
