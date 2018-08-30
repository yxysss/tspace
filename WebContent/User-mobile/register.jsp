<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.tspace.bean.School" %>
<%@ page import="com.tspace.database.databaseschool" %>
<%@ page import="java.util.List" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>TSpace空间网</title>
<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
name="viewport" id="viewport"/>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<meta http-equiv="keywords" content="TSPACE,注册" />
<meta http-equiv="content" content="TSPACE注册" />
<script src="js/jquery.min.js"></script>
<script>
	$(document).bind("mobileinit", function() {
	    //disable ajax nav
	    $.mobile.ajaxEnabled=false;
	});
</script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
</head>
<body>

	<div data-role="page">
	
		<script src="js/code.js"></script>
		<link type="text/css" rel="stylesheet" href="css/register.css">
		
		<script>
			$.ajax({
				type: "post",
				dataType: "json",
				url: "../mServletuser?op=checkSmstime",
				contentType: "application/json;charset=utf-8",
				success: function(data) {
					// alert(data);
					// alert(typeof data);
					if (data == 0) {
						var codebtn = document.getElementById("codebtn");
						codebtn.text="点击获取短信验证码";
						codebtn.setAttribute("onclick","getcode()");
						
					} else {
						var codebtn = document.getElementById("codebtn");
						codebtn.text=data+"秒后重新获取";
						codebtn.removeAttribute("onclick");
						var timer = setInterval(function() {
							data -= 1;
							if (data == 0) {
								clearInterval(timer);
								codebtn.text="点击获取短信验证码";
								codebtn.setAttribute("onclick","getcode()");
							} else {
								codebtn.text=data+"秒后重新获取";
							}
						}, 1000);
						return false;
					}
				}
			});
			
			function getcode() {
				var data = 60;
				codebtn.text=data+"秒后重新获取";
				codebtn.removeAttribute("onclick");
				var timer = setInterval(function() {
					data -= 1;
					if (data == 0) {
						clearInterval(timer);
						codebtn.text="点击获取短信验证码";
						codebtn.setAttribute("onclick","getcode()");
					} else {
						codebtn.text=data+"秒后重新获取";
					}
				}, 1000);
				var mobile = $("input[name=mobile]").val();
				$.ajax({
					type: "post",
					dataType: "json",
					url: "../mServletuser?op=getSmsVerify",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"mobile":mobile}),
					success: function(data) {
						// alert(data);
						// alert(typeof data);
						if (data == 0) {
							$("#popup1").popup('open');
							return ;
						} else {
							$("#popup2").popup('open');
							return false;
						}
					}
				});
				
			}
		</script>
		
		<div data-role="popup" data-dismissible="false" id="popup1" class="ui-content" data-position-to="window">
  			<p>短信发送成功，验证有效时间3分钟！</p>
  			<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="popup" data-dismissible="false" id="popup2" class="ui-content" data-position-to="window">
  				<p>短信发送失败，请检查手机号是否正确！</p>
  				<a href="#" class="ui-btn ui-corner-all" data-rel="back">确定</a>
		</div>
		
		<div data-role="header">
			<h1>TSpace用户注册</h1>
		</div>
		
		<div data-role="content">
			<p name="notice" id="notice" class="notice"></p>
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
			<input type="text" name="studentid" id="studentid" class="studentid" placeholder="学号">
			<input type="text" name="identification" id="indentificatioa" class="identification" placeholder="身份证">
			<input type="text" name="name" id="name" class="name" placeholder="姓名">
			<input type="password" name="password" id="password" class="password" placeholder="密码" onBlur="pwblur()" onFocus="pwfocus()">
			<p name="pwnotice" id="pwnotice" class="pwnotice"></p>
			<input type="password" name="confirmpassword" id="confirmpassword" class="confirmpassword" placeholder="确认密码">
			<input type="text" name="mobile" id="mobile" class="mobile" placeholder="手机">
			<div class="codeclass">
				<input type="text" name="code" id="code" class="code" placeholder="验证码"/>
				<a onclick="getcode()" data-role="button" name="codebtn" id="codebtn" class="codebtn"></a>
			</div>			
			<a data-role="button" name="register" id="register" class="register">注册</a>
		</div>
		<script type="text/javascript">
		
			function pwblur() {
				var pwnotice = document.getElementById("pwnotice");
				pwnotice.innerText="";
			}
			
			function pwfocus() {
				var pwnotice = document.getElementById("pwnotice");
				pwnotice.innerText="密码长度在10-20位之间，由大小写英文字母和数字组成";
			}
			
			$("#register").click(function() {
				var school = $("#school").val();
				// alert(school);
				var studentid = $("input[name=studentid]").val();
				var password = $("input[name=password]").val();
				var confirmpassword = $("input[name=confirmpassword]").val();
				var identification = $("input[name=identification]").val();
				var mobile = $("input[name=mobile]").val();
				var name = $("input[name=name]").val();
				var code = $("input[name=code]").val();
				if (school == 0) {
					var notice = document.getElementById("notice");
					notice.innerText="*请选择学校！";
					return false;
				}
				if (studentid=="" || password=="" || name=="" || identification=="" || mobile=="" || confirmpassword=="") {
					var notice = document.getElementById("notice");
					notice.innerText="*请完整填写所有信息！";
					return false;
				}
				if (identification.length != 18) {
					var notice = document.getElementById("notice");
					notice.innerText="*身份证信息有误！";
					return false;
				}
				if (password != confirmpassword) {
					var notice = document.getElementById("notice");
					notice.innerText="*两次密码输入不一致！";
					return false;
				} else{
					var p = hex_md5(hex_md5(hex_md5(password)));
					$.ajax({
						type: "post",
						dataType: "json",
						url: "../mServletuser?op=register",
						contentType: "application/json;charset=utf-8",
						data: JSON.stringify({"school":school,"studentid":studentid,"password":p,"identification":identification,"mobile":mobile,"name":name,"code":code}),
						success: function(data) {
							// alert(data);
							// alert(typeof data);
							if (data == 0) {
								window.location.href="registersuccess.jsp";
								return ;
							} 
							if (data == -2) {
								var notice = document.getElementById("notice");
								notice.innerText="*系统异常！";
								return false;
							}
							if (data == -7) {
								var notice = document.getElementById("notice");
								notice.innerText="*验证码有误！";
								return false;
							}
							if (data == -10) {
								var notice = document.getElementById("notice");
								notice.innerText="*注册失败，学号，身份证号或者手机号已被使用！";
								return false;
							}
						}
					});
				}
			});
			
		</script>
		
	
		<div data-role="footer">
			<h4>Powered By TSpace</h4>
		</div>
	</div>

</body>
</html>