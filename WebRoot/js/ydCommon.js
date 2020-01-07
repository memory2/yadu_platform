function jsEncode(word) {
    function encodeMethod(str) {
        if (str == "") {
            return str;
        }
        var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);
        var out, i, len;
        var c1, c2, c3;
        len = str.length;
        i = 0;
        out = "";
        while (i < len) {
            c1 = str.charCodeAt(i++) & 255;
            if (i == len) {
                out += base64EncodeChars.charAt(c1 >> 2);
                out += base64EncodeChars.charAt((c1 & 3) << 4);
                out += "==";
                break;
            }
            c2 = str.charCodeAt(i++);
            if (i == len) {
                out += base64EncodeChars.charAt(c1 >> 2);
                out += base64EncodeChars.charAt(((c1 & 3) << 4) | ((c2 & 240) >> 4));
                out += base64EncodeChars.charAt((c2 & 15) << 2);
                out += "=";
                break;
            }
            c3 = str.charCodeAt(i++);
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt(((c1 & 3) << 4) | ((c2 & 240) >> 4));
            out += base64EncodeChars.charAt(((c2 & 15) << 2) | ((c3 & 192) >> 6));
            out += base64EncodeChars.charAt(c3 & 63);
        }
        return out;
    }

    var word1 = encodeMethod(word);
    return "MD5" + word1;
}


//datagrid导出到excel
function ExporterExcel(gridID, gridBT) {
    //获取Datagride的列
    var rows = $("#" + gridID).datagrid('getRows');
    var columns = $("#" + gridID).datagrid("options").columns[0];
    var oXL = new ActiveXObject("Excel.Application"); //创建AX对象excel 
    var oWB = oXL.Workbooks.Add(); //获取workbook对象 
    var oSheet = oWB.ActiveSheet; //激活当前sheet
    //设置工作薄名称
    oSheet.name = gridBT;
    //设置表头
    for (var i = 0; i < columns.length; i++) {
        oSheet.Cells(1, i + 1).value = columns[i].title;
    }
    //设置内容部分
    for (var i = 0; i < rows.length; i++) {
        //动态获取每一行每一列的数据值
        for (var j = 0; j < columns.length; j++) {
            oSheet.Cells(i + 2, j + 1).value = rows[i][columns[j].field];
        }
    }
    oXL.Visible = true; //设置excel可见属性
    oXL = null;//释放对象
}

//datagrid通用
function getDataGird(url, gridID, gridBT) {
    var editRow = undefined;
    $('#' + gridID).datagrid({
        url: url,
        pagination: true,
        toolbar: [
            {
                text: '导出',
                iconCls: 'icon-dload',
                handler: function () {
                    ExporterExcel(gridID, gridBT);
                }
            }
        ]
    });


}

//datagrid通用
function getDataGird1(url, gridID, gridBT, updUrl, delUrl, width, height) {
    var editRow = undefined;
    $('#' + gridID).datagrid({
        url: url,
        pagination: true,
        toolbar: [
            {
                text: '导出',
                iconCls: 'icon-dload',
                handler: function () {
                    ExporterExcel(gridID, gridBT);
                }
            }, {
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    edit(gridID, gridBT, updUrl, width, height, 'add');
                }
            }, {
                text: '修改',
                iconCls: 'icon-edit',
                handler: function () {
                    edit(gridID, gridBT, updUrl, width, height, 'upd');
                }
            }, {
                text: '删除',
                iconCls: 'icon-cancel',
                handler: function () {
                    del(gridID, delUrl);
                }
            }
        ]
    });
    var p = $('#' + gridID).datagrid('getPager');
    $(p).pagination({
        pageSize: 10,//每页显示的记录条数，默认为10   
        pageList: [5, 10, 15, 1000],//可以设置每页记录条数的列表   
        beforePageText: '第',//页数文本框前显示的汉字   
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
    });


}

function edit(gridID, gridBT, updUrl, width, height, czlb) {
    var row;
    var href;
    var info;
    if ('add' != czlb) {
        row = $('#' + gridID).datagrid('getSelected');
        if (czlb != 'add' && !row) {
            $.messager.alert('提示', '请先选中行', 'info');
            return;
        }
        href = updUrl + '?zj=' + row.id + '&czlb=' + czlb;
    } else {
        href = updUrl + '?czlb=' + czlb;
    }
    $('#myWindow').window(
        {
            title: gridBT,
            width: width === undefined ? 600 : width,
            height: height === undefined ? 400 : height,
            content: '<iframe scrolling="yes" frameborder="0"  src="'
                + href
                + '" style="width:98%;height:98%;"></iframe>',
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: false,
            loadingMessage: '正在加载数据，请稍等片刻......'
        });

}


function del(gridID, delUrl) {
    var row = $('#' + gridID).datagrid('getSelected');
    if (!row) {
        $.messager.alert('提示', '请先选中行', 'info');
        return;
    }
    $.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
        if (r) {
            var href = delUrl + '&zj=' + row.id;
            $.ajax({
                type: 'POST',
                url: href,
                success: function (data) {
                    //alert(data);
                    if ('ok' == data) {
                        alert("删除成功！");
                    } else {
                        alert("删除失败,请联系管理员！");
                    }
                    cx();
                }
            });
        }
    });
}

function setDefaultDate(ksrq, jsrq) {
    var myDate = new Date();
    var y = myDate.getFullYear();
    var m = myDate.getMonth() + 1;
    var d = new Date(y, m, 0).getDate();
    $("#" + ksrq).datebox('setValue', y + "-" + m + "-01");
    $("#" + jsrq).datebox('setValue', y + "-" + m + "-" + d);
}

function setDefaultTime(ksrq, jsrq) {
    var myDate = new Date();
    var y = myDate.getFullYear();
    var m = myDate.getMonth() + 1;
    var d = myDate.getDate();
    var h = myDate.getHours();
    var n = myDate.getMinutes();
    var s = myDate.getSeconds();
    //alert(y+"-"+m+"-"+d+" "+h+":"+n+":"+s);
    $("#" + ksrq).datetimebox('setValue', y + "-" + m + "-" + d + " 08:00:00");
    $("#" + jsrq).datetimebox('setValue', y + "-" + m + "-" + d + " " + h + ":" + n + ":" + s);
}

function setDefaultTime1(rqid) {
    var myDate = new Date();
    var y = myDate.getFullYear();
    var m = myDate.getMonth() + 1;
    var d = myDate.getDate();
    var h = myDate.getHours();
    var n = myDate.getMinutes();
    var s = myDate.getSeconds();
    //alert(y+"-"+m+"-"+d+" "+h+":"+n+":"+s);
    $("#" + rqid).datetimebox('setValue', y + "-" + m + "-" + d + " " + h + ":" + n + ":" + s);
}

function getYear() {
    var myDate = new Date();
    var y = myDate.getFullYear();
    return y;
}

function getMonth() {
    var myDate = new Date();
    var m = myDate.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    return m;
}

function getTjt(lb, bt, tb, sz) {
    var content0 = '<chart caption="';
    var content1 = ' baseFontSize="14" showLimits="1" yAxisMaxValue="';
    var content = ' yAxisMinValue="1000" name="333" showNames="1" showBorder="0" outCnvBaseFont="华文新魏"  outCnvBaseFontSize="20" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="1" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="0" numDivLines="3"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="元" plotSpacePercent="50">';

    var sm = {};
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: 'servlet/OaAction?method=' + lb,
        success: function (data) {
            //var d = eval("("+data+")");//效果等同于加dataType
            //alert(JSON.stringify(data[0]));
            var m1 = JSON.stringify(data[0].m1) != undefined ? JSON.stringify(data[0].m1) : '"0"';
            var m2 = JSON.stringify(data[0].m2) != undefined ? JSON.stringify(data[0].m2) : '"0"';
            var m3 = JSON.stringify(data[0].m3) != undefined ? JSON.stringify(data[0].m3) : '"0"';
            var m4 = JSON.stringify(data[0].m4) != undefined ? JSON.stringify(data[0].m4) : '"0"';
            var m5 = JSON.stringify(data[0].m5) != undefined ? JSON.stringify(data[0].m5) : '"0"';

            var m6 = JSON.stringify(data[0].m6) != undefined ? JSON.stringify(data[0].m6) : '"0"';
            var m7 = JSON.stringify(data[0].m7) != undefined ? JSON.stringify(data[0].m7) : '"0"';
            var m8 = JSON.stringify(data[0].m8) != undefined ? JSON.stringify(data[0].m8) : '"0"';
            var m9 = JSON.stringify(data[0].m9) != undefined ? JSON.stringify(data[0].m9) : '"0"';
            var m10 = JSON.stringify(data[0].m10) != undefined ? JSON.stringify(data[0].m10) : '"0"';
            var m11 = JSON.stringify(data[0].m11) != undefined ? JSON.stringify(data[0].m11) : '"0"';
            var m12 = JSON.stringify(data[0].m12) != undefined ? JSON.stringify(data[0].m12) : '"0"';

            var year = JSON.stringify(data[0].year).substring(1, 5);
            content = content0 + year + bt + '"' + content1 + sz + '"' + content;
            //alert(content);
            var date = new Date;
            var month = date.getMonth() + 1;
            if (year != date.getFullYear()) {
                month = 12;
            }
            var char = new Array(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12);
            for (var i = 1; i <= month; i++) {
                content += '<set label="' + i + '月"  value=' + char[i - 1] + ' />'
            }
            //alert(content);
            // if(myChart==null){
            myChart = new FusionCharts("js/tjjs/FusionCharts/" + tb + ".swf", "myChartId", "100%", "450");
            myChart.setXMLData(content + '</chart>');
            myChart.render("chartContainer");
            // }else{
            //$("#chartContainer").show();
            // }

        }
    });
}

//当前时间
function tick(divId) {
    var today = new Date();
    var ww = today.getDay();
    if (ww == 0) ww = "星期日";
    if (ww == 1) ww = "星期一";
    if (ww == 2) ww = "星期二";
    if (ww == 3) ww = "星期三";
    if (ww == 4) ww = "星期四";
    if (ww == 5) ww = "星期五";
    if (ww == 6) ww = "星期六";
    $("#" + divId).text(today.toLocaleString() + " " + ww);
    window.setTimeout("tick('" + divId + "')", 1000);
};


//json字符串解析程json
function strToJson(str) {
    var json = eval('(' + str + ')');
    return json;
}

//获取部门下拉列表
function getDept(selectid, deptid) {
    var theurl = "";
    if (deptid.substring(0, 2) == "41") {
        theurl = "servlet/KqAction?method=bm_yd&deptid=" + deptid;
    } else if (deptid.substring(0, 2) == "51") {
        theurl = "servlet/KqAction?method=bm_mdk&deptid=" + deptid;
    } else if (deptid.substring(0, 2) == "61") {
        theurl = "servlet/KqAction?method=bm_wps&deptid=" + deptid;
    } else if (deptid.substring(0, 2) == "71") {
        theurl = "servlet/KqAction?method=bm_wpscj&deptid=" + deptid;
    }
    $.ajax({
        type: "POST",
        async: false,
        url: theurl,
        success: function (treeData) {
            var data = strToJson(treeData);
            console.info(data);
            $('#' + selectid).combotree('loadData', data);
            $("#" + selectid).combotree({
                multiple: false,
                onlyLeafCheck: false,//true只有最里层项可选
                cascadeCheck: false,
                onSelect: function (node) {
                    if (9000000000 == node.id) {
                        //清除选中
                        $('#' + selectid).combotree('clear');
                    }
                },
                onLoadSuccess: function (node, data1) {
                    $("#" + selectid).combotree('tree').tree("collapseAll");
                }

            });
        }
    });
}


//获取人员下拉列表
function getPerson(selectid, deptid) {
    var theurl = "";
    if (deptid.substring(0, 2) == "41") {
        theurl = "servlet/KqAction?method=zd&zdmc=ry_yd&zdlb=" + deptid;
    } else if (deptid.substring(0, 2) == "51") {
        theurl = "servlet/KqAction?method=zd&zdmc=ry_mdk&zdlb=" + deptid;
    } else if (deptid.substring(0, 2) == "61") {
        theurl = "servlet/KqAction?method=zd&zdmc=ry_wps&zdlb=" + deptid;
    } else if (deptid.substring(0, 2) == "71") {
        theurl = "servlet/KqAction?method=zd&zdmc=ry_wpscj&zdlb=" + deptid;
    }

    $.ajax({
        type: "POST",
        async: false,
        url: theurl,
        success: function (treeData) {
            if (treeData != '[]') {
                var data = strToJson(treeData);

                $('#' + selectid).combobox({
                    data: data,
                    valueField: 'id',
                    textField: 'text'
                });
            }

        }
    });
}


//获取人员列表时，验证是否选择所属部门
function yzry(deptid, personid) {
    var bm = $("#" + deptid).combobox('getValue');
    if (bm == null || bm == undefined || bm == "") {
        alert('请先选择所属部门!');
    } else {
        getPerson(personid, bm);
    }
}


function yzry1(deptid, personid) {
    var bm = $("#" + deptid).combobox('getValue');
    getPerson(personid, bm);
}

function isEmpty(str) {
    if (str == undefined || str == null || str == "") {
        return true;
    }
    return false;
}
	  
	  