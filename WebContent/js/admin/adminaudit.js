		function acceptapplication(idapplication, i) {
			
			var r = confirm("确认同意序号为"+i+"的申请吗？");
			if (r == true) {
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/Servletapplication?op=acceptapplication",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"idapplication":idapplication}),
					success: function(data) {
						
						// alert("data="+data);
						if (data == 0) {
							alert("申请审核成功");
							window.location.reload();
						}
					}
				});
			}
		}
		
		function decline(idapplication, i) {
			var r = confirm("确认拒绝序号为"+i+"的申请吗？");
			if (r == true) {
				$.ajax({
					type: "post",
					dataType: "json",
					url: "/tspace/Servletapplication?op=declineapplication",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify({"idapplication":idapplication}),
					success: function(data) {
						
						// alert("data="+data);
						if (data == 0) {
							alert("申请审核成功");
							window.location.reload();
						}
					}
				});
			}
		}	
		
		function conflict(idapplication) {
			window.location.href="/tspace/Administrator/adminaudit.jsp?id="+idapplication;
		}