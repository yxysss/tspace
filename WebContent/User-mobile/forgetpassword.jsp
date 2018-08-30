<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.listener.TimerTask" %>
<%
	TimerTask.browsingvolume += 1;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>TSpace空间网</title>
<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
name="viewport" id="viewport"/>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<meta http-equiv="keywords" content="TSPACE,忘记密码" />
<meta http-equiv="content" content="TSPACE忘记密码" />
<meta http-equiv="refresh"content="800;url=https://www.tspace.top">
<link type="text/css" rel="stylesheet" href="../css/usermobile/usermobile.css">
<script src="/tspace/js/usermobile/jquery.min.js"></script>
<script>
	$(document).bind("mobileinit", function() {
	    //disable ajax nav
	    $.mobile.ajaxEnabled=false;
	});
</script>
<script src="/tspace/js/usermobile/bootstrap.min.js"></script>
<script src="/tspace/js/usermobile/jquery.mobile-1.4.5.min.js"></script>
<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/jquery.mobile-1.4.5.min.css">
</head>
<body>
	
	<div data-role="page">
		<div data-role="popup" data-dismissible="false" id="popup1" class="ui-content pop" data-position-to="window">
  			<p>请输入用户名！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup2" class="ui-content pop" data-position-to="window">
  			<p>请输入验证码！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup3" class="ui-content pop" data-position-to="window">
  			<p>请输入密码！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup4" class="ui-content pop" data-position-to="window">
			<p>两次新密码输入不一致！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup5" class="ui-content pop" data-position-to="window">
			<p>密码修改成功，请重新登录！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back" onclick="backindex()">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup6" class="ui-content pop" data-position-to="window">
			<p>密码修改失败！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup7" class="ui-content pop" data-position-to="window">
			<p>验证码错误！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="smssuccess" class="ui-content pop" data-position-to="window">
  			<p>短信已成功发送到注册手机号，验证有效时间3分钟！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="smsfailure" class="ui-content pop" data-position-to="window">
  			<p>短信发送失败，请检查学号/工号是否正确以及注册所用的手机号是否可以使用！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<script>
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/mServletuser?op=fpcheckSmstime",
				contentType: "application/json;charset=utf-8",
				success: function(data) {
					/* alert(data);
					alert(typeof data); */
					if (data == 0) {
						var codebtn = document.getElementById("codebtn");
						codebtn.text="点击获取短信验证码";
						codebtn.setAttribute("onclick","getcode()");
						
					} else {
						var codebtn = document.getElementById("codebtn");
						codebtn.text=data+"秒后重新获取";
						codebtn.removeAttribute("onclick");
						var timer = setInterval(function() {
							data -= 1;
							if (data == 0) {
								clearInterval(timer);
								codebtn.text="点击获取短信验证码";
								codebtn.setAttribute("onclick","getcode()");
							} else {
								codebtn.text=data+"秒后重新获取";
							}
						}, 1000);
						return false;
					}
				}
			});
		</script>
		<script src="/tspace/js/usermobile/code.js"></script>
		<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/forgetpassword.css">
		
		<div data-role="header">
			<a onclick="backindex()" data-icon="arrow-l" data-rel="back">返回</a>
			<h1>忘记密码</h1>
		</div>
		
		<div data-role="content">
			<input type="text" name="username" id="username" class="username" placeholder="用户名"/>
			<p name="notice" id="notice" class="notice"></p>
			<div class="codeclass" id="pinblock">
    			<input type="text" id="inputpin" name="inputpin" class="inputpin" placeholder="验证码">
    			<img src="/tspace/mServletuser?op=forgetpwpin" id="pin" name="pin" class="pin">
    			<a id="getpin" style="cursor:pointer;" onclick="reload()">看不清楚？ 换一张</a>
    		</div>
    		<a data-role="button" name="verify" id="verify" class="verify">提交验证</a>
			<div class="codeclass" id="smsblock">
				<input type="text" name="code" id="code" class="code" placeholder="验证码"/>
				<a onclick="getcode()" data-role="button" name="codebtn" id="codebtn" class="codebtn"></a>
			</div>
			<input type="password" name="password" id="password" class="password" placeholder="新密码"/>
			<input type="password" name="confirmpw" id="confirmpw" class="confirmpw" placeholder="确认新密码"/>
			<a data-role="button" name="submit" id="submit" class="submit">确认</a>
			
		</div>
		
		<div data-role="footer">
			<h4>Powered By TSpace</h4>
		</div>
		
		<script type="text/javascript">
			//$("#username").hide();
			$("#smsblock").hide();
			//$("#password").hide();
			//$("#confirmpw").hide();
			$("#submit").hide();
		</script>
		
		<script src="/tspace/js/usermobile/forgetpassword.js"></script>
	
	</div>
</body>
</html>