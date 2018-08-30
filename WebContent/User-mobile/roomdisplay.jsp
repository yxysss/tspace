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
<!-- 禁用缓存 -->
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<meta http-equiv="keywords" content="TSPACE,房间信息" />
<meta http-equiv="content" content="TSPACE房间信息" />
<title>TSpace空间网</title>
<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
name="viewport" id="viewport"/>
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
		<link type="text/css" rel="stylesheet" href="/tspace/css/usermobile/roomdisplay.css">
		<div data-role="header" data-position="fixed">
			<a href="#"  data-icon="arrow-l" onclick="goback()">返回</a>
			<h1>空间申请</h1>
		</div>
		<div data-role="content">
		 	<div class="roomclass" id="roomdetail">
		 		<img class="imageclass" id="roomimage" src="" alt="roomimage"/>
		 		<h4 id="roomname"></h4>
		 		<p class="fontclass">地址:<span id="roomaddress"></span></p>
		 		<p class="fontclass type">类型:<span id="roomtype"></span></p>
		 		<p class="fontclass capacity">可容纳人数:<span id="roomcapacity"></span></p>
		 	</div>
		 	
		 	<div class="descriptionclass">
		 		<p class="fontclass">描述:<span id="roomdescription"></span></p>
		 	</div>
		 	
		 	<div class="pickclass" data-role="collapsible">
		 		<h1>选择预约时段</h1>
		 		<div data-role="controlgroup" data-type="horizontal">
		 			<a class="cclass" data-role="button" id="date1" onclick="cdate(1)"></a>
		 			<a class="cclass" data-role="button" id="date2" onclick="cdate(2)"></a>
		 			<a class="cclass" data-role="button" id="date3" onclick="cdate(3)"></a>
		 			<a class="cclass" data-role="button" id="date4" onclick="cdate(4)"></a>
		 			<a class="cclass" data-role="button" id="date5" onclick="cdate(5)"></a>
		 			<a class="cclass" data-role="button" id="date6" onclick="cdate(6)"></a>
		 			<a class="cclass" data-role="button" id="date7" onclick="cdate(7)"></a>
		 		</div>
		 		<div data-role="controlgroup" data-type="horizontal">
		 			<%
			 			for (int i = 0; i < 30; i ++) {
							int time = (i+1)/2+7;
					%>
					<a class="aclass" onclick="ctime(<% out.print(i); %>)" data-role="button" id="time<% out.print(i); %>">
						<%
							if (i%2==0) out.print(time + ":30-<br/>" + (time+1) + ":00\n");
							else out.print(time + ":00-<br/>" + time + ":30</td>\n");
						%>
					</a>
					<% } %>
		 		</div>
		 	</div>
		 	<div>
		 		<input placeholder="开始时间" name="starttime" id="starttime" class="starttime" disabled>
		 		<input placeholder="结束时间" name="endtime" id="endtime" class="endtime" disabled>
		 		<textarea placeholder="参与人员(填写帮助管理员审核通过申请，如XX部成员，共8人；国创小组成员，共10人，50字以内)" name="members" id="members" class="members" style="resize:none"></textarea>
		 		<textarea placeholder="申请理由(填写可帮助管理员审核通过申请，如XX部例会；国创小组研讨会，50字以内)" name="reason" id="reason" class="reason" style="resize:none"></textarea>
		 	</div>
		</div>
		<div data-role="footer" data-tap-toggle="false" data-position="fixed">
			<div class="instantclass" onclick="commit()">立即预约</div>
		</div>
		<div data-role="popup" data-dismissible="false" id="popupm1" class="ui-content pop" data-position-to="window">
  			<p>系统异常！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popupm2" class="ui-content pop" data-position-to="window">
  			<p>房间不存在！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup00" class="ui-content pop" data-position-to="window">
  			<p>日期已发生变化，需要刷新页面！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup01" class="ui-content pop" data-position-to="window">
  			<p>请选择使用的时间段！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup02" class="ui-content pop" data-position-to="window">
  			<p>参与人员字段长度超过50字！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup03" class="ui-content pop" data-position-to="window">
  			<p>申请理由字段长度超过50字！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup1" class="ui-content pop" data-position-to="window">
  			<p>亲，请登录！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup2" class="ui-content pop" data-position-to="window">
  			<p>申请提交失败！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup3" class="ui-content pop" data-position-to="window">
  			<p>您在当天已有其他申请，同一天不可多次申请，请到我的申请中查看！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup4" class="ui-content pop" data-position-to="window">
  			<p>您申请的时间段已管理员批准给其他人，请重新选择时间段！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popup9" class="ui-content pop" data-position-to="window">
  			<p>系统将在每日00:00到00:05分时间段内进行更新，请稍后再试！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		<div data-role="popup" data-dismissible="false" id="popuplogin" class="ui-content pop" data-position-to="window">
			
		</div>
		<div data-role="popup" data-dismissible="false" id="commit" class="ui-content" data-position-to="window">
  			<h3 class="commithead">申请确认</h3>
  			<div class="pop">
	  			<p class="commitfont">申请房间:<span id="croomname"></span></p>
	  			<p class="commitfont">开始时间:<span id="cstarttime"></span></p>
	  			<p class="commitfont">结束时间:<span id="cendtime"></span></p>
	  			<div style="word-wrap:break-word;">
	  				<p class="commitfont">参与人员:<br/><span id="cmembers"></span></p>
	  			</div>
	  			<div style="word-wrap:break-word;">
	  				<p class="commitfont">申请理由:<br/><span id="creason"></span></p>
	  			</div>   			
	  			<a href="#" class="ui-btn ui-corner-all" data-rel="back" onclick="submit()">确定</a>
	  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">稍后再试</a>
  			</div>
		</div>
		<script>
			/* 获取生成页面时的日期 */
			var date0 = new Date();
			var day0 = date0.getDate();
			var chosen = 0;
			var pick1 = 0;
			var starttime1 = "";
			var endtime1 = "";
			var pick2 = 0;
			var starttime2 = "";
			var endtime2 = "";
			var redate = 1;
			var date = new Date();
			var month, day;
			for (var i = 1; i <= 7; i ++) {
				date.setDate(date.getDate() + 1);
				month = date.getMonth()+1;
				day = date.getDate();
				document.getElementById("date"+i).innerText = month+"-"+day;
			}
			var loc = location.href;
			var n1 = loc.length;
			var n2 = loc.indexOf("=");
			// alert("idroom="+loc.substr(n2+1, n1-n2));
			var idroom = decodeURI(loc.substr(n2+1, n1-n2));
			var availableroom = "";
			var roomname;
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/Servletroom?op=detail",
				contentType: "application/json;charset=utf-8",
				data: JSON.stringify({"idroom":idroom}),
				success: function(data) {
					// alert("success");
					if (data == -2) {
						// alert("系统异常");
						$("#popupm1").popup('open');
						return ;
					}
					if (data == -3) {
						$("#popupm2").popup('open');
						return ;
					}
					// alert("获取信息成功");
					roomname = data.nameroom;
					document.getElementById('roomname').innerHTML=data.nameroom;
					document.getElementById('roomaddress').innerHTML=data.addressroom;
					document.getElementById('roomcapacity').innerHTML=data.capacityroom;
					document.getElementById('roomtype').innerHTML=data.typeroom;
					document.getElementById('roomimage').src=data.pictureroom;
					availableroom = data.availableroom;
					// alert(availableroom);
					document.getElementById('roomdescription').innerHTML=data.descriptionroom;
					var datex = document.getElementById("date1");
					datex.style.background="#00ffff";
					for (var i = 0; i < 30; i ++) {
						var text = document.getElementById("time"+i);
						if (availableroom[i] == '1') {
							text.style.backgroundColor="#ff6a6a";
						} else {
							text.style.backgroundColor="#00ff40";
						}
					}
				}
			});
		</script>
		<script src="/tspace/js/usermobile/roomdisplay.js"></script>
	</div>
</body>
</html>