<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="TSPACE空间管理系统,登录">
<meta http-equiv="description" content="TSPACE空间管理系统登录">
<title>TSpace空间管理系统</title>
<!-- css -->
<link rel="stylesheet" href="/tspace/css/admin/adminlogin.css">
<link rel="stylesheet" href="/tspace/css/admin/bootstrap.min.css">
</head>
<body>

	<!-- javascript -->
	<script src="/tspace/js/admin/jquery.min.js" type="text/javascript"></script>
	<script src="/tspace/js/admin/bootstrap.min.js"></script>
	<script src="/tspace/js/admin/code.js"></script>
	
	<div class="topblank">
		
	</div>
		
	<div class="container">
		<form action="" method="POST">
			<h3 class="h3class">TSPACE空间管理系统登录</h3>
			<div>
				<input type="text" id="adminid" name="adminid" class="adminid" placeholder="用户名"><br/>
			</div><br/>
			<div class="form-group">
    			<input type="password" id="password" name="password" class="password" placeholder="密码"><br/>
    		</div><br/>
    		<div class="form-group">
    			<input type="inputpin" id="inputpin" name="inputpin" class="inputpin" placeholder="验证码">
    			<br/><br/>
    			<img src="/tspace/Servletadmin?op=pin" id="pin" name="pin" class="pin">
    			<a id="getpin" style="cursor:pointer;" onclick="reload()">看不清楚？ 换一张</a>
    		</div>
    		<div>
    			<input type="button" id="login" class="btn btn-success" onkeypress="BindEnter();" value="登入">
			</div>
			<br/>
			<!-- <div>
				<a href="#" target="_blank">忘记密码？</a>
				<strong>|</strong>
				<a href="#" target="_blank">注册新账号</a>
			</div> -->
		</form>
	</div>
	
	<script src="/tspace/js/admin/adminlogin.js">
		/*
		$('input#adminid').keyup(function(){  // 只能输入数字
	        var c=$(this);  
	        if(/[^\d]/.test(c.val())){//替换非数字字符  
	          var temp_amount=c.val().replace(/[^\d]/g,'');  
	          $(this).val(temp_amount);  
	        }  
	     }); 
		*/
	</script>
	
</body>
</html>