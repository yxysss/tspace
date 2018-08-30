		function reload() {
			var pin = document.getElementById("pin");
			pin.src="/tspace/mServletuser?op=forgetpwpin&d="+Math.random();
		}
		
		function backindex() {
			window.location.href="/tspace/User-mobile/login.jsp";
		}
		
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
			var username = $("input[name=username]").val();
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/mServletuser?op=fpgetVerify",
				contentType: "application/json;charset=utf-8",
				data: JSON.stringify({"username":username}),
				success: function(data) {
					// alert(data);
					// alert(typeof data);
					if (data == 0) {
						// alert("短信发送成功！");
						$("#smssuccess").popup('open');
						return ;
					} else {
						// alert("短信发送失败！");
						$("#smsfailure").popup('open');
						return false;
					}
				}
			});
		}
		$("#submit").on("tap", function() {  
			var username = $("#username").val();
			var code = $("#code").val();
			var password = $("#password").val();
			var confirmpw = $("#confirmpw").val();
			
			// alert(username + "," + code + "," + password);		
			
			if (username == "") {
				$("#popup1").popup('open');
				return false;
			}
				
			if (code == "") {
				$("#popup2").popup('open');
				return false;
			}
			
			if (password == "") {
				$("#popup3").popup('open');
				return false;
			}
			
			if (password != confirmpw) {
				$("#popup4").popup('open');
				return false;
			}
				
			var p = hex_md5(hex_md5(hex_md5(password)));
			
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/mServletuser?op=forgetpassword",
				contentType: "application/json;charset=utf-8",
				data: JSON.stringify({"username":username,"code":code,"password":p}),
				success: function(data) {
					// alert(data);
					if (data == 0) {
						$("#popup5").popup('open');
						return ;
					} 
					if (data == -2) {
						$("#popup6").popup('open');
						$("#password").val("");
						return false;
					}
					$("#popup7").popup('open');
					$("#password").val("");
					return ;
				}
			});  
		});
		$("#verify").click(function() {
			var inputpin = $("input[name=inputpin]").val();
			if (inputpin.length != 4) {
				var notice = document.getElementById("notice");
				notice.innerText="*验证码格式不正确！";
				return false;
			}
			$.ajax({
				type: "post",
				dataType: "json",
				url: "/tspace/mServletuser?op=forgetpwpinverify",
				contentType: "application/json;charset=utf-8",
				data: JSON.stringify({"inputpin":inputpin}),
				success: function(data) {
					// alert(data);
					// alert(typeof data);
					if (data == 0) {
						$("#smsblock").show();
						$("#submit").show();
						$("#pinblock").hide();
						$("#verify").hide();
						var notice = document.getElementById("notice");
						notice.innerText="";
						return ;
					} 
					if (data == -6){
						location.reload();
					} else {
						var notice = document.getElementById("notice");
						notice.innerText="*验证码有误！";
						return false;
					}
				}
			});
		});