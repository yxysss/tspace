<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.tspace.bean.User" %>
<%@ page import="com.tspace.database.databaseuser" %>
<%@ page import="com.tspace.database.databaseschool" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expire" content="0"/>
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,管理用户">
<meta http-equiv="description" content="TSPACE空间管理系统管理用户">
<title>TSpace空间网管理员系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
<link rel="stylesheet" href="/tspace/css/admin/adminmanageuser.css">
</head>
<body>

	<jsp:include page="adminheadnav.jsp" flush="true"/>
	<div style="float:right;width:400px;height:30px;margin-right:50px;margin-top:20px" class="form-group">
		<input style="width:270px;height:30px" type="text" id="searchuser" name="searchuser" class="searchuser" placeholder="输入要搜索的学号/工号">
		<input style="float:right;width:60px;height:30px" type="button" id="search" class="btn btn-success" value="搜索">
	</div>
	<table class="table1">
		<thead>
			<tr>
				<td class="tdclass">用户序号</td>
				<td class="tdclass">学号/工号</td>
				<td class="tdclass">姓名</td>
				<td class="tdclass">身份</td>
				<td class="tdclass">手机</td>
				<td class="tdclass">学校</td>
				<td class="tdclass">删除</td>
			</tr>
		</thead>
		<tbody>
			<%
				String username = request.getParameter("username");
				String sql;
				if (username == null) {
					sql = "select * from tspace.user";
				} else {
					sql = "select * from tspace.user where username LIKE '%"+username+"%'";
				}
				List<User> users = databaseuser.get(sql);
				HashMap<Integer, String> schools = databaseschool.getMap("select * from tspace.school");
				if (users == null) return ;
				for (int i = 0; i < users.size(); i ++) {
					User user = users.get(i);
					out.println("<tr><td class=\"tdclass0\">"+i+"</td>");
					out.println("<td class=\"tdclass0\">"+user.getusername()+"</td>");
					out.println("<td class=\"tdclass0\">"+user.getname()+"</td>");
					String identity = "学生";
					if (user.getidentity() == 1) identity = "老师";
					out.println("<td class=\"tdclass0\">"+identity+"</td>");
					out.println("<td class=\"tdclass0\">"+user.getmobile()+"</td>");
					// 学校不存在，说明系统故障
					if (schools.get(user.getschool()) == null) continue;
					out.println("<td class=\"tdclass0\">"+schools.get(user.getschool())+"</td>");
					out.println("<td class=\"tdclass0\"><input type=\"button\" onclick='deleteuser(" + user.getusername() + ")' class=\"btn btn-danger\" value=\"删除\"></td></tr>");
				}
			 %>
		</tbody>
		
	</table>
	
	<script src="/tspace/js/admin/adminmanageuser.js"></script>

</body>
</html>