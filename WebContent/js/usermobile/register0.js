			function reload() {
				var pin = document.getElementById("pin");
				pin.src="/tspace/mServletuser?op=registerpin&d="+Math.random();
			}	
			function getcode() {
				var mobile = $("input[name=mobile]").val();
				if (mobile.length != 11) {
					var notice = document.getElementById("notice");
					notice.innerText="*请正确填写手机号！";
					return false;
				}
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
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/mServletuser?op=getSmsVerify",
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
			$("#nextstep").click(function() {
				var mobile = $("input[name=mobile]").val();
				var code = $("input[name=code]").val();
				if (mobile.length != 11) {
					var notice = document.getElementById("notice");
					notice.innerText="*手机号格式不正确！";
					return false;
				}
				if (code.length != 6) {
					var notice = document.getElementById("notice");
					notice.innerText="*验证码格式不正确！";
					return false;
				}
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/mServletuser?op=register0",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"mobile":mobile,"code":code}),
					success: function(data) {
						// alert(data);
						// alert(typeof data);
						if (data == 0) {
							window.location.href="/tspace/User-mobile/register1.jsp";
							return ;
						}
						if (data == -2) {
							location.reload();
							return ;
						}
						if (data == -7) {
							var notice = document.getElementById("notice");
							notice.innerText="*验证码有误！";
							return false;
						}
						if (data == -10) {
							var notice = document.getElementById("notice");
							notice.innerText="*注册手机号已被使用！";
							return false;
						}
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
					url: "/tspace/mServletuser?op=registerpinverify",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"inputpin":inputpin}),
					success: function(data) {
						// alert(data);
						// alert(typeof data);
						if (data == 0) {
							$("#smsblock").show();
							$("#nextstep").show();
							$("#pinblock").hide();
							$("#verify").hide();
							var notice = document.getElementById("notice");
							notice.innerText="";
							return ;
						} else {
							var notice = document.getElementById("notice");
							notice.innerText="*验证码有误！";
							return false;
						}
					}
				});
			});