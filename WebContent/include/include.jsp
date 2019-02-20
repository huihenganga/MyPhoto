<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta name="format-detection" content="telephone=no" />
<link rel="stylesheet" href="../js/jquery-easyui-1.4.1/themes/default/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="../js/jquery-easyui-1.4.1/themes/icon.css" type="text/css"></link>
<link rel="stylesheet" href="../js/uploadify/uploadify.css" type="text/css"></link>
<link rel="stylesheet" href="../js/artDialog/skins/default.css" type="text/css"></link>
<link rel="stylesheet" href="../css/common.css" type="text/css"></link>
<script type="text/javascript" src="../js/jquery-1.9.1/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../js/artDialog/jquery.artDialog.js"></script>
<script type="text/javascript" src="../js/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="../js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../js/public.js"></script>
<script type="text/javascript" src="../js/static/public.js"></script>
<script type="text/javascript" src="../js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="../kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<style>
a {
	text-decoration: none
}
</style>
<script>
	//重新载入
	function ttreload() {
		$("#tt").datagrid("reload");
	}
	function ajaxLoading() {
		$("<div class=\"datagrid-mask\"></div>").css({
			display : "block",
			width : "100%",
			height : $(window).height()
		}).appendTo("body");
		$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。")
				.appendTo("body").css({
					display : "block",
					left : ($(document.body).outerWidth(true) - 190) / 2,
					top : ($(window).height() - 45) / 2
				});
	}
	function ajaxLoadEnd() {
		$(".datagrid-mask").remove();
		$(".datagrid-mask-msg").remove();
	}
	var frmIndex = 0;
	function addTab(tit, url) {
		// url="../js/jquery-easyui-1.4.1/demo/datagrid/basic.html";
		var ct = $('#mainTabs');
		if (ct.tabs('exists', tit)) {
			ct.tabs('select', tit);
			reload();
		} else {
			ct.tabs('add', {
				title : tit,
				content : '<iframe  frameborder="0"  src="' + url
						+ '" style="width:100%;height:99%" id="mainfrm'
						+ (++frmIndex) + '"></iframe>',
				closable : true
			});
		}
	}
	//重新载入
	function reload() {
		var selTab = $('#mainTabs').tabs('getSelected');
		if (selTab != null && selTab.find('iframe').length > 0) {
			var reloadIframe = selTab.find('iframe')[0];
			var reloadUrl = reloadIframe.src;
			reloadIframe.contentWindow.location.href = reloadUrl;
		}
	}
	function out() {
		$.messager.confirm('提示', '你确定要退出吗?', function(r) {
			if (r) {
				document.location.href = "../admin/logout";
			}
		});
	}

	function dyniframesize(frmId) {
		var h = $("#" + frmId).contents().find("body").height() + 100;
		$("#" + frmId).css("height", h);
	}
	var aCity = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	}

	function isCardID(sId) {
		var iSum = 0;
		var info = "";
		if (!/^\d{17}(\d|x)$/i.test(sId))
			return "你输入的身份证长度或格式错误";
		sId = sId.replace(/x$/i, "a");
		if (aCity[parseInt(sId.substr(0, 2))] == null)
			return "你的身份证地区非法";
		sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-"
				+ Number(sId.substr(12, 2));
		var d = new Date(sBirthday.replace(/-/g, "/"));
		if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d
				.getDate()))
			return "身份证上的出生日期非法";
		for ( var i = 17; i >= 0; i--)
			iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
		if (iSum % 11 != 1)
			return "你输入的身份证号非法";
		return true;//aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女")   
	}

	$.extend($.fn.validatebox.defaults.rules, {
		idcared : {
			validator : function(value, param) {
				var flag = isCardID(value);
				return flag == true ? true : false;
			},
			message : '不是有效的身份证号码'
		}
	});
</script>
</head>
</html>
