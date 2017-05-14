<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%
	String userName = (String) session.getAttribute("userName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Quang Ngo">

<title>Home Page</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Code Javascript -->
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<img alt="ngoquang.com mail service" src="img/banner.png"
				align="middle">
		</div>
		<div class="col-sm-2">
			<div class="row" style="margin: 5px auto;">
				<b>Hi <%=userName%>!</b>
			</div>
			<hr>
			<div class="row" style="margin: 5px auto;">
				<a href="HomePageController?act=NewMail" target="_main">New</a>
			</div>
			<hr>
			<div class="row" style="margin: 5px auto;">
				<a href="HomePageController?act=Inbox" target="_main">Inbox</a>
			</div>
			<hr>
			<div class="row" style="margin: 5px auto;">
				<a href="HomePageController?act=Sent" target="_main">Sent</a>
			</div>
			<hr>
			<div class="row" style="margin: 5px auto;">
				<a href="OthersController?act=SignOut">Sign out</a>
			</div>
		</div>
		<div class="col-sm-10">
			<iframe src="HomePageController?act=Inbox" name="_main"
				style="border: none; width: 100%; height: 650px;"> </iframe>
		</div>
	</div>
</body>
</html>