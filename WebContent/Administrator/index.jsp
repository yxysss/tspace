<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="com.tspace.database.databaseuser" %>
<%@ page import="com.tspace.database.databaseroom" %>
<%@ page import="com.tspace.database.databaseapplication" %>
<%@ page import="com.tspace.listener.TimerTask" %>
<%
	String adminid = (String) session.getAttribute("adminid");
	if (adminid == null) {
		response.sendRedirect("adminlogin.jsp");
		return ;
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,主页">
<meta http-equiv="description" content="TSPACE空间管理系统主页">
<title>TSpace空间网管理员系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
<link rel="stylesheet" href="/tspace/css/admin/adminindex.css">
</head>
<body class="bodyclass">

	<jsp:include page="adminheadnav.jsp" flush="true"/>
	
	<div class="divclass">
		<img class="imageclass" src="/tspace/css/admin/images/adminaudit.png"></img>
		<h1 class="hclass">
		<%
			Date date = new Date();
			String sql = "select count(*) from application where state='unsettle' and starttime>'" + TimerTask.dateToString(date) + "'";
			Integer count = databaseapplication.getCount(sql);
			if (count == null) {
				out.print("系统异常");
			} else {
				out.print(count);
			}
		%>
		</h1>
		<p class="pclass">待审核申请</p>
	</div>
	
	<div class="divclass">
		<img class="imageclass" src="/tspace/css/admin/images/adminroom.png"></img>
		<h1 class="hclass">
		<%
			sql = "select count(*) from room";
			count = databaseroom.getCount(sql);
			if (count == null) {
				out.print("系统异常");
			} else {
				out.print(count);
			}
		%>
		</h1>
		<p class="pclass">已加入房间</p>
	</div>
	
	<div class="divclass">
		<img class="imageclass" src="/tspace/css/admin/images/adminuser.png"></img>
		<h1 class="hclass">
		<%
			sql = "select count(*) from user";
			count = databaseuser.getCount(sql);
			if (count == null) {
				out.print("系统异常");
			} else {
				out.print(count);
			}
		%>
		</h1>
		<p class="pclass">注册用户</p>
	</div>

</body>
</html>