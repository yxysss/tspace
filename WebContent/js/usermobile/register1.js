			function pwblur() {
				var pwnotice = document.getElementById("pwnotice");
				pwnotice.innerText="";
			}
			
			function pwfocus() {
				var pwnotice = document.getElementById("pwnotice");
				pwnotice.innerText="密码长度在10-20位之间，请确保密码足够复杂";
			}
			
			$("#register").click(function() {
				var school = $("#school").val();
				// alert(school);
				var identity = $("input[name='identity']:checked").val();
				// alert(identity);
				var username = $("input[name=username]").val();
				var password = $("input[name=password]").val();
				var confirmpassword = $("input[name=confirmpassword]").val();
				var identification = $("input[name=identification]").val();
				var name = $("input[name=name]").val();
				if (school == 0) {
					var notice = document.getElementById("notice");
					notice.innerText="*请选择学校！";
					return false;
				}
				if (username=="" || password=="" || name=="" || identification=="" || confirmpassword=="") {
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
						url: "/tspace/mServletuser?op=register1",
						contentType: "application/json;charset=utf-8",
						data: JSON.stringify({"school":school,"identity":identity,"username":username,"password":p,"identification":identification,"name":name}),
						success: function(data) {
							// alert(data);
							// alert(typeof data);
							if (data == 0) {
								window.location.href="/tspace/User-mobile/registersuccess.jsp";
								return ;
							} 
							if (data == -2) {
								var notice = document.getElementById("notice");
								notice.innerText="*系统异常！";
								return false;
							}
							if (data == -10) {
								var notice = document.getElementById("notice");
								notice.innerText="*学号，身份证号或手机号已被使用！";
								return false;
							}
						}
					});
				}
			});