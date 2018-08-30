<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@ page import="java.util.List" %>
<%@ page import="com.tspace.bean.Room" %>
<%@ page import="com.tspace.database.databaseroom" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expire" content="0"/>
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,管理房间">
<meta http-equiv="description" content="TSPACE空间管理系统管理房间">
<title>TSpace空间网管理员系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
<link rel="stylesheet" href="/tspace/css/admin/adminmanageroom.css">
</head>
<body>

	<jsp:include page="adminheadnav.jsp" flush="true"/>
	
	<div class="roommanage">
		<div class="displaymatrix">
			<a href="admininsertroom.jsp"><img class="imgclass" src="/tspace/css/admin/images/addroom.jpg"></a>
		</div>
		<%
		
			// System.out.println("roompage.jsp");
			request.setCharacterEncoding("utf-8");
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select * from tspace.room");
			
			// System.out.println(sql);
			
			List<Room> rooms = databaseroom.get(sql.toString());
			for (int i = 0; i < rooms.size(); i ++) {
				Room room = rooms.get(i);
				out.println("<div class=\"displaymatrix\">");
				out.println("<img class=\"imgclass\" alt=\"\" src=\"" + room.getpictureroom() + "\"/>");
				out.println("<span class=\"textclass\"><strong>房间名:</strong><font>" + room.getnameroom() + "</font></span>");
				out.println("<span class=\"textclass\"><strong>房间地址:</strong><font>" + room.getaddressroom() + "</font></span>");
				out.println("<span class=\"textclass\"><strong>描述:</strong><font>" + room.getdescriptionroom() + "</font></span>");
				out.println("<input style=\"margin:2px\" type=\"button\" id=\"button\" class=\"btn btn-success\" value=\"点击编辑\" onclick='queryId("+  room.getidroom() + ")' ></td>");
				out.println("</div>");
			}
			
		%>
	</div>
	
	<script src="/tspace/js/admin/adminmanageroom.js"></script>

</body>
</html>