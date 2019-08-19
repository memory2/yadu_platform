/**
 * jQuery JSON plugin 2.4.0
 *
 * @author Brantley Harris, 2009-2011
 * @author Timo Tijhof, 2011-2012
 * @source This plugin is heavily influenced by MochiKit's serializeJSON, which is
 *         copyrighted 2005 by Bob Ippolito.
 * @source Brantley Harris wrote this plugin. It is based somewhat on the JSON.org
 *         website's http://www.json.org/json2.js, which proclaims:
 *         "NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.", a sentiment that
 *         I uphold.
 * @license MIT License <http://www.opensource.org/licenses/mit-license.php>
 */

(function($) {
	'use strict';

	var escape = /["\\\x00-\x1f\x7f-\x9f]/g,
		meta = {
			'\b': '\\b',
			'\t': '\\t',
			'\n': '\\n',
			'\f': '\\f',
			'\r': '\\r',
			'"': '\'',
			'\\': '\\\\'
		},
		hasOwn = Object.prototype.hasOwnProperty;

	/**
	 * jQuery.toJSON
	 * Converts the given argument into a JSON representation.
	 *
	 * @param o {Mixed} The json-serializable *thing* to be converted
	 *
	 * If an object has a toJSON prototype, that will be used to get the representation.
	 * Non-integer/string keys are skipped in the object, as are keys that point to a
	 * function.
	 *
	 */
	$.toJSON = typeof JSON === 'object' && JSON.stringify ? JSON.stringify : function(o) {
		if (o === null) {
			return 'null';
		}

		var pairs, k, name, val,
			type = $.type(o);

		if (type === 'undefined') {
			return undefined;
		}

		// Also covers instantiated Number and Boolean objects,
		// which are typeof 'object' but thanks to $.type, we
		// catch them here. I don't know whether it is right
		// or wrong that instantiated primitives are not
		// exported to JSON as an {"object":..}.
		// We choose this path because that's what the browsers did.
		if (type === 'number' || type === 'boolean') {
			return String(o);
		}
		if (type === 'string') {
			return $.quoteString(o);
		}
		if (typeof o.toJSON === 'function') {
			return $.toJSON(o.toJSON());
		}
		if (type === 'date') {
			var month = o.getUTCMonth() + 1,
				day = o.getUTCDate(),
				year = o.getUTCFullYear(),
				hours = o.getUTCHours(),
				minutes = o.getUTCMinutes(),
				seconds = o.getUTCSeconds(),
				milli = o.getUTCMilliseconds();

			if (month < 10) {
				month = '0' + month;
			}
			if (day < 10) {
				day = '0' + day;
			}
			if (hours < 10) {
				hours = '0' + hours;
			}
			if (minutes < 10) {
				minutes = '0' + minutes;
			}
			if (seconds < 10) {
				seconds = '0' + seconds;
			}
			if (milli < 100) {
				milli = '0' + milli;
			}
			if (milli < 10) {
				milli = '0' + milli;
			}
			return '"' + year + '-' + month + '-' + day + 'T' +
				hours + ':' + minutes + ':' + seconds +
				'.' + milli + 'Z"';
		}

		pairs = [];

		if ($.isArray(o)) {
			for (k = 0; k < o.length; k++) {
				pairs.push($.toJSON(o[k]) || 'null');
			}
			return '[' + pairs.join(',') + ']';
		}

		// Any other object (plain object, RegExp, ..)
		// Need to do typeof instead of $.type, because we also
		// want to catch non-plain objects.
		if (typeof o === 'object') {
			for (k in o) {
				// Only include own properties,
				// Filter out inherited prototypes
				if (hasOwn.call(o, k)) {
					// Keys must be numerical or string. Skip others
					type = typeof k;
					if (type === 'number') {
						name = '"' + k + '"';
					} else if (type === 'string') {
						name = $.quoteString(k);
					} else {
						continue;
					}
					type = typeof o[k];

					// Invalid values like these return undefined
					// from toJSON, however those object members
					// shouldn't be included in the JSON string at all.
					if (type !== 'function' && type !== 'undefined') {
						val = $.toJSON(o[k]);
						pairs.push(name + ':' + val);
					}
				}
			}
			return '{' + pairs.join(',') + '}';
		}
	};

	/**
	 * jQuery.evalJSON
	 * Evaluates a given json string.
	 * 格式化json
	 * @param str {String}
	 */
	$.evalJSON = typeof JSON === 'object' && JSON.parse ? JSON.parse : function(str) {
		/*jshint evil: true */
		return eval('(' + str + ')');
	};

	/**
	 * jQuery.secureEvalJSON
	 * Evals JSON in a way that is *more* secure.
	 * 安全环境下格式化json
	 * @param str {String}
	 */
	$.secureEvalJSON = typeof JSON === 'object' && JSON.parse ? JSON.parse : function(str) {
		var filtered =
			str
			.replace(/\\["\\\/bfnrtu]/g, '@')
			.replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
			.replace(/(?:^|:|,)(?:\s*\[)+/g, '');

		if (/^[\],:{}\s]*$/.test(filtered)) {
			/*jshint evil: true */
			return eval('(' + str + ')');
		}
		throw new SyntaxError('Error parsing JSON, source is not valid.');
	};

	/**
	 * jQuery.quoteString
	 * Returns a string-repr of a string, escaping quotes intelligently.
	 * Mostly a support function for toJSON.
	 * Examples:
	 * >>> jQuery.quoteString('apple')
	 * "apple"
	 *
	 * >>> jQuery.quoteString('"Where are we going?", she asked.')
	 * "'Where are we going?', she asked."
	 */
	$.quoteString = function(str) {
		if (str.match(escape)) {
			return '"' + str.replace(escape, function(a) {
				var c = meta[a];
				if (typeof c === 'string') {
					return c;
				}
				c = a.charCodeAt();
				return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
			}) + '"';
		}
		return '"' + str + '"';
	};
	/**
	 * 处理IE6下面png图片问题
	 */
	$.correctPNG = function() {
		var arVersion = navigator.appVersion.split("MSIE")
		var version = parseFloat(arVersion[1])
		if ((version >= 5.5) && (document.body.filters)) {
			for (var j = 0; j < document.images.length; j++) {
				var img = document.images[j]
				var imgName = img.src.toUpperCase()
				if (imgName.substring(imgName.length - 3, imgName.length) == "PNG") {
					var imgID = (img.id) ? "id='" + img.id + "' " : ""
					var imgClass = (img.className) ? "class='" + img.className + "' " : ""
					var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
					var imgStyle = "display:inline-block;" + img.style.cssText
					if (img.align == "left") imgStyle = "float:left;" + imgStyle
					if (img.align == "right") imgStyle = "float:right;" + imgStyle
					if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle
					var strNewHTML = "<span " + imgID + imgClass + imgTitle + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";" + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader" + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>"
					img.outerHTML = strNewHTML
					j = j - 1
				}
			}
		}
	}
	/**********************************
	 * 自定义函数
	 *********************************/
	/**
	 * 在DataGrid中获取所选记录的id数组
	 * 注：该方法使用的前提是：DataGrid的idField属性对应到列表Json数据中的字段名必须为id
	 *
	 * @param dataTableId目标记录所在的DataGrid列表table的id
	 * @return 所选记录的id数组
	 */
	$.getCheckedIds = function(dataTableId, noOneSelectMessage) {
		var rows = $('#' + dataTableId).datagrid('getChecked');
		var ids = [],
			num = rows.length;
		if (num < 1 && null != noOneSelectMessage) {
			$.messager.confirm('提示消息', noOneSelectMessage, function(r) {
				if (!r) {
					return false;
				}
			});
		} else {
			for (var i = 0; i < num; i++) {
				ids.push(rows[i].id);
			}
		}
		return ids;
	};
	/**
	 * 根据编号id，选中表格，或者不选中表格
	 */
	$.checkGridRow = function(grid, id, check) {
		var dt = $('#' + grid);
		if (check) {
			dt.datagrid('checkRow', dt.datagrid('getRowIndex', id));
		} else {
			dt.datagrid('uncheckRow', dt.datagrid('getRowIndex', id));
		}
	}
	/**
	 * 在ComboTree多选树中获取所选记录的id数组
	 *
	 * @param comboTreeId目标记录所在的ComboTree组件的id
	 * @return 所选记录的id数组
	 */
	$.getComboTreeIds = function(comboTreeId) {
		var nodes = $('#' + comboTreeId).combotree('getValues');
		var ids = [];
		for (var i = 0, l = nodes.length; i < l; i++) {
			if (nodes[i] > 0) {
				ids.push(nodes[i]);
			}
		}
		return ids;
	};
	/**
	 * 监听DataGrid组件记录的选择操作，包括onCheck、onUncheck、onCheckAll、onUncheckAll四个事件
	 * 注：该方法使用的前提是：DataGrid的idField属性对应到列表Json数据中的字段名必须为id
	 *
	 * @param dataTableId目标记录所在的DataGrid列表table的id
	 */
	$.comboTreeCheckListener = function(comboTreeId, gridId, url, iframeName, callback) {
		$('#' + comboTreeId).combotree({
			onCheck: function(node, checked) {
				var ids = $.getComboTreeIds(comboTreeId);
				$('#' + comboTreeId).combotree('hidePanel');
				if (callback) {
					callback();
				}
				var dtDevices = $('#' + gridId);
				dtDevices.datagrid('loadData', {
					total: 0,
					rows: []
				}).datagrid('loading');
				$.post(url, {
					ids: ids.join(',')
				}, function(data) {
					dtDevices.datagrid('loaded');
					if (data) {
						var rows = $.parseJSON(data);
						dtDevices.datagrid('loadData', rows).datagrid('checkAll');
						dtDevices.datagrid({
							onCheck: function(rowIndex, rowData) {
								iframeMap.selectOneObject(rowData.id, 2);
							},
							onUncheck: function(rowIndex, rowData) {
								iframeMap.selectOneObject(rowData.id, 1);
							},
							onCheckAll: function(rows) {
								var dtIds = [];
								for (var i = 0, l = rows.length; i < l; i++) {
									dtIds.push(rows[i].id);
								}
								iframeMap.selectAllObject(dtIds, 2); //地图选中
							},
							onUncheckAll: function(rows) {
								iframeMap.unSelectAllObject(); //地图选中
							}
						});
					}
				});
				var iframeMap = window.frames[iframeName];
				iframeMap.selectAllObject(ids, 2); //地图选中
			}
		});
	};
	/**
	 * 监听DataGrid组件记录的选择操作，包括onCheck、onUncheck、onCheckAll、onUncheckAll四个事件
	 * 注：该方法使用的前提是：DataGrid的idField属性对应到列表Json数据中的字段名必须为id
	 *
	 * @param dataTableId目标记录所在的DataGrid列表table的id
	 * @param url：查询服务对应的url路径
	 */
	$.paginationListener = function(dataTableId, url, sortField) {
		var dt = $("#" + dataTableId);
		var pg = dt.datagrid("getPager");
		if (pg) {
			$(pg).pagination({
				onSelectPage: function(pageNumber, pageSize) {
					dt.datagrid('loading');
					$.post(url, $.toJSON($.getPageParam(pageNumber, pageSize, $('#tableName').val(), sortField)), function(data) {
						dt.datagrid('loaded');
						var rows = $.parseJSON(data);
						$('#tableName').val(rows.tableName);
						dt.datagrid('loadData', {
							"rows": rows.listResult
						});
					});
					return;
				}
			});
		}
	};
	/**
	 * 针对开始、结束时间进行对比判断，并检查imsi和imei
	 *
	 * @param beginTime：开始时间
	 * @param endTime：结束时间
	 * @param checkIm：是否检测imsi和imei
	 * @param imsi：imsi的值
	 * @param imei：imei的值
	 */
	$.checkDateTime = function(beginTime, endTime, check) {
		if (check) {
			if (!$.trim(beginTime) || !$.trim(endTime)) {
				$.messager.alert('提示', '请填写必填项！');
				return false;
			}
		}
		var begin = new Date(beginTime.replace(/-/g, '/'));
		var end = new Date(endTime.replace(/-/g, '/'));
		if (end < begin) {
			$.messager.alert('提示', '开始日期小于结束日期， 请重新选择查询日期！');
			return false;
		}
		return true;
	};
	/**
	 * 根据表格id，路径url和参数查询数据，并显示
	 */
	$.queryGrid = function(dataTableId, url, param) {
		var dtGrids = $('#' + dataTableId);
		dtGrids.datagrid('loading');
		$.post(url, param, function(data) {
			dtGrids.datagrid('loaded');
			var rows = $.parseJSON(data);
			dtGrids.datagrid('loadData', rows);
		});
	};
	/**
	 * 根据给定的html元素，寻找其范围内带有query样式的对象，并取出对应值，组成查询键值对
	 */
	$.getQueryParam = function(id) {
		var param = {};
		$('#' + id).find('.query').each(function() {
			var name = $(this).attr('name');
			var status=$(this).attr('status');//充值状态
			var payway=$(this).attr('payway');//充值方式
			var sign=$(this).attr('sign');//小于大于等号
			var money=$(this).attr('money');
			var  truename=$(this).attr('truename');//真实姓名 
			var idCard=$(this).attr('idCard');
			var startTime=$(this).attr('startTime');//开始结束时间
			var endTime=$(this).attr('endTime');
			var val = $(this).val();
			if ($(this).hasClass('combobox-f')) {
				name = $(this).attr('comboname');
				val = $(this).combobox('getValue');
			}
			if ($(this).hasClass('combogrid-f')) {
				name = $(this).attr('comboname');
				val = $(this).combogrid('getValue');
			}
			if ($(this).hasClass('combotree-f')) {
				name = $(this).attr('comboname');
				val = $(this).combotree('getValue');
			}
			if ($(this).hasClass('datebox-f')) {
				name = $(this).attr('comboname');
				val = $(this).datebox('getValue');
			}
			if ($(this).hasClass('datetimebox-f')) {
				name = $(this).attr('comboname');
				val = $(this).datetimebox('getValue');
			}
			
			param[name] = val;
		});
		return param;
	};
	/**
	 * 根据网页访问的url取到?号后面的参数
	 */
	$.request = function(url, paras) {
		var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
		var paraObj = {}, j;
		for (var i = 0; j = paraString[i]; i++) {
			paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
		}
		var returnValue = paraObj[paras.toLowerCase()];
		if (typeof(returnValue) == "undefined") {
			return "";
		} else {
			return returnValue;
		}
	};
	/**
	 * 根据树节点，获取所选择的树节点编号
	 */
	$.getNodes = function($tree, node) {
		var id = node.id + "";
		if ($.inArray(id, ids) == -1) {
			ids.push(id);
		}
		var pnode = $tree.tree('getParent', node.target); //获取当前节点的父节点
		if (pnode) {
			$.getNodes($tree, pnode);
		}
	};
	/**
	 * 根据参数和url以及方法下载文件
	 */
	$.upload = function(url, data, handler, rows) {
		// 获得url和data
		if (!url) {
			url = ctx + '/file/upload';
		}
		var inputs = '<input id="fileUpload" type="file" name="fileupload" style="display:none"/>';
		if (data) {
			// data 是 string 或者 array/object
			data = typeof data == 'string' ? data : $.param(data);
			// 把参数组装成 form的  input
			$.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
			});
		}
		// request发送请求
		var form = $('<form method="post" enctype="multipart/form-data">' + inputs + '</form>');
		var divDlg = $('<div id="updlg"><table id="dt-upgrids"></table></div>').append(form).appendTo('body');
		divDlg.dialog({
			title: '文件上传',
			width: 400,
			height: 300,
			closed: true,
			modal: true,
			onBeforeClose: function() {
				divDlg.empty(); //在关闭之前如果不清空，则下次打开可能存在多个
			},
			onBeforeOpen: function() {
				$('#dt-upgrids').datagrid({
					singleSelect: true,
					rownumbers: true,
					fit: true,
					columns: [
						[{
							field: 'url',
							hidden: true
						}, {
							field: 'fileName',
							title: '文件名',
							width: 150
						}, {
							field: 'fileSize',
							title: '大小',
							width: 50
						}, {
							field: 'down',
							title: '下载',
							width: 80,
							formatter: function(value,row){

								return '<a href="'+ctx+'/file/download?p='+row.url+'&&n='+row.fileName+'">'+row.fileName+'</a>'
							}
						}]
					],
					toolbar: [{
						text: '选择',
						iconCls: 'icon-search',
						handler: function() {
							$('#fileUpload').click()
						}
					}, {
						text: '删除',
						iconCls: 'icon-remove',
						handler: function() {
							var dt = $('#dt-upgrids');
							var row = dt.datagrid('getSelected');
							if (row) {
								$.messager.confirm('确认', '您确定要删除选中的文件吗?', function(r) {
									if (r) {
										$.post(ctx + '/file/delete?p=' + row.url, function(data) {
											$.messager.show({
												title: '提示',
												msg: '文件删除成功！',
												timeout: 2000
											},'json');
											dt.datagrid('deleteRow', dt.datagrid('getRowIndex', row)).datagrid('reload');
										}, "text");
									}
								});
							} else {
								$.messager.show({
									title: '提示',
									msg: '请先选择一条记录，再进行删除！',
									timeout: 2000
								});
							}
						}
					}, {
						text: '确定',
						iconCls: 'icon-save',
						handler: function() {
							if (handler) {
								var rows = $('#dt-upgrids').datagrid('getRows');
								handler(rows);
								divDlg.dialog('close');
							}
						}
					}]
				});
				if(rows){
					$('#dt-upgrids').datagrid('loadData',{rows:rows})
				}
				$('#fileUpload').change(function() {
					form.form('submit', {
						url: url,
						onSubmit: function() {
							return true;
						},
						success: function(data) {
//							var fileRow = $.parseJSON(data);//原来1.3.5使用方式
							var fileRow = eval('(' + data + ')');  //升级到1.4.1后不能使用原来的json格式,需要将json转换成JavaScript使用
							if (fileRow.error == 0) {
								$('#dt-upgrids').datagrid('insertRow', {
									row: fileRow
								}).datagrid('reload');
							} else {
								$.messager.alert('错误', fileRow.msg);
							}
						}
					});
				});
			}
		}).dialog('open');
	};
	/**
	 * 根据参数和url以及方法下载文件
	 */
	$.download = function(url, data, method) {
		// 获得url和data
		if (!url) {
			url = ctx + '/file/download';
		}
		if (data) {
			// data 是 string 或者 array/object
			data = typeof data == 'string' ? data : $.param(data);
			// 把参数组装成 form的  input
			var inputs = '';
			$.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
			});
			// request发送请求
			$('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>').appendTo('body').submit().remove();
		};
	};
	$.formatToDecimal = function(num, n){
		var s = 1;
		var k = n;
		while(k-- > 0){
			s *= 10;
		}
		var f_num = Math.round(num * s)/s;
		var s_num = f_num.toString();
		var pos = s_num.indexOf('.');
		if(pos < 0){
			pos = s_num.length;
			s_num += '.';
		}
		while(s_num.length <= pos + n){
			s_num += '0';
		}
		return s_num;
	};
	/**
	 * 等额付款的现金支出（还款计划）数据生成
	 * item 款项
	 * lastSum 债务本金（贷款总额）
	 * beginCal 债务开始计息时间
	 * beginPay 债务开始还款时间
	 * times 债务的期限（贷款期限（月份数））
	 * rate 债务的年利率
	 * flag 是否取整
	 * xs 贷款利率系数
	 */
	$.CalcLoanPay = function(item, lastSum, beginCal, beginPay, times, rate, xs, flag) {
		var r, m, inter, c, dtemp, i = 0,
			list = [],
			fTotalInterest = 0;
		// 推算第一个月贷款天数
		var bCal = new XDate(beginCal);
		var bPay = new XDate(beginPay);
		var days = bCal.diffDays(bPay);
		// 计算月利率，需要乘上系数
		r = rate*xs / 12;
		dtemp = Math.pow((1 + r), times);
		//计算每月定额还款额度
		if (dtemp != 1)
			m = (lastSum * r * dtemp / (dtemp - 1));
		else
			m = lastSum / times;
		//计算每月还款本金和利息
		for (i = 0; i < times; i++) {
			inter = lastSum * r; //计算利息
			c = m - inter; //计算本金
			//如果是第一个月，则根据实际情况处理
			if (i == 0) {
				inter = lastSum * r * days / 30;
			}
			//如果需要取整，则进行取整操作
			if (flag) {
				inter = Math.ceil(inter);
				c = Math.ceil(c);
			}
			if(i>0){
				bPay.addMonths(1);//日期会自动保存计算后的值	
			}
			lastSum = lastSum - c;
			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = Math.round(inter*100)/100;
			fTotalInterest += node.hklx;
			node.hkbj = Math.round(c*100)/100;
			node.hkhj = Math.round((inter + c)*100)/100;
			node.bjye = Math.round(lastSum*100)/100;
			node.hkll = rate;
			list.push(node);
		}
		return {'list':list,'totalLx':Math.round(fTotalInterest*100)/100};
	};
	/**
	 * 等本付款的现金支出（还款计划）数据生成（本金还款法）
	 * item 款项
	 * lastSum 债务本金（贷款总额）
	 * beginCal 债务开始计息时间
	 * beginPay 债务开始还款时间
	 * times 债务的期限（贷款期限（月份数））
	 * rate 债务的年利率
	 * xs 贷款利率系数
	 */
	$.ECorpus = function(item, lastSum, beginCal, beginPay, times, rate, xs) {
		var r, inter, c, i = 0,
			m, fTotalInterest = 0,
			list = [];
		var bPay = new XDate(beginPay);
		r = rate*xs / 12;
		if (times != 0)
			c = lastSum / times;
		else
			c = 0;
		for (i = 0; i < times; i++) {
			if(i>0){
				bPay.addMonths(1);
			}
			inter = lastSum * r;
			lastSum = lastSum - c;

			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = Math.round(inter * 100) / 100;
			fTotalInterest += node.hklx;
			node.hkbj = Math.round(c * 100) / 100;
			node.hkhj = Math.round((inter + c) * 100) / 100;
			node.bjye = Math.round(lastSum * 100) / 100;
			node.hkll = rate;
			list.push(node);
		}
		return {'list':list,'totalLx':Math.round(fTotalInterest*100)/100};
	};
	/**
	 * 信用卡（自定义）还款方式计算
	 * item 款项名称
	 * lastSum 总款项
	 * beginPay 开始还款日期
	 * times 还款期次
	 */
	$.A4 = function(item, lastSum, beginPay, times){
		var m, c, l=lastSum, i = 0, list = [];
		// 推算第一个月贷款天数
		var bPay = new XDate(beginPay);
		m = Math.floor(lastSum/times);
		//计算每月还款本金和利息
		for (i = 0; i < times; i++) {
			c = m;
			if (i == 0) {
				c = lastSum - (m*(times-1));
			}
			l = l - c;
			if(i>0){
				bPay.addMonths(1);//日期会自动保存计算后的值	
			}
			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = 0.0;
			node.hkbj = Math.round(c * 100) / 100;
			node.hkhj = Math.round(c * 100) / 100;
			node.bjye = Math.round(l * 100) / 100;
			node.hkll = 0.0;
			list.push(node);
		}
		return {'list':list,'totalLx':0.0};
	};
	/**
	 * 等额付款的现金支出（还款计划）数据生成
	 * item 款项
	 * lastSum 债务本金（贷款总额）
	 * beginPay 债务开始还款时间
	 * times 债务的期限（贷款期限（月份数））
	 * rate 债务的年利率
	 * flag 是否取整
	 * xs 贷款利率系数
	 */
	$.A5 = function(item, lastSum, beginPay, times, rate, xs, flag) {
		var r, m, inter, c, dtemp, i = 0,
			list = [],
			fTotalInterest = 0;
		// 推算第一个月贷款天数
		var bPay = new XDate(beginPay);
		// 计算月利率，需要乘上系数
		r = rate*xs / 12;
		dtemp = Math.pow((1 + r), times);
		//计算每月定额还款额度
		if (dtemp != 1)
			m = (lastSum * r * dtemp / (dtemp - 1));
		else
			m = lastSum / times;
		//计算每月还款本金和利息
		for (i = 0; i < times; i++) {
			inter = lastSum * r; //计算利息
			//如果需要取整，则进行取整操作
			if (flag) {
				m = Math.ceil(m);
			}
			c = m - inter; //计算本金
			if(i>0){
				bPay.addMonths(1);//日期会自动保存计算后的值	
			}
			//如果最后一期剩余本金不够，则使用所有剩余金额
			if(lastSum<c){
				c=Math.ceil(lastSum)-inter;
				lastSum = 0;
			}else{
				lastSum = lastSum - c;
			}

			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = Math.round(inter * 100) / 100;
			fTotalInterest += node.hklx;
			node.hkbj = Math.round(c * 100) / 100;
			node.hkhj = Math.round((inter + c) * 100) / 100;
			node.bjye = Math.round(lastSum * 100) / 100;
			node.hkll = rate;
			list.push(node);
		}
		return {'list':list,'totalLx':Math.round(fTotalInterest*100)/100};
	};
	$.A10 = function(item, lastSum, lastLx, beginPay, times, rate){
		var m, c, l=lastSum, lx=0.0, i = 0, list = [];
		// 推算第一个月贷款天数
		var bPay = new XDate(beginPay);
		lx = lastLx / times;
		//计算每月还款本金和利息
		for (i = 0; i < times; i++) {
			if(i>=0 && i<=5){
				m = lastSum * 0.3/6;
			}else if(i>=6 && i<= 11){
				m = lastSum * 0.275/6;
			}else if(i>=12 && i<= 17){
				m = lastSum * 0.225/6;
			}else{
				m = lastSum * 0.2/6;
			}
			l = l - m;
			if(i>0){
				bPay.addMonths(1);//日期会自动保存计算后的值	
			}
			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = lx;
			node.hkbj = Math.round((c-lx) * 100) / 100;
			node.hkhj = Math.round(c * 100) / 100;
			node.bjye = Math.round(l * 100) / 100;
			node.hkll = rate;
			list.push(node);
		}
		return {'list':list,'totalLx':lastLx};
	};
	$.A13 = function(item, lastSum, lastLx, beginPay, times, rate){
		var m, c, l=lastSum, lx=0.0, i = 0, list = [];
		// 推算第一个月贷款天数
		var bPay = new XDate(beginPay);
		lx = lastLx / times;
		//计算每月还款本金和利息
		for (i = 0; i < times; i++) {
			if(i>=0 && i<=5){
				m = lastSum * 0.3/6;
			}else if(i>=6 && i<= 11){
				m = lastSum * 0.275/6;
			}else if(i>=12 && i<= 17){
				m = lastSum * 0.225/6;
			}else{
				m = lastSum * 0.2/6;
			}
			l = l - m;
			if(i>0){
				bPay.addMonths(1);//日期会自动保存计算后的值	
			}
			//-----绑定数据到表格-----
			var node = {};
			node.kx = item;
			node.qc = i+1;
			node.jhhkrq = bPay.toString("yyyy-MM-dd");
			node.hklx = lx;
			node.hkbj = Math.round((c-lx) * 100) / 100;
			node.hkhj = Math.round(c * 100) / 100;
			node.bjye = Math.round(l * 100) / 100;
			node.hkll = rate;
			list.push(node);
		}
		return {'list':list,'totalLx':lastLx};
	};
	$.selectKh = function(form, callback) {
		var dlg = $('#dlg-selectKh');
		if (!dlg.length) {
			dlg = $('<div id="dlg-selectKh"></div>').appendTo('body');
			dlg.dialog({
				title: '选择客户信息',
				width: 600,
				height: 450,
				closed: true,
				href: ctx + form,
				buttons: [{
					text: '查询',
					iconCls: 'icon-search',
					handler: function() {
							
					}
				}],
				onLoad: function() {

				}
			});
		}
		dlg.dialog('open');
	};
})(jQuery);

//判断当前字符串是否以str开始 先判断是否存在function是避免和js原生方法冲突，自定义方法的效率不如原生的高
if (typeof String.prototype.startsWith != 'function') {
	String.prototype.startsWith = function(str) {
		return this.slice(0, str.length) == str;
	};
}
//判断当前字符串是否以str结束
if (typeof String.prototype.endsWith != 'function') {
	String.prototype.endsWith = function(str) {
		return this.slice(-str.length) == str;
	};
}