<%@page import="mail.client.ClientUtilities"%>
<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Quang Ngo">

<title>Login</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Code Javascript -->
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<%
		String message = (String) request.getAttribute("message");
		if (message == null) {
			message = (String) request.getParameter("message");
			if (message == null) {
				message = "";
			}
		}
		String userName = (String) session.getAttribute("userName1");
		String password = (String) session.getAttribute("password1");
		if (userName == null || password == null) {
			userName = "";
			password = "";
		}
		String serverPath = "";
		if (!Utilities.isSocketInited) {
			if (AllProcessing.initSocket()) {
				AllProcessing.inputPop.readLine();
				serverPath = AllProcessing.inputSmtp.readLine();
				Utilities.isSocketInited = true;
			} else {
				//FIXME
			}
		}
	%>
	<!-- <%=serverPath%> -->
	<div class="container-fluid">
		<div class="row">
			<img alt="ngoquang.com mail service" src="img/banner.png">
		</div>
		<div class="col-sm-6 col-sm-offset-3" style="margin-top: 20px;">
			<form action="LoginController" method="post">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<b style="font-size: 16px;">Login</b>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-8">
								<input class="form-control" type="text" name="userName" value="<%=userName %>"
									placeholder="Ex: abc123">
							</div>
							<div class="col-sm-4">@ngoquang.com</div>
						</div>
						<div class="row" style="margin: 30px auto">
							<input class="form-control" type="password" name="password"
								value="<%=password%>" placeholder="Password">
						</div>
						<div class="row" style="margin: 30px auto">
							<a href="OthersController?act=Register">Don't have an
								account? Register one</a>
						</div>
						<div class="row" style="margin: 10px">
							<div class="col-sm-2 col-sm-offset-8">
								<button class="btn btn-default btn-block" type="button"
									onClick="window.close();">Cancel</button>
							</div>
							<div class="col-sm-2">
								<button class="btn btn-primary btn-block" type="submit">Login</button>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<b style="color: red;"><%=message%></b>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>