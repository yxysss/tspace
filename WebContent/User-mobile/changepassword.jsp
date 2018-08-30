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
<meta http-equiv="keywords" content="TSPACE,修改密码" />
<meta http-equiv="content" content="TSPACE修改密码" />
<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/usermobile.css">
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
		<script src="/tspace/js/usermobile/code.js"></script>
		<div data-role="header">
			<a href="#" data-icon="arrow-l" data-rel="back">返回</a>
			<h1>修改密码</h1>
		</div>
		
		<div data-role="content">
			<input type="password" name="oldpassword" id="oldpassword" class="oldpassword" placeholder="原密码"/>
			<input type="password" name="newpassword" id="newpassword" class="newpassword" placeholder="新密码"/>
			<input type="password" name="confirmnpw" id="confirmnpw" class="confirmnpw" placeholder="确认新密码"/>
			<a data-role="button" name="submit" id="submit" class="submit">确认</a>
			
		</div>
		
		<div data-role="footer">
			<h4>Powered By TSpace</h4>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup1" class="ui-content" data-position-to="window">
  			<p>请输入原密码！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup2" class="ui-content" data-position-to="window">
  			<p>请输入新密码！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup3" class="ui-content" data-position-to="window">
			<p>两次新密码输入不一致！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
			
		<div data-role="popup" data-dismissible="false" id="popup4" class="ui-content" data-position-to="window">
			<p>密码修改成功，请重新登录！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back" onclick="backindex()">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup5" class="ui-content" data-position-to="window">
			<p>原密码有误！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup6" class="ui-content" data-position-to="window">
			<p>密码修改失败！</p>
			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<script src="/tspace/js/usermobile/changepassword.js"></script>
	</div>
</body>
</html>