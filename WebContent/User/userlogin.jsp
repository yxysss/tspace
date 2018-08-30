<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<title>Transform空间管理系统</title>
<!-- css -->
<link rel="stylesheet" href="css/login/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>

	<!-- javascript -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/code.js"></script>
	
	<script>
		function reload() {
			
			var pin = document.getElementById("pin");
			pin.src="Servletuser?op=pin&d="+Math.random();
		}	
	</script>
	
	<div class="topblank">
		
	</div>
		
	<div class="container">
		<form action="" method="POST">
			<h2> 用户登录</h2>
			<div>
				<span style="margin:6px;color:red;float:left" id="notice"> </span>
				<br/>
			</div>
			<div>
				<input type="text" id="studentid" name="studentid" class="studentid" placeholder="学号"><br/>
			</div><br/>
			<div class="form-group">
    			<input type="password" id="password" name="password" class="password" placeholder="密码"><br/>
    		</div><br/>
    		<div class="form-group">
    			<input type="inputpin" id="inputpin" name="inputpin" class="inputpin" placeholder="验证码">
    			<br/><br/>
    			<img src="Servletuser?op=pin" id="pin" name="pin" class="pin">
    			<a id="getpin" style="cursor:pointer;" onclick="reload()">看不清楚？ 换一张</a>
    		</div>
    		<div>
    			<input type="button" id="login" class="btn btn-success" onkeypress="BindEnter();" value="登入">
			</div>
			<br/>
			<div>
				<a href="userforgetpassword.jsp" target="_blank">忘记密码？</a>
				<strong>|</strong>
				<a href="userregister.jsp" target="_blank">注册新账号</a>
			</div>
		</form>
	</div>
	
	<script>
		$('input#studentid').keyup(function(){  // 只能输入数字
	        var c=$(this);  
	        if(/[^\d]/.test(c.val())){//替换非数字字符  
	          var temp_amount=c.val().replace(/[^\d]/g,'');  
	          $(this).val(temp_amount);  
	        }  
	     }); 
		$('input#inputpin').keyup(function(){  // 只能输入数字和英文
			var c = $(this);
			var temp = c.val().replace(/[\W]/g,'');
			if (temp != c) {
				$(this).val(temp);
			}
		});
		document.onkeypress = function(event) {
			if (event.keyCode == 13) {
				$("#login").click();
			}
		}
	</script>
	
	<!-- jquery -->
	<!-- url: "loginsubmit?action=login&username="+username+"&password="+password, -->
	<script type="text/javascript">
		$("#login").click(function() {
			var studentid = $("input[name=studentid]").val();
			var password = $("input[name=password]").val();
			var inputpin = $("input[name=inputpin]").val();
			if (studentid == "" || password == "" || inputpin == "") {
				document.getElementById("notice").innerText="*请完整填写信息！";
				return false;
			} else{
				var p = hex_md5(hex_md5(hex_md5(password)));
				$.ajax({
					type: "post",
					dataType: "json",
					url: "Servletuser?op=login",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"studentid":studentid,"password":p,"pin":inputpin}),
					success: function(data) {
						alert(data);
						alert(typeof data);
						if (data == true) {
							window.location.href="index.jsp";
						} else {
							document.getElementById("notice").innerText="*输入信息有误！";
							return false;
						}
					}
				});
			}
		});
		
		$("#register").click(function() {
			window.open("userregister.jsp");
		})
		
	</script>

</body>
</html>