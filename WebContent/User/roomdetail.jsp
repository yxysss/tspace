<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ page import="java.text.SimpleDateFormat"%>
 <%@ page import="java.text.DateFormat"%>
 <%@ page import="java.util.Date" %>
 <%@ page import="java.util.Calendar" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<title>Transform空间管理系统</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/roomdetailcss.css" type="text/css">
<link rel="stylesheet" href="css/divcss.css" type="text/css">
</head>
<body> 


	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	
	<jsp:include page="headnav.jsp" flush="true"/>
	
	<br/>
	
	<script type="text/javascript">
	
		var date0 = new Date();
		var day0 = date0.getDate();
	</script>
	
	
	<table class="table table-bordered table-hover">
		<thead>
			<tr>
				<td>房间编号</td>
				<td>房间地址</td>
				<td>可容纳人数</td>
				<td>房间图片</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><div id="idroom0"></div></td>
				<td><div id="addressroom0"></div></td>
				<td><div id="capacityroom0"></div></td>
				<td><img id="pictureroom0" width="200px" height="100px"></img></td>
			</tr>
		</tbody>
	</table>
	
	<div class="roomdescription">
		<div class="description">
			<strong>房间编号:</strong>
			<span id="idroom"></span>
		</div><br/>
		<div class="description">
			<strong>房间地址:</strong>
			<span id="addressroom"></span>
		</div><br/>
		<div class="description">
			<strong>可容纳人数:</strong>
			<span id="capacityroom"></span>
		</div>
		<img id="pictureroom" width="50%" class="pictureroom"></img>
	</div>
	
	<div class="roomtable">
	
		<h2>之后一周的教室安排</h2>
		
		<table>
			<thead>
				<tr>
					<td>   </td>
					<%
						Calendar calendar = Calendar.getInstance();
						StringBuilder builder0 = new StringBuilder();
						for (int i = 0; i < 7; i ++) {
							calendar.add(Calendar.DATE, 1);
							builder0.append("<td>");
							builder0.append((1+calendar.get(Calendar.MONTH))+"月");
							builder0.append(calendar.get(Calendar.DAY_OF_MONTH)+"日");
							builder0.append("</td>");
							out.print(builder0.toString());
							builder0.delete(0, builder0.length());
						}
					%>
				</tr>
			</thead>
			<tbody>
				<%
					for (int i = 0; i < 31; i ++) {
						int time = (i+1)/2+6;
						out.print("<tr>\n");
						out.print("<td>");
						if (i%2==0) out.print(time + ":30-" + (time+1) + ":00</td>\n");
						else out.print(time + ":00-" + time + ":30</td>\n");
						for (int j = 0; j < 7; j ++) {
							StringBuilder builder = new StringBuilder();
							builder.append((j+1)+"");
							if (i < 9) builder.append("0" + (i+1));
							else builder.append((i+1)+"");
							out.print("<td><div id=\"" + builder.toString() + "\" onclick='choose(\"" + builder.toString());
							out.print("\")'></div></td>\n");
						}
						out.print("</tr>\n");
					}
				%>
				<!--
				<tr>
					<td>6:30-7:00</td>
					<td><div id="101" onclick='choose("101")'></div></td>
					<td><div id="201" onclick='choose("201")'></div></td>
					<td><div id="301" onclick='choose("301")'></div></td>
					<td><div id="401" onclick='choose("401")'></div></td>
					<td><div id="501" onclick='choose("501")'></div></td>
					<td><div id="601" onclick='choose("601")'></div></td>
					<td><div id="701" onclick='choose("701")'></div></td>
				</tr>
				-->
			</tbody>
		</table>
	</div>
	<div class="applyroom">
		<h2>房间申请</h2>
		<div class="applyinput">
			<div>
				<span class="input-group-addon">申请开始时间</span>
				<input type="text" name="starttime" class="starttime" disabled/><br/>
			</div>
			<div>
				<span class="input-group-addon">申请结束时间</span>
				<input type="text" name="endtime" class="endtime" disabled/><br/>
			</div>
			<div>
				<span class="input-group-addon">申请理由</span>
				<textarea rows="3" cols="50" id="reason" name="reason" class="reason"></textarea>
			</div>
			<div>
				<span class="input-group-addon">参与人员</span>
				<textarea rows="3" cols="50" id="participants" name="participants" class="participants"></textarea>
			</div>
			<div>
				<input type="button" id="button" class="btn btn-success" onclick='submit()' value="提交"/><br/>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		var chosen = 0;
		var pick1 = 0;
		var starttime1 = "";
		var endtime1 = "";
		var pick2 = 0;
		var starttime2 = "";
		var endtime2 = "";
		var loc = location.href;
		var n1 = loc.length;
		var n2 = loc.indexOf("=");
		alert("idroom="+loc.substr(n2+1, n1-n2));
		var idroom = decodeURI(loc.substr(n2+1, n1-n2));
		var availableroom = "";
		$.ajax({
			type: "post",
			dataType: "json",
			url: "Servletroom?op=detail",
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify({"idroom":idroom}),
			success: function(data) {
				alert("success");
				if (data == false) {
					alert("获取信息失败");
				} else {
					alert("获取信息成功");
					alert(data.addressroom);
					document.getElementById('idroom').innerHTML=data.idroom;
					document.getElementById('addressroom').innerHTML=data.addressroom;
					document.getElementById('capacityroom').innerHTML=data.capacityroom;
					document.getElementById('pictureroom').src=data.pictureroom;
					availableroom = data.availableroom;
					alert(availableroom.length);
					for (var i = 0; i < availableroom.length; i ++) {
						var day = parseInt(i / 31 + 1);
						var num = i % 31 + 1; 
						var id;
						if (num < 10) id = "" + day + "0" + num;
						else id = "" + day + "" + num;
						var text = document.getElementById(id);
						if (availableroom[i] == '1') {
							text.innerHTML = "占用";
							text.style.backgroundColor="red";
						} else {
							text.innerHTML = "空闲";
							text.style.backgroundColor="green";
						}
					}
				}
			}
		});
	</script>
		
	<script type="text/javascript">
	
		function submit() {
			var date1 = new Date();
			var day1 = date1.getDate();
			if (day1 != day0) {
				alert("晚上12点已过，日期已发生变化，请重新选择时间段进行申请");
				location.reload();
			} else {
				if (chosen == 0) {
					alert("请选择使用的时间段");
					return ;
				}
				var starttime = 0;
				var endtime = 0;
				if (chosen == 1) {
					starttime = endtime = pick1+pick2;
				} else {
					starttime = pick1<pick2?pick1:pick2;
					endtime = pick1>pick2?pick1:pick2;
				}			
				var reason = document.getElementById("reason").value+"";
				var participants = document.getElementById("participants").value+"";
				$.ajax({
					type: "post",
					dataType: "json",
					url: "Servletapplication?op=submit",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"idroom":idroom,"starttime":starttime,"endtime":endtime,"reason":reason,"participants":participants}),
					success: function(data) {
						if (data == -1) {
							alert("亲，请登录");
							window.open("userlogin.jsp");
						} else {
							if (data == -2) {
								alert("申请提交出现错误！");
							} else {
								if (data == -3) {
									alert("您在当天已有申请，请到我的申请中查看。");									
								} else {
									alert("提交成功，您的申请已提交到管理员处审核，审核结果将以短信方式通知您，请密切留意您的手机");
								}
							}
						}
					}
				});
			}
		}
		
		function choose(id) {
			// 取得时间段的起止时间
			var date2 = new Date();
			var day0 = parseInt(id/100);
			date2.setDate(date2.getDate()+day0);
			var time0 = parseInt(id%100);
			var hour0 = parseInt(time0/2)+6;
			var displaystart = (date2.getMonth()+1).toString()+"月"+(date2.getDate()).toString()+"日 "+hour0.toString()+"时";
			if (time0%2 == 0) displaystart = displaystart + "00分";
			else displaystart = displaystart + "30分";
			var hour1 = parseInt((time0-1)/2)+7;
			var displayend = (date2.getMonth()+1).toString()+"月"+(date2.getDate()).toString()+"日 "+hour1.toString()+"时";
			if (time0%2 == 0) displayend = displayend + "30分";
			else displayend = displayend + "00分";
			var textclick = document.getElementById(id);
			if (textclick.style.backgroundColor == "red") {
				return ;
			}
			if (chosen == 0) { // 之前是0选中的情况
				chosen = 1;
				pick1 = id;
				starttime1 = displaystart;
				endtime1 = displayend;
				$("input[name=starttime]").val(displaystart);
				$("input[name=endtime]").val(displayend);
				textclick.innerHTML = "选中";
				textclick.style.backgroundColor = "yellow";
				textclick.onmouseover = function () {
					if (textclick.style.backgroundColor == "yellow") {
						textclick.innerHTML = "取消";
					}
				};
				textclick.onmouseout = function () {
					if (textclick.style.backgroundColor == "yellow") {
						textclick.innerHTML = "选中";
					}
				};
			} else {
				if (chosen == 1) { // 之前选中了一个时间段
					if (pick1 != 0) { // 选中了pick1
						if (pick1 != id) { // 新选中的不是之前选中的
							if (parseInt(pick1/100) != parseInt(id/100)) { // 判断是否在一天
								return ;
							}
							if (checkschedule(pick1, id) == false) { // 判断是否是一个空闲的连续时间段
								return ;
							}
							chosen = 2;
							pick2 = id;
							starttime2 = displaystart;
							endtime2 = displayend;
							if (pick1>pick2) {
								$("input[name=starttime]").val(displaystart);
							} else {
								$("input[name=endtime]").val(displayend);
							}
							textclick.innerHTML = "选中";
							textclick.style.backgroundColor = "yellow";
							textclick.onmouseover = function () {
								if (textclick.style.backgroundColor == "yellow") {
									textclick.innerHTML = "取消";
								}
							};
							textclick.onmouseout = function () {
								if (textclick.style.backgroundColor == "yellow") {
									textclick.innerHTML = "选中";
								}
							};
						} else { // 否则取消之前选中的
							chosen = 0;
							pick1 = 0;
							pick2 = 0;
							$("input[name=starttime]").val("");
							$("input[name=endtime]").val("");
							textclick.innerHTML = "空闲";
							textclick.style.backgroundColor = "green";
						}
						
					} else {
						if (pick2 != id) { // pick2同理
							if (parseInt(pick2/100) != parseInt(id/100)) {
								return ;
							}
							if (checkschedule(pick2, id) == false) { // 判断是否是一个空闲的连续时间段
								return ;
							}
							chosen = 2;
							pick1 = id;
							starttime1 = displaystart;
							endtime1 = displayend;
							if (pick2>pick1) {
								$("input[name=starttime]").val(displaystart);
							} else {
								$("input[name=endtime]").val(displayend);
							}
							textclick.innerHTML = "选中";
							textclick.style.backgroundColor = "yellow";
							textclick.onmouseover = function () {
								if (textclick.style.backgroundColor == "yellow") {
									textclick.innerHTML = "取消";
								}
							};
							textclick.onmouseout = function () {
								if (textclick.style.backgroundColor == "yellow") {
									textclick.innerHTML = "选中";
								}
							};
						} else {
							chosen = 0;
							pick1 = 0;
							pick2 = 0;
							$("input[name=starttime]").val("");
							$("input[name=endtime]").val("");
							textclick.innerHTML = "空闲";
							textclick.style.backgroundColor = "green";
						}
					}
				} else { // 之前选中了两个时间段
					if (pick1 == id || pick2 == id) {
						textclick.innerHTML = "空闲";
						textclick.style.backgroundColor = "green";
						if (pick1 == id) {
							pick1 = 0;
							$("input[name=starttime]").val(starttime2);
							$("input[name=endtime]").val(endtime2);
						}
						else {
							pick2 = 0;
							$("input[name=starttime]").val(starttime1);
							$("input[name=endtime]").val(endtime1);
						}
						chosen = 1;
					}
				}
			}
		}
		
		function checkschedule(a, b) {
			var sstart = a<b?a:b;
			var eend = a>b?a:b;
			var i = 0;
			for (i = sstart; i <= eend; i ++) {
				var aspan = document.getElementById(i.toString());
				if (aspan.style.backgroundColor == "red") return false;
			}
			return true;
		}
	</script>
</body>
</html>