<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.Application"  %>
<%@ page import="com.tspace.database.databaseapplication"  %>
<%@ page import="com.tspace.bean.Room"  %>
<%@ page import="com.tspace.database.databaseroom"  %>
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
<!-- 禁用缓存 -->
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<meta http-equiv="keywords" content="TSPACE,我的申请" />
<meta http-equiv="content" content="TSPACE我的申请" />
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

	<div data-role="page" id="myapplication-running">
		<div data-role="popup" data-dismissible="false" id="popupm1" class="ui-content pop" data-position-to="window">
  			<p>系统异常！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="detail" class="ui-content" data-position-to="window">
  			<h3 class="detailhead">申请详情</h3>
  			<div class="pop">
	  			<p class="detailfont">状态:<span id="cstate"></span></p>
	  			<p class="detailfont">申请时间:<span id="capplytime"></span></p>
	  			<p class="detailfont">审批时间:<span id="caudittime"></span></p>
	  			<p class="detailfont">开门密码:<span id="cpassword"></span></p>   			
	  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
  			</div>
		</div>
		<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/myapplication.css">
		<div data-role="header">
			<div data-role="navbar" class="navbarclass">
				<ul>
					<li><a id="running0">进行中</a></li>
					<li><a id="past0" href="#myapplication-past">历史申请</a></li>		
				</ul>
			</div>
		</div>
		<div data-role="content">
			<ul data-role="listview">
				<%
					String username = (String)session.getAttribute("username");
				
					if (username == null) {
						out.print("<li>暂无申请</li>");
					} else {
						List<Application> applications = databaseapplication.getRunning(username);
						if (applications == null) {
							out.print("<li>系统异常</li>");
						} else {
							if (applications.size() == 0) {
								out.print("<li>暂无申请</li>");
							} else {
								for (int i = 0; i < applications.size(); i ++) {
									Application apply = applications.get(i);
									int idroom = apply.getidroom();
									Room room = databaseroom.getroom(idroom);
				%>
				<li>
					<a onclick="queryapplication0(<% out.print(apply.getidapplication()); %>)">
						<img class="imageclass" src="<% out.print(room.getpictureroom()); %>" alt="roomimage"/>
						<h4><% out.print(room.getnameroom()); %></h4>
						<p>开始时间:<% out.print(TimerTask.dateToString(apply.getstarttime())); %></p>
						<p>结束时间:<% out.print(TimerTask.dateToString(apply.getendtime())); %></p>
						<p>状态:<span class="<%
							if ("accept".equals(apply.getstate())) {
								out.print("acceptclass");
							}
							if ("decline".equals(apply.getstate())) {
								out.print("declineclass");
							}
							if ("unsettle".equals(apply.getstate())) {
								out.print("unsettleclass");
							}
						%>"><% 
							if ("accept".equals(apply.getstate())) {
								out.print("接受");
							}
							if ("decline".equals(apply.getstate())) {
								out.print("拒绝");
							}
							if ("unsettle".equals(apply.getstate())) {
								out.print("待审核");
							} 
						%></span></p>
						
						<%
							if ("unsettle".equals(apply.getstate())) {
								out.print("<a onclick=\"commitcancel(" + apply.getidapplication() + ")\" style=\"background:red !important;border:0 !important;\" data-role=\"button\" data-icon=\"delete\"></a>");
							}
						%>
					</a>
				</li>
				<% } } } }%>
			</ul>
			
		</div>
		<div data-role="footer" data-tap-toggle="false" data-theme="b" data-position="fixed">
			<div data-role="navbar">
				<ul>
					<li><a id="nav00" href="/tspace/User-mobile/index.jsp" data-icon="home">首页</a></li>
					<li><a id="nav01" data-icon="grid">我的申请</a></li>
					<li><a id="nav02" href="/tspace/User-mobile/mytspace.jsp" data-icon="user">我的TSpace</a></li>
				</ul>
			</div>
		</div>
		<div data-role="popup" data-dismissible="false" id="ccancel" class="ui-content pop" data-position-to="window">
  			<p>确定取消这个申请吗？</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back" onclick="submitcancel()">确定</a>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">点错了</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="cancelfail" class="ui-content pop" data-position-to="window">
  			<p>取消申请失败！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back" onclick="reloadwindow()">确定</a>
		</div>
		<script src="/tspace/js/usermobile/myapplication0.js"></script>
	</div>
	
	<div data-role="page" id="myapplication-past">
		<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/myapplication.css">
		<div data-role="header">
			<div data-role="navbar">
				<ul>
					<li><a id="running1" href="/tspace/User-mobile/myapplication.jsp" data-ajax="false">进行中</a></li>
					<li><a id="past1">历史申请</a></li>		
				</ul>
			</div>
		</div>
		<div data-role="content">
			<ul data-role="listview">
				<%					
					String pusername = (String)session.getAttribute("username");
				
					if (pusername == null) {
						out.print("<li>暂无申请</li>");
					} else {
						List<Application> papplications = databaseapplication.getPast(pusername);
						if (papplications == null) {
							out.print("<li>系统异常</li>");
						} else {
							if (papplications.size() == 0) {
								out.print("<li>暂无申请</li>");
							} else {
								for (int i = 0; i < papplications.size(); i ++) {
									Application apply = papplications.get(i);
									int idroom = apply.getidroom();
									Room room = databaseroom.getroom(idroom);
				%>
				<li>
					<a>
						<img class="imageclass" src="<% out.print(room.getpictureroom()); %>" alt="roomimage"/>
						<h4><% out.print(room.getnameroom()); %></h4>
						<p>开始时间:<% out.print(TimerTask.dateToString(apply.getstarttime())); %></p>
						<p>结束时间:<% out.print(TimerTask.dateToString(apply.getendtime())); %></p>
						<p>状态:<span class="<%
							if ("accept".equals(apply.getstate())) {
								out.print("acceptclass");
							}
							if ("decline".equals(apply.getstate())) {
								out.print("declineclass");
							}
							if ("unsettle".equals(apply.getstate())) {
								out.print("unsettleclass");
							}
							if ("cancel".equals(apply.getstate())) {
								out.print("cancelclass");
							}
						%>"><% 
							if ("accept".equals(apply.getstate())) {
								out.print("接受");
							}
							if ("decline".equals(apply.getstate())) {
								out.print("拒绝");
							}
							if ("unsettle".equals(apply.getstate())) {
								out.print("待审核");
							} 
							if ("cancel".equals(apply.getstate())) {
								out.print("已取消");
							}
						%></span></p>
					</a>
				</li>
				<% } } } }%>
			</ul>
		</div>
		<div data-role="footer" data-tap-toggle="false" data-theme="b" data-position="fixed">
			<div data-role="navbar">
				<ul>
					<li><a id="nav10" href="/tspace/User-mobile/index.jsp" data-icon="home"">首页</a></li>
					<li><a id="nav11" data-icon="grid">我的申请</a></li>
					<li><a id="nav12" href="/tspace/User-mobile/mytspace.jsp" data-icon="user">我的TSpace</a></li>
				</ul>
			</div>
		</div>
		<script src="/tspace/js/usermobile/myapplication1.js"></script>
	</div>

</body>
</html>