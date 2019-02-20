<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
<script type="text/javascript"
	src="../js/jquery-1.9.1/jquery-1.9.1.min.js"></script>
<meta charset="UTF-8">
<title></title>
<script>
        document.createElement("header");
        document.createElement("footer");
    </script>
<style>
body {
	width: 100%;
	margin: 0px auto;
	height: 614px;
	background: #174672 url("../images/index/log_bg.jpg") no-repeat center
		center;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/index/log_bg.jpg',
		sizingMethod='scale' );
	-ms-filter:
		"progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/index/log_bg.jpg', sizingMethod='scale')";
}

h1,ul,li,h2 {
	margin: 0px;
	padding: 0px;
}

li {
	list-style: none;
}

.login {
	width: 1200px;
	margin: 25px auto 0px;
}

footer {
	display: block;
}

header {
	display: block;
}

.logFrom {
	width: 100%;
	margin: 40px auto 0px;
}

@media screen and (min-height: 650px) {
	body {
		height: 1000px;
	}
	.logFrom {
		width: 100%;
		margin-top: 100px;
	}
}

h2 {
	background: url("../images/index/tit_bg.png") no-repeat center bottom;
	width: 100%;
	float: left;
	text-align: center;
	padding-bottom: 15px;
	height: 30px;
	line-height: 30px;
	color: #fff;
	text-shadow: 3px 0px 3px #000;
	letter-spacing: 3px;
}

.logFrom ul {
	z-index: 9999;
	position: relative;
	margin: 0px 0px 0px 105px;
	display: inline;
	width: 282px;
	height: 290px;
	padding: 25px 19px 0px 19px;
	float: left;
	border: 1px solid #7594B2;
	/*  background: url("/images/index/log_box.png") no-repeat; */
	/*  background-color: rgba(0,73,134,0.6);
            border: 1px solid #deeef8;
            border-radius: 5px;
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1,startColorstr=#99013059, endColorstr=#99013059); */
}

.logFrom label {
	width: 75px;
	color: #fff;
	text-align: center;
	float: left;
	line-height: 25px;
	margin-left: 15px;
}

.logFrom li {
	margin-top: 20px;
	float: left;
}

.logFrom li input {
	float: left;
	background-color: #fff;
	padding: 0px;
}

.logFrom li span {
	width: 78px;
	float: left;
	margin-left: 16px;
}

.logFrom li span img {
	height: 28px;
	width: 60px;
}

.iptxt1,.iptxt2 {
	border: 1px solid #fff;
	height: 25px;
	line-height: 25px;
	float: left;
}

.iptxt1 {
	width: 170px;
}

.iptxt2 {
	width: 94px;
}

.ipbtn {
	background: url("../images/index/ipbtn.png") no-repeat;
	width: 123px;
	height: 34px;
	line-height: 34px;
	border: 0;
	padding: 0px;
	font-size: 14px;
	color: #fff;
	margin-left: 90px;
	display: inline;
}

footer {
	margin: 0px auto;
	position: fixed;
	bottom: 20px;
	width: 1100px;
	_position: absolute;
}

p {
	margin: 0px auto;
	text-align: center;
	color: #284168;
	line-height: 22px;
	font-size: 14px;
	letter-spacing: 1px;
}



.con_img{position: relative; width: 240px; height: 240px;}
.ms{position: absolute; bottom: 0;left: 0; width: 240px; height: 25px; background: #000;}
</style>
<script>
        document.createElement("header");
        document.createElement("footer");
		if("${s}"=='5'){
			    alert("登陆密码错误");
				location.href="../admin/index";
			}
			if("${s}"=='4'){
				alert("该用户不存在，请重新填写!");
				location.href="../admin/index";
			}
			function checkInput(){
				if($("#userName").val()==null || $("#userName").val()==""){
				   	alert("用户名不能为空");
					return false;
				}
				if($("#password").val()==null || $("#password").val()==""){
				    alert("登陆密码不能为空");
					return false;
				}
			}
			
    </script>
</head>
<body>
	<div class="login">
		<!-- <header>
			<h1>
				<img src="../images/index/logo.png">
			</h1>
		</header>
		<div class="logFrom">
			<form action="../admin/login" method="post"
				onsubmit="return checkInput();">
				<ul>
					<h2>用户登录</h2>
					<li><label>用户名：</label> <input type="text" id="userName"
						name="userName" class="iptxt1"></li>
					<li><label for="password">密 码：</label> <input type="password"
						id="password" name="password" class="iptxt1"></li>
					<li><input type="submit" value="登   录" class="ipbtn">
					</li>
				</ul>
			</form>
		</div>
		<footer>
			<p>wuchuijiu&copy;2018</p>
			<p>FUJIAN ZUNSHANG</p>
		</footer> -->
		<div class="con_img">
			<img src="../images/index/logo.png">
			<span class="ms">
				<div class="logFrom">
					<form action="../admin/login" method="post"
						onsubmit="return checkInput();">
						<ul>
							<h2>用户登录</h2>
							<li><label>用户名：</label> <input type="text" id="userName"
								name="userName" class="iptxt1"></li>
							<li><label for="password">密 码：</label> <input type="password"
								id="password" name="password" class="iptxt1"></li>
							<li><input type="submit" value="登   录" class="ipbtn">
							</li>
						</ul>
					</form>
				</div>
			</span>
		</div>
	</div>
</body>
</html>