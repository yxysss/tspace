<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
	
	<div class="navclass">
		<ul class="nav nav-pills">
			<li><a class="aclass" id="checklogin0" href="userlogin.jsp" target="_blank">亲,请登录</a></li>
			<li><a class="aclass" id="checklogin1" href="userregister.jsp" target="_blank">免费注册</a>
			<li id="contactnav"><a class="aclass" href="appeal.jsp" target="_blank">联系客服</a></li> 
			<li id="myapplynav"><a class="aclass" href="applyquery.jsp" target="_blank">我的申请</a></li>
		</ul>
	</div>
	
	<script>
		$.ajax({
			type: "post",
			dataType: "json",
			url: "Servletuser?op=checklogin",
			contentType: "application/json;charset=utf-8",
			success: function(data) {
				alert(data);
				alert(typeof data);
				if (data != false && data != null) {
					var checklogin0 = document.getElementById("checklogin0");
					checklogin0.innerText = data + "  欢迎你!";
					checklogin0.removeAttribute('href');
					var checklogin1 = document.getElementById("checklogin1");
					checklogin1.innerText = "退出";
					checklogin1.onclick = function () {
						alert("安全退出！");
						$.ajax({
							type: "post",
							dataType: "json",
							url: "Servletuser?op=logout",
							contentType: "application/json;charset=utf-8",
							success: function(data) {
								if (data == true) {
									location.reload();
								}
							}
						});
					};
					checklogin1.removeAttribute('href');
				}
			}
		});
	</script>

</body>
</html>