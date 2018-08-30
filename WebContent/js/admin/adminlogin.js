		function reload() {
			
			var pin = document.getElementById("pin");
			pin.src="/tspace/Servletadmin?op=pin&d="+Math.random();
		}	
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
		$("#login").click(function() {
			var adminid = $("input[name=adminid]").val();
			var password = $("input[name=password]").val();
			var inputpin = $("input[name=inputpin]").val();
			if (adminid == "" || password == "" || inputpin == "") {
				alert("请完整填写信息");
				return false;
			} else{
				// var p = hex_md5(hex_md5(hex_md5(password)));
				var p = password;
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/Servletadmin?op=login",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"adminid":adminid,"password":p,"pin":inputpin}),
					success: function(data) {
						// alert(data);
						// alert(typeof data);
						if (data == true) {
							window.location.href="index.jsp";
						} else {
							alert("登录信息有误！");
							window.location.href="adminlogin.jsp";
							return false;
						}
					}
				});
			}
		});