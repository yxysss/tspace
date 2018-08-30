			function backindex() {
				window.location.href="/tspace/User-mobile/index.jsp";
				return ;
			}
			
			$("#submit").on("tap", function() {  
				var oldpassword = $("#oldpassword").val();
				var newpassword = $("#newpassword").val();
				var confirmnpw = $("#confirmnpw").val();
				
				/* alert(oldpassword + "," + oldpassword + "," + confirmnpw); */		
				
				if (oldpassword == "") {
					$("#popup1").popup('open');
					return false;
				}
					
				if (newpassword == "") {
					$("#popup2").popup('open');
					return false;
				}
				
				if (newpassword != confirmnpw) {
					$("#popup3").popup('open');
					return false;
				}
					
				var op = hex_md5(hex_md5(hex_md5(oldpassword)));
				var np = hex_md5(hex_md5(hex_md5(newpassword)));
				
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/mServletuser?op=changepassword",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"oldpassword":op,"newpassword":np}),
					success: function(data) {
						/* alert(data); */
						if (data == 0) {
							$("#popup4").popup('open');
							return ;
						} 
						if (data == -3) {
							$("#popup5").popup('open');
							$("#newpassword").val("");
							$("#confirmpw").val("");
							return false;
						}
						if (data == -2) {
							$("#popup6").popup('open');
							$("#newpassword").val("");
							$("#confirmpw").val("");
							return false;
						}
					}
				});  
			});