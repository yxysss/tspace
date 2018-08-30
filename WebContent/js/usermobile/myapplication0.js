			var idapplication;
			function queryapplication0(qidapplication) {
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/Servletapplication?op=detail",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"idapplication":qidapplication}),
					success: function(data) {
						// alert("data="+data);
						if (data == -1) {
							return ;
						}
						if (data == -5) {
							return ;
						}
						if (data == -6) {
							return ;
						}
						if (data == -3) {
							return ;
						}
						if (data == -2) {
							$("#popupm1").popup('open');
							return ;
						}
						// alert(data);
						// cstate = data.state;
						// capplytime = data.applytime;
						// caudittime = data.audittime;
						// cpassword = data.password;
						var cstate = data.state;
						if (cstate == "accept") cstate="接受";
						if (cstate == "unsettle") cstate="待审核";
						if (cstate == "decline") cstate="拒绝";
						document.getElementById("cstate").innerHTML = cstate;
						document.getElementById("capplytime").innerHTML = data.applytime;
						if (data.audittime != null) {
							document.getElementById("caudittime").innerHTML = data.audittime;
						} else {
							document.getElementById("caudittime").innerHTML = "";
						}
						if (data.password != null) {
							document.getElementById("cpassword").innerHTML = data.password;
						} else {
							document.getElementById("cpassword").innerHTML = "";
						}
						$("#detail").popup('open');
						return ;
					}
				});
			}
			
			function commitcancel(cidapplication) {
				// alert(cidapplication);
				// alert(typeof(cidapplication));
				idapplication = cidapplication;
				$("#ccancel").popup('open');
				return ;
			}
			
			function submitcancel() {
				// alert(idapplication);
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/Servletapplication?op=cancel",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"idapplication":idapplication}),
					success: function(data) {
						// alert("data="+data);
						if (data == 0) {
							window.location.reload();
							return ;
						}
						if (data == -3) {
							$("cancelfail").popup('open');
							return ;
						}
					}
				});
			}
			function reloadwindow() {
				window.reload();
				return ;
			}
			var running0 = document.getElementById("running0");
			running0.setAttribute("class","ui-btn-active");
			var nav01 = document.getElementById("nav01");
			nav01.setAttribute("class","ui-btn-active");