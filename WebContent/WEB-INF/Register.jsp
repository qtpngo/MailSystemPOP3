<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%
	String message = (String) request.getAttribute("message");
	if (message == null) {
		message = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Quang Ngo">

<title>Sign Up</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Code Javascript -->
<script src="js/bootstrap.min.js"></script>

<script type="text/javascript">
	function goBack() {
		window.history.back();
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<img alt="ngoquang.com mail service" src="img/banner.png">
		</div>
		<div class="col-sm-6 col-sm-offset-3" style="margin-top: 20px;">
			<div class="panel panel-danger">
				<div class="panel-heading">
					<b style="font-size: 16px;">Register</b>
				</div>
				<div class="panel-body">
					<form action="RegisterController" method="post">
						<div class="row">
							<div class="col-sm-8">
								<input class="form-control" type="text" name="userName"
									placeholder="Ex: abc123">
							</div>
							<div class="col-sm-4">@ngoquang.com</div>
						</div>
						<div class="row" style="margin: 20px auto">
							<input class="form-control" type="password" name="password"
								placeholder="Password">
						</div>
						<div class="row" style="margin: 20px auto">
							<input class="form-control" type="password" name="rePassword"
								placeholder="Retype Password">
						</div>
						<div class="row" style="margin: 10px auto">
							<div class="col-sm-2 col-sm-offset-7">
								<button class="btn btn-default btn-block" type="button"
									onFocus="goBack();">Cancel</button>
							</div>
							<div class="col-sm-3">
								<button class="btn btn-success btn-block" type="submit">Register</button>
							</div>
						</div>
					</form>
				</div>
				<div class="panel-footer">
					<b style="color: red;"><%=message%></b>
				</div>
			</div>
		</div>
	</div>
</body>
</html>