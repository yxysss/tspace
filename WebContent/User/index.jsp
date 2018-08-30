<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.Room" %>
<%@ page import="com.tspace.database.databaseroom" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<title>Transform空间管理系统</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/divcss.css" type="text/css">
</head>
<body>
	<!-- javascript -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	
	<jsp:include page="headnav.jsp" flush="true"/>
	
	<div>
		<h1>Transform空间网</h1>
	</div>
	
	<div class="container">
	
		<div class="divclass">
			<img src="http://pic.58pic.com/58pic/15/63/06/40258PICArf_1024.jpg" alt="" class="imgclass"/>
			<strong class="textclass">房间名:</strong><font>B-406</font><br/>
			<strong class="textclass">房间地址:</strong><font>B楼406</font><br/>
			<strong class="textclass">描述:</strong><font>一件可供团队开会的研讨室</font><br/>
			<input style="margin:2px" type="button" id="button" class="btn btn-success" value="点击预约">
		</div>
		
		<%
		
			System.out.println("roompage.jsp");
			request.setCharacterEncoding("utf-8");
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select * from transform.room");
			
			System.out.println(sql);
			
			List<Room> rooms = databaseroom.get(sql.toString());
			for (int i = 0; i < rooms.size(); i ++) {
				Room room = rooms.get(i);
				out.println("<div class=\"divclass\">");
				out.println("<img class=\"imgclass\" alt=\"\" src=\"" + room.getpictureroom() + "\"/>");
				out.println("<strong class=\"textclass\">房间名:</strong><font>" + room.getnameroom() + "</font><br/>");
				out.println("<strong class=\"textclass\">房间地址:</strong><font>" + room.getaddressroom() + "</font><br/>");
				out.println("<strong class=\"textclass\">描述:</strong><font>" + room.getdescriptionroom() + "</font><br/>");
				out.println("<input style=\"margin:2px\" type=\"button\" id=\"button\" class=\"btn btn-success\" value=\"点击预约\" onclick='queryId("+  room.getidroom() + ")' ></td>");
				out.println("</div>");
			}
			
		%>
	
	</div>
	
	
	<%-- <table class="table table-bordered table-hover">
		<thead>
			<tr>
				<td>房间编号</td>
				<td>房间地址</td>
				<td>可容纳人数</td>
				<td>房间图片</td>
				<td></td> 
			</tr>
		</thead>
		<tbody>
			<!-- <td><input type="button" id="button3" class="btn btn-danger" value="删除"></td>
			<td><input type="button" id="modify3" class="btn btn-success" value="更新"></td> -->
			<%	
				System.out.println("roompage.jsp");
				request.setCharacterEncoding("utf-8");
				String id = request.getParameter("id");
			
				System.out.println(id);
				
				StringBuilder sql = new StringBuilder();
				
				sql.append("select * from transform.room");
				
				System.out.println(sql);
				
				List<Room> rooms = databaseroom.get(sql.toString());
				for (int i = 0; i < rooms.size(); i ++) {
					Room room = rooms.get(i);
					out.print("<tr>");
					out.println("<td>" + room.getidroom() + "</td>");
					out.println("<td>" + room.getaddressroom() + "</td>");
					out.print("<td>" + room.getcapacityroom() + "</td>");
					out.print("<td> <img src=\"" + room.getpictureroom() + "\" + alt=\"\" width=\"200px\" height=\"100px\"/> </td>");
					out.println("<td><input type=\"button\" id=\"button\" class=\"btn btn-danger\" value=\"详情\" onclick='queryId("+  room.getidroom() + ")' ></td>");
					out.println("</tr>");
				}
			%>
		</tbody>
	</table> --%>
	
	<script type="text/javascript">
	
		function queryId(id){
			
			window.open("roomdetail.jsp?idroom=" + encodeURI(id));
			
		}
		
	</script>
</body>
</html>