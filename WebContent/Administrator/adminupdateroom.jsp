<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.School" %>
<%@ page import="com.tspace.database.databaseschool" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,更新房间">
<meta http-equiv="description" content="TSPACE空间管理系统更新房间">
<title>TSpace空间管理系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
<link rel="stylesheet" href="/tspace/css/admin/admininsertroom.css">
</head>
<body>
	<jsp:include page="adminheadnav.jsp" flush="true"/>
     <form name="form1" id="form1">  
    	<h2>更新房间</h2>
    	<div><input type="text" id="roomid" name="roomid" type="hidden"></div>
    	<select name="school" id="school" class="school" placeholder="学校">
			<option value="0" selected="selected">请选择学校</option>
			<%
				List<School> schools = databaseschool.getAll();
				for (int i = 0; i < schools.size(); i ++) {
					School school = schools.get(i);
			%>
			<option value="<% out.print(school.getidschool()); %>">
				<% out.print(school.getschoolname()); %>
			</option>
			<% } %>
		</select>
        <div><input type="text" id="roomname" name="roomname" placeholder="房间名"></div>  
        <div><input type="text" id="roomaddress" name="roomaddress" placeholder="房间地址"></div>
        <div><input type="text" id="roomcapacity" name="roomcapacity" placeholder="房间容量"></div>
        <select name="roomtype" id="roomtype" class="roomtype" placeholder="房间类型">
			<option value="0" selected="selected">请选择房间类型</option>
			<option value="研讨室">研讨室</option>
			<option value="实验室">实验室</option>
			<option value="活动室">活动室</option>
		</select>
        <div><textarea rows="5" name="roomdescription" id="roomdescription" placeholder="房间描述" style="resize:none;"></textarea></div>
        <div><input type="file" onchange="loadpreview(this)" name="roompicture" id="roompicture"></div>
        <img id="preview" src="" alt="房间图片"></img>  
        <div><input type="button" id="b1" name="b1" value="submit" onclick="fsubmit()"></div>  
    </form>  

    
    <script type="text/javascript">
    
	    var loc = location.href;
		var n1 = loc.length;
		var n2 = loc.indexOf("=");
		// alert("roomid="+loc.substr(n2+1, n1-n2));
		var roomid = decodeURI(loc.substr(n2+1, n1-n2));
		
		$.ajax({
			type: "post",
			dataType: "json",
			url: "/tspace/Servletroom?op=detail",
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify({"idroom":roomid}),
			success: function(data) {
				// alert(data);
				// alert(typeof data);
				document.getElementById("roomid").value = data.idroom;
				// document.getElementById("school").value = data.school;
				document.getElementById("roomname").value = data.nameroom;
				document.getElementById("roomaddress").value = data.addressroom;
				document.getElementById("roomcapacity").value = data.capacityroom;
				document.getElementById("roomtype").value = data.typeroom;
				document.getElementById("roomdescription").value = data.descriptionroom;
				document.getElementById("preview").src = data.pictureroom;
			}
		});
    </script>
    
    <script src="/tspace/js/admin/adminupdateroom.js"></script>

</body>
</html>