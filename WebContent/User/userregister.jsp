<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
	
	<div class="topblank">
		
	</div>
	
	<div class="container">
		<form action="" method="POST">
			<h2> 用户注册</h2>
			<div>
				<span style="margin:6px;color:red;float:left" id="notice"> </span>
				<br/>
			</div>
			<div>
				<input type="text" id="name" name="name" class="name" placeholder="姓名">
			</div><br/>
			<div>
				<!-- 只能输入数字 -->
				<input type="text" id="studentid" name="studentid" class="studentid" placeholder="学号">
			</div><br/>
			<div>
    			<input type="text" id="identification" name="identification" class="identification" placeholder="身份证">
    		</div>
    		<span style="float:left;color:red;margin:5px 5px 5px 5px;">
    			*请务必保证上述信息填写无误且均为本人信息
    		</span>
    		<div>
    			<input type="password" id="password" name="password" class="password" placeholder="密码">
    		</div><br/>
    		<div>
    			<input type="password" id="confirmpassword" name="confirmpassword" class="confirmpassword" placeholder="确认密码">
     		</div><br/>
    		<div>
    			<input type="text" id="mobile" name="mobile" class="mobile" placeholder="手机">
    		</div><br/>    			
			<div>
				<input type="button" id="register" class="btn btn-warning" value="注册">
			</div><br/>
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
		$('input#identification').keyup(function(){  // 只能输入数字
	        var c=$(this);  
	        if(/[^\d]/.test(c.val())){//替换非数字字符  
	          var temp_amount=c.val().replace(/[^\d]/g,'');  
	          $(this).val(temp_amount);  
	        }  
	     }); 
		$('input#mobile').keyup(function(){  // 只能输入数字
	        var c=$(this);  
	        if(/[^\d]/.test(c.val())){//替换非数字字符  
	          var temp_amount=c.val().replace(/[^\d]/g,'');  
	          $(this).val(temp_amount);  
	        }  
	     }); 
		$('input#name').keyup(function() {
			var c = $(this);
			var temp = c.val().replace(/[^\u4e00-\u9fa5]/g,'')
			if (temp != c) {
				$(this).val(temp);
			}
		});
	
	</script>
			
	<!-- jquery -->
	<!-- url: "loginsubmit?action=login&username="+username+"&password="+password, -->
	<script type="text/javascript">
		$("#register").click(function() {
			var studentid = $("input[name=studentid]").val();
			var password = $("input[name=password]").val();
			var confirmpassword = $("input[name=confirmpassword]").val();
			var identification = $("input[name=identification]").val();
			var mobile = $("input[name=mobile]").val();
			var name = $("input[name=name]").val();
			if (studentid=="" || password=="" || name=="" || identification=="" || mobile=="" || confirmpassword=="") {
				document.getElementById("notice").innerText="*请完整填写所有信息！";
				return false;
			}
			if (identification.length() != 14) {
				document.getElementById("notice").innerText="*身份证信息有误！";
				return false;
			}
			if (password != confirmpassword) {
				document.getElementById("notice").innerText="*两次密码输入不一致！";
				return false;
			} else{
				var p = hex_md5(hex_md5(hex_md5(password)));
				$.ajax({
					type: "post",
					dataType: "json",
					url: "../Servletuser?op=register",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"studentid":studentid,"password":p,"identification":identification,"mobile":mobile,"name":name}),
					success: function(data) {
						alert(data);
						alert(typeof data);
						if (data == true) {
							alert("注册成功！");
						} else {
							document.getElementById("notice").innerText="*注册失败，学号或身份证号已被使用！";
							return false;
						}
					}
				});
			}
		});
		
	</script>

</body>
</html>