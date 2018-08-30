<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="com.tspace.bean.Room" %>
<%@ page import="com.tspace.database.databaseroom" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="net.sf.json.JSONObject" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="Font-Awesome-3.2.1/css/font-awesome.min.css">
<link rel="stylesheet" href="css/divcss.css">
</head>
<body style="margin:10px;">

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	
	<script type="text/javascript">
	
		$.ajax({
			type: "post",
			dataType: "json",
			url: "checklogin",
			contentType: "application/json;charset=utf-8",
			success: function(data) {
				if (data == false) {
					top.location.href="login.jsp";
				}
			}
		});
	</script>
	
	<span class="glyphicon glyphicon-search"></span>
	<span class="glyphicon glyphicon-plus"></span>
	
	<i class="icon-hospital"></i>
	
	<table class="table table-bordered table-hover">
		<thead>
			<tr>
				<td>房间编号</td>
				<td>房间地址</td>
				<td>可容纳人数</td>
				<td>房间图片</td>
				<td></td> 
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
					//out.print("<td>" + room.getcapacityroom() + "</td>");
					out.print("<td> <img src=\"" + room.getpictureroom() + "\" + alt=\"\" width=\"200px\" height=\"100px\"/> </td>");
					out.println("<td><input type=\"button\" id=\"button\" class=\"btn btn-danger\" value=\"详情\" onclick='queryId("+  room.getidroom() + ")' ></td>");
					out.println("</tr>");
				}
			%>
		</tbody>
	</table>
	
	<script type="text/javascript">
	
		function queryId(id){
			
			window.open("roomdetail.jsp?idroom=" + encodeURI(id));
			
		}
		
	</script>
	

</body>
</html>