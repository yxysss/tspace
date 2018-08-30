		$("#submit").on("tap", function() {  
				var username = $("#username").val();
				var password = $("#password").val();
				
				// alert(username + "," + password);		
				
				if (username == "") {
					$("#popup1").popup('open');
					return ;
				}
					
				if (password == "") {
					$("#popup2").popup('open');
					return ;
				}
					
				var p = hex_md5(hex_md5(hex_md5(password)));
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/mServletuser?op=login",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"username":username,"password":p}),
					success: function(data) {
						// alert(data);
						// alert(typeof data);
						if (data == 0) {
							// alert(document.referrer);
							var str = document.referrer;
							var n1 = str.length;
							var n2 = str.lastIndexOf("/");
							var s = str.substr(n2+1, n1-n2);
							if (str == "" || s == "" || s == "index.jsp") {
								// window.location.href 跳转时会刷新页面
								window.location.href="/tspace/User-mobile/index.jsp";
							} else {
								if (s == "mytspace.jsp") {
									window.location.href="/tspace/User-mobile/mytspace.jsp";
								} else {
									// window.history 返回上级页面时不会刷新
									window.history.go(-1);
								}
							}
							return ;
						}
						if (data == -2) {
							// 系统异常
							$("#popup3").popup('open');
							return ;
						}
						$("#popup4").popup('open');						
					}
				});  
			});
		$('forgetpassword').on('click', function() {
			window.location.href="/tspace/User-mobile/forgetpassword.jsp";
		});
			
		$('register').on('click', function() {
			window.location.href="/tspace/User-mobile/register.jsp";
		});