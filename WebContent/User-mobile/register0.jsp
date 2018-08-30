<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.School" %>
<%@ page import="com.tspace.database.databaseschool" %>
<%@ page import="java.util.List" %>
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
<meta http-equiv="keywords" content="TSPACE,注册" />
<meta http-equiv="content" content="TSPACE注册" />
<meta http-equiv="refresh" content="800;url=https://www.tspace.top">
<script src="/tspace/js/usermobile/jquery.min.js"></script><!-- jquery.js 一定要加载在jquery.mobile.js之前 -->
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
		<script type="text/javascript">
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/mServletuser?op=checkSmstime",
				contentType: "application/json;charset=utf-8",
				success: function(data) {
					// alert(data);
					// alert(typeof data);
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
		<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/register.css">
		
		<div data-role="popup" data-dismissible="false" id="popup1" class="ui-content" data-position-to="window">
  			<p>短信发送成功，验证有效时间3分钟！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup2" class="ui-content" data-position-to="window">
  				<p>短信发送失败，请检查手机号是否正确！</p>
  				<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="header">
			<h1>TSpace用户注册</h1>
		</div>
		
		<div data-role="content">
			<p name="notice" id="notice" class="notice"></p>
			<input type="text" name="mobile" id="mobile" class="mobile" placeholder="手机">
			<div class="codeclass" id="pinblock">
    			<input type="text" id="inputpin" name="inputpin" class="inputpin" placeholder="验证码">
    			<img src="/tspace/mServletuser?op=registerpin" id="pin" name="pin" class="pin">
    			<a id="getpin" style="cursor:pointer;" onclick="reload()">看不清楚？ 换一张</a>
    		</div>
    		<a data-role="button" name="verify" id="verify" class="verify">提交验证</a>
			<div class="codeclass" id="smsblock">
				<input type="text" name="code" id="code" class="code" placeholder="验证码"/>
				<a onclick="getcode()" data-role="button" name="codebtn" id="codebtn" class="codebtn"></a>
			</div>			
			<a data-role="button" name="nextstep" id="nextstep" class="nextstep">下一步</a>
		</div>
		
		<div data-role="footer">
			<h4>Powered By TSpace</h4>
		</div>
		<script type="text/javascript">
			$("#smsblock").hide();
			$("#nextstep").hide();
		</script>
		<script src="/tspace/js/usermobile/register0.js"></script>
	</div>

</body>
</html>