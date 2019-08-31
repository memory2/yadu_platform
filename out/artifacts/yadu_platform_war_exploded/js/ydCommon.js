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


//datagrid������excel
function ExporterExcel(gridID, gridBT) {
    //��ȡDatagride����
    var rows = $("#" + gridID).datagrid('getRows');
    var columns = $("#" + gridID).datagrid("options").columns[0];
    var oXL = new ActiveXObject("Excel.Application"); //����AX����excel 
    var oWB = oXL.Workbooks.Add(); //��ȡworkbook���� 
    var oSheet = oWB.ActiveSheet; //���ǰsheet
    //���ù���������
    oSheet.name = gridBT;
    //���ñ�ͷ
    for (var i = 0; i < columns.length; i++) {
        oSheet.Cells(1, i + 1).value = columns[i].title;
    }
    //�������ݲ���
    for (var i = 0; i < rows.length; i++) {
        //��̬��ȡÿһ��ÿһ�е�����ֵ
        for (var j = 0; j < columns.length; j++) {
            oSheet.Cells(i + 2, j + 1).value = rows[i][columns[j].field];
        }
    }
    oXL.Visible = true; //����excel�ɼ�����
    oXL = null;//�ͷŶ���
}

//datagridͨ��
function getDataGird(url, gridID, gridBT) {
    var editRow = undefined;
    $('#' + gridID).datagrid({
        url: url,
        pagination: true,
        toolbar: [
            {
                text: '����',
                iconCls: 'icon-dload',
                handler: function () {
                    ExporterExcel(gridID, gridBT);
                }
            }
        ]
    });


}

//datagridͨ��
function getDataGird1(url, gridID, gridBT, updUrl, delUrl, width, height) {
    var editRow = undefined;
    $('#' + gridID).datagrid({
        url: url,
        pagination: true,
        toolbar: [
            {
                text: '����',
                iconCls: 'icon-dload',
                handler: function () {
                    ExporterExcel(gridID, gridBT);
                }
            }, {
                text: '���',
                iconCls: 'icon-add',
                handler: function () {
                    edit(gridID, gridBT, updUrl, width, height, 'add');
                }
            }, {
                text: '�޸�',
                iconCls: 'icon-edit',
                handler: function () {
                    edit(gridID, gridBT, updUrl, width, height, 'upd');
                }
            }, {
                text: 'ɾ��',
                iconCls: 'icon-cancel',
                handler: function () {
                    del(gridID, delUrl);
                }
            }
        ]
    });
    var p = $('#' + gridID).datagrid('getPager');
    $(p).pagination({
        pageSize: 10,//ÿҳ��ʾ�ļ�¼������Ĭ��Ϊ10   
        pageList: [5, 10, 15, 1000],//��������ÿҳ��¼�������б�   
        beforePageText: '��',//ҳ���ı���ǰ��ʾ�ĺ���   
        afterPageText: 'ҳ    �� {pages} ҳ',
        displayMsg: '��ǰ��ʾ {from} - {to} ����¼   �� {total} ����¼',
    });


}

function edit(gridID, gridBT, updUrl, width, height, czlb) {
    var row;
    var href;
    var info;
    if ('add' != czlb) {
        row = $('#' + gridID).datagrid('getSelected');
        if (czlb != 'add' && !row) {
            $.messager.alert('��ʾ', '����ѡ����', 'info');
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
            loadingMessage: '���ڼ������ݣ����Ե�Ƭ��......'
        });

}


function del(gridID, delUrl) {
    var row = $('#' + gridID).datagrid('getSelected');
    if (!row) {
        $.messager.alert('��ʾ', '����ѡ����', 'info');
        return;
    }
    $.messager.confirm('ȷ��', '��ȷ����Ҫɾ����¼��', function (r) {
        if (r) {
            var href = delUrl + '&zj=' + row.id;
            $.ajax({
                type: 'POST',
                url: href,
                success: function (data) {
                    //alert(data);
                    if ('ok' == data) {
                        alert("ɾ���ɹ���");
                    } else {
                        alert("ɾ��ʧ��,����ϵ����Ա��");
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
    var content = ' yAxisMinValue="1000" name="333" showNames="1" showBorder="0" outCnvBaseFont="������κ"  outCnvBaseFontSize="20" bgColor="EEF3FA" canvasBgColor="EEF3FA" formatNumberScale="0" canvasBorderThickness="0"  showValues="1" showYAxisValues="1" showLegend="0"  labelDisplay="STAGGER" showPlotBorder="0" numDivLines="3"  borderThickness ="0" yAxisName="" xAxisName="" numberSuffix="Ԫ" plotSpacePercent="50">';

    var sm = {};
    $.ajax({
        type: 'POST',
        dataType: "json",
        url: 'servlet/OaAction?method=' + lb,
        success: function (data) {
            //var d = eval("("+data+")");//Ч����ͬ�ڼ�dataType
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
                content += '<set label="' + i + '��"  value=' + char[i - 1] + ' />'
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

//��ǰʱ��
function tick(divId) {
    var today = new Date();
    var ww = today.getDay();
    if (ww == 0) ww = "������";
    if (ww == 1) ww = "����һ";
    if (ww == 2) ww = "���ڶ�";
    if (ww == 3) ww = "������";
    if (ww == 4) ww = "������";
    if (ww == 5) ww = "������";
    if (ww == 6) ww = "������";
    $("#" + divId).text(today.toLocaleString() + " " + ww);
    window.setTimeout("tick('" + divId + "')", 1000);
};


//json�ַ���������json
function strToJson(str) {
    var json = eval('(' + str + ')');
    return json;
}

//��ȡ���������б�
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
            $('#' + selectid).combotree('loadData', data);
            $("#" + selectid).combotree({
                multiple: false,
                onlyLeafCheck: false,//trueֻ����������ѡ
                cascadeCheck: false,
                onSelect: function (node) {
                    if (9000000000 == node.id) {
                        //���ѡ��
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


//��ȡ��Ա�����б�
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


//��ȡ��Ա�б�ʱ����֤�Ƿ�ѡ����������
function yzry(deptid, personid) {
    var bm = $("#" + deptid).combobox('getValue');
    if (bm == null || bm == undefined || bm == "") {
        alert('����ѡ����������!');
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
	  
	  