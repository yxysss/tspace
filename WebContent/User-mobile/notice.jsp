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
<!-- 禁用缓存 -->
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<meta http-equiv="keywords" content="TSPACE,通知" />
<meta http-equiv="content" content="TSPACE通知" />
<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/usermobile.css">
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
	
		<div data-role="header">
			<h1>TSpace是什么？</h1>
		</div>
		
		<div data-role="content">
			<br>
			<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TSpace以打造高质量，高效率的共享空间为宗旨，致力于校园内空间的管理，为在校师生提供了一整套在线预约，审批，现场密码输入的软硬件结合的服务系统。
			高质量代表了我们服务的高质量，以及共享空间的高质量。对所有注册用户，我们会提供客服，解决您的各种问题；每一个开放的共享空间，都会经过精心的装修与布置。
			高效率代表了我们服务的高效率以及用户使用的高效率。对每一位用户的申请，最快在当天即可收到回复；审核通过的申请，会收到相应的密码，用户在申请时间即可通过密码进入空间。
			整个流程简单方便。</p>
			<p style="text-align:center"><img style="width:50%;" class="qcodeclass" src="/tspace/css/usermobile/images/qcode.jpg"/></p>
			<p style="text-align:center">关注上方TSpace官方公众号，即可获取TSpace最新信息。</p>
		</div>
		
	
		<div data-role="footer">
			<h4>Powered By TSpace</h4>
		</div>
	</div>

</body>
</html>