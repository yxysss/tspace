<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.Application" %>
<%@ page import="com.tspace.bean.Room" %>
<%@ page import="com.tspace.database.databaseapplication" %>
<%@ page import="com.tspace.database.databaseroom" %>
<%@ page import="com.tspace.listener.TimerTask" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,审核申请">
<meta http-equiv="description" content="TSPACE空间管理系统审核申请">
<title>TSpace空间网管理员系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
<link rel="stylesheet" href="/tspace/css/admin/adminaudit.css">
</head>
<body>

	<jsp:include page="adminheadnav.jsp" flush="true"/>
	
	<table class="table1">
		<thead>
			<tr>
				<td class="tdclass">申请序号</td>
				<td class="tdclass">申请房间</td>
				<td class="tdclass">申请人</td>
				<td class="tdclass">参与人员</td>
				<td class="tdclass">申请理由</td>
				<td class="tdclass">申请时间</td>
				<td class="tdclass">同意</td>
				<td class="tdclass">拒绝</td>
				<td class="tdclass">冲突</td>
			</tr>
		</thead>
		<tbody>
			<%
				String ids = request.getParameter("id");
				Integer id;
				if (ids != null) id = Integer.valueOf(ids);
				else id = -1;
				// out.println("id="+id);
				Date date = new Date();
				String sql = "select * from application where state='unsettle' and starttime>'" + TimerTask.dateToString(date) + "'";
				List<Application> applications = databaseapplication.getsorted(sql.toString());
				Application chosen = null;
				if (id != -1) {
					try {
						chosen = applications.get(id);
					} catch(Exception e) {
						chosen = null;
					}
				}
				String color;
				HashMap<Integer, String> rooms = databaseroom.getMap("select * from tspace.room");
				for (int i = 0; i < applications.size(); i ++) {
					Application apply = applications.get(i);
					color = "white";
					out.println("<tr>");
					if (chosen != null) {
						if (apply.getidroom() == chosen.getidroom()) {
							if (apply.getstarttime().getTime()<chosen.getendtime().getTime()-5*60*1000 && apply.getstarttime().getTime()>chosen.getstarttime().getTime()-5*60*1000) {
								color = "red";
							}
							if (apply.getendtime().getTime()<chosen.getendtime().getTime()+5*60*1000 && apply.getendtime().getTime()>chosen.getstarttime().getTime()+5*60*1000) {
								color = "red";
							}
							if (apply.getstarttime().getTime()<chosen.getstarttime().getTime()-5*60*1000 && apply.getendtime().getTime() > chosen.getendtime().getTime()+5*6*1000) {
								color = "red";
							}
						}
					}
					out.println("<td class=\"tdclass0\" style=\"background:"+color+"\">"+i+"</td>");
					// 房间不存在，说明系统出现故障
					if (rooms.get(apply.getidroom()) == null) continue;
					out.println("<td class=\"tdclass0\">"+apply.getidroom()+"<br/>"+ rooms.get(apply.getidroom()) +"</td>");
					out.println("<td class=\"tdclass0\">"+apply.getapplicant()+"</td>");
					if (apply.getparticipants().length() > 25) {
						out.println("<td class=\"tdclass0\">"+apply.getparticipants().substring(0,25)+"<br>"+apply.getparticipants().substring(25)+"</td>");
					}
					else {
						out.println("<td class=\"tdclass0\">"+apply.getparticipants()+"</td>");
					}
					if (apply.getreason().length() > 25) {
						out.println("<td class=\"tdclass0\">"+apply.getreason().substring(0,25)+"<br>"+apply.getreason().substring(25)+"</td>");
					} else {
						out.println("<td class=\"tdclass0\">"+apply.getreason()+"</td>");
					}
					
					out.println("<td class=\"tdclass0\">"+TimerTask.dateToString(apply.getstarttime())+"<br/>"+TimerTask.dateToString(apply.getendtime())+"</td>");
					out.println("<td class=\"tdclass0\"><input type=\"button\" onclick='acceptapplication(" + apply.getidapplication() + "," + i + ")' class=\"btn btn-success\" value=\"同意\"></td>");
					out.println("<td class=\"tdclass0\"><input type=\"button\" onclick='decline(" + apply.getidapplication() + ","+ i + ")' class=\"btn btn-danger\" value=\"拒绝\"></td>");
					out.println("<td class=\"tdclass0\"><input type=\"button\" onclick='conflict(" + i +")' class=\"btn btn-warning\" value=\"冲突\"></td>");
					out.println("</tr>");
				}
			%>
		</tbody>
	</table>
	
	<script src="/tspace/js/admin/adminaudit.js"></script>

</body>
</html>