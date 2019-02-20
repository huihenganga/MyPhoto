<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/include.jsp" %>
<!DOCTYPE html>
<html>
  <head>
  	<title>管理平台</title>
  	 <style>
        .header{
            display: block;
            width: 100%;
            margin: 0px auto;
            height:80px;
            background:#174672 url("../images/index/header_bg.jpg") no-repeat center top;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/index/header_bg.jpg', sizingMethod='scale');
            -ms-filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/index/header_bg.jpg', sizingMethod='scale')";
        }
        .header h1{
            margin: 13px 0px 0px 30px;
            float: left;
            height: 50px;
            line-height: 50px;
            /* font-family: "microsoft yahei"; */
            /* font-weight: 100; */
        }
        .header img, .header span{float: left;}
        .header span{
            font-size: 30px;
            color: #fff;
            padding-left: 10px;
        }
        .header div{
            float: right;
            width: 430px;
        }
        .header div span{
            float: right;
            width: 56px;
            background: url("../images/index/out_bg.png") no-repeat;
            height: 16px;
            line-height: 16px;
            padding:5px 0px 5px 0px;
            text-align: right;
        }
        .header div span a{
            /* background: url("../images/index/out_icon.png") no-repeat; */
            font-size: 12px;
            width:100%;
            text-align:center;
            color: #fff;
            float: left;
            font-weight: bold;
            text-decoration: none;
            z-index: 9999;
            position: relative;
        }
        .header p{
            width: 100%;
            display: block;
            float: left;
            color:#fff ;
            font-size: 14px;
            font-weight: bold;
            letter-spacing: 1px;
            margin-top: 15px;
        }
        .header b{color: #eaff35;padding-right: 10px;}
    </style>
  </head>
  <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:85px;background:#fff">
		<div class="header">
		    <h1>
		        <%--<img src="../images/index/logo_icon.png">
		        --%><span>管理平台</span>
		    </h1>
		    <div>
		        <span>
		            <a href="javascript:void(0)" onclick="out()">退 出</a>
		        </span>
		        <p>
		            欢迎您：${name}，现在是<time>${time }</time>
		        </p>
		    </div>
		</div>
	</div>
	<div data-options="region:'west',split:true" 
        style="width: 180px; padding: 1px 1px 80px 1px; color: Red; overflow: hidden;">
	    <div class="easyui-accordion" data-options="fit:true,border:false">
            ${menuHtml}
       </div>
       </div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div class="easyui-tabs" id="mainTabs" data-options="fit:true,border:false,tools:'#tab-tools'" style="background-image:url('');background-repeat: no-repeat;background-position: right top; ">
            
        </div>
	</div>
	</body>
</html>

