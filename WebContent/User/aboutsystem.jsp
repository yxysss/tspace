<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body style="margin:10px"> 


	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
		
	<script type="text/javascript">
	
		$.ajax({
			type: "post",
			dataType: "json",
			url: "checklogin",
			contentType: "application/json;charset=utf-8",
			success: function(data) {		
				if (data == false) {
					top.location.href="login.jsp";
				}
			}
		});
	</script>
	
	<div>
		Transform空间管理系统帮助在校学生对教室，会议室，实验室等公共空间的申请与使用提供服务。
	</div>
		

</body>
</html>